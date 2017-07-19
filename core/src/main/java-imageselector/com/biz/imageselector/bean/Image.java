package com.biz.imageselector.bean;

/**
 * Title: Image
 * Description: 图片实体
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/7/27  下午2:36
 *
 * @author dengqinsheng
 * @version 1.0
 */

public class Image {
    public String path;
    public String name;
    public long time;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Image other = (Image) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
