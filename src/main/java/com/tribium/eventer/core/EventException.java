package com.tribium.eventer.core;

public class EventException extends RuntimeException {
    public EventMessage message;

    public EventException(String templateId, Throwable cause) {
        // TODO
        super("Message here", cause);
    }

    public EventException(String templateId, Throwable cause, Object... context) {
        // TODO
        super("Message here", cause);
    }

    public EventException(String templateId, Object... context) {
        EventCapturer mc = new EventCapturer();
        this.message = mc.capture(templateId, context);
    }

    public EventException(EventMessage message) {
        this.message = message;
    }

}
