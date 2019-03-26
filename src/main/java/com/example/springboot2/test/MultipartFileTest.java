package com.example.springboot2.test;


/**
 * Created by zx9035 on 2018/5/30.
 */
public class MultipartFileTest {


    public static void main(String[] args) throws Exception {

        //File file = new File("D:\\01.jpg");
        //FileInputStream in_file = new FileInputStream(file);
        //MultipartFile multipartFile = new MockMultipartFile("01.jpg", in_file);

        /*CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://127.0.0.1:80/test/uploadImage");
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        FileInputStream fileInputStream = new FileInputStream("D:\\01.jpg");
        mEntityBuilder.addBinaryBody("imgData", fileInputStream);
        httpPost.setEntity(mEntityBuilder.build());
        CloseableHttpResponse response = null;
        response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);*/

        /*File file = new File("D:\\01.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://172.17.118.108:8080/test/uploadImage");
        ContentBody files = new ByteArrayBody(multipartFile.getBytes(), multipartFile.getName());
        *//*MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("imgData",files);//file为请求后台的Fileupload参数
        httpPost.setEntity(reqEntity);*//*
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        //multipartEntityBuilder.addBinaryBody("imgData", multipartFile.getBytes());
        multipartEntityBuilder.addPart("imgData",files);
        httpPost.setEntity(multipartEntityBuilder.build());
        CloseableHttpResponse response = httpclient.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        String resutt = EntityUtils.toString(response.getEntity());
        System.out.println(resutt);*/


    }

}
