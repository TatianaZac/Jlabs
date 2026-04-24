package com.example.lab2.service;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Сервісний клас, який працює з квартирами.
 * тут поки нема бази даних,тому дані зберігаються в пам'яті у списку apartments.
 */


public class ApartmentService {
    // Singleton: один спільний об'єкт сервісу на весь застосунок.
    private static final ApartmentService INSTANCE = new ApartmentService();

    // Список квартир - тимчасова "база даних".
    private final List<Apartment> apartments = new ArrayList<>();

    // Генератор нових id для доданих квартир.
    private final AtomicLong idGenerator = new AtomicLong(1000);


    /**
     * Конструктор закритий, щоб сервіс не створювали через new.
     * Так контролюємо, що використовується лише один екземпляр.
     */

    private ApartmentService() {
        // Тестові дані для демонстрації роботи застосунку.
        apartments.add(new Apartment(1L, "1-кімнатна квартира біля метро", "Київ", "Оболонь", 1, 42.5, 5,
                new BigDecimal("14000"), true, true,
                "Світла квартира після ремонту. Можна з маленькою твариною.", "Олена", "+380501112233"));
        apartments.add(new Apartment(2L, "2-кімнатна квартира в центрі", "Львів", "Галицький", 2, 63.0, 3,
                new BigDecimal("18500"), true, false,
                "Поруч транспорт, магазини та парк. Ідеально для пари.", "Ігор", "+380671234567"));
        apartments.add(new Apartment(3L, "Простора квартира для сім'ї", "Київ", "Позняки", 3, 86.0, 9,
                new BigDecimal("23000"), false, true,
                "Три окремі кімнати, велика кухня, поруч школа та дитсадок.", "Марина", "+380931234567"));
        apartments.add(new Apartment(4L, "Смарт-квартира", "Одеса", "Приморський", 1, 28.0, 7,
                new BigDecimal("9000"), true, false,
                "Компактна квартира для студента або однієї людини.", "Дмитро", "+380661234567"));

        // Наступна нова квартира отримає id = 5.
        idGenerator.set(5);
    }

    /**
     * Повертає єдиний екземпляр сервісу.
     */
    public static ApartmentService getInstance() {
        return INSTANCE;
    }

    /**
     * Повертає всі квартири, відсортовані за id.
     */
    public List<Apartment> findAll() {
        return apartments.stream()
                .sorted(Comparator.comparing(Apartment::getId))
                .collect(Collectors.toList());
    }


    /**
     * Шукає квартиру за її id.
     * Якщо нічого не знайдено - повертає null.
     */
    public Apartment findById(Long id) {
        return apartments.stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findFirst()
                .orElse(null);
    }


    /**
     * Зберігає квартиру.Якщо id немає — це нова квартира, тому додаємо її у список.
     * Якщо id є — це редагування, тому оновлюємо існуючий запис.
     */
    public void save(Apartment apartment) {
        if (apartment.getId() == null) {
            // Для нової квартири генеруємо id і додаємо її у список.
            apartment.setId(idGenerator.getAndIncrement());
            apartments.add(apartment);
            return;
        }

        // Якщо квартира вже існує — оновлюємо її поля.
        Apartment existing = findById(apartment.getId());
        if (existing != null) {
            existing.setTitle(apartment.getTitle());
            existing.setCity(apartment.getCity());
            existing.setDistrict(apartment.getDistrict());
            existing.setRooms(apartment.getRooms());
            existing.setArea(apartment.getArea());
            existing.setFloor(apartment.getFloor());
            existing.setPricePerMonth(apartment.getPricePerMonth());
            existing.setFurnished(apartment.isFurnished());
            existing.setPetsAllowed(apartment.isPetsAllowed());
            existing.setDescription(apartment.getDescription());
            existing.setOwnerName(apartment.getOwnerName());
            existing.setPhone(apartment.getPhone());
        }
    }


    /**
     * Видаляє квартиру за id.
     */
    public void delete(Long id) {
        apartments.removeIf(a -> Objects.equals(a.getId(), id));
    }


    /**
     * Пошук квартир за введеними критеріями.
     * Кожен filter перевіряє одну умову.
     * Якщо певний критерій не заданий, цей filter фактично пропускає всі записи.
     */
    public List<Apartment> search(ApartmentSearchCriteria criteria) {
        return apartments.stream()
                // Пошук по місту без урахування регістру.
                .filter(a -> isBlank(criteria.getCity()) || a.getCity().toLowerCase(Locale.ROOT)
                        .contains(criteria.getCity().trim().toLowerCase(Locale.ROOT)))

                // Кількість кімнат повинна бути не меншою за задану.
                .filter(a -> criteria.getMinRooms() == null || a.getRooms() >= criteria.getMinRooms())

                // Ціна повинна бути не більшою за максимальну.
                .filter(a -> criteria.getMaxPrice() == null || a.getPricePerMonth().compareTo(criteria.getMaxPrice()) <= 0)

                // Якщо furnished = null, то по меблях не фільтруємо.
                .filter(a -> criteria.getFurnished() == null || a.isFurnished() == criteria.getFurnished())

                // Аналогічна перевірка для тварин.
                .filter(a -> criteria.getPetsAllowed() == null || a.isPetsAllowed() == criteria.getPetsAllowed())

                // Для зручності виводимо результати від дешевших до дорожчих.
                .sorted(Comparator.comparing(Apartment::getPricePerMonth))
                .collect(Collectors.toList());
    }

    /**
     * Допоміжний метод: перевіряє, що рядок порожній або складається лише з пробілів.
     */

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
