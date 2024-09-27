package com.aixbox.examplespringbootprobider;

import com.aixbox.rpcspringbootstarter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class ExampleSpringbootProbiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootProbiderApplication.class, args);
    }

}
