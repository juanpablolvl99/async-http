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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lazuli.async.http.util.ParametersUtil;

/**
 *
 * @author kuuhaku
 */
public class Lazuli {

    private HttpURLConnection connection;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private CompletableFuture async;

    public Lazuli() {
        headers = new HashMap<>();
        parameters = new HashMap<>();
    }

    public void setConnectionTimeout(int ms) {
        this.connection.setConnectTimeout(ms);
    }

    public void setReadTimeout(int ms) {
        this.connection.setReadTimeout(ms);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    // do a GET request
    public void get(String url, Consumer callback) throws MalformedURLException {
        if (!this.parameters.isEmpty()) {
            url = new StringBuilder()
                    .append(url)
                    .append("?")
                    .append(ParametersUtil.stringfly(parameters))
                    .toString();
        }
        request(new URL(url), null, callback);
    }

    // do a POST request
    public void post(String url, Consumer callback) throws MalformedURLException {
        byte[] data = null;
        if (!parameters.isEmpty()) {
            String strParam = ParametersUtil.stringfly(parameters);
            try {
                data = strParam.getBytes("UTF-8");
            } catch (UnsupportedEncodingException ex) {
                System.out.println(ex.getMessage());
                data = strParam.getBytes();
            }
        }
        request(new URL(url), data, callback);
    }

    private void request(URL url, byte[] data, Consumer callback) {
        this.async = CompletableFuture.supplyAsync(() -> {
            try {
                this.connection = (HttpURLConnection) url.openConnection();
                // adding the headers <key, value> pairs to the request properties 
                this.headers.entrySet().stream().forEach(entry -> {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                });
                if (data != null) {
                    connection.setDoOutput(true);
                    OutputStream otpStream = connection.getOutputStream();
                    otpStream.write(data);
                    // forcing data to be outputed and close the stream
                    otpStream.flush();
                    otpStream.close();
                }
                // reading the url inputstream
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                return builder.toString();
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }); // after the suplly work is done, the callback accept method is called
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
