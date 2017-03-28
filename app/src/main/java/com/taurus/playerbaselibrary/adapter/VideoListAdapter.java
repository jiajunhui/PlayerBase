package com.taurus.playerbaselibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.playerbase.utils.TimeUtil;
import com.taurus.playerbaselibrary.R;
import com.taurus.playerbaselibrary.utils.BytesUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Taurus on 16/8/20.
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoHolder>{

    private List<VideoItem> mList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoListAdapter(Context context, List<VideoItem> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(View.inflate(mContext, R.layout.item_video,null));
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {
        VideoItem videoInfo = mList.get(position);
        holder.tv_name.setText(videoInfo.getDisplayName());
        holder.tv_info.setText(TimeUtil.getTime(videoInfo.getDuration())+ "     " + BytesUtil.formatBytes(videoInfo.getSize()));
        if(onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder,position);
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
        TextView tv_info;

        public VideoHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

}
