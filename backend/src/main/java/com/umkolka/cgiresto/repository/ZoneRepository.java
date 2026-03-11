package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-jpa
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
