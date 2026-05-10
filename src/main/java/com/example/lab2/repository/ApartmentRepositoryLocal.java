package com.example.lab2.repository;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;

import java.util.List;

/**
 * Локальний бізнес-інтерфейс рівня доступу до даних.
 * Для ЛР6 реалізація працює через JPA + DAO.
 */
public interface ApartmentRepositoryLocal {
    List<Apartment> findAll();

    Apartment findById(Long id);

    Apartment create(Apartment apartment);

    boolean update(Long id, Apartment apartment);

    boolean deleteById(Long id);

    List<Apartment> search(ApartmentSearchCriteria criteria);
}
