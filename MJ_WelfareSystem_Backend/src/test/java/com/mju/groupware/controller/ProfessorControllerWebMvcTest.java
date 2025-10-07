package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdminProfessorController;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfessorController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class))
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
class ProfessorControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private ProfessorService professorService;
    @MockBean private StudentService studentService;
    @MockBean private ConstantAdminProfessorController constant;

    @BeforeEach
    void setupCommon() {
        // 공통적으로 참조되는 사용자 프로필 스텁
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "PROFESSOR")));
    }

    @Test
    @DisplayName("GET /signupProfessor returns 200")
    void signupProfessorReturnsOk() throws Exception {
        given(constant.getRSignupProfessor()).willReturn("professor/signupProfessor");
        mockMvc.perform(get("/signupProfessor"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myPageProfessor returns 200")
    void myPageProfessorReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        // 교수 기본 정보
        given(professorService.selectProfessorProfileInfo("UID123"))
                .willReturn(new ArrayList<>(Arrays.asList("COLL", "MAJOR", "ROOM")));
        // 역할/마이페이지 표시용 정보
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "PROFESSOR")));
        // 마이페이지 상세 정보 (인덱스 매핑 주석 참조)
        ArrayList<String> myPage = new ArrayList<>(Arrays.asList(
                "loginId0", // 0: UserLoginID
                "userName1", // 1: UserName
                "010-0000-0000", // 2: Phone
                "user@example.com", // 3: Email
                "x4","x5","x6","x7","x8",
                "COLL9", // 9: ProfessorColleges
                "MAJOR10", // 10: ProfessorMajor
                "ROOM11", // 11: ProfessorRoom
                "02-000-0000" // 12: ProfessorRoomNum
        ));
        given(userService.SelectMyPageUserInfo("testUser")).willReturn(myPage);
        given(userService.SelectOpenInfo("testUser")).willReturn("OPEN");

        // 상수 키들
        given(constant.getUserLoginID()).willReturn("UserLoginID");
        given(constant.getUserName()).willReturn("UserName");
        given(constant.getUserPhoneNum()).willReturn("UserPhoneNum");
        given(constant.getUserEmail()).willReturn("UserEmail");
        given(constant.getRMyPageProfessor()).willReturn("professor/myPageProfessor");

        mockMvc.perform(get("/myPageProfessor").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /modifyProfessor returns 200")
    void modifyProfessorReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        User user = new User();
        user.setUserID(1);
        user.setUserLoginID("testUser");
        user.setUserName("Name");
        user.setUserEmail("email@example.com");
        given(userService.SelectModifyUserInfo("testUser")).willReturn(user);

        Professor professor = new Professor();
        professor.setUserPhoneNum("010-0000-0000");
        professor.setProfessorColleges("COLL");
        professor.setProfessorMajor("MAJOR");
        given(professorService.selectModifyProfessorInfo(1)).willReturn(professor);

        given(constant.getEmail()).willReturn("Email");
        given(constant.getUserPhoneNum()).willReturn("UserPhoneNum");
        given(constant.getRModifyProfessor()).willReturn("professor/modifyProfessor");

        mockMvc.perform(get("/modifyProfessor").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /modifyProfessor.do returns 200")
    void modifyProfessorPostReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.SelectUserInformation("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("1", "testUser")));

        given(constant.getUserPhoneNum()).willReturn("UserPhoneNum");
        given(constant.getUserPhone()).willReturn("UserPhone");
        given(constant.getRModifyProfessor()).willReturn("professor/modifyProfessor");

        mockMvc.perform(post("/modifyProfessor.do")
                        .principal(principal)
                        .param("UserPhoneNum", "010-0000-0000")
                        .param("ProfessorRoom", "ROOM")
                        .param("ProfessorRoomNum", "02-0000-0000")
                        .param("UserPhone", "on"))
                .andExpect(status().isOk());
    }
}


