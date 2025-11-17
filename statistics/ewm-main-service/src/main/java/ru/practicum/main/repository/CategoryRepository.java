package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Category;

import java.util.Optional;

/* # Репозиторий категорий */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /* # Поиск категории по имени без учёта регистра */
    Optional<Category> findByNameIgnoreCase(String name);

    /* # Проверка существования категории с таким именем (без учёта регистра) */
    boolean existsByNameIgnoreCase(String name);

    /* # Пагинированный список категорий, отсортированных по id по возрастанию */
    Page<Category> findAllByOrderByIdAsc(Pageable pageable);
}