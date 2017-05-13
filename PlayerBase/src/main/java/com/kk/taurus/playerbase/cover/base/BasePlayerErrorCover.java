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

package com.kk.taurus.playerbase.cover.base;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.playerbase.R;
import com.kk.taurus.playerbase.inter.IErrorCover;

/**
 * Created by Taurus on 2017/3/27.
 */

public abstract class BasePlayerErrorCover extends BaseCover implements IErrorCover{

    public static final String KEY = "error_cover";
    protected View mErrorView;
    protected ImageView mIvErrorIcon;
    protected TextView mTvTipText;

    public BasePlayerErrorCover(Context context){
        super(context);
    }

    public BasePlayerErrorCover(Context context, BaseCoverObserver coverObserver) {
        super(context, coverObserver);
    }

    @Override
    protected void findView() {
        mErrorView = findViewById(R.id.cover_player_error_state_view_box);
        mIvErrorIcon = findViewById(R.id.cover_player_error_state_image_view_icon);
        mTvTipText = findViewById(R.id.cover_player_error_state_text_view_tip);
    }

    @Override
    public void setErrorState(boolean state) {
        setCoverVisibility(state? View.VISIBLE:View.GONE);
    }

    @Override
    public void setImageIcon(int resId) {
        if(mIvErrorIcon!=null){
            mIvErrorIcon.setImageResource(resId);
        }
    }

    @Override
    public void setErrorTipText(String text) {
        if(mTvTipText!=null){
            mTvTipText.setText(text);
        }
    }

}
