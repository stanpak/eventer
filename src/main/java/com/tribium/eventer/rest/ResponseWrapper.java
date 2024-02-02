package com.tribium.eventer.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {
    public boolean success = true;
    public Object data = null;
    public Object errorInfo = null;

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "success=" + success +
                ", data=" + data +
                ", errorInfo=" + errorInfo +
                '}';
    }
}
