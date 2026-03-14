package com.umkolka.cgiresto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.umkolka.cgiresto.entity.RestoTable;
import com.umkolka.cgiresto.repository.RestoTableRepository;

@Service
public class TableService {

    public record TableView(
            Long tableId,
            String tableNumber,
            Long zoneId,
            String zoneName,
            Integer nSeats,
            Boolean nextToWindow,
            Integer privacyScore,
            Integer gridX,
            Integer gridY) {
    }

    private final RestoTableRepository repository;

    public TableService(RestoTableRepository repository) {
        this.repository = repository;
    }

    public List<TableView> getTables() {
        return repository.findAll().stream()
                .map(this::toTableView)
                .toList();

    }

    private TableView toTableView(RestoTable table) {
        return new TableView(
                table.getId(),
                table.getTableNumber(),
                table.getZone().getId(),
                table.getZone().getName(),
                table.getNSeats(),
                table.getIsNextToWindow(),
                table.getPrivacyScore(),
                table.getGridX(),
                table.getGridY());
    }
}
