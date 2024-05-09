package com.capstone.capstone.breadRecommend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BreadRecommendController {

    @GetMapping("/bread")
    public String breadRecommend(){
        return "breadPage";
    }
}
