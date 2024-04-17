package com.capstone.capstone.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceDto {

    private List<Double> distanceList;


    public void addDistance(Double distance){
        this.distanceList.add(distance);
    }
}
