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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.setting.VideoData;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/11/19.
 */

public class TestPlayerActivity extends AppCompatActivity {

    private DefaultPlayer mPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_player);

        Log.d("ConfigLoader","onCreate TestPlayerActivity");

        mPlayer = (DefaultPlayer) findViewById(R.id.player);

        VideoData data = new VideoData("http://192.168.1.3:8080/cymatics.mp4");
        mPlayer.setDataSource(data);
        mPlayer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer!=null){
            mPlayer.destroy(true);
        }
    }
}
