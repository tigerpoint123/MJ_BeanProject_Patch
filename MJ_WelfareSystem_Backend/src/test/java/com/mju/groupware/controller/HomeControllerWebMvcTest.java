package com.mju.groupware.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

import com.mju.groupware.constant.ConstantHomeController;
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
class HomeControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConstantHomeController constantHomeController;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "testInternalResourceViewResolver")
        ViewResolver testViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/WEB-INF/views/");
            resolver.setSuffix(".jsp");
            return resolver;
        }
    }

    @Test
    @DisplayName("GET /login 는 200을 반환한다")
    void loginReturnsOk() throws Exception {
        given(constantHomeController.getLogin()).willReturn("/signin/login");
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /signupSelect 는 200을 반환한다")
    void signupSelectReturnsOk() throws Exception {
        given(constantHomeController.getSelect()).willReturn("/signup/signupSelect");
        mockMvc.perform(get("/signupSelect"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /infoConsent 는 200을 반환한다")
    void infoConsentReturnsOk() throws Exception {
        given(constantHomeController.getConsent()).willReturn("/signup/infoConsent");
        mockMvc.perform(get("/infoConsent"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /access_denied 는 200을 반환한다")
    void accessDeniedReturnsOk() throws Exception {
        given(constantHomeController.getDenied()).willReturn("/homeView/accessDenied");
        mockMvc.perform(get("/access_denied"))
                .andExpect(status().isOk());
    }
}


