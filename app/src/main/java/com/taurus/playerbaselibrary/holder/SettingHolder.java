/*
 * Copyright 2017 jiajunhui<junhui_jia@163.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kk.taurus.playerbase.config.VideoCacheProxy;
import com.kk.taurus.playerbase.setting.DecoderType;
import com.kk.taurus.playerbase.setting.DecoderTypeEntity;
import com.kk.taurus.uiframe.i.HolderData;
import com.kk.taurus.uiframe.listener.OnHolderListener;
import com.kk.taurus.uiframe.v.ContentHolder;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.utils.SharedPrefer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Taurus on 2017/12/3.
 */

public class SettingHolder extends ContentHolder<HolderData> {

    private RadioGroup mRadioGroup;
    private TextView mVideoCacheTips;
    private TextView mIjkDecodeTips;
    private SwitchCompat mVideoCacheSwitch;
    private SwitchCompat mIjkDecodeSwitch;
    private List<Integer> radioIds = new ArrayList<>();
    public static final String KEY_PLAYER_TYPE = "play_type";
    public static final String KEY_PLAYER_VIDEO_CACHE = "video_cache";
    public static final String KEY_PLAYER_IJK_DECODE_MEDIACODEC = "ijk_decode_mediacodec";

    public SettingHolder(Context context) {
        super(context);
    }

    public SettingHolder(Context context, OnHolderListener onHolderListener) {
        super(context, onHolderListener);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onHolderCreated() {
        super.onHolderCreated();
        mRadioGroup = getViewById(R.id.radio_group);
        mVideoCacheTips = getViewById(R.id.tv_video_cache_tips);
        mIjkDecodeTips = getViewById(R.id.tv_ijk_decode_mode);
        mVideoCacheSwitch = getViewById(R.id.switch_video_cache);
        mIjkDecodeSwitch = getViewById(R.id.switch_ijk_decode_setting);

        initRadioButtons();

        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        handleVideoCacheSwitch();
        handleIjkDecodeSwitch();
    }

    private void handleVideoCacheSwitch() {
        mVideoCacheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mVideoCacheTips.setText("边播边缓存已打开");
                }else{
                    mVideoCacheTips.setText("边播边缓存已关闭");
                }
                VideoCacheProxy.get().setVideoCacheState(isChecked);
                SharedPrefer.getInstance().saveBoolean(mContext,KEY_PLAYER_VIDEO_CACHE,isChecked);
            }
        });

        boolean videoCacheOpen = SharedPrefer.getInstance().getBoolean(mContext, KEY_PLAYER_VIDEO_CACHE, false);
        mVideoCacheSwitch.setChecked(videoCacheOpen);
    }

    private void handleIjkDecodeSwitch() {
        mIjkDecodeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mIjkDecodeTips.setText("ijkplayer硬解打开");
                }else{
                    mIjkDecodeTips.setText("ijkplayer硬解关闭");
                }
                SharedPrefer.getInstance().saveBoolean(mContext,KEY_PLAYER_IJK_DECODE_MEDIACODEC,isChecked);
            }
        });

        boolean ijkMediaCodecOpen = SharedPrefer.getInstance().getBoolean(mContext, KEY_PLAYER_IJK_DECODE_MEDIACODEC, false);
        mIjkDecodeSwitch.setChecked(ijkMediaCodecOpen);
    }

    private void initRadioButtons() {
        Map<Integer, DecoderTypeEntity> decoderTypes = DecoderType.getInstance().getDecoderTypes();
        Set<Map.Entry<Integer, DecoderTypeEntity>> entries = decoderTypes.entrySet();
        RadioButton radioButton;
        int typeId = SharedPrefer.getInstance().getInt(mContext, KEY_PLAYER_TYPE, 0);
        for(Map.Entry<Integer, DecoderTypeEntity> entry:entries){
            radioButton = new RadioButton(mContext);
            DecoderTypeEntity value = entry.getValue();
            int id = entry.getKey() + 100;
            radioButton.setId(id);
            radioIds.add(id);
            if(typeId + 100 == id){
                radioButton.setChecked(true);
            }
            radioButton.setText(value.getDecoderName());
            mRadioGroup.addView(radioButton);
        }
    }

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int typeId = checkedId - 100;
            DecoderType.getInstance().setDefaultDecoderType(typeId);
            SharedPrefer.getInstance().saveInt(mContext, KEY_PLAYER_TYPE,typeId);
        }
    };

}
