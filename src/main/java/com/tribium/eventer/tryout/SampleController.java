package com.tribium.eventer.tryout;

import com.tribium.eventer.core.Emitter;
import com.tribium.eventer.core.EventException;
import com.tribium.eventer.rest.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tryout")
public class SampleController extends BaseController {
    @GetMapping("/throwRuntimeException")
    public void throwRuntimeException() {
        throw new RuntimeException("Sample exception");
    }

    @GetMapping("/throwException")
    public void throwException() throws Exception {
        throw new Exception("Sample exception");
    }

    @GetMapping("/throwEventException")
    public void throwEventException() {
        throw new EventException("msg01");
    }

    @GetMapping("/throwEventExceptionWithContext")
    public void throwEventExceptionWithContext() {
        throw new EventException("msg00", "myContext");
    }

    @GetMapping("/ok")
    public String ok() {
        return "OK";
    }

    @GetMapping("/emit")
    public void emit() {
        Emitter.emit("msg01");
    }

    @GetMapping("/emitUsingEnum")
    public void emitUsingEnum() {
        Emitter.emit(SampleErrorMessage.Message00);
    }

    @GetMapping("/emitWithContext")
    public void emitWithContext() {
        Emitter.emit("msg00", "myContext");
    }

    @GetMapping("/emitThrow")
    public void emitThrow() {
        Emitter.emitThrow("msg01");
    }

    @GetMapping("/emitThrowWithContext")
    public void emitThrowWithContext() {
        Emitter.emitThrow("msg00", "myContext");
    }
}
