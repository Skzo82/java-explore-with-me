package ru.practicum.main.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/* # Запрос на массовое обновление статусов заявок на участие */
@Getter
@Setter
public class UpdateEventRequestStatusRequest {

    /* # Идентификаторы заявок, которые нужно обновить */
    @NotNull
    private List<Long> requestIds;

    /* # Новый статус: "CONFIRMED" или "REJECTED" */
    @NotNull
    private String status;
}