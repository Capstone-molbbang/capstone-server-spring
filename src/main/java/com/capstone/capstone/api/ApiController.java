package com.capstone.capstone.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiController {
    private final RestTemplate restTemplate;
    private static final String FASTAPI_URL = "http://3.34.126.202/test";
    @PostMapping("/from-spring")
    public ResponseEntity<String> test() {

        String dataToSend = "Hello from Spring Boot";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("data", dataToSend);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(FASTAPI_URL, entity, String.class);

        return ResponseEntity.ok().body(response.getBody());
    }


    @PostMapping("/from-fastapi")
    public ResponseEntity<String> receiveData(@RequestBody Map<String, Object> data) {
        System.out.println("Received data: " + data);

        return new ResponseEntity<>("Data received successfully", HttpStatus.OK);
    }
}
