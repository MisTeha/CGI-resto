package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-rest ja
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
@RepositoryRestResource(path = "reservations", collectionResourceRel = "reservations")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.restoTable.id = :tableId " +
            "AND CAST(r.startTime AS date) = :day " +
            "ORDER BY r.startTime")
    List<Reservation> findReservationsByTableAndDay(@Param("tableId") Long tableId, @Param("day") LocalDate day);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.restoTable.id = :tableId AND r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsOverlappingReservation(
            @Param("tableId") Long tableId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
