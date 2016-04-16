package com.luckyhan.rubychina.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.luckyhan.rubychina.R;
import com.luckyhan.rubychina.model.Node;
import com.luckyhan.rubychina.ui.base.BaseSwipeActivity;
import com.luckyhan.rubychina.ui.fragment.TopicListFragment;
import com.luckyhan.rubychina.utils.AppUtils;

public class TopicListActivity extends BaseSwipeActivity {

    public static final String EXTRA_NODE = "node";

    private Node mNode;

    public static void newInstance(Context context, String nodeId, String nodeName) {
        Node node = new Node();
        node.id = nodeId;
        node.name = nodeName;
        newInstance(context, node);
    }

    public static void newInstance(Context context, Node node) {
        Intent i = new Intent(context, TopicListActivity.class);
        i.putExtra(EXTRA_NODE, node);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        mNode = getIntent().getParcelableExtra(EXTRA_NODE);
        initToolBar(mNode.name);
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.container) == null) {
            fm.beginTransaction().replace(R.id.container, TopicListFragment.newInstance(mNode.id, true, false)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_nodes_post) {
            if (AppUtils.jumpLogin(getContext())) {
                return true;
            }
            Intent intent = new Intent(getContext(), PostActivity.class);
            intent.putExtra(PostActivity.EXTRA_NODE, mNode);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
