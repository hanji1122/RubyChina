package com.luckyhan.rubychina.ui.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemBaseRecycleAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mList;
    protected LayoutInflater mLayoutInflater;

    public ItemBaseRecycleAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

    public List<T> getList() {
        return mList;
    }

    public T getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    public void clearList() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public void setList(List<T> list) {
        if (mList == list || list == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<>();
        }
        if (mList.size() > 0) {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        if (list == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        if (item == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (position == mList.size()) {
            return 0;
        }
        return position;
    }

}
