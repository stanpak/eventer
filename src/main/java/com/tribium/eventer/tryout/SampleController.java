package com.tribium.eventer.tryout;

import com.tribium.eventer.core.*;
import com.tribium.eventer.rest.BaseController;
import com.tribium.eventer.rest.StringWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a sample controller to test various simple scenarios that may happen in code
 * and to see how they will be handled.
 */
@RestController
@RequestMapping("/tryout")
public class SampleController extends BaseController {

    /**
     * Scenario when the RuntimeException was thrown somewhere in the code.
     */
    @GetMapping("/throwRuntimeException")
    public void throwRuntimeException() {
        throw new RuntimeException("Sample exception");
    }

    /**
     * Scenario when the basic checked Exception was thrown somewhere in the code.
     */
    @GetMapping("/throwException")
    public void throwException() throws Exception {
        throw new Exception("Sample exception");
    }

    @GetMapping("/nullPointerException")
    public void nullPointerException() throws NullPointerException {
        throw new NullPointerException();
    }

    @GetMapping("/classCastException")
    public void classCastException() throws Exception {
        Object obj = "Hello";
        System.out.println(obj);
    }

    @GetMapping("/classNotFoundException")
    public void classNotFoundException() throws Exception {
        Class.forName("mu.dummy.Class");
    }

    /**
     * Scenario when the EventException was thrown somewhere in the code.
     * In this case the exception uses message templating mechanism.
     */
    @GetMapping("/throwEventException")
    public void throwEventException() {
        throw new EventException("msg01");
    }

    /**
     * Scenario when the EventException was thrown somewhere in the code.
     * In this case the exception uses message templating mechanism
     * and attaches additional context.
     */
    @GetMapping("/throwEventExceptionWithContext")
    public void throwEventExceptionWithContext() {
        throw new EventException("msg00", "myContext");
    }

    /**
     * Just for the sanity sake we include here the response that does not carry any errors
     * or other functionality.
     */
    @GetMapping("/string")
    public String string() {
        return "hello!";
    }

    @GetMapping("/string2")
    public StringWrapper string2() {
        return new StringWrapper("hello!");
    }

    @GetMapping("/nothing")
    public void nothing() {}

    @GetMapping("/double")
    public double numberDouble() { return 10.2; }

    @GetMapping("/float")
    public float numberFloat() { return 10.2f; }

    @GetMapping("/integer")
    public int numberInteger() { return 10; }

    /**
     * Just to test if any object can be returned without the issue.
     * @return
     */
    @GetMapping("/object")
    public ExceptionLocation object() {
        return new ExceptionLocation();
    }

    /**
     * Emission of the event message using the templating mechanism.
     * This code that should not cause any errors to be reported back to the caller.
     */
    @GetMapping("/emit")
    public void emit() {
        Emitter.emit("msg01");
    }

    @GetMapping("/emitMessage")
    public void emitMessage() {
        Emitter.emitMessage("This is the text of my message");
    }

    /**
     * This is the test call to emit a message with direct text plus additional context variables.
     * This time the %s notation is used. Number of context variables must match the number of '%s' in the message.
     */
    @GetMapping("/emitMessageWithContext")
    public void emitMessageWithContext() {
        Emitter.emitMessage("This is the text of my message. context: v1: %s, v2: %s", 10, "value");
    }

    /**
     * This is the test call to emit a message with direct text plus additional context variables.
     * This time different notation is used: using the {N}.
     */
    @GetMapping("/emitMessageWithContext2")
    public void emitMessageWithContext2() {
        Emitter.emitMessage("This is the text of my message. context: v1: {0}, v2: {1}", 10, "value", 20);
    }

    /**
     * Emission of the event message using the templating mechanism by employing the "enum"
     * classes as event message template definitions.
     * This code that should not cause any errors to be reported back to the caller.
     */
    @GetMapping("/emitUsingEnum")
    public void emitUsingEnum() {
        Emitter.emit(SampleErrorMessage.Message00);
    }

    /**
     * Emission of the event message using the templating mechanism.
     * It also attaches to the message an additional contextual information from the code.
     * This code that should not cause any errors to be reported back to the caller.
     */
    @GetMapping("/emitWithContext")
    public void emitWithContext() {
        Emitter.emit("msg00", "myContext");
    }

    /**
     * Emission of the event message using the templating mechanism.
     * <p>
     * In addition to the event message being emitted this code also causes the exception
     * to be thrown (EventException). The response should contain 418 status code.
     */
    @GetMapping("/emitThrow")
    public void emitThrow() {
        Emitter.emitThrow("msg01");
    }

    /**
     * Emission of the event message using the templating mechanism.
     * It also attaches to the message an additional contextual information from the code.
     * <p>
     * In addition to the event message being emitted this code also causes the exception
     * to be thrown (EventException). The response should contain 418 status code.
     */
    @GetMapping("/emitThrowWithContext")
    public void emitThrowWithContext() {
        Emitter.emitThrow("msg00", "myContext");
    }

    @GetMapping("/multipleMessages")
    public void multipleMessages() {
        EventMessage m = new EventMessage("msg02");
        m.addMessageText("Problem #1 has happened.");
        m.addMessage("msg03");
        m.emitThrowOnMessages();
    }
}
