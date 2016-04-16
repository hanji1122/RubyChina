package com.luckyhan.rubychina.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.NodesRequest;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.model.response.NodesResponse;
import com.luckyhan.rubychina.ui.adapter.ChooseNodesAdapter;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class ChooseNodesActivity extends BaseSwipeActivity implements ChooseNodesAdapter.OnItemSelectedListener {

    public static final String EXTRA_NODE = "node";
    public static final int REQUEST_CODE = 10;

    @Bind(R.id.recycler) RecyclerView mRecyclerView;
    private ChooseNodesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_nodes);
        ButterKnife.bind(this);
        initToolBar(R.string.title_choose_node);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Node node = getIntent().getParcelableExtra(EXTRA_NODE);
        mAdapter = new ChooseNodesAdapter(getContext(), node, this);
        mRecyclerView.setAdapter(mAdapter);

        loadCachedList();
        sendRequest();
    }

    private void loadCachedList() {
        List<List<Node>> nodes = parseJsonData(Hawk.<List<Node>>get(Constants.KEY_CACHE_NODES));
        mAdapter.setList(nodes);
    }

    private void sendRequest() {
        new NodesRequest().getNodes(new Callback<NodesResponse>() {
            @Override
            public void onResponse(Response<NodesResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Node> nodesList = response.body().nodes;
                    List<List<Node>> nodes = parseJsonData(nodesList);
                    mAdapter.setList(nodes);
                    Hawk.put(Constants.KEY_CACHE_NODES, nodesList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private List<List<Node>> parseJsonData(List<Node> nodes) {
        if (nodes == null) return null;
        List<String> sectionIds = new ArrayList<>();
        for (Node node : nodes) {
            String sectionId = node.section_id;
            if (!sectionIds.contains(sectionId)) {
                sectionIds.add(sectionId);
            }
        }
        Collections.sort(sectionIds);
        List<List<Node>> sectionList = new ArrayList<>();

        for (String id : sectionIds) {
            List<Node> nodeList = new ArrayList<>();
            for (Node node : nodes) {
                if (id.equals(node.section_id)) {
                    nodeList.add(node);
                }
            }
            sectionList.add(nodeList);
        }
        return sectionList;
    }

    @Override
    public void onSelect(Node node) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NODE, node);
        setResult(RESULT_OK, intent);
        finish();
    }

}
