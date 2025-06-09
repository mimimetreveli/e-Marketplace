package com.example.demo.service;

import Dto.MarketDto;
import com.example.demo.model.Market;
import com.example.demo.repository.MarketRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MarketService {

    private final MarketRepository repository;

    public MarketService(MarketRepository repository) {
        this.repository = repository;
    }

    public Market save(MarketDto dto) {
        Market market = new Market();
        market.setName(dto.getName());
        market.setPrice(dto.getPrice());
        market.setDescription(dto.getDescription());
        market.setSubmissionTime(LocalDateTime.now());
        market.setPhotoUrl(dto.getPhotoUrl());
        return repository.save(market);
    }

    public Page<Market> getPaginated(int page) {
        return repository.findAllByOrderBySubmissionTimeDesc(PageRequest.of(page, 6));
    }

    public Market getById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
