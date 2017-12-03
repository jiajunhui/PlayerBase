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
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    private List<Integer> radioIds = new ArrayList<>();
    private final String KEY_PLAYER_TYPE = "play_type";

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

        initRadioButtons();

        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
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
