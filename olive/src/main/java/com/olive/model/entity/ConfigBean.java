package com.olive.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */
@Entity
public class ConfigBean implements Parcelable {
    @Id
    private String id;
    @Index
    private String type;
    private String cache;
    private String userId;
    private String key;
    private Long ts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.cache);
        dest.writeString(this.userId);
        dest.writeString(this.key);
        dest.writeValue(this.ts);
    }

    public ConfigBean() {
    }

    protected ConfigBean(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.cache = in.readString();
        this.userId = in.readString();
        this.key = in.readString();
        this.ts = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 236418609)
    public ConfigBean(String id, String type, String cache, String userId, String key, Long ts) {
        this.id = id;
        this.type = type;
        this.cache = cache;
        this.userId = userId;
        this.key = key;
        this.ts = ts;
    }

    public static final Parcelable.Creator<ConfigBean> CREATOR = new Parcelable.Creator<ConfigBean>() {
        @Override
        public ConfigBean createFromParcel(Parcel source) {
            return new ConfigBean(source);
        }

        @Override
        public ConfigBean[] newArray(int size) {
            return new ConfigBean[size];
        }
    };
}
