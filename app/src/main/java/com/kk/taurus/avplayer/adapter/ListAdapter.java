package com.kk.taurus.avplayer.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.utils.ImageDisplayEngine;
import com.kk.taurus.avplayer.utils.PUtil;
import com.kk.taurus.playerbase.log.PLog;

import java.util.List;

import com.kk.taurus.avplayer.play.ListPlayer;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.VideoItemHolder> {

    private final String TAG = "ListAdapter";

    private Context mContext;
    private List<VideoBean> mItems;

    private RecyclerView mRecycler;

    private OnListListener onListListener;

    private int mScreenUseW;

    private int mScreenH;

    private int mPlayPosition = -1;
    private int mVerticalRecyclerStart;

    public ListAdapter(Context context, RecyclerView recyclerView, List<VideoBean> list){
        this.mContext = context;
        this.mRecycler = recyclerView;
        this.mItems = list;
        mScreenUseW = PUtil.getScreenW(context) - PUtil.dip2px(context, 6*2);
        init();
    }

    public int getPlayPosition(){
        return mPlayPosition;
    }

    public void reset(){
        mPlayPosition = -1;
        notifyDataSetChanged();
    }

    private void init() {
        mScreenH = PUtil.getScreenH(mRecycler.getContext());
        mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                mRecycler.getLocationOnScreen(location);
                mVerticalRecyclerStart = location[1];
                mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemVisibleRectHeight = getItemVisibleRectHeight(mPlayPosition);
                if(mPlayPosition >= 0 && itemVisibleRectHeight <= 0 && dy != 0){
                    PLog.d(TAG,"onScrollStateChanged stop itemVisibleRectHeight = " + itemVisibleRectHeight);
                    ListPlayer.get().stop();
                    notifyItemChanged(mPlayPosition);
                    mPlayPosition = -1;
                }
            }
        });
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
        ViewCompat.setElevation(holder.card, PUtil.dip2px(mContext, 3));
        updateWH(holder);
        final VideoBean item = getItem(position);
        if(TextUtils.isEmpty(item.getCover())){
            Glide.with(mContext)
                    .setDefaultRequestOptions(
                            new RequestOptions()
                                    .frame(1500*1000)
                                    .centerCrop()
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher))
                    .load(item.getPath())
                    .into(holder.albumImage);
        }else{
            ImageDisplayEngine.display(mContext, holder.albumImage, item.getCover(), R.mipmap.ic_launcher);
        }
        holder.title.setText(item.getDisplayName());
        holder.layoutContainer.removeAllViews();
        holder.playIcon.setVisibility(mPlayPosition==position?View.GONE:View.VISIBLE);
        if(onListListener!=null){
            holder.albumLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPlayPosition >= 0)
                        notifyItemChanged(mPlayPosition);
                    holder.playIcon.setVisibility(View.GONE);
                    mPlayPosition = position;
                    onListListener.playItem(holder, item, position);
                }
            });
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListListener.onTitleClick(holder, item, position);
                }
            });
        }
    }

    private void updateWH(ListAdapter.VideoItemHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.layoutBox.getLayoutParams();
        layoutParams.width = mScreenUseW;
        layoutParams.height = mScreenUseW * 9/16;
        holder.layoutBox.setLayoutParams(layoutParams);
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

        View card;
        public FrameLayout layoutContainer;
        public RelativeLayout layoutBox;
        View albumLayout;
        ImageView albumImage;
        ImageView playIcon;
        TextView title;

        public VideoItemHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            layoutContainer = itemView.findViewById(R.id.layoutContainer);
            layoutBox = itemView.findViewById(R.id.layBox);
            albumLayout = itemView.findViewById(R.id.album_layout);
            albumImage = itemView.findViewById(R.id.albumImage);
            playIcon = itemView.findViewById(R.id.playIcon);
            title = itemView.findViewById(R.id.tv_title);
        }

    }

    public VideoItemHolder getCurrentHolder(){
        if(mPlayPosition < 0)
            return null;
        return getItemHolder(mPlayPosition);
    }

    /**
     * 获取Item中渲染视图的可见高度
     * @param position
     * @return
     */
    private int getItemVisibleRectHeight(int position){
        VideoItemHolder itemHolder = getItemHolder(position);
        if(itemHolder==null)
            return 0;
        int[] location = new int[2];
        itemHolder.layoutBox.getLocationOnScreen(location);
        int height = itemHolder.layoutBox.getHeight();

        int visibleRect;
        if(location[1] <= mVerticalRecyclerStart){
            visibleRect = location[1] - mVerticalRecyclerStart + height;
        }else{
            if(location[1] + height >= mScreenH){
                visibleRect = mScreenH - location[1];
            }else{
                visibleRect = height;
            }
        }
        return visibleRect;
    }

    private VideoItemHolder getItemHolder(int position){
        RecyclerView.ViewHolder viewHolder = mRecycler.findViewHolderForAdapterPosition(position);
        if(viewHolder!=null && viewHolder instanceof VideoItemHolder){
            return (VideoItemHolder)viewHolder;
        }
        return null;
    }

    public interface OnListListener{
        void onTitleClick(VideoItemHolder holder, VideoBean item, int position);
        void playItem(VideoItemHolder holder, VideoBean item, int position);
    }

}
