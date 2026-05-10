package com.example.lab2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;

/**  JPA-сутність квартири. **/
@Entity
@Table(name = "apartments")
public class Apartment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false)
    private int rooms;

    @Column(nullable = false)
    private double area;

    @Column(nullable = false)
    private int floor;

    @Column(name = "price_per_month", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerMonth;

    @Column(nullable = false)
    private boolean furnished;

    @Column(name = "pets_allowed", nullable = false)
    private boolean petsAllowed;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    /** Конструктор потрібен для JPA та заповнення форми. */
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
        this.owner = new Owner(ownerName, phone);
    }

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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Transient
    public String getOwnerName() {
        return owner != null ? owner.getName() : null;
    }

    public void setOwnerName(String ownerName) {
        ensureOwner();
        owner.setName(ownerName);
    }

    @Transient
    public String getPhone() {
        return owner != null ? owner.getPhone() : null;
    }

    public void setPhone(String phone) {
        ensureOwner();
        owner.setPhone(phone);
    }

    private void ensureOwner() {
        if (owner == null) {
            owner = new Owner();
        }
    }
}
