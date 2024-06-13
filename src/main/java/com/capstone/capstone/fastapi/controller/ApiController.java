package com.capstone.capstone.fastapi.controller;

import com.capstone.capstone.fastapi.dto.RouteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
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

        log.info("time = " + routeRequest.getData());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("data", routeRequest.getData().toString());
        requestBody.put("start", routeRequest.getStart());


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        Map<String, Object> result = new HashMap<>();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(FASTAPI_URL + "/predict", entity, Map.class);
            log.info(FASTAPI_URL+"/predict");
            Map<String, Integer> responseBody = response.getBody();
            log.info("responseBody = " + responseBody.toString());

            if (responseBody != null) {
                result.put("routeATime", responseBody.get("RouteA Time"));
                result.put("routeBTime", responseBody.get("RouteB Time"));
                result.put("routeCTime", responseBody.get("RouteC Time"));
            } else {
                log.error("Response body is null");
                result.put("routeATime", "Error");
                result.put("routeBTime", "Error");
                result.put("routeCTime", "Error");
            }
        } catch (HttpServerErrorException e) {
            log.error("Server error in /predict: " + e.getMessage());
            result.put("routeATime", "Error");
            result.put("routeBTime", "Error");
            result.put("routeCTime", "Error");
        } catch (RestClientException e) {
            log.error("Client error in /predict: " + e.getMessage());
            result.put("routeATime", "Error");
            result.put("routeBTime", "Error");
            result.put("routeCTime", "Error");
        }

        return ResponseEntity.ok(result);
    }

}
