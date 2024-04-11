package com.capstone.capstone.map.service;

import com.capstone.capstone.map.dto.KakaoSearchDto;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMapService {

    @Value("${kakao.map.restapi-key}")
    private String kakaoMapRestApiKey;

    public KakaoSearchDto getKakaoSearch(String searchKeyword) {
        String restApiKey = "KakaoAK " + kakaoMapRestApiKey;
        // 요청 URL과 검색어를 담음
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + searchKeyword;
        // RestTemplate를 이용해
        RestTemplate restTemplate = new RestTemplate();

        // HTTPHeader를 설정해줘야 하기 때문에 생성함
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", restApiKey);
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // ResTemplate를 이용해 요청을 보내고 KakaoSearchDto로 받아 response에 담음
        ResponseEntity<KakaoSearchDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KakaoSearchDto.class
        );

        return response.getBody();
    }

    /**
     * 검색 관련 장소 받아오는 코드
     */
    public Map<String, String> getPlaceSuggestions(String query) {

        String restApiKey = "KakaoAK " + kakaoMapRestApiKey;
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", restApiKey);
        headers.set("Accept", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                JsonNode.class
        );

        JsonNode responseBody = response.getBody();


        Map<String, String> suggestions = new HashMap<>();

        if (responseBody != null && responseBody.has("documents")) {
            JsonNode documents = responseBody.get("documents");
            for (JsonNode document : documents) {
                suggestions.put(document.get("place_name").asText(), document.get("address_name").asText());
            }
        }

        return suggestions;
    }
}
