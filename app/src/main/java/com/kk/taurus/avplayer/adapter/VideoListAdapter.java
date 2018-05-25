package com.kk.taurus.avplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.play.ListPlayLogic;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.utils.ImageDisplayEngine;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.mediadataretriever.MediaRetriever;

import java.util.List;

/**
 * Created by Taurus on 2018/4/15.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoItemHolder>{

    private Context mContext;
    private List<VideoBean> mItems;
    private OnListListener onListListener;

    private ListPlayLogic mListPlayLogic;

    private int mScreenW;

    public VideoListAdapter(Context context, RecyclerView recyclerView, List<VideoBean> list){
        this.mContext = context;
        this.mItems = list;
        mScreenW = PUtil.getScreenW(context);
        mListPlayLogic = new ListPlayLogic(context, recyclerView, this);
    }

    public ListPlayLogic getListPlayLogic(){
        return mListPlayLogic;
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    @Override
    public VideoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoItemHolder(View.inflate(mContext, R.layout.item_video, null));
    }

    @Override
    public void onBindViewHolder(final VideoItemHolder holder, final int position) {
        updateWH(holder);
        final VideoBean item = getItem(position);
        if(TextUtils.isEmpty(item.getCover())){
            MediaRetriever
                    .withVideo(item.getPath())
                    .option(MediaRetriever.FULL_SCREEN_KIND)
                    .placeHolder(R.mipmap.ic_launcher)
                    .into(holder.albumImage);
        }else{
            ImageDisplayEngine.display(mContext, holder.albumImage, item.getCover(), R.mipmap.ic_launcher);
        }
        holder.title.setText(item.getDisplayName());
        holder.layoutContainer.removeAllViews();
        holder.albumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayPosition(position);
                mListPlayLogic.playPosition(position);
            }
        });
        if(onListListener!=null){
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePlayPosition(position);
                    onListListener.onTitleClick(item, position);
                }
            });
        }
    }

    private void updateWH(VideoItemHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.layoutBox.getLayoutParams();
        layoutParams.width = mScreenW;
        layoutParams.height = mScreenW * 9/16;
        holder.layoutBox.setLayoutParams(layoutParams);
    }

    public void updatePlayPosition(int position){
        mListPlayLogic.updatePlayPosition(position);
    }

    public VideoBean getItem(int position){
        if(mItems==null)
            return null;
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        if(mItems==null)
            return 0;
        return mItems.size();
    }

    public static class VideoItemHolder extends RecyclerView.ViewHolder{

        public FrameLayout layoutContainer;
        public RelativeLayout layoutBox;
        View albumLayout;
        ImageView albumImage;
        TextView title;

        public VideoItemHolder(View itemView) {
            super(itemView);
            layoutContainer = itemView.findViewById(R.id.layoutContainer);
            layoutBox = itemView.findViewById(R.id.layBox);
            albumLayout = itemView.findViewById(R.id.album_layout);
            albumImage = itemView.findViewById(R.id.albumImage);
            title = itemView.findViewById(R.id.tv_title);
        }

    }

    public interface OnListListener{
        void onTitleClick(VideoBean item, int position);
    }

}
