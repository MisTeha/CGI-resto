package com.umkolka.cgiresto.service;

import com.umkolka.cgiresto.entity.Reservation;
import com.umkolka.cgiresto.entity.RestoTable;
import com.umkolka.cgiresto.repository.ReservationRepository;
import com.umkolka.cgiresto.repository.RestoTableRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    public record BookingView(
            Long id,
            Long tableId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer numberOfPeople,
            String customerName,
            String customerPhone) {
    }

    private final ReservationRepository reservationRepository;
    private final RestoTableRepository tableRepository;

    public BookingService(ReservationRepository reservationRepository, RestoTableRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    public BookingView createBooking(
            Long tableId,
            LocalDateTime startTime,
            Integer durationMinutes,
            Integer numberOfPeople,
            String customerName,
            String customerPhone) {
        RestoTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lauda ID-ga " + tableId + " ei leitud"));

        if (numberOfPeople > table.getNSeats()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Laual on " + table.getNSeats() + " istekohta, kuid inimesi on " + numberOfPeople);
        }

        LocalDateTime endTime = startTime.plusMinutes(durationMinutes);
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Broneeringu lõpp peab olema peale algust");
        }

        boolean hasConflict = reservationRepository.existsOverlappingReservation(tableId, startTime, endTime);
        if (hasConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Laud ID-ga " + tableId + " pole sel ajal saadaval");
        }

        Reservation created = reservationRepository.save(new Reservation(
                startTime,
                endTime,
                table,
                numberOfPeople,
                customerName,
                customerPhone));

        return toBookingView(created);
    }

    public List<BookingView> getBookingsByTableAndDay(Long tableId, LocalDate day) {
        if (!tableRepository.existsById(tableId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lauda ID-ga " + tableId + " ei leitud");
        }

        return reservationRepository.findReservationsByTableAndDay(tableId, day).stream()
                .map(this::toBookingView)
                .toList();
    }

    private BookingView toBookingView(Reservation reservation) {
        return new BookingView(
                reservation.getId(),
                reservation.getTable().getId(),
                reservation.getStart(),
                reservation.getEnd(),
                reservation.getNumberOfPeople(),
                reservation.getCustomerName(),
                reservation.getCustomerPhone());
    }
}
