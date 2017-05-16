package com.taurus.playerbaselibrary.ui.activity;

import android.os.Bundle;

import com.kk.taurus.baseframe.base.HolderData;
import com.kk.taurus.baseframe.ui.activity.ToolBarActivity;
import com.taurus.playerbaselibrary.holder.SettingHolder;

/**
 * Created by Taurus on 2017/3/30.
 */

public class SettingActivity extends ToolBarActivity<HolderData,SettingHolder> {
    @Override
    public SettingHolder getContentViewHolder(Bundle savedInstanceState) {
        return new SettingHolder(this);
    }

    @Override
    public void initData() {
        super.initData();
        setToolBarTitle("设置中心");
    }
}
