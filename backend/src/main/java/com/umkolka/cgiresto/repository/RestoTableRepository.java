package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.RestoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
public interface RestoTableRepository extends JpaRepository<RestoTable, Long> {

    @Query("SELECT t FROM RestoTable t WHERE NOT EXISTS (" +
            "SELECT 1 FROM Reservation r WHERE r.restoTable = t " +
            "AND r.startTime < :end AND r.endTime > :start)")
    List<RestoTable> findAvailableTables(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
