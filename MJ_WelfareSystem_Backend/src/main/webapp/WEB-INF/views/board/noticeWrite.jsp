<!-- 공지사항 작성 화면 -->

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
<spring:url value="/resources/js/boardContent.js" var="boardContentJs" />
<spring:url value="/resources/js/jquery-3.5.1.min.js" var="jqueryJs" />
<link rel="stylesheet" href="${boardContentCss}" type="text/css">
<link rel="stylesheet" href="${boardImageCss}" type="text/css">
<link rel="stylesheet" href="${menubarCss}" type="text/css">
<script src="${boardContentJs}"></script>
<script src="${jqueryJs}"></script>

</style>
<title>notice write</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/homeView/menubar.jsp"></jsp:include>
	<nav>
		<div class="mbody">
			<div class="mcontWidth">

				<jsp:include page="/WEB-INF/views/homeView/userInfoBox.jsp"></jsp:include> 
				<div class="rightBox">
					<section>
						<div class="section">
							<br>
							<h2>글 작성하기</h2>
							<hr>
						</div>
					</section>
					<section>
                        <spring:url value="/noticeWrite" var="noticeWriteAction" />
                        <form action="${noticeWriteAction}?${_csrf.parameterName}=${_csrf.token}" 
						name="NoticeWrite" enctype="multipart/form-data" method="POST"
							id="form">
							<div class="section2">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
								<table id="contentTable">
									<tr>
										<td><label for="title">제목 &nbsp; &nbsp; </label></td>
										<td><input
											type="text" name="NoticeTitle" id="noticeTitle"
											class="inputBox" value=${NoticeTitle}></td>
									</tr>
									<tr>
										<td><label for="writer">작성자 &nbsp; </label></td>
										<td><input
											type="text" name="NoticeWriter" id="noticeWriter"
											class="inputBox" placeholder = "작성자는 자동으로 입력됩니다." value="${NoticeWriter}" disabled readonly>
											<input
											type="text" name="Date" id="date" class="inputBox"
											placeholder="날짜가 자동으로 입력됩니다." disabled readonly value=${BoardDate}></td>
									</tr>
									<tr>
										<td><label for="content">내용</label>
										<td><textarea name="NoticeContent" id="noticeContent"
												class="inputBox" placeholder="내용을 입력하세요"></textarea></td>
									</tr>
								</table>
								<hr>
								<table>
									<tr>
										<td><label for="attachment">첨부파일</label>
										<input type="button" id="fileAddButton" value="파일추가" onclick="FileNameAddFile()"></td>
									</tr>
									<tr><td><input type="file" name="BoardFile" id="boardFile"
												class="inputBox" placeholder="파일을 첨부하세요."></td>
									</tr>
								</table>
								<table  id="fileIndex">
									
								</table>
							</div>
							<!-- section2 -->
							<div id="btn">
                                <input type="submit" value="저장" id="saveButton"> <a
                                    <spring:url value="/noticeList" var="noticeListUrl" />
                                    href="${noticeListUrl}"><input type="button" value="이전"
									id="listButton"></a>
							</div>
						</form>
					</section>
				</div>
				<!-- right_box -->

			</div>
			<!-- mcont_width -->
		</div>
		<!-- mbody -->
	</nav>
</body>
</html>