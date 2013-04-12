package extractor.client;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

/**
 *
 * @author Jakub Starka
 */
public abstract class Extractor {
    
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    protected Integer limit;
    protected Integer offset;
    protected Integer step;
    protected Integer max_limit;
    
    protected String method;
    protected String url;
    protected String limit_name;
    protected String offset_name;
    protected String exec;
    protected String outputExec;
    protected boolean keepParts;
    protected String partName;
    
    protected int outputNumber = 1;
    
    public abstract void execute();
    
    protected File prepareFile(String name) throws IOException {
        
        File outputExecFile = new File(name);
            
        if (outputExecFile.exists()) {
            outputExecFile.delete();
        }
        outputExecFile.createNewFile();
        
        return outputExecFile;
    }
    
    protected void run(String exec, File output) throws IOException, InterruptedException {
        if (exec != null) {

            File outputExecFile = prepareFile(outputExec + outputNumber++);
            
            String run = exec;
            String line;
            if (output != null) {
                run = exec.replace("$1", output.getAbsolutePath());
            } 

            System.out.println("Running: " + run);

            Process p = Runtime.getRuntime().exec(run);

            BufferedReader bre;
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            
            FileWriter bw = new FileWriter(outputExecFile);
            
            while ((line = bri.readLine()) != null) {
                bw.write(line + LINE_SEPARATOR);
            }
            while ((line = bre.readLine()) != null) {
                bw.write(line + LINE_SEPARATOR);
            }
            bre.close();
            p.waitFor();
            bw.close();
            System.out.println("Done.");
        }
    }
    
    protected Ini pref = null;

    protected Extractor() {
        
        try {
            pref = new Ini(new File("settings.txt"));
            
            method = pref.get("source", "method");
            url = pref.get("source", "url");
            limit_name = pref.get("paging", "limit_name");
            offset_name = pref.get("paging", "offset_name");
            String offset_s = pref.get("paging", "offset");
            String step_s = pref.get("paging", "step");
            String max_limit_s = pref.get("paging", "max_limit");

            offset = Integer.parseInt(offset_s);
            step = Integer.parseInt(step_s);
            max_limit = Integer.parseInt(max_limit_s);

            exec = pref.get("system", "exec");
            outputExec = pref.get("system", "output");
            
            String keepParts_s = pref.get("system", "parts");
            partName = pref.get("system", "partName");
            
            if (keepParts_s != null && keepParts_s.equals("KEEP") && partName != null) {
                keepParts = true;
            }
        } catch (InvalidFileFormatException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    
    
}
