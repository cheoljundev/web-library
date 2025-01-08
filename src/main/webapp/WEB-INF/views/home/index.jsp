<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>온라인 도서관</title>
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
</body>
</html>