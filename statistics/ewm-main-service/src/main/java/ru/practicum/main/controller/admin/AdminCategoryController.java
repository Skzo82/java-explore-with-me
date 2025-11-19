package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

    /* # create -> 201 */
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto request) {
        CategoryDto created = categories.create(request);
        return ResponseEntity.created(URI.create("/admin/categories/" + created.getId())).body(created);
    }

    /* # patch -> 200 */
    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable @Positive long catId,
                              @RequestBody @Valid UpdateCategoryRequest request) {
        return categories.update(catId, request);
    }

    /* # delete -> 204 */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable @Positive long catId) {
        categories.delete(catId);
        return ResponseEntity.noContent().build();
    }

    /* # list -> 200 (from/size -> Pageable) */
    @GetMapping
    public List<CategoryDto> list(@RequestParam(defaultValue = "0") @Min(0) int from,
                                  @RequestParam(defaultValue = "10") @Min(1) int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return categories.findAll(pageable);
    }
}