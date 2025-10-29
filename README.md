# ExploreWithMe — Этап 1: Сервис статистики

Этот репозиторий содержит реализацию **сервиса статистики** и **HTTP-клиента**, согласно спецификации:
- `ewm-stats-service-spec.json`
- Порт сервиса: **9090**

---

## Структура проекта

```
statistics/
│
├── ewm-stats-dto/        # Общие DTO (EndpointHitDto, ViewStatsDto)
├── ewm-stats-server/     # Spring Boot сервер (web + jpa + flyway + actuator)
├── ewm-stats-client/     # HTTP-клиент для основного сервиса (RestTemplate)
├── ewm-stats-tests/      # Тесты Postman (коллекция + окружение)
│
├── ewm-main-service/     # Заготовка (пока только actuator)
└── docker-compose.yml
```

---

## Требования

- Java 17
- Maven 3.9+
- Docker и Docker Compose
- PostgreSQL (через Docker)

---

## Сборка

```bash
# Сборка без тестов
mvn -q -DskipTests clean package
```

---

## Запуск через Docker

```bash
# Запускаем только базу данных и сервис статистики
docker compose up -d stats-db stats-server

# Проверяем состояние приложения
curl.exe http://localhost:9090/actuator/health
# Ожидаемый ответ: {"status":"UP"}
```

---

## Основные эндпоинты

### **POST** `/hit`
Сохраняет информацию о запросе пользователя.

Пример запроса:
```json
{
  "app": "ewm-main-service",
  "uri": "/events/1",
  "ip": "127.0.0.1",
  "timestamp": "2025-10-27 12:00:00"
}
```

Ответ:
```
201 Created
```

---

### **GET** `/stats`
Возвращает агрегированную статистику.

Параметры запроса:
```
start=2025-10-27 00:00:00
end=2025-10-27 23:59:59
uris=/events/1&uris=/events/2 (необязательно)
unique=true|false (по умолчанию false)
```

Пример:
```bash
curl "http://localhost:9090/stats?start=2025-10-27%2000:00:00&end=2025-10-27%2023:59:59&unique=true"
```

Пример ответа:
```json
[
  { "app": "ewm-main-service", "uri": "/events/1", "hits": 3 }
]
```

---

## Локальная разработка (без Docker)

В файле `statistics/ewm-stats-server/src/main/resources/application.yml` укажите:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/ewm_stats
    username: ewm
    password: ewm

server:
  port: 9090
```

Запуск из IntelliJ IDEA или командой:
```bash
mvn spring-boot:run -pl statistics/ewm-stats-server
```

---

## Тестирование сервиса статистики

Для проверки сервиса статистики добавлены Postman-тесты.  
Они проверяют корректную работу всех эндпоинтов `/actuator/health`, `/hit`, `/stats`.

---

### ⚙Подготовка

1. Убедитесь, что контейнеры запущены:
   ```bash
   docker compose up -d
   ```
2. Проверьте доступность сервиса:
   ```bash
   curl.exe http://localhost:9090/actuator/health
   # Ожидаемый ответ: {"status":"UP"}
   ```

---

### Тестирование в Postman

1. Импортируйте следующие файлы:
    - `statistics/ewm-stats-tests/ExploreWithMe - Stats Service.postman_collection.json`
    - `statistics/ewm-stats-tests/ExploreWithMe - local.postman_environment.json`
2. Выберите окружение **ExploreWithMe - local**
3. Откройте **Collection Runner**
4. Выберите коллекцию **ExploreWithMe — Stats Service**
5. Нажмите **Run ExploreWithMe — Stats Service**

Все тесты должны пройти успешно (код 200/201).

---

### Запуск тестов через CLI (Newman)

```bash
# Устанавливаем Newman, если не установлен
npm install -g newman

# Запускаем тесты
newman run "statistics/ewm-stats-tests/ExploreWithMe - Stats Service.postman_collection.json" ^
  -e "statistics/ewm-stats-tests/ExploreWithMe - local.postman_environment.json" ^
  --reporters cli
```

---

### Ожидаемый результат

```
GET /actuator/health         → 200 OK
POST /hit                    → 201 Created
GET /stats (все)             → 200 OK
GET /stats (фильтр + уник.)  → 200 OK
```

---

## Примечания

- **Actuator** доступен на `/actuator/health` и `/actuator/info`
- **Flyway** автоматически создаёт таблицу `hits`
- **Результаты** сортируются по количеству обращений (`hits`) по убыванию
- **Совместим** с основным модулем `ewm-main-service` через `StatsClient`
- **Postman тесты** и окружение находятся в `statistics/ewm-stats-tests`

---
