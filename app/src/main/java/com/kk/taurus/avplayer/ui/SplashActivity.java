package com.kk.taurus.avplayer.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kk.taurus.avplayer.R;

import com.kk.taurus.avplayer.HomeActivity;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Taurus on 2018/4/19.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                PermissionGen.with(SplashActivity.this)
                        .addRequestCode(100)
                        .permissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        .request();
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionSuccess() {
        intentToMainPage();
    }

    @PermissionFail(requestCode = 100)
    public void permissionFailure(){
        Toast.makeText(this, "权限拒绝,无法正常使用", Toast.LENGTH_LONG).show();

    }

    private void intentToMainPage() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
