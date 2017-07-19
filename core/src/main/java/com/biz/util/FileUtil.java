package com.biz.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by ThinkPad on 2015/3/10.
 */
public class FileUtil {
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir == null ? "" : sdDir.toString();
    }

    public static boolean copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                File file = new File(newPath);
                File dir = file.getParentFile();
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                OutputStream myOutput = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    myOutput.write(buffer, 0, byteread);
                }
                inStream.close();
                try {
                    myOutput.flush();
                } catch (Exception e2) {
                }
                try {
                    myOutput.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean copyFile(File oldfile, File file, int i) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldfile); //读入原文件
                File dir = file.getParentFile();
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                OutputStream myOutput = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    myOutput.write(buffer, 0, byteread);
                }
                inStream.close();
                try {
                    myOutput.flush();
                } catch (Exception e2) {
                }
                try {
                    myOutput.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */

    public static boolean fileChannelCopy(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);
            File dir = t.getParentFile();
            if (dir.exists() == false) {
                dir.mkdirs();
            }
            if (t.exists()) {
                t.delete();
            }
            t.createNewFile();
            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道

            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        } finally {

            try {

                fi.close();

                in.close();

                fo.close();

                out.close();
                return true;
            } catch (Exception e) {

                e.printStackTrace();
                return false;
            }

        }

    }

    public static void fileScan(Context context, String filePath) {
        Uri data = Uri.parse("file://" + filePath);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    public static boolean isGif(String path) {
        String type = FileType.getFileHeader(path);
        if (type != null && FileType.GIF_BYTE.contains(type)) {
            return true;
        }
        return false;
    }


    public static boolean copyChannelFile(String source, String dist) {

        if (!TextUtils.isEmpty(source) &&
                !TextUtils.isEmpty(dist) &&
                FileUtil.fileChannelCopy(new File(source), new File(dist))) {
            return true;
        }
        return false;
    }

}
