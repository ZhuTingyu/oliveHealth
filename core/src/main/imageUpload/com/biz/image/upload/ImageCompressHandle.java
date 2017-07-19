package com.biz.image.upload;

import com.biz.util.LogUtil;
import com.gridy.main.util.CropGridyBitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;

import rx.functions.Func2;

public class ImageCompressHandle implements Func2<String, String, ImageCompressEntity> {
    private final static int upload_w = 960;
    private final static int upload_h = 1500;
    private boolean isYt = false;

    @Override
    public ImageCompressEntity call(String src, String imageModule) {
        ImageCompressEntity entity = new ImageCompressEntity();
        entity.isDisCache = true;
        entity.DeleteFile = false;
        if (TextUtils.isEmpty(src)) {
            return entity;
        }
//        if (InitDaoHelper..original.equals(imageModule))//身份证不缓存
//        {
//            entity.src = src;
//            entity.isDisCache = false;
//            return entity;
//        }
        entity.degree = ExifInterfaceUtil.readPictureDegree(src);
        SoftReference<Bitmap> bitmap = null;
        isYt = false;
        try {
            LogUtil.print("---图片路径:" + src + "  开始");
            long date1 = System.currentTimeMillis();
            bitmap = getUploadBitmapFromFile(src);
            long date2 = System.currentTimeMillis();
            LogUtil.print("---图片处理完成:" + src + "  耗时：" + (date2 - date1) + "  是否上传原图" + isYt);
            if (isYt) {
                LogUtil.print(src + "         yt");
                entity.src = src;
            } else {
                LogUtil.print("---图片:" + src + " h:" + bitmap.get().getHeight() + "  w:" + bitmap.get().getWidth());
                //计算缩放长宽
                int w, h;
                if (bitmap.get().getHeight() > bitmap.get().getWidth()) {
                    h = upload_h;
                    w = CropGridyBitmap.getData(Double.valueOf(h) * Double.valueOf(bitmap.get().getWidth()) / Double.valueOf(bitmap.get().getHeight()));
                } else {
                    w = upload_h;
                    h = CropGridyBitmap.getData(Double.valueOf(w) * Double.valueOf(bitmap.get().getHeight()) / Double.valueOf(bitmap.get().getWidth()));
                }
                Bitmap bitmap1 = CropGridyBitmap.CropImageBitmap(bitmap.get(), w, h, 0);
                entity.src = (Bitmap2BytesFile(bitmap1));
                entity.DeleteFile = true;
                LogUtil.print("保存图片耗时：" + (System.currentTimeMillis() - date2));
            }
        } catch (Exception e) {
            if (bitmap != null) {
                if (bitmap.get() != null) {
                    if (!bitmap.get().isRecycled()) {
                        bitmap.get().recycle();
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            LogUtil.print("---------------------------- OutOfMemoryError --------------------------");
            if (bitmap != null) {
                if (bitmap.get() != null) {
                    if (!bitmap.get().isRecycled()) {
                        bitmap.get().recycle();
                    }
                }
            }
        }
        if (bitmap != null) {
            if (bitmap.get() != null) {
                if (!bitmap.get().isRecycled()) {
                    bitmap.get().recycle();
                }
            }
        }
        return entity;
    }

    private SoftReference<Bitmap> getUploadBitmapFromFile(String src)
            throws FileNotFoundException {
        if (src == null) {
            return null;
        }
        if (src.equals("")) {
            return null;
        }
        File file = new File(src);
        SoftReference<Bitmap> bitmap = null;
        BitmapFactory.Options opts = null;
        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, opts);
        BufferedInputStream in = null;
        int width, height;
        if (opts.outHeight > opts.outWidth) {
            height = upload_h;
            BigDecimal bigDecimal=new BigDecimal(height);
            bigDecimal=bigDecimal.multiply(new BigDecimal(opts.outWidth));
            bigDecimal=bigDecimal.divide(new BigDecimal(opts.outHeight),2, BigDecimal.ROUND_HALF_EVEN);
            width = CropGridyBitmap.getData(bigDecimal.doubleValue());
        } else {
            width = upload_h;
            BigDecimal bigDecimal=new BigDecimal(width);
            bigDecimal=bigDecimal.multiply(new BigDecimal(opts.outHeight));
            bigDecimal=bigDecimal.divide(new BigDecimal(opts.outWidth),2, BigDecimal.ROUND_HALF_EVEN);
            height = CropGridyBitmap.getData(bigDecimal.doubleValue());
        }
        int maxth = Math.max(width, height);
        int maxth1 = Math.max(opts.outWidth, opts.outHeight);
        if (maxth > maxth1 || Math.min(opts.outWidth, opts.outHeight) < 640) {
            isYt = true;
            return null;
        } else {

            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = calculateInSampleSize(opts, width, height);
            LogUtil.print("--opts.inSampleSize--:" + opts.inSampleSize);
            if (opts.inSampleSize == 1) {
                long filesize = file.length();
                BigDecimal bigDecimal=new BigDecimal(opts.outHeight);
                bigDecimal=bigDecimal.multiply(new BigDecimal(opts.outWidth));

                BigDecimal bigDecimal1=new BigDecimal(filesize);
                bigDecimal1=bigDecimal1.divide(new BigDecimal(1024),2, BigDecimal.ROUND_HALF_EVEN);
                bigDecimal=bigDecimal.divide(bigDecimal1,2, BigDecimal.ROUND_HALF_EVEN);
                //(opts.outHeight * opts.outWidth) / (filesize / 1024d)
                if (bigDecimal.intValue()> 5000) {
                    isYt = true;
                    return null;
                }
            }
            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
            try {
                bitmap = new SoftReference<Bitmap>(BitmapFactory.decodeFile(src, opts));
            } catch (OutOfMemoryError e) {
            }
        }
        return bitmap;
    }

    public static String Bitmap2BytesFile(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        String path = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + "tempImage.tmp";
        FileOutputStream baos = null;
        try {
            File file = new File(path);
            baos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        int quality = 35;
        try {
            quality = 85;
        } catch (Exception e) {
        }
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            baos.flush();
            baos.close();
        } catch (Exception ex) {
            return null;
        }
        bm.recycle();
        return path;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}