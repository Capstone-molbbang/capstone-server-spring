package com.capstone.capstone.map.controller;

import com.capstone.capstone.map.dto.DistanceDto;
import com.capstone.capstone.map.dto.KakaoSearchDto;
import com.capstone.capstone.map.service.CoordinateService;
import com.capstone.capstone.map.service.KakaoMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MapApiController {

    private final KakaoMapService kakaoMapService;
    private final CoordinateService distanceService;
  //  private final DistanceCalculatorService distanceCalculatorService;
    // FastAPI 서버의 엔드포인트 URL
    private final RestTemplate restTemplate;


    //출발지, 도착지의 위도, 경도 및 거리 반환
    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody Map<String, String> addresses) {
        log.info("addresses" + addresses);

        String startAddress = addresses.get("startAddress");
        String destinationAddress = addresses.get("destinationAddress");

        log.info("startAddress" + startAddress);

        try {
            // 출발지와 도착지를 이용하여 Kakao 검색 API를 호출하여 검색 결과를 받아옴
            KakaoSearchDto startSearchDto = kakaoMapService.getKakaoSearch(startAddress);
            KakaoSearchDto destinationSearchDto = kakaoMapService.getKakaoSearch(destinationAddress);

            // 검색 결과를 필요한 형태로 가공하여 반환
            Map<String, Object> searchResults = distanceService.getCoordsFromSearchDto(startSearchDto, destinationSearchDto);

            log.info("search Result : " + searchResults);
            return ResponseEntity.status(HttpStatus.OK).body(searchResults);
        } catch (Exception e) {
            log.error("Error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, String>> getSuggestions(@RequestParam("query") String query) {
        // 검색어를 이용하여 Kakao 검색 API를 호출하여 관련 장소를 검색
        Map<String, String> placeSuggestions = kakaoMapService.getPlaceSuggestions(query);
        log.info("placeInfo : " + placeSuggestions);
        return ResponseEntity.ok().body(placeSuggestions);
    }

}
