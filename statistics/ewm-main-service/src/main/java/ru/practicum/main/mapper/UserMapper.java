package ru.practicum.main.mapper;

import ru.practicum.main.dto.user.*;
import ru.practicum.main.model.User;

/* # Ручной mapper без сторонних библиотек */
public final class UserMapper {
    private UserMapper() {
    }

    public static UserDto toDto(User u) {
        return UserDto.builder().id(u.getId()).email(u.getEmail()).name(u.getName()).build();
    }

    public static User fromNew(NewUserRequest r) {
        return User.builder().email(r.getEmail()).name(r.getName()).build();
    }
}