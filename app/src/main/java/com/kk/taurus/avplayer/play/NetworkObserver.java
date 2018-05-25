package com.kk.taurus.avplayer.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

public class NetworkObserver {

    public static final int NETWORK_STATE_CONNECTING = -2;
    public static final int NETWORK_STATE_NONE = -1;
    public static final int NETWORK_STATE_WIFI = 1;
    public static final int NETWORK_STATE_2G = 2;
    public static final int NETWORK_STATE_3G = 3;
    public static final int NETWORK_STATE_4G = 4;
    public static final int NETWORK_STATE_MOBILE = 5;

    private int mState;

    private static final int MSG_CODE_NETWORK_CHANGE = 100;

    private static NetworkObserver i;

    private static Context mAppContext;
    private NetChangeBroadcastReceiver mBroadcastReceiver;

    private List<OnNetworkStateChangeListener> mOnNetworkStateChangeListeners;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_NETWORK_CHANGE:
                    for(OnNetworkStateChangeListener listener:mOnNetworkStateChangeListeners){
                        int state = (int) msg.obj;
                        if(mState==state)
                            return;
                        mState = state;
                        listener.onNetworkChange(state>0, state==1, state);
                    }
                    break;
            }
        }
    };

    private NetworkObserver(){}

    private NetworkObserver(Context context){
        this.mAppContext = context.getApplicationContext();
        mOnNetworkStateChangeListeners = new ArrayList<>();
        registerNetChangeReceiver();
    }

    public static NetworkObserver get(Context context){
        if(null==i){
            synchronized (NetworkObserver.class){
                if(null==i){
                    i = new NetworkObserver(context);
                }
            }
        }
        return i;
    }

    public void addOnNetworkStateChangeListener(OnNetworkStateChangeListener listener){
        if(listener==null || mOnNetworkStateChangeListeners.contains(listener))
            return;
        mOnNetworkStateChangeListeners.add(listener);
    }

    public boolean removeNetworkStateChangeListener(OnNetworkStateChangeListener listener){
        return mOnNetworkStateChangeListeners.remove(listener);
    }

    private void registerNetChangeReceiver(){
        if(mAppContext!=null){
            mBroadcastReceiver = new NetChangeBroadcastReceiver(mHandler);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mAppContext.registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    private void unregisterNetChangeReceiver(){
        try {
            if(mAppContext!=null && mBroadcastReceiver!=null){
                mAppContext.unregisterReceiver(mBroadcastReceiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void destroy(){
        if(mBroadcastReceiver!=null)
            mBroadcastReceiver.destroy();
        unregisterNetChangeReceiver();
    }

    public interface OnNetworkStateChangeListener{
        void onNetworkChange(boolean available, boolean isWifi, int networkState);
    }

    public static class NetChangeBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;

        public NetChangeBroadcastReceiver(Handler handler){
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                handler.removeCallbacks(mDelayRunnable);
                handler.postDelayed(mDelayRunnable, 1000);
            }
        }

        private Runnable mDelayRunnable = new Runnable() {
            @Override
            public void run() {
                int networkState = getNetworkState(mAppContext);
                Message message = Message.obtain();
                message.what = MSG_CODE_NETWORK_CHANGE;
                message.obj = networkState;
                handler.sendMessage(message);
            }
        };

        public void destroy(){
            handler.removeCallbacks(mDelayRunnable);
        }
    }

    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return NETWORK_STATE_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NETWORK_STATE_NONE;
        }else {
            NetworkInfo.State networkInfoState = networkInfo.getState();
            if(networkInfoState == NetworkInfo.State.CONNECTING){
                return NETWORK_STATE_CONNECTING;
            }
            if(!networkInfo.isAvailable()){
                return NETWORK_STATE_NONE;
            }
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_STATE_WIFI;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            /*
             GPRS : 2G(2.5) General Packet Radia Service 114kbps
             EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
             UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
             CDMA : 2G 电信 Code Division Multiple Access 码分多址
             EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
             EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
             1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
             HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
             HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
             HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
             IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
             EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
             LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
             EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
             HSPAP : 3G HSPAP 比 HSDPA 快些
             */
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_STATE_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_STATE_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_STATE_4G;
            default:
                return NETWORK_STATE_MOBILE;
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context context
     * @return true/false
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否wifi连接
     *
     * @param context context
     * @return true/false
     */
    public static synchronized boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                int networkInfoType = networkInfo.getType();
                if (networkInfoType == ConnectivityManager.TYPE_WIFI || networkInfoType == ConnectivityManager.TYPE_ETHERNET) {
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }

}
