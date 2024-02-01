package com.tribium.eventer.core;

import java.text.MessageFormat;
import java.util.*;

public class EventCapturer {

    private final Configuration configuration;
    private final Map<String, MessageTemplate> templateMap = new HashMap<>();

    public EventCapturer() {
        this.configuration = Configuration.getConfig();
        if (this.configuration.getTemplates() != null)
            for (MessageTemplate t : this.configuration.getTemplates())
                templateMap.put(t.templateId, t);
    }

    public EventCapturer(Configuration configuration) {
        this.configuration = configuration;
    }

    void furnishContent(EventMessage m, String templateId, Object... context) {
        if (templateId == null)
            throw new RuntimeException("The 'templateId' was not provided.");

        // Find the template...
        MessageTemplate mt = findTemplate(templateId);
        if (mt == null)
            throw new RuntimeException(String.format("The template with 'templateId' (%s) was not found.", templateId));

        m.templateId = templateId;

        m.addContext(context);
        m.setMessage(mt.message);

        if (mt.possibleCauses != null)
            m.possibleCauses = mt.possibleCauses;

        if (mt.howToFix != null)
            m.howToFix = mt.howToFix;

    }

    String furnishMessage(String message, List<String> context) {
        if (message != null && context != null) {
            if (message.contains("%s"))
                return String.format(message, context.toArray());
            else
                return MessageFormat.format(message, context.toArray());
        }
        return message;
    }

    public void captureTimeLocation(EventMessage m) {
        // Add the time of the event...
        m.emittedAt = new Date();

        // Add the location information...
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        m.location = new ExceptionLocation(stackTrace[0]);

        // Add eny stack trace information...
        m.stackTrace = new ArrayList<>();
        for (StackTraceElement e : stackTrace)
            m.stackTrace.add(e.toString());
    }

    public MessageTemplate findTemplate(String templateId) {
        return templateMap.get(templateId);
    }

    public EventMessage capture(Throwable exception, Object... context) {
        EventMessage m = new EventMessage();

        m.templateId = null;

        // Now build the response body...
        m.exceptionClassName = exception.getClass().getCanonicalName();

        if (exception.getLocalizedMessage() != null)
            m.setMessage(exception.getLocalizedMessage());

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
