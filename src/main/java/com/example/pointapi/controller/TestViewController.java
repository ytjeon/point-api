package com.example.pointapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestViewController {
    @GetMapping("/testview")
    public String testView(){
        System.out.println("testview");
        return "pages/view";
    }
}
