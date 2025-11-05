package ru.practicum.stats.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StatsFlowTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void postThenQueryStats_returnsHits() throws Exception {
        for (int i = 0; i < 3; i++) {
            var dto = new EndpointHitDto(null, "ewm-main-service", "/events/42",
                    "127.0.0." + i, "2025-10-27 12:0" + i + ":00");
            mvc.perform(post("/hit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(om.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
        }

        var res = mvc.perform(get("/stats")
                        .param("start", "2025-10-27 00:00:00")
                        .param("end", "2025-10-27 23:59:59")
                        .param("unique", "true"))
                .andExpect(status().isOk())
                .andReturn();

        var body = res.getResponse().getContentAsString();
        List<ViewStatsDto> stats = om.readValue(body, new TypeReference<>(){});

        assertThat(stats).anySatisfy(v -> assertThat(v.uri()).isEqualTo("/events/42"));
    }
}