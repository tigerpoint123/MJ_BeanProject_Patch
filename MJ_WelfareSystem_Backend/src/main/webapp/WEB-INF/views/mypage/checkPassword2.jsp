<!-- 회원 탈퇴 버튼 클릭 시 비밀번호 확인하는 화면 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/css/infoModify.css" var="infoModifyCss" />
<spring:url value="/resources/js/jquery-3.5.1.min.js" var="jqueryJs" />
<spring:url value="/resources/js/infoModify.js" var="infoModifyJs" />
<link rel="stylesheet" href="${infoModifyCss}">
<script src="${jqueryJs}"></script>
<script src="${infoModifyJs}"></script>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
   href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap"
   rel="stylesheet">
<link rel="stylesheet"
   href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
<%--비밀번호 감추기 아이콘 링크 --%>
<spring:url value="/resources/css/pwShowHide.css" var="pwCss" />
<link rel="stylesheet" href="${pwCss}">
<%--비밀번호 감추기 아이콘 css --%>
<spring:url value="/resources/js/pwShowHide.js" var="pwJs" />
<script src="${pwJs}"></script>
<%--비밀번호 감추기 js--%>
<title>check password</title>
</head>
<body>
   <div class="mjsWs">
      <div id="content">
         <section id="checkPwd">
            <h2>비밀번호 확인</h2>
            <br>
            <div id="showPwd">
               <spring:url value="/checkPassword2.do" var="checkPasswordAction" />
               <form action="${checkPasswordAction}" name="CheckPwd"
                  method="POST" id="form">
                  <input type="hidden" name="${_csrf.parameterName}"
                     value="${_csrf.token}" />
                  <table>
                     <tr>
                        <td class="col1"><label for="userPW">비밀번호 입력</label></td>
                        <td class="col2"><input type="password" class="inputBox"
                           id="userLoginPwd" name="UserLoginPwd" value=${UserLoginPwd}></input>
                           <i class="fa fa-eye fa-lg" id="icon"></i></td>
                     </tr>
                  </table>
                  <div>
                     <input type="submit" id="modifyBtn" value="확인"> <input
                        type="button" name="Cancel" id="cancelBtn" value="취소">
                  </div>

               </form>
            </div>
         </section>
      </div>
   </div>
</body>
</html>