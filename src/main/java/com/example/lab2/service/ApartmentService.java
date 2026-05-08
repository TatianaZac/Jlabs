package com.example.lab2.service;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.repository.ApartmentRepositoryLocal;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * EJB-компонент бізнес-логіки.
 * Тут знаходяться правила роботи з квартирами: створення, редагування,
 * видалення, отримання списку та пошук за параметрами.
 */
@Stateless
public class ApartmentService implements ApartmentServiceLocal {

    /**
     * Ін'єкція EJB-заглушки рівня доступу до даних.
     * У наступній лабораторній цю заглушку можна буде замінити на JDBC/JPA DAO.
     */
    @EJB
    private ApartmentRepositoryLocal apartmentRepository;

    /**
     * Повертає всі квартири.
     */
    @Override
    public List<Apartment> findAll() {
        return apartmentRepository.findAll();
    }

    /**
     * Шукає квартиру за id.
     */
    @Override
    public Apartment findById(Long id) {
        return apartmentRepository.findById(id);
    }

    /**
     * Створює нову квартиру. Бізнес-логіка делегує збереження репозиторію.
     */
    @Override
    public Apartment create(Apartment apartment) {
        return apartmentRepository.create(apartment);
    }

    /**
     * Якщо id немає — створює квартиру, якщо id є — оновлює існуючу.
     */
    @Override
    public void save(Apartment apartment) {
        if (apartment.getId() == null) {
            create(apartment);
            return;
        }
        update(apartment.getId(), apartment);
    }

    /**
     * Оновлює квартиру за id.
     */
    @Override
    public boolean update(Long id, Apartment apartment) {
        return apartmentRepository.update(id, apartment);
    }

    /**
     * Видаляє квартиру за id.
     */
    @Override
    public boolean deleteById(Long id) {
        return apartmentRepository.deleteById(id);
    }

    /**
     * Метод для старої JSP-частини застосунку.
     */
    @Override
    public void delete(Long id) {
        deleteById(id);
    }

    /**
     * Пошук квартир за параметрами клієнта.
     * Це саме бізнес-логіка: ми беремо всі дані з репозиторію і відбираємо
     * тільки ті квартири, які підходять під критерії.
     */
    @Override
    public List<Apartment> search(ApartmentSearchCriteria criteria) {
        ApartmentSearchCriteria safeCriteria = criteria == null ? new ApartmentSearchCriteria() : criteria;

        return apartmentRepository.findAll().stream()
                .filter(a -> isBlank(safeCriteria.getCity()) || a.getCity().toLowerCase(Locale.ROOT)
                        .contains(safeCriteria.getCity().trim().toLowerCase(Locale.ROOT)))
                .filter(a -> safeCriteria.getMinRooms() == null || a.getRooms() >= safeCriteria.getMinRooms())
                .filter(a -> safeCriteria.getMaxPrice() == null || a.getPricePerMonth().compareTo(safeCriteria.getMaxPrice()) <= 0)
                .filter(a -> safeCriteria.getFurnished() == null || a.isFurnished() == safeCriteria.getFurnished())
                .filter(a -> safeCriteria.getPetsAllowed() == null || a.isPetsAllowed() == safeCriteria.getPetsAllowed())
                .sorted(Comparator.comparing(Apartment::getPricePerMonth))
                .collect(Collectors.toList());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
