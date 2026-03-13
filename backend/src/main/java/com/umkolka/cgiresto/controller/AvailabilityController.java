package com.umkolka.cgiresto.controller;

import com.umkolka.cgiresto.service.AvailabilityService;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

//https://www.baeldung.com/spring-validate-requestparam-pathvariable
//ja https://www.baeldung.com/java-validation 
@RestController
@Validated
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<AvailabilityService.RecommendationBuckets> recommend(
            @RequestParam @NotNull @Future @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NotNull @Min(30) Integer durationMinutes,
            @RequestParam @NotNull @Min(1) Integer partySize,
            @RequestParam @NotNull Long zoneId,
            @RequestParam(required = false, defaultValue = "false") Boolean windowPreferred,
            @RequestParam(required = false, defaultValue = "false") Boolean privacyPreferred) {
        return ResponseEntity.ok(service.recommend(
                startTime,
                durationMinutes,
                partySize,
                zoneId,
                windowPreferred,
                privacyPreferred));
    }
}
