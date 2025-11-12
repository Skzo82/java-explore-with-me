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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto request) {
        // # проверка на уникальность имени
        if (repo.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Category name already exists: " + request.getName());
        }
        // NB: usa il metodo del mapper che hai realmente (fromNew o toNewEntity)
        Category saved = repo.save(CategoryMapper.fromNew(request));
        return CategoryMapper.toDto(saved);
    }

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

    @Override
    @Transactional
    public void delete(long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Category not found: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        // # корректно получаем контент страницы и маппим в DTO
        return repo.findAll(pageable)
                .map(CategoryMapper::toDto)
                .getContent();
    }

    @Override
    public CategoryDto getById(long id) {
        // # единичное чтение по id
        Category c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return CategoryMapper.toDto(c);
    }
}