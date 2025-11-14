package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.category.CategoryDto;
import ru.practicum.main.dto.category.NewCategoryDto;
import ru.practicum.main.dto.category.UpdateCategoryRequest;
import ru.practicum.main.service.CategoryService;

import java.net.URI;
import java.util.List;

/* # Админ-эндпоинты категорий */
@RestController
@Validated
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categories;

    public AdminCategoryController(CategoryService categories) {
        this.categories = categories;
    }

    /* # Создание категории -> 201 Created */
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto request) {
        CategoryDto created = categories.create(request);
        return ResponseEntity
                .created(URI.create("/admin/categories/" + created.getId()))
                .body(created);
    }

    /* # Частичное обновление категории -> 200 OK */
    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable @Positive long catId,
                              @RequestBody @Valid UpdateCategoryRequest request) {
        return categories.update(catId, request);
    }

    /* # Удаление категории -> 204 No Content */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable @Positive long catId) {
        categories.delete(catId);
        return ResponseEntity.noContent().build();
    }

    /* # Список категорий -> 200 OK (from/size -> Pageable), size по умолчанию = 10 */
    @GetMapping
    public List<CategoryDto> list(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        int page = from / size; // # offset -> page
        Pageable pageable = PageRequest.of(page, size);
        return categories.findAll(pageable);
    }
}