package com.capstone.capstone.map.controller;

import com.capstone.capstone.map.dto.KakaoSearchDto;
import com.capstone.capstone.map.service.CoordinateService;
import com.capstone.capstone.map.service.KakaoMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MapApiController {

    private final KakaoMapService kakaoMapService;
    private final CoordinateService distanceService;

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestBody Map<String, String> addresses) {

        String startAddress = addresses.get("startAddress");
        String destinationAddress = addresses.get("destinationAddress");

        try {
            KakaoSearchDto startSearchDto = kakaoMapService.getKakaoSearch(startAddress);
            KakaoSearchDto destinationSearchDto = kakaoMapService.getKakaoSearch(destinationAddress);

            Map<String, Object> searchResults = distanceService.getCoordsFromSearchDto(startSearchDto, destinationSearchDto);

            return ResponseEntity.status(HttpStatus.OK).body(searchResults);
        } catch (Exception e) {
            log.error("Error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, String>> getSuggestions(@RequestParam("query") String query) {
        Map<String, String> placeSuggestions = kakaoMapService.getPlaceSuggestions(query);
        return ResponseEntity.ok().body(placeSuggestions);
    }

}
