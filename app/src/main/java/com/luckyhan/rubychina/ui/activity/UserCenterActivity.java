package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.RubyChinaApp;
import com.luckyhan.rubychina.api.parser.ErrorUtils;
import com.luckyhan.rubychina.api.request.UserRequest;
import com.luckyhan.rubychina.data.DataUtils;
import com.luckyhan.rubychina.model.User;
import com.luckyhan.rubychina.model.response.ActionResponse;
import com.luckyhan.rubychina.model.response.ErrorResponse;
import com.luckyhan.rubychina.model.response.UserResponse;
import com.luckyhan.rubychina.ui.adapter.UserBaseFragment;
import com.luckyhan.rubychina.ui.adapter.user.UserCenterTabsAdapter;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.utils.AppUtils;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UserCenterActivity extends BaseSwipeActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_USER = "user";

    @Bind(R.id.appbar) AppBarLayout mAppBarLayout;
    @Bind(R.id.avatar) ImageView mAvatar;
    @Bind(R.id.header_content) View mHeaderContent;
    @Bind(R.id.avatar_small) CircleImageView mAvatarSmall;
    @Bind(R.id.location_company) TextView mLocationCompanyView;
    @Bind(R.id.tabs) TabLayout mTabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;

    private boolean mHasHided = false;
    private boolean mHasShowed = false;
    private UserResponse mUserModel = new UserResponse();

    public static void newInstance(Context context, User user) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        intent.putExtra(EXTRA_USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercenter);
        ButterKnife.bind(this);
        mAvatarSmall.setOnClickListener(this);
        mUserModel.user = getIntent().getParcelableExtra(EXTRA_USER);

        initView();
        loadUser();
        initToolBar(mUserModel.user.login);
        setUpViewPager();
    }

    private void setUpViewPager() {
        if (isFinishing()) {
            return;
        }
        mViewPager.setAdapter(new UserCenterTabsAdapter(getContext(), getSupportFragmentManager(), mUserModel.user));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        StringBuilder builder = new StringBuilder();
        User user = mUserModel.user;
        if (!TextUtils.isEmpty(user.location)) {
            builder.append(user.location);
        }
        if (!(TextUtils.isEmpty(user.location) || TextUtils.isEmpty(user.company))) {
            builder.append(", ");
        }
        if (!TextUtils.isEmpty(user.company)) {
            builder.append(user.company);
        }
        mLocationCompanyView.setText(builder.toString());
        loadAvatar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_follow, menu);
        if (mUserModel.meta != null) {
            MenuItem item = menu.findItem(R.id.action_nodes_follow_toggle).setVisible(true);
            if (mUserModel.meta.followed) {
                item.setTitle(R.string.action_unfollow);
            } else {
                item.setTitle(R.string.action_follow);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nodes_refresh:
                loadUser();
                return true;

            case R.id.action_nodes_follow_toggle:
                if (AppUtils.jumpLogin(getContext())) {
                    return true;
                }
                if (mUserModel.meta == null) return true;
                if (mUserModel.meta.followed) {
                    sendUnFollowRequest();
                } else {
                    sendFollowRequest();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    private void loadAvatar() {
        ImgLoader.INSTANCE.displayImage(mUserModel.user.avatar_url, mAvatarSmall);
        Glide.with(RubyChinaApp.getContext()).load(mUserModel.user.avatar_url)
                .bitmapTransform(new BlurTransformation(getContext()))
                .into(mAvatar);
    }

    private void loadUser() {
        new UserRequest().getUserDetailInfo(mUserModel.user.login, DataUtils.getToken(), new Callback<UserResponse>() {
            @Override
            public void onResponse(Response<UserResponse> response, Retrofit retrofit) {
                mUserModel = response.body();

                if (response.isSuccess() && mUserModel != null) {
                    setUpViewPager();
                    initView();
                    supportInvalidateOptionsMenu();
                    return;
                }

                if (response.code() == 404) {
                    ToastUtils.showShort(getContext(), R.string.msg_net_not_found);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void sendFollowRequest() {
        new UserRequest().follow(mUserModel.user.login, DataUtils.getToken(), new Callback<ActionResponse>() {
            @Override
            public void onResponse(Response<ActionResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    ActionResponse result = response.body();
                    if (result.ok == 1) {
                        mUserModel.meta.followed = true;
                        supportInvalidateOptionsMenu();
                        loadUser();
                    }
                } else {
                    ErrorResponse errorResponse = ErrorUtils.parseError(RubyChinaApp.getContext(), response, retrofit);
                    ToastUtils.showLong(RubyChinaApp.getContext(), errorResponse.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendUnFollowRequest() {
        new UserRequest().unFollow(mUserModel.user.login, DataUtils.getToken(), new Callback<ActionResponse>() {
            @Override
            public void onResponse(Response<ActionResponse> response, Retrofit retrofit) {
                ActionResponse result = response.body();
                if (result.ok == 1) {
                    mUserModel.meta.followed = false;
                    supportInvalidateOptionsMenu();
                    loadUser();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int position = mViewPager.getCurrentItem();
        Fragment f = findFragmentByPosition(position);
        if (f != null) {
            ((UserBaseFragment) f).toggleSwipeRefreshEnableState(i);
        }

        float abs = ((float) Math.abs(i)) / ((float) appBarLayout.getTotalScrollRange());
        if (abs > 0.5) {
            if (!mHasHided) {
                ViewPropertyAnimator.animate(mHeaderContent).alpha(0).scaleX(0.95f).scaleY(0.95f).setDuration(200).start();
                mHasHided = true;
                mHasShowed = false;
            }
        } else {
            if (!mHasShowed) {
                ViewPropertyAnimator.animate(mHeaderContent).alpha(1).scaleX(1f).scaleY(1f).setDuration(200).start();
                mHasShowed = true;
                mHasHided = false;
            }
        }
    }

    private Fragment findFragmentByPosition(int position) {
        return getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + position);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar_small: {
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                intent.putExtra(EXTRA_USER, mUserModel.user);
                startActivity(intent);
                break;
            }
        }
    }
}
