package com.kk.taurus.avplayer.cover;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.play.DataInter;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.BundlePool;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.player.OnTimerUpdateListener;
import com.kk.taurus.playerbase.utils.TimeUtil;

public class TvControllerCover extends BaseKeyEventCover implements OnTimerUpdateListener {
    private static final String TAG = "TvControllerCover";
    private TextView mTopTitle, mCurrTime, mTotalTime;
    private ImageView mStateIcon;
    private SeekBar mSeekBar, mBottomSeekBar;
    private View mBottomContainer;

    private int mBufferPercentage;

    private int mSeekProgress = -1;
    private String mTimeFormat;

    private boolean mTimerUpdateProgressEnable = false;

    private Handler mHandler = new Handler();
    public TvControllerCover(Context context) {
        super(context);
    }

    @Override
    protected View onCreateCoverView(Context context) {
        View view = View.inflate(context, R.layout.layout_controller_cover, null);
        mTopTitle = view.findViewById(R.id.cover_player_controller_text_view_video_title);
        mStateIcon = view.findViewById(R.id.cover_player_controller_image_view_play_state);
        mCurrTime = view.findViewById(R.id.cover_player_controller_text_view_curr_time);
        mTotalTime = view.findViewById(R.id.cover_player_controller_text_view_total_time);
        mSeekBar = view.findViewById(R.id.cover_player_controller_seek_bar);
        mBottomSeekBar = view.findViewById(R.id.cover_bottom_seek_bar);
        mBottomContainer = view.findViewById(R.id.cover_player_controller_bottom_container);
        view.findViewById(R.id.cover_player_controller_image_view_switch_screen).setVisibility(View.GONE);
        return view;
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
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED://某些机型没有render start这个消息
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

    private void updateUI(int curr, int duration) {
        setSeekProgress(curr, duration);
        setBottomSeekProgress(curr, duration);
        setCurrTime(curr);
        setTotalTime(duration);
    }

    private void setCurrTime(int curr){
        mCurrTime.setText(TimeUtil.getTime(mTimeFormat, curr));
    }

    private void setTotalTime(int duration){
        mTotalTime.setText(TimeUtil.getTime(mTimeFormat, duration));
    }

    private void setBottomSeekBarState(boolean state){
        mBottomSeekBar.setVisibility(state?View.VISIBLE:View.GONE);
    }

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

    private void sendSeekEvent(int progress){
        mTimerUpdateProgressEnable = false;
        setCurrTime(progress);
        int duration = getPlayerStateGetter().getDuration();
        setSeekProgress(progress, duration);
        setBottomContainerVisibility(true);
        mSeekProgress = progress;
        mHandler.removeCallbacks(mSeekEventRunnable);
        mHandler.postDelayed(mSeekEventRunnable, 500);
    }

    private void setBottomContainerVisibility(boolean visibility){
        int visible = visibility ? View.VISIBLE : View.GONE;
        if (mBottomContainer.getVisibility() != visible) {
            mBottomContainer.setVisibility(visible);
        }
    }
    private Runnable mSeekEventRunnable = new Runnable() {
        @Override
        public void run() {
            if(mSeekProgress < 0)
                return;
            Bundle bundle = BundlePool.obtain();
            bundle.putInt(EventKey.INT_DATA, mSeekProgress);
            Log.d(TAG, "seek: " + mSeekProgress);
            requestSeek(bundle);
            mSeekProgress = -1;
            boolean selected = mStateIcon.isSelected();
            if (!selected) {
                setBottomContainerVisibility(false);
            }
        }
    };

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

    /**----------- key control ------------------- */

    @Override
    protected void onBackKeyEvent(boolean down, KeyEvent event) {
        if (!down) {
            ((Activity) getContext()).finish();
        }
    }

    @Override
    protected void onCenterKeyEvent(boolean down, KeyEvent event) {
        if (!down) {
            boolean selected = mStateIcon.isSelected();
            if(selected){
                requestResume(null);
                setBottomContainerVisibility(false);
            }else{
                requestPause(null);
                setBottomContainerVisibility(true);
            }
            mStateIcon.setSelected(!selected);
        }
    }

    @Override
    protected void onLeftKeyEvent(boolean down, KeyEvent event) {
        if (down) {
            calculateSeekPosition(false);
        }
    }

    @Override
    protected void onRightKeyEvent(boolean down, KeyEvent event) {
        if (down) {
            calculateSeekPosition(true);
        }
    }

    private void calculateSeekPosition(boolean right){
        //-1代表不在快进状态中
        int seekingProgress;
        if (mSeekProgress == -1) {
            seekingProgress = mSeekProgress = getPlayerStateGetter().getCurrentPosition();
        } else {
            seekingProgress = mSeekProgress;
        }
        Log.d(TAG, "calculateSeekPosition: " + seekingProgress);
        if (right) {
            seekingProgress += 500;
        } else {
            seekingProgress -= 500;
        }
        int duration = getPlayerStateGetter().getDuration();
        //做一个前后判断，tv有些设备，seek到0或者duration的位置，会播放出错
        if (seekingProgress > duration) {
            seekingProgress = duration - 1000;
        }
        if (seekingProgress <= 0) {
            seekingProgress = 500;
        }
        sendSeekEvent(seekingProgress);
    }
}
