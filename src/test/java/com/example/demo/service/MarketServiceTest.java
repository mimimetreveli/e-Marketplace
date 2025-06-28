package com.example.demo.service;

import Dto.MarketDto;
import com.example.demo.model.Market;
import com.example.demo.repository.MarketRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MarketServiceTest {

    @Mock
    private MarketRepository repository;

    @InjectMocks
    private MarketService service;

    public MarketServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        MarketDto dto = new MarketDto("Item", 10.0, "description", "photo.png");
        Market mockMarket = new Market(1L, "Item", 10.0, "description", LocalDateTime.now(), "photo.png");
        when(repository.save(any(Market.class))).thenReturn(mockMarket);

        Market saved = service.save(dto);

        assertEquals("Item", saved.getName());
        verify(repository).save(any());
    }

    @Test
    void testGetPaginated() {
        Page<Market> page = new PageImpl<>(List.of());
        when(repository.findAllByOrderBySubmissionTimeDesc(any(PageRequest.class))).thenReturn(page);

        Page<Market> result = service.getPaginated(0);
        assertNotNull(result);
        verify(repository).findAllByOrderBySubmissionTimeDesc(any());
    }

    @Test
    void testGetById() {
        Market market = new Market();
        when(repository.findById(1L)).thenReturn(Optional.of(market));

        assertEquals(market, service.getById(1L));
        verify(repository).findById(1L);
    }
}
