package com.kk.taurus.avplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.avplayer.App;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.playerbase.config.PConst;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Taurus on 2018/4/20.
 */

public class ErrorCover extends BaseCover {

    final int STATUS_ERROR = -1;
    final int STATUS_UNDEFINE = 0;
    final int STATUS_MOBILE = 1;
    final int STATUS_NETWORK_ERROR = 2;

    int mStatus = STATUS_UNDEFINE;

    @BindView(R.id.tv_error_info)
    TextView mInfo;
    @BindView(R.id.tv_retry)
    TextView mRetry;

    private boolean mErrorShow;

    private int mCurrPosition;

    private Unbinder unbinder;

    public ErrorCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();

        unbinder = ButterKnife.bind(this, getView());


    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        handleStatusUI(NetworkUtils.getNetworkState(getContext()));
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_retry})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_retry:
                handleStatus();
                break;
        }
    }

    private void handleStatus(){
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, mCurrPosition);
        switch (mStatus){
            case STATUS_ERROR:
                setErrorState(false);
                requestRetry(bundle);
                break;
            case STATUS_MOBILE:
                App.ignoreMobile = true;
                setErrorState(false);
                requestResume(bundle);
                break;
            case STATUS_NETWORK_ERROR:
                setErrorState(false);
                requestRetry(bundle);
                break;
        }
    }

    @Override
    public void onProducerData(String key, Object data) {
        super.onProducerData(key, data);
        if(DataInter.Key.KEY_NETWORK_STATE.equals(key)){
            int networkState = (int) data;
            if(networkState== PConst.NETWORK_STATE_WIFI && mErrorShow){
                Bundle bundle = BundlePool.obtain();
                bundle.putInt(EventKey.INT_DATA, mCurrPosition);
                requestRetry(bundle);
            }
            handleStatusUI(networkState);
        }
    }

    private void handleStatusUI(int networkState) {
        if(!getGroupValue().getBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true))
            return;
        if(networkState < 0){
            mStatus = STATUS_NETWORK_ERROR;
            setErrorInfo("无网络！");
            setHandleInfo("重试");
            setErrorState(true);
        }else{
            if(networkState== PConst.NETWORK_STATE_WIFI){
                if(mErrorShow){
                    setErrorState(false);
                }
            }else{
                if(App.ignoreMobile)
                    return;
                mStatus = STATUS_MOBILE;
                setErrorInfo("您正在使用移动网络！");
                setHandleInfo("继续");
                setErrorState(true);
            }
        }
    }

    private void setErrorInfo(String text){
        mInfo.setText(text);
    }

    private void setHandleInfo(String text){
        mRetry.setText(text);
    }

    private void setErrorState(boolean state){
        mErrorShow = state;
        setCoverVisibility(state?View.VISIBLE:View.GONE);
        if(!state){
            mStatus = STATUS_UNDEFINE;
        }else{
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_ERROR_SHOW, null);
        }
        getGroupValue().putBoolean(DataInter.Key.KEY_ERROR_SHOW, state);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                mCurrPosition = 0;
                handleStatusUI(NetworkUtils.getNetworkState(getContext()));
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                mCurrPosition = bundle.getInt(EventKey.INT_ARG1);
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {
        mStatus = STATUS_ERROR;
        if(!mErrorShow){
            setErrorInfo("出错了！");
            setHandleInfo("重试");
            setErrorState(true);
        }
    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_error_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelHigh(0);
    }
}
