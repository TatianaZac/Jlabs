<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Форма квартири"/>
<c:set var="pageStyle" value="owner-form.css"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h1>
    <c:choose>
        <c:when test="${empty apartment.id}">Додати квартиру</c:when>
        <c:otherwise>Редагувати квартиру</c:otherwise>
    </c:choose>
</h1>

<form class="apartment-form" method="post" action="${pageContext.request.contextPath}/owner/save-apartment">
    <input type="hidden" name="id" value="${apartment.id}">

    <div class="row">
        <div class="field">
            <label>Назва</label>
            <input type="text" name="title" value="${fn:escapeXml(apartment.title)}" required>
        </div>
        <div class="field">
            <label>Місто</label>
            <input type="text" name="city" value="${fn:escapeXml(apartment.city)}" required>
        </div>
        <div class="field">
            <label>Район</label>
            <input type="text" name="district" value="${fn:escapeXml(apartment.district)}" required>
        </div>
    </div>

    <div class="row">
        <div class="field">
            <label>Кількість кімнат</label>
            <input type="number" name="rooms" min="1" value="${apartment.rooms}">
        </div>
        <div class="field">
            <label>Площа, м²</label>
            <input type="number" name="area" step="0.1" min="1" value="${apartment.area}">
        </div>
        <div class="field">
            <label>Поверх</label>
            <input type="number" name="floor" min="1" value="${apartment.floor}">
        </div>
        <div class="field">
            <label>Ціна за місяць</label>
            <input type="number" name="pricePerMonth" step="0.01" min="0" value="${apartment.pricePerMonth}">
        </div>
    </div>

    <div class="row checkbox-row">
        <div class="checkbox-field">
            <label class="checkbox-label"><input type="checkbox" name="furnished" <c:if test="${apartment.furnished}">checked</c:if>> Є меблі</label>
        </div>
        <div class="checkbox-field">
            <label class="checkbox-label"><input type="checkbox" name="petsAllowed" <c:if test="${apartment.petsAllowed}">checked</c:if>> Дозволити тварин</label>
        </div>
    </div>

    <div class="row">
        <div class="field">
            <label>Опис</label>
            <textarea name="description"><c:out value="${apartment.description}"/></textarea>
        </div>
    </div>

    <div class="row">
        <div class="field">
            <label>Ім'я хазяїна</label>
            <input type="text" name="ownerName" value="${fn:escapeXml(apartment.ownerName)}" required>
        </div>
        <div class="field">
            <label>Телефон</label>
            <input type="text" name="phone" value="${fn:escapeXml(apartment.phone)}" required>
        </div>
    </div>

    <div class="actions form-actions">
        <button class="btn" type="submit">Зберегти</button>
        <a class="btn" href="${pageContext.request.contextPath}/owner/apartments">Скасувати</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
