package com.biz.image.upload;

import android.media.ExifInterface;

import java.io.IOException;

public class ExifInterfaceUtil {
    public static void SavePictureDegree(String path,int degree) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            switch (degree) {
                case 90:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,Integer.toString(ExifInterface.ORIENTATION_ROTATE_90));
                    break;
                case 180:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,Integer.toString(ExifInterface.ORIENTATION_ROTATE_180));
                    break;
                case 270:
                    exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,Integer.toString(ExifInterface.ORIENTATION_ROTATE_270));
                    break;
            }
            exifInterface.saveAttributes();
        } catch (IOException e) {
        }
    }
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}