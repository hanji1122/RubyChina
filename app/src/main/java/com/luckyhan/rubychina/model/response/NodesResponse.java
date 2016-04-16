package com.luckyhan.rubychina.model.response;

import com.luckyhan.rubychina.model.Meta;
import com.luckyhan.rubychina.model.Node;

import java.util.List;

public class NodesResponse {

    public List<Node> nodes;
    public Meta meta;

    @Override
    public String toString() {
        return "NodesResponse{" +
                "nodes=" + nodes +
                ", meta=" + meta +
                '}';
    }
}
