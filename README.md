# пояснення до файлів проєкту 

1. **Веб-інтерфейс** (JSP + Servlet):
   - головна сторінка;
   - пошук квартир для клієнта;
   - кабінет хазяїна, де можна додавати, редагувати і видаляти квартири.

2. **REST API** (JAX-RS / Jersey):
   - API для роботи з квартирами через JSON;
   - CRUD-операції;
   - фільтрація, пагінація;
   - валідація даних.

---

## Структура проєкту

```text
pom.xml
src/main/java/com/example/lab2/
 ├─ api/                -> REST API
 ├─ model/              -> моделі даних
 ├─ service/            -> логіка роботи з квартирами
 └─ web/                -> сервлети для JSP-сторінок
src/main/webapp/
 ├─ WEB-INF/views/      -> JSP-сторінки
 ├─ WEB-INF/web.xml     -> налаштування REST servlet
 ├─ index.jsp           -> стартовий JSP
 └─ resources/css/      -> стилі
```
---

# Пояснення кожного файла

## 1. Корінь проєкту

### `pom.xml`

Основні залежності :
- `jakarta.servlet-api` — для сервлетів;
- `jakarta.servlet.jsp-api` — для JSP;
- `jstl` — для тегів JSTL;
- `jakarta.ws.rs-api` — для REST;
- `jersey-*` — реалізація REST API на Tomcat;
- `hibernate-validator` — для Bean Validation.
---

## 2. Моделі даних (`model`)

### `src/main/java/com/example/lab2/model/Apartment.java`
Головна модель проєкту
Цей клас описує одну квартиру

Які дані тут є:
- `id` — унікальний номер;
- `title` — назва оголошення;
- `city`, `district` — місто і район;
- `rooms`, `area`, `floor` — параметри квартири;
- `pricePerMonth` — ціна;
- `furnished`, `petsAllowed` — чи є меблі і чи можна з тваринами;
- `description` — опис;
- `ownerName`, `phone` — контакти хазяїна.

Це контейнер з даними про квартиру.

### `src/main/java/com/example/lab2/model/ApartmentSearchCriteria.java`
Модель не квартири, а **пошукових фільтрів**.
Вона зберігає те, що вводить користувач у формі пошуку:
- місто;
- мінімальну кількість кімнат;
- максимальну ціну;
- чи потрібні меблі;
- чи дозволені тварини.

---

## 3. Сервісний шар (`service`)

### `src/main/java/com/example/lab2/service/ApartmentService.java`

Тут:
- зберігаються тестові квартири;
- виконується пошук;
- виконується додавання квартири;
- виконується редагування;
- виконується видалення;
- виконується пошук по `id`.

Що важливо:
- база даних не використовується;
- дані лежать просто у списку `apartments` у пам'яті;

---

## 4. REST API (`api`)

### `src/main/java/com/example/lab2/api/RestApplication.java`

Тут задається:
- базовий шлях `/api`;
- реєстрація REST-ресурсу `ApartmentResource`.

Тобто все API починається з: **/api/...**


### `src/main/java/com/example/lab2/api/resource/ApartmentResource.java`
**Головний REST-клас**.

Тут описані HTTP-методи для квартир.

Що тут:
- `GET /api/apartments` — отримати список квартир;
- `GET /api/apartments/{id}` — отримати квартиру по id;
- `POST /api/apartments` — створити квартиру;
- `PUT /api/apartments/{id}` — оновити квартиру;
- `DELETE /api/apartments/{id}` — видалити квартиру.

Також тут реалізовано:
- фільтрацію (`city`, `minRooms`, `maxPrice`, `furnished`, `petsAllowed`);
- пагінацію (`page`, `size`);
- перевірку помилок;
- правильні HTTP-статуси;
- валідацію DTO через `Validator`.
---

## 5. DTO для REST API (`api/dto`)

### `src/main/java/com/example/lab2/api/dto/ApartmentRequest.java`
DTO для створення або редагування квартири через API.

