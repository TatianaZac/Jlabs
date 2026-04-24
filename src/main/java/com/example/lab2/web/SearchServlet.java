package com.example.lab2.web;

import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.service.ApartmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Сервлет сторінки пошуку для клієнта.
 * Він читає параметри з форми, збирає об'єкт критеріїв,
 * викликає сервіс пошуку і передає результати у JSP.
 */

@WebServlet("/client/search")
public class SearchServlet extends HttpServlet {
    // Отримуємо сервіс, через який працюємо з квартирами.
    private final ApartmentService apartmentService = ApartmentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Створюємо об'єкт, у який складемо всі параметри пошуку.
        ApartmentSearchCriteria criteria = new ApartmentSearchCriteria();

        // Читаємо значення з query-параметрів URL / форми.
        criteria.setCity(trimToNull(req.getParameter("city")));
        criteria.setMinRooms(parseInteger(req.getParameter("minRooms")));
        criteria.setMaxPrice(parseBigDecimal(req.getParameter("maxPrice")));
        criteria.setFurnished(parseBooleanFlag(req.getParameter("furnished")));
        criteria.setPetsAllowed(parseBooleanFlag(req.getParameter("petsAllowed")));

        // Передаємо у JSP і самі критерії (щоб форма не очищалась), і результати пошуку.
        req.setAttribute("criteria", criteria);
        req.setAttribute("results", apartmentService.search(criteria));

        // Відкриваємо JSP-сторінку з результатами.
        req.getRequestDispatcher("/WEB-INF/views/client/search.jsp").forward(req, resp);
    }


    /**
     * Перетворює порожній рядок у null.
     * Це зручно, щоб далі простіше перевіряти "критерій заданий чи ні".
     */
    private String trimToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }


    /**
     * Безпечне перетворення рядка у Integer.
     * Якщо поле не заповнене - повертаємо null.
     */
    private Integer parseInteger(String value) {
        return value == null || value.isBlank() ? null : Integer.valueOf(value);
    }


    /**
     * Безпечне перетворення рядка у BigDecimal.
     * Використовується для ціни.
     */
    private BigDecimal parseBigDecimal(String value) {
        return value == null || value.isBlank() ? null : new BigDecimal(value);
    }


    /**
     * Для select / radio / query-параметрів true/false.
     * Якщо параметр не заданий - повертаємо null.
     */
    private Boolean parseBooleanFlag(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Boolean.valueOf(value);
    }
}
