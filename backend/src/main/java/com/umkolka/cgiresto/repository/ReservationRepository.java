package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.Reservation;
import com.umkolka.cgiresto.entity.RestoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-rest ja
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
@RepositoryRestResource(path = "reservations", collectionResourceRel = "reservations")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.restoTable = :table " +
            "AND CAST(r.startTime AS date) = :day " +
            "ORDER BY r.startTime")
    List<Reservation> findReservationsByTableAndDay(@Param("table") RestoTable table, @Param("day") LocalDate day);
}
