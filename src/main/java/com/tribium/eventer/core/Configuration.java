package com.tribium.eventer.core;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties("error-handling")
public class Configuration {
    private static Configuration config;
    private final MessageContent capture = new MessageContent();
    private final MessageContent console = new MessageContent();
    private MessageContent restResponse = new MessageContent();
    private MessageContent log = new MessageContent();
    private List<MessageTemplate> templates;

    public static Configuration getConfig() {
        return config;
    }

    public MessageContent getConsole() {
        return console;
    }

    public MessageContent getLog() {
        return log;
    }

    public void setLog(MessageContent log) {
        this.log = log;
    }

    public MessageContent getCapture() {
        return capture;
    }

    public MessageContent getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(MessageContent restResponse) {
        this.restResponse = restResponse;
    }

    public List<MessageTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<MessageTemplate> templates) {
        this.templates = templates;
    }

    @PostConstruct
    private void initialize() {
        config = this;
    }

    public static class MessageContent {
        private boolean enabled = true;
        private boolean templateId = true;
        private boolean timing = true;
        private boolean message = true;
        private boolean causes = true;
        private boolean context = true;
        private boolean howToFix = true;
        private boolean exceptionInfo = true;
        private boolean stackTrace = true;
        private boolean location = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isTemplateId() {
            return templateId;
        }

        public void setTemplateId(boolean templateId) {
            this.templateId = templateId;
        }

        public boolean isTiming() {
            return timing;
        }

        public void setTiming(boolean timing) {
            this.timing = timing;
        }

        public boolean isMessage() {
            return message;
        }

        public void setMessage(boolean message) {
            this.message = message;
        }

        public boolean isCauses() {
            return causes;
        }

        public void setCauses(boolean causes) {
            this.causes = causes;
        }

        public boolean isContext() {
            return context;
        }

        public void setContext(boolean context) {
            this.context = context;
        }

        public boolean isHowToFix() {
            return howToFix;
        }

        public void setHowToFix(boolean howToFix) {
            this.howToFix = howToFix;
        }

        public boolean isExceptionInfo() {
            return exceptionInfo;
        }

        public void setExceptionInfo(boolean exceptionInfo) {
            this.exceptionInfo = exceptionInfo;
        }

        public boolean isStackTrace() {
            return stackTrace;
        }

        public void setStackTrace(boolean stackTrace) {
            this.stackTrace = stackTrace;
        }

        public boolean isLocation() {
            return location;
        }

        public void setLocation(boolean location) {
            this.location = location;
        }
    }
}
