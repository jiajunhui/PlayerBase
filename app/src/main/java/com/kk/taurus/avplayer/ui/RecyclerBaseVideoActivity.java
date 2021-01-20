package com.kk.taurus.avplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.kk.taurus.avplayer.R;
import com.kk.taurus.avplayer.adapter.RecyclerBaseVideoContentAdapter;
import com.kk.taurus.avplayer.bean.RecyclerBaseVideoBean;

public class RecyclerBaseVideoActivity extends AppCompatActivity {

    RecyclerBaseVideoContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_base_video);
        RecyclerView contentRv = findViewById(R.id.content_rv);
        contentRv.setLayoutManager(new LinearLayoutManager(this));
        contentRv.setAdapter(mAdapter = new RecyclerBaseVideoContentAdapter(RecyclerBaseVideoBean.getItemList()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.onDestroy();
    }
}
