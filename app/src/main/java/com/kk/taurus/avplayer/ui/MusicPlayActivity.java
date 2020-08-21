package com.kk.taurus.avplayer.ui;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.view.VisualizerView;
import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.EventKey;
import com.kk.taurus.playerbase.event.OnErrorEventListener;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.log.PLog;

/**
 * 音乐播放示例
 */
public class MusicPlayActivity extends AppCompatActivity implements OnPlayerEventListener {

    private AVPlayer mPlayer;
    private EditText mEtUrl;

    private Visualizer mVisualizer;
    private VisualizerView mMusicWave;

    private SeekBar mSeekBar;

    private SwitchCompat mSwitchCompat;

    private byte[] waveType = new byte[]{
            VisualizerView.WAVE_TYPE_BROKEN_LINE,
            VisualizerView.WAVE_TYPE_RECTANGLE,
            VisualizerView.WAVE_TYPE_CURVE};

    private int typeIndex;

    private float mVolumeLeft = 0.5f;
    private float mVolumeRight = 0.5f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);
        mEtUrl = findViewById(R.id.music_url_et);
        mMusicWave = findViewById(R.id.visualizerView);
        mSeekBar = findViewById(R.id.music_seek_bar);
        mSwitchCompat = findViewById(R.id.music_play_switch_compat);

        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mPlayer = new AVPlayer();
        mPlayer.setLooping(mSwitchCompat.isChecked());
        mPlayer.setVolume(mVolumeLeft, mVolumeRight);
        mPlayer.setOnPlayerEventListener(this);
        mPlayer.setOnErrorEventListener(new OnErrorEventListener() {
            @Override
            public void onErrorEvent(int eventCode, Bundle bundle) {
                Toast.makeText(MusicPlayActivity.this, "error:" + (bundle!=null?bundle.toString():""), Toast.LENGTH_SHORT).show();
            }
        });

        initMusicWave();
        updateVisualizer();

        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPlayer.setLooping(isChecked);
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPlayer.seekTo(seekBar.getProgress());
        }
    };

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        PLog.d("MusicPlayActivity", eventCode + "");
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                mSeekBar.setMax(mPlayer.getDuration());
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_START:
                PLog.d("MusicPlayActivity", "showLoading...");
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_AUDIO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
                PLog.d("MusicPlayActivity", "hiddenLoading...");
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                if(bundle!=null){
                    mSeekBar.setMax(bundle.getInt(EventKey.INT_ARG2));
                    mSeekBar.setProgress(bundle.getInt(EventKey.INT_ARG1));
                }
                break;
        }
    }

    private void initMusicWave() {
        mMusicWave.setWaveType(waveType[typeIndex]);
        mMusicWave.setColors(new int[]{Color.YELLOW, Color.BLUE});
        mMusicWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeIndex++;
                typeIndex %= waveType.length;
                mMusicWave.setWaveType(waveType[typeIndex]);
            }
        });
    }

    private void releaseVisualizer() {
        if(mVisualizer!=null){
            mVisualizer.release();
        }
    }

    private void updateVisualizer() {
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mMusicWave.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {

                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }

    public void startPlay(View view){
        String url = mEtUrl.getText().toString();
        if(TextUtils.isEmpty(url)){
            Toast.makeText(this, "please input url", Toast.LENGTH_SHORT).show();
            return;
        }
        DataSource dataSource = new DataSource();
        dataSource.setData(url);
        mPlayer.reset();
        mPlayer.setDataSource(dataSource);
        mPlayer.start();
    }

    public void volumeIncrease(View view){
        mVolumeLeft += 0.1f;
        mVolumeRight += 0.1f;
        mVolumeLeft = Math.min(mVolumeLeft, 1f);
        mVolumeRight = Math.min(mVolumeRight, 1f);
        mPlayer.setVolume(mVolumeLeft, mVolumeRight);
    }

    public void volumeReduce(View view){
        mVolumeLeft -= 0.1f;
        mVolumeRight -= 0.1f;
        mVolumeLeft = Math.max(mVolumeLeft, 0f);
        mVolumeRight = Math.max(mVolumeRight, 0f);
        mPlayer.setVolume(mVolumeLeft, mVolumeRight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.destroy();
        releaseVisualizer();
    }


}
