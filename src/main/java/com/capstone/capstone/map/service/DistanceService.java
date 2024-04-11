package com.capstone.capstone.map.service;

import com.capstone.capstone.map.dto.KakaoSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DistanceService {

    // 출발지와 도착지의 좌표를 검색하여 가공된 결과를 반환하는 메서드
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

    // 두 지점 사이의 거리를 계산하는 메서드
    private double calculateDistance(double startLat, double startLon, double destLat, double destLon) {
        final int R = 6371; // 지구 반지름 (단위: km)
        double latDistance = Math.toRadians(destLat - startLat);
        double lonDistance = Math.toRadians(destLon - startLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(destLat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (단위: km)
    }

    // 출발지와 도착지의 좌표 및 거리를 검색하여 가공된 결과를 반환하는 메서드
    public Map<String, Object> getCoordsAndDistanceFromSearchDto(KakaoSearchDto startSearchDto, KakaoSearchDto destinationSearchDto) {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> startCoords = getCoordsFromSearchDto(startSearchDto);
        Map<String, String> destinationCoords = getCoordsFromSearchDto(destinationSearchDto);

        double startLat = Double.parseDouble(startCoords.get("x"));
        double startLon = Double.parseDouble(startCoords.get("y"));
        double destLat = Double.parseDouble(destinationCoords.get("x"));
        double destLon = Double.parseDouble(destinationCoords.get("y"));

        // 두 지점 사이의 거리 계산
        double distance = calculateDistance(startLat, startLon, destLat, destLon);

        result.put("startCoords", startCoords);
        result.put("destinationCoords", destinationCoords);
        result.put("distance", distance); // 거리 추가

        log.info("result " + result);
        return result;
    }
}
