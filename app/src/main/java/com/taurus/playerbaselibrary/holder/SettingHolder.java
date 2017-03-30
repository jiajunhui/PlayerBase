package com.taurus.playerbaselibrary.holder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kk.taurus.baseframe.base.ContentHolder;
import com.kk.taurus.baseframe.base.HolderData;
import com.kk.taurus.playerbase.setting.PlayerType;
import com.kk.taurus.playerbase.setting.PlayerTypeEntity;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.utils.Setting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2017/3/30.
 */

public class SettingHolder extends ContentHolder<HolderData> {

    private RadioGroup mRadioGroup;
    private Map<Integer,Integer> id_keys = new HashMap<>();

    public SettingHolder(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_setting);
        mRadioGroup = getViewById(R.id.radio_group);
    }

    @Override
    public void onHolderCreated(Bundle savedInstanceState) {
        super.onHolderCreated(savedInstanceState);
        Map<Integer,PlayerTypeEntity> playerTypeEntityMap = PlayerType.getInstance().getPlayerTypes();
        for(int key:playerTypeEntityMap.keySet()){
            int id = key + 1000000;
            id_keys.put(id,key);
            RadioButton radioButton = getRadioButton(playerTypeEntityMap.get(key).getPlayerName(),id);
            if(PlayerType.getInstance().getDefaultPlayerType()==key){
                radioButton.setChecked(true);
            }
            mRadioGroup.addView(radioButton,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int playerType = id_keys.get(checkedId);
                PlayerType.getInstance().setDefaultPlayerType(playerType);
                Setting.getInstance().save(mContext,"player_type",playerType);
                PlayerType.getInstance().setDefaultPlayerType(playerType);
            }
        });
    }

    private RadioButton getRadioButton(String text, int id){
        RadioButton radioButton = new RadioButton(mContext);
        radioButton.setText(text);
        radioButton.setId(id);
        return radioButton;
    }

    @Override
    public void refreshView() {
        super.refreshView();

    }
}
