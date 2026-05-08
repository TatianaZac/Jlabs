package com.example.lab2.web;

import com.example.lab2.model.Apartment;
import com.example.lab2.service.ApartmentServiceLocal;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Сервлет збереження квартири.
 * Працює по POST, тому що змінює дані.
 */

@WebServlet("/owner/save-apartment")
public class SaveApartmentServlet extends HttpServlet {
    @EJB
    private ApartmentServiceLocal apartmentService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Щоб українські символи з форми читались коректно.
        req.setCharacterEncoding("UTF-8");

        Apartment apartment = new Apartment();

        // Якщо id є - це редагування існуючої квартири.
        // Якщо id немає - це створення нової.
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            apartment.setId(Long.parseLong(idParam));
        }

        // Зчитуємо всі поля форми і записуємо їх у модель.
        apartment.setTitle(req.getParameter("title"));
        apartment.setCity(req.getParameter("city"));
        apartment.setDistrict(req.getParameter("district"));
        apartment.setRooms(parseInt(req.getParameter("rooms")));
        apartment.setArea(parseDouble(req.getParameter("area")));
        apartment.setFloor(parseInt(req.getParameter("floor")));
        apartment.setPricePerMonth(parseBigDecimal(req.getParameter("pricePerMonth")));

        // якщо checkbox відмічений - параметр існує,
        // якщо ні - параметр не прийде взагалі.
        apartment.setFurnished(req.getParameter("furnished") != null);
        apartment.setPetsAllowed(req.getParameter("petsAllowed") != null);

        apartment.setDescription(req.getParameter("description"));
        apartment.setOwnerName(req.getParameter("ownerName"));
        apartment.setPhone(req.getParameter("phone"));

        // Зберігаємо квартиру у сервісі.
        apartmentService.save(apartment);

        // redirect = новий HTTP-запит від браузера на сторінку списку.
        resp.sendRedirect(req.getContextPath() + "/owner/apartments");
    }


    /**
     * Якщо поле порожнє - повертаємо 0.
     */
    private int parseInt(String value) {
        return value == null || value.isBlank() ? 0 : Integer.parseInt(value);
    }


    /**
     * Якщо поле порожнє - повертаємо 0.
     */
    private double parseDouble(String value) {
        return value == null || value.isBlank() ? 0 : Double.parseDouble(value);
    }


    /**
     * Якщо поле порожнє - повертаємо BigDecimal.ZERO.
     */
    private BigDecimal parseBigDecimal(String value) {
        return value == null || value.isBlank() ? BigDecimal.ZERO : new BigDecimal(value);
    }
}
