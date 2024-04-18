package com.capstone.capstone.map.service;

import com.capstone.capstone.map.dto.DistanceDto;
import com.capstone.capstone.map.dto.DistanceRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DistanceCalculatorService {
    // 두 점 간의 거리를 계산하는 메서드
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        // WTM 좌표계에서의 거리 계산
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

//     WTM 좌표 리스트를 받아서 각 점 간 거리를 계산하여 반환하는 메서드
    public DistanceRequestDto calculateDistances(List<List<Double>> wtmCoords) {
        List<Double> distanceList = new ArrayList<>();

        // WTM 좌표 리스트를 순회하면서 각 점 간의 거리를 계산하여 distanceList에 추가
        for (int i = 0; i < wtmCoords.size() - 1; i++) {
            List<Double> coord1 = wtmCoords.get(i);
            List<Double> coord2 = wtmCoords.get(i + 1);

            double x1 = coord1.get(0);
            double y1 = coord1.get(1);
            double x2 = coord2.get(0);
            double y2 = coord2.get(1);

            // 두 점 간의 거리 계산
            double distance = calculateDistance(x1, y1, x2, y2);
            distanceList.add(distance/1000);
        }

        return new DistanceRequestDto(distanceList);
    }
}
