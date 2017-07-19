package com.biz.http;

import com.biz.application.BaseApplication;
import com.google.gson.reflect.TypeToken;

import com.biz.http.cache.HttpUrlCache;
import com.biz.util.GsonUtil;
import com.biz.util.LogUtil;

import android.os.Looper;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by wangwei on 2016/3/15.
 */
public class RxNet {
    public static Observable<String> newRequest(Request request) {
        return Observable.create(subscriber -> {
            String s = null;
            List<String> listHead = request.getHeadUrl();
            if (listHead == null || listHead.size() == 0) {
                throw new RuntimeException("http host is empty !");
            }
            request.setBeginTime(System.currentTimeMillis());
            String strUrl = null;
            for (int i = 0; i < listHead.size(); i++) {
                String headUrl = listHead.get(i);
                if (i > 0)
                    strUrl = headUrl;
                String url = request.getUrl(headUrl);
                if (TextUtils.isEmpty(url)) {
                    throw new RuntimeException("http url is empty !");
                }
                LogUtil.print(request.getBeginTime() + " headUrl:" + headUrl);
                LogUtil.print(request.getBeginTime() + " url:" + url);
                if (request.getRestMethodEnum() != null && request.getRestMethodEnum() == RestMethodEnum.GET) {
                    s = get(subscriber, url, request.isHttps(), request.getConnectTime(), request.getReadTime());
                } else {
                    s = post(subscriber, url, request.isHttps(), request.getBodyObj(), request.getConnectTime(), request.getReadTime());
                }
                LogUtil.print(request.getBeginTime() + " s:" + s);
                if (!request.isHtml() && !TextUtils.isEmpty(s)) {
                    ResponsePara responsePara = GsonUtil.fromJson(s, new TypeToken<ResponsePara>() {
                    }.getType());
                    if (responsePara.code != 800 && responsePara.code != 900) {
                        break;
                    } else {
                        s = "";
                    }
                } else if (request.isHtml() && !TextUtils.isEmpty(s)) {
                    break;
                }
            }
            if (!request.isHtml() && TextUtils.isEmpty(s)) {
                ResponseJson responseJson = new ResponseJson();
                responseJson.code = -1;
                responseJson.msg = BaseApplication.getAppContext().getString(R.string.text_network_error);
                s = GsonUtil.toJson(responseJson);
            } else if (request.isHtml() && TextUtils.isEmpty(s)) {
                s = "";
            } else {
                if (!TextUtils.isEmpty(strUrl)) {
                    HttpUrlCache.getInstance().sortIndex(strUrl);
                }
            }
            request.setEndTime(System.currentTimeMillis());
            subscriber.onNext(s);
            subscriber.onCompleted();
        });
    }

    private synchronized static String post(Subscriber subscriber, String url, boolean isHttps, String param, long CONNECT_TIME_OUT, long READ_TIME_OUT) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        if (isHttps) {
            try {
                builder.sslSocketFactory(getCertificates(BaseApplication.getAppContext().getAssets().open("cert.cer")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OkHttpClient client = builder.build();
        String result = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        Call call = null;
        try {
            if (param == null) {
                param = "";
            }
            okhttp3.Request request;
            RequestBody requestBody = new FormBody.Builder().add("data", param).build();
            request = new okhttp3.Request.Builder().url(url)
                    .post(requestBody).build();
            final Call call1 = client.newCall(request);
            call = call1;
            subscriber.add(unsubscribeInUiThread(() -> {
                if (call1 != null)
                    call1.cancel();
            }));
            Response response = call.execute();
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        }
        return result;
    }

    public static String get(Subscriber subscriber, String url, boolean isHttps, long CONNECT_TIME_OUT, long READ_TIME_OUT) {
        LogUtil.print(url);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS);
        if (isHttps) {
            try {
                builder.sslSocketFactory(getCertificates(BaseApplication.getAppContext().getAssets().open("srca.cer")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OkHttpClient client = builder.build();
        String result = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        Call call = null;
        try {
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
            final Call call1 = client.newCall(request);
            call = call1;
            subscriber.add(unsubscribeInUiThread(() -> {
                if (call1 != null)
                    call1.cancel();
            }));
            Response response = call.execute();
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        }
        return result;
    }

    private static SSLSocketFactory getCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not exists ssl");
    }

    public static Subscription unsubscribeInUiThread(final Action0 unsubscribe) {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    unsubscribe.call();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Action0() {
                        @Override
                        public void call() {
                            unsubscribe.call();
                            inner.unsubscribe();
                        }
                    });
                }
            }
        });
    }
}
