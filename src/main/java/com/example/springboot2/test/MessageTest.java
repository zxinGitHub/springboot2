package com.example.springboot2.test;

import java.util.HashMap;
import java.util.Map;

public class MessageTest {
    public static void main(String[] args) {

        Map<String, String> headers = new HashMap<>(2);
        String message = "{ \n" +
                "  \"data\": {\n" +
                "    \"link\": \"p365launch://TryItPage?tryLive=0&style=7411\",\n" +
                "    \"message\": \"notifycationMessage\"\n" +
                "  },\n" +
                "  \"to\" : \"8_4Y:APA91bFxHk7LqdbGt_iZc4VD3C0GT17NOWf9FgjEL7qBomOZuH1O9DYtt8124SMafNBFTiwj6kBJUOtnpKtjDrTIhTPZR0EIELJIhwgy9NbuaJr7uRzeZXZ7FiJ30sFRjOSEvqPieikW\"\n" +
                "}";
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "key=AIzaSyCSE1ibf2o6UzMt6hrKbUBtiFCR3_C4A-w");
        HttpPoolUtil.DefaultHttpResponse httpResponse = (HttpPoolUtil.DefaultHttpResponse) HttpPoolUtil.sendPostRequest("https://fcm.googleapis.com/fcm/send", message, headers, 3);

        System.out.println(httpResponse.isSuccess());


    }


}
