package com.clouddrive.quark.controller;

import com.clouddrive.quark.service.QuarkAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuarkAuthController.class)
class QuarkAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuarkAuthService service;

    @Test
    void sessionShouldReturnNormalizedCookieHeader() throws Exception {
        ResponseEntity<String> upstream = ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.SET_COOKIE, "sid=abc; Path=/; HttpOnly")
                .header(HttpHeaders.SET_COOKIE, "uid=123; Expires=Wed, 21 Oct 2030 07:28:00 GMT; Path=/")
                .body("{\"status\":0}");
        when(service.exchangeTicket("ticket-1")).thenReturn(upstream);

        mockMvc.perform(get("/api/quark/auth/session").param("ticket", "ticket-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().string("X-Quark-Cookie", "sid=abc; uid=123"))
                .andExpect(content().json("{\"status\":0}"));
    }
}
