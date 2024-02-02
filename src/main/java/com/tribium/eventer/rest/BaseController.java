package com.tribium.eventer.rest;

import com.tribium.eventer.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {
    @Autowired
    protected Configuration configuration;

    private ResponseEntity<?> _handleEventException(EventException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(exception.message.filterFor(configuration.getRestResponse()),
                    headers, getResponseHttpStatus());
    }

    private HttpStatus getResponseHttpStatus() {
        HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
        if(configuration.isWrapResponse())
            status = HttpStatus.OK;
        return status;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> _handleAnyException(Throwable exception) {
        if (exception instanceof EventException)
            return _handleEventException((EventException) exception);

        EventCapturer ec = new EventCapturer(configuration);
        EventMessage message = ec.capture(exception);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(message.filterFor(configuration.getRestResponse()),
                headers, getResponseHttpStatus());
    }
}
