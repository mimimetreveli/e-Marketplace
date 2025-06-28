package com.example.demo.controller;

import Dto.MarketDto;
import com.example.demo.model.Market;
import com.example.demo.service.MarketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MarketControllerTest.TestConfig.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarketService marketService;

    @Autowired
    private ObjectMapper objectMapper;

    private Market market;

    @BeforeEach
    void setUp() {
        market = new Market(1L, "Item", 12.5, "Description", LocalDateTime.now(), "photo.jpg");
    }

    @Test
    void testCreateMarket() throws Exception {
        MarketDto dto = new MarketDto("Item", 12.5, "Description", "photo.jpg");
        when(marketService.save(any())).thenReturn(market);

        mockMvc.perform(post("/market")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item"));

        verify(marketService).save(any());
    }

    @Test
    void testGetPage() throws Exception {
        when(marketService.getPaginated(0)).thenReturn(new PageImpl<>(List.of(market)));

        mockMvc.perform(get("/market?page=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Item"));

        verify(marketService).getPaginated(0);
    }

    @Test
    void testGetOne() throws Exception {
        when(marketService.getById(1L)).thenReturn(market);

        mockMvc.perform(get("/market/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item"));

        verify(marketService).getById(1L);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MarketService marketService() {
            return Mockito.mock(MarketService.class);
        }
    }
}
