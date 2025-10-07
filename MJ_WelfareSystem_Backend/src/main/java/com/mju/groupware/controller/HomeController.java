package com.mju.groupware.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.mju.groupware.constant.ConstantHomeController;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ConstantHomeController Constant;

	@GetMapping("/signupSelect")
	public String signupSelect() {
		return this.Constant.getSelect();
	}

    // 정보동의화면
    @GetMapping("/infoConsent")
    public String infoConsent() {
        return this.Constant.getConsent();
    }

    // 사용자 로그인 화면
    @GetMapping("/login")
    public String loginGet() {
        return this.Constant.getLogin();
    }

    @PostMapping("/login")
    public String loginPost() {
        return this.Constant.getLogin();
    }

    // 403 에러
    @GetMapping("/access_denied")
    public String accessDeniedPage() throws Exception {
        return this.Constant.getDenied();
    }

}