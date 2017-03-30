package com.taurus.playerbaselibrary.ui;

import android.graphics.Color;
import android.os.Bundle;

import com.kk.taurus.baseframe.base.HolderData;
import com.kk.taurus.baseframe.ui.activity.TopBarActivity;
import com.taurus.playerbaselibrary.holder.SettingHolder;

/**
 * Created by Taurus on 2017/3/30.
 */

public class SettingActivity extends TopBarActivity<HolderData,SettingHolder> {
    @Override
    public SettingHolder getContentViewHolder(Bundle savedInstanceState) {
        return new SettingHolder(this);
    }

    @Override
    public void initData() {
        super.initData();
        setTopBarTitle("设置中心");
        setStatusBarColor(Color.parseColor("#f83d46"));
    }
}
