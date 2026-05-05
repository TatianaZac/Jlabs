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

    // Список квартир - тимчасова база даних
    private final List<Apartment> apartments = new ArrayList<>();

    // Генератор нових id для доданих квартир.
    private final AtomicLong idGenerator = new AtomicLong(1000);


    /**
     * Конструктор закритий, щоб сервіс не створювали через new.
     * Так контролюємо, що використовується лише один екземпляр.
     */

    private ApartmentService() {
        apartments.add(new Apartment(
                1L,
                "1-кімнатна квартира біля метро",
                "Київ",
                "Оболонь",
                1,
                42.5,
                5,
                new BigDecimal("14000"),
                true,
                true,
                "Світла квартира після ремонту. Можна з маленькою твариною.",
                "Олена",
                "+380501112233"
        ));

        apartments.add(new Apartment(
                2L,
                "2-кімнатна квартира в центрі",
                "Львів",
                "Галицький",
                2,
                63.0,
                3,
                new BigDecimal("18500"),
                true,
                false,
                "Поруч транспорт, магазини та парк. Ідеально для пари.",
                "Ігор",
                "+380671234567"
        ));

        apartments.add(new Apartment(
                3L,
                "Простора квартира для сім'ї",
                "Київ",
                "Позняки",
                3,
                86.0,
                9,
                new BigDecimal("23000"),
                false,
                true,
                "Три окремі кімнати, велика кухня, поруч школа та дитсадок.",
                "Марина",
                "+380931234567"
        ));

        apartments.add(new Apartment(
                4L,
                "Смарт-квартира",
                "Одеса",
                "Приморський",
                1,
                28.0,
                7,
                new BigDecimal("9000"),
                true,
                false,
                "Компактна квартира для студента або однієї людини.",
                "Дмитро",
                "+380661234567"
        ));

        apartments.add(new Apartment(
                5L,
                "Квартира біля університету",
                "Харків",
                "Шевченківський",
                1,
                34.0,
                4,
                new BigDecimal("8500"),
                true,
                false,
                "Зручний варіант для студента. Поруч метро та магазини.",
                "Наталія",
                "+380681112244"
        ));

        apartments.add(new Apartment(
                6L,
                "2-кімнатна квартира з балконом",
                "Київ",
                "Солом'янський",
                2,
                55.0,
                8,
                new BigDecimal("16500"),
                true,
                true,
                "Є меблі, техніка, балкон. Дозволено проживання з котом.",
                "Андрій",
                "+380991234111"
        ));

        apartments.add(new Apartment(
                7L,
                "Недорога квартира на тривалий термін",
                "Дніпро",
                "Соборний",
                1,
                38.0,
                2,
                new BigDecimal("7800"),
                false,
                true,
                "Проста квартира без меблів. Підійде для довгострокової оренди.",
                "Світлана",
                "+380631234222"
        ));

        apartments.add(new Apartment(
                8L,
                "Квартира з новим ремонтом",
                "Львів",
                "Франківський",
                2,
                60.0,
                6,
                new BigDecimal("21000"),
                true,
                false,
                "Сучасний ремонт, нова техніка, тихий район.",
                "Оксана",
                "+380671234333"
        ));

        apartments.add(new Apartment(
                9L,
                "3-кімнатна квартира біля парку",
                "Одеса",
                "Київський",
                3,
                74.0,
                5,
                new BigDecimal("19000"),
                true,
                true,
                "Поруч парк, школа та супермаркет. Підійде для сім'ї.",
                "Віктор",
                "+380501234444"
        ));

        apartments.add(new Apartment(
                10L,
                "Маленька квартира біля моря",
                "Одеса",
                "Приморський",
                1,
                31.0,
                10,
                new BigDecimal("12000"),
                true,
                false,
                "До моря 10 хвилин пішки. Є кондиціонер.",
                "Катерина",
                "+380931234555"
        ));

        apartments.add(new Apartment(
                11L,
                "Квартира для сімейної пари",
                "Київ",
                "Дарницький",
                2,
                52.0,
                11,
                new BigDecimal("15500"),
                true,
                false,
                "Затишна квартира у новому будинку. Поруч ТРЦ і метро.",
                "Роман",
                "+380661234666"
        ));

        apartments.add(new Apartment(
                12L,
                "Велика квартира з трьома кімнатами",
                "Харків",
                "Київський",
                3,
                82.0,
                7,
                new BigDecimal("17500"),
                false,
                true,
                "Простора квартира без меблів. Можна з тваринами.",
                "Юлія",
                "+380991234777"
        ));

        idGenerator.set(13);
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
    public synchronized List<Apartment> findAll() {
        return apartments.stream()
                .sorted(Comparator.comparing(Apartment::getId))
                .collect(Collectors.toList());
    }


    /**
     * Шукає квартиру за її id.
     * Якщо нічого не знайдено - повертає null.
     */
    public synchronized Apartment findById(Long id) {
        return apartments.stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findFirst()
                .orElse(null);
    }


    /**
     * Створює нову квартиру.
     * Генерує для неї id, додає у список і повертає створений об'єкт.
     */
    public synchronized Apartment create(Apartment apartment) {
        apartment.setId(idGenerator.getAndIncrement());
        apartments.add(apartment);
        return apartment;
    }

    /**
     * Зберігає квартиру.
     * Якщо id немає — створює нову квартиру.
     * Якщо id є — оновлює існуючу квартиру.
     */
    public synchronized void save(Apartment apartment) {
        if (apartment.getId() == null) {
            create(apartment);
            return;
        }

        update(apartment.getId(), apartment);
    }

    /**
     * Оновлює квартиру за id.
     * Повертає true, якщо квартира знайдена і оновлена.
     * Повертає false, якщо квартири з таким id немає.
     */
    public synchronized boolean update(Long id, Apartment apartment) {
        Apartment existing = findById(id);

        if (existing == null) {
            return false;
        }

        copyFields(apartment, existing);
        existing.setId(id);

        return true;
    }

    /**
     * Копіює дані з переданої квартири в уже існуючу квартиру.
     * Використовується під час редагування.
     */
    private void copyFields(Apartment source, Apartment target) {
        target.setTitle(source.getTitle());
        target.setCity(source.getCity());
        target.setDistrict(source.getDistrict());
        target.setRooms(source.getRooms());
        target.setArea(source.getArea());
        target.setFloor(source.getFloor());
        target.setPricePerMonth(source.getPricePerMonth());
        target.setFurnished(source.isFurnished());
        target.setPetsAllowed(source.isPetsAllowed());
        target.setDescription(source.getDescription());
        target.setOwnerName(source.getOwnerName());
        target.setPhone(source.getPhone());
    }


    /**
     * Видаляє квартиру за id.
     * Повертає true, якщо квартира була знайдена і видалена.
     */
    public synchronized boolean deleteById(Long id) {
        return apartments.removeIf(a -> Objects.equals(a.getId(), id));
    }

    /**
     * Видаляє квартиру за id.
     * Метод використовується старою JSP-частиною застосунку.
     */
    public synchronized void delete(Long id) {
        deleteById(id);
    }


    /**
     * Пошук квартир за введеними критеріями.
     * Кожен filter перевіряє одну умову.
     * Якщо певний критерій не заданий, цей filter фактично пропускає всі записи.
     */
    public synchronized List<Apartment> search(ApartmentSearchCriteria criteria) {
        ApartmentSearchCriteria safeCriteria = criteria == null ? new ApartmentSearchCriteria() : criteria;

        return apartments.stream()
                // Пошук по місту без урахування регістру.
                .filter(a -> isBlank(safeCriteria.getCity()) || a.getCity().toLowerCase(Locale.ROOT)
                        .contains(safeCriteria.getCity().trim().toLowerCase(Locale.ROOT)))

                // Кількість кімнат повинна бути не меншою за задану.
                .filter(a -> safeCriteria.getMinRooms() == null || a.getRooms() >= safeCriteria.getMinRooms())

                // Ціна повинна бути не більшою за максимальну.
                .filter(a -> safeCriteria.getMaxPrice() == null || a.getPricePerMonth().compareTo(safeCriteria.getMaxPrice()) <= 0)

                // Якщо furnished = null, то по меблях не фільтруємо.
                .filter(a -> safeCriteria.getFurnished() == null || a.isFurnished() == safeCriteria.getFurnished())

                // Аналогічна перевірка для тварин.
                .filter(a -> safeCriteria.getPetsAllowed() == null || a.isPetsAllowed() == safeCriteria.getPetsAllowed())

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
