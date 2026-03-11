package com.umkolka.cgiresto.repository;

import com.umkolka.cgiresto.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Koodijupid võetud ja modifitseeritud järgmistelt:
// https://spring.io/guides/gs/accessing-data-rest
@RepositoryRestResource(path = "zones", collectionResourceRel = "zones")
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
