package com.biz.image.upload;

import android.content.Context;

import com.biz.util.SysTimeUtil;

/**
 * Title: OssTokenEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/22  16:41
 *
 * @author wangwei
 * @version 1.0
 */
public class OssTokenEntity {
    public String accessKeyId;
    public String accessKeySecret;
    public String securityToken;
    public long expiration;

    public boolean isEffective(Context context){
        if(expiration-1000*60*10< SysTimeUtil.getSysTime(context)){
            return false;
        }
        return true;
    }
}
