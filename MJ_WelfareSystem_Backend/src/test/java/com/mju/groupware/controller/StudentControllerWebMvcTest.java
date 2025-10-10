package com.mju.groupware.controller;

import global.properties.StudentProperties;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.student.params.user-name=UserName",
        "app.student.params.user-login-id=UserLoginID",
        "app.student.params.user-phone-num=UserPhoneNum",
        "app.student.params.user-phone=UserPhone",
        "app.student.params.user-email=UserEmail",
        "app.student.params.user-major=UserMajor",
        "app.student.params.user-grade=UserGrade",
        "app.student.params.grade=Grade",
        "app.student.params.email=Email",
        "app.student.urls.signup=/signup/signupStudent",
        "app.student.urls.mypage=/mypage/myPageStudent",
        "app.student.urls.modify=/mypage/modifyStudent"
})
@Import({TestMvcSharedConfig.class, StudentProperties.class})
class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @Autowired private StudentProperties studentProps;

    @BeforeEach
    void setupCommon() {
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "STUDENT")));
        
        // StudentProperties는 @TestPropertySource를 통해 자동으로 설정됨
    }

    @Test
    @DisplayName("GET /signupStudent returns 200")
    void signupStudentReturnsOk() throws Exception {
        mockMvc.perform(get("/signupStudent"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myPageStudent returns 200")
    void myPageStudentReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(studentService.selectStudentProfileInfo("UID123"))
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
        given(userService.selectMyPageUserInfo("testUser")).willReturn(myPage);
        given(userService.selectOpenInfo("testUser")).willReturn("OPEN");

        mockMvc.perform(get("/myPageStudent").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /modifyStudent returns 200")
    void modifyStudentReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        User user = new User();
        user.setUserID(1);
        user.setUserLoginID("testUser");
        user.setUserName("Name");
        user.setUserEmail("email@example.com");
        given(userService.selectModifyUserInfo("testUser")).willReturn(user);

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
    @DisplayName("POST /modifyStudent.do returns 200")
    void modifyStudentPostReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.selectUserInformation("testUser"))
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

