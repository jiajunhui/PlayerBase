package com.kk.taurus.avplayer.cover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.player.OnTimerUpdateListener;
import com.kk.taurus.playerbase.receiver.IReceiverGroup;
import com.kk.taurus.playerbase.touch.OnTouchGestureListener;
import com.kk.taurus.playerbase.receiver.BaseCover;
import com.kk.taurus.playerbase.utils.TimeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Taurus on 2018/4/15.
 */

public class ControllerCover extends BaseCover implements OnTimerUpdateListener, OnTouchGestureListener{

    private final int MSG_CODE_DELAY_HIDDEN_CONTROLLER = 101;

    @BindView(R.id.cover_player_controller_top_container)
    View mTopContainer;
    @BindView(R.id.cover_player_controller_bottom_container)
    View mBottomContainer;
    @BindView(R.id.cover_player_controller_image_view_back_icon)
    ImageView mBackIcon;
    @BindView(R.id.cover_player_controller_text_view_video_title)
    TextView mTopTitle;
    @BindView(R.id.cover_player_controller_image_view_play_state)
    ImageView mStateIcon;
    @BindView(R.id.cover_player_controller_text_view_curr_time)
    TextView mCurrTime;
    @BindView(R.id.cover_player_controller_text_view_total_time)
    TextView mTotalTime;
    @BindView(R.id.cover_player_controller_image_view_switch_screen)
    ImageView mSwitchScreen;
    @BindView(R.id.cover_player_controller_seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.cover_bottom_seek_bar)
    SeekBar mBottomSeekBar;

    private int mBufferPercentage;

    private int mSeekProgress = -1;

