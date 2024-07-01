package com.tribium.eventer.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties("event-handling")
public class EventHandlingConfiguration {
    private static EventHandlingConfiguration config;
    private final MessageContent capture = new MessageContent();
    private final MessageContent console = new MessageContent();
    /**
     * Option that tells the engine to get any response from the controllers and wrap them in the
     * JSON envelope structure with "success" flag and the content property ("error" or "data").
     * In such scenario status 419 is converted to OK and the only way to tell that there was error
     * is to check the value of the "success" property.
     */
    private boolean wrapResponse = true;
    private MessageContent restResponse = new MessageContent();
    private MessageContent log = new MessageContent();
    private List<MessageTemplate> templates;

    public static EventHandlingConfiguration getConfig() {
        return config;
    }

    /**
     * This is the alternative method that can be used to initialize the framework, whe the Spring Framework initialization
     * cannot be used for whatever reason.
     * The configuration file can be provided in the parameter.
     */
    public static void initializeFromYml(String ymlConfigFilePath, String context) throws IOException {
        config = getConfigFromYml(ymlConfigFilePath, context);
    }

    /**
     * Creates new instance of the Configuration directly from the provided YML file.
     *
     * @param ymlConfigFilePath
     * @return
     * @throws IOException
     */
    public static EventHandlingConfiguration getConfigFromYml(String ymlConfigFilePath, String context) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        ClassPathResource resource = new ClassPathResource(ymlConfigFilePath);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String jsonString = new String(bytes, StandardCharsets.UTF_8);

        JsonNode node = mapper.readTree(jsonString);
        if (context != null)
            node = node.get(context);

        if(node == null)
            throw new RuntimeException("The context node cannot be found in the configuration file! Please make sure that the configuration file is there in the classpath under its name ('"+ymlConfigFilePath+"') and the context element in this file is the correct one ('"+context+"').");

        return mapper.treeToValue(node, EventHandlingConfiguration.class);
    }

    public boolean isWrapResponse() {
        return wrapResponse;
    }

    public void setWrapResponse(boolean wrapResponse) {
        this.wrapResponse = wrapResponse;
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

    /**
     * This method is invoked by the Spring Framework that is presumably initiating the structure from the configuration file
     * and the singleton of that class is created in the Spring context.
     */
    @PostConstruct
    private void springFrameworkInitialize() {
        config = this;
    }

    public static class MessageContent {
        private boolean enabled = true;
        private Format format = Format.Text;
        private boolean multiLine = false;
        private boolean templateId = true;
        private boolean timing = true;
        private boolean message = true;
        private boolean causes = true;
        private boolean context = true;
        private boolean howToFix = true;
        private boolean exceptionInfo = true;
        private boolean stackTrace = true;
        private boolean location = true;

        public Format getFormat() {
            return format;
        }

        public void setFormat(Format format) {
            this.format = format;
        }

        public boolean isMultiLine() {
            return multiLine;
        }

        public void setMultiLine(boolean multiLine) {
            this.multiLine = multiLine;
        }

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

        public enum Format {
            Text, JSON
        }
    }
}
