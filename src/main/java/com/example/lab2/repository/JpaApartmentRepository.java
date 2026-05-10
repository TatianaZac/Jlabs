package com.example.lab2.repository;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.model.Owner;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA DAO для роботи з сутністю Apartment.
 * Тут замість JDBC використовується EntityManager та JPQL.
 */
@Stateless
public class JpaApartmentRepository implements ApartmentRepositoryLocal {

    @PersistenceContext(unitName = "ApartmentPU")
    private EntityManager entityManager;

    @Override
    public List<Apartment> findAll() {
        return entityManager.createQuery(
                        "SELECT a FROM Apartment a JOIN FETCH a.owner ORDER BY a.id",
                        Apartment.class)
                .getResultList();
    }

    @Override
    public Apartment findById(Long id) {
        List<Apartment> result = entityManager.createQuery(
                        "SELECT a FROM Apartment a JOIN FETCH a.owner WHERE a.id = :id",
                        Apartment.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public Apartment create(Apartment apartment) {
        Owner owner = resolveOwner(apartment);
        apartment.setOwner(owner);
        entityManager.persist(apartment);
        entityManager.flush();
        return apartment;
    }

    @Override
    public boolean update(Long id, Apartment apartment) {
        Apartment managed = findById(id);
        if (managed == null) {
            return false;
        }

        Owner owner = resolveOwner(apartment);

        managed.setTitle(apartment.getTitle());
        managed.setCity(apartment.getCity());
        managed.setDistrict(apartment.getDistrict());
        managed.setRooms(apartment.getRooms());
        managed.setArea(apartment.getArea());
        managed.setFloor(apartment.getFloor());
        managed.setPricePerMonth(apartment.getPricePerMonth());
        managed.setFurnished(apartment.isFurnished());
        managed.setPetsAllowed(apartment.isPetsAllowed());
        managed.setDescription(apartment.getDescription());
        managed.setOwner(owner);
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        Apartment apartment = entityManager.find(Apartment.class, id);
        if (apartment == null) {
            return false;
        }
        entityManager.remove(apartment);
        return true;
    }

    @Override
    public List<Apartment> search(ApartmentSearchCriteria criteria) {
        ApartmentSearchCriteria safeCriteria = criteria == null ? new ApartmentSearchCriteria() : criteria;

        StringBuilder jpql = new StringBuilder(
                "SELECT a FROM Apartment a JOIN FETCH a.owner o WHERE 1 = 1");
        List<ParameterBinder> binders = new ArrayList<>();

        if (hasText(safeCriteria.getCity())) {
            jpql.append(" AND LOWER(a.city) LIKE :city");
            String cityValue = "%" + safeCriteria.getCity().trim().toLowerCase() + "%";
            binders.add(query -> query.setParameter("city", cityValue));
        }
        if (safeCriteria.getMinRooms() != null) {
            jpql.append(" AND a.rooms >= :minRooms");
            binders.add(query -> query.setParameter("minRooms", safeCriteria.getMinRooms()));
        }
        if (safeCriteria.getMaxPrice() != null) {
            jpql.append(" AND a.pricePerMonth <= :maxPrice");
            binders.add(query -> query.setParameter("maxPrice", safeCriteria.getMaxPrice()));
        }
        if (safeCriteria.getFurnished() != null) {
            jpql.append(" AND a.furnished = :furnished");
            binders.add(query -> query.setParameter("furnished", safeCriteria.getFurnished()));
        }
        if (safeCriteria.getPetsAllowed() != null) {
            jpql.append(" AND a.petsAllowed = :petsAllowed");
            binders.add(query -> query.setParameter("petsAllowed", safeCriteria.getPetsAllowed()));
        }

        jpql.append(" ORDER BY a.pricePerMonth ASC, a.id ASC");

        TypedQuery<Apartment> query = entityManager.createQuery(jpql.toString(), Apartment.class);
        for (ParameterBinder binder : binders) {
            binder.bind(query);
        }
        return query.getResultList();
    }

    private Owner resolveOwner(Apartment apartment) {
        String ownerName = apartment.getOwnerName();
        String phone = apartment.getPhone();

        try {
            Owner existingOwner = entityManager.createQuery(
                            "SELECT o FROM Owner o WHERE o.phone = :phone",
                            Owner.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            existingOwner.setName(ownerName);
            return existingOwner;
        } catch (NoResultException e) {
            Owner owner = new Owner(ownerName, phone);
            entityManager.persist(owner);
            entityManager.flush();
            return owner;
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @FunctionalInterface
    private interface ParameterBinder {
        void bind(TypedQuery<Apartment> query);
    }
}
