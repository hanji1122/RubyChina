package com.luckyhan.rubychina.api.request;

import com.luckyhan.rubychina.api.api.NodesApi;
import com.luckyhan.rubychina.model.response.NodesResponse;

import retrofit.Callback;

public class NodesRequest extends BaseRequest {

    private NodesApi nodesApi;

    public void getNodes(Callback<NodesResponse> callback) {
        nodesApi = getRetrofitForSimpleJson().create(NodesApi.class);
        nodesApi.getNodes().enqueue(callback);
    }

}
