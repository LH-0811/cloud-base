package com.cloud.base.core.common.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;


/**
 * 微信使用加解密工具（JAVA版本）
 */
public class EncDecHelper {


    /**
     * sha1加密
     *
     * @param inStr
     * @return
     * @throws Exception
     */
    public static String sha1Encode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes("UTF-8");
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    /**
     * 微信小程序 开放数据解密
     * AES解密（Base64）
     *
     * @param encryptedData 已加密的数据
     * @param sessionKey    解密密钥
     * @param iv            IV偏移量
     * @return
     * @throws Exception
     */
    public static String decryptForWeChatApplet(String encryptedData, String sessionKey, String iv) throws Exception {
        byte[] decryptBytes = Base64.decodeBase64(encryptedData);
        byte[] keyBytes = Base64.decodeBase64(sessionKey);
        byte[] ivBytes = Base64.decodeBase64(iv);

        return new String(decryptByAesBytes(decryptBytes, keyBytes, ivBytes));
    }

    /**
     * AES解密
     *
     * @param decryptedBytes 待解密的字节数组
     * @param keyBytes       解密密钥字节数组
     * @param ivBytes        IV初始化向量字节数组
     * @return
     * @throws Exception
     */
    private static byte[] decryptByAesBytes(byte[] decryptedBytes, byte[] keyBytes, byte[] ivBytes) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] outputBytes = cipher.doFinal(decryptedBytes);
        ;
        return outputBytes;
    }


//    public static void main(String[] args) throws Exception {

//        String data = "{\"nickName\":\"Band\",\"gender\":1,\"language\":\"zh_CN\",\"city\":\"Guangzhou\",\"province\":\"Guangdong\",\"country\":\"CN\",\"avatarUrl\":\"http://wx.qlogo.cn/mmopen/vi_32/1vZvI39NWFQ9XM4LtQpFrQJ1xlgZxx3w7bQxKARol6503Iuswjjn6nIGBiaycAjAtpujxyzYsrztuuICqIM5ibXQ/0\"}HyVFkGl5F5OQWJZZaNzBBg==";


//        System.out.println(sha1Encode(data));
//        String encryptedData =
//                "tsyLVebikY1aLQ0aNpg10NHxCTV2Ar+FJHUZdwIchBXFbJU7hXyf5gbDibaLU+lT6bzzut/nVymRFp/U8MrF0c8yOCFbnK5aevyearR7vopeel2y929weVA/s16shDPnRMkIn9xiMfVY3LDmuptnBpy1loZfSW2CPfXFuKXQf2z+Kiruynve1cq2mnzAadNaw3/g/tjHRPzxBnTkMsu8sQ==";
//        //会话密钥
//        String sessionKey = "vBwBswWRVmD0WQvRbdbMZg==";
//        //解密算法初始向量
//        String iv = "8IzE0WUF0j5hXy4oIKuLHA==";
//
//
//        try {
//            String result = EncDecHelper.decryptForWeChatApplet(encryptedData, sessionKey, iv);
//            System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
