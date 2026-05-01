package com.java110.front;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * @author huang.sheng
 * @date 2022/6/9
 */
public class JasyptUtil {

    private static String password = "Ozsc/xsCeZlR9FL1i8nwTA==";


    /**
     * 加密
     * @param str
     * @return
     */
    public static String encrypt(String str){
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setPassword(password);
        String password = stringEncryptor.encrypt(str);
        return password;
    }

    /**
     * 解密
     * @param str
     * @return
     */
    public static String decrypt(String str){
        StandardPBEStringEncryptor stringEncryptor = new StandardPBEStringEncryptor();
        stringEncryptor.setPassword(password);
        String password = stringEncryptor.decrypt(str);
        return password;
    }


    public static void main(String[] args) {
        String encryptStr = JasyptUtil.encrypt("123456");
        System.out.println("密文："+encryptStr);

        String enStr = "Zjk+ejfSAAbZB0QHyzbEcg8tFFMkYHXc";
        String decryptStr = JasyptUtil.decrypt(enStr);
        System.out.println("明文：" + decryptStr);
    }
}
