package com.tribium.eventer;

import com.tribium.eventer.core.EventHandlingConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        EventHandlingConfiguration.initializeFromYml("application.yml","event-handling");
        SpringApplication.run(Application.class, args);
    }
}
