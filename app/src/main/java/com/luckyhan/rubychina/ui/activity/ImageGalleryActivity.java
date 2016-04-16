package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.ui.adapter.ImageGalleryPagerAdapter;
import com.luckyhan.rubychina.ui.base.BaseActivity;
import com.luckyhan.rubychina.utils.IOUtils;
import com.luckyhan.rubychina.utils.ImageUtils;
import com.luckyhan.rubychina.utils.MediaUtils;
import com.luckyhan.rubychina.utils.MimeType;
import com.luckyhan.rubychina.utils.StringUtils;
import com.luckyhan.rubychina.utils.ToastUtils;
import com.luckyhan.rubychina.widget.compat.HackyViewPager;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener, PhotoViewAttacher.OnViewTapListener {

    public static final int FROM_POST_UPLOAD = 1;

    private int mFrom = 0;
    private int position;
    private List<Image> imgList;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ImageGalleryPagerAdapter mAdapter;

    public static void newInstance(Context context, int position, List<Image> list) {
        newInstance(context, position, list, 0);
    }

    public static void newInstance(Context context, int position, List<Image> list, int from) {
        Intent intent = new Intent(context, ImageGalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelableArrayList("imgList", (ArrayList<? extends Parcelable>) list);
        bundle.putInt("from", from);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (HackyViewPager) findViewById(R.id.viewpager);

        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        imgList = bundle.getParcelableArrayList("imgList");
        mFrom = bundle.getInt("from");

        initToolBar(getString(R.string.title_image_gallery, position + 1, imgList != null ? imgList.size() : 0));

        mAdapter = new ImageGalleryPagerAdapter(getContext(), imgList, this);
        mViewPager.setPageTransformer(true, new CardTransformer(0.8f));
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mFrom == 0) {
            getMenuInflater().inflate(R.menu.menu_download, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_download) {
            String fixedUrl = StringUtils.fixUrl(mAdapter.getItem(position).url);
            final File file = ImageLoader.getInstance().getDiskCache().get(fixedUrl);
            if (file == null) {
                ToastUtils.showShort(getContext(), R.string.msg_image_viewer_loading);
                return false;
            }
            Image image = mAdapter.getItem(mViewPager.getCurrentItem());
            String suffix = MimeType.getImageSuffix(image.mimeType);
            AsyncTaskCompat.executeParallel(new AsyncTask<String, Void, File>() {

                @Override
                protected File doInBackground(String... params) {
                    File dest = ImageUtils.createImageFile(params[0]);
                    try {
                        IOUtils.copyFile(file, dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                    return dest;
                }

                @Override
                protected void onPostExecute(File result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        MediaUtils.addMediaToGallery(getContext(), result.getPath());
                        ToastUtils.showShort(getContext(), getString(R.string.msg_image_viewer_save_success, result.getPath()));
                    } else {
                        ToastUtils.showShort(getContext(), R.string.msg_image_viewer_save_fail);
                    }

                }
            }, suffix);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        setTitle(getString(R.string.title_image_gallery, position + 1, imgList != null ? imgList.size() : 0));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (ViewHelper.getTranslationY(mToolbar) == 0) {
            ViewPropertyAnimator.animate(mToolbar)
                    .translationY(-mToolbar.getHeight())
                    .setDuration(200)
                    .setInterpolator(new LinearInterpolator());
        } else {
            ViewPropertyAnimator.animate(mToolbar)
                    .translationY(0)
                    .setDuration(200)
                    .setInterpolator(new LinearInterpolator());
        }
    }

    private static class CardTransformer implements ViewPager.PageTransformer {

        private final float scaleAmount;

        public CardTransformer(float scalingStart) {
            scaleAmount = 1 - scalingStart;
        }

        @Override
        public void transformPage(View page, float position) {
            if (position >= 0f) {
                final int w = page.getWidth();
                float scaleFactor = 1 - scaleAmount * position;
                ViewHelper.setAlpha(page, 1f - position);
                ViewHelper.setScaleX(page, scaleFactor);
                ViewHelper.setScaleY(page, scaleFactor);
                ViewHelper.setTranslationX(page, w * (1 - position) - w);
            }
        }
    }

}