Це спеціальний клас, у який приходять JSON-дані від клієнта.

Тут є:
- стандартні анотації валідації (`@NotBlank`, `@Min`, `@Max`, `@DecimalMin`, `@NotNull`, `@Size`);
- кастомна перевірка телефону `@UkrainianPhone`;
- кастомна перевірка логіки квартири `@ApartmentLayout`.

### `src/main/java/com/example/lab2/api/dto/ApartmentPageResponse.java`
DTO для відповіді зі списком квартир по сторінках.

Тут є:
- `items` — список квартир на поточній сторінці;
- `page` — номер сторінки;
- `size` — скільки елементів на сторінці;
- `totalItems` — загальна кількість знайдених квартир;
- `totalPages` — загальна кількість сторінок.

### `src/main/java/com/example/lab2/api/dto/ApiError.java`
DTO для помилок у JSON.

Використовується, коли API хоче повернути щось типу:
- загальне повідомлення;
- список помилок валідації.

Наприклад: якщо клієнт передав неправильний телефон або порожню назву.

---

## 6. Кастомна валідація (`api/validation`)

### `src/main/java/com/example/lab2/api/validation/UkrainianPhone.java`
Кастомна анотація для перевірки телефону.
Вона означає: поле має бути у форматі українського номера.

### `src/main/java/com/example/lab2/api/validation/UkrainianPhoneValidator.java`
Реальна логіка перевірки для `@UkrainianPhone`.


### `src/main/java/com/example/lab2/api/validation/ApartmentLayout.java`
Кастомна анотація рівня всього об'єкта.

Потрібна для перевірки не одного поля, а логіки між полями.

### `src/main/java/com/example/lab2/api/validation/ApartmentLayoutValidator.java`
Логіка для `@ApartmentLayout`.

Цей валідатор перевіряє, чи адекватно співвідносяться:
- кількість кімнат;
- площа квартири.

---

## 7. Звичайний вебінтерфейс (`web`)

### `src/main/java/com/example/lab2/web/HomeServlet.java`
Сервлет головної сторінки.

Коли користувач заходить на `/`, цей сервлет:
- не генерує HTML сам;
- просто передає керування на `WEB-INF/views/index.jsp` через `forward`.

### `src/main/java/com/example/lab2/web/SearchServlet.java`
Сервлет сторінки пошуку для клієнта.

Що він робить:
1. читає параметри з форми;
2. створює `ApartmentSearchCriteria`;
3. викликає `ApartmentService.search(...)`;
4. розбиває результати на сторінки;
5. передає результати у `search.jsp`.

Це один з ключових файлів для звичайної web-частини.

### `src/main/java/com/example/lab2/web/OwnerApartmentsServlet.java`
Сервлет кабінету хазяїна.

Його задача :
- взяти всі квартири із сервісу;
- передати їх у JSP-сторінку списку.

### `src/main/java/com/example/lab2/web/OwnerApartmentFormServlet.java`
Сервлет форми додавання / редагування.

Як працює:
- якщо `id` не передали — відкривається порожня форма для нової квартири;
- якщо `id` передали — знаходиться існуюча квартира і форма заповнюється її даними.

### `src/main/java/com/example/lab2/web/SaveApartmentServlet.java`
Сервлет збереження квартири.

Він працює через `POST` і:
- читає поля форми;
- створює або оновлює `Apartment`;
- передає квартиру в `ApartmentService.save(...)`;
- робить `redirect` назад до списку.


### `src/main/java/com/example/lab2/web/DeleteApartmentServlet.java`
Сервлет видалення квартири.

Він:
- читає `id` з запиту;
- викликає видалення у сервісі;
- повертає користувача до списку квартир.

---

## 8. JSP-сторінки (`WEB-INF/views`)

> Усі головні JSP лежать у `WEB-INF`, щоб користувач не відкривав їх напряму без сервлета.

