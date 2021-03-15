package com.github.webproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Component("com.github.webproxy.core")
@SpringBootApplication
public class WebproxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebproxyApplication.class, args);
    }

}
