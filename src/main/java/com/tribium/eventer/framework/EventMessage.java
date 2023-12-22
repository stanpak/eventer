package com.tribium.eventer.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventMessage {

    public List<String> possibleCauses;

    public List<String> howToFix;

    public EventMessage(){
        this.id = UUID.randomUUID().toString();
    }

    public String id;
    public String templateId;
    public String ExceptionClassName;
    public String message;
    public Date emittedAt;
    public ExceptionLocation location;
    public ArrayList<Object> stackTrace;
    public ExceptionExceptionCause cause;

    public EventMessage filterFor(Configuration.MessageContent configuration) {
        EventMessage e = new EventMessage();
        e.id = this.id;

        if (configuration.isTemplateId())
            e.templateId = this.templateId;

        if (configuration.isMessage())
            e.message = this.message;

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

//        if (configuration.context)
//            e.context = this.stackTrace;

        return e;
    }

}
