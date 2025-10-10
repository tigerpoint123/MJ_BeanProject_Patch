package com.mju.groupware.controller;

import global.properties.ProfessorProperties;
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

@WebMvcTest(controllers = ProfessorController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.professor.params.user-name=UserName",
        "app.professor.params.user-login-id=UserLoginID",
        "app.professor.params.user-phone-num=UserPhoneNum",
        "app.professor.params.user-phone=UserPhone",
        "app.professor.params.user-email=UserEmail",
        "app.professor.params.email=Email",
        "app.professor.urls.signup=/signup/signupProfessor",
        "app.professor.urls.mypage=/mypage/myPageProfessor",
        "app.professor.urls.modify=/mypage/modifyProfessor"
})
@Import({TestMvcSharedConfig.class, ProfessorProperties.class})
class ProfessorControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private ProfessorService professorService;
    @MockBean private StudentService studentService;
    @Autowired private ProfessorProperties professorProps;

    @BeforeEach
    void setupCommon() {
        // 공통적으로 참조되는 사용자 프로필 스텁
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "PROFESSOR")));
    }

    @Test
    @DisplayName("GET /signupProfessor returns 200")
    void signupProfessorReturnsOk() throws Exception {
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
        given(userService.selectMyPageUserInfo("testUser")).willReturn(myPage);
        given(userService.selectOpenInfo("testUser")).willReturn("OPEN");

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
        given(userService.selectModifyUserInfo("testUser")).willReturn(user);

        Professor professor = new Professor();
        professor.setUserPhoneNum("010-0000-0000");
        professor.setProfessorColleges("COLL");
        professor.setProfessorMajor("MAJOR");
        given(professorService.selectModifyProfessorInfo(1)).willReturn(professor);

        mockMvc.perform(get("/modifyProfessor").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /modifyProfessor.do returns 200")
    void modifyProfessorPostReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.selectUserInformation("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("1", "testUser")));

        mockMvc.perform(post("/modifyProfessor.do")
                        .principal(principal)
                        .param("UserPhoneNum", "010-0000-0000")
                        .param("ProfessorRoom", "ROOM")
                        .param("ProfessorRoomNum", "02-0000-0000")
                        .param("UserPhone", "on"))
                .andExpect(status().isOk());
    }
}


