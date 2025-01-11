<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>온라인 도서관 - 관리자 페이지</title>
    <script type="module" src="/js/admin.js"></script>
</head>
<body>
<h1>관리자 페이지</h1>
<p>안녕하세요, 관리자 ${sessionScope.user.username}님!</p>

<h2>회원 관리</h2>
<c:if test="${users != null}">
    <ul>
        <c:forEach var="user" items="${users}">
            <li>
                ${user.username}
                <select id="roleType">
                    <option value="default">일반 유저</option>
                    <option value="admin">관리자</option>
                </select>
                <button value="${user.id}" onclick="setRole(this)">권한 변경</button>
                <button value="${user.id}" onclick="deleteUser(this.value)">삭제</button>
            </li>
        </c:forEach>
    </ul>
</c:if>

<h2>도서 관리</h2>

<form action="/site/books/add" method="post">
    <input type="text" name="bookName">
    <input type="text" name="isbn">
    <button type="submit">등록</button>
</form>
<c:if test="${books != null}">
    <c:forEach var="book" items="${books}">
        <ul>
            <li>
                <span>책 이름 : ${book.name}</span>
                <span>isbn : ${book.isbn}</span>
                <button value="${book.id}">수정</button>
                <button value="${book.id}">삭제</button>
            </li>
        </ul>
    </c:forEach>
</c:if>
</body>
</html>