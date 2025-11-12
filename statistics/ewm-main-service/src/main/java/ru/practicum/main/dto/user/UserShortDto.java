package ru.practicum.main.dto.user;

import lombok.*;

/* # Сокращённое представление пользователя (id + name) */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortDto {
    private Long id;
    private String name;
}