package com.biz.share.sina;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import android.content.Context;
import android.text.TextUtils;

public class SendSinaPostApi extends AbsOpenAPI {
	public final static String AppSendUrl = API_SERVER
			+ "/statuses/upload_url_text.json";
	public final static String AppSend = API_SERVER + "/statuses/update.json";
	public final static String AppToShortUrl = API_SERVER + "/short_url/shorten.json";

	public SendSinaPostApi(Context context, String appKey, Oauth2AccessToken accessToken) {
		super(context,appKey,accessToken);
	}

	// http://open.weibo.com/wiki/2/statuses/upload_url_text
	public void sendMessage(String str, String url, double lat, double lon,
							RequestListener listener) {
		if (!TextUtils.isEmpty(url)) {
			WeiboParameters params = new WeiboParameters(mAppKey);
			params.put("status", str);
			params.put("visible",0);
			params.put("url", url);
			params.put("lat", lat);
			params.put("long", lon);
			requestAsync(AppSendUrl, params, HTTPMETHOD_POST, listener);
		}else{
			sendMessage(str,lat, lon,listener);
		}
	}

	private void sendMessage(String str, double lat, double lon,
							 RequestListener listener) {
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("status", str);
		params.put("visible", 0);
		params.put("lat", lat);
		params.put("long", lon);
		requestAsync(AppSend, params, HTTPMETHOD_POST, listener);
	}
	public void toShortUrl(String url, RequestListener listener)
	{
		WeiboParameters params = new WeiboParameters(mAppKey);
		params.put("url_long", url);
		requestAsync(AppToShortUrl, params, HTTPMETHOD_GET, listener);
	}
}
