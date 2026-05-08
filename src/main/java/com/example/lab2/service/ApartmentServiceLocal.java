package com.example.lab2.service;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;

import java.util.List;

/**
 * Локальний бізнес-інтерфейс EJB для рівня бізнес-логіки.
 * Через цей інтерфейс сервлети і REST-ресурс звертаються до бізнес-логіки.
 */
public interface ApartmentServiceLocal {
    List<Apartment> findAll();

    Apartment findById(Long id);

    Apartment create(Apartment apartment);

    void save(Apartment apartment);

    boolean update(Long id, Apartment apartment);

    boolean deleteById(Long id);

    void delete(Long id);

    List<Apartment> search(ApartmentSearchCriteria criteria);
}
