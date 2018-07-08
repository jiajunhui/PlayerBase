package com.kk.taurus.avplayer.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

public class WindowPermissionCheck {

    public static boolean checkPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(activity)) {
            Toast.makeText(activity, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            activity.startActivityForResult(
                    new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName())), 0);
            return false;
        }
        return true;
    }

    public static void onActivityResult(Activity activity,
                                        int requestCode,
                                        int resultCode,
                                        Intent data,
                                        OnWindowPermissionListener onWindowPermissionListener) {
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(activity)) {
                Toast.makeText(activity.getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
                if(onWindowPermissionListener!=null)
                    onWindowPermissionListener.onFailure();
            }else {
                Toast.makeText(activity.getApplicationContext(), "授权成功", Toast.LENGTH_SHORT).show();
                if(onWindowPermissionListener!=null)
                    onWindowPermissionListener.onSuccess();
            }
        }
    }

    public interface OnWindowPermissionListener{
        void onSuccess();
        void onFailure();
    }

}
