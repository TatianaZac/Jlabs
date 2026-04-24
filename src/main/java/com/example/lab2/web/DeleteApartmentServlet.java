package com.example.lab2.web;

import com.example.lab2.service.ApartmentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет видалення квартири.
 */

@WebServlet("/owner/delete-apartment")
public class DeleteApartmentServlet extends HttpServlet {
    private final ApartmentService apartmentService = ApartmentService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Отримуємо id квартири, яку треба видалити.
        String idParam = req.getParameter("id");

        // Якщо id переданий — видаляємо квартиру.
        if (idParam != null && !idParam.isBlank()) {
            apartmentService.delete(Long.parseLong(idParam));
        }

        // Після видалення повертаємося до списку квартир.
        resp.sendRedirect(req.getContextPath() + "/owner/apartments");
    }
}
