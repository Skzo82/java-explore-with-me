package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @Test
    void postHit_returns201() throws Exception {
        // успешная запись хита
        var dto = new EndpointHitDto(null, "app", "/events/1", "127.0.0.1", "2025-10-27 12:00:00");
        mvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getStats_invalidRange_returns400() throws Exception {
        // end < start -> ожидаем 400
        mvc.perform(get("/stats")
                        .param("start", "2025-10-27 12:00:00")
                        .param("end", "2025-10-27 11:00:00"))
                .andExpect(status().isBadRequest());
    }
}