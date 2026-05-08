package com.example.lab2.web;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.service.ApartmentServiceLocal;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Сервлет сторінки пошуку квартир для клієнта.
 * Цей клас відповідає за сторінку, на якій користувач може шукати квартири за параметрами.
 * Сервлет:
 * 1. Приймає параметри з форми пошуку.
 * 2. Формує об'єкт ApartmentSearchCriteria.
 * 3. Передає критерії у ApartmentService.
 * 4. Отримує список знайдених квартир.
 * 5. Ділить список на сторінки.
 * 6. Передає результат у search.jsp.
 */
@WebServlet("/client/search")
public class SearchServlet extends HttpServlet {

    // Сторінка за замовчуванням, якщо користувач не передав параметр page
    private static final int DEFAULT_PAGE = 1;

    // Кількість оголошень на одній сторінці за замовчуванням
    private static final int DEFAULT_PAGE_SIZE = 3;

    // Отримуємо спільний об'єкт сервісу, який зберігає та шукає квартири
    @EJB
    private ApartmentServiceLocal apartmentService;

    /**
     * Метод doGet виконується, коли користувач відкриває сторінку пошуку
     * або відправляє форму методом GET.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Створюємо об'єкт, у який будуть записані параметри пошуку
        ApartmentSearchCriteria criteria = new ApartmentSearchCriteria();

        // Зчитуємо місто з параметра city.
        // Якщо поле порожнє, буде записано null.
        criteria.setCity(trimToNull(req.getParameter("city")));

        // Зчитуємо мінімальну кількість кімнат
        criteria.setMinRooms(parseInteger(req.getParameter("minRooms")));

        // Зчитуємо максимальну ціну оренди
        criteria.setMaxPrice(parseBigDecimal(req.getParameter("maxPrice")));

        // Зчитуємо параметр "мебльована квартира"
        criteria.setFurnished(parseBooleanFlag(req.getParameter("furnished")));

        // Зчитуємо параметр "можна з тваринами"
        criteria.setPetsAllowed(parseBooleanFlag(req.getParameter("petsAllowed")));

        // Зчитуємо номер поточної сторінки.
        // Якщо параметра немає або він некоректний, буде використано DEFAULT_PAGE.
        int page = parsePositiveInteger(req.getParameter("page"), DEFAULT_PAGE);

        // Зчитуємо кількість квартир на сторінці.
        // Якщо параметра немає або він некоректний, буде використано DEFAULT_PAGE_SIZE.
        int pageSize = parsePositiveInteger(req.getParameter("size"), DEFAULT_PAGE_SIZE);

        // Отримуємо всі квартири, які відповідають критеріям пошуку
        List<Apartment> allResults = apartmentService.search(criteria);

        // Загальна кількість знайдених квартир
        int totalItems = allResults.size();

        // Рахуємо кількість сторінок.
        // Наприклад: якщо знайдено 12 квартир і pageSize = 3, буде 4 сторінки.
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        // Якщо нічого не знайдено, все одно залишаємо одну сторінку,
        // щоб у JSP не було проблем з відображенням.
        if (totalPages == 0) {
            totalPages = 1;
        }

        // Якщо користувач вручну ввів сторінку більшу, ніж існує,
        // переносимо його на останню доступну сторінку.
        if (page > totalPages) {
            page = totalPages;
        }

        // Визначаємо індекс першого елемента для поточної сторінки.
        // Наприклад, для page = 2 і pageSize = 3 починаємо з індекса 3.
        int fromIndex = Math.min((page - 1) * pageSize, totalItems);

        // Визначаємо індекс, на якому поточна сторінка закінчується.
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        // Беремо тільки ті квартири, які мають бути показані на поточній сторінці
        List<Apartment> pageResults = allResults.subList(fromIndex, toIndex);

        // Передаємо у JSP критерії пошуку, щоб форма не очищувалась після пошуку
        req.setAttribute("criteria", criteria);

        // Передаємо у JSP тільки квартири для поточної сторінки
        req.setAttribute("results", pageResults);

        // Передаємо номер поточної сторінки
        req.setAttribute("currentPage", page);

        // Передаємо кількість оголошень на сторінці
        req.setAttribute("pageSize", pageSize);

        // Передаємо загальну кількість знайдених оголошень
        req.setAttribute("totalItems", totalItems);

        // Передаємо загальну кількість сторінок
        req.setAttribute("totalPages", totalPages);

        // Показуємо JSP, чи є попередня сторінка
        req.setAttribute("hasPreviousPage", page > 1);

        // Показуємо JSP, чи є наступна сторінка
        req.setAttribute("hasNextPage", page < totalPages);

        // Передаємо керування JSP-сторінці, яка відобразить результат у браузері
        req.getRequestDispatcher("/WEB-INF/views/client/search.jsp").forward(req, resp);
    }

    /**
     * Метод прибирає зайві пробіли.
     * Якщо рядок порожній, повертає null.
     */
    private String trimToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    /**
     * Перетворює рядок у Integer.
     * Використовується для кількості кімнат.
     */
    private Integer parseInteger(String value) {
        return value == null || value.isBlank() ? null : Integer.valueOf(value);
    }

    /**
     * Перетворює рядок у BigDecimal.
     * Використовується для ціни, бо гроші краще зберігати не в double.
     */
    private BigDecimal parseBigDecimal(String value) {
        return value == null || value.isBlank() ? null : new BigDecimal(value);
    }

    /**
     * Перетворює параметр з форми у Boolean.
     * Наприклад: "true" стане true, "false" стане false.
     */
    private Boolean parseBooleanFlag(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Boolean.valueOf(value);
    }

    /**
     * Перетворює рядок у додатне число.
     * Якщо значення неправильне, повертається значення за замовчуванням.
     * Цей метод потрібен для page і size.
     */
    private int parsePositiveInteger(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            int number = Integer.parseInt(value);

            // Якщо число більше 0, використовуємо його.
            // Якщо 0 або від'ємне — беремо значення за замовчуванням.
            return number > 0 ? number : defaultValue;
        } catch (NumberFormatException e) {
            // Якщо користувач передав не число, наприклад page=abc,
            // програма не падає, а бере значення за замовчуванням.
            return defaultValue;
        }
    }
}