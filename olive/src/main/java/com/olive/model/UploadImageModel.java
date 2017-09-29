package com.olive.model;

import android.content.Context;

import com.biz.http.ResponseJson;
import com.biz.http.cache.HttpUrlCache;
import com.biz.util.GsonUtil;
import com.biz.util.LogUtil;
import com.google.gson.reflect.TypeToken;
import com.olive.util.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class UploadImageModel {
    public static Observable<ResponseJson<String>> uploadImg(Context context, String url, String fileUrl){
        return Observable.create(subscriber -> {
            //获取拼接签名的地址
            com.biz.http.Request temp = com.biz.http.Request.builder();
            temp.url(url);
            String postUrl = temp.getUrl(HttpUrlCache.getInstance().getMasterHeadUrl());

            File file = null;
            try {
                file = ImageUtils.createTmpFile(context);
                ImageUtils.compressImg(fileUrl, file);
            } catch (IOException e) {
                e.printStackTrace();
            }


            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), fileBody)
                    .addFormDataPart("userId", UserModel.getInstance().getUserId())
                    .addFormDataPart("token", UserModel.getInstance().getToken())
                    .build();
            Request request = new Request.Builder()
                    .url(postUrl)
                    .post(requestBody)
                    .build();


            final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            OkHttpClient okHttpClient  = httpBuilder
                    //设置超时
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.print("upload" + e.toString());
                    ResponseJson<String> responseJson = new ResponseJson<>();
                    responseJson.code = -1;
                    responseJson.message = "上传失败，请重新上传";
                    subscriber.onNext(responseJson);
                    LogUtil.print("s: " + responseJson.toJsonString());

                }


                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = null;
                    InputStream is = null;
                    ByteArrayOutputStream bos = null;

                    if (response.code() == 200) {
                        is = response.body().byteStream();
                        if (is != null) {
                            bos = new ByteArrayOutputStream();
                            byte b[] = new byte[1024];
                            int i = 0;
                            while ((i = is.read(b, 0, b.length)) != -1) {
                                bos.write(b, 0, i);
                            }
                            result = new String(bos.toByteArray(), "UTF-8");
                        } else {
                            result = null;
                        }
                    } else {
                        LogUtil.print("error code:" + response.code() + " url:" + url);
                    }

                    try {
                        if (call != null) {
                            call.cancel();
                        }
                        if (is != null) {
                            is.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                    }
                    ResponseJson<String> responseJson = GsonUtil.fromJson(result, new TypeToken<ResponseJson<String>>() {}.getType());
                    subscriber.onNext(responseJson);
                    LogUtil.print("s: " + responseJson.toJsonString());
                }
            });
        });
    }
}
