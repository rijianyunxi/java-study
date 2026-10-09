package com.study.user.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 使用 H2 MySQL 模式走完整链路，不连接或修改本机 study 数据库。 */
@SpringBootTest(properties = {
        "spring.profiles.active=user-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:userdemo;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=classpath:user-demo-schema.sql",
        "spring.sql.init.data-locations=classpath:user-demo-data.sql"
})
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private JdbcTemplate jdbc;

    private static final String PROFILE = "{\"name\":\"Alice Updated\",\"age\":23,\"email\":\"alice@example.com\"}";

    @Test
    void listAndDetailReadExistingUsers() throws Exception {
        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(5))
                .andExpect(jsonPath("$.data[0].name").value("Jone"))
                .andExpect(jsonPath("$.data[0].status").value(1));
        mvc.perform(get("/api/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Jack"));
        mvc.perform(get("/api/users/search").param("name", "Tom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(3));
    }

    @Test
    void createPersistsUserWithEnabledStatus() throws Exception {
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":6,\"name\":\"Alice\",\"age\":22,\"email\":\"alice@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.id").value(6))
                .andExpect(jsonPath("$.data.status").value(1));
        mvc.perform(get("/api/users/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Alice"));
    }

    @Test
    void duplicateIdDoesNotOverwriteExistingUser() throws Exception {
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Alice\",\"age\":22,\"email\":\"alice@example.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.status").value(409));
        assertEquals("Jone", jdbc.queryForObject("SELECT name FROM `user` WHERE id=1", String.class));
    }

    @Test
    void updateProfileDoesNotChangeStatusOrId() throws Exception {
        jdbc.update("UPDATE `user` SET status=0 WHERE id=1");
        // 即使 JSON 额外带 status/id，也不能通过基本资料 DTO 更改这两项。
        String body = PROFILE.substring(0, PROFILE.length()-1) + ",\"status\":1,\"id\":100}";
        mvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Alice Updated"))
                .andExpect(jsonPath("$.data.age").value(23))
                .andExpect(jsonPath("$.data.status").value(0));
        assertEquals(0, jdbc.queryForObject("SELECT status FROM `user` WHERE id=1", Integer.class));
    }

    @Test
    void statusUpdateDoesNotChangeProfile() throws Exception {
        mvc.perform(patch("/api/users/1/status").contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":0,\"name\":\"Should Not Change\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value(0))
                .andExpect(jsonPath("$.data.name").value("Jone"))
                .andExpect(jsonPath("$.data.age").value(18))
                .andExpect(jsonPath("$.data.email").value("test1@baomidou.com"));
        assertEquals("Jone", jdbc.queryForObject("SELECT name FROM `user` WHERE id=1", String.class));
        assertEquals(0, jdbc.queryForObject("SELECT status FROM `user` WHERE id=1", Integer.class));
        mvc.perform(patch("/api/users/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":1}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.status").value(1));
    }

    @Test
    void noOpUpdatesStillSucceed() throws Exception {
        mvc.perform(patch("/api/users/1/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":1}"))
                .andExpect(status().isOk());
        String original = "{\"name\":\"Jone\",\"age\":18,\"email\":\"test1@baomidou.com\"}";
        mvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(original))
                .andExpect(status().isOk());
    }

    @Test
    void missingUserReturns404ForReadsAndWrites() throws Exception {
        mvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.result").value(false));
        mvc.perform(put("/api/users/999").contentType(MediaType.APPLICATION_JSON).content(PROFILE))
                .andExpect(status().isNotFound());
        mvc.perform(patch("/api/users/999/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void invalidCreateAndProfileAreRejected() throws Exception {
        String[] invalid = {
                "{\"name\":\"Alice\",\"age\":22,\"email\":\"alice@example.com\"}",
                "{\"id\":-1,\"name\":\"Alice\",\"age\":22,\"email\":\"alice@example.com\"}",
                "{\"id\":6,\"name\":\" \",\"age\":22,\"email\":\"alice@example.com\"}",
                "{\"id\":6,\"name\":\"Alice\",\"age\":-1,\"email\":\"alice@example.com\"}",
                "{\"id\":6,\"name\":\"Alice\",\"age\":151,\"email\":\"alice@example.com\"}",
                "{\"id\":6,\"name\":\"Alice\",\"age\":22,\"email\":\"bad-email\"}"
        };
        for (String body : invalid) {
            mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.result").value(false));
        }
        mvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        assertEquals(5, jdbc.queryForObject("SELECT COUNT(*) FROM `user`", Integer.class));
    }

    @Test
    void invalidStatusesAreRejectedWithoutChangingData() throws Exception {
        for (String body : new String[]{"{\"status\":2}", "{\"status\":-1}", "{\"status\":null}", "{}"}) {
            mvc.perform(patch("/api/users/1/status").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.result").value(false));
        }
        assertEquals(1, jdbc.queryForObject("SELECT status FROM `user` WHERE id=1", Integer.class));
    }

    @Test
    void malformedJsonAndPathIdsAreRejected() throws Exception {
        mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("not-json"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/users/not-a-number")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/users/0")).andExpect(status().isBadRequest());
    }
}
