package com.mju.groupware.controller;

import global.properties.UserFunctionProperties;
import com.mju.groupware.dto.*;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserFunctionController 테스트
 * 사용자 기능 관련 엔드포인트 테스트: 비밀번호 찾기, 홈, 마이페이지, 회원탈퇴, 이메일 등
 */
@WebMvcTest(controllers = UserFunctionController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "app.user.roles.student=STUDENT",
                "app.user.roles.professor=PROFESSOR",
                "app.user.roles.administrator=ADMINISTRATOR",
                "app.user.urls.withdrawal=/mypage/withdrawal",
                "app.user.urls.signup.student=/signup/signupStudent",
                "app.user.urls.signup.professor=/signup/signupProfessor",
                "app.user.urls.signup.select=/signup/signupSelect",
                "app.user.urls.signin.login=/signin/login",
                "app.user.urls.signin.find-password=/signin/findPassword",
                "app.user.urls.signin.show-password=/signin/showPassword",
                "app.user.urls.mypage.post-list=/mypage/myPostList",
                "app.user.urls.mypage.inquiry-list=/mypage/myInquiryList",
                "app.user.urls.mypage.check-password=/mypage/checkPassword",
                "app.user.urls.mypage.check-password2=/mypage/checkPassword2",
                "app.user.urls.mypage.check-password3=/mypage/checkPassword3",
                "app.user.urls.mypage.modify-password=/mypage/modifyPassword",
                "app.user.urls.mypage.check-my-team=/mypage/checkMyTeam",
                "app.user.urls.email.authentication=/signup/emailAuthentication",
                "app.user.urls.email.list=/email/emailList",
                "app.user.urls.email.content=/email/emailContent",
                "app.user.urls.email.login=/email/emailLogin",
                "app.user.urls.home=/homeView/home",
                "app.user.redirects.withdrawal=redirect:withdrawal",
                "app.user.redirects.modify-student=redirect:modifyStudent",
                "app.user.redirects.modify-professor=redirect:modifyProfessor",
                "app.user.redirects.modify-password=redirect:modifyPassword",
                "app.user.redirects.signup-student=/signin/login",
                "app.user.redirects.email-list=redirect:/email/emailList",
                "app.user.redirects.email-login=redirect:/email/emailLogin",
                "app.user.redirects.mypage-student=redirect:myPageStudent",
                "app.user.redirects.mypage-professor=redirect:myPageProfessor",
                "app.user.redirects.home=redirect:home",
                "app.user.formats.date=yyyy-MM-dd",
                "app.user.formats.datetime=yyyy-MM-dd HH:mm:ss",
                "app.user.email.domain=@mju.ac.kr",
                "app.user.params.withdrawal.parameter1=AgreeWithdrawal",
                "app.user.params.withdrawal.parameter2=yyyy-MM-dd",
                "app.user.params.auth-num=Number",
                "app.user.params.student-name=StudentName",
                "app.user.params.professor-name=ProfessorName",
                "app.user.params.user-name=UserName",
                "app.user.params.user-login-pwd=UserLoginPwd",
                "app.user.params.user-phone-num=UserPhoneNum",
                "app.user.params.student-num=StudentNum",
                "app.user.params.professor-num=ProfessorNum",
                "app.user.params.email-login-pwd=EmailLoginPwd",
                "app.user.params.email-check=EmailCheck",
                "app.user.params.email-valid=EmailValid",
                "app.user.params.btn-agree=BtnAgree",
                "app.user.params.email=Email",
                "app.user.params.user-new-pwd=UserNewPwd",
                "app.user.params.user-new-pwd-check=UserNewPwdCheck",
                "app.user.attributes.notice-list=noticeList",
                "app.user.attributes.community-list=communityList",
                "app.user.attributes.my-board-list=MyBoardList",
                "app.user.attributes.my-inquiry-list=MyInquiryList"
        }
)
@Import({TestMvcSharedConfig.class, UserFunctionProperties.class})
class UserFunctionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private EmailService emailService;
    @MockBean private UserEmailService userEmailService;
    @MockBean private UserInfoMethod userInfoMethod;
    
    @Autowired
    private UserFunctionProperties userProps;

    private Principal testPrincipal;

    @BeforeEach
    void setUp() {
        testPrincipal = () -> "testUser";

        // 공통 사용자 프로필 Mock
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("TestName", "UID123", "ROLE_STUDENT")));
    }

    @Test
    @DisplayName("GET /findPassword returns 200")
    void findPasswordReturnsOk() throws Exception {
        mockMvc.perform(get("/findPassword"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /showPassword returns 200")
    void showPasswordReturnsOk() throws Exception {
        mockMvc.perform(get("/showPassword"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /home returns 200 with principal")
    void homeReturnsOkWithPrincipal() throws Exception {
        given(studentService.selectStudentProfileInfo("UID123"))
                .willReturn(new ArrayList<>(Arrays.asList("College", "Major", "3")));
        given(userService.getNoticeBoardList()).willReturn(Collections.emptyList());
        given(userService.getCommunityBoardList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/home").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /home returns 200 without principal")
    void homeReturnsOkWithoutPrincipal() throws Exception {
        given(userService.getNoticeBoardList()).willReturn(Collections.emptyList());
        given(userService.getCommunityBoardList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET / returns 200")
    void rootHomeReturnsOk() throws Exception {
        given(userService.getNoticeBoardList()).willReturn(Collections.emptyList());
        given(userService.getCommunityBoardList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myPage redirects based on role")
    void myPageByRoleRedirects() throws Exception {
        mockMvc.perform(get("/myPage").param("R", "STUDENT"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /myPostList returns 200")
    void myPostListReturnsOk() throws Exception {
        given(userService.getMyBoardList("testUser")).willReturn(Collections.emptyList());

        mockMvc.perform(get("/myPostList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myInquiryList returns 200")
    void myInquiryListReturnsOk() throws Exception {
        given(userService.getMyInquiryList("testUser")).willReturn(Collections.emptyList());

        mockMvc.perform(get("/myInquiryList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /checkMyTeam returns 200")
    void checkMyTeamReturnsOk() throws Exception {
        mockMvc.perform(get("/checkMyTeam").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /checkPassword returns 200")
    void checkPasswordReturnsOk() throws Exception {
        mockMvc.perform(get("/checkPassword"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /modifyPassword returns 200")
    void modifyPasswordReturnsOk() throws Exception {
        mockMvc.perform(get("/modifyPassword"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /withdrawal returns 200")
    void withdrawalReturnsOk() throws Exception {
        mockMvc.perform(get("/withdrawal"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /withdrawal returns 200")
    void withdrawalPostReturnsOk() throws Exception {
        mockMvc.perform(post("/withdrawal")
                        .principal(testPrincipal)
                        .param("AgreeWithdrawal", "true"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /emailAuthentication returns 200")
    void emailAuthenticationReturnsOk() throws Exception {
        mockMvc.perform(get("/emailAuthentication"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /email.do returns 200")
    void emailDoPostReturnsOk() throws Exception {
        given(userEmailService.processEmailCertification(anyString(), anyString())).willReturn("success");

        mockMvc.perform(post("/email.do")
                        .param("Email", "test")
                        .param("EmailCheck", "true"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /checkPassword.do redirects when password correct")
    void checkPasswordPostRedirectsWhenCorrect() throws Exception {
        given(userService.selectForPwdCheckBeforeModify("testUser", "password123"))
                .willReturn(true);
        given(userService.getRedirectUrlByRole(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn("redirect:modifyStudent");

        mockMvc.perform(post("/checkPassword.do")
                        .principal(testPrincipal)
                        .param("UserLoginPwd", "password123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /modifyPassword.do returns 200")
    void modifyPasswordPostReturnsOk() throws Exception {
        mockMvc.perform(post("/modifyPassword.do")
                        .principal(testPrincipal)
                        .param("UserNewPwd", "newPassword123")
                        .param("UserNewPwdCheck", "newPassword123"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /email/emailLogin returns 200")
    void emailLoginReturnsOk() throws Exception {
        mockMvc.perform(get("/email/emailLogin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /email/emailList redirects when login succeeds")
    void emailListPostRedirectsWhenLoginSucceeds() throws Exception {
        given(emailService.CheckEmailLogin(anyString(), anyString())).willReturn(true);

        mockMvc.perform(post("/email/emailList")
                        .principal(testPrincipal)
                        .param("EmailLoginID", "test")
                        .param("EmailLoginPwd", "password"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /email/emailList returns 200")
    void emailListGetReturnsOk() throws Exception {
        given(emailService.PrintEmailList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/email/emailList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /email/emailContent returns 200")
    void emailContentReturnsOk() throws Exception {
        UserEmail mockEmail = new UserEmail();
        mockEmail.setFrom("test@mju.ac.kr");
        mockEmail.setTitle("Test Email");
        mockEmail.setDate("2024-01-01");
        mockEmail.setContent("Test Content");
        given(emailService.getEmailContentByIndex(0)).willReturn(mockEmail);

        mockMvc.perform(get("/email/emailContent")
                        .principal(testPrincipal)
                        .param("no", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /signupStudent.do returns 200 when ID check requested")
    void signupStudentIdCheckReturnsOk() throws Exception {
        given(userService.validateUserLoginID(anyString(), any(User.class)))
                .willReturn("등록 가능한 계정 입니다.");

        mockMvc.perform(post("/signupStudent.do")
                        .param("IdCheck", "true")
                        .param("UserLoginID", "12345678"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /findPassword.do returns 200 when ID check requested")
    void findPasswordIdCheckReturnsOk() throws Exception {
        given(userService.verifyUserForPasswordReset(any(User.class))).willReturn(true);

        mockMvc.perform(post("/findPassword.do")
                        .param("IdCheck", "true")
                        .param("UserLoginID", "testUser")
                        .param("UserName", "TestName"))
                .andExpect(status().isOk());
    }
}
