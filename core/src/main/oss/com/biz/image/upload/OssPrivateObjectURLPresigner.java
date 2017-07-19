package com.biz.image.upload;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.HttpUtil;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.internal.ObjectURLPresigner;

import java.net.URI;
import java.util.Calendar;

/**
 * Title: PrivateObjectURLPresigner
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/7/26  14:34
 *
 * @author wangwei
 * @version 1.0
 */
public class OssPrivateObjectURLPresigner extends ObjectURLPresigner {
    private URI endpoint;
    private OSSCredentialProvider credentialProvider;
    private ClientConfiguration conf;
    public OssPrivateObjectURLPresigner(URI endpoint, OSSCredentialProvider credentialProvider, ClientConfiguration conf) {
        super(endpoint, credentialProvider, conf);
        this.endpoint = endpoint;
        this.credentialProvider = credentialProvider;
        this.conf = conf;
    }

    public String privatePresignConstrainedURL(String bucketName, String objectKey)
            throws ClientException {

        String resource = "/" + bucketName + "/" + objectKey;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long deadline = calendar.getTimeInMillis()/ 1000 + (30*24*60*60);
        String expires = String.valueOf(deadline);
        OSSFederationToken token = null;

        if (credentialProvider instanceof OSSFederationCredentialProvider) {
            token = ((OSSFederationCredentialProvider) credentialProvider).getValidFederationToken();
            if (token == null) {
                throw new ClientException("Can not get a federation token!");
            }
            resource += "?security-token=" + token.getSecurityToken();
        } else if (credentialProvider instanceof OSSStsTokenCredentialProvider) {
            token = ((OSSStsTokenCredentialProvider) credentialProvider).getFederationToken();
            resource += "?security-token=" + token.getSecurityToken();
        }

        String contentToSign = "GET\n\n\n" + expires + "\n" + resource;
        String signature = "";

        if (credentialProvider instanceof OSSFederationCredentialProvider
                || credentialProvider instanceof OSSStsTokenCredentialProvider) {
            signature = OSSUtils.sign(token.getTempAK(), token.getTempSK(), contentToSign);
        } else if (credentialProvider instanceof OSSPlainTextAKSKCredentialProvider) {
            signature = OSSUtils.sign(((OSSPlainTextAKSKCredentialProvider) credentialProvider).getAccessKeyId(),
                    ((OSSPlainTextAKSKCredentialProvider) credentialProvider).getAccessKeySecret(), contentToSign);
        } else if (credentialProvider instanceof OSSCustomSignerCredentialProvider) {
            signature = ((OSSCustomSignerCredentialProvider) credentialProvider).signContent(contentToSign);
        } else {
            throw new ClientException("Unknown credentialProvider!");
        }

        String accessKey = signature.split(":")[0].substring(4);
        signature = signature.split(":")[1];

        String host = endpoint.getHost();
        if (!OSSUtils.isCname(host) || OSSUtils.isInCustomCnameExcludeList(host, conf.getCustomCnameExcludeList())) {
            host = bucketName + "." + host;
        }

        String url = endpoint.getScheme() + "://" + host + "/" + HttpUtil.urlEncode(objectKey, OSSConstants.DEFAULT_CHARSET_NAME)
                + "?OSSAccessKeyId=" + HttpUtil.urlEncode(accessKey, OSSConstants.DEFAULT_CHARSET_NAME)
                + "&Expires=" + expires
                + "&Signature=" + HttpUtil.urlEncode(signature, OSSConstants.DEFAULT_CHARSET_NAME);

        if (credentialProvider instanceof OSSFederationCredentialProvider
                || credentialProvider instanceof OSSStsTokenCredentialProvider) {
            url = url + "&security-token=" + HttpUtil.urlEncode(token.getSecurityToken(), OSSConstants.DEFAULT_CHARSET_NAME);
        }

        return url;
    }
}
