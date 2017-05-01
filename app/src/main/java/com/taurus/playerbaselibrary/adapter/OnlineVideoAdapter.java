package com.taurus.playerbaselibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.bean.VideoEntity;
import com.taurus.playerbaselibrary.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/4/30.
 */

public class OnlineVideoAdapter extends RecyclerView.Adapter<OnlineVideoAdapter.VideoItemHolder>{

    private Context mContext;
    private List<VideoEntity> videoEntities = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public OnlineVideoAdapter(Context context, List<VideoEntity> videoEntities){
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
        VideoEntity entity = videoEntities.get(position);
        holder.title.setText(entity.getTitle());
        Glide.with(mContext)
                .load(entity.getCover())
                .centerCrop()
                .placeholder(R.mipmap.ic_cover_default2)
                .crossFade()
                .into(holder.cover);
        VideoEntity.VideoTopic videoTopic = entity.getVideoTopic();
        if(videoTopic!=null){
            Glide.with(mContext)
                    .load(videoTopic.getTopic_icons())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(holder.topicImg);
        }

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
        ImageView topicImg;
        TextView title;
        TextView timeLength;
        TextView videoSource;

        public VideoItemHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            topicImg = (ImageView) itemView.findViewById(R.id.topic_image);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

}
