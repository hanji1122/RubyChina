package com.luckyhan.rubychina.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.SharedPrefUtil;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.orhanobut.hawk.Hawk;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseSwipeActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.setting_logout) View mLogoutLayout;
    @Bind(R.id.setting_browser_pick) View mBrowserSwitchLayout;
    @Bind(R.id.setting_browser_switch) SwitchCompat mBrowserSwitch;
    @Bind(R.id.setting_sorting) View mSetSortingLayout;
    @Bind(R.id.setting_sorting_name) TextView mSortingNameTv;
    @Bind(R.id.setting_auto_update) View mAutoUpdateLayout;
    @Bind(R.id.setting_auto_update_switch) SwitchCompat mAutoUpdateSwitchCompat;
    @Bind(R.id.fl_check_update) View mCheckUpdateLayout;
    @Bind(R.id.setting_update_version) TextView mVersionView;
    @Bind(R.id.fl_about) View mAboutLayout;
    @Bind(R.id.setting_rating) View mRatingView;
    @Bind(R.id.setting_recommend) View mRecommendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initToolBar(R.string.title_settings);
        initView();
        updateView();
    }

    private void initView() {
        mSetSortingLayout.setOnClickListener(this);
        mLogoutLayout.setOnClickListener(this);
        mBrowserSwitchLayout.setOnClickListener(this);
        mBrowserSwitch.setOnCheckedChangeListener(this);
        mCheckUpdateLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mRatingView.setOnClickListener(this);
        mRecommendView.setOnClickListener(this);
        mAutoUpdateLayout.setOnClickListener(this);
        mAutoUpdateSwitchCompat.setOnCheckedChangeListener(this);
    }

    private void updateView() {
        // browser
        boolean browserType = SharedPrefUtil.getBoolean(getContext(), Constants.Pref.BROWSER, false);
        if (browserType) {
            mBrowserSwitch.setChecked(true);
        } else {
            mBrowserSwitch.setChecked(false);
        }
        // auto update
        boolean isAutoUpdate = SharedPrefUtil.getBoolean(getContext(), Constants.Pref.AUTO_UPDATE, true);
        if (isAutoUpdate) {
            mAutoUpdateSwitchCompat.setChecked(true);
        } else {
            mAutoUpdateSwitchCompat.setChecked(false);
        }
        // sorting
        updateSortingView();
        // version
        mVersionView.setText(AppUtils.getVersionName(getContext()));
        // logout
        if (DataUtils.isUserLogin()) {
            mLogoutLayout.setVisibility(View.VISIBLE);
        } else {
            mLogoutLayout.setVisibility(View.GONE);
        }
    }

    private void updateSortingView() {
        int sorting = SharedPrefUtil.getInt(getContext(), Constants.Pref.SORTING, 0);
        String[] stringArray = getResources().getStringArray(R.array.sorting_value);
        mSortingNameTv.setText(stringArray[sorting]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.setting_browser_pick: {
                mBrowserSwitch.toggle();
                break;
            }

            case R.id.setting_logout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_title_tip);
                builder.setMessage(getString(R.string.dialog_exit_tip));
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Hawk.clear();
                        ActivityCompat.finishAffinity(SettingsActivity.this);
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                }).show();
                break;
            }

            case R.id.setting_sorting: {
                int sorting = SharedPrefUtil.getInt(getContext(), Constants.Pref.SORTING, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.dialog_title_select_ordering);
                final String[] choiceItems = getResources().getStringArray(R.array.sorting_value);
                builder.setSingleChoiceItems(choiceItems, sorting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPrefUtil.putInt(getContext(), Constants.Pref.SORTING, which);
                        dialog.cancel();
                        updateSortingView();
                    }
                }).show();
                break;
            }

            case R.id.setting_rating: {
                AppUtils.openAppMarket(getContext());
                break;
            }

            case R.id.setting_auto_update: {
                mAutoUpdateSwitchCompat.toggle();
                break;
            }

            case R.id.fl_check_update: {
                PgyUpdateManager.register(this, new UpdateManagerListener() {

                    @Override
                    public void onNoUpdateAvailable() {
                        ToastUtils.showShort(getContext(), R.string.msg_no_update);
                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        final AppBean appBean = getAppBeanFromString(result);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.dialog_title_app_new_update);
                        builder.setMessage(getString(R.string.update_release_notes, appBean.getVersionName(), appBean.getReleaseNote()));
                        builder.setNegativeButton(R.string.dialog_option_remind_later, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setPositiveButton(R.string.dialog_option_update_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startDownloadTask(SettingsActivity.this, appBean.getDownloadURL());
                            }
                        }).show();
                    }
                });
                break;
            }

            case R.id.setting_recommend: {
                AppUtils.openShareIntent(getContext(), getString(R.string.share_app_content));
                break;
            }

            case R.id.fl_about: {
                AppUtils.startActivity(getContext(), AboutActivity.class);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.setting_browser_switch: {
                SharedPrefUtil.putBoolean(getContext(), Constants.Pref.BROWSER, isChecked);
                break;
            }

            case R.id.setting_auto_update_switch: {
                SharedPrefUtil.putBoolean(getContext(), Constants.Pref.AUTO_UPDATE, isChecked);
                break;
            }

            default:
                break;
        }
    }

}
