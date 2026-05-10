package com.example.lab2.repository;

import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC DAO для роботи з таблицею apartments.
 * Саме тут знаходяться SQL-запити та перетворення ResultSet -> Apartment.
 */

public class JdbcApartmentRepository implements ApartmentRepositoryLocal {

    private static final String FIND_ALL_SQL = """
            SELECT id, title, city, district, rooms, area, floor, price_per_month,
                   furnished, pets_allowed, description, owner_name, phone
            FROM apartments
            ORDER BY id
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, title, city, district, rooms, area, floor, price_per_month,
                   furnished, pets_allowed, description, owner_name, phone
            FROM apartments
            WHERE id = ?
            """;

    private static final String INSERT_SQL = """
            INSERT INTO apartments (
                title, city, district, rooms, area, floor, price_per_month,
                furnished, pets_allowed, description, owner_name, phone
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE apartments
            SET title = ?,
                city = ?,
                district = ?,
                rooms = ?,
                area = ?,
                floor = ?,
                price_per_month = ?,
                furnished = ?,
                pets_allowed = ?,
                description = ?,
                owner_name = ?,
                phone = ?
            WHERE id = ?
            """;

    private static final String DELETE_SQL = "DELETE FROM apartments WHERE id = ?";

    @Resource(lookup = "jdbc/ApartmentDS")
    private DataSource dataSource;

    @Override
    public List<Apartment> findAll() {
        List<Apartment> apartments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                apartments.add(mapRow(resultSet));
            }
            return apartments;
        } catch (SQLException e) {
            throw new EJBException("Не вдалося отримати список квартир з БД", e);
        }
    }

    @Override
    public Apartment findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? mapRow(resultSet) : null;
            }
        } catch (SQLException e) {
            throw new EJBException("Не вдалося отримати квартиру з id=" + id, e);
        }
    }

    @Override
    public Apartment create(Apartment apartment) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(statement, apartment);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    apartment.setId(keys.getLong(1));
                }
            }
            return apartment;
        } catch (SQLException e) {
            throw new EJBException("Не вдалося створити квартиру", e);
        }
    }

    @Override
    public boolean update(Long id, Apartment apartment) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            fillStatement(statement, apartment);
            statement.setLong(13, id);

            int updatedRows = statement.executeUpdate();
            if (updatedRows > 0) {
                apartment.setId(id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new EJBException("Не вдалося оновити квартиру з id=" + id, e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new EJBException("Не вдалося видалити квартиру з id=" + id, e);
        }
    }

    @Override
    public List<Apartment> search(ApartmentSearchCriteria criteria) {
        ApartmentSearchCriteria safeCriteria = criteria == null ? new ApartmentSearchCriteria() : criteria;

        StringBuilder sql = new StringBuilder("""
                SELECT id, title, city, district, rooms, area, floor, price_per_month,
                       furnished, pets_allowed, description, owner_name, phone
                FROM apartments
                WHERE 1=1
                """);

        List<Object> parameters = new ArrayList<>();

        if (hasText(safeCriteria.getCity())) {
            sql.append(" AND LOWER(city) LIKE ? ");
            parameters.add("%" + safeCriteria.getCity().trim().toLowerCase() + "%");
        }
        if (safeCriteria.getMinRooms() != null) {
            sql.append(" AND rooms >= ? ");
            parameters.add(safeCriteria.getMinRooms());
        }
        if (safeCriteria.getMaxPrice() != null) {
            sql.append(" AND price_per_month <= ? ");
            parameters.add(safeCriteria.getMaxPrice());
        }
        if (safeCriteria.getFurnished() != null) {
            sql.append(" AND furnished = ? ");
            parameters.add(safeCriteria.getFurnished());
        }
        if (safeCriteria.getPetsAllowed() != null) {
            sql.append(" AND pets_allowed = ? ");
            parameters.add(safeCriteria.getPetsAllowed());
        }

        sql.append(" ORDER BY price_per_month ASC, id ASC ");

        List<Apartment> apartments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    apartments.add(mapRow(resultSet));
                }
            }
            return apartments;
        } catch (SQLException e) {
            throw new EJBException("Не вдалося виконати пошук квартир", e);
        }
    }

    private void fillStatement(PreparedStatement statement, Apartment apartment) throws SQLException {
        statement.setString(1, apartment.getTitle());
        statement.setString(2, apartment.getCity());
        statement.setString(3, apartment.getDistrict());
        statement.setInt(4, apartment.getRooms());
        statement.setDouble(5, apartment.getArea());
        statement.setInt(6, apartment.getFloor());
        statement.setBigDecimal(7, apartment.getPricePerMonth());
        statement.setBoolean(8, apartment.isFurnished());
        statement.setBoolean(9, apartment.isPetsAllowed());
        statement.setString(10, apartment.getDescription());
        statement.setString(11, apartment.getOwnerName());
        statement.setString(12, apartment.getPhone());
    }

    private Apartment mapRow(ResultSet resultSet) throws SQLException {
        Apartment apartment = new Apartment();
        apartment.setId(resultSet.getLong("id"));
        apartment.setTitle(resultSet.getString("title"));
        apartment.setCity(resultSet.getString("city"));
        apartment.setDistrict(resultSet.getString("district"));
        apartment.setRooms(resultSet.getInt("rooms"));
        apartment.setArea(resultSet.getDouble("area"));
        apartment.setFloor(resultSet.getInt("floor"));
        apartment.setPricePerMonth(resultSet.getBigDecimal("price_per_month"));
        apartment.setFurnished(resultSet.getBoolean("furnished"));
        apartment.setPetsAllowed(resultSet.getBoolean("pets_allowed"));
        apartment.setDescription(resultSet.getString("description"));
        apartment.setOwnerName(resultSet.getString("owner_name"));
        apartment.setPhone(resultSet.getString("phone"));
        return apartment;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
