package com.example.demo.controller;

import com.example.demo.model.Market;
import com.example.demo.service.MarketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketControllerTests {

    @Mock
    private MarketService service;

    @InjectMocks
    private MarketController controller;

    @Test
    void testGetPage() {
        Page<Market> page = new PageImpl<>(List.of(new Market()));
        when(service.getPaginated(0)).thenReturn(page);

        ResponseEntity<Page<Market>> response = controller.getPage(0);
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void testGetOne() {
        Market market = new Market();
        market.setId(1L);
        when(service.getById(1L)).thenReturn(market);

        ResponseEntity<Market> response = controller.getOne(1L);
        assertEquals(1L, response.getBody().getId());
    }
}