package com.biz.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        StringBuffer buf = new StringBuffer("");
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            byte b[] = digest.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            return "";
        }
        return buf.toString();
    }

    public static String getByteArrayMD5(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuffer buf = new StringBuffer("");
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            byte b[] = digest.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (Exception e) {
            return "";
        }
        return buf.toString();
    }

    public static String toMD5(String inStr) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr.getBytes());
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // Log.e("555","result: " + buf.toString());//32位的加密
            // Log.e("555","result: " + buf.toString().substring(8,24));//16位的加密

        } catch (NoSuchAlgorithmException e) {
        }
        return buf.toString();
    }
}