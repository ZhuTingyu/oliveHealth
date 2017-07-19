package com.biz.util;

import com.biz.application.BaseApplication;
import com.biz.util.LogUtil;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;


public class FrescoImageUtil {
    /**
     * 缓存XML名称
     */
    public static final String SharedPreferences_ConfigNAME = "volleyConfig";
    private static final String DEFAULT_CACHE_DIR = "ImageCache";
    public static final String SD_FILE_DIRECTORY = "warehouse";
    private static long DEFAULT_DISK_USAGE_BYTES = 0;
    private static int DEFAULT_MEMORY_USAGE_BYTES = 5 * 1024 * 1024;

    public static void initFresco(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int size = activityManager.getMemoryClass();
        //获取总内存大小
        if (size < 35) {
            DEFAULT_MEMORY_USAGE_BYTES = 5 * 1024 * 1024;
        } else if (size < 48 * 1024 * 1024) {
            DEFAULT_MEMORY_USAGE_BYTES = 10 * 1024 * 1024;
        } else {
            DEFAULT_MEMORY_USAGE_BYTES = 20 * 1024 * 1024;
        }
        File cacheDir = getCacheDir(context);
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(new File(cacheDir.getParent() + File.separator, DEFAULT_CACHE_DIR))
                .setMaxCacheSize(DEFAULT_DISK_USAGE_BYTES > 0 ? DEFAULT_DISK_USAGE_BYTES : DEFAULT_MEMORY_USAGE_BYTES)
                .setMaxCacheSizeOnLowDiskSpace(5 * 1024 * 1024)
                .setMaxCacheSizeOnVeryLowDiskSpace(5 * 1024 * 1024)
                .setBaseDirectoryName("fresco")
                .build();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
//                .setWebpSupportEnabled(true)
//                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
//                .setCacheKeyFactory(cacheKeyFactory)
//                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
//                .setExecutorSupplier(executorSupplier)
//                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(diskCacheConfig)
//                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
//                .setNetworkFetchProducer(networkFetchProducer)
//                .setPoolFactory(poolFactory)
//                .setProgressiveJpegConfig(progressiveJpegConfig)
//                .setRequestListeners(requestListeners)
//                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();
        Fresco.initialize(context, config);
    }

    public static long getCacheSize() {
        File file = getCacheDir(BaseApplication.getAppContext());
        LogUtil.print("D:" + getDirSize(file));
        LogUtil.print("F:" + file.getPath());
        LogUtil.print("F:" + file.length());
        LogUtil.print("L:" + Fresco.getImagePipelineFactory().getSmallImageFileCache().getSize());
        LogUtil.print("L:" + Fresco.getImagePipelineFactory().getMainFileCache().getSize());
        return getDirSize(file);
    }

    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                long size = file.length();
                return size;
            }
        } else {
            return 0l;
        }
    }

    public static void clearCache() {
        Fresco.getImagePipeline().clearDiskCaches();
        Fresco.getImagePipeline().clearMemoryCaches();
        Fresco.getImagePipeline().clearCaches();
        Fresco.getImagePipelineFactory().getMainFileCache().clearAll();
        deleteVolley(BaseApplication.getAppContext().getCacheDir());
    }

    public static void deleteVolley(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteVolley(childFiles[i]);
            }
            file.delete();
        }
    }

    public static File getCacheDir(Context context) {
        File cacheDir = null;
        String type = getSaveName(context, "volleySaveType");
        String saveAvailableSize = getSaveName(context, "saveAvailableSize");
        if (type == null || "".equals(type)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
                    && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                type = "SD";
//                StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
//                long blockSize = sf.getBlockSize();
//                long availCount = sf.getAvailableBlocks();
//                long availableSize = availCount * blockSize / 1024 / 1024;//MB
//                if (availableSize > 100) {
                cacheDir = new File(context.getCacheDir() + "/" + SD_FILE_DIRECTORY + "/", DEFAULT_CACHE_DIR);
                saveAvailableSize = "20";
//                } else if (availableSize > 50) {
//                    cacheDir = new File(context.getCacheDir() + "/" + SD_FILE_DIRECTORY + "/", DEFAULT_CACHE_DIR);
//                    saveAvailableSize = "20";
//                } else if (availableSize > 20) {
//                    cacheDir = new File(context.getCacheDir() + "/" + SD_FILE_DIRECTORY + "/", DEFAULT_CACHE_DIR);
//                    saveAvailableSize = "10";
//                } else {
//                    type = "System";
//                    saveAvailableSize = "5";
//                    cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
//                }
            } else {
                type = "System";
                File root = Environment.getRootDirectory();
                StatFs sf = new StatFs(root.getPath());
                long blockSize = sf.getBlockSize();
                long availCount = sf.getAvailableBlocks();
                long availableSize = availCount * blockSize / 1024 / 1024;//MB
                if (availableSize > 100) {
                    saveAvailableSize = "20";
                } else if (availableSize > 50) {
                    saveAvailableSize = "10";
                } else {
                    saveAvailableSize = "5";
                }
                cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
            }
            setSaveName(context, "volleySaveType", type);
            setSaveName(context, "saveAvailableSize", saveAvailableSize);
        }
        try {
            DEFAULT_DISK_USAGE_BYTES = Integer.valueOf(saveAvailableSize) * 1024 * 1024;
        } catch (Exception e) {
            DEFAULT_DISK_USAGE_BYTES = 5 * 1024 * 1024;
        }
        return cacheDir;
    }

    /**
     * 读取配置信息
     *
     * @param context
     * @param name
     * @return
     */
    private static String getSaveName(Context context, String name) {
        SharedPreferences config = context.getSharedPreferences(SharedPreferences_ConfigNAME, 0);
        if (config == null) {
            return "";
        }
        String check = config.getString(name, "");
        return check;
    }

    /**
     * 修改配置信息
     *
     * @param context
     * @param name
     * @param data
     */
    private static void setSaveName(Context context, String name, String data) {
        SharedPreferences config = context.getSharedPreferences(SharedPreferences_ConfigNAME, 0);
        if (config == null) {
            return;
        }
        config.edit().putString(name, data);
    }

    public class BitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
        private static final int MAX_CACHE_ENTRIES = 256;
        private static final int MAX_EVICTION_QUEUE_SIZE = Integer.MAX_VALUE;
        private static final int MAX_EVICTION_QUEUE_ENTRIES = Integer.MAX_VALUE;
        private static final int MAX_CACHE_ENTRY_SIZE = Integer.MAX_VALUE;

        private final ActivityManager mActivityManager;

        public BitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
            mActivityManager = activityManager;
        }

        @Override
        public MemoryCacheParams get() {
            return new MemoryCacheParams(
                    getMaxCacheSize(),
                    MAX_CACHE_ENTRIES,
                    MAX_EVICTION_QUEUE_SIZE,
                    MAX_EVICTION_QUEUE_ENTRIES,
                    MAX_CACHE_ENTRY_SIZE);
        }

        private int getMaxCacheSize() {
            final int maxMemory =
                    Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
            if (maxMemory < 32 * ByteConstants.MB) {
                return 4 * ByteConstants.MB;
            } else if (maxMemory < 64 * ByteConstants.MB) {
                return 6 * ByteConstants.MB;
            } else {
                // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
                // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                    return 8 * ByteConstants.MB;
                } else {
                    return maxMemory / 4;
                }
            }
        }
    }
}