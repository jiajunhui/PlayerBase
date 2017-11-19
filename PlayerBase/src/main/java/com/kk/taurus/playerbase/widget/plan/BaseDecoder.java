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
import com.kk.taurus.playerbase.setting.DecodeMode;

/**
 * Created by mtime on 2017/11/17.
 */

public abstract class BaseDecoder implements IDecoder {

    protected Context mContext;
    protected int mStatus = STATUS_IDLE;
    protected int mTargetStatus = STATUS_IDLE;
    private OnErrorListener mOnErrorListener;
    private OnPlayerEventListener mOnPlayerEventListener;
    protected int startSeekPos;
    private DecodeMode mDecodeMode;

    public BaseDecoder(Context context){
        this.mContext = context;
    }

    public void setDecodeMode(DecodeMode mDecodeMode){
        this.mDecodeMode = mDecodeMode;
    }

    public DecodeMode getDecodeMode() {
        return mDecodeMode;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener){
        this.mOnErrorListener = onErrorListener;
    }

    public void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener){
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    protected void onErrorEvent(int eventCode, Bundle bundle){
        if(mOnErrorListener!=null){
            mOnErrorListener.onError(eventCode,bundle);
        }
    }

    protected void onPlayerEvent(int eventCode, Bundle bundle){
        if(mOnPlayerEventListener!=null){
            mOnPlayerEventListener.onPlayerEvent(eventCode,bundle);
        }
    }

    public int getStatus() {
        return mStatus;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void destroy() {
        mOnErrorListener = null;
        mOnPlayerEventListener = null;
    }

}
