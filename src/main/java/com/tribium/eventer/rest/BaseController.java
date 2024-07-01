package com.tribium.eventer.rest;

import com.tribium.eventer.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {
    @Autowired
    protected EventHandlingConfiguration eventHandlingConfiguration;

    private ResponseEntity<?> _handleEventException(EventException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(exception.message.filterFor(eventHandlingConfiguration.getRestResponse()),
                    headers, getResponseHttpStatus());
    }

    private HttpStatus getResponseHttpStatus() {
        HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
        if(eventHandlingConfiguration.isWrapResponse())
            status = HttpStatus.OK;
        return status;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> _handleAnyException(Throwable exception) {
        if (exception instanceof EventException)
            return _handleEventException((EventException) exception);

        EventCapturer ec = new EventCapturer(eventHandlingConfiguration);
        EventMessage message = ec.capture(exception);
        EventDistributor.distribute(true, eventHandlingConfiguration,message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(message.filterFor(eventHandlingConfiguration.getRestResponse()),
                headers, getResponseHttpStatus());
    }
}
