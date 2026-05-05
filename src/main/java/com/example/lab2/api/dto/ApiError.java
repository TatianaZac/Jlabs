package com.example.lab2.api.dto;

import java.util.List;

/**
 * DTO для повернення помилок у JSON-форматі.
 * Використовується, коли REST API має повідомити клієнту про помилку.
 */
public class ApiError {

    // Загальне повідомлення про помилку.
    private String message;

    // Список конкретних помилок, наприклад помилки валідації полів.
    private List<String> errors;

    /**
     * Порожній конструктор потрібен для автоматичного перетворення об'єкта в JSON.
     */
    public ApiError() {
    }

    /**
     * Конструктор для помилки з одним загальним повідомленням.
     */
    public ApiError(String message) {
        this.message = message;
    }

    /**
     * Конструктор для помилки із загальним повідомленням і списком деталей.
     */
    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}