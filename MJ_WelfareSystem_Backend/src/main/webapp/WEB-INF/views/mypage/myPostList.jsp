<!-- 내 게시글 리스트 출력 화면 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap"
	rel="stylesheet">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url value="/resources/css/myBoardList.css" var="myBoardListCss" />
<spring:url value="/resources/css/menubar.css" var="menubarCss" />
<link rel="stylesheet" href="${myBoardListCss}" type="text/css">
<link rel="stylesheet" href="${menubarCss}" type="text/css">
<title>my post list</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/homeView/menubar.jsp"></jsp:include>
	<nav>
		<div class="mbody">
			<div class="mcontWidth">
				<jsp:include page="/WEB-INF/views/homeView/userInfoBox.jsp"></jsp:include>

				<div class="rightBox">
					<div class="subMenuBox">
						<ul>
                            <spring:url value="/myPage" var="myPageBase" />
                            <spring:url value="/myPostList" var="myPostListUrl" />
                            <spring:url value="/confirmMyReservation" var="confirmResUrl" />
                            <spring:url value="/myInquiryList" var="myInquiryListUrl" />
                            <li><a href="${myPageBase}?R=${UserRole}">내 정보 확인</a></li>
                            <li><a href="${myPostListUrl}">내 게시글 조회</a></li>
                            <li><a href="${confirmResUrl}">강의실 예약 조회</a></li>
                            <li><a href="${myInquiryListUrl}">내 문의 조회</a></li>
						</ul>
					</div>
					<section id="boardlist">
						<div class="section">
							<br>
							<h2>내 게시글 목록</h2>
							<hr>
						</div>

						<div class="section2">
							<div id="search">
								<select name="SelectOption" id="selectOption">
									<option value="all">전체</option>
								</select> <input type="text" placeholder="검색어 입력하세요."> <input
									type="submit" value="검색">
							</div>

							<form action="" name="CommunityList" method="POST" id="form">
								<input type="hidden" name="${_csrf.parameterName}"
									value="${_csrf.token}" />
								<table id="communityList">
									<thead>
										<tr>
											<th class="col1">번호</th>
											<th class="col2">제목</th>
											<th class="col3">작성자</th>
											<th class="col4">작성일</th>
											<th class="col1">조회수</th>
										</tr>
										<hr>
									</thead>
									<tbody>
										<c:forEach items="${MyBoardList}" var="MyBoardList"
											varStatus="status">
											<tr>
												<td><c:out value="${status.count}" /></td>
												<td><c:if test="${MyBoardList.getBoardType() eq '커뮤니티' }">
                                                <spring:url value="/communityContent" var="communityContentBase" />
                                                <a href="${communityContentBase}?no=${MyBoardList.getBoardID()}">
													<c:out value="${MyBoardList.getBoardSubject()}" /> </a>
													</c:if> <c:if test="${MyBoardList.getBoardType() eq '공지사항' }">
                                                        <spring:url value="/noticeContent" var="noticeContentBase" />
                                                        <a href="${noticeContentBase}?no=${MyBoardList.getBoardID()}">
															<c:out value="${MyBoardList.getBoardSubject()}" />
														</a>
													</c:if></td>
												<td><c:out value="${MyBoardList.getBoardWriter()}" /></td>
												<td><c:out value="${MyBoardList.getBoardDate()}" /></td>
												<td><c:out value="${MyBoardList.getBoardHit()}" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<hr>
							</form>
						</div>
						<!-- section2 -->
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