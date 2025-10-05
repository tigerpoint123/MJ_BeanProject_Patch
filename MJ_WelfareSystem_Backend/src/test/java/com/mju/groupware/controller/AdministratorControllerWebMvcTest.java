package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.service.AdminService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.util.UserInfoMethod;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdministratorController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class))
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
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

    @BeforeEach
    void setupConstants() {
        // 기본 뷰 이름/리다이렉트 경로 스텁
        // 뷰 이름과 상수 스텁
        given(constantAdmin.getList()).willReturn("admin/list");
        given(constantAdmin.getSleepList()).willReturn("admin/sleepList");
        given(constantAdmin.getSecessionList()).willReturn("admin/secessionList");
        given(constantAdmin.getSManage()).willReturn("admin/manageStudent");
        given(constantAdmin.getPManage()).willReturn("admin/manageProfessor");
        given(constantAdmin.getSManageModify()).willReturn("admin/manageModifyStudent");
        given(constantAdmin.getPManageModify()).willReturn("admin/manageModifyProfessor");
        given(constantAdmin.getReList()).willReturn("redirect:/admin/manageList");
        given(constantAdmin.getSDetail()).willReturn("admin/sDetail");
        given(constantAdmin.getEmail()).willReturn("Email");
        given(constantAdmin.getSTUDENT()).willReturn("STUDENT");
        given(constantAdmin.getPROFESSOR()).willReturn("PROFESSOR");
        given(constantAdmin.getADMINISTRATOR()).willReturn("ADMINISTRATOR");
    }

    @Test
    @DisplayName("GET /admin/manageList returns 200")
    void manageListReturnsOk() throws Exception {
        // given
        Principal principal = () -> "testUser";
        // advice에서 사용자 정보 메서드 호출은 mock으로 통과

        // when/then
        mockMvc.perform(get("/admin/manageList").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getList()))
                .andExpect(model().attributeExists("list"));
    }

    @Test
    @DisplayName("GET /admin/manageSleep returns 200")
    void manageSleepReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        // advice에서 사용자 정보 메서드 호출은 mock으로 통과

        mockMvc.perform(get("/admin/manageSleep").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getSleepList()))
                .andExpect(model().attributeExists("DormantList"));
    }

    @Test
    @DisplayName("GET /admin/manageSecession returns 200")
    void manageSecessionReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        // advice에서 사용자 정보 메서드 호출은 mock으로 통과

        mockMvc.perform(get("/admin/manageSecession").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getSecessionList()))
                .andExpect(model().attributeExists("SelectWithdrawalUserList"));
    }

    @Test
    @DisplayName("GET /admin/manageStudent returns 200")
    void manageStudentReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/manageStudent"))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getSManage()));
    }

    @Test
    @DisplayName("GET /admin/manageProfessor returns 200")
    void manageProfessorReturnsOk() throws Exception {
        mockMvc.perform(get("/admin/manageProfessor"))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getPManage()));
    }

    @Test
    @DisplayName("GET /admin/manageModifyStudent returns 200")
    void manageModifyStudentReturnsOk() throws Exception {
        // given
        com.mju.groupware.dto.User user = new com.mju.groupware.dto.User();
        user.setUserEmail("student@example.com");
        user.setOpenPhoneNum("010-0000-0000");
        user.setOpenGrade("학년");
        given(userService.SelectModifyUserInfo("U001")).willReturn(user);

        mockMvc.perform(get("/admin/manageModifyStudent").param("no", "U001"))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getSManageModify()))
                .andExpect(model().attributeExists("UserLoginID"))
                .andExpect(model().attributeExists("Email"))
                .andExpect(model().attributeExists("OpenPhoneNum"))
                .andExpect(model().attributeExists("OpenGrade"));
    }

    @Test
    @DisplayName("GET /admin/manageModifyProfessor returns 200")
    void manageModifyProfessorReturnsOk() throws Exception {
        // given
        com.mju.groupware.dto.User puser = new com.mju.groupware.dto.User();
        puser.setUserEmail("test@example.com");
        puser.setOpenPhoneNum("02-123-4567");
        given(userService.SelectModifyUserInfo("P001")).willReturn(puser);

        mockMvc.perform(get("/admin/manageModifyProfessor").param("no", "P001"))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getPManageModify()))
                .andExpect(model().attributeExists("UserLoginID"))
                .andExpect(model().attributeExists("Email"))
                .andExpect(model().attributeExists("OpenPhoneNum"));
    }

    @Test
    @DisplayName("GET /admin/detail may return 3xx redirect")
    void detailReturnsView() throws Exception {
        // 최소 happy path: 학생 역할과 ROLE_USER 조합으로 분기 타지 않고 목록으로 돌려보내는 경우 등 다양함.
        mockMvc.perform(get("/admin/detail")
                        .param("no", "1")
                        .param("R", "SROLE")
                        .param("A", "ROLE_USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(constantAdmin.getReList()));
    }

    // 내부 유틸: 알 수 없는 역할 문자열(컨트롤러 내부 분기 안 타도록)
    private String constantRoleUnknown() { return "UNKNOWN_ROLE"; }

    @Test
    @DisplayName("GET /admin/detailStudent returns 200 and sDetail view")
    void detailStudentReturnsOk() throws Exception {
        // given: 학생 프로필 정보 스텁 (필요 인덱스: 0,1,2,3,4,5,6,7,8,9,10)
        ArrayList<String> info = new ArrayList<>();
        info.add("U001");                     // 0: UserLoginID
        info.add("홍길동");                      // 1: SUserName
        info.add("010-0000-0000");            // 2: UserPhoneNum
        info.add("student@example.com");      // 3: UserEmail
        info.add("전화번호");                    // 4: 공개 항목1
        info.add("학년");                        // 5: 공개 항목2
        info.add("공과대학");                    // 6: StudentColleges
        info.add("컴퓨터공학");                  // 7: StudentMajor
        info.add("3");                         // 8: StudentGrade
        info.add("수학");                        // 9: StudentDoubleMajor
        info.add("남");                         // 10: StudentGender
        given(userService.SelectUserProfileInfoByID("U001")).willReturn(info);

        // when/then
        mockMvc.perform(get("/admin/detailStudent").param("no", "U001"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/sDetail"))
                .andExpect(model().attributeExists(
                        "UserLoginID",
                        "SUserName",
                        "StudentGender",
                        "UserPhoneNum",
                        "StudentGrade",
                        "StudentColleges",
                        "StudentMajor",
                        "StudentDoubleMajor",
                        "UserEmail",
                        "StudentInfoOpen"
                ));
    }
}


