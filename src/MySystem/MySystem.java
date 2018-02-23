/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MySystem;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.codec.Charsets;

public class MySystem {
    
    public static void main(String[] args) throws RedmineException, IOException {
        //Frame frm = new Frame();
       // frm.setVisible(true);
        
        ConnectionWithAPI connection = new ConnectionWithAPI(); 
        List<Issue> issues = connection.getIssues();
        ArrayList<Integer> attachID = new ArrayList<>();
        for (Issue issue : issues) {
            if (issue.getStatusName() != "Closed") {
               System.out.println(issue.toString());
               Collection<Attachment> attach = issue.getAttachments();
               System.out.println(attach.toString());
               connection.saveAttachment(issue);
               
            }
        }
        
        
       //connection.uploadAttachment(connection.getIssueByID(712686), "testingFile.txt");
      //connection.uploadAttachmentSonar(connection.getIssueByID(713276), "report.zip");
      
    }   
}
