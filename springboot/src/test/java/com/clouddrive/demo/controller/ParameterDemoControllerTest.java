package com.clouddrive.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParameterDemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queryShouldParseRequiredOptionalAndDefaultQueryParameters() throws Exception {
        mockMvc.perform(get("/api/demo/query")
                        .param("name", "小明")
                        .param("keyword", "照片"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"status\":0,\"result\":true,\"msg\":\"\","
                        + "\"data\":{\"name\":\"小明\",\"page\":1,\"keyword\":\"照片\"}}", true));
    }

    @Test
    void fileDetailShouldConvertPathVariableToLong() throws Exception {
        mockMvc.perform(get("/api/demo/files/123"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":0,\"result\":true,\"msg\":\"\","
                        + "\"data\":{\"fileId\":123}}", true));
    }

    @Test
    void createFolderShouldConvertJsonBodyToRequestDto() throws Exception {
        mockMvc.perform(post("/api/demo/folders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"学习资料\",\"parentId\":0}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":0,\"result\":true,\"msg\":\"\","
                        + "\"data\":{\"name\":\"学习资料\",\"parentId\":0}}", true));
    }

    @Test
    void headerShouldUseProvidedHeaderOrDefaultValue() throws Exception {
        mockMvc.perform(get("/api/demo/header").header("X-Client-Name", "web-client"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":0,\"result\":true,\"msg\":\"\","
                        + "\"data\":{\"clientName\":\"web-client\"}}", true));

        mockMvc.perform(get("/api/demo/header"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":0,\"result\":true,\"msg\":\"\","
                        + "\"data\":{\"clientName\":\"anonymous\"}}", true));
    }
}
