package com.tribium.eventer.tryout;

import com.tribium.eventer.core.EventMessageTemplate;

public class SampleErrorMessage extends EventMessageTemplate {
    public static SampleErrorMessage Message00 = new SampleErrorMessage("msg00", "Error has happened!");

    public SampleErrorMessage(String templateId, String message) {
        super(templateId, message);
    }
}
