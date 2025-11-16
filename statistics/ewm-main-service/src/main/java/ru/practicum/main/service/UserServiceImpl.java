package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.user.NewUserRequest;
import ru.practicum.main.dto.user.UserDto;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.UserMapper;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.UserRepository;

import java.util.List;

/* # Реализация сервиса пользователей */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(NewUserRequest request) {
        /* # Проверка уникальности email -> 409 Conflict */
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new ConflictException("Email already exists: " + request.getEmail());
        }
        User saved = userRepository.save(UserMapper.fromNew(request));
        return UserMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findAll(List<Long> ids, Pageable pageable) {
        if (ids != null && !ids.isEmpty()) {
            // # Если ids заданы — возвращаем только этих пользователей, пагинацию игнорируем
            return userRepository.findAllByIdIn(ids)
                    .stream()
                    .map(UserMapper::toDto)
                    .toList();
        }
        // # Без ids — обычная пагинация
        return userRepository.findAll(pageable)
                .map(UserMapper::toDto)
                .toList();
    }
}