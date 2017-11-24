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

package com.taurus.playerbaselibrary.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.view.RenderSurfaceView;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/11/23.
 */

public class SecondActivity extends ToolsActivity {

    private DefaultPlayer mPlayer;

    @Override
    public void initData() {
        super.initData();

        keepScreenOn();
        mPlayer = (DefaultPlayer) findViewById(R.id.player);

        DefaultReceiverCollections receiverCollections = new DefaultReceiverCollections(this);
        receiverCollections.setDefaultPlayerControllerCover().setDefaultPlayerGestureCover().setDefaultPlayerLoadingCover().build();
        mPlayer.bindReceiverCollections(receiverCollections);

        mPlayer.setRenderViewForDecoder(new RenderSurfaceView(getApplicationContext()));
    }

    @Override
    public View getContentView(Bundle savedInstanceState) {
        return View.inflate(this,R.layout.activity_second,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy(false);
        }
    }
}
