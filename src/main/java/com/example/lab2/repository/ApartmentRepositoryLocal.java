package com.example.lab2.repository;

import com.example.lab2.model.Apartment;

import java.util.List;

/**
 * Локальний бізнес-інтерфейс EJB для доступу до даних.
 * заглушка замість повноцінної БД: дані зберігаються у пам'яті.
 */
public interface ApartmentRepositoryLocal {
    List<Apartment> findAll();

    Apartment findById(Long id);

    Apartment create(Apartment apartment);

    boolean update(Long id, Apartment apartment);

    boolean deleteById(Long id);
}
