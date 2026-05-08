package com.example.lab2.api.resource;

import com.example.lab2.api.dto.ApiError;
import com.example.lab2.api.dto.ApartmentPageResponse;
import com.example.lab2.api.dto.ApartmentRequest;
import com.example.lab2.model.Apartment;
import com.example.lab2.model.ApartmentSearchCriteria;
import com.example.lab2.service.ApartmentServiceLocal;
import jakarta.ejb.EJB;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * REST-ресурс для роботи з квартирами.
 * Тут реалізовано CRUD, фільтрацію, пагінацію та HTTP-статуси.
 */

@Path("/apartments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApartmentResource {

    /**
     * apartmentService — це сервісний шар.Він виконує основну логіку роботи з квартирами:
     */
    @EJB
    private ApartmentServiceLocal apartmentService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Метод для отримання списку квартир.
     * READ + фільтрація + пагінація.
     * Приклад:
     * GET /api/apartments?city=Київ&minRooms=1&maxPrice=20000&page=1&size=5
     */
    @GET
    public Response findApartments(

            /**
             * @QueryParam читає параметри після знака ? у URL.
             * /api/apartments?city=Київ
             */

            @QueryParam("city") String city,
            @QueryParam("minRooms") Integer minRooms,
            @QueryParam("maxPrice") BigDecimal maxPrice,
            @QueryParam("furnished") Boolean furnished,
            @QueryParam("petsAllowed") Boolean petsAllowed,

//          @DefaultValue("1") означає шо якщо клієнт не передав page, тоді page буде 1.
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("5") @QueryParam("size") int size
    ) {
        if (page < 1) {
            return badRequest(List.of("page має бути не менше 1"));
        }
        if (size < 1 || size > 50) {
            return badRequest(List.of("size має бути від 1 до 50"));
        }

        /**
         * Створюємо об'єкт критеріїв пошуку.
         * ApartmentSearchCriteria — це окремий клас у який складаються всі параметри фільтрації.
         */
        ApartmentSearchCriteria criteria = new ApartmentSearchCriteria();

        // trimToNull(city) прибирає зайві пробіли.
        criteria.setCity(trimToNull(city));
        criteria.setMinRooms(minRooms);
        criteria.setMaxPrice(maxPrice);
        criteria.setFurnished(furnished);
        criteria.setPetsAllowed(petsAllowed);


//      Передаємо критерії в сервіс.Сервіс повертає список квартир, які підходять під умови пошуку.
        List<Apartment> filtered = apartmentService.search(criteria);
        int totalItems = filtered.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        int fromIndex = Math.min((page - 1) * size, totalItems); // з якого елемента починається поточна сторінка
        int toIndex = Math.min(fromIndex + size, totalItems); //до якого елемента брати список
        List<Apartment> pageItems = filtered.subList(fromIndex, toIndex); //subList бере тільки частину списку


//      DTO-відповідь для сторінки.
        ApartmentPageResponse response = new ApartmentPageResponse(
                pageItems,
                page,
                size,
                totalItems,
                totalPages
        );
        return Response.ok(response).build();
    }

    /**
     * Метод для отримання однієї квартири за id.
     */
    @GET
    @Path("/{id}") // {id} означає, що id береться прямо з URL.
    public Response findById(@PathParam("id") Long id) {
        Apartment apartment = apartmentService.findById(id);
        if (apartment == null) {
            return notFound("Квартиру з id=" + id + " не знайдено");
        }
        return Response.ok(apartment).build();
    }

    /**
     * CREATE.
     */
    @POST
    public Response create(ApartmentRequest request, @Context UriInfo uriInfo) {
        List<String> errors = validate(request);
        if (!errors.isEmpty()) {
            return badRequest(errors);
        }

        /**
         * toApartment(request) перетворює DTO ApartmentRequest у звичайний об'єкт Apartment.
         * Потім сервіс створює нову квартиру.
         */
        Apartment created = apartmentService.create(toApartment(request));

        /**
         * Location показує адресу новоствореного ресурсу.
         * Location буде: /api/apartments/10
         */
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(created.getId()))
                .build();

//      Повертаємо HTTP 201 Created + створену квартиру в тілі відповіді.
        return Response.created(location)
                .entity(created)
                .build();
    }

    /**
     * UPDATE.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ApartmentRequest request) {
        List<String> errors = validate(request);
        if (!errors.isEmpty()) {
            return badRequest(errors);
        }
        boolean updated = apartmentService.update(id, toApartment(request));
        if (!updated) {
            return notFound("Квартиру з id=" + id + " не знайдено");
        }
        return Response.ok(apartmentService.findById(id)).build();
    }

    /**
     * DELETE.
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = apartmentService.deleteById(id);
        if (!deleted) {
            return notFound("Квартиру з id=" + id + " не знайдено");
        }
        return Response.noContent().build();
    }

    /**
     * Допоміжний метод, перетворює ApartmentRequest у Apartment.
     * ApartmentRequest — це дані, які прийшли від клієнта.
     * Apartment — це модель квартири, з якою працює програма.
     */
    private Apartment toApartment(ApartmentRequest request) {
        Apartment apartment = new Apartment();

//       Далі переносимо дані з request у apartment.

        apartment.setTitle(request.getTitle());
        apartment.setCity(request.getCity());
        apartment.setDistrict(request.getDistrict());
        apartment.setRooms(request.getRooms());
        apartment.setArea(request.getArea().doubleValue());
        apartment.setFloor(request.getFloor());
        apartment.setPricePerMonth(request.getPricePerMonth());
        apartment.setFurnished(request.getFurnished());
        apartment.setPetsAllowed(request.getPetsAllowed());
        apartment.setDescription(request.getDescription());
        apartment.setOwnerName(request.getOwnerName());
        apartment.setPhone(request.getPhone());
        return apartment;
    }

    /**
     * Метод для перевірки ApartmentRequest.Він повертає список помилок
     * Якщо список порожній - дані правильні.Якщо список не порожній - у запиті є помилки.
     */
    private List<String> validate(ApartmentRequest request) {
        if (request == null) {
            return List.of("Тіло запиту не може бути порожнім");
        }

        /**
         * validator.validate(request) запускає Bean Validation. Він перевіряє всі анотації в ApartmentRequest.
         * Наприклад: @NotBlank @NotNull @Min @Max
         */
        Set<ConstraintViolation<ApartmentRequest>> violations = validator.validate(request);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .sorted()
                .toList();
    }

    /**
     * Допоміжний метод для відповіді 400 Bad Request.
     * Використовується тоді, коли клієнт передав неправильні дані.
     */
    private Response badRequest(List<String> errors) {
        /**
         * Response.status(Response.Status.BAD_REQUEST)- встановлює HTTP-код 400.
         * entity(...) задає тіло відповіді. ApiError - це DTO для помилки.
         */
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiError("Запит містить некоректні дані", errors))
                .build();
    }

    /**
     * Допоміжний метод для відповіді 404 Not Found.
     * Використовується тоді, коли потрібний ресурс не знайдено.
     */
    private Response notFound(String message) {

//      Response.Status.NOT_FOUND — це HTTP-код 404.
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiError(message))
                .build();
    }


    private String trimToNull(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }
}