package com.ky.logic.utils;

import com.ky.logic.common.cache.SystemCache;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * Created by yl on 2017/7/6.
 */
public enum AesUtil {
    INSTANCE;

    /**
     * 算法模式 AES
     */
    private String MODE = "AES";


    /**
     * 根密钥key
     */
    private String root_key = "0123456789012345";

    /**
     * 密钥首段
     */
    private String str = "1233EE2C2880986304439A3122161300";

    /**
     * 加密解密
     */
    private Cipher cipher;

    private AesUtil() {
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized String getKey() throws Exception {
        String str1 = rootDecrypt(str);
        String str2 = getT();
        String str3 = getF();
        return str1 + str2 + str3;
    }

    private String getT(){
        String str = String.valueOf(System.currentTimeMillis());
        str = str.substring(0,6);
        return str;
    }

    private String getF(){
        String str = SystemCache.getInstance().getString("aes.key");
        str = str.substring(0,8);
        return str;
    }

    /**
     * 加密
     *
     * @param txt 待解密的内容
     * @return 明文
     */
    public synchronized String encrypt(String txt) throws Exception {
        String key = getKey();
        return encrypt2hex(key, txt);
    }


    public synchronized String encrypt2hex(String key, String txt) throws Exception{
        byte[] b = encrypt2Bytes(key, txt);
        return hexEncode(b);
    }

    public synchronized String encrypt2b64(String key, String txt) throws Exception{
        byte[] b = encrypt2Bytes(key, txt);
        return base64Encode(b);
    }

    /**
     * 解密
     *
     * @param txt 待解密的内容
     * @return 明文
     */
    public synchronized String decrypt(String txt) throws Exception {
        String key = getKey();
        return decrypt2hex(key, txt);
    }


    public synchronized String decrypt2hex(String key, String txt) throws Exception {
        byte[] b = hexDecode(txt);
        return new String(decryptByBytes(key, b),"utf-8");
    }

    public synchronized String decrypt2b64(String key, String txt) throws Exception {
        byte[] b = base64Decode(txt);
        return new String(decryptByBytes(key, b),"utf-8");
    }


    private String rootEncrypt(String txt) throws Exception {
        return encrypt2hex(root_key, txt);
    }

    private String rootDecrypt(String txt) throws Exception {
        return decrypt2hex(root_key, txt);
    }

    private byte[] encrypt2Bytes(String key, String txt) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), MODE));
        return cipher.doFinal(txt.getBytes("utf-8"));
    }


    private byte[] decryptByBytes(String key, byte[] bytes) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("utf-8"), MODE));
        return cipher.doFinal(bytes);
    }


    // byte 2 string
    private String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    // string 2 byte
    private byte[] base64Decode(String content) throws Exception{
        return new BASE64Decoder().decodeBuffer(content);
    }


    // byte 2 string
    private String hexEncode(byte[] bytes) {
        return new HexBinaryAdapter().marshal(bytes);
    }

    // string 2 byte
    private byte[] hexDecode(String content) {
        return new HexBinaryAdapter().unmarshal(content);
    }


    public static void main(String[] args) {
        try {
            //System.out.println(INSTANCE.getKey());
            String txt = "dt";

            String encrypt = AesUtil.INSTANCE.encrypt(txt);
            System.out.println("encrypt:" + encrypt + "      " + encrypt.length());

            String decrypt = AesUtil.INSTANCE.decrypt(encrypt);
            System.out.println("decrypt:" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
