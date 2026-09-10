package com.clouddrive.quark.controller;

import com.clouddrive.quark.client.QuarkApiClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuarkProxyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuarkApiClient quarkApiClient;

    @Test
    void fileListShouldForwardCookieAndReturnUpstreamJson() throws Exception {
        String upstreamJson = "{\"status\":0,\"data\":{\"list\":[]}}";
        when(quarkApiClient.get(
                eq("https://drive-pc.quark.cn/1/clouddrive"),
                eq("file/sort"),
                anyMap(),
                eq("test-cookie")))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(upstreamJson));

        mockMvc.perform(get("/api/quark/files")
                        .header("X-Quark-Cookie", "test-cookie"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(upstreamJson, true));

        verify(quarkApiClient).get(
                eq("https://drive-pc.quark.cn/1/clouddrive"),
                eq("file/sort"),
                anyMap(),
                eq("test-cookie"));
    }

    @Test
    void createFolderShouldConvertRequestBodyAndForwardItToClient() throws Exception {
        when(quarkApiClient.post(
                eq("https://drive-pc.quark.cn/1/clouddrive"),
                eq("file"),
                eq(null),
                any(Map.class),
                eq("test-cookie")))
                .thenReturn(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"status\":0}"));

        mockMvc.perform(post("/api/quark/files/folders")
                        .header("X-Quark-Cookie", "test-cookie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pdirFid\":\"0\",\"fileName\":\"照片\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":0}", true));

        verify(quarkApiClient).post(
                eq("https://drive-pc.quark.cn/1/clouddrive"),
                eq("file"),
                eq(null),
                any(Map.class),
                eq("test-cookie"));
    }
}
