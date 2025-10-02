package com.mju.groupware.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Service dependencies
import com.mju.groupware.service.AdminService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.util.UserInfoMethod;
import com.mju.groupware.constant.ConstantAdmin;

@WebMvcTest(controllers = AdministratorController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
class AdministratorControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private UserService userService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private UserInfoMethod userInfoMethod;

    @MockBean
    private ConstantAdmin constantAdmin;

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
    void setupConstants() {
        // 기본 뷰 이름/리다이렉트 경로 스텁
        given(constantAdmin.getList()).willReturn("admin/list");
        given(constantAdmin.getSleepList()).willReturn("admin/sleepList");
        given(constantAdmin.getSecessionList()).willReturn("admin/secessionList");
        given(constantAdmin.getSManage()).willReturn("admin/manageStudent");
        given(constantAdmin.getPManage()).willReturn("admin/manageProfessor");
        given(constantAdmin.getSManageModify()).willReturn("admin/manageModifyStudent");
        given(constantAdmin.getPManageModify()).willReturn("admin/manageModifyProfessor");
        given(constantAdmin.getReList()).willReturn("redirect:/admin/manageList");

        // 역할 비교에 사용
        given(constantAdmin.getSTUDENT()).willReturn("STUDENT");
        given(constantAdmin.getPROFESSOR()).willReturn("PROFESSOR");
        given(constantAdmin.getADMINISTRATOR()).willReturn("ADMINISTRATOR");

        // 속성 키 등 자주 쓰는 상수
        given(constantAdmin.getEmail()).willReturn("Email");
    }

    @Test
    @DisplayName("GET /admin/manageList 는 200을 반환한다")
    void manageListReturnsOk() throws Exception {
        // given
        Principal principal = () -> "testUser";
        given(userService.SelectUserProfileInfo("testUser")).willReturn(
                new ArrayList<>(Arrays.asList("name", "id", "UNKNOWN_ROLE"))
        );
        given(adminService.SelectUserlist()).willReturn(Collections.emptyList());

        // when/then
        mockMvc.perform(get("/admin/manageList").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageSleep 는 200을 반환한다")
    void manageSleepReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.SelectUserProfileInfo("testUser")).willReturn(
                new ArrayList<>(Arrays.asList("name", "someId", constantRoleUnknown()))
        );
        given(adminService.SelectDormantUserList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/manageSleep").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageSecession 는 200을 반환한다")
    void manageSecessionReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.SelectUserProfileInfo("testUser")).willReturn(
                new ArrayList<>(Arrays.asList("name", "someId", constantRoleUnknown()))
        );
        given(adminService.SelectWithdrawalUserList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/manageSecession").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageStudent 는 200을 반환한다")
    void manageStudentReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/manageStudent"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageProfessor 는 200을 반환한다")
    void manageProfessorReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/manageProfessor"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageModifyStudent 는 200을 반환한다")
    void manageModifyStudentReturnsOk() throws Exception {
        com.mju.groupware.dto.User user = new com.mju.groupware.dto.User();
        user.setUserEmail("student@example.com");
        given(userService.SelectModifyUserInfo("U001")).willReturn(user);

        mockMvc.perform(get("/admin/manageModifyStudent").param("no", "U001"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/manageModifyProfessor 는 200을 반환한다")
    void manageModifyProfessorReturnsOk() throws Exception {
        com.mju.groupware.dto.User user = new com.mju.groupware.dto.User();
        user.setUserEmail("test@example.com");
        given(userService.SelectModifyUserInfo("P001")).willReturn(user);

        mockMvc.perform(get("/admin/manageModifyProfessor").param("no", "P001"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/detail 는 3xx 리다이렉트를 반환할 수 있다")
    void detailReturnsView() throws Exception {
        // 최소 happy path: 학생 역할과 ROLE_USER 조합으로 분기 타지 않고 목록으로 돌려보내는 경우 등 다양함.
        mockMvc.perform(get("/admin/detail")
                        .param("no", "1")
                        .param("R", "SROLE")
                        .param("A", "ROLE_USER"))
                .andExpect(status().is3xxRedirection());
    }

    // 내부 유틸: 알 수 없는 역할 문자열(컨트롤러 내부 분기 안 타도록)
    private String constantRoleUnknown() { return "UNKNOWN_ROLE"; }
}


