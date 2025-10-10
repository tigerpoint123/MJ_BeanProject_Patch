package com.mju.groupware.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import global.properties.HomeProperties;

@Controller
@RequiredArgsConstructor
public class HomeController {
    // TODO : 컨트롤러 분리(userFunction). 트랜잭션 관리 추가. 통일된 예외처리. 상수 관리 개선. 유틸 클래스 정리 (중복 코드 제거, 공통 검증로직 추출). 테스트 코드 보강
    private final HomeProperties homeProps;

	@GetMapping("/signupSelect")
	public String signupSelect() {
		return homeProps.getUrls().getSelect();
	}

    // 정보동의화면
    @GetMapping("/infoConsent")
    public String infoConsent() {
        return homeProps.getUrls().getConsent();
    }

    // 사용자 로그인 화면
    @GetMapping("/login")
    public String loginGet() {
        return homeProps.getUrls().getLogin();
    }

    @PostMapping("/login")
    public String loginPost() {
        return homeProps.getUrls().getLogin();
    }

    // 403 에러
    @GetMapping("/access_denied")
    public String accessDeniedPage() throws Exception {
        return homeProps.getUrls().getDenied();
    }

}