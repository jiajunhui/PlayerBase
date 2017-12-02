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

package com.taurus.playerbaselibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.uiframe.f.StateFragment;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.listener.OnHolderListener;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.holder.InputUrlHolder;
import com.taurus.playerbaselibrary.ui.activity.PlayerActivity;

/**
 * Created by Taurus on 2017/12/2.
 */

public class InputUrlFragment extends StateFragment<HolderData,InputUrlHolder> {

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);

    }

    @Override
    public void onHolderEvent(int eventCode, Bundle bundle) {
        super.onHolderEvent(eventCode, bundle);
        switch (eventCode){
            case InputUrlHolder.INPUT_EVENT_CODE_PLAY:
                String url = bundle.getString(OnHolderListener.KEY_STRING_DATA);
                VideoItem item = new VideoItem();
                item.setDisplayName(url);
                item.setPath(url);
                Intent intent = new Intent(getApplicationContext(),PlayerActivity.class);
                intent.putExtra("data",item);
                startActivity(intent);
                break;
        }
    }

    @Override
    public ContentHolder onBindContentHolder() {
        return new InputUrlHolder(mContext,this);
    }
}
