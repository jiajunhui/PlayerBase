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

package com.kk.taurus.playerbase.config;

import androidx.collection.SparseArrayCompat;

import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.player.SysMediaPlayer;

/**
 * Created by Taurus on 2018/3/17.
 *
 * The configuration of the player is used for
 * the management of the decoder scheme.
 * You can add more than one decoding scheme.
 *
 */

public class PlayerConfig {

    public static final int DEFAULT_PLAN_ID = 0;

    //default decoder plan id is use System MediaPlayer.
    private static int defaultPlanId = DEFAULT_PLAN_ID;

    //decoder plans arrays.
    private static SparseArrayCompat<DecoderPlan> mPlans;

    //Whether or not use the default NetworkEventProducer.
    //default state false.
    private static boolean useDefaultNetworkEventProducer = false;

    private static boolean playRecordState = false;

    static {
        mPlans = new SparseArrayCompat<>(2);

        //add default plan
        DecoderPlan defaultPlan = new DecoderPlan(DEFAULT_PLAN_ID, SysMediaPlayer.class.getName(),"MediaPlayer");
        addDecoderPlan(defaultPlan);
        //set default plan id
        setDefaultPlanId(DEFAULT_PLAN_ID);
    }

    public static void addDecoderPlan(DecoderPlan plan){
        mPlans.put(plan.getIdNumber(), plan);
    }

    /**
     * setting default DecoderPlanId.
     * @param planId
     */
    public static void setDefaultPlanId(int planId){
        defaultPlanId = planId;
    }

    /**
     * get current DecoderPlanId.
     * @return
     */
    public static int getDefaultPlanId(){
        return defaultPlanId;
    }

    public static DecoderPlan getDefaultPlan(){
        return getPlan(defaultPlanId);
    }

    public static DecoderPlan getPlan(int planId){
        return mPlans.get(planId);
    }

    /**
     * Judging the legality of planId.
     * @param planId
     * @return
     */
    public static boolean isLegalPlanId(int planId){
        DecoderPlan plan = getPlan(planId);
        return plan!=null;
    }

    //if you want to use default NetworkEventProducer, set it true.
    public static void setUseDefaultNetworkEventProducer(boolean useDefaultNetworkEventProducer) {
        PlayerConfig.useDefaultNetworkEventProducer = useDefaultNetworkEventProducer;
    }

    public static boolean isUseDefaultNetworkEventProducer() {
        return useDefaultNetworkEventProducer;
    }

    public static void playRecord(boolean open){
        playRecordState = open;
    }

    public static boolean isPlayRecordOpen(){
        return playRecordState;
    }

}
