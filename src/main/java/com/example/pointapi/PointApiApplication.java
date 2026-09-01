package com.example.pointapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.example.pointapi","com.example.pointapi.mapper","com.example.pointapi.model"})
public class PointApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointApiApplication.class, args);
    }

}
