package com.tribium.eventer.tryout;

import com.tribium.eventer.framework.BaseController;
import com.tribium.eventer.framework.EventException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tryout")
public class SampleController extends BaseController {
    @GetMapping("/throwRuntimeException")
    public void throwRuntimeException(){
        throw new RuntimeException("Sample exception");
    }

    @GetMapping("/throwException")
    public void throwException() throws Exception {
        throw new Exception("Sample exception");
    }

    @GetMapping("/throwEventException")
    public void throwEventException(){
        throw new EventException("msg01");
    }

    @GetMapping("/throwEventExceptionWithContext")
    public void throwEventExceptionWithContext(){
        throw new EventException("msg00","myContext");
    }

}
