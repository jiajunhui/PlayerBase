package com.taurus.playerbaselibrary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.kk.taurus.baseframe.ui.activity.ToolsActivity;
import com.taurus.playerbaselibrary.R;

/**
 * Created by Taurus on 2017/3/28.
 */

public class SplashActivity extends ToolsActivity {
    @Override
    public void loadState() {

    }

    @Override
    public void initData() {
        super.initData();
        fullScreen();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public View getContentView(Bundle savedInstanceState) {
        return View.inflate(this, R.layout.activity_splash,null);
    }
}
