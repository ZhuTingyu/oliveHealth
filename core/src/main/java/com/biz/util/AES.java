package com.biz.util;

/**
 * Created by wangwei on 2016/4/18.
 */
public class AES {
    public static final String AES_PWD = "e0ab52bb342d4a44";
    public static final String AES_IV = "9c39d21518004fa3";

    public static String toAesPwd(String content) {
        AesCryptographer aesCryptographer = new AesCryptographer();
        StringBuffer buf = new StringBuffer("");
        try {
            byte[] b = aesCryptographer.encrypt(content, AES_PWD, AES_IV);
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            return content;
        }
    }

    public static String byte2string(byte[] b) {
        StringBuffer hs = new StringBuffer(100);
        for (int n = 0; n < b.length; n++) {
            hs.append(byte2fex(b[n]));
        }
        return hs.toString();
    }

    public static String byte2fex(byte ib) {
        char[] Digit =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }
}
