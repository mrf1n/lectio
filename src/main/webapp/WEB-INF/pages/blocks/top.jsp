<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html;charset=utf-8" %>
<spring:url value="/resources" var="theme"/>
<link rel="stylesheet" type="text/css" href="${theme}/css/top.css"/>
<link href="https://fonts.googleapis.com/css2?family=Montserrat&display=swap" rel="stylesheet">

<div class="top">
    <div class="logo">
        <img class="logo_img" src="${theme}/img/logo_transparent.png" alt=""/>
        <span class="logo_name">ectio</span>
    </div>
    <div class="buttons top_buttons">
        <a class="button top_button" href="#"><spring:message code="top.teach"/></a>
        <a class="button top_button" href="#"><spring:message code="top.study"/></a>
    </div>
</div>

