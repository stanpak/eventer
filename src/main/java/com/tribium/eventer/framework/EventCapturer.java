package com.tribium.eventer.framework;

import java.util.ArrayList;
import java.util.Date;

public class EventCapturer {

    private final Configuration configuration;

    public EventCapturer() {
        this.configuration = Configuration.config;
    }

    public EventCapturer(Configuration configuration) {
        this.configuration = configuration;
    }

    public EventMessage capture(String templateId, Object... context) {

        if (templateId == null)
            throw new RuntimeException("The 'templateId' was not provided.");

        // Find the template...
        MessageTemplate mt = findTemplate(templateId);
        if (mt == null)
            throw new RuntimeException(String.format("The template with 'templateId' (%s) was not found.", templateId));

        // Now build the response body...
        EventMessage m = new EventMessage();

        m.templateId = templateId;

        if (context != null)
            m.message = String.format(mt.message, context);
        else
            m.message = mt.message;

        // Add the time of the event...
        m.emittedAt = new Date();

        // Add the location information...
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        m.location = new ExceptionLocation(stackTrace[0]);

        // Add eny stack trace information...
        m.stackTrace = new ArrayList<>();
        for (StackTraceElement e : stackTrace)
            m.stackTrace.add(e.toString());

        if (mt.possibleCauses != null)
            m.possibleCauses = mt.possibleCauses;

        if (mt.howToFix != null)
            m.howToFix = mt.howToFix;

        return m;
    }

    private MessageTemplate findTemplate(String templateId) {
        if (configuration.templates != null)
            for (MessageTemplate mt : configuration.templates)
                if (mt.templateId.equals(templateId))
                    return mt;
        return null;
    }

    public EventMessage capture(Throwable exception, Object... context) {
        EventMessage m = new EventMessage();

        m.templateId = null;

        // Now build the response body...
        m.ExceptionClassName = exception.getClass().getCanonicalName();

        if (exception.getLocalizedMessage() != null)
            m.message = exception.getLocalizedMessage();

        // Add the time of the event...
        m.emittedAt = new Date();

        // Add the location information...
        StackTraceElement[] stackTrace = exception.getStackTrace();
        m.location = new ExceptionLocation(stackTrace[0]);

        // Add eny stack trace information...
        StackTraceElement[] st = exception.getStackTrace();
        if (st != null) {
            m.stackTrace = new ArrayList<>();
            for (StackTraceElement e : st)
                m.stackTrace.add(e.toString());
        }

        // Add the information on the causal exception chain...
        if (exception.getCause() != null)
            m.cause = new ExceptionExceptionCause(exception.getCause());

        // Add contextual information if provided...
//        if(exception instanceof EventException)
//        {
//            EventException ex = ((EventException) exception);
//            List<ExceptionContext> cl = ex.getContextList();
//            if(cl != null)
//            {
//                response.context = new ArrayList<>();
//                response.context.addAll(cl);
//            }
//
//            // Add any advice information if it is available
//            if(ex.getAdvice() != null)
//                response.advice = ex.getAdvice();
//        }

        return m;
    }
}
