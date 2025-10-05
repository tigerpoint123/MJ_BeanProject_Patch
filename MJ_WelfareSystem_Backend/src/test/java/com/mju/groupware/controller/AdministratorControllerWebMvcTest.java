package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.service.AdminService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
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
        given(constantAdmin.getReSDetail()).willReturn("redirect:detailStudent");
        given(constantAdmin.getRePDetail()).willReturn("redirect:detailProfessor");
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
        // given: AdminService.modifyStudentList가 호출될 때 모델에 속성들을 추가하도록 모킹
        willAnswer(invocation -> {
            Model model = invocation.getArgument(1);
            model.addAttribute("UserLoginID", "U001");
            model.addAttribute("Email", "student");
            model.addAttribute("OpenPhoneNum", "010-0000-0000");
            model.addAttribute("OpenGrade", "학년");
            return null;
        }).given(adminService).modifyStudentList(any(HttpServletRequest.class), any(Model.class));
        
        // when & then
        mockMvc.perform(get("/admin/manageModifyStudent").param("no", "U001"))
                .andExpect(status().isOk())
                .andExpect(view().name(constantAdmin.getSManageModify()))
                .andExpect(model().attributeExists("UserLoginID"))
                .andExpect(model().attributeExists("Email"))
                .andExpect(model().attributeExists("OpenPhoneNum"))
                .andExpect(model().attributeExists("OpenGrade"))
                .andExpect(model().attribute("UserLoginID", "U001"))
                .andExpect(model().attribute("Email", "student"))
                .andExpect(model().attribute("OpenPhoneNum", "010-0000-0000"))
                .andExpect(model().attribute("OpenGrade", "학년"));
    }

    @Test
    @DisplayName("GET /admin/manageModifyProfessor returns 200")
    void manageModifyProfessorReturnsOk() throws Exception {
        // given: AdminService.modifyProfessorList가 호출될 때 모델에 속성들을 추가하도록 모킹
        willAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.addAttribute("UserLoginID", "P001");
            model.addAttribute("Email", "test");
            model.addAttribute("OpenPhoneNum", "02-123-4567");
            return null;
        }).given(adminService).modifyProfessorList(any(Model.class), any(HttpServletRequest.class), any(String.class));

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
        // given: AdminService가 getDetailPage 호출 시 redirect 문자열을 반환하도록 설정
        given(adminService.getDetailPage(any(HttpServletRequest.class), any(RedirectAttributes.class)))
                .willReturn("redirect:/admin/manageList");
        
        // when & then
        mockMvc.perform(get("/admin/detail")
                        .param("no", "1")
                        .param("R", "UNKNOWN_ROLE")
                        .param("A", "ROLE_USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/manageList"));
    }

    @Test
    @DisplayName("GET /admin/detailStudent returns 200 and sDetail view")
    void detailStudentReturnsOk() throws Exception {
        // given: AdminService.detailStudent가 호출될 때 모델에 속성들을 추가하도록 모킹
        willAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.addAttribute("UserLoginID", "U001");
            model.addAttribute("SUserName", "홍길동");
            model.addAttribute("StudentGender", "남");
            model.addAttribute("UserPhoneNum", "010-0000-0000");
            model.addAttribute("StudentGrade", "3");
            model.addAttribute("StudentColleges", "공과대학");
            model.addAttribute("StudentMajor", "컴퓨터공학");
            model.addAttribute("StudentDoubleMajor", "수학");
            model.addAttribute("UserEmail", "student@example.com");
            model.addAttribute("StudentInfoOpen", "전화번호,학년");
            return true;
        }).given(adminService).detailStudent(any(Model.class), any(String.class));

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


