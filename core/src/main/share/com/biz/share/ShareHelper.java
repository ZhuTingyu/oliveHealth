package com.biz.share;

import com.biz.application.BaseApplication;
import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.biz.http.R;
import com.biz.share.qq.SendQQ;
import com.biz.share.sina.SinaShareUtil;
import com.biz.share.weixin.SendWX;
import com.biz.share.weixin.WeiXinEvent;
import com.biz.util.BitmapUtil;
import com.biz.util.DialogUtil;
import com.biz.util.FileUtil;
import com.biz.util.MD5;
import com.biz.util.Utils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Title: ShareHelper
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/13  14:00
 *
 * @author wangwei
 * @version 1.0
 */
public class ShareHelper {
    private SinaShareUtil sinaShareUtil;
    private SendQQ sendQQ;
    private SendWX sendWX;
    private String url;
    private String title;
    private String message;
    private String imageUrl;
    private BaseFragment baseFragment;
    private BaseActivity baseActivity;
    private boolean isShareCompleteServer = false;
    private long orderId;
    private String shareTag;
    private static final int w = 150;
    private static final int h = 150;
    private final static int numCount = 4;


    public static ShareHelper shareDialog(Context context, String urlString) {
        if (urlString != null &&
                Uri.parse(urlString).getPath().startsWith("/b2c1919/share")) {

            Uri uri = Uri.parse(urlString);
            String imageUrl = uri.getQueryParameter("imagePath");
            String url = uri.getQueryParameter("url");
            String title = uri.getQueryParameter("title");
            String content = uri.getQueryParameter("content");
            ShareHelper helper = new ShareHelper((BaseActivity) context);

            helper.imageUrl(imageUrl).url(url).message(content).shareTitle(title);
            createBottomSheet(context, helper);
            return helper;
        }
        return null;
    }

    public static ShareHelper shareDialog(Context context, String imageUrl, String url, String title, String content) {
        ShareHelper helper = new ShareHelper((BaseActivity) context);
        helper.imageUrl(imageUrl).url(url).message(content).shareTitle(title);
        createBottomSheet(context, helper);
        return helper;
    }
    public static ShareHelper shareDialog(Context context, ShareHelper helper) {
        createBottomSheet(context, helper);
        return helper;
    }



