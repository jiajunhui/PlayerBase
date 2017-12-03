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

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.kk.taurus.uiframe.a.TitleBarActivity;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.v.BaseTitleBarHolder;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.holder.SettingHolder;

/**
 * Created by Taurus on 2017/12/3.
 */

public class SettingActivity extends TitleBarActivity<HolderData,SettingHolder> {

    @Override
    public ContentHolder onBindContentHolder() {
        return new SettingHolder(this,this);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        super.onInit(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
        }
        BaseTitleBarHolder titleBarHolder = getUserHolder().titleBarHolder;
        titleBarHolder.setTitle("设置");
    }
}
