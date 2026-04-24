package com.example.lab2.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Сервлет головної сторінки.
 * Обробляє запит на корінь застосунку: /
 */

@WebServlet("")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // forward = передати керування JSP на сервері без зміни URL у браузері.
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
