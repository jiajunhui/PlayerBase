package com.kk.taurus.avplayer.bean;

import com.kk.taurus.avplayer.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ (张耀中)
 * DESCRIPTION
 * @name RecyclerBaseVideoBean
 * @date 2021/01/15 14:51
 */
public class RecyclerBaseVideoBean {

    public static final List<RecyclerBaseVideoBean> getItemList(){
        List<RecyclerBaseVideoBean> itemList = new ArrayList<>();
        itemList.add(new RecyclerBaseVideoBean(1));
        for (int i = 0; i < 50; i++){
            itemList.add(new RecyclerBaseVideoBean());
        }
        return itemList;
    }
    public int itemType;
    public String videoUrl = DataUtils.VIDEO_URL_09,itemStr = "音乐和艺术如何改变世界";

    public RecyclerBaseVideoBean(int itemType) {
        this.itemType = itemType;
    }

    public RecyclerBaseVideoBean() {
    }
}
