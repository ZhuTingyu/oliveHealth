package com.biz.image.upload;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.text.TextUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Title: OpcvUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/1/10  18:17
 *
 * @author wangwei
 * @version 1.0
 */
public class OpenCVUtil {
    public final static int MAX_LENGTH = 1024;
    static {
        System.loadLibrary("opencv_java");
    }

    public static void init() {

    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param resourceId
     * @return
     */
    public static Mat loadResource(Context context, int resourceId) {
        Mat matImageDes = null;
        try {
            matImageDes = Utils.loadResource(context, resourceId, Highgui.IMWRITE_JPEG_QUALITY);
        } catch (IOException e) {
        }
        Mat matImage = new Mat(matImageDes.rows(), matImageDes.cols(), CvType.CV_8UC4);
        Imgproc.cvtColor(matImageDes, matImage, Imgproc.COLOR_BGRA2RGBA);//COLOR_BGRA2RGBA
        matImageDes.release();
        return matImage;

    }

    public static Mat load(String file, boolean isRotation) {
        Mat gray = Highgui.imread(file);
        if (isRotation) {
            int rotation = ExifInterfaceUtil.readPictureDegree(file);
            if (rotation != 0) {
                Mat dst = rotation(rotation, gray);
                gray.release();
                return dst;
            }
        }
        return gray;

    }

    /**
     * @param degree
     * @param mat_bmp
     * @return
     */
    public static Mat rotation(int degree, Mat mat_bmp) {
        degree = 360 - degree;//因为是逆时针旋转
        // 计算旋转后图像的宽高
        double radians = Math.toRadians(degree);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int width = mat_bmp.width();
        int height = mat_bmp.height();
        int newWidth = (int) (width * cos + height * sin);
        int newHeight = (int) (width * sin + height * cos);
        // 能把原图像和旋转后图像同时放入的外框
        int frameWidth = Math.max(width, newWidth);
        int frameHeight = Math.max(height, newHeight);
        Size frameSize = new Size(frameWidth, frameHeight);
        Mat mat_frame = new Mat(frameSize, mat_bmp.type());
        // 将原图像copy进外框
        int offsetX = (frameWidth - width) / 2;
        int offsetY = (frameHeight - height) / 2;
        Mat mat_frame_submat = mat_frame.submat(offsetY, offsetY + height, offsetX, offsetX
                + width);
        mat_bmp.copyTo(mat_frame_submat);
        // 旋转外框
        Point center = new Point(frameWidth / 2, frameHeight / 2);
        Mat mat_rot = Imgproc.getRotationMatrix2D(center, degree, 1.0);
        Mat mat_res = new Mat(); // result
        Imgproc.warpAffine(mat_frame, mat_res, mat_rot, frameSize, Imgproc.INTER_LINEAR,
                Imgproc.BORDER_CONSTANT, Scalar.all(0));
        // 从旋转后的外框获取新图像
        offsetX = (frameWidth - newWidth) / 2;
        offsetY = (frameHeight - newHeight) / 2;
        Mat mat_res_submat = mat_res.submat(offsetY, offsetY + newHeight, offsetX, offsetX
                + newWidth);
        return mat_res_submat;
    }

    public static boolean resizeImageScale(Mat imageSrc, double scale, String savePath) {
        Size dsize = new Size(imageSrc.width() * scale, imageSrc.height() * scale); // 设置新图片的大小
        Mat img2 = new Mat(dsize, CvType.CV_8UC4);// 创建一个新的Mat（opencv的矩阵数据类型）
        Imgproc.resize(imageSrc, img2, dsize);//调用Imgproc的Resize方法，进行图片缩放
        if (Highgui.imwrite(savePath, img2)) {//将图形保存到new.jpg中
            return true;
        } else {
            return false;
        }
    }

    public static Mat resizeImage(Mat matImage, int maxLength) {
        if (maxLength > 0 && matImage != null) {
            int w, h;
            if (matImage.width() > matImage.height()) {
                BigDecimal bigDecimal = new BigDecimal(maxLength);
                bigDecimal = bigDecimal.divide(new BigDecimal(matImage.width()), 2, BigDecimal.ROUND_HALF_EVEN);
                bigDecimal = bigDecimal.multiply(new BigDecimal(matImage.height()));
                w = maxLength;
                h = bigDecimal.intValue();
            } else {
                BigDecimal bigDecimal = new BigDecimal(maxLength);
                bigDecimal = bigDecimal.divide(new BigDecimal(matImage.height()), 2, BigDecimal.ROUND_HALF_EVEN);
                bigDecimal = bigDecimal.multiply(new BigDecimal(matImage.width()));
                w = bigDecimal.intValue();
                h = maxLength;
            }
            Size size = new Size(w, h);
            Mat matImageResize = new Mat(h, w, CvType.CV_8UC4);
            Imgproc.resize(matImage, matImageResize, size);
            matImage.release();
            return matImageResize;
        }
        return matImage;
    }

    public static boolean resizeImage(Mat matImage, int maxLength, String savePath) {
        return saveMat(resizeImage(matImage, maxLength), savePath);
    }

    public static boolean saveMat(Mat matImage, String savePath) {
        if (matImage == null || TextUtils.isEmpty(savePath)) return false;
//        MatOfInt matOfInt=new MatOfInt();
//        matOfInt.alloc(80);
        if (Highgui.imwrite(savePath, matImage)) {
            matImage.release();
            return true;
        } else {
            matImage.release();
            return false;
        }
    }
//    Scalar( a, b, c )
//    那么定义的RGB颜色值为： Red = c, Green = b and Blue = a

    public static void putText(Mat drawMat, String title1, String title2,String title3) {
        double fontScale=2.2;
        int thickness=2;
        Scalar color=new Scalar(0, 80, 247);
        Size size=Core.getTextSize(title1,Core.FONT_HERSHEY_PLAIN,fontScale,thickness,null);
        int w=getData(size.width);
        int mag=50;
        int begin1=drawMat.width()-mag-w;
        int h=getData(size.height);
        Core.putText(drawMat, title1, new Point(begin1, mag), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);
        Core.putText(drawMat, title2, new Point(begin1, mag+h+10+10), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);
        Core.putText(drawMat, title3, new Point(begin1, mag+h+10+10+h+10+10), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);
        Core.putText(drawMat, title1, new Point(mag, drawMat.height()-(mag+h+10+10+h+10+10)), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);
        Core.putText(drawMat, title2, new Point(mag, drawMat.height()-(mag+h+10+10)), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);
        Core.putText(drawMat, title3, new Point(mag, drawMat.height()-mag), Core.FONT_HERSHEY_PLAIN,
                fontScale, color, thickness);

    }

    public static boolean disposeImage(String src, int maxLength, String savePath,String para1,String para2,String para3) {
        Mat mat = OpenCVUtil.load(src, false);
        mat = OpenCVUtil.resizeImage(mat, maxLength);
        int rotation = ExifInterfaceUtil.readPictureDegree(src);
        if (rotation != 0) {
            Mat dst = rotation(rotation, mat);
            mat.release();
            mat = dst;
        }
        putText(mat, para1, para2,para3);
        return saveMat(mat, savePath);
    }
    public static boolean disposeImage(String src, int maxLength, String savePath) {
        Mat mat = OpenCVUtil.load(src, false);
        mat = OpenCVUtil.resizeImage(mat, maxLength);
        int rotation = ExifInterfaceUtil.readPictureDegree(src);
        if (rotation != 0) {
            Mat dst = rotation(rotation, mat);
            mat.release();
            mat = dst;
        }
        return saveMat(mat, savePath);
    }
    /**
     * 是否使用原图
     * @param src
     * @return
     */
    public static boolean isOriginal(String src) {
        if (TextUtils.isEmpty(src)) return true;
        BitmapFactory.Options opts= new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, opts);
        int width, height;
        if(opts.outHeight<=0||opts.outWidth<=0){
            return true;
        }
        if (opts.outHeight > opts.outWidth) {
            height = MAX_LENGTH;
            BigDecimal bigDecimal = new BigDecimal(height);
            bigDecimal = bigDecimal.multiply(new BigDecimal(opts.outWidth));
            bigDecimal = bigDecimal.divide(new BigDecimal(opts.outHeight), 2, BigDecimal.ROUND_HALF_EVEN);
            width = getData(bigDecimal.doubleValue());
        } else {
            width = MAX_LENGTH;
            BigDecimal bigDecimal = new BigDecimal(width);
            bigDecimal = bigDecimal.multiply(new BigDecimal(opts.outHeight));
            bigDecimal = bigDecimal.divide(new BigDecimal(opts.outWidth), 2, BigDecimal.ROUND_HALF_EVEN);
            height = getData(bigDecimal.doubleValue());
        }
        int maxth = Math.max(width, height);
        int maxth1 = Math.max(opts.outWidth, opts.outHeight);
        if (maxth > maxth1 || Math.min(opts.outWidth, opts.outHeight) < 640) {
            return true;
        } else {
            return false;
        }
    }

    private static int getData(double d)
    {
        double dd=Math.round(d*100 + 0.5)/100.0;
        double dd1=Math.round(dd*10 + 0.5)/10.0;
        return (int)Math.round(dd1);
    }
}
