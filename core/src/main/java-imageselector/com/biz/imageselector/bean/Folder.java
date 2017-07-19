package com.biz.imageselector.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Title: Folder
 * Description: 文件夹实体
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/7/27  下午2:35
 *
 * @author dengqinsheng
 * @version 1.0
 */

public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return TextUtils.equals(other.path, path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
