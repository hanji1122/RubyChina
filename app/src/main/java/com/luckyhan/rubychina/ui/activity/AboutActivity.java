package com.luckyhan.rubychina.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends BaseSwipeActivity implements View.OnClickListener {

    @Bind(R.id.about_official_website) View mOfficialSiteView;
    @Bind(R.id.about_opensource_site) View mOpenSourceSiteView;
    @Bind(R.id.about_contact) View mContactView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initToolBar(R.string.app_name);
        mOfficialSiteView.setOnClickListener(this);
        mOpenSourceSiteView.setOnClickListener(this);
        mContactView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_official_website: {
                AppUtils.openUrl(getContext(), getString(R.string.url_official_site));
                break;
            }

            case R.id.about_opensource_site: {
                AppUtils.openUrl(getContext(), getString(R.string.url_app_source));
                break;
            }

            case R.id.about_contact: {
                AppUtils.openEmail(getContext(), getString(R.string.email_developer));
                break;
            }

            default:
                break;
        }
    }
}
