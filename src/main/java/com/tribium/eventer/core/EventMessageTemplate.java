package com.tribium.eventer.core;

public class EventMessageTemplate {
    private final String templateId;
    private final String message;

    public EventMessageTemplate(String templateId, String message) {
        this.templateId = templateId;
        this.message = message;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getMessage() {
        return message;
    }
}