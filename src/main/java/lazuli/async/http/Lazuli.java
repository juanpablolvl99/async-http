/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazuli.async.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 *
 * @author kuuhaku
 */
public class Lazuli {

    private CompletableFuture async;

    public Lazuli() {
    }

    public void request(String method, String url, Map<String, String> params, Map<String, String> headers, Consumer callback) {
        Map.Entry<String, byte[]> entry = null;
        try {
            entry = HttpMethod.valueOf(method.toUpperCase()).handleData(url, params);
        } catch (NullPointerException ex) {
            throw new RuntimeException("you need to pass a valid method");
        }
        // final url string
        String str = entry.getKey();
        // parameters byte[]
        byte[] data = entry.getValue();
        //
        this.async = CompletableFuture.supplyAsync(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(str).openConnection();
                connection.setRequestMethod(method);
                // adding the headers <key, value> pairs to the request properties 
                headers.forEach((k, v) -> {
                    connection.setRequestProperty(k, v);
                });
                //
                if (data != null) {
                    connection.setDoOutput(true);
                    OutputStream otp = connection.getOutputStream();
                    otp.write(data);
                    // forcing data to be outputed and close the stream
                    otp.flush();
                    otp.close();
                }
                // reading the url inputstream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                connection.disconnect();
                return builder.toString();
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }).thenAccept(callback); // after the suplly work is done, the callback accept method is called
    }

    //force the async supply complete with a given parameter
    public void cancel() {
        if (async == null) {
            return;
        }
        if (!async.isDone()) {
            async.complete(new Object());
        }
    }
}
