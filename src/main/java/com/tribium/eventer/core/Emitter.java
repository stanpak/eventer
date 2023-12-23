package com.tribium.eventer.core;


import java.util.logging.Level;
import java.util.logging.Logger;

public class Emitter {
    private static Logger logger= Logger.getLogger(Emitter.class.getName());
    public static EventMessage emit(String templateId,  Object... context){
        return _emit(templateId,false,context);
    }

    private static EventMessage _emit(String templateId, boolean error, Object... context){
        Configuration configuration = Configuration.getConfig();
        if(configuration.getCapture().isEnabled()) {
            EventCapturer mc = new EventCapturer();
            EventMessage message = mc.capture(templateId, context);

            if (configuration.getLog().isEnabled()) {
                EventMessage m = message.filterFor(configuration.getLog());

                // TODO log the message
                logger.log(error? Level.SEVERE:Level.INFO,m.message);
            }

            if (configuration.getCapture().isEnabled()) {
                EventMessage m = message.filterFor(configuration.getConsole());

                // TODO Display the message to the console
                System.out.println(m.message);
            }
            return message;
        }
        return null;
    }

    public static void emitThrow(String templateId, Object... context){
        EventMessage m = _emit(templateId, true, context);
        throw new EventException(m);
    }
}
