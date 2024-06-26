package com.capstone.capstone.map.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DistanceDto {

    private List<Integer> distanceList = new ArrayList<>();

    public void addDistance(Integer distance){
        distanceList.add(distance);
    }
}
