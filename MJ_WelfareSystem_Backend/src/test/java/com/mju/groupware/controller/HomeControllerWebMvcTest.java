package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantHomeController;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class))
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
class HomeControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConstantHomeController constantHomeController;
    @MockBean
    private UserService userService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private ProfessorService professorService;

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


