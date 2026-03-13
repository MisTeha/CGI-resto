package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.RestoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

public interface RestoTableRepository extends JpaRepository<RestoTable, Long> {
    /*
     * Tegin selle JPA klassi nagu rakendusele, mis oleks vöga suurte andmemahtudega
     * (ehk palju funktsioone on andmebaasi tasemel).
     * Seda kõige enam sellepärast, et ma olen lähedane andmebaasidega ning leian,
     * et kood on puhtam,
     * kui Java steramide või muuga filtreerimine (kuigi mõistan, et service tasemel
     * on nendega hiljem kergem muudatusi teha).
     */

    @Query("SELECT t FROM RestoTable t WHERE t.zone.id = :zoneId AND t.nSeats >= :partySize AND NOT EXISTS (" +
            "SELECT 1 FROM Reservation r WHERE r.restoTable = t " +
            "AND r.startTime < :end AND r.endTime > :start)")
    List<RestoTable> findAvailableTablesByZoneAndMinSeats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("zoneId") Long zoneId,
            @Param("partySize") Integer partySize);

    @Query("SELECT t FROM RestoTable t WHERE t.zone.id = :zoneId AND t.nSeats >= :partySize AND EXISTS (" +
            "SELECT 1 FROM Reservation r WHERE r.restoTable = t " +
            "AND r.startTime < :end AND r.endTime > :start)")
    List<RestoTable> findUnavailableTablesByZoneAndMinSeats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("zoneId") Long zoneId,
            @Param("partySize") Integer partySize);

    @Query("SELECT t FROM RestoTable t WHERE t.zone.id = :zoneId AND t.nSeats < :partySize")
    List<RestoTable> findTablesByZoneAndMaxSeatsExclusive(
            @Param("zoneId") Long zoneId,
            @Param("partySize") Integer partySize);

    @Query("SELECT t FROM RestoTable t WHERE t.zone.id = :zoneId")
    List<RestoTable> findByZoneId(@Param("zoneId") Long zoneId);
}