    private boolean mTimerUpdateProgressEnable = true;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_DELAY_HIDDEN_CONTROLLER:
                    PLog.d(getTag().toString(), "msg_delay_hidden...");
                    setControllerState(false);
                    break;
            }
        }
    };

    private boolean mGestureEnable = true;

    private String mTimeFormat;

    private boolean mControllerTopEnable;
    private Unbinder unbinder;
    private ObjectAnimator mBottomAnimator;
    private ObjectAnimator mTopAnimator;

    public ControllerCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        unbinder = ButterKnife.bind(this, getView());

        mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        getGroupValue().registerOnGroupValueUpdateListener(mOnGroupValueUpdateListener);

    }

    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        DataSource dataSource = getGroupValue().get(DataInter.Key.KEY_DATA_SOURCE);
        setTitle(dataSource);

        boolean topEnable = getGroupValue().getBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
        mControllerTopEnable = topEnable;
        if(!topEnable){
            setTopContainerState(false);
        }

        boolean screenSwitchEnable = getGroupValue().getBoolean(DataInter.Key.KEY_CONTROLLER_SCREEN_SWITCH_ENABLE, true);
        setScreenSwitchEnable(screenSwitchEnable);
    }

    @Override
    protected void onCoverDetachedToWindow() {
        super.onCoverDetachedToWindow();
        mTopContainer.setVisibility(View.GONE);
        mBottomContainer.setVisibility(View.GONE);
        removeDelayHiddenMessage();
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();

        cancelTopAnimation();
        cancelBottomAnimation();

        getGroupValue().unregisterOnGroupValueUpdateListener(mOnGroupValueUpdateListener);
        removeDelayHiddenMessage();
        mHandler.removeCallbacks(mSeekEventRunnable);

        unbinder.unbind();

    }

    @OnClick({
            R.id.cover_player_controller_image_view_back_icon,
            R.id.cover_player_controller_image_view_play_state,
            R.id.cover_player_controller_image_view_switch_screen})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.cover_player_controller_image_view_back_icon:
                notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_BACK, null);
                break;
            case R.id.cover_player_controller_image_view_play_state:
                boolean selected = mStateIcon.isSelected();
                if(selected){
                    requestResume(null);
                }else{
                    requestPause(null);
                }
                mStateIcon.setSelected(!selected);
                break;
            case R.id.cover_player_controller_image_view_switch_screen:
                notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN, null);
                break;
        }
    }

    private IReceiverGroup.OnGroupValueUpdateListener mOnGroupValueUpdateListener =
            new IReceiverGroup.OnGroupValueUpdateListener() {
        @Override
        public String[] filterKeys() {
            return new String[]{
                    DataInter.Key.KEY_COMPLETE_SHOW,
                    DataInter.Key.KEY_TIMER_UPDATE_ENABLE,
                    DataInter.Key.KEY_DATA_SOURCE,
                    DataInter.Key.KEY_IS_LANDSCAPE,
                    DataInter.Key.KEY_CONTROLLER_TOP_ENABLE};
        }

        @Override
        public void onValueUpdate(String key, Object value) {
            if(key.equals(DataInter.Key.KEY_COMPLETE_SHOW)){
                boolean show = (boolean) value;
                if(show){
                    setControllerState(false);
                }
                setGestureEnable(!show);
            }else if(key.equals(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE)){
                mControllerTopEnable = (boolean) value;
                if(!mControllerTopEnable){
                    setTopContainerState(false);
                }
            }else if(key.equals(DataInter.Key.KEY_IS_LANDSCAPE)){
                setSwitchScreenIcon((Boolean) value);
            }else if(key.equals(DataInter.Key.KEY_TIMER_UPDATE_ENABLE)){
                mTimerUpdateProgressEnable = (boolean) value;
            }else if(key.equals(DataInter.Key.KEY_DATA_SOURCE)){
                DataSource dataSource = (DataSource) value;
                setTitle(dataSource);
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser)
                updateUI(progress, seekBar.getMax());
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            sendSeekEvent(seekBar.getProgress());
        }
    };

    private void sendSeekEvent(int progress){
        mTimerUpdateProgressEnable = false;
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

    private void setTitle(DataSource dataSource){
        if(dataSource!=null){
            String title = dataSource.getTitle();
            if(!TextUtils.isEmpty(title)){
                setTitle(title);
                return;
            }
            String data = dataSource.getData();
            if(!TextUtils.isEmpty(data)){
                setTitle(data);
            }
        }
    }

    private void setTitle(String text){
        mTopTitle.setText(text);
    }

    private void setSwitchScreenIcon(boolean isFullScreen){
        mSwitchScreen.setImageResource(isFullScreen?R.mipmap.icon_exit_full_screen:R.mipmap.icon_full_screen);
    }

    private void setScreenSwitchEnable(boolean screenSwitchEnable) {
        mSwitchScreen.setVisibility(screenSwitchEnable?View.VISIBLE:View.GONE);
    }

    private void setBottomSeekBarState(boolean state){
        mBottomSeekBar.setVisibility(state?View.VISIBLE:View.GONE);
    }

    private void setGestureEnable(boolean gestureEnable) {
        this.mGestureEnable = gestureEnable;
    }

    private void cancelTopAnimation(){
        if(mTopAnimator!=null){
            mTopAnimator.cancel();
            mTopAnimator.removeAllListeners();
            mTopAnimator.removeAllUpdateListeners();
        }
    }

    private void setTopContainerState(final boolean state){
        if(mControllerTopEnable){
            mTopContainer.clearAnimation();
            cancelTopAnimation();
            mTopAnimator = ObjectAnimator.ofFloat(mTopContainer,
                            "alpha", state ? 0 : 1, state ? 1 : 0).setDuration(300);
            mTopAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if(state){
                        mTopContainer.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(!state){
                        mTopContainer.setVisibility(View.GONE);
                    }
                }
            });
            mTopAnimator.start();
        }else{
            mTopContainer.setVisibility(View.GONE);
        }
    }

    private void cancelBottomAnimation(){
        if(mBottomAnimator!=null){
            mBottomAnimator.cancel();
            mBottomAnimator.removeAllListeners();
            mBottomAnimator.removeAllUpdateListeners();
        }
    }

    private void setBottomContainerState(final boolean state){
        mBottomContainer.clearAnimation();
        cancelBottomAnimation();
        mBottomAnimator = ObjectAnimator.ofFloat(mBottomContainer,
                "alpha", state ? 0 : 1, state ? 1 : 0).setDuration(300);
        mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if(state){
                    mBottomContainer.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(!state){
                    mBottomContainer.setVisibility(View.GONE);
                }
            }
        });
        mBottomAnimator.start();
        setBottomSeekBarState(!state);
    }

    private void setControllerState(boolean state){
        if(state){
            sendDelayHiddenMessage();
        }else{
            removeDelayHiddenMessage();
        }
        setTopContainerState(state);
        setBottomContainerState(state);
    }

    private boolean isControllerShow(){
        return mBottomContainer.getVisibility()==View.VISIBLE;
    }

    private void toggleController(){
        if(isControllerShow()){
            setControllerState(false);
        }else{
            setControllerState(true);
        }
    }

    private void sendDelayHiddenMessage(){
        removeDelayHiddenMessage();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_DELAY_HIDDEN_CONTROLLER, 5000);
    }

    private void removeDelayHiddenMessage(){
        mHandler.removeMessages(MSG_CODE_DELAY_HIDDEN_CONTROLLER);
    }

    private void setCurrTime(int curr){
        mCurrTime.setText(TimeUtil.getTime(mTimeFormat, curr));
    }

    private void setTotalTime(int duration){
        mTotalTime.setText(TimeUtil.getTime(mTimeFormat, duration));
    }

    private void setSeekProgress(int curr, int duration){
        mSeekBar.setMax(duration);
        mSeekBar.setProgress(curr);
        float secondProgress = mBufferPercentage * 1.0f/100 * duration;
        setSecondProgress((int) secondProgress);
    }

    private void setSecondProgress(int secondProgress){
        mSeekBar.setSecondaryProgress(secondProgress);
    }

    private void setBottomSeekProgress(int curr, int duration){
        mBottomSeekBar.setMax(duration);
        mBottomSeekBar.setProgress(curr);
        float secondProgress = mBufferPercentage * 1.0f/100 * duration;
        mBottomSeekBar.setSecondaryProgress((int) secondProgress);
    }

    @Override
    public void onTimerUpdate(int curr, int duration, int bufferPercentage) {
        if(!mTimerUpdateProgressEnable)
            return;
        if(mTimeFormat==null || duration != mSeekBar.getMax()){
            mTimeFormat = TimeUtil.getFormat(duration);
        }
        mBufferPercentage = bufferPercentage;
        updateUI(curr, duration);
    }

    private void updateUI(int curr, int duration) {
        setSeekProgress(curr, duration);
        setBottomSeekProgress(curr, duration);
        setCurrTime(curr);
        setTotalTime(duration);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                mBufferPercentage = 0;
                mTimeFormat = null;
                updateUI(0, 0);
                setBottomSeekBarState(true);
                DataSource data = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                getGroupValue().putObject(DataInter.Key.KEY_DATA_SOURCE, data);
                setTitle(data);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                int status = bundle.getInt(EventKey.INT_DATA);
                if(status== IPlayer.STATE_PAUSED){
                    mStateIcon.setSelected(true);
                }else if(status==IPlayer.STATE_STARTED){
                    mStateIcon.setSelected(false);
                }
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                mTimerUpdateProgressEnable = true;
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
    public Bundle onPrivateEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case DataInter.PrivateEvent.EVENT_CODE_UPDATE_SEEK:
                if(bundle!=null){
                    int curr = bundle.getInt(EventKey.INT_ARG1);
                    int duration = bundle.getInt(EventKey.INT_ARG2);
                    updateUI(curr, duration);
                }
                break;
        }
        return null;
    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_controller_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelLow(1);
    }

    @Override
    public void onSingleTapUp(MotionEvent event) {
        if(!mGestureEnable)
            return;
        toggleController();
    }

    @Override
    public void onDoubleTap(MotionEvent event) {
    }

    @Override
    public void onDown(MotionEvent event) {
    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(!mGestureEnable)
            return;
    }

    @Override
    public void onEndGesture() {
    }
}
