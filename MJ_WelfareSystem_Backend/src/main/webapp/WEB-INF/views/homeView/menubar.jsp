<!-- 페이지 상단 메뉴바 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link rel="preconnect" href="https://fonts.gstatic.com">
<link
	href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap"
	rel="stylesheet">
<spring:url value="/resources/css/homeLayout.css" var="homeLayoutCss" />
<spring:url value="/resources/css/menubar.css" var="menubarCss" />
<link rel="stylesheet" href="${homeLayoutCss}" type="text/css">
<link rel="stylesheet" href="${menubarCss}" type="text/css">
</head>
<body>
	<div class="mheader">
		<!--메뉴바 -->
		<div class="menubar">
			<div class="menubarWidth">
				<div class="menubarMid">
					
					<nav id="navigation1">
						<div class="menubarLogo">
                            <spring:url value="/resources/image/logo.png" var="logoP" />
                            <spring:url value="/resources/image/title.png" var="titleP" />
                            <img id="logo1" src="${logoP}">
                            <img id="title1" src="${titleP}">
                            <img id="logo2" src="${logoP}">
                            <img id="title2" src="${titleP}">
						</div>
						<ul id="topInfo">
							<!-- sign in -->
							<li><sec:authorize access="isAnonymous()">
                                    <spring:url value="/login" var="loginUrl" />
                                    <a href="${loginUrl}">로그인</a>
								</sec:authorize>
                                <spring:url value="/login" var="loginAction" />
                                <form id="signUp" action="${loginAction}" method="POST">
									<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}" />
								</form></li>
							<!-- sign out -->
							<li><sec:authorize access="isAuthenticated()">
									<a href="#" onclick="document.getElementById('logout').submit();">로그아웃</a>
								</sec:authorize>
                            <spring:url value="/logout.do" var="logoutUrl" />
                            <form id="logout" action="${logoutUrl}" method="POST">
									<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}" /></form></li>
                            <spring:url value="/inquiryList" var="inquiryUrl" />
                            <spring:url value="/myPage" var="myPageBaseUrl" />
                            <spring:url value="/admin/manageList" var="adminManageUrl" />
                            <li><a href="${inquiryUrl}">문의</a></li>
                            <li><sec:authorize access="isAuthenticated()"><a href="${myPageBaseUrl}?R=${UserRole}">마이페이지</a></sec:authorize></li>
                            <li><sec:authorize access="hasRole('ROLE_ADMIN')"><a href="${adminManageUrl}">관리자 메뉴</a></sec:authorize></li>
						</ul>
					</nav>
					<!-- 메뉴 -->
					<nav id="navigation2">
						<ul id="topMenu">
                            <spring:url value="/home" var="homeUrl" />
                            <spring:url value="/email/emailLogin" var="emailLoginUrl" />
                            <spring:url value="/noticeList" var="noticeListUrl" />
                            <spring:url value="/communityList" var="communityListUrl" />
                            <spring:url value="/lectureRoom/lectureRoomList" var="lectureListUrl" />
                            <spring:url value="/lectureRoom/reservationConfirm" var="reservationConfirmUrl" />
                            <spring:url value="/schedule/mySchedule" var="myScheduleUrl" />
                            <spring:url value="/search/searchUser" var="searchUserUrl" />
                            <spring:url value="/team/teamList" var="teamListUrl" />
                            <spring:url value="/team/searchLecture" var="searchLectureUrl" />
                            <spring:url value="/team/searchMyTeam" var="searchMyTeamUrl" />
                            <spring:url value="/team/myTeamList" var="myTeamListUrl" />
                            <li><a href="${homeUrl}">홈</a></li>
                            <li><a href="${emailLoginUrl}">메일</a></li>
                            <li><a href="${noticeListUrl}">게시판</a>
								<ul id="subMenu">
                                    <li><a href="${noticeListUrl}">공지사항</a></li>
                                    <li><a href="${communityListUrl}">커뮤니티</a></li>
								</ul></li>
                            <li><a href="${lectureListUrl}">강의실</a><ul id="subMenu">
                                    <li><a href="${lectureListUrl}">강의실 예약</a></li>
                                    <li><a href="${reservationConfirmUrl}">예약확인</a></li>
								</ul></li>
                            <li><a href="${myScheduleUrl}">일정</a></li>
                            <li><a href="${searchUserUrl}">사용자 검색</a></li>
                            <li><a href="${teamListUrl}">&nbsp;&nbsp; 팀 &nbsp;&nbsp;</a><ul id="subMenu">
                                    <li><a href="${searchLectureUrl}">팀 생성</a></li>
                                    <li><a href="${teamListUrl}">팀 조회</a></li>
                                    <li><a href="${searchMyTeamUrl}">후기 작성</a></li>
								</ul></li>
                                <li><a href="${myTeamListUrl}">문서</a></li>
						</ul>
					</nav>
				</div>
				<!-- menubar_mid -->
			</div>
			<!-- menubar_width -->
		</div>
		<!-- menubar -->
	</div><!-- mheader -->

</body>
</html>