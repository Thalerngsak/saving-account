package com.example.saving.account.controller;

import com.example.saving.account.controller.RegistrationController;
import com.example.saving.account.dto.RegistrationRequest;
import com.example.saving.account.model.User;
import com.example.saving.account.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@Import(RegistrationControllerTest.TestSecurityConfig.class)
class RegistrationControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Primary
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.ignoringRequestMatchers("/api/register/**"));
            return http.build();
        }
    }


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void registerUser() throws Exception {
        String json = "{\"email\":\"test@example.com\",\"password\":\"pass\",\"citizenId\":\"12345\",\"thaiName\":\"Thai\",\"englishName\":\"English\",\"pin\":\"123456\"}";

        User user = new User();
        user.setEmail("test@example.com");
        when(userService.register(any(RegistrationRequest.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}