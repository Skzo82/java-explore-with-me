package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.main.model.Event;

/* # Репозиторий событий: поддержка Specification для динамических фильтров */
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    /* # Список событий автора с пагинацией */
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);
}