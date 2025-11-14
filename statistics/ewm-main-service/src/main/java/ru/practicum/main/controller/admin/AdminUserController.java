package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

    /* # Создание пользователя → 201 */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody NewUserRequest body) {
        UserDto created = users.create(body);
        return ResponseEntity.created(URI.create("/admin/users/" + created.getId())).body(created);
    }

    /* # Получение списка пользователей (пагинация) → 200 */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@RequestParam(defaultValue = "0") @Min(0) int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        Pageable page = PageRequest.of(from / size, size);
        return ResponseEntity.ok(users.findAll(page));
    }

    /* # Удаление пользователя → 204 */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable long userId) {
        users.delete(userId);
        return ResponseEntity.noContent().build();
    }
}