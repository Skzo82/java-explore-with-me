package ru.practicum.main.dto.user;

import lombok.*;

/* # DTO ответа по пользователю */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String name;
}