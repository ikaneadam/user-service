package com.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;


@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "/api");
        System.setProperty("server.port", "9080");
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
