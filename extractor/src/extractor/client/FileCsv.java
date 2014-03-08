package extractor.client;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jakub Starka
 */
public class FileCsv extends Extractor {
    
    
    
    @Override
    public void execute() {
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(url));
            String header = br.readLine();
            System.out.println("CSV split started");
            while (offset + step <= max_limit) {

                File output = null;
                    output = prepareFile(partName + outputNumber);
                if (keepParts) {
                    
                } else {
                    output = File.createTempFile("dump", "");
                }
                

                FileWriter bw = new FileWriter(output);


                String line = null;
                int stepLine = 0;
                bw.write(header + LINE_SEPARATOR);
                while ( stepLine < step && (line = br.readLine()) != null) {
                    bw.write(line + LINE_SEPARATOR);
                    stepLine ++;
                }
                bw.close();

                run(exec, output);

                offset += step;
                System.out.println("CSV lines processed: " + offset);

                if (line == null) {
                    System.out.println("Reached end of file, ending");
                    break;
                }
            }
            br.close();
            System.out.println("CSV split finished");
        } catch (InterruptedException ex) {
            Logger.getLogger(FileCsv.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCsv.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
