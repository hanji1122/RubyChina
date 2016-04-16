package com.luckyhan.rubychina.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemBaseAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mList;

    protected ItemBaseAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = new ArrayList<>();
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    public void addItem(T obj) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(obj);
    }

    public void addList(List<T> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setList(List<T> list) {
        if (mList == null) {
            mList = new ArrayList<T>(list.size());
        } else {
            mList.clear();
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void clearList() {
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public T getItem(int i) {
        if (i < 0 || i >= getCount()) {
            return null;
        } else {
            return mList.get(i);
        }
    }

}
