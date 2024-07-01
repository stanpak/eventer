package com.tribium.eventer.rest;

import com.tribium.eventer.core.EventHandlingConfiguration;
import com.tribium.eventer.core.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    protected EventHandlingConfiguration eventHandlingConfiguration;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (eventHandlingConfiguration.isWrapResponse()) {
            ResponseWrapper w = new ResponseWrapper();
            if(body instanceof EventMessage){
                w.success = false;
                w.errorInfo = body;
            }
            else {
                w.success = true;
                w.data = body;
            }
            body = w;
        }
        return body;
    }
}
