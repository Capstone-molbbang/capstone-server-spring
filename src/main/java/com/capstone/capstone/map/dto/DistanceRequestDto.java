package com.capstone.capstone.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceRequestDto {
    private List<Double> distanceList = new ArrayList<>();

    public void addDistance(Double distance){
        distanceList.add(distance);
    }
}
