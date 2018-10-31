package com.kk.taurus.avplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.utils.GlideApp;

import java.util.List;

public class PlayPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<VideoBean> mItems;

    public PlayPagerAdapter(Context context, List<VideoBean> list){
        this.mContext = context;
        this.mItems = list;
    }

    @Override
    public int getCount() {
        if(mItems!=null)
            return mItems.size();
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        VideoBean bean = mItems.get(position);
        View itemView = View.inflate(mContext, R.layout.item_pager_play, null);
        FrameLayout playerContainer = itemView.findViewById(R.id.playerContainer);
        playerContainer.setTag(bean.getPath());
        ImageView coverView = itemView.findViewById(R.id.iv_cover);
        GlideApp.with(mContext)
                .load(bean.getPath())
                .centerInside()
                .into(coverView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
