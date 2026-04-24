package com.example.lab2.web;

import com.example.lab2.service.ApartmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет кабінету хазяїна.
 * Показує список усіх квартир.
 */

@WebServlet("/owner/apartments")
public class OwnerApartmentsServlet extends HttpServlet {
    private final ApartmentService apartmentService = ApartmentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Кладемо список квартир у request, щоб JSP могла його вивести.
        req.setAttribute("apartments", apartmentService.findAll());
        req.getRequestDispatcher("/WEB-INF/views/owner/list.jsp").forward(req, resp);
    }
}
