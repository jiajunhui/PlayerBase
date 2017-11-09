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

package com.kk.taurus.playerbase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.config.ConfigLoader;
import com.kk.taurus.playerbase.cover.container.DefaultLevelCoverContainer;
import com.kk.taurus.playerbase.inter.ICoverContainer;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.kk.taurus.playerbase.widget.BaseSinglePlayer;

import java.io.Serializable;
import java.util.List;

/**
 *
 * Created by Taurus on 2017/3/28.
 *
 */

public class DefaultPlayer extends BasePlayer implements IDataProvider.OnProviderListener {

    private final String TAG = "DefaultPlayer";

    private IDataProvider mDataProvider;
    protected List<Rate> mRateList;
    protected VideoData mProviderData;

    public DefaultPlayer(@NonNull Context context) {
        super(context);
    }

    public DefaultPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultPlayer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setDataSource(VideoData data) {
        if(mDataProvider!=null){
            dataSource = null;
            mProviderData = data;
            mDataProvider.handleSourceData(data);
            return;
        }
        super.setDataSource(data);
    }

    public void setDataProvider(IDataProvider dataProvider){
        this.mDataProvider = dataProvider;
        this.mDataProvider.bindPlayer(this);
        this.mDataProvider.setOnProviderListener(this);
    }

    @Override
    public void onProvideDataSource(VideoData data) {
        super.setDataSource(data);
    }

    @Override
    public void onProvideDefinitionList(List<Rate> rates) {
        this.mRateList = rates;
        Bundle bundle = new Bundle();
        bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA, (Serializable) rates);
        sendEvent(OnPlayerEventListener.EVENT_CODE_ON_DEFINITION_LIST_READY,bundle);
    }

    @Override
    public void onProvideError(int type, String message) {
        Bundle bundle = new Bundle();
        bundle.putInt(OnErrorListener.KEY_EXTRA,type);
        bundle.putString(OnErrorListener.KEY_MESSAGE,message);
        onErrorEvent(OnErrorListener.ERROR_CODE_COMMON,bundle);
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(mDataProvider!=null && mRateList!=null){
            return mRateList;
        }
        return super.getVideoDefinitions();
    }

    @Override
    public Rate getCurrentDefinition() {
        if(dataSource!=null){
            Rate rate = dataSource.getRate();
            return rate;
        }
        return super.getCurrentDefinition();
    }

    @Override
    public void rePlay(int msc) {
        if(mProviderData==null){
            //当provider未取到数据时的重试
            stop();
            setDataSource(mProviderData);
            start(msc);
        }else{
            super.rePlay(msc);
        }
    }

    @Override
    protected View getPlayerWidget(Context context) {
        destroyInternalPlayer();
        mInternalPlayer = (BaseSinglePlayer) ConfigLoader.getPlayerInstance(mAppContext,getPlayerType());
        if(mInternalPlayer !=null){
            Log.d(TAG,"init player : " + mInternalPlayer.getClass().getName());
            mInternalPlayer.setDecodeMode(getDecodeMode());
            mInternalPlayer.setAspectRatio(getAspectRatio());
            mInternalPlayer.setOnErrorListener(new OnErrorListener() {
                @Override
                public void onError(int errorCode, Bundle bundle) {
                    onErrorEvent(errorCode,bundle);
                }
            });
            mInternalPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                @Override
                public void onPlayerEvent(int eventCode, Bundle bundle) {
                    DefaultPlayer.this.onPlayerEvent(eventCode,bundle);
                }
            });
            mInternalPlayer.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    onPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_PLAYER_PREPARING,null);
                }
                @Override
                public void onViewDetachedFromWindow(View v) {

                }
            });
        }else{
            return new FrameLayout(mAppContext);
        }
        return mInternalPlayer;
    }

    @Override
    protected ICoverContainer getCoverContainer(Context context) {
        return new DefaultLevelCoverContainer(context);
    }

}
