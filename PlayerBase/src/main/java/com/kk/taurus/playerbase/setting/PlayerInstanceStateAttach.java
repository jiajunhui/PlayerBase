package com.kk.taurus.playerbase.setting;

import android.os.Bundle;

import com.kk.taurus.playerbase.inter.IPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mtime on 2017/12/13.
 */

public class PlayerInstanceStateAttach {

    public static final int STATE_CODE_ON_NEW_INSTANCE_CREATE = 1;

    private static PlayerInstanceStateAttach i;

    private PlayerInstanceStateAttach(){}

    public static PlayerInstanceStateAttach get(){
        if(null==i){
            synchronized (PlayerInstanceStateAttach.class){
                if(null==i){
                    i = new PlayerInstanceStateAttach();
                }
            }
        }
        return i;
    }

    private List<AttachEntity> mInstances;

    public void attachInstance(IPlayer instance, OnInstanceStateListener onInstanceStateListener){
        if(mInstances==null){
            mInstances = new ArrayList<>();
        }
        onStateChangeOnOlderInstances(instance);
        mInstances.add(new AttachEntity(instance,onInstanceStateListener));
    }

    public void detachInstance(IPlayer instance, OnInstanceStateListener onInstanceStateListener){
        if(mInstances==null)
            return;
        Iterator<AttachEntity> iterator = mInstances.iterator();
        while (iterator.hasNext()){
            AttachEntity next = iterator.next();
            if(next.player==instance
                    && next.onInstanceStateListener==onInstanceStateListener){
                iterator.remove();
                break;
            }
        }
    }

    private void onStateChangeOnOlderInstances(IPlayer instance) {
        for(AttachEntity entity:mInstances){
            if(entity.player!=instance && entity.onInstanceStateListener!=null){
                entity.onInstanceStateListener.onInstanceStateChange(STATE_CODE_ON_NEW_INSTANCE_CREATE,null);
            }
        }
    }

    public static class AttachEntity{
        public AttachEntity(IPlayer player, OnInstanceStateListener onInstanceStateListener) {
            this.player = player;
            this.onInstanceStateListener = onInstanceStateListener;
        }

        public IPlayer player;
        public OnInstanceStateListener onInstanceStateListener;
    }

    public interface OnInstanceStateListener{
        void onInstanceStateChange(int code, Bundle bundle);
    }

}
