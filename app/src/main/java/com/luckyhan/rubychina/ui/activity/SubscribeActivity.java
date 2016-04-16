package com.luckyhan.rubychina.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.ui.fragment.NodesFragment;

public class SubscribeActivity extends BaseSwipeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            NodesFragment f = new NodesFragment();
            fm.beginTransaction().replace(android.R.id.content, f).commit();
        }
    }

}
