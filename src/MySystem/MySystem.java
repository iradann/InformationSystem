/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MySystem;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.Version;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import static jdk.nashorn.tools.ShellFunctions.input;

public class MySystem {
    
    public static void main(String[] args) throws RedmineException, IOException {
        //Frame frm = new Frame();
       // frm.setVisible(true);
        
        ConnectionWithAPI connection = new ConnectionWithAPI(); 
        List<Issue> issues = connection.getIssues();
        ArrayList<Integer> attachID = new ArrayList<>();
        //System.out.println("From which target version you need to get issue, type:");
        //Scanner in = new Scanner(System.in);
        //String sometext = in.nextLine();
        
        for (Issue issue : issues) {
             if (issue.getStatusName() != "Closed" ) {
               //Collection<Attachment> attach = issue.getAttachments();
               //System.out.println(attach.toString());
               //connection.saveAttachment(issue);
              
               Collection<Attachment> attach = issue.getAttachments();
               Version version = issue.getTargetVersion();
               System.out.println(issue.toString());
               System.out.println(version);
               if (version  == null) {
                       System.out.println("net nichego");
               } else if (version.getName().equals("Контрольная работа")) {
                            connection.saveAttachment(issue);
                       };
                
               
            } 
        }
        
    }   
}
