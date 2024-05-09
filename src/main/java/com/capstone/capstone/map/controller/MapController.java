package com.capstone.capstone.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

    @Value("${kakao.map.api-key}")
    private String jsKey;
    @Value("${kakao.map.restapi-key}")
    private String kakaoMapApiKey;

    @GetMapping("/map")
    public String map(Model model) {
        model.addAttribute("jsKey", jsKey);
        model.addAttribute("restapiKey", kakaoMapApiKey);
        return "map";
    }


}
