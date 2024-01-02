package com.tribium.eventer.tryout;

import com.tribium.eventer.core.Emitter;
import com.tribium.eventer.core.EventException;
import com.tribium.eventer.rest.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/** 
 * This is a sample controller to test various simple scenarios that may happen in code 
 * and to see how they will be handled.
 */
@Controller
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
     * Just for sanity sake we include here the response that does not carry any errors 
     * or other functionality.
     */
    @GetMapping("/ok")
    public String ok() {
        return "OK";
    }

    /**
     * Emission of the event message using the templating mechanism. 
     * This code that should not cause any errors to be reported back to the caller.
     */
    @GetMapping("/emit")
    public void emit() {
        Emitter.emit("msg01");
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
     * 
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
     * 
     * In addition to the event message being emitted this code also causes the exception 
     * to be thrown (EventException). The response should contain 418 status code.
     */
    @GetMapping("/emitThrowWithContext")
    public void emitThrowWithContext() {
        Emitter.emitThrow("msg00", "myContext");
    }
}
