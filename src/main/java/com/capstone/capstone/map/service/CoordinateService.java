package com.capstone.capstone.map.service;

import com.capstone.capstone.map.dto.KakaoSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CoordinateService {

    // 출발지와 도착지의 좌표
    private Map<String, String> getCoordsFromSearchDto(KakaoSearchDto searchDto) {
        Map<String, String> coords = new HashMap<>();
        if (searchDto != null && !searchDto.getDocuments().isEmpty()) {
            KakaoSearchDto.Document document = searchDto.getDocuments().get(0);
            coords.put("x", document.getX());
            coords.put("y", document.getY());
            log.info("coors" + coords);
        }
        return coords;
    }

    // 출발지와 도착지의 좌표
    public Map<String, Object> getCoordsAndDistanceFromSearchDto(KakaoSearchDto startSearchDto, KakaoSearchDto destinationSearchDto) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> startCoords = getCoordsFromSearchDto(startSearchDto);
        Map<String, String> destinationCoords = getCoordsFromSearchDto(destinationSearchDto);

        double startLat = Double.parseDouble(startCoords.get("x"));
        double startLon = Double.parseDouble(startCoords.get("y"));
        double destLat = Double.parseDouble(destinationCoords.get("x"));
        double destLon = Double.parseDouble(destinationCoords.get("y"));


        result.put("startCoords", startCoords);
        result.put("destinationCoords", destinationCoords);

        log.info("result " + result);
        return result;
    }
}
