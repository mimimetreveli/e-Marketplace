package com.example.demo;

import Dto.MarketDto;
import com.example.demo.model.Market;
import com.example.demo.repository.MarketRepository;
import com.example.demo.service.MarketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTests {

    @Mock
    private MarketRepository repository;

    @InjectMocks
    private MarketService service;

    @Test
    void testSave() {
        MarketDto dto = new MarketDto();
        dto.setName("Test");
        dto.setPrice(10.0);
        dto.setDescription("Test Desc");
        dto.setPhotoUrl("img.jpg");

        Market market = new Market(1L, "Test", 10.0, "Test Desc", LocalDateTime.now(), "img.jpg");
        when(repository.save(any())).thenReturn(market);

        Market result = service.save(dto);

        assertEquals("Test", result.getName());
        assertEquals(10.0, result.getPrice());
        assertNotNull(result.getSubmissionTime());
        verify(repository).save(any());
    }

    @Test
    void testGetPaginated() {
        Page<Market> page = new PageImpl<>(List.of(new Market()));
        when(repository.findAllByOrderBySubmissionTimeDesc(any())).thenReturn(page);

        Page<Market> result = service.getPaginated(0);
        assertEquals(1, result.getContent().size());
        verify(repository).findAllByOrderBySubmissionTimeDesc(any());
    }

    @Test
    void testGetById_Success() {
        Market market = new Market();
        market.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(market));

        Market result = service.getById(1L);
        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void testGetById_Throws() {
        when(repository.findById(2L)).thenThrow(new RuntimeException("Not found"));
        assertThrows(RuntimeException.class, () -> service.getById(2L));
        verify(repository).findById(2L);
    }
}
