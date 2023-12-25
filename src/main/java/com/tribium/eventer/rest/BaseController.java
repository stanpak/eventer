package com.tribium.eventer.rest;

import com.tribium.eventer.core.Configuration;
import com.tribium.eventer.core.EventCapturer;
import com.tribium.eventer.core.EventException;
import com.tribium.eventer.core.EventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {
    @Autowired
    protected Configuration configuration;

    private ResponseEntity<EventMessage> _handleEventException(EventException exception) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(exception.message.filterFor(configuration.getRestResponse()), headers, HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<EventMessage> _handleAnyException(Throwable exception) {
        if (exception instanceof EventException)
            return _handleEventException((EventException) exception);

        EventCapturer ec = new EventCapturer(configuration);
        EventMessage message = ec.capture(exception);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(message.filterFor(configuration.getRestResponse()),
                headers, HttpStatus.I_AM_A_TEAPOT);
    }

}
