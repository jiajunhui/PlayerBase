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

package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.listener.OnHolderListener;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/12/2.
 */

public class InputUrlHolder extends ContentHolder<HolderData> {

    public static final int INPUT_EVENT_CODE_PLAY = 100;
    private EditText mEtUrl;
    private Button mPlay;

    public InputUrlHolder(Context context) {
        super(context);
    }

    public InputUrlHolder(Context context, OnHolderListener onHolderListener) {
        super(context, onHolderListener);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.fragment_input_url);
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();
        mEtUrl = getViewById(R.id.et_url);
        mPlay = getViewById(R.id.btn_play);

        mPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_play:
                handlePlay();
                break;
        }
    }

    private void handlePlay() {
        String url = mEtUrl.getText().toString();
        if(TextUtils.isEmpty(url))
            return;
        Bundle bundle = new Bundle();
        bundle.putString(OnHolderListener.KEY_STRING_DATA,url);
        onHolderEvent(INPUT_EVENT_CODE_PLAY,bundle);
    }
}
