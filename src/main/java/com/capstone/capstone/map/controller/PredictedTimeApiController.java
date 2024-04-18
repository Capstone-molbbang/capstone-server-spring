package com.capstone.capstone.map.controller;

import com.capstone.capstone.map.dto.PredictedTimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PredictedTimeApiController {

    @PostMapping("/api/predictedTime")
    public ResponseEntity<PredictedTimeDto> getPredictedTime(@RequestBody int predictedTime){
        PredictedTimeDto predictedTimeDto = new PredictedTimeDto(predictedTime);
        log.info("time : " + predictedTimeDto.getPredictedTime());
        return ResponseEntity.ok().body(predictedTimeDto);
    }
}
