package com.xiangyang.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	//十六进制下数字到字符的映射数组 
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"}; 
    private static final char[] hexChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
 
    /**把inputString加密*/ 
    public static String md5(String inputStr){ 
        return encodeByMD5(inputStr); 
    } 
 
    /**对字符串进行MD5编码*/ 
    private static String encodeByMD5(String originString){ 
        if (originString!=null) { 
            try { 
            	MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算 
                byte[] results = md5.digest(originString.getBytes()); 
                //将得到的字节数组变成字符串返回  
                String result = byteArrayToHexString(results); 
                return result; 
            } catch (Exception e) { 
                e.printStackTrace(); 
            } 
        } 
        return null; 
    } 
 
    /** 
    * 轮换字节数组为十六进制字符串 
    * @param b 字节数组 
    * @return 十六进制字符串 
    */ 
    private static String byteArrayToHexString(byte[] b){ 
        StringBuffer resultSb = new StringBuffer(); 
        for(int i=0;i<b.length;i++){ 
            resultSb.append(byteToHexString(b[i])); 
        } 
        return resultSb.toString(); 
    } 
 
    //将一个字节转化成十六进制形式的字符串 
    private static String byteToHexString(byte b){ 
        int n = b; 
        if(n<0) 
        n=256+n; 
        int d1 = n/16; 
        int d2 = n%16; 
        return hexDigits[d1] + hexDigits[d2]; 
    } 
    
    public static String encoderHmacSha256(String data, String key) {
        try {
            byte[] dataByte = data.getBytes();
            byte[] keyByte = key.getBytes();

            SecretKeySpec signingKey = new SecretKeySpec(keyByte, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return byte2hex(mac.doFinal(dataByte));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String byte2hex(byte[] b) {
        return byte2hex(b, '\u0000');
    }

    public static String byte2hex(byte[] b, char delimeter) {
        StringBuffer sb = new StringBuffer();

        for(int n = 0; n < b.length; ++n) {
            byte2hex(b[n], sb);
            if(n < b.length - 1 && delimeter != 0) {
                sb.append(delimeter);
            }
        }

        return sb.toString().toLowerCase();
    }

    private static void byte2hex(byte b, StringBuffer buf) {
        int high = (b & 240) >> 4;
        int low = b & 15;
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
}
