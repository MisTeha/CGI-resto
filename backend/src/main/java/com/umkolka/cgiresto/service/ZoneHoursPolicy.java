package com.umkolka.cgiresto.service;

import com.umkolka.cgiresto.entity.Zone;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ZoneHoursPolicy {

    public void validateBookingWindow(Zone zone, LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime openingTime = zone.getOpeningTime();
        LocalTime closingTime = zone.getClosingTime();

        if (openingTime == null || closingTime == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tsooni \"" + zone.getName() + "\" lahtiolekuajad puuduvad");
        }

        if (!openingTime.isBefore(closingTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tsooni \"" + zone.getName() + "\" lahtiolekuajad on vigased");
        }

        if (!startTime.toLocalDate().equals(endTime.toLocalDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Broneering peab algama ja lõppema samal kuupäeval");
        }

        LocalTime requestedStart = startTime.toLocalTime();
        LocalTime requestedEnd = endTime.toLocalTime();

        if (requestedStart.isBefore(openingTime) || requestedEnd.isAfter(closingTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Broneering peab jääma tsooni \"" + zone.getName() + "\" lahtiolekuaega "
                            + openingTime + "-" + closingTime);
        }
    }
}