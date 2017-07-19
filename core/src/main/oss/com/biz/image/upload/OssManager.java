package com.biz.image.upload;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.biz.application.BaseApplication;
import com.biz.http.R;
import com.biz.util.LogUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Title: OssManager
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/7/27  15:07
 *
 * @author wangwei
 * @version 1.0
 */
public class OssManager {
    private Context context;
    private OSS oss;
    private String bucketName = "";
    private String imageEndpoint = "http://img-cn-beijing.aliyuncs.com";
    private OSSCredentialProvider credentialProvider;

    public OssManager(Context context, String bucketName) {
        this.bucketName = bucketName;
        this.context = context;
        credentialProvider = new OSSStsTokenCredentialProvider(getAk(), getSk(),getToken());
        oss = new OSSClient(context, imageEndpoint, credentialProvider);
    }
    public OssManager(Context context, String bucketName,String ak,String sk) {
        this.bucketName = bucketName;
        this.context = context;
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(ak,sk);
        oss = new OSSClient(context, imageEndpoint, credentialProvider);
    }
    public OssManager(Context context, String bucketName,String ak,String sk,String token) {
        this.bucketName = bucketName;
        this.context = context;
        credentialProvider = new OSSStsTokenCredentialProvider(ak,sk,token);
        oss = new OSSClient(context, imageEndpoint, credentialProvider);
    }
    public String getAk() {
        return context.getString(R.string.oss_ak);
    }

    public String getSk() {
        return context.getString(R.string.oss_sk);
    }

    public String getToken() {
       return context.getString(R.string.oss_token);
    }

    /**
     * 生成图片名称
     *
     * @param type
     * @return
     */
    private static String getName(String type) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + ".jpg";
    }

    /**
     * 获取私有空间图片全链接
     *
     * @param imageUrl
     * @return
     */
    public String getPrivateURL(String imageUrl) {
        try {

            String s = (new OssPrivateObjectURLPresigner(new URI(imageEndpoint),
                    credentialProvider, ClientConfiguration.getDefaultConf()))
                    .privatePresignConstrainedURL(bucketName, imageUrl);
            LogUtil.print("URL:" + s);
            return s;
        } catch (URISyntaxException e) {
        } catch (ClientException e) {
        }
        return null;
    }

    public String getPublicURL(String imageUrl) {
        try {
            String s = (new OssPrivateObjectURLPresigner(new URI(imageEndpoint),
                    credentialProvider, ClientConfiguration.getDefaultConf()))
                    .presignPublicURL(bucketName, imageUrl);
            LogUtil.print("URL:" + s);
            return s;
        } catch (URISyntaxException e) {
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param type
     * @param rotate
     * @param filename
     * @param observable
     */
    public void uploadImage(String type, int rotate, String filename, final UploadObserver observable) {
        String serviceName = getName(type);
        PutObjectRequest put = new PutObjectRequest(bucketName, serviceName, filename);
        try {
            PutObjectResult putResult = oss.putObject(put);
            LogUtil.print("UploadSuccess");
            LogUtil.print(putResult.getETag());
            LogUtil.print(putResult.getRequestId());
            if (observable != null) {
                observable.onNext(100);
                observable.onCompleted(serviceName);
                return;
            }
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            LogUtil.print(e.getRequestId());
            LogUtil.print(e.getErrorCode());
            LogUtil.print(e.getHostId());
            LogUtil.print(e.getRawMessage());
        }
        if (observable != null)
            observable.onError(BaseApplication.getAppContext().getString(R.string.text_upload_image_network_error));
    }
    public void uploadImage(String filename, final UploadObserver observable) {
        uploadImage("",0,filename,observable);
    }
}
