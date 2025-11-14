package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.user.NewUserRequest;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.service.UserService;

import java.net.URI;
import java.util.List;

/* # Админ-эндпоинты пользователей */
@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final UserService users;

    public AdminUserController(UserService users) {
        this.users = users;
    }

    /* # Создание пользователя -> 201 Created */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody NewUserRequest body) {
        UserDto created = users.create(body);
        return ResponseEntity
                .created(URI.create("/admin/users/" + created.getId()))
                .body(created);
    }

    /* # Список пользователей (пагинация) -> 200 OK, size по умолчанию = 10 */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        Pageable page = PageRequest.of(from / size, size);
        return ResponseEntity.ok(users.findAll(page));
    }

    /* # Удаление пользователя -> 204 No Content */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable @Positive long userId) {
        users.delete(userId);
        return ResponseEntity.noContent().build();
    }
}