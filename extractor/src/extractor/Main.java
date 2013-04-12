/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import extractor.client.Extractor;
import extractor.client.HTTPGet;
import extractor.client.HTTPPost;
import extractor.client.FileCsv;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;

/**
 *
 * @author Jakub
 */
public class Main {

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Ini pref = new Ini(new File("settings.txt"));
            String method = pref.get("source", "method");

            Extractor extractor = null;

            if (method.equals("CSV")) {
                extractor = new FileCsv();
            } else if (method.equals("GET")) {
                extractor = new HTTPGet();
            } else if (method.equals("POST")) {
                extractor = new HTTPPost();
            }
            
            extractor.execute();
            
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}
