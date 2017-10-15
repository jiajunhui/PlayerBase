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
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.playerbase.callback.OnPlayerEventListener;
import com.kk.taurus.playerbase.setting.VideoData;
import com.kk.taurus.playerbase.widget.BasePlayer;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.widget.MSurfaceView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Taurus on 16/8/20.
 */
public class VideoListAdapter2 extends RecyclerView.Adapter<VideoListAdapter2.VideoHolder>{

    private List<VideoItem> mList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private BasePlayer mPlayer;

    private RecyclerView mRecycler;

    private int mPosition = -1;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void bindPlayer(BasePlayer player){
        this.mPlayer = player;
    }

    public VideoListAdapter2(Context context, List<VideoItem> list, RecyclerView recyclerView){
        this.mContext = context;
        this.mList = list;
        this.mRecycler = recyclerView;
        setHasStableIds(true);
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(View.inflate(mContext, R.layout.item_video_scan,null));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {
        final VideoItem videoInfo = mList.get(position);
        holder.tv_name.setText(videoInfo.getDisplayName());
        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPosition==position)
                        return;

                    if(mPosition!=-1){
                        RecyclerView.ViewHolder viewHolder = mRecycler.findViewHolderForItemId(getItemId(mPosition));
                        if(viewHolder!=null){
                            ((VideoHolder)viewHolder).renderContainer.removeAllViews();
                        }
                    }

                    mPosition = position;
                    onItemClickListener.onItemClick(holder,position);
                    MSurfaceView surfaceView = new MSurfaceView(mContext, new MSurfaceView.OnSurfaceListener() {
                        @Override
                        public void onSurfaceCreated(final SurfaceHolder holder) {
                            String path = videoInfo.getPath();
                            VideoData data = new VideoData(path);
                            mPlayer.setOnPlayerEventListener(new OnPlayerEventListener() {
                                @Override
                                public void onPlayerEvent(int eventCode, Bundle bundle) {
//                                    if(eventCode==OnPlayerEventListener.EVENT_CODE_PREPARED){
//                                        mPlayer.setDisplay(holder);
//                                    }
                                }
                            });
                            mPlayer.setDataSource(data);
                            mPlayer.setDisplay(holder);
                            mPlayer.start();
                        }
                        @Override
                        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                        }
                        @Override
                        public void onSurfaceDestroyed(SurfaceHolder holder) {
                            mPlayer.reset();
                            mPlayer.setDisplay(null);
                        }
                        @Override
                        public void onSurfaceViewAttachedToWindow() {

                        }
                        @Override
                        public void onSurfaceViewDetachedFromWindow() {
                            holder.renderContainer.removeAllViews();
                            mPlayer.reset();
                            mPlayer.setDisplay(null);
                        }
                    });

                    holder.renderContainer.addView(surfaceView
                            ,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                    , ViewGroup.LayoutParams.MATCH_PARENT));

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class VideoHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        RelativeLayout renderContainer;

        public VideoHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            renderContainer = (RelativeLayout) itemView.findViewById(R.id.render_container);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

}
