package com.example.lab2.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**  Модель квартири. Java-клас, який зберігає дані про одну квартиру. **/
// клас Apartment реалізує інтерфейс Serializable
public class Apartment implements Serializable {
    private Long id;    // Унікальний ідентифікатор квартири.
    private String title;   // Коротка назва оголошення.
    private String city;    // Місто, у якому розташована квартира.
    private String district;
    private int rooms;
    private double area;    // Площа квартири у квадратних метрах.
    private int floor;
    private BigDecimal pricePerMonth;
    private boolean furnished;  // Чи є меблі.
    private boolean petsAllowed;
    private String description;
    private String ownerName;
    private String phone;


    /** Конструктор потрібен, для заповнення форми. **/
    public Apartment() {
    }

    public Apartment(Long id, String title, String city, String district, int rooms, double area, int floor,
                     BigDecimal pricePerMonth, boolean furnished, boolean petsAllowed,
                     String description, String ownerName, String phone) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.district = district;
        this.rooms = rooms;
        this.area = area;
        this.floor = floor;
        this.pricePerMonth = pricePerMonth;
        this.furnished = furnished;
        this.petsAllowed = petsAllowed;
        this.description = description;
        this.ownerName = ownerName;
        this.phone = phone;
    }

    // Нижче йдуть getter-и та setter-и.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public int getRooms() { return rooms; }
    public void setRooms(int rooms) { this.rooms = rooms; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }

    public boolean isFurnished() { return furnished; }
    public void setFurnished(boolean furnished) { this.furnished = furnished; }

    public boolean isPetsAllowed() { return petsAllowed; }
    public void setPetsAllowed(boolean petsAllowed) { this.petsAllowed = petsAllowed; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
