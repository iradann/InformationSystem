/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MySystem;

import com.taskadapter.redmineapi.AttachmentManager;
import com.taskadapter.redmineapi.CustomFieldManager;
import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.CustomFieldDefinition;
import com.taskadapter.redmineapi.bean.Issue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.Charsets;
import org.apache.http.entity.ContentType;

public class ConnectionWithAPI {

    /* Если перенести инициализацию этих переменных в MySystem и задавать как
        connection.uri = "...";
        то программа не компилируется
     */
    String uri = "http://www.hostedredmine.com";
    String apiAccessKey = "f4b2a2953034f344cb45c08359ee76577dc20977";
    String projectKey = "1a1a";
    Integer queryId = null;

    private RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
    private IssueManager issueManager = mgr.getIssueManager();
    private AttachmentManager attachmentManager = mgr.getAttachmentManager();
    private List<Issue> issues;
    
    
    public ConnectionWithAPI() throws RedmineException {

    }

    public void saveAttachment(Issue issue) throws IOException {
        Collection<Attachment> issueAttachment = issue.getAttachments();
        ArrayList<Attachment> issueAttachments = new ArrayList<>(issueAttachment);
        File dir = new File(".\\myFiles\\");
        dir.mkdirs();

        for (Attachment attach : issueAttachments) {

            if (attach.getFileName().endsWith(".py") || attach.getFileName().endsWith(".java")) {

                if (checkAttachmentID(attach.getId()) == 0) {

                    String fileToManage = ".\\myFiles\\" + attach.getFileName();
                    downloadAttachments(attach.getContentURL(),
                            apiAccessKey,
                            fileToManage);

                    if (attach.getFileName().endsWith(".py")) {
                        new MyPLint().startPylint(attach.getFileName());
                        this.uploadAttachment(issue, ".\\myFiles\\" + attach.getFileName() + ".txt");
                        String result = readLastLineInFile(".\\myFiles\\" + attach.getFileName() + ".txt");
                        issue.setNotes(result);
                        this.updateIssue(issue);
                        //вывод финального статуса в журнал от имени текущего аккаунта

                    }

                    if (attach.getFileName().endsWith(".java")) {
                        new MyCheckStyle().startCheckStyle(attach.getFileName());
                        this.uploadAttachment(issue, "C:\\Projects\\MySystem\\myFiles\\" + attach.getFileName() + ".txt");
                    }
                    //cleanDirectory(new File(".\\myFiles\\"));
                }
            } else {
                continue;
            }

        }
    } 
public void saveAttachment2(Issue issue) throws IOException {
        Collection<Attachment> issueAttachment = issue.getAttachments();
        ArrayList<Attachment> issueAttachments = new ArrayList<>(issueAttachment);
        File dir = new File(".\\myFiles\\");
        dir.mkdirs();

        for (Attachment attach : issueAttachments) {
            if (attach.getFileName().endsWith(".java") || attach.getFileName().endsWith(".py") ) {
                    String fileToManage = ".\\myFiles\\" + attach.getFileName();
                    downloadAttachments(attach.getContentURL(),
                            apiAccessKey,
                            fileToManage);
            } else continue;
        }
    }
    private void downloadAttachments(String url, String apikey, String fileName) throws MalformedURLException, IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-redmine-api-key", apikey)
                .addHeader("cache-control", "no-cache") //не обязательно
                .build();

        Response response = client.newCall(request).execute();

        try (InputStream in = response.body().byteStream()) {
            Path to = Paths.get(fileName); //convert from String to Path
            Files.copy(in, to, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    public List<Issue> getIssues() throws RedmineException {
        issues = issueManager.getIssues(projectKey, queryId, Include.journals, Include.attachments, Include.changesets);
        return issues;
    }

    public Issue getIssueByID(int issueID) throws RedmineException {
        Issue issue = issueManager.getIssueById(issueID, Include.journals);
        return issue;
    }
   

    public void uploadAttachment(Issue issue, String path) {

        try {
            String filename = path;
            File file = new File(filename);
            attachmentManager.addAttachmentToIssue(issue.getId(), file, ContentType.TEXT_PLAIN.getMimeType());
        } catch (RedmineException ex) {
            Logger.getLogger(ConnectionWithAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionWithAPI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //срабатывает, но неверно загружает поток данных на сайт -- не как .zip файл
    public void uploadAttachmentSonar(Issue issue, String path) throws RedmineException, IOException {

        String filename = path;
        File file = new File(filename);
        // не работает пока что...
        attachmentManager.addAttachmentToIssue(issue.getId(), file, ContentType.create("application/zip").getMimeType());

    }

    public int checkAttachmentID(Integer id) throws IOException {
        List<String> attachmentIDs = new ArrayList<String>();
        attachmentIDs = Files.readAllLines(Paths.get("AttachmentID.txt"), Charsets.UTF_8);
        int response = 0;
        int attachWasCheckedBefore = 1;
        int attachIsNew = 0;

        for (String attach : attachmentIDs) {
            int idFromFile = Integer.parseInt(attach);
            if (idFromFile == id) {
                response = attachWasCheckedBefore; //да, уже проверяли этот аттач
                break;
            } else {
                response = attachIsNew; //нет, не проверяли
            }
        }
        if (response == attachIsNew) {
            String fromIntToString = Integer.toString(id) + "\r\n";
            Files.write(Paths.get("AttachmentID.txt"), fromIntToString.getBytes(), StandardOpenOption.APPEND); //занести id в файл

        }
        return response;
    }

    private void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    private void cleanDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }
    
    private String readLastLineInFile(String filename) {
        return "To do read result from filename";
    }
    
    public void updateIssue(Issue issue) {
        try {
            issueManager.update(issue);
        } catch (RedmineException ex) {
            Logger.getLogger(ConnectionWithAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
