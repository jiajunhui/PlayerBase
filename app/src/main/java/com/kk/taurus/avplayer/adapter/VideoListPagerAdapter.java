package com.kk.taurus.avplayer.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kk.taurus.avplayer.ui.fragment.VideoListFragment;

import java.util.List;

public class VideoListPagerAdapter extends FragmentStatePagerAdapter {

    private List<VideoListFragment> mFragments;

    public VideoListPagerAdapter(FragmentManager fm, List<VideoListFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "列表" + (position + 1);
    }

    @Override
    public int getCount() {
        if(mFragments==null)
            return 0;
        return mFragments.size();
    }
}
