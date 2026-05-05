package com.example.lab2.api.dto;

import com.example.lab2.api.validation.ApartmentLayout;
import com.example.lab2.api.validation.UkrainianPhone;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO для створення та редагування квартири через REST API.
 * Тут додано стандартну Bean Validation і кастомні перевірки.
 */
@ApartmentLayout
public class ApartmentRequest {

    // Основна інформація про квартиру.
    @NotBlank(message = "Назва квартири не може бути порожньою")
    @Size(max = 100, message = "Назва квартири не може бути довшою за 100 символів")
    private String title;

    @NotBlank(message = "Місто не може бути порожнім")
    @Size(max = 50, message = "Назва міста не може бути довшою за 50 символів")
    private String city;

    @NotBlank(message = "Район не може бути порожнім")
    @Size(max = 50, message = "Назва району не може бути довшою за 50 символів")
    private String district;

    // Характеристики квартири.
    @NotNull(message = "Кількість кімнат потрібно вказати")
    @Min(value = 1, message = "Кімнат має бути не менше 1")
    @Max(value = 10, message = "Кімнат має бути не більше 10")
    private Integer rooms;

    @NotNull(message = "Площу потрібно вказати")
    @DecimalMin(value = "10.0", message = "Площа має бути не меншою за 10 м²")
    private BigDecimal area;

    @NotNull(message = "Поверх потрібно вказати")
    @Min(value = 1, message = "Поверх має бути не менше 1")
    @Max(value = 50, message = "Поверх має бути не більше 50")
    private Integer floor;

    @NotNull(message = "Ціну потрібно вказати")
    @DecimalMin(value = "1.0", message = "Ціна має бути більшою за 0")
    private BigDecimal pricePerMonth;

    @NotNull(message = "Потрібно вказати, чи є меблі")
    private Boolean furnished;

    @NotNull(message = "Потрібно вказати, чи дозволені тварини")
    private Boolean petsAllowed;

    @NotBlank(message = "Опис не може бути порожнім")
    @Size(max = 500, message = "Опис не може бути довшим за 500 символів")
    private String description;

    @NotBlank(message = "Ім'я власника не може бути порожнім")
    @Size(max = 60, message = "Ім'я власника не може бути довшим за 60 символів")
    private String ownerName;

    // Контактні дані власника.
    @NotBlank(message = "Телефон не може бути порожнім")
    @UkrainianPhone
    private String phone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public BigDecimal getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(BigDecimal pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public Boolean getFurnished() {
        return furnished;
    }

    public void setFurnished(Boolean furnished) {
        this.furnished = furnished;
    }

    public Boolean getPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(Boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
