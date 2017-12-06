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

package com.kk.taurus.playerbase.config;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.kk.taurus.playerbase.inter.CacheFileNameGenerator;

import java.io.File;

/**
 * Created by Taurus on 2017/12/3.
 */

public class VideoCacheProxy {

    private static VideoCacheProxy instance;

    private VideoCacheProxy(){}

    public static VideoCacheProxy get(){
        if(null==instance){
            synchronized (VideoCacheProxy.class){
                if(null==instance){
                    instance = new VideoCacheProxy();
                }
            }
        }
        return instance;
    }

    private boolean videoCacheOpen;

    private boolean isNetSource(String sourceUrl){
        if(TextUtils.isEmpty(sourceUrl)){
            return false;
        }
        sourceUrl = sourceUrl.trim();
        Uri uri = Uri.parse(sourceUrl);
        String scheme = uri.getScheme();
        if(TextUtils.isEmpty(scheme) || scheme.equalsIgnoreCase("file"))
            return false;
        if(sourceUrl.startsWith("http://")||sourceUrl.startsWith("https://"))
            return true;
        return false;
    }

    public boolean isVideoCacheOpen() {
        return videoCacheOpen;
    }

    public void setVideoCacheState(boolean open){
        this.videoCacheOpen = open;
    }

    public String proxyVideoUrl(Context context, String sourceUrl){
        if(!videoCacheOpen || !isNetSource(sourceUrl))
            return sourceUrl;
        return VideoCacheLibraryLoader.getProxyUrl(context, sourceUrl);
    }

    public void initHttpProxyCacheServer(VideoCacheProxy.Builder builder){
        if(builder!=null){
            VideoCacheLibraryLoader.initHttpProxyCacheServer(builder);
        }
    }

    public static final class Builder{

        private Context context;
        private File cacheDirectory;
        private long maxCacheSize;
        private int maxCacheCount;
        private CacheFileNameGenerator fileNameGenerator;

        public Builder(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public File getCacheDirectory() {
            return cacheDirectory;
        }

        public Builder setCacheDirectory(File cacheDirectory) {
            this.cacheDirectory = cacheDirectory;
            return this;
        }

        public long getMaxCacheSize() {
            return maxCacheSize;
        }

        public Builder setMaxCacheSize(long maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public int getMaxCacheCount() {
            return maxCacheCount;
        }

        public Builder setMaxCacheCount(int maxCacheCount) {
            this.maxCacheCount = maxCacheCount;
            return this;
        }

        public CacheFileNameGenerator getFileNameGenerator() {
            return fileNameGenerator;
        }

        public Builder setFileNameGenerator(CacheFileNameGenerator fileNameGenerator) {
            this.fileNameGenerator = fileNameGenerator;
            return this;
        }
    }

}
