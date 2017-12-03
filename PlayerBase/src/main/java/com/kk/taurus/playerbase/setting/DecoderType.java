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

package com.kk.taurus.playerbase.setting;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2016/11/14.
 * 该类为解码器类型管理（注：此处的解码器即为不包含渲染视图的解码器，如系统的MediaPlayer。）
 */

public class DecoderType {

    public static final int TYPE_DECODER_DEFAULT = 0;

    private Map<Integer,DecoderTypeEntity> decoderTypes;

    private int defaultDecoderType;

    private static DecoderType instance;

    private DecoderType(){
        decoderTypes = new HashMap<>();
        addDecoderType(TYPE_DECODER_DEFAULT,new DecoderTypeEntity("原生解码","com.kk.taurus.playerbase.player.DefaultDecoderPlayer"));
    }

    public static DecoderType getInstance(){
        if(null==instance){
            synchronized (DecoderType.class){
                if(null==instance){
                    instance = new DecoderType();
                }
            }
        }
        return instance;
    }

    public DecoderType setDefaultDecoderType(int decoderType){
        this.defaultDecoderType = decoderType;
        return this;
    }

    public int getDefaultPlayerType() {
        return defaultDecoderType;
    }

    public DecoderType addDecoderType(int decoderType, DecoderTypeEntity entity){
        decoderTypes.put(decoderType,entity);
        return this;
    }

    public Map<Integer,DecoderTypeEntity> getDecoderTypes(){
        return decoderTypes;
    }

    public String getDecoderPath(int playerType){
        DecoderTypeEntity entity = decoderTypes.get(playerType);
        if(entity==null)
            return null;
        return entity.getDecoderClassPath();
    }

}
