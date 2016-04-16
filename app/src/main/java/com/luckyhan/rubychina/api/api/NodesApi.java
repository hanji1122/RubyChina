package com.luckyhan.rubychina.api.api;

import com.luckyhan.rubychina.model.response.NodesResponse;

import retrofit.Call;
import retrofit.http.GET;

public interface NodesApi {

    @GET("nodes.json")
    Call<NodesResponse> getNodes();

}
