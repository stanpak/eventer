package com.tribium.eventer.tryout;

import com.tribium.eventer.core.EventMessageTemplate;

/**
 * This class is an example of how the event message templates can be defined in code (hardcoded)
 * in an organized way.
 * <p>
 * This is quick and dirty solution for the situations when the externalization
 * of the templates in not really necessary and the messages do not really need to be revised and modified
 * in the future without rebuilding and redeployment of the system.
 * <p>
 * To use this definition in code just follow the example below:
 * <p>
 * Emitter.emit(SampleErrorMessage.Message00, ...);
 * <p>
 * or:
 * <p>
 * Emitter.emitThrow(SampleErrorMessage.Message00, ...);
 * <p>
 * or:
 * <p>
 * throw EventException(SampleErrorMessage.Message00, ...);
 */
public class SampleErrorMessage extends EventMessageTemplate {
    public static SampleErrorMessage Message00 = new SampleErrorMessage("msg00", "Error has happened!");

    public SampleErrorMessage(String templateId, String message) {
        super(templateId, message);
    }
}
