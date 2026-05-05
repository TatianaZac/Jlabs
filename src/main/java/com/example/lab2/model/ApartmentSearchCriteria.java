package com.example.lab2.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Об'єкт з параметрами пошуку.
 * Тут зберігаються не дані самої квартири, а фільтри, які вводить клієнт на сторінці пошуку.
 */

public class ApartmentSearchCriteria implements Serializable {
    private String city;
    private Integer minRooms;
    private BigDecimal maxPrice;

    // Якщо null — цей параметр не враховується.
    // Якщо true/false — шукаємо тільки відповідні квартири.
    private Boolean furnished;

    // Аналогічно: null означає не фільтрувати за цією ознакою.
    private Boolean petsAllowed;

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Integer getMinRooms() { return minRooms; }
    public void setMinRooms(Integer minRooms) { this.minRooms = minRooms; }
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    public Boolean getFurnished() { return furnished; }
    public void setFurnished(Boolean furnished) { this.furnished = furnished; }
    public Boolean getPetsAllowed() { return petsAllowed; }
    public void setPetsAllowed(Boolean petsAllowed) { this.petsAllowed = petsAllowed; }
}