    public static void createBottomSheet(Context context, ShareHelper shareHelper) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setPadding(0, 0, 0, Utils.dip2px(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context, numCount));
        ShareAdapter adapter = new ShareAdapter(context);
        adapter.setNumCount(numCount);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            bottomSheetDialog.dismiss();
            String s = (String) view.getTag();
            if (s.equals(view.getResources().getString(R.string.text_qq))) {
                shareHelper.shareQQ();
            } else if (s.equals(view.getResources().getString(R.string.text_qzone))) {
                shareHelper.shareQQzone();
            } else if (s.equals(view.getResources().getString(R.string.text_wechat_friend))) {
                shareHelper.shareWeiXin();
            } else if (s.equals(view.getResources().getString(R.string.text_wechat_cycle))) {
                shareHelper.shareWeiXinTimeLine();
            } else if (s.equals(view.getResources().getString(R.string.text_sina))) {
                shareHelper.shareSina();
            }
        });
        recyclerView.setAdapter(adapter);
        bottomSheetDialog.setContentView(recyclerView);
        bottomSheetDialog.show();

    }

    /**
     * 设置是否开启分享成功后告诉服务器
     * 当设置为true 的时候 需要设置orderId shareTag
     *
     * @param shareCompleteServer
     */
    public void setShareCompleteServer(boolean shareCompleteServer) {
        isShareCompleteServer = shareCompleteServer;
    }

    /**
     * @param orderId
     */
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    /**
     * @param shareTag
     */
    public void setShareTag(String shareTag) {
        this.shareTag = shareTag;
    }

    public ShareHelper(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
        init();
    }

    public ShareHelper(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        init();
    }

    private void init() {
        EventBus.getDefault().register(this);
    }

    /**
     * 分享的url
     *
     * @param url
     * @return
     */
    public ShareHelper url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 分享的标题
     *
     * @param title
     * @return
     */
    public ShareHelper shareTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 分享的内容
     *
     * @param message
     * @return
     */
    public ShareHelper message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 分享的图片url 网络
     *
     * @param imageUrl
     * @return
     */
    public ShareHelper imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }


    public String getUrl() {
        return url;
    }


    /**
     * 建议用这个  file是图片本地路径
     * 如果安装有客户端 这样才有图片
     * imageUrl 网络图片
     */
    public void shareSina() {
        if (sinaShareUtil == null)
            sinaShareUtil = new SinaShareUtil(getActivity());
        getActivity().setProgressVisible(true);
        submitBindMainThreadRequest(downloadImage(getActivity(), w, h, imageUrl), file -> {
            sinaShareUtil.send(title, message, imageUrl, file, url, () -> {
                getActivity().setProgressVisible(false);
                shareSina();
            }, b -> {
                shareSinaComplete(b);
            });
        }, throwable -> {
            shareSinaComplete(false);
        });
    }


    private void shareSinaComplete(boolean isOk) {
        getActivity().setProgressVisible(false);
        if (isOk)
            shareCompleteServer();
        DialogUtil.createDialogView(getActivity(), getActivity().getString(isOk ? R.string.text_share_sina_ok :
                R.string.text_share_sina_error), (dialog, which) -> {
            dialog.dismiss();
        }, R.string.btn_confirm);
    }

    public void onPause() {
        getActivity().setProgressVisible(false);
    }


    /**
     * imageUrl 需要网络图片或者本地图片路径
     */
    public void shareQQ() {
        if (sendQQ == null)
            sendQQ = new SendQQ(getActivity());
        submitBindMainThreadRequest(downloadImage(getActivity(), w, h, imageUrl), s -> {
            sendQQ.sendQQ(url, title, message, Uri.fromFile(new File(s)).getPath(), b -> {
                if (b)
                    shareCompleteServer();
            });
        }, t -> {
        });

    }

    /**
     * imageUrl 需要网络图片或者本地图片路径
     */
    public void shareQQzone() {
        if (sendQQ == null)
            sendQQ = new SendQQ(getActivity());
        submitBindMainThreadRequest(downloadImage(getActivity(), w, h, imageUrl), s -> {
            sendQQ.shareToQQzone(url, title, message, Uri.fromFile(new File(s)).getPath(), b -> {
                if (b)
                    shareCompleteServer();
            });
        }, t -> {
        });
    }

    public void shareWeiXin() {
        if (sendWX == null)
            sendWX = new SendWX(getActivity());
        submitBindMainThreadRequest(downloadImage(getActivity(), w, h, imageUrl), file -> {
            Bitmap bitmap = BitmapUtil
                    .getCropBitmapFromFile(file,
                            150, 150).get();
            sendWX.send(url, title, message, bitmap);
        }, t -> {
        });
    }

    public void shareWeiXinTimeLine() {
        if (sendWX == null)
            sendWX = new SendWX(getActivity());
        submitBindMainThreadRequest(downloadImage(getActivity(), w, h, imageUrl), file -> {
            Bitmap bitmap = BitmapUtil
                    .getCropBitmapFromFile(file,
                            150, 150).get();
            sendWX.sendTimeLine(url, title, message, bitmap);
        }, t -> {
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getActivity().setProgressVisible(false);
        if (sinaShareUtil != null) {
            sinaShareUtil.onActivityResult(requestCode, resultCode, data);
        }
        if(sendQQ!=null)
        {
            sendQQ.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void onNewIntent(Intent intent) {
        if (sinaShareUtil != null)
            sinaShareUtil.onNewIntent(intent);
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        sendQQ = null;
        sinaShareUtil = null;
        sendWX = null;
    }

    public void onEventMainThread(WeiXinEvent event) {
        if (event != null)
            if (event.isOK)
                shareCompleteServer();
    }

    private BaseActivity getActivity() {
        if (baseActivity != null) return baseActivity;
        if (baseFragment != null) baseFragment.getActivity();
        return null;
    }

    private void shareCompleteServer() {
//        if (getActivity()!=null&&getActivity() instanceof WebViewActivity){
//            ((WebViewActivity) getActivity()).shareCompleteServer();
//        }
//        if (!isShareCompleteServer) return;
//        submitRequest(OrderModel.shareCallback(orderId, shareTag), r -> {
//        }, throwable -> {
//        });
    }


    private <T> void submitRequest(Observable<T> observable, final Action1<? super T> onNext, final Action1<Throwable> onError) {
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    public <T> void submitBindMainThreadRequest(Observable<T> observable, final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (null != baseActivity && baseActivity instanceof BaseActivity) {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
        } else if (null != baseFragment && baseFragment instanceof BaseFragment) {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
        } else {
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
        }
    }

    private Observable<String> downloadImage(Context callerContext, int width, int height, String uri) {
        return Observable.create(subscriber -> {
            String fileName = "";
            if (TextUtils.isEmpty(uri)) {
                fileName = getCacheImageName();
            } else {
                SimpleCacheKey simpleCacheKey = new SimpleCacheKey(uri);
                if (Fresco.getImagePipelineFactory().getMainFileCache().probe(simpleCacheKey)) {
                    FileBinaryResource fileBinaryResource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(simpleCacheKey);
                    if (fileBinaryResource != null)
                        fileName = fileBinaryResource.getFile().getAbsolutePath();
                } else {

                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
                            .writeTimeout(50 * 10000, TimeUnit.MILLISECONDS)
                            .readTimeout(50 * 10000, TimeUnit.MILLISECONDS);
                    OkHttpClient client = builder.build();
                    okhttp3.Request request = new okhttp3.Request.Builder().url(uri).get().build();
                    Call call = client.newCall(request);
                    Response response = null;
                    InputStream is = null;
                    try {
                        response = call.execute();
                    } catch (MalformedURLException e) {
                    } catch (SocketTimeoutException e) {
                    } catch (IOException e) {
                    }
                    if (response.code() == 200) {
                        is = response.body().byteStream();
                        if (is != null) {
                            InputStream finalIs = is;
                            try {
                                Fresco.getImagePipelineFactory().getMainFileCache().insert(simpleCacheKey, os -> {
                                    byte[] buffer = new byte[1024];
                                    int byteread = 0;
                                    while ((byteread = finalIs.read(buffer)) != -1) {
                                        os.write(buffer, 0, byteread);
                                    }
                                    finalIs.close();
                                });
                            } catch (IOException e) {
                            }
                            try {
                                FileBinaryResource fileBinaryResource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(simpleCacheKey);
                                if (fileBinaryResource != null)
                                    fileName = fileBinaryResource.getFile().getAbsolutePath();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(fileName)) {
                subscriber.onNext(getCacheImageName());
                subscriber.onCompleted();
            } else {
                String shareFileName = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/share/" + MD5.toMD5(fileName);
                File file = new File(shareFileName);
                if (!file.exists()) {
                    FileUtil.copyChannelFile(fileName, shareFileName);
                }
                subscriber.onNext(shareFileName);
                subscriber.onCompleted();
            }
        });

    }

    private String getCacheImageName() {
        String fileName = "";
        String fileCacheName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/share/ic_share_logo.png";
        File file = new File(fileCacheName);
        if (file.exists()&&file.length()>1024*1) {
            fileName = fileCacheName;
        } else {
            InputStream inStream = null;
            try {
                inStream = BaseApplication.getAppContext().getResources().getAssets().open("ic_share_logo.png");
            } catch (IOException e) {
            }
            if (!file.getParentFile().exists())
                file.mkdirs();
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            int byteread = 0;
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteread);
                }
                inStream.close();
                fos.flush();
                fos.close();
            } catch (IOException e) {
            }
            fileName = fileCacheName;
        }
        return fileName;
    }
}
