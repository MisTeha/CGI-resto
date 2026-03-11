package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.Reservation;
import com.umkolka.cgiresto.entity.RestoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-jpa ja
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.restoTable = :table " +
            "AND CAST(r.start AS date) = :day " +
            "ORDER BY r.start")
    List<Reservation> findReservationsByTableAndDay(RestoTable table, LocalDate day);
}
