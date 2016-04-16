package com.luckyhan.rubychina.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.ui.adapter.base.ItemBaseRecycleAdapter;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChooseNodesAdapter extends ItemBaseRecycleAdapter<List<Node>> {

    private Node mCheckedNode;
    private Context mContext;
    private OnItemSelectedListener mListener;

    public ChooseNodesAdapter(Context context, Node checkedNode, OnItemSelectedListener listener) {
        super(context);
        mContext = context;
        mCheckedNode = checkedNode;
        mListener = listener;
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
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 15;
        params.bottomMargin = 10;
        for (final Node node : item) {
            View view = View.inflate(mContext, R.layout.flow_item_select, null);
            CheckedTextView toggleBt = (CheckedTextView) view.findViewById(R.id.flow_item_view);
            view.setLayoutParams(params);
            toggleBt.setText(node.name);
            nodesViewHolder.flowLayout.addView(toggleBt);

            if (mCheckedNode != null && node.id.equals(mCheckedNode.id)) {
                toggleBt.setChecked(true);
            }

            toggleBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onSelect(node);
                    }
                }
            });
        }

    }

    class NodesViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_item_nodes_title) TextView title;
        @Bind(R.id.list_item_nodes_flowlayout) FlowLayout flowLayout;

        public NodesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemSelectedListener {
        void onSelect(Node node);
    }

}
