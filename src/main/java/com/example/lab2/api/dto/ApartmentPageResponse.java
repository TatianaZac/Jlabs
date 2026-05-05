package com.example.lab2.api.dto;

import com.example.lab2.model.Apartment;

import java.util.List;

/**
 * DTO-клас для відповіді REST API зі списком квартир.
 * Використовується, коли результат треба повернути сторінками.
 */
public class ApartmentPageResponse {

    // Список квартир на поточній сторінці.
    private List<Apartment> items;

    // Номер поточної сторінки.
    private int page;

    // Кількість квартир на одній сторінці.
    private int size;

    // Загальна кількість квартир після фільтрації.
    private long totalItems;

    // Загальна кількість сторінок.
    private int totalPages;

    /**
     * Порожній конструктор потрібен для автоматичного перетворення об'єкта в JSON.
     */
    public ApartmentPageResponse() {
    }

    /**
     * Конструктор для швидкого створення відповіді з усіма даними пагінації.
     */
    public ApartmentPageResponse(List<Apartment> items, int page, int size, long totalItems, int totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<Apartment> getItems() {
        return items;
    }

    public void setItems(List<Apartment> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}