package com.tribium.eventer.core;

import java.util.List;

public class MessageTemplate {
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPossibleCauses() {
        return possibleCauses;
    }

    public void setPossibleCauses(List<String> possibleCauses) {
        this.possibleCauses = possibleCauses;
    }

    public List<String> getHowToFix() {
        return howToFix;
    }

    public void setHowToFix(List<String> howToFix) {
        this.howToFix = howToFix;
    }

    public String templateId;
    public String message;

    public List<String> possibleCauses;

    public List<String> howToFix;
}
