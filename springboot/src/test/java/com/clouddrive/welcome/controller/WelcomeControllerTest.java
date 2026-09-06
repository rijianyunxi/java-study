package com.clouddrive.welcome.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WelcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void welcomeShouldReturnExpectedJson() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                // 严格比较字段和值（不依赖 JSON 对象的字段排列顺序）。
                .andExpect(content().json(
                        "{\"status\":0,\"result\":true,\"msg\":\"\","
                                + "\"data\":{\"text\":\"hello world\"}}", true));
    }
}
