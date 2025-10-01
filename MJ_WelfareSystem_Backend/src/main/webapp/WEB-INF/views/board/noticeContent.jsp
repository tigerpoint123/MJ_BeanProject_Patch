<!-- 공지사항 내용 출력 화면 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap"
	rel="stylesheet">
    <spring:url value="/resources/css/boardContent.css" var="boardContentCss" />
    <spring:url value="/resources/css/boardImageOption2" var="boardImageCss" />
<spring:url value="/resources/css/menubar.css" var="menubarCss" />
<spring:url value="/resources/js/jquery-3.5.1.min.js" var="jqueryJs" />
<spring:url value="/resources/js/boardContent.js" var="boardContentJs" />
    <link rel="stylesheet" href="${boardContentCss}" type="text/css">
    <link rel="stylesheet" href="${boardImageCss}" type="text/css">
<link rel="stylesheet" href="${menubarCss}" type="text/css">
<script src="${jqueryJs}"></script>
<script src="${boardContentJs}"></script>


<title>notice content</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/homeView/menubar.jsp"></jsp:include>
		<nav>
			<div class="mbody">
				<div class="mcontWidth">
				<sec:authorize access="isAnonymous()">
					<jsp:include page="/WEB-INF/views/homeView/loginBox.jsp"></jsp:include>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<jsp:include page="/WEB-INF/views/homeView/userInfoBox.jsp"></jsp:include>
				</sec:authorize>
					<div class="rightBox">
						<section>
							<div class="section">
								<br>
								<h2>공지사항</h2>
								<hr>
							</div>
						</section>
						<section>
                        <spring:url value="/NoticeDelete.do" var="noticeDeleteAction" />
                        <form action="${noticeDeleteAction}?boardID=${BoardID}" 
						name="NoticeDelete" method="POST" id="form">
							<div class="section2">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
								<table id="contentTable">
									<tr id="title">
										<td><input type="text" name="NoticeTitle"
											id="noticeTitle" class="inputBox" placeholder="제목"
											disabled readonly value="${NoticeTitle}"></td>
									</tr>
									<tr id="receive">
										<td colspan="2"><input type="text" name="NoticeWriter"
											id="noticeWriter" class="inputBox" placeholder="작성자"
											disabled readonly value="${NoticeWriter}"> <input
											type="text" name="Date" id="date" class="inputBox"
											placeholder="날짜" disabled readonly value="${BoardDate}">
										</td>
									</tr>
									<tr id="content">
										<td><textarea name="NoticeContent"
											id="noticeContent"  class="inputBox"  placeholder="${NoticeContent}" disabled readonly></textarea>
							 			<c:out value="" escapeXml="false" /></td>
									</tr>
								</table>
								<hr>
								<table>
								<tr>
										<td><label for="attachment">첨부파일 :</label><br></td>

										<td><c:forEach var="NoticeFile"
												items="${NoticeFile}">
												<a href="#"
													onclick="FileDown('${NoticeFile.BFileID}'); return false;">${NoticeFile.BOriginalFileName}</a>(${NoticeFile.BFileSize}kb)
										</c:forEach></td>
									</tr>
								</table>
							</div>
							<div id="btn">
                                <spring:url value="/noticeList" var="noticeListUrl" />
                                <a href="${noticeListUrl}"><input type="button" value="목록"
									id="listButton"></a>
								<!-- 접속한 UserID와 해당 글을 작성한 UserID가 같을 때 수정/삭제 버튼 보이게 하기 -->
								<c:set var="UserID" value="${UserID}" />
								<c:set var="UserIDFromWriter" value="${UserIDFromWriter}" />
								<c:if test="${UserID == UserIDFromWriter}">
                                    <spring:url value="/noticeModify" var="noticeModifyUrl" />
                                    <a href="${noticeModifyUrl}?boardID=${BoardID}"><button
											type="button" id="modifyButton">수정</button></a>
									<button type="submit" id="deleteButton" onclick="if(!confirm('삭제 하시겠습니까?')){return false;}">삭제</button>
								</c:if>
							</div>
						</form>
					</section>
						<form name="readForm" role="form" method="post">
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /> <input type="hidden" id="bFileID"
							name="BFileID" value="">
					</form>
				</div>
					<!-- right_box -->
				</div>
				<!-- mcont_width -->
			</div>
			<!-- mbody -->
		</nav>
</body>
</html>