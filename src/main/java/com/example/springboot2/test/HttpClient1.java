package com.example.springboot2.test;

/*import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;*/

import com.alibaba.druid.support.json.JSONUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tomcat.util.security.MD5Encoder;
import sun.security.provider.MD5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient1 {

    public static void main(String[] args) throws Exception {
        /*CloseableHttpClient httpclient = HttpClients.createDefault();//创建 CloseableHttpClient

        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("businessName", "yidong_sd"));
        list.add(new BasicNameValuePair("gsdmList", "2168301,"));
        list.add(new BasicNameValuePair("sign", "1fcd8687936ee669c0ccc29b036592a3"));
        String params = EntityUtils.toString(new UrlEncodedFormEntity(list,Consts.UTF_8));
        HttpGet httpGet = new HttpGet("http://testapi.139erp.com/testApi/yidong/getGsdmRelationList?" + params);


        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);//返回请求执行结果
            int statusCode = response.getStatusLine().getStatusCode();//获取返回的状态值
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("c出错了");
            } else {
                String result = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        HttpClient client = new HttpClient();
        String url = "http://testapi.139erp.com/testApi/"+"yidong/getGsdmRelationList11";
        //	PostMethod post = new PostMethod(url);
        GetMethod get = new GetMethod("http://testapi.139erp.com/testApi/yidong/getGsdmRelationList");
        Map<String, String> reqMap = new HashMap<String, String>();
        reqMap.put("gsdmList", "2168301,");
        reqMap.put("businessName", "yidong_sd");
        String sign_md5 = "1fcd8687936ee669c0ccc29b036592a3";
        reqMap.put("sign", sign_md5);//（getGsdmRelationList+gsdmList+秘钥）的md5
        get.setQueryString(reqMap.toString());
        client.executeMethod(get);
        System.out.println(get.getResponseBody());

    }
}
