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

import com.danikula.videocache.HttpProxyCacheServer;

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

    private HttpProxyCacheServer proxy;
    private boolean videoCacheOpen;

    public boolean isVideoCacheOpen() {
        return videoCacheOpen;
    }

    public void setVideoCacheState(boolean open){
        this.videoCacheOpen = open;
    }

    public String proxyVideoUrl(Context context, String sourceUrl){
        if(!videoCacheOpen)
            return sourceUrl;
        HttpProxyCacheServer proxy = getProxy(context);
        return proxy.getProxyUrl(sourceUrl);
    }


    private HttpProxyCacheServer getProxy(Context context) {
        if(proxy==null){
            proxy = newProxy(context);
        }
        return proxy;
    }

    public void initHttpProxyCacheServer(HttpProxyCacheServer.Builder builder){
        if(builder!=null){
            proxy = builder.build();
        }
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer(context);
    }

}
