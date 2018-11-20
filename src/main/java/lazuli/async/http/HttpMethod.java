/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lazuli.async.http;

import java.io.UnsupportedEncodingException;
import java.util.AbstractMap;
import java.util.Map;
import lazuli.async.http.util.ParametersUtil;

/**
 *
 * @author kuuhaku
 */
public enum HttpMethod {

    GET {
        @Override
        public Map.Entry<String, byte[]> handleData(String url, Map<String, String> params) {
            if (!params.isEmpty()) {
                url = new StringBuilder()
                        .append(url)
                        .append("?")
                        .append(ParametersUtil.stringfly(params))
                        .toString();
            }
            return new AbstractMap.SimpleEntry<>(url, null);
        }
    },
    DELETE {
        @Override
        public Map.Entry<String, byte[]> handleData(String url, Map<String, String> params) {
            return new AbstractMap.SimpleEntry<>(url, null);
        }
    },
    POST {
        @Override
        public Map.Entry<String, byte[]> handleData(String url, Map<String, String> params) {
            byte[] data = null;
            if (!params.isEmpty()) {
                String strParam = ParametersUtil.stringfly(params);
                try {
                    data = strParam.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    System.out.println(ex.getMessage());
                    data = strParam.getBytes();
                }
            }
            return new AbstractMap.SimpleEntry<>(url, data);
        }
    },
    PUT {
        @Override
        public Map.Entry<String, byte[]> handleData(String url, Map<String, String> params) {
            byte[] data = null;
            if (!params.isEmpty()) {
                String strParam = ParametersUtil.stringfly(params);
                try {
                    data = strParam.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    System.out.println(ex.getMessage());
                    data = strParam.getBytes();
                }
            }
            return new AbstractMap.SimpleEntry<>(url, data);
        }
    };

    public abstract Map.Entry<String, byte[]> handleData(String url, Map<String, String> params);

}
