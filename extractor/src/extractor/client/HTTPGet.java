package extractor.client;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.ini4j.Profile.Section;

/**
 *
 * @author Jakub Starka
 */
public class HTTPGet extends Extractor {

    @Override
    public void execute() {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        Section paramSection = pref.get("params");
        
        while (offset + step < max_limit) {
            try {
                File output = null;

                String request = url + "?";

                for(Map.Entry<String, String> e: paramSection.entrySet()) {
                    request += e.getKey() + "=" + e.getValue() + "&";
                }

                request += limit_name + "=" + step + "&" + offset_name + "=" + offset;

                System.out.println("Requesting URL: " + request);

                HttpGet httpGet = new HttpGet(request);
                HttpResponse response = httpclient.execute(httpGet);
                File outputExecFile = new File(outputExec + outputNumber++);


                try {
                    System.out.println(response.getStatusLine());
                    HttpEntity entity1 = response.getEntity();

                    output = File.createTempFile("dump", "");

                    // read this file into InputStream
                    InputStream inputStream = entity1.getContent();

                    // write the inputStream to a FileOutputStream
                    OutputStream out = new FileOutputStream(output);

                    int read = 0;
                    byte[] bytes = new byte[1024];

                    while ((read = inputStream.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                    }

                    inputStream.close();
                    out.flush();
                    out.close();

                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity1);
                } finally {
                    httpGet.releaseConnection();
                }

                run(exec, output);

                offset += step;
            } catch (InterruptedException ex) {
                Logger.getLogger(HTTPGet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(HTTPGet.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
}
