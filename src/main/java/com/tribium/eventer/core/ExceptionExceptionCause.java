package com.tribium.eventer.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ExceptionExceptionCause {
    public String message;

    @JsonProperty("class")
    public String clazz;

    public List<String> stackTrace;

    public ExceptionExceptionCause cause;

    public ExceptionExceptionCause() {
    }

    public ExceptionExceptionCause(Throwable cause) {
        message = cause.getLocalizedMessage();

        clazz = cause.getClass().getCanonicalName();

        StackTraceElement[] st = cause.getStackTrace();
        if (st != null) {
            stackTrace = new ArrayList<>();
            for (StackTraceElement e : st)
                stackTrace.add(e.toString());
        }

        if (cause.getCause() != null)
            this.cause = new ExceptionExceptionCause(cause.getCause());
    }
}
