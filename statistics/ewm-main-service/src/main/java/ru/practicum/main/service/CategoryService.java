package ru.practicum.main.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;
import ru.practicum.main.dto.category.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto request);

    CategoryDto update(long id, UpdateCategoryRequest request);

    void delete(long id);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(long id);   // <- questo è il metodo che manca nella tua Impl
}