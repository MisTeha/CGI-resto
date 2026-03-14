package com.umkolka.cgiresto.controller;

import com.umkolka.cgiresto.service.BookingService;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//https://www.baeldung.com/spring-validate-requestparam-pathvariable
//ja https://www.baeldung.com/java-validation
@RestController
@Validated
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BookingService.BookingView> create(
            @RequestParam @NotNull Long tableId,
            @RequestParam @NotNull @Future @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @NotNull @Min(30) Integer durationMinutes,
            @RequestParam @NotNull @Min(1) Integer numberOfPeople,
            @RequestParam @NotBlank String customerName,
            @RequestParam @NotBlank String customerPhone) {
        BookingService.BookingView created = service.createBooking(
                tableId,
                startTime,
                durationMinutes,
                numberOfPeople,
                customerName,
                customerPhone);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/by-table-and-day")
    public ResponseEntity<List<BookingService.BookingView>> getByTableAndDay(
            @RequestParam Long tableId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        return ResponseEntity.ok(service.getBookingsByTableAndDay(tableId, day));
    }
}
