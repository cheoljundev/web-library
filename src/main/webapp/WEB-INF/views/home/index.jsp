<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>온라인 도서관</title>
    <script defer src="/js/rent.js"></script>
</head>
<body>
<%-- user가 없을 경우에 폼 보여줌 --%>
<c:if test="${sessionScope.user == null}">
    <form method="post" action="/site/login">
        <legend>로그인</legend>
        <input type="text" name="username"/>
        <input type="password" name="password"/>
        <button type="submit">로그인</button>
    </form>
    <a href="/site/join">회원가입</a>
</c:if>
<c:if test="${sessionScope.user != null}">
    <p>안녕하세요, ${sessionScope.user.username}님!</p>
</c:if>

<c:if test="${books != null}">
    <ul>
        <c:forEach var="book" items="${books}">
            <li>${book.name} ${book.isbn} <button value="${book.id}" onclick="rent(this.value)">대출</button> <button value="${book.id}" onclick="unRent(this.value)">반납</button></li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>