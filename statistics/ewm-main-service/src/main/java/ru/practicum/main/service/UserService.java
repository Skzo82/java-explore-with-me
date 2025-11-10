package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.user.*;

import java.util.List;

/* # Сервисный слой для пользователей */
public interface UserService {
    UserDto create(NewUserRequest request);

    void delete(Long userId);

    List<UserDto> findAll(Pageable pageable);
}