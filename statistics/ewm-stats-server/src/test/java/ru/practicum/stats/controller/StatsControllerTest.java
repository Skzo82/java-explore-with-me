package ru.practicum.stats.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.handler.ApiExceptionHandler;
import ru.practicum.stats.service.StatsService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatsController.class)
@Import(ApiExceptionHandler.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatsService service;

    @Test
    @DisplayName("POST /hit -> 201 Created")
    void postHit_returns201() throws Exception {
        Mockito.doNothing().when(service).save(any(EndpointHitDto.class));

        String body = """
                {
                  "app": "ewm-main-service",
                  "uri": "/events/1",
                  "ip": "127.0.0.1",
                  "timestamp": "2025-10-27 12:00:00"
                }
                """;

        mvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /stats -> 200 OK и JSON-массив")
    void getStats_returns200AndArray() throws Exception {
        Mockito.when(service.stats(any(), any(), any(), anyBoolean()))
                .thenReturn(List.of(new ViewStatsDto("ewm-main-service", "/events/1", 3L)));

        mvc.perform(get("/stats")
                        .param("start", "2025-10-27 00:00:00")
                        .param("end", "2025-10-27 23:59:59")
                        .param("unique", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].app").value("ewm-main-service"))
                .andExpect(jsonPath("$[0].uri").value("/events/1"))
                .andExpect(jsonPath("$[0].hits").value(3));
    }

    @Test
    @DisplayName("GET /stats с неправильным диапазоном -> 400 Bad Request")
    void getStats_invalidRange_returns400() throws Exception {
        Mockito.when(service.stats(any(), any(), any(), anyBoolean()))
                .thenThrow(new IllegalArgumentException("start must be before end"));

        mvc.perform(get("/stats")
                        .param("start", "2025-10-27 12:00:00")
                        .param("end", "2025-10-27 11:00:00"))
                .andExpect(status().isBadRequest());
    }
}