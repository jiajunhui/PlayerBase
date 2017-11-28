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

package com.kk.taurus.playerbase.widget.plan;

import android.content.Context;
import android.os.Bundle;

import com.kk.taurus.playerbase.callback.OnErrorListener;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.inter.IDataProvider;
import com.kk.taurus.playerbase.setting.Rate;
import com.kk.taurus.playerbase.setting.VideoData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mtime on 2017/11/17.
 */

public class MixMediaPlayer extends DelegateDecoderPlayer implements IDataProvider.OnProviderListener {

    private IDataProvider mDataProvider;
    private VideoData mProviderUseData;
    private List<Rate> mProviderGetRateList;

    public MixMediaPlayer(Context context) {
        super(context);
    }

    public void setDataProvider(IDataProvider dataProvider){
        this.mDataProvider = dataProvider;
        if(mDataProvider!=null){
            this.mDataProvider.setEventBinder(this);
            this.mDataProvider.setOnProviderListener(this);
        }
    }

    @Override
    public void setDataSource(VideoData data) {
        if(mDataProvider!=null){
            this.mDataSource = null;
            this.mProviderUseData = data;
            mDataProvider.handleSourceData(data);
        }else{
            super.setDataSource(data);
        }
    }

    @Override
    public void rePlay(int msc) {
        if(mDataSource==null){
            stop();
            setDataSource(mProviderUseData);
            start(msc);
        }else{
            super.rePlay(msc);
        }
    }

    @Override
    public void onProvideDataSource(VideoData data) {
        super.setDataSource(data);
    }

    @Override
    public void onProvideDefinitionList(List<Rate> rates) {
        this.mProviderGetRateList = rates;
        Bundle bundle = new Bundle();
        bundle.putSerializable(OnPlayerEventListener.BUNDLE_KEY_RATE_DATA, (Serializable) rates);
        onBindPlayerEvent(OnPlayerEventListener.EVENT_CODE_ON_DEFINITION_LIST_READY,bundle);
    }

    @Override
    public void onProvideError(int type, String message) {
        Bundle bundle = new Bundle();
        bundle.putInt(OnErrorListener.KEY_EXTRA,type);
        bundle.putString(OnErrorListener.KEY_MESSAGE,message);
        onBindErrorEvent(OnErrorListener.ERROR_CODE_COMMON,bundle);
    }

    @Override
    public List<Rate> getVideoDefinitions() {
        if(mDataProvider!=null && mProviderGetRateList !=null){
            return mProviderGetRateList;
        }
        return super.getVideoDefinitions();
    }

    @Override
    public Rate getCurrentDefinition() {
        if(mDataSource!=null){
            Rate rate = mDataSource.getRate();
            return rate;
        }
        return super.getCurrentDefinition();
    }
}
