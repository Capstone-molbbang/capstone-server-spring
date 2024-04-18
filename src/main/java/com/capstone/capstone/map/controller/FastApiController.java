package com.capstone.capstone.map.controller;

import com.capstone.capstone.map.dto.DistanceDto;
import com.capstone.capstone.map.dto.DistanceRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestController
@Slf4j
public class FastApiController {
    private static final String FASTAPI_ENDPOINT = "http://fastapi-server-address/api/coordinates";


    /**
     * 출발지, 도착지 좌표 및 출발 예정 시간 전송
     */
    @PostMapping("/api/info")
    public ResponseEntity<Map<String, String>> sendInfoToFastApi(@RequestBody Map<String, Object> requestData){
        try {
            // 출발 좌표, 도착 좌표, 출발 시간을 추출
            Map<String, Double> startCoords = (Map<String, Double>) requestData.get("startCoords");
            Map<String, Double> destinationCoords = (Map<String, Double>) requestData.get("destinationCoords");
            String departureTime = (String) requestData.get("departureTime");

            log.info("startCoords : " + startCoords);
            log.info("destinationCoords : " + destinationCoords);
            log.info("departureTime : " + departureTime);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Info sent to FastAPI successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 요청 처리 중 오류 발생 시 클라이언트에게 오류 메시지 응답
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error sending info to FastAPI: " + e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    /**
     * 리스트 형식으로 예측 경로 받아옴!
     */
    @GetMapping("/path-prediction")
    public ResponseEntity<List<List<Double>>> getPathPredictionCoordinates() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(FASTAPI_ENDPOINT);

        try{
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if(statusCode == HttpStatus.SC_OK){
                String responseBody = EntityUtils.toString(response.getEntity());

                //FastApi로부터 받은 JSON 응답을 리스트로 변환하여 반환
                List<List<Double>> coordinates = parseCoordinates(responseBody);
                return ResponseEntity.ok(coordinates);
            }
            else{
                System.err.println("Failed to fetch coordinates from FastAPI server");
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
            }


        } catch (IOException e) {
            System.err.println("Error while fetching coordinates from FastAPI server: " + e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/api/distance")
    public ResponseEntity<DistanceRequestDto> toDistance(@RequestBody List<Double> distanceList) {
        log.info("Received distance list: {}", distanceList);
        DistanceRequestDto distanceDtoList = null;
        for (Double distance : distanceList) {
            distanceDtoList.addDistance(distance);
        }
        return ResponseEntity.ok(distanceDtoList);

//        HttpClient httpClient = HttpClients.createDefault();
//        HttpPost request = new HttpPost(FASTAPI_ENDPOINT);
//        try {
//            String requestBody = new ObjectMapper().writeValueAsString(distanceList);
//            StringEntity entity = new StringEntity(requestBody);
//            request.setEntity(entity);
//
//            HttpResponse response = httpClient.execute(request);
//
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            if(statusCode == HttpStatus.SC_OK){
//                String responseBody = EntityUtils.toString(response.getEntity());
//                DistanceDto distanceDto = parseDistanceDto(responseBody);
//                return ResponseEntity.ok(distanceDto);
//            }
//            else{
//                log.error("Failed to calculate distances. Status code: {}", statusCode);
//                return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
//            }
//        }catch (IOException e) {
//            log.error("Error while calculating distances: {}", e.getMessage());
//            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(null);
//        }
    }


    private DistanceDto parseDistanceDto(String responseBody) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseBody, DistanceDto.class);
    }
    private void sendToFastAPI(List<Double> startCoords, List<Double> destinationCoords, String departureTime) {
        // FastAPI 엔드포인트 URL
        String fastAPIEndpoint = "http://fastapi-server-address/api/info";

        // JSON 형태의 요청 본문 생성
        String requestBody = String.format("{\"startCoords\": %s, \"destinationCoords\": %s, \"departureTime\": \"%s\"}",
                startCoords, destinationCoords, departureTime);

        // HttpClient 생성
        HttpClient httpClient = HttpClients.createDefault();

        // HTTP POST 요청 생성
        HttpPost httpPost = new HttpPost(fastAPIEndpoint);

        // 요청 헤더 설정
        httpPost.setHeader("Content-Type", "application/json");

        try {
            // 요청 본문 설정
            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // HTTP 요청 실행
            HttpResponse response = httpClient.execute(httpPost);

            // 응답 상태 코드 확인
            int statusCode = response.getStatusLine().getStatusCode();

            // 응답 본문 가져오기
            HttpEntity responseEntity = response.getEntity();
            String responseContent = EntityUtils.toString(responseEntity);

            if (statusCode == 200) {
                // 성공적으로 요청이 처리된 경우
                System.out.println("Info sent to FastAPI successfully");
                System.out.println("Response from FastAPI: " + responseContent);
            } else {
                // 요청이 실패한 경우
                System.err.println("Failed to send info to FastAPI. Status code: " + statusCode);
                System.err.println("Response from FastAPI: " + responseContent);
            }
        } catch (IOException e) {
            // 요청 실행 중 오류 발생 시
            System.err.println("Error sending info to FastAPI: " + e.getMessage());
        }
    }

    private List<List<Double>> parseCoordinates(String responseBody){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(responseBody, new TypeReference<List<List<Double>>>() {});

        }catch (IOException e){
            System.err.println("Error while parsing coordinates from JSON: " + e.getMessage());
            return null;        }
    }
}
