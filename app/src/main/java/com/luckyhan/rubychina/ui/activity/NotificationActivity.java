package com.luckyhan.rubychina.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.ui.fragment.NotificationFragment;

import butterknife.ButterKnife;

public class NotificationActivity extends BaseSwipeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        ButterKnife.bind(this);
        initToolBar(R.string.title_notification);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.container) == null) {
            fm.beginTransaction().replace(R.id.container, new NotificationFragment()).commit();
        }
    }

}
