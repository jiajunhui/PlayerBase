package com.kk.taurus.avplayer.adapter;

import android.support.v7.widget.RecyclerView;

public interface OnItemClickListener<H extends RecyclerView.ViewHolder, T> {

    void onItemClick(H holder, T item, int position);

}
