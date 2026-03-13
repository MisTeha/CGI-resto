package com.umkolka.cgiresto.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umkolka.cgiresto.service.TableService;

//https://www.baeldung.com/spring-validate-requestparam-pathvariable
// ilma valideerimiseta, hiljem seda ka
@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService service;

    public TableController(TableService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTables() {
        return ResponseEntity.ok(service.getTables());
    }
}
