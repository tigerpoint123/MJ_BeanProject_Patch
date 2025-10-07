package com.mju.groupware.controller;

import com.mju.groupware.constant.*;
import com.mju.groupware.dto.*;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
import global.config.HomeControllerXmlConfig;
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
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserFunctionController 테스트
 * 사용자 기능 관련 엔드포인트 테스트: 비밀번호 찾기, 홈, 마이페이지, 회원탈퇴, 이메일 등
 */
@WebMvcTest(
        controllers = UserFunctionController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = HomeControllerXmlConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
class UserFunctionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private EmailService emailService;
    @MockBean private UserEmailService userEmailService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private BoardService boardService;
    @MockBean private InquiryService inquiryService;
    @MockBean private GenericXmlApplicationContext ctx;

    private Principal testPrincipal;
    
    // Constant Mock 객체들을 필드로 선언
    private ConstantHome constantHome;
    private ConstantFindPassword constantFindPassword;
    private ConstantMyPostList constantMyPostList;
    private ConstantMyInquiryList constantMyInquiryList;
    private ConstantUserFunctionURL constantUserFunctionURL;
    private ConstantWithdrawal constantWithdrawal;
    private ConstantDoEmail constantDoEmail;
    private ConstantDoFindPassword constantDoFindPassword;
    private ConstantDoSignUp constantDoSignUp;
    private ConstantEmail constantEmail;

    @BeforeEach
    void setUp() {
        testPrincipal = () -> "testUser";

        // 공통 사용자 프로필 Mock
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("TestName", "UID123", "ROLE_STUDENT")));

        // 모든 Constant Mock 객체 생성 및 설정
        setupConstantHome();
        setupConstantFindPassword();
        setupConstantMyPostList();
        setupConstantMyInquiryList();
        setupConstantUserFunctionURL();
        setupConstantWithdrawal();
        setupConstantDoEmail();
        setupConstantDoFindPassword();
        setupConstantDoSignUp();
        setupConstantEmail();
    }

    private void setupConstantHome() {
        constantHome = mock(ConstantHome.class);
        given(constantHome.getHUrl()).willReturn("home");
        given(constantHome.getSRole()).willReturn("ROLE_STUDENT");
        given(constantHome.getPRole()).willReturn("ROLE_PROFESSOR");
        given(constantHome.getARole()).willReturn("ROLE_ADMINISTRATOR");
        given(constantHome.getDFormat()).willReturn("yyyy-MM-dd HH:mm:ss");
        given(constantHome.getNL()).willReturn("noticeList");
        given(constantHome.getCL()).willReturn("communityList");
        given(constantHome.getMPSUrl()).willReturn("redirect:/myPageStudent");
        given(constantHome.getMPPUrl()).willReturn("redirect:/myPageProfessor");
        given(constantHome.getRUrl()).willReturn("redirect:/");
        given(ctx.getBean("Home")).willReturn(constantHome);
    }

    private void setupConstantFindPassword() {
        constantFindPassword = mock(ConstantFindPassword.class);
        given(constantFindPassword.getFPUrl()).willReturn("user/findPassword");
        given(constantFindPassword.getSPUrl()).willReturn("user/showPassword");
        given(ctx.getBean("FindPassword")).willReturn(constantFindPassword);
    }

    private void setupConstantMyPostList() {
        constantMyPostList = mock(ConstantMyPostList.class);
        given(constantMyPostList.getMBUrl()).willReturn("user/myPostList");
        given(constantMyPostList.getMBList()).willReturn("myBoardList");
        given(ctx.getBean("MyPostList")).willReturn(constantMyPostList);
    }

    private void setupConstantMyInquiryList() {
        constantMyInquiryList = mock(ConstantMyInquiryList.class);
        given(constantMyInquiryList.getMIUrl()).willReturn("user/myInquiryList");
        given(constantMyInquiryList.getMIList()).willReturn("myInquiryList");
        given(ctx.getBean("MyInquiryList")).willReturn(constantMyInquiryList);
    }

    private void setupConstantUserFunctionURL() {
        constantUserFunctionURL = mock(ConstantUserFunctionURL.class);
        given(constantUserFunctionURL.getCMTUrl()).willReturn("user/checkMyTeam");
        given(constantUserFunctionURL.getCPUrl()).willReturn("user/checkPassword");
        given(constantUserFunctionURL.getMPUrl()).willReturn("user/modifyPassword");
        given(constantUserFunctionURL.getCPWUrl()).willReturn("user/checkPassword2");
        given(constantUserFunctionURL.getRWUrl()).willReturn("redirect:/withdrawal");
        given(constantUserFunctionURL.getEAUrl()).willReturn("user/emailAuthentication");
        given(constantUserFunctionURL.getULPWD()).willReturn("UserLoginPwd");
        given(ctx.getBean("UserFunctionURL")).willReturn(constantUserFunctionURL);
    }

    private void setupConstantWithdrawal() {
        constantWithdrawal = mock(ConstantWithdrawal.class);
        given(constantWithdrawal.getUrl()).willReturn("user/withdrawal");
        given(constantWithdrawal.getParameter1()).willReturn("Submit");
        given(constantWithdrawal.getParameter2()).willReturn("yyyy-MM-dd");
        given(ctx.getBean("Withdrawal")).willReturn(constantWithdrawal);
    }

    private void setupConstantDoEmail() {
        constantDoEmail = mock(ConstantDoEmail.class);
        given(constantDoEmail.getEM()).willReturn("Email");
        given(constantDoEmail.getAuthNum()).willReturn("AuthNum");
        given(constantDoEmail.getEC()).willReturn("EmailCheck");
        given(constantDoEmail.getEV()).willReturn("EmailValid");
        given(constantDoEmail.getBA()).willReturn("ButtonAgree");
        given(constantDoEmail.getEmailAdress()).willReturn("@mju.ac.kr");
        given(constantDoEmail.getDateFormat()).willReturn("yyyy-MM-dd HH:mm:ss");
        given(constantDoEmail.getAuthUrl()).willReturn("user/emailAuthentication");
        given(constantDoEmail.getAgreeUrl()).willReturn("user/agree");
        given(constantDoEmail.getREURL()).willReturn("redirect:/email/emailList");
        given(constantDoEmail.getRELURL()).willReturn("redirect:/email/emailLogin");
        given(constantDoEmail.getEURL()).willReturn("email/emailList");
        given(constantDoEmail.getECURL()).willReturn("email/emailContent");
        given(constantDoEmail.getEPwd()).willReturn("EmailPwd");
        given(ctx.getBean("DoEmail")).willReturn(constantDoEmail);
    }

    private void setupConstantDoFindPassword() {
        constantDoFindPassword = mock(ConstantDoFindPassword.class);
        given(constantDoFindPassword.getUName()).willReturn("UserName");
        given(constantDoFindPassword.getFPUrl()).willReturn("user/findPassword");
        given(constantDoFindPassword.getPwd()).willReturn("UserLoginPwd");
        given(constantDoFindPassword.getSRole()).willReturn("ROLE_STUDENT");
        given(constantDoFindPassword.getPRole()).willReturn("ROLE_PROFESSOR");
        given(constantDoFindPassword.getRMS()).willReturn("redirect:/modifyStudent");
        given(constantDoFindPassword.getRMP()).willReturn("redirect:/modifyProfessor");
        given(constantDoFindPassword.getCPUrl()).willReturn("user/checkPassword");
        given(constantDoFindPassword.getCPUrl3()).willReturn("user/checkPassword3");
        given(constantDoFindPassword.getRMPWD()).willReturn("redirect:/modifyPassword");
        given(constantDoFindPassword.getULPWD()).willReturn("UserLoginPwd");
        given(constantDoFindPassword.getUNPWD()).willReturn("UserNewPwd");
        given(constantDoFindPassword.getUNPWDC()).willReturn("UserNewPwdConfirm");
        given(constantDoFindPassword.getSSPUrl()).willReturn("user/showPassword");
        given(constantDoFindPassword.getAuthNum()).willReturn("AuthNum");
        given(constantDoFindPassword.getMPUrl()).willReturn("user/modifyPassword");
        given(ctx.getBean("DoFindPassword")).willReturn(constantDoFindPassword);
    }

    private void setupConstantDoSignUp() {
        constantDoSignUp = mock(ConstantDoSignUp.class);
        given(constantDoSignUp.getSSUrl()).willReturn("/signup/signupStudent");
        given(constantDoSignUp.getPwd()).willReturn("UserLoginPwd");
        given(constantDoSignUp.getSName()).willReturn("StudentName");
        given(constantDoSignUp.getPhoneNum()).willReturn("UserPhoneNum");
        given(constantDoSignUp.getSNum()).willReturn("StudentNum");
        given(constantDoSignUp.getSRole()).willReturn("ROLE_STUDENT");
        given(constantDoSignUp.getSLUrl()).willReturn("/signin/login");
        given(constantDoSignUp.getPName()).willReturn("ProfessorName");
        given(constantDoSignUp.getPNum()).willReturn("ProfessorNum");
        given(constantDoSignUp.getPRole()).willReturn("ROLE_PROFESSOR");
        given(ctx.getBean("DoSignUp")).willReturn(constantDoSignUp);
    }

    private void setupConstantEmail() {
        constantEmail = mock(ConstantEmail.class);
        given(constantEmail.getEMURL()).willReturn("email/emailLogin");
        given(ctx.getBean("Email")).willReturn(constantEmail);
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
        given(userService.SelectUserIDForDate("testUser")).willReturn("123");
        given(userService.SelectDormant("testUser")).willReturn(false);
        given(studentService.selectStudentProfileInfo("UID123"))
                .willReturn(new ArrayList<>(Arrays.asList("College", "Major", "3")));
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());
        given(boardService.getCommunityList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/home").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /home returns 200 without principal")
    void homeReturnsOkWithoutPrincipal() throws Exception {
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());
        given(boardService.getCommunityList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET / returns 200")
    void rootHomeReturnsOk() throws Exception {
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());
        given(boardService.getCommunityList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myPage redirects based on role")
    void myPageByRoleRedirects() throws Exception {
        mockMvc.perform(get("/myPage").param("R", "ROLE_STUDENT"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /myPostList returns 200")
    void myPostListReturnsOk() throws Exception {
        given(userService.SelectUserIDForMyBoard("testUser")).willReturn("123");
        given(boardService.SelectMyBoardList("123")).willReturn(Collections.emptyList());

        mockMvc.perform(get("/myPostList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /myInquiryList returns 200")
    void myInquiryListReturnsOk() throws Exception {
        given(userService.SelectUserIDForMyBoard("testUser")).willReturn("123");
        given(inquiryService.SelectMyInquiryList("123")).willReturn(Collections.emptyList());

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
        User user = new User();
        user.setUserLoginID("testUser");
        given(userService.SelectUserInfo("testUser")).willReturn(user);

        mockMvc.perform(post("/withdrawal")
                        .principal(testPrincipal)
                        .param("Submit", "true"))
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
        given(emailService.SelectForEmailDuplicateCheck(any(User.class))).willReturn(false);
        given(emailService.sendEmail(any(User.class))).willReturn(123456);

        mockMvc.perform(post("/email.do")
                        .param("Email", "test")
                        .param("EmailCheck", "true"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /checkPassword.do redirects when password correct")
    void checkPasswordPostRedirectsWhenCorrect() throws Exception {
        given(userService.SelectForPwdCheckBeforeModify("testUser", "password123"))
                .willReturn(true);
        given(userService.SelectUserRole("testUser")).willReturn("ROLE_STUDENT");

        mockMvc.perform(post("/checkPassword.do")
                        .principal(testPrincipal)
                        .param("UserLoginPwd", "password123"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /modifyPassword.do returns 200")
    void modifyPasswordPostReturnsOk() throws Exception {
        given(userService.SelectCurrentPwd("testUser")).willReturn("oldHashedPwd");

        mockMvc.perform(post("/modifyPassword.do")
                        .principal(testPrincipal)
                        .param("UserNewPwd", "newPassword123")
                        .param("UserNewPwdConfirm", "newPassword123"))
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
                        .param("EmailPwd", "password"))
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
        given(emailService.GetEmailList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/email/emailContent")
                        .principal(testPrincipal)
                        .param("no", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /signupStudent.do returns 200 when ID check requested")
    void signupStudentIdCheckReturnsOk() throws Exception {
        given(userService.SelctForIDConfirm(any(User.class))).willReturn(false);

        mockMvc.perform(post("/signupStudent.do")
                        .param("IdCheck", "true")
                        .param("UserLoginID", "12345678"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /findPassword.do returns 200 when ID check requested")
    void findPasswordIdCheckReturnsOk() throws Exception {
        given(userService.SelectPwdForConfirmToFindPwd(any(User.class))).willReturn(true);

        mockMvc.perform(post("/findPassword.do")
                        .param("IdCheck", "true")
                        .param("UserLoginID", "testUser")
                        .param("UserName", "TestName"))
                .andExpect(status().isOk());
    }
}
