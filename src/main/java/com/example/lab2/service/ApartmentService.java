package com.example.lab2.service;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.repository.ApartmentRepositoryLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

/**
 * EJB-компонент бізнес-логіки.
 * Саме тут знаходяться правила роботи з квартирами: створення, редагування,
 * видалення, отримання списку та пошук за параметрами.
 */
@Stateless
public class ApartmentService implements ApartmentServiceLocal {

    /**
     * Ін'єкція JPA DAO рівня доступу до даних.
     */
    @EJB
    private ApartmentRepositoryLocal apartmentRepository;

    @Override
    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    @Override
    public Apartment findById(Long id) {
        return apartmentRepository.findById(id);
    }

    @Override
    public Apartment create(Apartment apartment) {
        return apartmentRepository.create(apartment);
    }

    @Override
    public void save(Apartment apartment) {
        if (apartment.getId() == null) {
            create(apartment);
            return;
        }
        update(apartment.getId(), apartment);
    }

    @Override
    public boolean update(Long id, Apartment apartment) {
        return apartmentRepository.update(id, apartment);
    }

    @Override
    public boolean deleteById(Long id) {
        return apartmentRepository.deleteById(id);
    }

    @Override
    public void delete(Long id) {
        deleteById(id);
    }

    @Override
    public List<Apartment> search(ApartmentSearchCriteria criteria) {
        return apartmentRepository.search(criteria);
    }
}
