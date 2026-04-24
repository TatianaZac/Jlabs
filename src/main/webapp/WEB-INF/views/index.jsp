<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="ЛР2 — Оренда квартир"/>
<c:set var="pageStyle" value="home.css"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>


<div class="row"> <%--контейнер для клієнта та хозяїна--%>

    <div class="card field home-card">
        <h2>Клієнт</h2>
        <p>Пошук квартири за параметрами: місто, кількість кімнат, ціна, меблі, тварини.</p>
        <a class="btn" href="${pageContext.request.contextPath}/client/search">Відкрити пошук</a>
        <%--Посилання на сторінку пошуку квартир для клієнта.
            contextPath додається, щоб шлях працював правильно після запуску на сервері --%>
    </div>

    <div class="card field home-card">
        <h2>Хазяїн квартири</h2>
        <p>Створення, редагування та видалення інформації про квартиру.</p>
        <a class="btn" href="${pageContext.request.contextPath}/owner/apartments">Відкрити кабінет</a>
        <%--Посилання на сторінку кабінету хазяїна, де можна переглядати, додавати, редагувати та видаляти квартири--%>
    </div>

</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
