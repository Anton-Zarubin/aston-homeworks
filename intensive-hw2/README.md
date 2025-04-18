# News Service
REST сервис для работы с новостями.

##  Технологии
- Maven 3
- Lombok
- Mapstruct
- JDBC
- PostgreSQL
- Servlet API
- JUnit
- Testcontainers

## Endpoints
Приложение доступно по адресу http://localhost:8080/news-service

### Категории
- GET /categories - Получить все категории.

- GET /categories/{id} - Получить категорию по ID.

- POST /categories - Создать новую категорию. В теле запроса нужно передать title, указав название категории.

- PUT /categories/{id} - Обновить категорию по ID. Тело запроса идентично телу запроса на создание.

- DELETE /categories/{id} - Удалить категорию по ID.


### Теги
- GET /tags - Получить все теги.

- GET /tags/{id} - Получить тег по ID.

- POST /tags - Создать новый тег. В теле запроса нужно передать name, указав имя тега.

- PUT /tags/{id} - Обновить тег по ID. Тело запроса идентично телу запроса на создание.

- DELETE /tags/{id} - Удалить тег по ID.


### Новости
- GET /news - Получить все новости.

- GET /news/{id} - Получить новость по ID.

- POST /news - Создать новую новость. В теле запроса нужно передать title, text, categoryTitle, указав заголовок новости, 
текст новости и название категории соответственно.

- PUT /news/{id} - Обновить новость по ID. Тело запроса идентично телу запроса на создание.

- POST /news/tags - Добавить тег к новости. В параметрах запроса нужно указать newsid и tagid.

- DELETE /news/tags - Убрать тег из новости. Параметры запроса совпадают с параметрами при добавлении тега.

- DELETE /news/{id} - Удалить новость по ID.

## Мини-тесты CRUD

```bash
# создать категорию
curl -X POST -H "Content-Type: application/json" -d '{"title":"Some category"}' \
     http://localhost:8080/news-service/categories

# создать тег
curl -X POST -H "Content-Type: application/json" -d '{"name":"Some tag"}' \
     http://localhost:8080/news-service/tags

# создать новость
curl -X POST -H "Content-Type: application/json" \
     -d '{"title":"Test news","text":"Any text","categoryTitle":"Some category"}' \
     http://localhost:8080/news-service/news
```

Для тестирования в Postman можно использовать коллекцию postman.json