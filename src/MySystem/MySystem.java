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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySystem {
    
    public static void main(String[] args) throws RedmineException, IOException {
        //Frame frm = new Frame();
       // frm.setVisible(true);
        
        ConnectionWithAPI connection = new ConnectionWithAPI(); 
        connection.checkAttachmentID(269976);
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
        
       Issue issue = connection.getIssueByID(730968);
       Collection <Journal> journals = issue.getJournals();
       for(Journal journal : journals) {
           System.out.println(journal.getUser());
           System.out.println(journal.toString());
       }
       //System.out.println(journals.toString());
       System.out.println(issue.getUpdatedOn());

    }   
}
