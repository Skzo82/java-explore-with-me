package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.User;

import java.util.Optional;

/* # Репозиторий пользователей */
public interface UserRepository extends JpaRepository<User, Long> {

    /* # Проверка уникальности email */
    Optional<User> findByEmailIgnoreCase(String email);

    /* # Проверка существования по email */
    boolean existsByEmailIgnoreCase(String email);
}