package com.example.demo.security;

import com.example.demo.service.MarketService;
import com.example.demo.service.UserService;
import com.example.demo.util.TokenUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WebSecurityConfigTest.TestMocks.class)
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicMarketGetEndpointAccessible() throws Exception {
        mockMvc.perform(get("/market"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterIsPublic() throws Exception {
        mockMvc.perform(post("/users/register"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginIsPublic() throws Exception {
        mockMvc.perform(post("/users/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testSecuredEndpointBlockedWithoutToken() throws Exception {
        mockMvc.perform(post("/market/private"))
                .andExpect(status().isForbidden());
    }

    @TestConfiguration
    static class TestMocks {

        @Bean
        public TokenAuthenticationFilter tokenAuthenticationFilter() {
            return Mockito.mock(TokenAuthenticationFilter.class);
        }

        @Bean
        public MarketService marketService() {
            return Mockito.mock(MarketService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public TokenUtils tokenUtils() {
            return Mockito.mock(TokenUtils.class);
        }
    }
}
