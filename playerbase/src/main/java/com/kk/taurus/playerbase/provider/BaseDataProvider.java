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

package com.kk.taurus.playerbase.provider;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;

/**
 * Created by Taurus on 2018/4/15.
 */

public abstract class BaseDataProvider implements IDataProvider {

    private OnProviderListener mOnProviderListener;

    @Override
    public final void setOnProviderListener(OnProviderListener onProviderListener) {
        this.mOnProviderListener = onProviderListener;
    }

    /**
     * call back provider start. Recommended invocation.
     */
    protected final void onProviderDataStart(){
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderDataStart();
    }

    /**
     * send media data for player. must invocation.
     * @param bundle
     */
    @Deprecated
    protected final void onProviderMediaDataSuccess(@NonNull Bundle bundle){
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderDataSuccess(PROVIDER_CODE_SUCCESS_MEDIA_DATA, bundle);
    }

    /**
     * send media data for player. must invocation.
     * @param dataSource
     */
    protected final void onProviderMediaDataSuccess(@NonNull DataSource dataSource){
        Bundle bundle = BundlePool.obtain();
        bundle.putSerializable(EventKey.SERIALIZABLE_DATA, dataSource);
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderDataSuccess(PROVIDER_CODE_SUCCESS_MEDIA_DATA, bundle);
    }

    /**
     * send extra data, usually custom by yourself according to your need.
     * @param code
     * @param bundle
     */
    protected final void onProviderExtraDataSuccess(int code, Bundle bundle){
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderDataSuccess(code, bundle);
    }

    /**
     * when provider media data error. must invocation.
     * @param bundle
     */
    protected final void onProviderMediaDataError(@NonNull Bundle bundle){
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderError(PROVIDER_CODE_DATA_PROVIDER_ERROR, bundle);
    }

    /**
     * if occur error, It is strongly recommended to call the method.
     * @param code
     * @param bundle
     */
    protected final void onProviderError(int code, Bundle bundle){
        if(mOnProviderListener!=null)
            mOnProviderListener.onProviderError(code, bundle);
    }

}
