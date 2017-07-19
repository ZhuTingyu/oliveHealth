package com.biz.image.upload;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.biz.http.R;
import com.biz.http.application.HttpApplication;
import com.biz.util.LogUtil;

import android.content.Context;
import android.util.Log;

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
    private String bucketName = "kuaihe-pro";
//    private String AK = "gU5GxYCrU4Srs3kv";
    private String AK = "LTAIylsKo5EcHkY4";
//    private String SK = "I9eQmbaODqvRtJBFgjENoItiLjAyBM";
    private String SK = "rSljxfBsbmPGNZsfqjgWzSyQUgHWKS";
    private String imageEndpoint = "http://img-cn-beijing.aliyuncs.com";
    private OSSCredentialProvider credentialProvider;

    public OssManager(Context context,String bucketName) {
        this.bucketName=bucketName;
        this.context = context;
        credentialProvider = new OSSPlainTextAKSKCredentialProvider(AK, SK);
        oss = new OSSClient(context, imageEndpoint, credentialProvider);
    }

    private static int getData(double d) {
        double dd = Math.round(d * 100 + 0.5) / 100.0;
        double dd1 = Math.round(dd * 10 + 0.5) / 10.0;
        return (int) Math.round(dd1);
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
            Log.e("URL", "" + s);
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
            Log.e("URL", "" + s);
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
            } else {
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
            observable.onError(HttpApplication.getAppContext().getString(R.string.text_upload_image_network_error));
    }
}
