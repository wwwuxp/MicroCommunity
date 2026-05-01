package com.java110.utils.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;
import java.util.Arrays;

/**
 * AES对称加密
 * @author hh
 * @date 2021/11/12
 */
public class AESUtil {

    /**
     * 算法名称
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * IV
     */
    public static final String IV = "89DFA17B6BB06A22";

    /**
     * 加密密钥
     */
    public static final String KEY = "1234567890123456";

    public static Key init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 加密方法
     * @param content 要加密的字符串
     */
    public static String encrypt(String content) {
        byte[] encryptedText = null;
        Key key = init(KEY.getBytes());
        try {
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));
            encryptedText = cipher.doFinal(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(Hex.encode(encryptedText)).toUpperCase();
    }

    /**
     * 解密方法
     * @param encryptedData 要解密的字符串
     */
    public static String decrypt(String encryptedData) {
        byte[] encryptedText = null;
        Key key = init(KEY.getBytes());
        try {
            // 初始化cipher
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes()));
            encryptedText = cipher.doFinal(Hex.decode(encryptedData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encryptedText);
    }

    /*public static void main(String[] args) {
        String content = "GS#11111112345678909876543212345678909876543";
        // // 加密字符串
        // System.out.println("加密前的：" + content);
        // // 加密方法
        // String encrypt = encrypt(content);
        // System.out.println("加密后的内容：" + encrypt);
        // 解密方法
        String enc = "1C73B46E725AA0565CFA50F79EBF60FDBF6277D3C5AA824EA423CC9C7E5057C4DB0220BD12BC84797A77261D43CF1E5B";
        System.out.println("解密后的内容：" + decrypt(enc));
    }*/

}
