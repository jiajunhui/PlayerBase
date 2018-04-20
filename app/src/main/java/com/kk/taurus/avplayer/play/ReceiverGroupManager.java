package com.kk.taurus.avplayer.play;

import android.content.Context;

import com.kk.taurus.avplayer.cover.CompleteCover;
import com.kk.taurus.avplayer.cover.ControllerCover;
import com.kk.taurus.avplayer.cover.LoadingCover;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;

/**
 * Created by Taurus on 2018/4/18.
 */

public class ReceiverGroupManager {

    public static final String KEY_LOADING_COVER = "loading_cover";
    public static final String KEY_CONTROLLER_COVER = "controller_cover";
    public static final String KEY_COMPLETE_COVER = "complete_cover";

    private static ReceiverGroupManager i;

    private ReceiverGroupManager(){}

    public static ReceiverGroupManager get(){
        if(null==i){
            synchronized (ReceiverGroupManager.class){
                if(null==i){
                    i = new ReceiverGroupManager();
                }
            }
        }
        return i;
    }

    public ReceiverGroup getLiteReceiverGroup(Context context){
        ReceiverGroup receiverGroup = new ReceiverGroup();
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context));
        return receiverGroup;
    }

    public ReceiverGroup getReceiverGroup(Context context){
        ReceiverGroup receiverGroup = new ReceiverGroup();
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context));
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
        return receiverGroup;
    }

}
