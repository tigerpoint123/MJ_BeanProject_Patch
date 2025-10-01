<!-- 이메일 로그인 화면 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title>Please sign in</title>
<link
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M"
	crossorigin="anonymous">
<spring:url value="/resources/css/login.css" var="loginCss" />
<spring:url value="/resources/js/jquery-3.5.1.min.js" var="jqueryJs" />
<spring:url value="/resources/js/login.js" var="loginJs" />
<link rel="stylesheet" href="${loginCss}">
<script src="${jqueryJs}"></script>
<script src="${loginJs}"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
<%--비밀번호 감추기 아이콘 링크 --%>
<spring:url value="/resources/css/pwShowHide.css" var="pwCss" />
<link rel="stylesheet" href="${pwCss}">
<%--비밀번호 감추기 아이콘 css --%>
<spring:url value="/resources/js/pwShowHide.js" var="pwJs" />
<script src="${pwJs}"></script>
<%--비밀번호 감추기 js--%>
</head>
<body>

<script>
      $(document).ready(function() {
         let result = '<c:out value="${Checker}"/>';
         console.log(result);
         checkAlert(result);

         function checkAlert(result) {
            if (result === '') {
               return;
            }
            if (result === "LoginFail") {
               alert("아이디 또는 비밀번호를 체크해주세요");
            }
         }
      });
   </script>

	<div class="mjsWs">
		<div id="content">
			<section id="login">
				<h2>이메일 로그인</h2>
				<br>
                <spring:url value="/email/emailList" var="emailListAction" />
                <form action="${emailListAction}" name="emailLogin" method="POST" id="form">
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /></input>
					<div id="content">
						<table>
							<tr>
								<td class="col1"><input type="text" class="inputBox"
									name="EmailLoginID" id="eamilLoginID" autofocus
									autocomplete="off" value="${EmailLoginID}" placeholder="ID"></td>
								<td class="col1">
									 <input type="text" class="inputBox" value="@mju.ac.kr" disabled readonly>
								</td>
							</tr>
							<tr>
								<td class="col1" colspan="2"><input type="password" class="inputBox"
									name="EmailLoginPwd" id="emailLoginPW" autocomplete="off"
									value="${EmailLoginPwd}" placeholder="Password"> <i
									class="fa fa-eye fa-lg" id="icon"></i></td>
							</tr>
							<tr>
							</tr>
						</table>
					</div>

					<div id="content">
						<input type="submit" name="SubmitName" id="submitId" value="로그인">
					</div>
				</form>
			</section>
		</div>
	</div>
</body>
</html>