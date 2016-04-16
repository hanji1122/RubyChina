package com.luckyhan.rubychina.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.OAuthRequest;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.model.Token;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.UserResponse;
import com.luckyhan.rubychina.ui.base.BaseActivity;
import com.luckyhan.rubychina.ui.fragment.ActiveUserFragment;
import com.luckyhan.rubychina.ui.fragment.TopicTabsFragment;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.SharedPrefUtil;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.orhanobut.hawk.Hawk;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final int REQUEST_LOGIN = 11;
    public static final int REQUEST_CHANGE_NODES = 22;

    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private ImageView mBlurUserView;
    private CircleImageView mUserAvatar;
    private TextView mUserName;

    private boolean mDoubleBackToExitPressedOnce;
    private int mCurrentDrawerItemId;
    private Fragment mCurrentFragment;
    private Fragment mTopTabsFragment, mActiveUserFragment;

    private AlertDialog mUpdateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        View header = mNavigationView.inflateHeaderView(R.layout.drawer_header);
        mBlurUserView = ButterKnife.findById(header, R.id.home_menu_avatar_blur);
        mUserAvatar = ButterKnife.findById(header, R.id.home_menu_avatar);
        mUserName = ButterKnife.findById(header, R.id.home_menu_user);
        ButterKnife.findById(header, R.id.drawer_header_container).setOnClickListener(this);
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);
        initDrawer();

        checkToken();
        initBroadcast();
        initPgrUpdate();
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                updateMainContent();
            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
        mCurrentDrawerItemId = R.id.navi_item_home;

        updateMainContent();
        updateUserCenterView((User) Hawk.get("user"));
    }

    private void initPgrUpdate() {
        boolean isAutoCheckUpdate = SharedPrefUtil.getBoolean(getContext(), Constants.Pref.AUTO_UPDATE, true);
        if (!isAutoCheckUpdate) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity().isFinishing()) {
                    return;
                }
                PgyUpdateManager.register(getActivity(), new UpdateManagerListener() {

                    @Override
                    public void onNoUpdateAvailable() {
                    }

                    @Override
                    public void onUpdateAvailable(String result) {
                        if (getActivity().isFinishing()) {
                            return;
                        }
                        final AppBean appBean = getAppBeanFromString(result);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.dialog_title_app_new_update);
                        builder.setMessage(getString(R.string.update_release_notes, appBean.getVersionName(), appBean.getReleaseNote()));
                        builder.setNegativeButton(R.string.dialog_option_remind_later, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNeutralButton(R.string.dialog_option_no_remind, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPrefUtil.putBoolean(getContext(), Constants.Pref.AUTO_UPDATE, false);
                                ToastUtils.showLong(getContext(), R.string.msg_close_auto_update);
                            }
                        }).setPositiveButton(R.string.dialog_option_update_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startDownloadTask(MainActivity.this, appBean.getDownloadURL());
                            }
                        });
                        mUpdateDialog = builder.show();
                    }
                });
            }
        }, 6000);
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Intent.LOGIN_SUCCESS);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mLoginSuccessReceiver, filter);
    }

    private void checkToken() {
        if (DataUtils.isTokenExpires()) {
            requestRefreshToken(DataUtils.getUserToken().refresh_token);
        }
    }

    private void updateUserCenterView(User user) {
        if (user == null) {
            return;
        }
        mUserName.setText(user.login);
        ImgLoader.INSTANCE.displayAvatarImage(user.avatar_url, mUserAvatar, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                mUserAvatar.post(new Runnable() {
                    @Override
                    public void run() {
                        Blurry.with(getContext())
                                .radius(10)
                                .sampling(3)
                                .async()
                                .capture(mUserAvatar)
                                .into(mBlurUserView);
                    }
                });
            }
        });
    }

    private void requestRefreshToken(String refresh_token) {
        new OAuthRequest().refreshToken(refresh_token, new Callback<Token>() {

            @Override
            public void onResponse(Response<Token> response, Retrofit retrofit) {
                if (!response.isSuccess()) {
                    return;
                }
                ToastUtils.showShort(getContext(), R.string.msg_auth_updated);
                Token token = response.body();
                Hawk.put("token", token);
                requestCurrentUser(token.access_token);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void requestCurrentUser(String access_token) {
        new UserRequest().getLoginUser(access_token, new Callback<UserResponse>() {

            @Override
            public void onResponse(Response<UserResponse> response, Retrofit retrofit) {
                User user = response.body().user;
                Hawk.put("user", user);
                updateUserCenterView(user);
            }

            @Override
            public void onFailure(Throwable t) {
                CrashReport.postCatchedException(t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentFragment instanceof TopicTabsFragment) {
            mNavigationView.getMenu().findItem(R.id.navi_item_home).setChecked(true);
            mCurrentDrawerItemId = R.id.navi_item_home;
        }
        if (mCurrentFragment instanceof ActiveUserFragment) {
            mNavigationView.getMenu().findItem(R.id.navi_item_active).setChecked(true);
            mCurrentDrawerItemId = R.id.navi_item_active;
        }
    }

    private void switchFragment(Fragment toFragment) {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, toFragment);
        ft.commit();

        mCurrentFragment = toFragment;
    }

    public void updateNodes(List<Node> nodes) {
        if (mTopTabsFragment != null) {
            ((TopicTabsFragment) mTopTabsFragment).updateNodes(nodes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_NODES && resultCode == RESULT_OK) {
            List<Node> nodes = data.getParcelableArrayListExtra("nodes");
            updateNodes(nodes);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
        } else {
            mDoubleBackToExitPressedOnce = true;
            ToastUtils.showShort(getContext(), getString(R.string.msg_double_click_exit));
            ((new Handler())).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDoubleBackToExitPressedOnce = false;
                }
            }, 2000L);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mCurrentDrawerItemId = item.getItemId();
        return true;
    }

    private void updateMainContent() {
        switch (mCurrentDrawerItemId) {
            case R.id.drawer_header_container: {
                if (DataUtils.isUserLogin()) {
                    UserCenterActivity.newInstance(getContext(), DataUtils.getLoginUser());
                } else {
                    Intent intent = new Intent(getContext(), OAuthActivity.class);
                    startActivityForResult(intent, REQUEST_LOGIN);
                }
                break;
            }
            case R.id.navi_item_home: {
                if (mCurrentFragment instanceof TopicTabsFragment) {
                    return;
                }
                if (mTopTabsFragment == null) {
                    mTopTabsFragment = new TopicTabsFragment();
                }
                switchFragment(mTopTabsFragment);
                break;
            }

            case R.id.navi_item_active: {
                if (mCurrentFragment instanceof ActiveUserFragment) {
                    return;
                }
                if (mActiveUserFragment == null) {
                    mActiveUserFragment = new ActiveUserFragment();
                }
                switchFragment(mActiveUserFragment);
                break;
            }

            case R.id.navi_item_post: {
                if (AppUtils.jumpLogin(getContext())) {
                    return;
                }
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.navi_item_settings: {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawer_header_container:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mCurrentDrawerItemId = v.getId();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLoginSuccessReceiver);
        if (mUpdateDialog != null) {
            mUpdateDialog.dismiss();
        }
        super.onDestroy();
    }

    private BroadcastReceiver mLoginSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUserCenterView((User) Hawk.get("user"));
        }
    };

}
