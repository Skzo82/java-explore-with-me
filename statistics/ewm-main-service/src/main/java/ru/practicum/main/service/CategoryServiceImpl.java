package ru.practicum.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;
import ru.practicum.main.mapper.CategoryMapper;
import ru.practicum.main.model.Category;
import ru.practicum.main.repository.CategoryRepository;
import ru.practicum.main.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repo;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto request) {
        if (repo.existsByNameIgnoreCase(request.getName())) {
            throw new DataIntegrityViolationException("Category name already exists: " + request.getName());
        }
        Category saved = repo.save(CategoryMapper.fromNew(request));
        return CategoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CategoryDto update(Long catId, NewCategoryDto request) {
        Category c = repo.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + catId));

        repo.findByNameIgnoreCase(request.getName()).ifPresent(ex -> {
            if (!ex.getId().equals(catId)) {
                throw new DataIntegrityViolationException("Category name already exists: " + request.getName());
            }
        });

        c.setName(request.getName());
        return CategoryMapper.toDto(repo.save(c));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        if (!repo.existsById(catId)) {
            throw new NotFoundException("Category not found: " + catId);
        }
        repo.deleteById(catId);
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return repo.findAll(pageable)
                .stream()
                .map(CategoryMapper::toDto)
                .toList();
    }

    public CategoryDto get(Long id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found: " + id));
        return CategoryMapper.toDto(c);
    }
}