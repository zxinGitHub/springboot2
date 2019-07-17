package com.example.springboot2.test;

import com.google.common.collect.Maps;
import com.sun.istack.internal.logging.Logger;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;

public class HttpPoolUtil {
    private static Logger logger = Logger.getLogger(HttpPoolUtil.class);


    private static CloseableHttpClient httpClient = null;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(20);
        //请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 1) {// 如果已经重试了1次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .build();
    }


    private static void config(HttpRequestBase httpRequestBase, int timeout) {
        httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        httpRequestBase.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpRequestBase.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");//"en-US,en;q=0.5");
        httpRequestBase.setHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        httpRequestBase.setConfig(requestConfig);
    }

    public static HttpResponse sendHttpRequest(String url, Map<String, Object> mapData, String method) {
        ;
        return sendHttpRequest(url, method, buildData(mapData), "UTF-8", 3000, 1, "formData");
    }

    public static HttpResponse sendHttpRequest(String url, Map<String, Object> mapData, String method, String contentType) {
        return sendHttpRequest(url, method, buildData(mapData), "UTF-8", 3000, 1, contentType);
    }

    public static HttpResponse sendHttpRequest(String url, Map<String, Object> mapData, String method, String contentType, int timeout) {
        return sendHttpRequest(url, method, buildData(mapData), "UTF-8", timeout, 1, contentType);
    }

    private static String buildData(Map<String, Object> mapData) {
        StringBuilder sb = new StringBuilder();
        if (MapUtils.isNotEmpty(mapData)) {
            for (Map.Entry<String, Object> e : mapData.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                try {
                    sb.append(e.getValue() == null ? "" : URLEncoder.encode(e.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                    logger.info("", e1);
                }
                sb.append("&");
            }
        }

        return sb.substring(0, sb.length() - 1);
    }

    public static HttpResponse sendPostRequest(String url, String data, Map<String,String> headers, int retryCount) {
        long startTime = System.currentTimeMillis();
        boolean isSuccess = false;
        String httpResponseTxt = "";
        int retry = 0;
        while (retry <= retryCount && !isSuccess) {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.get(key));
            }
            config(httpPost, 1000);
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                httpResponseTxt = EntityUtils.toString(entity, "UTF-8");

                //EntityUtils.consume(entity);

                if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    isSuccess = true;
                } else {
                    logger.info("http send response fail,response=" + httpResponseTxt);
                }
            } catch (IOException e) {
                logger.info("http send response fail,response=" + e);
                e.printStackTrace();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            retry++;
        }

        DefaultHttpResponse httpResponse = new DefaultHttpResponse();
        httpResponse.setSuccess(isSuccess);
        httpResponse.setResult(httpResponseTxt);
        logger.info("http cost time:url=" + url + ",time=" + (System.currentTimeMillis() - startTime));
        return httpResponse;
    }

    public static HttpResponse sendHttpRequest(String url, String data, String method) {
        return sendHttpRequest(url, method, data, "UTF-8", 3000, 1, "json");
    }

    public static HttpResponse sendHttpRequest(String url, String data) {
        return sendHttpRequest(url, "GET", data, "UTF-8", 3000, 1, "json");
    }

    public static HttpResponse sendHttpRequest(String url) {
        return sendHttpRequest(url, "GET", null, "UTF-8", 3000, 1, "json");
    }

    public static HttpResponse sendHttpRequest(String url, String method, String data, String characterSet, int timeout, int retryCount, String contentType) {
        long startTime = System.currentTimeMillis();
        Map<String, String> result = Maps.newHashMap();
        boolean isSuccess = false;
        String httpResponseTxt = "";
        int retry = 0;
        while (retry <= retryCount && !isSuccess) {
            try {
                CloseableHttpResponse response = null;
                try {
                    if ("POST".equalsIgnoreCase(method)) {
                        HttpPost postReq = new HttpPost(url);
                        if ("formData".equals(contentType)) {
                            StringEntity entity = new StringEntity(data, ContentType.create("application/x-www-form-urlencoded", characterSet));
                            postReq.setEntity(entity);
                        } else if ("json".equals(contentType)) {
                            StringEntity entity = new StringEntity(data, ContentType.create("application/json", characterSet));
                            postReq.setEntity(entity);
                        } else if ("xml".equals(contentType)) {
                            StringEntity entity = new StringEntity(data, ContentType.create("application/xml", characterSet));
                            postReq.setEntity(entity);
                        } else {
                            StringEntity entity = new StringEntity(data, characterSet);
                            postReq.setEntity(entity);
                        }
                        config(postReq, timeout);
                        response = httpClient.execute(postReq, HttpClientContext.create());
                    } else {
                        HttpGet getReq = null;
                        if (StringUtils.isBlank(data)) {
                            getReq = new HttpGet(url);
                        } else {
                            getReq = new HttpGet(url + "?" + data);
                        }

                        config(getReq, timeout);
                        response = httpClient.execute(getReq, HttpClientContext.create());
                    }

                    HttpEntity entity = response.getEntity();
                    if (characterSet != null) {
                        httpResponseTxt = EntityUtils.toString(entity, characterSet);
                    } else {
                        httpResponseTxt = EntityUtils.toString(entity);
                    }
                    EntityUtils.consume(entity);

                    if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        isSuccess = true;
                    } else {
                        logger.info("http send response fail,response=" + httpResponseTxt);
                    }

                } catch (IOException e) {
                    logger.info("http send io error:{}", e);
                } finally {
                    try {
                        if (response != null)
                            response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                logger.info("http send error:{}", e);
            }
            retry++;
        }


        DefaultHttpResponse httpResponse = new DefaultHttpResponse();

        httpResponse.setSuccess(isSuccess);
        httpResponse.setResult(httpResponseTxt);
        logger.info("http cost time:url=" + url + ",time=" + (System.currentTimeMillis() - startTime));
        return httpResponse;
    }

    public interface HttpResponse {
        public String getResult();

        public boolean isSuccess();
    }

    public static class DefaultHttpResponse implements HttpResponse {


        private boolean success;
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        @Override
        public String toString() {
            return "DefaultHttpResponse{" +
                    "result='" + result + '\'' +
                    ", success=" + success +
                    '}';
        }
    }

    public static void main(String[] args) {
        Map map = Maps.newHashMap();
        map.put("method", "check_token");
        map.put("userid", "1");
        map.put("token", "4B3BD61B-5508-40CA-8FCD-1967C9C3A7A01");


        System.out.println(HttpPoolUtil.sendHttpRequest("http://testservice.perfect365.com/api/iphone/user.json", map, "get"));
    }

}
