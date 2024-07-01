package com.tribium.eventer.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventMessage {

    /**
     * Each event message can have embedded a list of problems. This feature is
     * especially useful for data
     * structure validation purposes.
     */
    public List<String> messages;

    public List<String> possibleCauses;
    public List<String> howToFix;
    public String id;
    public String templateId;
    public String exceptionClassName;
    public Date emittedAt;
    public ExceptionLocation location;
    public ArrayList<Object> stackTrace;
    public ExceptionExceptionCause cause;
    /**
     * Context is a list of variable values that are important to determine the
     * context of the message.
     * They are specified at the place where this event is emitted and their purpose
     * is to help in finding
     * the causes of the message or place it in specific situational context.
     * This can be also a way to dump some variable names for debugging or error
     * fixing purposes etc.
     */
    public List<String> context;
    private String message;


    public EventMessage() {
        this.id = UUID.randomUUID().toString();
    }

    public EventMessage(String templateId, Object... context) {
        this();
        EventCapturer mc = new EventCapturer();
        mc.furnishContent(this, templateId, context);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        EventCapturer mc = new EventCapturer();
        this.message = mc.furnishMessage(message, this.context);
    }

    public void addMessage(String templateId) {
        if (messages == null)
            messages = new ArrayList<>();

        EventCapturer ec = new EventCapturer();
        MessageTemplate mt = ec.findTemplate(templateId);
        if (mt == null)
            throw new RuntimeException(String.format("The template with 'templateId' (%s) was not found.", templateId));

        messages.add(mt.getMessage());
    }

    public void addMessageText(String msg) {
        if (messages == null)
            messages = new ArrayList<>();
        messages.add(msg);
    }

    public void addContext(Object[] context) {
        if (context != null && context.length != 0) {
            if (this.context == null)
                this.context = new ArrayList<>();
            for (Object o : context)
                this.context.add(o.toString());
        }
    }

    public void emitThrowOnMessages() {
        if (messages != null && !messages.isEmpty()) {
            EventHandlingConfiguration eventHandlingConfiguration = EventHandlingConfiguration.getConfig();
            if (eventHandlingConfiguration.getCapture().isEnabled()) {
                EventCapturer mc = new EventCapturer();
                mc.captureTimeLocation(this);
            }
            Emitter.emitThrow(this);
        }
    }

    public EventMessage filterFor(EventHandlingConfiguration.MessageContent configuration) {
        EventMessage e = new EventMessage();
        e.id = this.id;

        if (configuration.isTemplateId())
            e.templateId = this.templateId;

        if (configuration.isMessage()) {
            e.message = this.message;
            e.messages = this.messages;
        }

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

        if (configuration.isExceptionInfo())
            e.exceptionClassName = this.exceptionClassName;

        if (configuration.isContext())
            e.context = this.context;

        return e;
    }

    public String toString(EventHandlingConfiguration.MessageContent c) {
        if (c.getFormat() == EventHandlingConfiguration.MessageContent.Format.Text) {
            if (c.isMultiLine())
                return new org.apache.commons.lang3.builder.ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                        .append("cause", cause)
                        .append("context", context)
                        .append("emittedAt", emittedAt)
                        .append("exceptionClassName", exceptionClassName)
                        .append("howToFix", howToFix)
                        .append("id", id)
                        .append("location", location)
                        .append("message", message)
                        .append("messages", messages)
                        .append("possibleCauses", possibleCauses)
                        .append("stackTrace", stackTrace)
                        .append("templateId", templateId)
                        .toString();
            return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                    .append("cause", cause)
                    .append("context", context)
                    .append("emittedAt", emittedAt)
                    .append("exceptionClassName", exceptionClassName)
                    .append("howToFix", howToFix)
                    .append("id", id)
                    .append("location", location)
                    .append("message", message)
                    .append("messages", messages)
                    .append("possibleCauses", possibleCauses)
                    .append("stackTrace", stackTrace)
                    .append("templateId", templateId)
                    .toString();
        } else if (c.getFormat() == EventHandlingConfiguration.MessageContent.Format.JSON) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            if (c.isMultiLine())
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            try {
                return mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return messages.toString();
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("cause", cause)
                .append("context", context)
                .append("emittedAt", emittedAt)
                .append("exceptionClassName", exceptionClassName)
                .append("howToFix", howToFix)
                .append("id", id)
                .append("location", location)
                .append("message", message)
                .append("messages", messages)
                .append("possibleCauses", possibleCauses)
                .append("stackTrace", stackTrace)
                .append("templateId", templateId)
                .toString();
    }
}
