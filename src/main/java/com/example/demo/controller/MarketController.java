package com.example.demo.controller;

import Dto.MarketDto;
import com.example.demo.model.Market;
import com.example.demo.service.MarketService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/market")
@AllArgsConstructor
public class MarketController {

    private final MarketService service;

    public MarketController(MarketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Market> create(@RequestBody MarketDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Market>> getPage(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.getPaginated(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Market> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
