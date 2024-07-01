package com.tribium.eventer.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EventDistributor {
    private static final Logger logger = Logger.getLogger(Emitter.class.getName());
    /**
     * Distributes the message to various destinations depending on the settings.
     * Primary destinations are LOG and CONSOLE.
     * @param error
     * @param eventHandlingConfiguration
     * @param message
     */
    public static void distribute(boolean error, EventHandlingConfiguration eventHandlingConfiguration, EventMessage message) {
        EventHandlingConfiguration.MessageContent c = eventHandlingConfiguration.getLog();
        if (c.isEnabled()) {
            EventMessage m = message.filterFor(c);
            String t = m.toString(c);

            // TODO log the message. We may change it in the future
            logger.log(error ? Level.SEVERE : Level.INFO, t);
        }

        c = eventHandlingConfiguration.getConsole();
        if (c.isEnabled()) {
            EventMessage m = message.filterFor(c);
            String t = m.toString(c);

            // TODO Display the message to the console. For now we just dump it to stdout.
            System.out.println(t);
        }
    }
}
