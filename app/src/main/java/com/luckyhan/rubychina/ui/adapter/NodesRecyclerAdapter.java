package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.ui.activity.TopicListActivity;
import com.luckyhan.rubychina.ui.adapter.base.ItemBaseRecycleAdapter;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NodesRecyclerAdapter extends ItemBaseRecycleAdapter<List<Node>> {

    private Context mContext;

    public List<Node> mCheckedNodes = new CopyOnWriteArrayList<>();

    public NodesRecyclerAdapter(Context context, List<Node> nodes) {
        super(context);
        mContext = context;
        if (nodes != null) {
            mCheckedNodes.addAll(nodes);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_nodes, parent, false);
        return new NodesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NodesViewHolder nodesViewHolder = (NodesViewHolder) holder;
        List<Node> item = mList.get(position);
        if (item.size() > 0) {
            String title = item.get(0).section_name;
            nodesViewHolder.title.setText(title);
        }
        if (nodesViewHolder.flowLayout.getChildCount() > 0) {
            nodesViewHolder.flowLayout.removeAllViews();
        }
        for (final Node node : item) {

            View view = View.inflate(mContext, R.layout.flow_item_toggle, null);
            final CheckBox toggleBt = (CheckBox) view.findViewById(R.id.flow_item_view);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 15;
            params.bottomMargin = 10;
            view.setLayoutParams(params);

            toggleBt.setText(node.name);
            nodesViewHolder.flowLayout.addView(toggleBt);

            for (Node checked : mCheckedNodes) {
                if (checked.id.equals(node.id)) {
                    toggleBt.setChecked(true);
                }
            }

            toggleBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mCheckedNodes.add(node);
                    } else {
                        for (Node checked : mCheckedNodes) {
                            if (checked.id.equals(node.id)) {
                                mCheckedNodes.remove(checked);
                            }
                        }
                    }
                }
            });
            toggleBt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TopicListActivity.newInstance(mContext, node);
                    return false;
                }
            });
        }

    }

    static class NodesViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_item_nodes_title) TextView title;
        @Bind(R.id.list_item_nodes_flowlayout) FlowLayout flowLayout;

        public NodesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
