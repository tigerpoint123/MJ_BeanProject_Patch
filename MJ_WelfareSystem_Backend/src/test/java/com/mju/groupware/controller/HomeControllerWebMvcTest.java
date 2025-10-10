package com.mju.groupware.controller;

import global.properties.HomeProperties;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.home.urls.home=/homeView/home",
        "app.home.urls.select=/signup/signupSelect",
        "app.home.urls.consent=/signup/infoConsent",
        "app.home.urls.login=/signin/login",
        "app.home.urls.admin-login=/signin/mjuAdminLogin",
        "app.home.urls.denied=/homeView/accessDenied"
})
@Import({TestMvcSharedConfig.class, HomeProperties.class})
class HomeControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HomeProperties homeProps;
    @MockBean
    private UserService userService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private ProfessorService professorService;

    @Test
    @DisplayName("GET /login returns 200")
    void loginReturnsOk() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /signupSelect returns 200")
    void signupSelectReturnsOk() throws Exception {
        mockMvc.perform(get("/signupSelect"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /infoConsent returns 200")
    void infoConsentReturnsOk() throws Exception {
        mockMvc.perform(get("/infoConsent"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /access_denied returns 200")
    void accessDeniedReturnsOk() throws Exception {
        mockMvc.perform(get("/access_denied"))
                .andExpect(status().isOk());
    }
}


