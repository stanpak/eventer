package com.tribium.eventer.core;


import java.util.logging.Logger;

public class Emitter {

    /**
     * Emits a message using just plain text.
     * This type of message will be a regular message as if they were emitted using `emit` method.
     * (So they will be logged as INFO.)
     *
     * @param message
     * @param context
     * @return
     */
    public static EventMessage emitMessage(String message, Object... context) {
        return _emitMessage(message, false, context);
    }

    public static EventMessage emit(String templateId, Object... context) {
        return _emit(templateId, false, context);
    }

    public static EventMessage emit(EventMessageTemplate template, Object... context) {
        return _emit(template.getTemplateId(), false, context);
    }

    private static EventMessage _emitMessage(String message, boolean error, Object... context) {
        EventHandlingConfiguration eventHandlingConfiguration = EventHandlingConfiguration.getConfig();
        if (eventHandlingConfiguration.getCapture().isEnabled()) {
            EventMessage m = new EventMessage();
            m.addContext(context);
            m.setMessage(message);
            EventCapturer mc = new EventCapturer();
            mc.captureTimeLocation(m);
            EventDistributor.distribute(error, eventHandlingConfiguration, m);
            return m;
        }
        return null;
    }

    private static EventMessage _emit(String templateId, boolean error, Object... context) {
        EventHandlingConfiguration eventHandlingConfiguration = EventHandlingConfiguration.getConfig();
        if (eventHandlingConfiguration.getCapture().isEnabled()) {
            EventMessage message = new EventMessage(templateId, context);
            EventCapturer mc = new EventCapturer();
            mc.captureTimeLocation(message);
            EventDistributor.distribute(error, eventHandlingConfiguration, message);
            return message;
        }
        return null;
    }

    public static void emitThrow(String templateId, Object... context) {
        EventMessage m = _emit(templateId, true, context);
        throw new EventException(m);
    }

    public static void emitThrow(EventMessageTemplate template, Object... context) {
        EventMessage m = _emit(template.getTemplateId(), true, context);
        throw new EventException(m);
    }

    public static void emitThrow(EventMessage m) {
        throw new EventException(m);
    }

    public static void emit(EventMessage message) {
        EventHandlingConfiguration eventHandlingConfiguration = EventHandlingConfiguration.getConfig();
        if (eventHandlingConfiguration.getCapture().isEnabled()) {
            EventCapturer mc = new EventCapturer();
            mc.captureTimeLocation(message);
        }
        EventDistributor.distribute(false, eventHandlingConfiguration, message);
    }
}
