package com.capstone.capstone.fastapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteRequest {

    private LocalDateTime data;
    private String start;
}
