package com.luckyhan.rubychina.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.api.request.NodesRequest;
import com.luckyhan.rubychina.model.Constants;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.model.response.NodesResponse;
import com.luckyhan.rubychina.ui.adapter.NodesRecyclerAdapter;
import com.luckyhan.rubychina.ui.base.BaseFragment;
import com.luckyhan.rubychina.utils.CollectionUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NodesFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private NodesRecyclerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nodes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new NodesRecyclerAdapter(getContext(), Hawk.<List<Node>>get(Constants.KEY_CACHE_SUBSCRIBE));
        mRecyclerView.setAdapter(mAdapter);

        setTitle(R.string.title_nodes);
        setHasBack();
        setHasOptionsMenu(true);
        loadCachedList();
        sendRequest();
        return view;
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
                    NodesResponse nodesResponse = response.body();
                    List<List<Node>> nodes = parseJsonData(nodesResponse.nodes);
                    mAdapter.setList(nodes);
                    Hawk.put(Constants.KEY_CACHE_NODES, nodesResponse.nodes);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_nodes_save) {
            List<Node> nodes = new ArrayList<>(mAdapter.mCheckedNodes);
            cacheSubscribeNodes(nodes);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("nodes", (ArrayList<? extends Parcelable>) nodes);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cacheSubscribeNodes(List<Node> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            Hawk.remove(Constants.KEY_CACHE_SUBSCRIBE);
        } else {
            Hawk.put(Constants.KEY_CACHE_SUBSCRIBE, nodes);
        }
    }

}
