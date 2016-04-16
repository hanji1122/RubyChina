package com.luckyhan.rubychina.model.response;

import java.util.ArrayList;

public class ErrorResponse {

    public Object error;

    @Override
    public String toString() {
        if (error instanceof ArrayList) {
            return (String) ((ArrayList) error).get(0);
        }
        if (error instanceof String) {
            return (String) error;
        }
        return "";
    }

}
