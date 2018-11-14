package com.kk.taurus.avplayer.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.bean.VideoBean;
import com.kk.taurus.avplayer.ui.listplay.MultiListActivity;
import com.kk.taurus.avplayer.utils.DataUtils;
import com.kk.taurus.playerbase.entity.DataSource;

import com.kk.taurus.avplayer.adapter.ListAdapter;
import com.kk.taurus.avplayer.base.ISPayer;
import com.kk.taurus.avplayer.ui.DetailPlayActivity;
import com.kk.taurus.avplayer.play.ListPlayer;
import com.kk.taurus.playerbase.log.PLog;

public class VideoListFragment extends Fragment implements ListAdapter.OnListListener {

    private static final String KEY_FRAG_INDEX = "frag_index";

    private RecyclerView mRecycler;
    private ListAdapter mListAdapter;

    private int mFragIndex;

    private boolean hasInit;

    public static VideoListFragment create(int fragIndex){
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FRAG_INDEX,fragIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, null);
        mRecycler = root.findViewById(R.id.fragment_recycler);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            mFragIndex = bundle.getInt(KEY_FRAG_INDEX);
        }
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mListAdapter = new ListAdapter(getContext(), mRecycler, DataUtils.getVideoList(mFragIndex*3, 3));
        mListAdapter.setOnListListener(this);
        mRecycler.setAdapter(mListAdapter);
        hasInit = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(ListPlayer.get().getPlayPageIndex() != mFragIndex){
            return;
        }
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecycler.post(new Runnable() {
                @Override
                public void run() {
                    ListAdapter.VideoItemHolder currentHolder = mListAdapter.getCurrentHolder();
                    if(currentHolder!=null){
                        ListPlayer.get().setReceiverConfigState(getActivity(), ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
                        ListPlayer.get().attachContainer(currentHolder.layoutContainer, false);
                    }else{
                        PLog.e("VideoListFragment", "portrait itemHolder null");
                    }
                }
            });
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(hasInit){
            if(!getUserVisibleHint()){
                mListAdapter.reset();
            }
        }
    }

    @Override
    public void onTitleClick(ListAdapter.VideoItemHolder holder, VideoBean item, int position) {
        ((MultiListActivity)getActivity()).toDetail();
        DetailPlayActivity.launch(getActivity(), mListAdapter.getPlayPosition()==position, item);
        mListAdapter.reset();
    }

    @Override
    public void playItem(ListAdapter.VideoItemHolder holder, VideoBean item, int position) {
        ListPlayer.get().setReceiverConfigState(getActivity(), ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
        ListPlayer.get().attachContainer(holder.layoutContainer);
        ListPlayer.get().play(new DataSource(item.getPath()));
    }

}
