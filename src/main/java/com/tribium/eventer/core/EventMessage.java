package com.tribium.eventer.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventMessage {

    /**
     * Each event message can have embedded a list of problems. This feature is especially useful for data
     * structure validation purposes.
     */
    public List<String> messages;

    public List<String> possibleCauses;
    public List<String> howToFix;
    public String id;
    public String templateId;
    public String exceptionClassName;
    public String message;
    public Date emittedAt;
    public ExceptionLocation location;
    public ArrayList<Object> stackTrace;
    public ExceptionExceptionCause cause;

    public EventMessage() {
        this.id = UUID.randomUUID().toString();
    }

    public EventMessage(String templateId, Object... context) {
        this();
        EventCapturer mc = new EventCapturer();
        mc.furnishContent(this, templateId, context);
    }

    public void addMessage(String templateId) {
        if (messages == null)
            messages = new ArrayList<>();

        EventCapturer ec = new EventCapturer();
        MessageTemplate mt = ec.findTemplate(templateId);
        if (mt == null)
            throw new RuntimeException(String.format("The template with 'templateId' (%s) was not found.", templateId));

        messages.add(mt.getMessage());
    }

    public void addMessageText(String msg) {
        if (messages == null)
            messages = new ArrayList<>();
        messages.add(msg);
    }

    public void emitThrowOnMessages() {
        if (messages != null && !messages.isEmpty()) {
            Configuration configuration = Configuration.getConfig();
            if (configuration.getCapture().isEnabled()) {
                EventCapturer mc = new EventCapturer();
                mc.captureTimeLocation(this);
            }
            Emitter.emitThrow(this);
        }
    }

    public EventMessage filterFor(Configuration.MessageContent configuration) {
        EventMessage e = new EventMessage();
        e.id = this.id;

        if (configuration.isTemplateId())
            e.templateId = this.templateId;

        if (configuration.isMessage()) {
            e.message = this.message;
            e.messages = this.messages;
        }

        // Add the time of the event...
        if (configuration.isTiming())
            e.emittedAt = this.emittedAt;

        // Add the location information...
        if (configuration.isLocation())
            e.location = this.location;

        if (configuration.isStackTrace())
            e.stackTrace = this.stackTrace;

        if (configuration.isCauses()) {
            e.cause = this.cause;
            e.possibleCauses = this.possibleCauses;
        }

        if (configuration.isHowToFix())
            e.howToFix = this.howToFix;

        if (configuration.isExceptionInfo())
            e.exceptionClassName = this.exceptionClassName;

        return e;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "possibleCauses=" + possibleCauses +
                ", howToFix=" + howToFix +
                ", id='" + id + '\'' +
                ", templateId='" + templateId + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", message='" + message + '\'' +
                ", messages='" + messages + '\'' +
                ", emittedAt=" + emittedAt +
                ", location=" + location +
                ", stackTrace=" + stackTrace +
                ", cause=" + cause +
                '}';
    }
}
