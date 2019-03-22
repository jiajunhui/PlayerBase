package com.kk.taurus.avplayer.cover;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.receiver.PlayerStateGetter;
import com.kk.taurus.playerbase.touch.OnTouchGestureListener;
import com.kk.taurus.playerbase.utils.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GestureCover extends BaseCover implements OnTouchGestureListener {

    @BindView(R.id.cover_player_gesture_operation_volume_box)
    View mVolumeBox;
    @BindView(R.id.cover_player_gesture_operation_brightness_box)
    View mBrightnessBox;
    @BindView(R.id.cover_player_gesture_operation_volume_icon)
    ImageView mVolumeIcon;
    @BindView(R.id.cover_player_gesture_operation_volume_text)
    TextView mVolumeText;
    @BindView(R.id.cover_player_gesture_operation_brightness_text)
    TextView mBrightnessText;
    @BindView(R.id.cover_player_gesture_operation_fast_forward_box)
    View mFastForwardBox;
    @BindView(R.id.cover_player_gesture_operation_fast_forward_text_view_step_time)
    TextView mFastForwardStepTime;
    @BindView(R.id.cover_player_gesture_operation_fast_forward_text_view_progress_time)
    TextView mFastForwardProgressTime;

    private boolean firstTouch;

    private int mSeekProgress = -1;

    private int mWidth,mHeight;
    private long newPosition;

    private boolean mHorizontalSlide;
    private float brightness = -1;
    private int volume;
    private AudioManager audioManager;
    private int mMaxVolume;

    private boolean mGestureEnable = true;

    private Bundle mBundle;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

            }
        }
    };
    private boolean horizontalSlide;
    private boolean rightVerticalSlide;
    private Unbinder unbinder;

    public GestureCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        unbinder = ButterKnife.bind(this, getView());

        mBundle = new Bundle();
        initAudioManager(getContext());

    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        unbinder.unbind();
    }

    private void initAudioManager(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    private void sendSeekEvent(int progress){
        getGroupValue().putBoolean(DataInter.Key.KEY_TIMER_UPDATE_ENABLE, false);
        mSeekProgress = progress;
        mHandler.removeCallbacks(mSeekEventRunnable);
        mHandler.postDelayed(mSeekEventRunnable, 300);
    }

    private Runnable mSeekEventRunnable = new Runnable() {
        @Override
        public void run() {
            if(mSeekProgress < 0)
                return;
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_DATA, mSeekProgress);
            requestSeek(bundle);
        }
    };

    private IReceiverGroup.OnGroupValueUpdateListener mOnGroupValueUpdateListener =
            new IReceiverGroup.OnGroupValueUpdateListener() {
                @Override
                public String[] filterKeys() {
                    return new String[]{
                            DataInter.Key.KEY_COMPLETE_SHOW,
                            DataInter.Key.KEY_IS_LANDSCAPE
                    };
                }

                @Override
                public void onValueUpdate(String key, Object value) {
                    if(DataInter.Key.KEY_COMPLETE_SHOW.equals(key)){
                        setGestureEnable(!(boolean) value);
                    }else if(DataInter.Key.KEY_IS_LANDSCAPE.equals(key)){
                        notifyWH();
                    }
                }
            };

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        getGroupValue().registerOnGroupValueUpdateListener(mOnGroupValueUpdateListener);
        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                notifyWH();
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void notifyWH() {
        mWidth = getView().getWidth();
        mHeight = getView().getHeight();
    }

    @Override
    protected void onCoverDetachedToWindow() {
        super.onCoverDetachedToWindow();
        getGroupValue().unregisterOnGroupValueUpdateListener(mOnGroupValueUpdateListener);
    }

    public void setVolumeBoxState(boolean state) {
        if(mVolumeBox!=null){
            mVolumeBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    public void setVolumeIcon(int resId) {
        if(mVolumeIcon!=null){
            mVolumeIcon.setImageResource(resId);
        }
    }

    public void setVolumeText(String text) {
        if(mVolumeText!=null){
            mVolumeText.setText(text);
        }
    }

    public void setBrightnessBoxState(boolean state) {
        if(mBrightnessBox!=null){
            mBrightnessBox.setVisibility(state?View.VISIBLE:View.GONE);
        }
    }

    public void setBrightnessText(String text) {
        if(mBrightnessText!=null){
            mBrightnessText.setText(text);
        }
    }

    private void setFastForwardState(boolean state) {
        mFastForwardBox.setVisibility(state?View.VISIBLE:View.GONE);
    }

    private void setFastForwardStepTime(String text) {
        mFastForwardStepTime.setText(text);
    }

    private void setFastForwardProgressTime(String text) {
        mFastForwardProgressTime.setText(text);
    }

    public void setGestureEnable(boolean gestureEnable) {
        this.mGestureEnable = gestureEnable;
    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_gesture_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelLow(0);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                setGestureEnable(true);
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onSingleTapUp(MotionEvent event) {

    }

    @Override
    public void onDoubleTap(MotionEvent event) {

    }

    @Override
    public void onDown(MotionEvent event) {
        mHorizontalSlide = false;
        firstTouch = true;
        volume = getVolume();
    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(!mGestureEnable)
            return;
        float mOldX = e1.getX(), mOldY = e1.getY();
        float deltaY = mOldY - e2.getY();
        float deltaX = mOldX - e2.getX();
        if (firstTouch) {
            horizontalSlide = Math.abs(distanceX) >= Math.abs(distanceY);
            rightVerticalSlide = mOldX > mWidth * 0.5f;
            firstTouch = false;
        }

        if(horizontalSlide){
            onHorizontalSlide(-deltaX / mWidth);
        }else{
            if(Math.abs(deltaY) > mHeight)
                return;
            if(rightVerticalSlide){
                onRightVerticalSlide(deltaY / mHeight);
            }else{
                onLeftVerticalSlide(deltaY / mHeight);
            }
        }
    }

    private int getDuration(){
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        return playerStateGetter==null?0:playerStateGetter.getDuration();
    }

    private int getCurrentPosition(){
        PlayerStateGetter playerStateGetter = getPlayerStateGetter();
        return playerStateGetter==null?0:playerStateGetter.getCurrentPosition();
    }

    private void onHorizontalSlide(float percent){
        if(getDuration() <= 0)
            return;
        mHorizontalSlide = true;
        if(getGroupValue().getBoolean(DataInter.Key.KEY_TIMER_UPDATE_ENABLE)){
            getGroupValue().putBoolean(DataInter.Key.KEY_TIMER_UPDATE_ENABLE, false);
        }
        long position = getCurrentPosition();
        long duration = getDuration();
        long deltaMax = Math.min(getDuration()/2, duration - position);
        long delta = (long) (deltaMax * percent);
        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta=-position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            mBundle.putInt(EventKey.INT_ARG1, (int) newPosition);
            mBundle.putInt(EventKey.INT_ARG2, (int) duration);
            notifyReceiverPrivateEvent(
                    DataInter.ReceiverKey.KEY_CONTROLLER_COVER,
                    DataInter.PrivateEvent.EVENT_CODE_UPDATE_SEEK,
                    mBundle);
            setFastForwardState(true);
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
            setFastForwardStepTime(text + "s");
            String progressText = TimeUtil.getTimeSmartFormat(newPosition)+"/" + TimeUtil.getTimeSmartFormat(duration);
            setFastForwardProgressTime(progressText);
        }
    }

    private void onRightVerticalSlide(float percent){
        mHorizontalSlide = false;
        int index = (int) (percent * mMaxVolume) + volume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;
        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        // 变更进度条
        int i = (int) (index * 1.0 / mMaxVolume * 100);
        String s = i + "%";
        if (i == 0) {
            s = "OFF";
        }
        // 显示
        setVolumeIcon(i==0?R.mipmap.ic_volume_off_white: R.mipmap.ic_volume_up_white);
        setBrightnessBoxState(false);
        setFastForwardState(false);
        setVolumeBoxState(true);
        setVolumeText(s);
    }

    private void onLeftVerticalSlide(float percent){
        mHorizontalSlide = false;
        Activity activity = getActivity();
        if(activity==null)
            return;
        if (brightness < 0) {
            brightness = activity.getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f){
                brightness = 0.50f;
            }else if (brightness < 0.01f){
                brightness = 0.01f;
            }
        }
        setVolumeBoxState(false);
        setFastForwardState(false);
        setBrightnessBoxState(true);
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f){
            lpa.screenBrightness = 1.0f;
        }else if (lpa.screenBrightness < 0.01f){
            lpa.screenBrightness = 0.01f;
        }
        setBrightnessText(((int) (lpa.screenBrightness * 100))+"%");
        activity.getWindow().setAttributes(lpa);
    }

    private Activity getActivity(){
        Context context = getContext();
        if(context instanceof Activity){
            return (Activity) context;
        }
        return null;
    }

    private int getVolume(){
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (volume < 0)
            volume = 0;
        return volume;
    }

    @Override
    public void onEndGesture() {
        volume = -1;
        brightness = -1f;
        setVolumeBoxState(false);
        setBrightnessBoxState(false);
        setFastForwardState(false);
        if(newPosition >= 0 && mHorizontalSlide){
            sendSeekEvent((int) newPosition);
            newPosition = 0;
        }else{
            getGroupValue().putBoolean(DataInter.Key.KEY_TIMER_UPDATE_ENABLE, true);
        }
        mHorizontalSlide = false;
    }
}
