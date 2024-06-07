package com.capstone.capstone.fastapi.controller;

import com.capstone.capstone.fastapi.dto.RouteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final RestTemplate restTemplate;
    private static final String FASTAPI_URL = "http://3.34.126.202";
    @PostMapping("/from-spring")
    public ResponseEntity<String> test() {

        String dataToSend = "Hello from Spring Boot";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("data", dataToSend);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(FASTAPI_URL+"/from-spring", entity, String.class);

        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("/from-fastapi")
    public ResponseEntity<Map<String, Object>> receiveData(@RequestBody Map<String, Object> data) {
        System.out.println("Received data: " + data);
        data.put("processed", true);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/api/departureTime")
    public ResponseEntity<Map<String, Object>> sendDepartureTime(@RequestBody RouteRequest routeRequest) {

        log.info("time = " + routeRequest.getDepartureTime());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("data", routeRequest.getDepartureTime().toString());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

      //   ResponseEntity<Map> responseC = restTemplate.postForEntity(FASTAPI_URL + "/predict_router3", entity, Map.class);

        Map<String, Object> result = new HashMap<>();


        try {
            ResponseEntity<Map> responseA = restTemplate.postForEntity(FASTAPI_URL + "/predict_router1", entity, Map.class);
            Map<String, Object> responseBodyA = responseA.getBody();
            log.info("responseBodyA = " + responseBodyA.toString());
            result.put("routeATime", responseBodyA.get("RouteA Time"));
        } catch (HttpServerErrorException e) {
            log.error("Error in /predict_router1: " + e.getMessage());
            result.put("routeATime", "Error");
        }

        try {
            ResponseEntity<Map> responseB = restTemplate.postForEntity(FASTAPI_URL + "/predict_router2", entity, Map.class);
            Map<String, Object> responseBodyB = responseB.getBody();
            log.info("responseBodyB = " + responseBodyB.toString());
            result.put("routeCTime", responseBodyB.get("RouteB time"));
        } catch (HttpServerErrorException e) {
            log.error("Error in /predict_router2: " + e.getMessage());
            result.put("routeCTime", "Error");
        }
        return ResponseEntity.ok(result);
    }

}
