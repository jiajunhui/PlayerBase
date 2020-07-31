package com.kk.taurus.avplayer.ui;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.view.VisualizerView;
import com.kk.taurus.playerbase.AVPlayer;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;

public class MusicPlayActivity extends AppCompatActivity implements OnPlayerEventListener {

    private AVPlayer mPlayer;
    private EditText mEtUrl;

    private Visualizer mVisualizer;
    private VisualizerView mMusicWave;

    private byte[] waveType = new byte[]{
            VisualizerView.WAVE_TYPE_BROKEN_LINE,
            VisualizerView.WAVE_TYPE_RECTANGLE,
            VisualizerView.WAVE_TYPE_CURVE};

    private int typeIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);
        mEtUrl = findViewById(R.id.music_url_et);
        mMusicWave = findViewById(R.id.visualizerView);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mPlayer = new AVPlayer();
        mPlayer.setOnPlayerEventListener(this);

        initMusicWave();
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED:
                releaseVisualizer();
                updateVisualizer();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.destroy();
        releaseVisualizer();
    }


}
