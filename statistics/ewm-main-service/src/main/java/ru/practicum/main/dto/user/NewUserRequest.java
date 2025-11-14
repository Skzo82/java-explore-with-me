package ru.practicum.main.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/* # DTO создания пользователя */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {

    /* # Имя пользователя: обязательно и не пустое (2..250) */
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    /* # Email: обязателен, валидный, длина 6..254 */
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;
}