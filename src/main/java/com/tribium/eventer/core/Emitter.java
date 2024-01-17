package com.tribium.eventer.core;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Emitter {
    private static final Logger logger = Logger.getLogger(Emitter.class.getName());

    public static EventMessage emit(String templateId, Object... context) {
        return _emit(templateId, false, context);
    }

    public static EventMessage emit(EventMessageTemplate template, Object... context) {
        return _emit(template.getTemplateId(), false, context);
    }

    private static EventMessage _emit(String templateId, boolean error, Object... context) {
        Configuration configuration = Configuration.getConfig();
        if (configuration.getCapture().isEnabled()) {
            EventMessage message = new EventMessage(templateId, context);
            EventCapturer mc = new EventCapturer();
            mc.captureTimeLocation(message);
            distribute(error, configuration, message);
            return message;
        }
        return null;
    }

    private static void distribute(boolean error, Configuration configuration, EventMessage message) {
        if (configuration.getLog().isEnabled()) {
            EventMessage m = message.filterFor(configuration.getLog());

            // TODO log the message
            logger.log(error ? Level.SEVERE : Level.INFO, m.toString());
        }

        if (configuration.getCapture().isEnabled()) {
            EventMessage m = message.filterFor(configuration.getConsole());

            // TODO Display the message to the console
            System.out.println(m.toString());
        }
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
        Configuration configuration = Configuration.getConfig();
        if (configuration.getCapture().isEnabled()) {
            EventCapturer mc = new EventCapturer();
            mc.captureTimeLocation(message);
        }
        distribute(false, configuration, message);
    }
}
