package com.person.http;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

/**
 * Created by huangchangling on 2018/9/5.
 * post请求的封装
 */
public class HttpUtil {

    private static volatile HttpUtil instance;

    private static HttpClient httpClient = new HttpClient();

    /**连接超时时间，毫秒*/
    public static final int CONNECTION_TIME_OUT = 2000;
    /**响应超时时间，毫秒*/
    public static final int SOCKET_TIME_OUT = 3000;

    private HttpUtil(){}

    public static HttpUtil getInstance(){
        if (instance == null) {
            synchronized (HttpUtil.class){
                if (instance == null) {
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public String post(String requestUrl,Map<String,String> requestParams) throws IOException {
        String response = null;
        PostMethod postMethod = null;
        //设置超时时间
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIME_OUT);
        //设置超时响应时间
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(SOCKET_TIME_OUT);
        postMethod = new PostMethod(requestUrl);
        if (requestParams != null && !requestParams.isEmpty()) {
            for(Map.Entry<String,String> entry: requestParams.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    postMethod.addParameter(entry.getKey(),entry.getValue());
                }
            }
        }
        int status = httpClient.executeMethod(postMethod);
        if ( status == HttpStatus.SC_OK) {
            response = postMethod.getResponseBodyAsString();
        }
        return response;
    }

    public static void main(String[] args) throws IOException {
        String requestUrl = "http://localhost:8082/mvc/page/login.do";
        String res = getInstance().post(requestUrl,null);
        System.out.println(res);
    }
}
