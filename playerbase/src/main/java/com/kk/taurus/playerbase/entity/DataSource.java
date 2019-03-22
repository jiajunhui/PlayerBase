/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.kk.taurus.playerbase.entity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.kk.taurus.playerbase.provider.IDataProvider;

/**
 * Created by Taurus on 2018/3/17.
 *
 * if you have video url ,please set it to data field.{@link DataSource#data}
 *
 * if you use DataProvider{@link IDataProvider},According to yourself needs,
 * set them to the corresponding fields. For example, you need to use a long id
 * to get playback address, you can set the long id to id field{@link DataSource#id}.
 *
 */

public class DataSource implements Serializable {

    /**
     * extension field, you can use it if you need.
     */
    private String tag;

    /**
     * extension field, you can use it if you need.
     */
    private String sid;

    /**
     * Usually it's a video url.
     */
    private String data;

    /**
     * you can set video name to it.
     */
    private String title;

    /**
     * extension field, you can use it if you need.
     */
    private long id;

    /**
     * if you want set uri data,you can use this filed.
     */
    private Uri uri;

    /**
     * if you want set some data to decoder
     * or some extra data, you can set this field.
     */
    private HashMap<String, String> extra;

    /**
     * timed text source for video
     */
    private TimedTextSource timedTextSource;

    //delete 2018/11/17
//    private FileDescriptor fileDescriptor;
//
//    private AssetFileDescriptor assetFileDescriptor;

    //eg. a video folder in assets, the path name is video/xxx.mp4
    private String assetsPath;

    //when play android raw resource, set this.
    private int rawId = -1;

    /**
     * If you want to start play at a specified time,
     * please set this field.
     */
    private int startPos;

    private boolean isLive;

    public DataSource() {
    }

    public DataSource(String data) {
        this.data = data;
    }

    public DataSource(String tag, String data) {
        this.tag = tag;
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public HashMap<String, String> getExtra() {
        return extra;
    }

    public void setExtra(HashMap<String, String> extra) {
        this.extra = extra;
    }

    public TimedTextSource getTimedTextSource() {
        return timedTextSource;
    }

    public void setTimedTextSource(TimedTextSource timedTextSource) {
        this.timedTextSource = timedTextSource;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public void setAssetsPath(String assetsPath) {
        this.assetsPath = assetsPath;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public int getRawId() {
        return rawId;
    }

    public void setRawId(int rawId) {
        this.rawId = rawId;
    }

    public static Uri buildRawPath(String packageName, int rawId){
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + rawId);
    }

    public static Uri buildAssetsUri(String assetsPath){
        return Uri.parse("file:///android_asset/" + assetsPath);
    }

    public static AssetFileDescriptor getAssetsFileDescriptor(Context context, String assetsPath){
        try {
            if(TextUtils.isEmpty(assetsPath))
                return null;
            return context.getAssets().openFd(assetsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "tag='" + tag + '\'' +
                ", sid='" + sid + '\'' +
                ", data='" + data + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", uri=" + uri +
                ", extra=" + extra +
                ", timedTextSource=" + timedTextSource +
                ", assetsPath='" + assetsPath + '\'' +
                ", rawId=" + rawId +
                ", startPos=" + startPos +
                ", isLive=" + isLive +
                '}';
    }
}
