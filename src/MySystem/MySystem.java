/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MySystem;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Issue;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class MySystem {
    
    public static void main(String[] args) throws RedmineException, IOException {
        //Frame frm = new Frame();
       // frm.setVisible(true);
        
        ConnectionWithAPI connection = new ConnectionWithAPI(); 
        List<Issue> issues = connection.getIssues();
        for (Issue issue : issues) {
            if (issue.getStatusName() != "Closed") {
                Collection<Attachment> attach = issue.getAttachments();
                System.out.println(attach.toString());
                connection.saveAttachment(issue);
                System.out.println(issue.toString());
                
            }
        }
       
       connection.uploadAttachment(connection.getIssueByID(712686), "testingFile.txt");
    }   
}
