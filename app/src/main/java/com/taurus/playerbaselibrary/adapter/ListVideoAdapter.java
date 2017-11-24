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

package com.taurus.playerbaselibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.playerbase.DefaultPlayer;
import com.kk.taurus.playerbase.cover.DefaultReceiverCollections;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.view.RenderSurfaceView;
import com.kk.taurus.playerbase.view.RenderTextureView;
import com.taurus.playerbaselibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/11/19.
 */

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.VideoItemHolder> {

    private Context mContext;
    private List<VideoItem> mItems = new ArrayList<>();

    private int mCurrPlayPos = -1;
    private RecyclerView mRecycler;
    private OnItemListener onItemListener;
    private DefaultReceiverCollections receiverCollections;

    private List<DefaultPlayer> playerList = new ArrayList<>();


    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public ListVideoAdapter(Context context, List<VideoItem> items){
        this.mContext = context;
        this.mItems = items;
        receiverCollections = new DefaultReceiverCollections(context);
        receiverCollections.buildDefault();
        setHasStableIds(true);
    }

    public void setRecycler(RecyclerView recycler){
        mRecycler = recycler;
    }

    @Override
    public long getItemId(int position) {
        return (position + 1) * 111;
    }

    @Override
    public VideoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoItemHolder(View.inflate(mContext, R.layout.item_list_video_play,null));
    }

    public void addPlayer(DefaultPlayer player){
        destroy(false);
        playerList.add(player);
    }

    public void destroy(boolean destroyInternalPlayer){
        for(DefaultPlayer player : playerList){
            player.destroy(destroyInternalPlayer);
        }
        playerList.clear();
    }

    @Override
    public void onBindViewHolder(final VideoItemHolder holder, final int position) {
        final VideoItem item = getItem(position);
        holder.name.setText(item.getDisplayName());
        if(mCurrPlayPos != position){
            holder.container.removeAllViews();
        }
        holder.start.setVisibility(View.VISIBLE);
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DefaultPlayer player = new DefaultPlayer(mContext);
                addPlayer(player);
                receiverCollections = new DefaultReceiverCollections(mContext);
                receiverCollections.buildDefault();
                player.bindReceiverCollections(receiverCollections);
                player.stop();
                player.setDataSource(new VideoData(item.getPath()));
//                RenderTextureView renderTextureView = new RenderTextureView(mContext){
//                    @Override
//                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//                        super.onSurfaceTextureAvailable(surface, width, height);
//                        player.setSurface(new Surface(surface));
//                    }
//                };
                player.setRenderViewForDecoder(new RenderTextureView(mContext));
                player.start();
                holder.container.addView(player,
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT));
                if(mCurrPlayPos!=-1){
                    RecyclerView.ViewHolder viewHolderForItemId = mRecycler.findViewHolderForItemId(getItemId(mCurrPlayPos));
                    if(viewHolderForItemId!=null){
                        RelativeLayout container = ((VideoItemHolder)viewHolderForItemId).container;
                        container.removeAllViews();
                    }
                }
                mCurrPlayPos = position;
                holder.start.setVisibility(View.GONE);
                holder.detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemListener!=null){
                            onItemListener.onIntentToDetail(item,position);
                        }
                    }
                });
                holder.fullScreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.container.post(new Runnable() {
                            @Override
                            public void run() {
                                int[] location = new int[2];
                                holder.container.getLocationInWindow(location);
                                if(onItemListener!=null){
                                    onItemListener.onFullScreen(player, location);
                                }
                            }
                        });
                    }
                });
                player.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        Log.d("IRender","player onViewAttachedToWindow...");
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        Log.d("IRender","player onViewDetachedFromWindow");
                    }
                });
            }
        });
    }

    private VideoItem getItem(int position){
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class VideoItemHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView detail;
        TextView fullScreen;
        RelativeLayout container;
        ImageView start;

        public VideoItemHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            detail = (TextView) itemView.findViewById(R.id.detail);
            fullScreen = (TextView) itemView.findViewById(R.id.fullScreen);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            start = (ImageView) itemView.findViewById(R.id.start);
        }
    }

    public interface OnItemListener{
        void onIntentToDetail(VideoItem item, int position);
        void onFullScreen(DefaultPlayer player, int[] location);
    }

}
