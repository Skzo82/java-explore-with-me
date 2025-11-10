package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;

import java.util.List;

/* # Контракт сервиса категорий */
public interface CategoryService {
    CategoryDto create(NewCategoryDto request);

    CategoryDto update(Long catId, NewCategoryDto request);

    void delete(Long catId);

    List<CategoryDto> findAll(Pageable pageable);
}