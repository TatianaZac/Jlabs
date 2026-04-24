<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Кабінет хазяїна"/>
<c:set var="pageStyle" value="owner-list.css"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h1>Кабінет хазяїна квартири</h1>
<p class="owner-toolbar"><a class="btn" href="${pageContext.request.contextPath}/owner/apartment-form">Додати квартиру</a></p>

<table class="table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Назва</th>
        <th>Локація</th>
        <th>Параметри</th>
        <th>Ціна</th>
        <th>Статус</th>
        <th>Дії</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="apartment" items="${apartments}">
        <tr>
            <td>${apartment.id}</td>
            <td><c:out value="${apartment.title}"/></td>
            <td>
                <c:out value="${apartment.city}"/>,
                <c:out value="${apartment.district}"/>
            </td>
            <td>
                ${apartment.rooms} кімн., ${apartment.area} м², ${apartment.floor} поверх
            </td>
            <td>${apartment.pricePerMonth} грн/міс</td>
            <td class="owner-status">
                <c:if test="${apartment.furnished}">
                    <div class="success">Є меблі</div>
                </c:if>
                <c:if test="${not apartment.furnished}">
                    <div class="warning">Без меблів</div>
                </c:if>

                <c:choose>
                    <c:when test="${apartment.petsAllowed}">
                        <div>Тварини дозволені</div>
                    </c:when>
                    <c:otherwise>
                        <div>Без тварин</div>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="owner-actions">
                <div class="actions">
                    <a class="btn" href="${pageContext.request.contextPath}/owner/apartment-form?id=${apartment.id}">Редагувати</a>
                    <form class="inline-form" method="post" action="${pageContext.request.contextPath}/owner/delete-apartment">
                        <input type="hidden" name="id" value="${apartment.id}">
                        <button class="btn" type="submit">Видалити</button>
                    </form>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
