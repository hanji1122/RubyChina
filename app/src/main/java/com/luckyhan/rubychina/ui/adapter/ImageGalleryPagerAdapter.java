package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Image;
import com.luckyhan.rubychina.utils.BitmapUtils;
import com.luckyhan.rubychina.utils.ImageUtils;
import com.luckyhan.rubychina.utils.ImgLoader;
import com.luckyhan.rubychina.utils.MimeType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageGalleryPagerAdapter extends PagerAdapter {

    private Context mContext;
    private PhotoViewAttacher.OnViewTapListener mListener;
    private List<Image> mImgList;


    public ImageGalleryPagerAdapter(Context context, List<Image> imgList, PhotoViewAttacher.OnViewTapListener listener) {
        mContext = context;
        mImgList = imgList;
        mListener = listener;
    }

    public Image getItem(int position) {
        return mImgList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_gallery_item, container, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.photoView.setOnViewTapListener(mListener);

        if (getItem(position).mimeType != null) {
            updateImage(viewHolder, getItem(position));
        } else {
            final String imageUrl = getItem(position).url;
            ImgLoader.INSTANCE.loadImage(imageUrl, ImgLoader.INSTANCE.getDisplayOptions(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(final String imageUri, final View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    final File file = new File(ImageUtils.getImageLoaderFilePath(mContext, imageUrl));

                    AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            getItem(position).mimeType = BitmapUtils.getBitmapInMimeType(mContext, Uri.fromFile(file));
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            super.onPostExecute(result);
                            updateImage(viewHolder, getItem(position));
                        }
                    });
                }
            });
        }
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    private void updateImage(ViewHolder viewHolder, Image image) {
        if (image.mimeType.equalsIgnoreCase(MimeType.GIF)) {
            File file = new File(ImageUtils.getImageLoaderFilePath(mContext, image.url));
            viewHolder.photoView.setVisibility(View.GONE);
            viewHolder.gifImageView.setVisibility(View.VISIBLE);
            viewHolder.gifImageView.setImageURI(Uri.fromFile(file));
        } else {
            viewHolder.gifImageView.setVisibility(View.GONE);
            viewHolder.photoView.setVisibility(View.VISIBLE);
            ImgLoader.INSTANCE.displayImage(image.url, viewHolder.photoView, R.drawable.occupy_transparent);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImgList != null ? mImgList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public class ViewHolder {

        @Bind(R.id.gifView) GifImageView gifImageView;
        @Bind(R.id.imageView) PhotoView photoView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }

    }

}
