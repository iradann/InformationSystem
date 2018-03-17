/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MySystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author user
 */
class MyCheckStyle {

    public MyCheckStyle() {
    }
    
    
    public void startCheckStyle (String attachmentName) throws IOException {
        
        ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe", "/c", "cd \"C:\\Projects\\MySystem\\checkstyle\" && java -jar checkstyle-8.8-all.jar -c /sun_checks.xml \"C:\\Projects\\MySystem\\myFiles\" " 
                            + attachmentName 
                            + " > C:\\Projects\\MySystem\\myFiles\\" + attachmentName + ".txt");
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
