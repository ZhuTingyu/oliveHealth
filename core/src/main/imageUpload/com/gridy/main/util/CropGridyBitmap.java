package com.gridy.main.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class CropGridyBitmap {
	 static {
	        System.loadLibrary("CropGridyNdk");
	    }
	 public static synchronized native Bitmap CropImage(String instringpath,int srcleft,int srctop,int right,int bottom,int copyw,int copyh,int orientationDegree);
	 public static synchronized native Bitmap CropImageBitmap(Bitmap bitmap,int copyw,int copyh,int orientationDegree);
	/**
	 * 
	 * @param src 路径
	 * @param width 手机显示图片长
	 * @param height 手机显示图片高
	 * @param orientationDegree 旋转角度
	 * @param centerx left
	 * @param centiry top
	 * @param copyw  选中区域长
	 * @param copyh 选中区域高
	 * @param neww 新区域长
	 * @param newh 新区域高
	 */
	public static synchronized Bitmap getImageInit(String src,
			int width, int height, int orientationDegree, int centerx,
			int centiry, int copyw, int copyh, int neww, int newh)
	{
		if(TextUtils.isEmpty(src)){
			return null;
		}
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(src, opts);
		int mainwidth=opts.outWidth;
		int mainheight=opts.outHeight;
		
		//计算left right top bottom
		int left,top,right,bottom;
		double zindexW=(Double.valueOf(mainwidth) / Double.valueOf(width));
		double zindexH=(Double.valueOf(mainheight) / Double.valueOf(height));
		if(orientationDegree==90){
			zindexW=(Double.valueOf(mainheight) / Double.valueOf(width));
			zindexH=(Double.valueOf(mainwidth) / Double.valueOf(height));
			left= getData(zindexW * Double.valueOf(centiry));
			top=mainheight-getData(zindexH* Double.valueOf(centerx))-getData(zindexW* Double.valueOf(copyw));
			right=left+getData (zindexH* Double.valueOf(copyh));
			bottom=mainheight-getData(zindexH* Double.valueOf(centerx));
		}else if(orientationDegree==180){
			zindexW=(Double.valueOf(mainwidth) / Double.valueOf(width));
			zindexH=(Double.valueOf(mainheight) / Double.valueOf(height));
			bottom=mainheight-getData(zindexH * Double.valueOf(centiry));
			right=mainwidth-getData(zindexW* Double.valueOf(centerx));
			left=right-getData(zindexW* Double.valueOf(copyw));
			top=bottom-getData(zindexH* Double.valueOf(copyh));
		}else if(orientationDegree==270){
			zindexW=(Double.valueOf(mainheight) / Double.valueOf(width));
			zindexH=(Double.valueOf(mainwidth) / Double.valueOf(height));
			top=getData(zindexH* Double.valueOf(centerx));
			bottom=top+getData(zindexW* Double.valueOf(copyw));
			right=mainwidth-getData (zindexH* Double.valueOf(centiry));
			left=right-getData(zindexW* Double.valueOf(copyh));
		}else{//不旋转
			zindexW=(Double.valueOf(mainwidth) / Double.valueOf(width));
			zindexH=(Double.valueOf(mainheight) / Double.valueOf(height));
			top=getData(zindexH * Double.valueOf(centiry));
			left=getData(zindexW* Double.valueOf(centerx));
			right=left+getData(zindexW* Double.valueOf(copyw));
			bottom=top+getData(zindexH* Double.valueOf(copyh));
		}
		
		if(right>mainwidth){
			right=mainwidth;
		}
		if(bottom>mainheight){
			bottom=mainheight;
		}
		if(left<0)
		{
			left=0;
		}
		if(top<0)
		{
			top=0;
		}
		if((right-left)>(bottom-top)){
			right=Math.abs(right-((right-left)-(bottom-top)));
		}else{
			bottom=Math.abs(bottom+((right-left)-(bottom-top)));
		}
		Log.e("w", ""+(right-left));
		Log.e("h", ""+(bottom-top));
		//需要保存的大小
		return CropImage(src,left,top,right,bottom,neww,newh,orientationDegree);
//		return CropImage(src,0,0,2048,2048,neww,newh,orientationDegree);
//		Bitmap bitmap=getBitmap(src,left,top,right,bottom);
//		if(bitmap==null)
//		{
//			return null;
//		}
//		return CropImageBitmap(bitmap, neww, newh, orientationDegree);
	}
	public static synchronized Bitmap getBitmap(String path,int left,int top,int right,int copyw,int bottom){
		BitmapRegionDecoder mDecoder=null;
		try {
			InputStream is =new FileInputStream(path);
			mDecoder = BitmapRegionDecoder.newInstance(is, true);
		} catch (IOException e) {
		}
		if(mDecoder==null)
		{
			return null;
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Config.ARGB_8888;
		opts.inInputShareable = true;
		int d=(int) (right-left)/copyw;
		if(d>1){
		opts.inSampleSize=d;
		}
		Rect mRect = new Rect();
		mRect.set(left, top, right, bottom);
		try {
			Bitmap bm = mDecoder.decodeRegion(mRect, opts);
			return bm;
		} catch (Exception e) {
		}catch(OutOfMemoryError oom){
			
		}
		return null;
	}
	
	
	public static int getData(double d)
	{
		double dd=Math.round(d*100 + 0.5)/100.0;
		double dd1=Math.round(dd*10 + 0.5)/10.0;
		return (int)Math.round(dd1);
	}
	public static Bitmap convertToMutable(Bitmap imgIn){
	    try{
	        //this is the file going to use temporally to save the bytes. 
	        // This file will not be a image, it will store the raw image data.
	        File file =new File(Environment.getExternalStorageDirectory()+File.separator +"temp.tmp");

	        //Open an RandomAccessFile
	        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	        //into AndroidManifest.xml file
	        RandomAccessFile randomAccessFile =new RandomAccessFile(file,"rw");

	        // get the width and height of the source bitmap.
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        Config type = imgIn.getConfig();

	        //Copy the byte to the file
	        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE,0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	        //recycle the source bitmap, this will be no longer used.
	        imgIn.recycle();
	        System.gc();// try to force the bytes from the imgIn to be released

	        //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
	        imgIn =Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    }catch(FileNotFoundException e){
	    }catch(IOException e){
	    } 
	    return imgIn;
	}
}
