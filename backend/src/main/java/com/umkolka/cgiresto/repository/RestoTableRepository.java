package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.RestoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-jpa ja
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
@Repository
public interface RestoTableRepository extends JpaRepository<RestoTable, Long> {

    @Query("SELECT t FROM RestoTable t WHERE NOT EXISTS (" +
            "SELECT 1 FROM Reservation r WHERE r.restoTable = t " +
            "AND r.start < :end AND r.end > :start)")
    List<RestoTable> findAvailableTables(LocalDateTime start, LocalDateTime end);
}
