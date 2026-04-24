package com.example.lab2.web;

import com.example.lab2.model.Apartment;
import com.example.lab2.service.ApartmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет форми додавання / редагування квартири.
 */

@WebServlet("/owner/apartment-form")
public class OwnerApartmentFormServlet extends HttpServlet {
    // Якщо id переданий - значить відкриваємо форму редагування.
    // Якщо ні - відкриваємо порожню форму для створення.
    private final ApartmentService apartmentService = ApartmentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        // За замовчуванням створюємо порожній об'єкт.
        Apartment apartment = new Apartment();
        if (idParam != null && !idParam.isBlank()) {
            apartment = apartmentService.findById(Long.parseLong(idParam));

            // Якщо id передали, але квартиру не знайдено — повертаємося до списку.
            if (apartment == null) {
                resp.sendRedirect(req.getContextPath() + "/owner/apartments");
                return;
            }
        }

        // Передаємо квартиру у JSP, щоб заповнити форму.
        req.setAttribute("apartment", apartment);
        req.getRequestDispatcher("/WEB-INF/views/owner/form.jsp").forward(req, resp);
    }
}