### `src/main/webapp/WEB-INF/views/common/header.jspf`
Спільний верх сторінок.

Що тут є:
- `<html>`, `<head>`;
- підключення `common.css`;
- підключення додаткового CSS для конкретної сторінки;
- навігація між сторінками.

Це спільний шаблон, щоб не дублювати однаковий код у кожній JSP.

### `src/main/webapp/WEB-INF/views/common/footer.jspf`
Спільний низ сторінок.

### `src/main/webapp/WEB-INF/views/index.jsp`
Головна сторінка застосунку.

Показує дві ролі:
- клієнт;
- хазяїн квартири.

І дає посилання:
- на пошук квартир;
- на кабінет хазяїна.

### `src/main/webapp/WEB-INF/views/client/search.jsp`
JSP для пошуку квартир.

Що тут є:
- форма фільтрів;
- список результатів;
- умовний вивід, якщо нічого не знайдено;
- JSTL `forEach`, `if`, `choose/when/otherwise`;
- пагінація;
- безпечне виведення через `c:out` і `fn:escapeXml`.

Це одна з головних сторінок у проєкті.

### `src/main/webapp/WEB-INF/views/owner/list.jsp`
Сторінка зі списком квартир хазяїна.

Тут у таблиці показуються:
- усі квартири;
- статус меблів;
- чи можна з тваринами;
- кнопки редагування і видалення.

### `src/main/webapp/WEB-INF/views/owner/form.jsp`
Форма для додавання або редагування квартири.

Що тут є:
- поля вводу;
- чекбокси;
- textarea для опису;
- одна форма і для create, і для update;
- захист від XSS у значеннях через `fn:escapeXml` і `c:out`.

---

## 9. `webapp` файли поза `views`

### `src/main/webapp/WEB-INF/web.xml`
Конфігурація вебзастосунку.

Тут налаштовано Jersey servlet для REST API:
- який клас запускати (`RestApplication`);
- що REST API обробляється по шляху `/api/*`.

---

## 10. CSS-файли (`resources/css`)

### `src/main/webapp/resources/css/common.css`
Головні спільні стилі:
- контейнер;
- кнопки;
- таблиці;
- поля форми;
- загальні відступи;
- класи `success`, `warning`.


### `src/main/webapp/resources/css/home.css`
Стилі тільки для головної сторінки. Робить картки головної сторінки акуратнішими.

### `src/main/webapp/resources/css/client-search.css`
Стилі сторінки пошуку.

Тут оформлені:
- форма пошуку;
- картки квартир;
- пагінація;
- стрілки переходу між сторінками.

### `src/main/webapp/resources/css/owner-form.css`
Стилі форми хазяїна.

Тут оформлено:
- форму;
- чекбокси;
- блок із кнопками.

### `src/main/webapp/resources/css/owner-list.css`
Стилі списку квартир хазяїна.

Тут оформлено:
- панель дій;
- статуси;
- колонку з кнопками.


# Як усе пов'язано між собою

## Веб-частина

### Головна сторінка
`/`  
→ `HomeServlet`  
→ `WEB-INF/views/index.jsp`

### Пошук квартир
`/client/search`  
→ `SearchServlet`  
→ `ApartmentSearchCriteria`  
→ `ApartmentService.search(...)`  
→ `WEB-INF/views/client/search.jsp`

### Кабінет хазяїна
`/owner/apartments`  
→ `OwnerApartmentsServlet`  
→ `ApartmentService.findAll()`  
→ `WEB-INF/views/owner/list.jsp`

### Додавання / редагування
`/owner/apartment-form`  
→ `OwnerApartmentFormServlet`  
→ `WEB-INF/views/owner/form.jsp`

### Збереження
`POST /owner/save-apartment`  
→ `SaveApartmentServlet`  
→ `ApartmentService.save(...)`

### Видалення
`POST /owner/delete-apartment`  
→ `DeleteApartmentServlet`  
→ `ApartmentService.delete(...)`

---