package ru.practicum.main.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;

/* # DTO для создания пользователя */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 128)
    private String name;
}