package com.example.springboot2.test;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CoderUtil {

    /**

     * MAC算法可选以下多种算法

     *

     * <pre>

     * HmacMD5

     * HmacSHA1

     * HmacSHA256

     * HmacSHA384

     * HmacSHA512

     * </pre>

     */

    public static final String KEY_MAC = "HmacMD5";

    public static final String MAC_NAME = "HmacMD5";



    /**

     * HMAC加密

     *

     * @param data

     * @param key

     * @return

     * @throws Exception

     */

    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {



        SecretKey secretKey = new SecretKeySpec(key.getBytes(), KEY_MAC);

        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

        mac.init(secretKey);



        return mac.doFinal(data);



    }



    /*byte数组转换为HexString*/

    public static String byteArrayToHexString(byte[] b) {

        StringBuffer sb = new StringBuffer(b.length * 2);

        for (int i = 0; i < b.length; i++) {

            int v = b[i] & 0xff;

            if (v < 16) {

                sb.append('0');

            }

            sb.append(Integer.toHexString(v));

        }

        return sb.toString();

    }

    public static String getSignature(String data, String key) throws Exception {

        byte[] keyBytes = key.getBytes();

        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, MAC_NAME);

        Mac mac = Mac.getInstance(MAC_NAME);

        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(data.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : rawHmac) {

            sb.append(byteToHexString(b));

        }

        return sb.toString();

    }



    private static String byteToHexString(byte ib) {

        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        char[] ob = new char[2];

        ob[0] = Digit[(ib >>> 4) & 0X0f];

        ob[1] = Digit[ib & 0X0F];

        return new String(ob);

    }


    public static String getSignature1(String data, String key) throws Exception {



        byte[] keyBytes = key.getBytes();

        // 根据给定的字节数组构造一个密钥。

        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, KEY_MAC);

        Mac mac = Mac.getInstance(KEY_MAC);

        mac.init(signingKey);



        byte[] rawHmac = mac.doFinal(data.getBytes());



        String hexBytes = byte2hex(rawHmac);

        return hexBytes;

    }



    private static String byte2hex(final byte[] b) {

        String hs = "";

        String stmp = "";

        for (int n = 0; n < b.length; n++) {

            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式。

            stmp = (java.lang.Integer.toHexString(b[n] & 0xFF));

            if (stmp.length() == 1) {

                hs = hs + "0" + stmp;

            } else {

                hs = hs + stmp;

            }

        }

        return hs;

    }

    /**
     * 生成签名数据
     *
     * @param data 待加密的数据
     */
    public static String getSignature(String data) throws Exception{
        byte[] key="Perfect365".getBytes();
        //byte[] key="myappsecret".getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(key, KEY_MAC);
        Mac mac = Mac.getInstance(KEY_MAC);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        StringBuilder sb=new StringBuilder();
        for(byte b:rawHmac){
            sb.append(byteToHexString(b));
        }
        //System.out.println("sb:"+sb.toString());
        return sb.toString();
    }

    public static void main(String[] args)throws Exception{
        //"campaign_title=bar&credits=100&user_id=foo"
        /*String inputStr = "{\"campaign_title\":\"Survey for Money:Earn Cash App\",\"credits\":2,\"user_id\":\"xiang\"}";

        byte[] inputData = inputStr.getBytes();

        String key = "Perfect365";

        System.out.println(CoderUtil.byteArrayToHexString(CoderUtil.encryptHMAC(inputData, key)));
*/

    /*String inputStr = "{\"campaign_title\":\"Survey for Money:Earn Cash App\",\"credits\":\"2\",\"user_id\":\"xiang\"}";

        System.out.println(CoderUtil.getSignature(inputStr, "Perfect365"));
    }*/

        String inputStr = "\"campaign_title\":\"Survey for Money:Earn Cash App\",\"credits\":\"2\",\"user_id\":\"xiang\"";
        //String inputStr = "campaign_title=Survey for Money:Earn Cash App&credits=2&user_id=xiang";

        System.out.println(CoderUtil.getSignature1(inputStr, "Perfect365"));
    }



}