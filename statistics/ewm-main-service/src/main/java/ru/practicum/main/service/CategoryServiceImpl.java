package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;
import ru.practicum.main.dto.category.UpdateCategoryRequest;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.List;

/* # Сервис для работы с категориями */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;
    private final EventRepository eventRepo;

    /* # Создание категории */
    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto request) {
        // # проверка на уникальность имени
        if (repo.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Category name already exists: " + request.getName());
        }
        Category saved = repo.save(CategoryMapper.fromNew(request));
        return CategoryMapper.toDto(saved);
    }

    /* # Обновление категории */
    @Override
    @Transactional
    public CategoryDto update(long id, UpdateCategoryRequest request) {
        Category cat = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));

        // # если имя меняется — проверяем на дубли
        if (!cat.getName().equalsIgnoreCase(request.getName())
                && repo.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Category name already exists: " + request.getName());
        }

        cat.setName(request.getName());
        Category updated = repo.save(cat);
        return CategoryMapper.toDto(updated);
    }

    /* # Удаление категории */
    @Override
    @Transactional
    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Category not found: " + id);
        }
        // # перед удалением проверяем, что нет событий с этой категорией
        if (eventRepo.existsByCategory_Id(id)) {
            throw new DataIntegrityViolationException("Cannot delete category with linked events");
        }
        repo.deleteById(id);
    }

    /* # Публичный список категорий с пагинацией (отсортирован по id) */
    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return repo.findAllByOrderByIdAsc(pageable)
                .map(CategoryMapper::toDto)
                .getContent();
    }

    /* # Публичное получение категории по id */
    @Override
    public CategoryDto getById(long id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return CategoryMapper.toDto(c);
    }
}