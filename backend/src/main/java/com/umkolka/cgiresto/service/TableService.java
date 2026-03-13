package com.umkolka.cgiresto.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.umkolka.cgiresto.entity.RestoTable;
import com.umkolka.cgiresto.repository.RestoTableRepository;

@Service
public class TableService {

    private final RestoTableRepository repository;

    public TableService(RestoTableRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getTables() {
        return repository.findAll().stream()
                .map(this::toTableMap)
                .toList();

    }

    private Map<String, Object> toTableMap(RestoTable table) {
        return Map.of(
                "tableId", table.getId(),
                "tableNumber", table.getTableNumber(),
                "zoneId", table.getZone().getId(),
                "zoneName", table.getZone().getName(),
                "nSeats", table.getNSeats(),
                "nextToWindow", table.getIsNextToWindow(),
                "privacyScore", table.getPrivacyScore());
    }
}
