package com.mju.groupware.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.constant.ConstantAdminStudentController;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ConstantAdminStudentController constant;

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

    @BeforeEach
    void setupCommon() {
        given(userService.SelectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "STUDENT")));
        given(constant.getRSignupStudent()).willReturn("student/signupStudent");
        given(constant.getGrade()).willReturn("Grade");
        given(constant.getUserLoginID()).willReturn("UserLoginID");
        given(constant.getUserName()).willReturn("UserName");
        given(constant.getUserPhoneNum()).willReturn("UserPhoneNum");
        given(constant.getUserEmail()).willReturn("UserEmail");
        given(constant.getRMyPageStudent()).willReturn("student/myPageStudent");
        given(constant.getEmail()).willReturn("Email");
        given(constant.getRModifyStudent()).willReturn("student/modifyStudent");
        given(constant.getUserPhone()).willReturn("UserPhone");
        given(constant.getUserGrade()).willReturn("UserGrade");
    }

    @Test
    @DisplayName("GET /signupStudent 는 200을 반환한다")
    void signupStudentReturnsOk() throws Exception {
        mockMvc.perform(get("/signupStudent"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myPageStudent 는 200을 반환한다")
    void myPageStudentReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(studentService.SelectStudentProfileInfo("UID123"))
                .willReturn(new ArrayList<>(Arrays.asList("COLL", "MAJOR", "GRADE")));
        ArrayList<String> myPage = new ArrayList<>(Arrays.asList(
                "loginId0", // 0
                "userName1", // 1
                "010-0000-0000", // 2
                "user@example.com", // 3
                "COLL4", // 4
                "MAJOR5", // 5
                "3", // 6 grade
                "DOUBLE7", // 7 double major
                "M", // 8 gender
                "..."
        ));
        given(userService.SelectMyPageUserInfo("testUser")).willReturn(myPage);
        given(userService.SelectOpenInfo("testUser")).willReturn("OPEN");

        mockMvc.perform(get("/myPageStudent").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /modifyStudent 는 200을 반환한다")
    void modifyStudentReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        User user = new User();
        user.setUserID(1);
        user.setUserLoginID("testUser");
        user.setUserName("Name");
        user.setUserEmail("email@example.com");
        given(userService.SelectModifyUserInfo("testUser")).willReturn(user);

        Student student = new Student();
        student.setStudentGender("M");
        student.setStudentColleges("COLL");
        student.setStudentMajor("MAJOR");
        student.setStudentDoubleMajor("DOUBLE");
        given(studentService.SelectModifyStudentInfo(1)).willReturn(student);

        mockMvc.perform(get("/modifyStudent").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /modifyStudent.do 는 200을 반환한다")
    void modifyStudentPostReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.SelectUserInformation("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("1", "testUser")));

        mockMvc.perform(post("/modifyStudent.do")
                        .principal(principal)
                        .param("UserPhoneNum", "010-0000-0000")
                        .param("StudentGrade", "3")
                        .param("UserPhone", "on")
                        .param("UserGrade", "on"))
                .andExpect(status().isOk());
    }
}

