/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MySystem;

import com.taskadapter.redmineapi.AttachmentManager;
import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.CustomFieldDefinition;
import com.taskadapter.redmineapi.bean.CustomFieldFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssueCategoryFactory;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.Version;
import com.taskadapter.redmineapi.bean.VersionFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import static java.util.Arrays.stream;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

public class ConnectionWithAPI  {
    
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

    public ConnectionWithAPI () throws RedmineException {
        
    }

    public void saveAttachment (Issue issue) throws IOException {
            Collection<Attachment> issueAttachment = issue.getAttachments();
            ArrayList<Attachment> issueAttachments = new ArrayList<>(issueAttachment);
            File dir = new File(".\\myFiles\\");
            dir.mkdirs();
           
            for (Attachment attach : issueAttachments ) {
                
                String fileToManage = ".\\myFiles\\" +  attach.getFileName();
                downloadAttachments(attach.getContentURL(), 
                    apiAccessKey,
                    fileToManage);
                
                if (attach.getFileName().endsWith(".py")) {
                    startPylint(attach.getFileName());
                }
                
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
        issues = issueManager.getIssues(projectKey, queryId, Include.journals, Include.attachments );
        return issues;
    }
    
    public Issue getIssueByID(int issueID) throws RedmineException {
        Issue issue = issueManager.getIssueById(issueID, Include.journals);
        return issue;
    }
    
    public void uploadAttachment (Issue issue, String path) throws RedmineException, IOException {
        
        String filename = path;
        File file = new File(filename); 
        attachmentManager.addAttachmentToIssue(issue.getId(), file, ContentType.TEXT_PLAIN.getMimeType());
            
    }

    private void startPylint(String attachmentName) throws IOException {
        String attachName = attachmentName; 
        ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd \"C:\\Projects\\MySystem\\myFiles\" && pylint " + attachName + ">report.txt");
                builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
                
    }
    
}


