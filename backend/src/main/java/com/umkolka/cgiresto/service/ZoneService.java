package com.umkolka.cgiresto.service;

import com.umkolka.cgiresto.entity.Zone;
import com.umkolka.cgiresto.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ZoneService {

    public record ZoneView(Long id, String name, String description) {
    }

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<ZoneView> getZones() {
        return zoneRepository.findAll().stream()
                .sorted(Comparator.comparing(Zone::getId))
                .map(zone -> new ZoneView(zone.getId(), zone.getName(), zone.getDescription()))
                .toList();
    }
}