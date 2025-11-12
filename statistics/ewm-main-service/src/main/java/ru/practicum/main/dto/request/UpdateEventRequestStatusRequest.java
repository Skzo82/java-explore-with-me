package ru.practicum.main.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/* # Запрос на массовое обновление статусов заявок по событию */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventRequestStatusRequest {

    /* # Идентификаторы заявок, которые нужно обновить */
    @NotEmpty
    private List<Long> requestIds;

    /* # Новый статус: "CONFIRMED" или "REJECTED" */
    @NotNull
    private String status;
}