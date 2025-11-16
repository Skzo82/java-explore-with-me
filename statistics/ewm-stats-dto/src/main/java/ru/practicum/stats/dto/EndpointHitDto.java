package ru.practicum.stats.dto;

/* # DTO для сохранения хита в статистике
 * timestamp передаётся как строка, парсинг делаем на стороне сервиса
 */
public record EndpointHitDto(
        Long id,        // идентификатор записи
        String app,     // название сервиса
        String uri,     // запрошенный URI
        String ip,      // IP-адрес клиента
        String timestamp // время запроса в строковом формате
) {
}