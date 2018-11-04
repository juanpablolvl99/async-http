/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazuli.async.http.util;

import java.util.Map;

/**
 *
 * @author kuuhaku
 */
public class ParametersUtil {
    
    public static String stringfly(Map<String, String> parameters) {
        if (parameters.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        parameters.entrySet().stream()
                .forEach(entry -> {
                    builder.append("&")
                           .append(entry.getKey())
                           .append("=")
                           .append(entry.getValue());
                });
        builder.replace(0, 1, "");
        return builder.toString();
    }
    
}
