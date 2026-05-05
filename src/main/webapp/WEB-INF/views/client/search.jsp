<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="Пошук квартир"/>
<c:set var="pageStyle" value="client-search.css"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h1>Пошук квартири по параметрам</h1>

<form class="search-form" method="get" action="${pageContext.request.contextPath}/client/search">
    <div class="row">

        <div class="field">
            <label>Місто</label>
            <input type="text" name="city" value="${fn:escapeXml(criteria.city)}">
        </div>

        <div class="field">
            <label>Мінімум кімнат</label>
            <input type="number" name="minRooms" min="1" value="${criteria.minRooms}">
        </div>

        <div class="field">
            <label>Максимальна ціна</label>
            <input type="number" name="maxPrice" min="0" step="0.01" value="${criteria.maxPrice}">
        </div>

        <div class="field">
            <label>Меблі</label>
            <select name="furnished">
                <option value="">Неважливо</option>
                <option value="true" ${criteria.furnished == true ? 'selected' : ''}>Так</option>
                <option value="false" ${criteria.furnished == false ? 'selected' : ''}>Ні</option>
            </select>
        </div>

        <div class="field">
            <label>Тварини</label>
            <select name="petsAllowed">
                <option value="">Неважливо</option>
                <option value="true" ${criteria.petsAllowed == true ? 'selected' : ''}>Так</option>
                <option value="false" ${criteria.petsAllowed == false ? 'selected' : ''}>Ні</option>
            </select>
        </div>

        <div class="field">
            <label>На сторінці</label>
            <select name="size">
                <option value="3" ${pageSize == 3 ? 'selected' : ''}>3</option>
                <option value="5" ${pageSize == 5 ? 'selected' : ''}>5</option>
                <option value="10" ${pageSize == 10 ? 'selected' : ''}>10</option>
            </select>
        </div>

    </div>
    <div class="search-actions">
        <button class="btn" type="submit">Знайти</button>
    </div>
</form>

<h2 class="search-results-title">Результати</h2>

<c:choose>
    <c:when test="${empty results}">
        <p class="warning">За заданими параметрами квартир не знайдено.</p>
    </c:when>

    <c:otherwise>
        <p class="pagination-info">
            Знайдено оголошень: ${totalItems}. Сторінка ${currentPage} з ${totalPages}.
        </p>

        <c:forEach var="apartment" items="${results}">
            <div class="card apartment-card">
                <h3><c:out value="${apartment.title}"/></h3>
                <p><strong>Місто:</strong> <c:out value="${apartment.city}"/>, <c:out value="${apartment.district}"/></p>
                <p><strong>Параметри:</strong> ${apartment.rooms} кімн., ${apartment.area} м², ${apartment.floor} поверх</p>
                <p><strong>Ціна:</strong> ${apartment.pricePerMonth} грн/міс</p>
                <p><strong>Опис:</strong> <c:out value="${apartment.description}"/></p>
                <p><strong>Контакт:</strong> <c:out value="${apartment.ownerName}"/>, <c:out value="${apartment.phone}"/></p>

                <c:if test="${apartment.furnished}">
                    <p class="success">Квартира мебльована</p>
                </c:if>

                <c:choose>
                    <c:when test="${apartment.petsAllowed}">
                        <p>Можна з тваринами</p>
                    </c:when>
                    <c:otherwise>
                        <p>Тварини не дозволені</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>

        <c:if test="${totalPages > 1}">
            <div class="pagination-wrap">
                <div class="pagination">

                    <!-- Стрілка назад -->
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <c:url var="prevPageUrl" value="/client/search">
                                <c:param name="city" value="${param.city}" />
                                <c:param name="minRooms" value="${param.minRooms}" />
                                <c:param name="maxPrice" value="${param.maxPrice}" />
                                <c:param name="furnished" value="${param.furnished}" />
                                <c:param name="petsAllowed" value="${param.petsAllowed}" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="page" value="${currentPage - 1}" />
                            </c:url>
                            <a class="page-arrow" href="${prevPageUrl}">&#8592;</a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-arrow disabled">&#8592;</span>
                        </c:otherwise>
                    </c:choose>

                    <!-- Номери сторінок -->
                    <div class="page-numbers">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="page-number active">${i}</span>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="pageUrl" value="/client/search">
                                        <c:param name="city" value="${param.city}" />
                                        <c:param name="minRooms" value="${param.minRooms}" />
                                        <c:param name="maxPrice" value="${param.maxPrice}" />
                                        <c:param name="furnished" value="${param.furnished}" />
                                        <c:param name="petsAllowed" value="${param.petsAllowed}" />
                                        <c:param name="size" value="${pageSize}" />
                                        <c:param name="page" value="${i}" />
                                    </c:url>
                                    <a class="page-number" href="${pageUrl}">${i}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </div>

                    <!-- Стрілка вперед -->
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/client/search">
                                <c:param name="city" value="${param.city}" />
                                <c:param name="minRooms" value="${param.minRooms}" />
                                <c:param name="maxPrice" value="${param.maxPrice}" />
                                <c:param name="furnished" value="${param.furnished}" />
                                <c:param name="petsAllowed" value="${param.petsAllowed}" />
                                <c:param name="size" value="${pageSize}" />
                                <c:param name="page" value="${currentPage + 1}" />
                            </c:url>
                            <a class="page-arrow" href="${nextPageUrl}">&#8594;</a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-arrow disabled">&#8594;</span>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </c:if>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
