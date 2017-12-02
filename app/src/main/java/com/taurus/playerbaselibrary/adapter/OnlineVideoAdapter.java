package com.taurus.playerbaselibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.bean.OnlineVideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideoAdapter extends RecyclerView.Adapter<OnlineVideoAdapter.VideoItemHolder>{

    private Context mContext;
    private List<OnlineVideoItem> videoEntities = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public OnlineVideoAdapter(Context context, List<OnlineVideoItem> videoEntities){
        this.mContext = context;
        this.videoEntities = videoEntities;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public VideoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoItemHolder(View.inflate(mContext,R.layout.item_online_video,null));
    }

    @Override
    public void onBindViewHolder(final VideoItemHolder holder, final int position) {
        OnlineVideoItem onlineVideoItem = videoEntities.get(position);
        holder.title.setText(onlineVideoItem.getUrl());
        holder.cover.setImageResource(onlineVideoItem.getResId());

        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(holder,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoEntities.size();
    }

    public static class VideoItemHolder extends RecyclerView.ViewHolder{

        ImageView cover;
        TextView title;

        public VideoItemHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

}
