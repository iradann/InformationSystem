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
        

        ConnectionWithAPI connection = new ConnectionWithAPI();
        List<Issue> issues = connection.getIssues();
        ArrayList<Integer> attachID = new ArrayList<>();
        System.out.println("From which target version you need to get issues, type:");
        Scanner in = new Scanner(System.in, "Windows-1251");
        String inputTargetVersion = in.nextLine();

        for (Issue issue : issues) {
            if (issue.getStatusName() != "Closed" && issue.getStatusName() != "Approved") {
                System.out.println(issue.toString());
                connection.setVersionForCheck(inputTargetVersion, issue);
                System.out.println(issue.getAuthorName());
            }
        }

    }
}
