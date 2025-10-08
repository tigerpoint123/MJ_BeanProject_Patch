package com.mju.groupware.controller;

import com.mju.groupware.constant.*;
import com.mju.groupware.dto.*;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserFunctionController {
   private final UserService userService;
   private final StudentService studentService;
   private final ProfessorService professorService;
   private final EmailService emailService;
   private final UserEmailService userEmailService;
   private final UserInfoMethod userInfoMethod;

    private final ConstantWithdrawal constantWithdrawal;
    private final ConstantFindPassword constantFindPassword;
    private final ConstantHome constantHome;
    private final ConstantMyPostList constantMyPostList;
    private final ConstantMyInquiryList constantMyInquiryList;
    private final ConstantUserFunctionURL constantUserFunctionURL;
    private final ConstantDoEmail constantDoEmail;
    private final ConstantDoSignUp constantDoSignUp;
    private final ConstantDoFindPassword constantDoFindPassword;
    private final ConstantEmail constantEmail;

   @GetMapping("/findPassword")
   public String findPassword() {
        return this.constantFindPassword.getFPUrl();
   }

   /* 이메일 인증 후 비밀번호 보여주기 */
    @GetMapping("/showPassword")
    public String showPassword() {
        return this.constantFindPassword.getSPUrl();
   }

   // home 로그인 완료 화면 + 날짜 업데이트
     @GetMapping("/home")
     public String home(User user, Principal principal, Model model) {
      if (principal != null) { // 지우면 오류(로그인 안해서 principal 못가져오는)납니다
             // 유저 정보 조회 및 모델 설정
             userInfoMethod.getUserInformation(principal, user, model, 
                 this.constantHome.getSRole(), 
                 this.constantHome.getPRole(), 
                 this.constantHome.getARole());

             // 로그인 날짜 업데이트 및 휴먼계정 복구
             userService.processLoginDateUpdate(principal.getName(), this.constantHome.getDFormat());
      }

      // 공지사항 리스트 띄우기
         List<Board> noticeList = userService.getNoticeBoardList();
         model.addAttribute(this.constantHome.getNL(), noticeList);

      // 커뮤니티 리스트 띄우기
         List<Board> communityList = userService.getCommunityBoardList();
         model.addAttribute(this.constantHome.getCL(), communityList);

         return this.constantHome.getHUrl();
     }

    @GetMapping("/")
    public String blankHome(User user, Principal principal, Model model) {
      if (principal != null) {
            // 유저 정보 조회 및 모델 설정
            userInfoMethod.getUserInformation(principal, user, model, 
                this.constantHome.getSRole(), 
                this.constantHome.getPRole(), 
                this.constantHome.getARole());

            // 로그인 날짜 업데이트 및 휴먼계정 복구
            userService.processLoginDateUpdate(principal.getName(), this.constantHome.getDFormat());
      }

      // 공지사항 리스트 띄우기
        List<Board> noticeList = userService.getNoticeBoardList();
        model.addAttribute("noticeList", noticeList);

      // 커뮤니티 리스트 띄우기
        List<Board> communityList = userService.getCommunityBoardList();
        model.addAttribute(this.constantHome.getCL(), communityList);

        return this.constantHome.getHUrl();
   }

   // home에서 마이페이지 클릭 시 회원 role에 따라 페이지 리턴
    @GetMapping("/myPage")
   public String myPageByRole(HttpServletRequest request, Model model) throws IOException {
        String mysqlRole = request.getParameter("R");

        if (mysqlRole.equals(this.constantHome.getSRole()))
            return this.constantHome.getMPSUrl();

        if (mysqlRole.equals(this.constantHome.getPRole()))
            return this.constantHome.getMPPUrl();

        if (mysqlRole.equals(this.constantHome.getARole()))
            return this.constantHome.getRUrl();

        return this.constantHome.getRUrl();
   }

   // 마이페이지 - 내 게시글 조회
    @GetMapping("/myPostList")
   public String myPostList(Model model, Principal principal, User user) {
        // 유저 정보 조회
        getUserInformation(principal, user, model);
        
        // 내 게시글 리스트 조회
        String loginID = principal.getName();
        List<Board> myBoardList = userService.getMyBoardList(loginID);
        model.addAttribute(this.constantMyPostList.getMBList(), myBoardList);

        return this.constantMyPostList.getMBUrl();
   }

   // 마이페이지 - 내 문의 조회
    @GetMapping("/myInquiryList")
   public String myInquiryList(Model model, Principal principal, User user) {
        // 유저 정보 조회
        getUserInformation(principal, user, model);
        
        // 내 문의 리스트 조회
        String loginID = principal.getName();
        List<Inquiry> myInquiryList = userService.getMyInquiryList(loginID);
        if (!myInquiryList.isEmpty()) {
            model.addAttribute(this.constantMyInquiryList.getMIList(), myInquiryList);
        }
        return this.constantMyInquiryList.getMIUrl();
   }

   /* 마이페이지 - 내 팀 조회 */
    @GetMapping("/checkMyTeam")
   public String checkMyTeam(Model model, Principal principal, User user) {
      // 유저 정보
        getUserInformation(principal, user, model);
      //
        return this.constantUserFunctionURL.getCMTUrl();
   }

   /* 정보 수정 버튼 클릭 시 비밀번호 확인 */
    @GetMapping("/checkPassword")
   public String checkPassword() {
        return this.constantUserFunctionURL.getCPUrl();
   }

   /* 비밀번호 변경 화면 */
   @GetMapping("/modifyPassword")
   public String modifyPassword() {
        return this.constantUserFunctionURL.getMPUrl();
   }

   // 탈퇴 매뉴얼
   @GetMapping("/withdrawal")
   public String withdrawal() {
        return this.constantWithdrawal.getUrl();
    }

    @PostMapping("/withdrawal")
    public String doWithdrawal(HttpServletRequest request, Principal principal) {
        if (request.getParameter(this.constantWithdrawal.getParameter1()) == null) {
            return this.constantWithdrawal.getUrl();
        }
        
        String userLoginID = principal.getName();
        userService.processWithdrawal(userLoginID, this.constantWithdrawal.getParameter2());
        return this.constantWithdrawal.getUrl();
   }

   // 이거는 탈퇴 전에 비밀번호를 확인하기 위함. 수정하기 눌렀을때의 화면, 로직 다 똑같음.
    @GetMapping("/checkPassword2")
   public String checkPassword2() {
        return this.constantUserFunctionURL.getCPWUrl();
   }

    @PostMapping("/checkPassword2.do")
    public String checkPassword2(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userID = principal.getName();

        boolean checker = userService.SelectForPwdCheckBeforeModify(userID,
                request.getParameter(this.constantUserFunctionURL.getULPWD()));
        if (checker) {
            return this.constantUserFunctionURL.getRWUrl();
      } else {
         response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('비밀번호를 다시 입력해주세요.' );</script>");
            out.flush();
            return this.constantUserFunctionURL.getCPWUrl();
      }
   }

    @GetMapping("/emailAuthentication")
   public String emailAuthentication() {
        return this.constantUserFunctionURL.getEAUrl();
   }

   // 이메일 중복확인, 이메일 발송
   @PostMapping("/email.do")
    public String doEmail(RedirectAttributes redirectAttributes, Model model,
                          HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String userEmail = request.getParameter(this.constantDoEmail.getEM());

        // 이메일 값이 있으면 모델에 추가
        if (request.getParameter(this.constantDoEmail.getEM()) != null) {
            model.addAttribute(this.constantDoEmail.getEM(), userEmail);
            String address = this.constantDoEmail.getEmailAdress();
            userEmail = userEmail + address;
        }
        
        // 인증번호 값이 있으면 모델에 추가
        if (request.getParameter(this.constantDoEmail.getAuthNum()) != null) {
            model.addAttribute(this.constantDoEmail.getAuthNum(),
                    request.getParameter(this.constantDoEmail.getAuthNum()));
        }

        // 이메일 체크 (이메일 발송)
        if (request.getParameter(this.constantDoEmail.getEC()) != null && !userEmail.isEmpty()) {
            String result = userEmailService.processEmailCertification(userEmail, this.constantDoEmail.getDateFormat());
            
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            if ("duplicate".equals(result)) {
                out.println("<script>alert('이미 등록된 이메일 입니다.' );</script>");
         } else {
                out.println("<script>alert('성공적으로 이메일 발송이 완료되었습니다.' );</script>");
            }
            out.flush();
            return this.constantDoEmail.getAuthUrl();
        }
        
        // 이메일이 비어있는 경우
        if (userEmail.isEmpty()) {
            return this.constantDoEmail.getAuthUrl();
        }
        
        // 인증번호 확인
        if (request.getParameter(this.constantDoEmail.getEV()) != null
                && !request.getParameter(this.constantDoEmail.getAuthNum()).isEmpty()) {
            boolean checker = userEmailService.verifyCertification(request.getParameter(this.constantDoEmail.getAuthNum()));
            
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            if (checker) {
                out.println("<script>alert('인증에 성공하셨습니다.' );</script>");
                out.flush();
                session.setAttribute("emailChecker", true);
                session.setAttribute("userEmail", userEmail); // 인증된 이메일 세션에 저장
         } else {
                out.println("<script>alert('일치하지 않는 인증번호입니다. 다시한번 확인해주세요' );</script>");
                out.flush();
                session.setAttribute("emailChecker", false);
                return this.constantDoEmail.getAuthUrl();
            }
        }

        // 동의 버튼 클릭
        Boolean emailChecker = (Boolean) session.getAttribute("emailChecker");
        if (request.getParameter(this.constantDoEmail.getBA()) != null && Boolean.TRUE.equals(emailChecker)) {
            session.removeAttribute("emailChecker");
            return this.constantDoEmail.getAgreeUrl();
        }
        
        return this.constantDoEmail.getAuthUrl();
   }

   // 학생 회원가입
    @PostMapping("/signupStudent.do")
    public String doSignUpStudent(User user, Student student, RedirectAttributes redirectAttributes, Model model,
                                  HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // 파라미터 추출
        String userLoginID = request.getParameter("UserLoginID");
        String studentGender = request.getParameter("StudentGender");
        String studentGradeForSignUp = request.getParameter("StudentGrade");
        String studentColleges = request.getParameter("StudentColleges");
        String studentMajor = request.getParameter("StudentMajor");
        String studentDoubleMajor = request.getParameter("StudentDoubleMajor");

        // 입력값 유지를 위한 모델 추가
        addStudentAttributesToModel(model, request, userLoginID, studentGender, studentGradeForSignUp, 
                                   studentColleges, studentMajor, studentDoubleMajor);

        // ID 중복 체크
        if (request.getParameter("IdCheck") != null) {
            return handleIdCheck(request, response, session, user, this.constantDoSignUp.getSSUrl());
        }
        
        // 회원가입 제출
        Boolean idChecker = (Boolean) session.getAttribute("idChecker");
        if (request.getParameter("submitName") != null && Boolean.TRUE.equals(idChecker)) {
            // 입력값 검증
            String validationError = userService.validateStudentSignUp(studentColleges, studentMajor);
            if (validationError != null) {
                showAlert(response, validationError);
                return this.constantDoSignUp.getSSUrl();
            }
            
            // 세션에서 저장된 이메일 가져오기
            String userEmail = (String) session.getAttribute("userEmail");
            user.setUserRole(this.constantDoSignUp.getSRole());
            
            // 회원가입 처리
            boolean isDoubleMajor = "Y".equals(request.getParameter("member"));
            userService.registerStudent(user, student, studentColleges, studentMajor, 
                                       studentDoubleMajor, userEmail, isDoubleMajor);
            
            clearSignUpSession(session);

            redirectAttributes.addFlashAttribute("msg", "signupERED");
            showAlert(response, "회원가입이 완료 되었습니다.");
            return this.constantDoSignUp.getSLUrl();
        }
        
        return this.constantDoSignUp.getSSUrl();
   }

   // 교수 회원가입
    @PostMapping("/signupProfessor.do")
    public String doSignUpProfessor(User user, Professor professor, RedirectAttributes redirectAttributes, Model model,
                                    HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        // 파라미터 추출
        String userLoginID = request.getParameter("UserLoginID");
        String professorColleges = request.getParameter("ProfessorColleges");
        String professorMajor = request.getParameter("ProfessorMajor");
        String professorRoom = request.getParameter("ProfessorRoom");
        String professorRoomNum = request.getParameter("ProfessorRoomNum");

        // 입력값 유지를 위한 모델 추가
        addProfessorAttributesToModel(model, request, userLoginID, professorColleges, professorMajor, professorRoom, professorRoomNum);

        // ID 중복 체크
        if (request.getParameter("IdCheck") != null) {
            return handleIdCheck(request, response, session, user, "/signup/signupProfessor");
        }
        
        // 회원가입 제출
        Boolean idChecker = (Boolean) session.getAttribute("idChecker");
        if (request.getParameter("submitName") != null && Boolean.TRUE.equals(idChecker)) {
            // 입력값 검증
            String validationError = userService.validateProfessorSignUp(professorColleges, professorMajor);
            if (validationError != null) {
                showAlert(response, validationError);
               return "/signup/signupProfessor";
            }
            
            // 세션에서 저장된 이메일 가져오기
            String userEmail = (String) session.getAttribute("userEmail");
            user.setUserRole(this.constantDoSignUp.getPRole());
            
            // 회원가입 처리
            userService.registerProfessor(user, professor, professorColleges, professorMajor,
                                        professorRoom, professorRoomNum, userEmail);
            
            clearSignUpSession(session);

            redirectAttributes.addFlashAttribute("msg", "signupERED");
            showAlert(response, "회원가입이 완료 되었습니다.");
            return "/signin/login";
         }
        
         return "/signup/signupProfessor";
   }

   // 비밀번호 찾기 findPassword.do여기서부터하기
    @PostMapping("/findPassword.do")
   public String findPassword(User user, RedirectAttributes redirectAttributes, Model model,
                               HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String userLoginID = request.getParameter("UserLoginID");
        String userEmail = request.getParameter("UserEmail");

        // ID 확인
      if (request.getParameter("IdCheck") != null) {
            user.setUserLoginID(userLoginID);
            user.setUserName(request.getParameter(this.constantDoFindPassword.getUName()));
            
            if (userLoginID.isEmpty()) {
                showAlert(response, "계정을 입력하지 않으셨습니다.");
                return this.constantDoFindPassword.getFPUrl();
            }
            
            if (request.getParameter(this.constantDoFindPassword.getUName()).isEmpty()) {
                model.addAttribute("UserLoginID", userLoginID);
                showAlert(response, "이름을 입력하지 않으셨습니다.");
                return this.constantDoFindPassword.getFPUrl();
            }
            
            boolean localIdChecker = this.userService.verifyUserForPasswordReset(user);
            
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(this.constantDoFindPassword.getUName(),
                    request.getParameter(this.constantDoFindPassword.getUName()));
            
            if (localIdChecker) {
                showAlert(response, "계정이 확인되었습니다.");
                session.setAttribute("findPwdIdChecker", true);
         } else {
                showAlert(response, "등록된 사용자가 아닙니다.");
                session.setAttribute("findPwdIdChecker", false);
            }
            return this.constantDoFindPassword.getFPUrl();
        }
        
        // 이메일 체크
        if (request.getParameter("EmailCheck") != null) {
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(this.constantDoFindPassword.getUName(),
                    request.getParameter(this.constantDoFindPassword.getUName()));
            
            if (userEmail.isEmpty()) {
                showAlert(response, "이메일을 입력하지 않으셨습니다.");
                return this.constantDoFindPassword.getFPUrl();
            }
            
            model.addAttribute("UserEmail", userEmail);
            String fullEmail = userEmail + "@mju.ac.kr";
            
            // 이메일 발송 처리
            String emailResult = emailService.sendEmailForPasswordReset(fullEmail);
            showAlert(response, emailResult);
            
            return this.constantDoFindPassword.getFPUrl();
        }
        
        // 인증번호 확인
        if (request.getParameter("EmailValid") != null) {
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(this.constantDoFindPassword.getUName(),
                    request.getParameter(this.constantDoFindPassword.getUName()));
            model.addAttribute("UserEmail", userEmail);
            
            boolean localNameChecker = emailService.AuthNum(request.getParameter(this.constantDoFindPassword.getAuthNum()));
            
            if (localNameChecker) {
                model.addAttribute(this.constantDoFindPassword.getAuthNum(),
                        request.getParameter(this.constantDoFindPassword.getAuthNum()));
                showAlert(response, "인증번호가 일치합니다.");
                session.setAttribute("findPwdNameChecker", true);
            } else {
                showAlert(response, "인증번호가 일치하지 않습니다.");
                session.setAttribute("findPwdNameChecker", false);
            }
            return this.constantDoFindPassword.getFPUrl();
        }
        
        // 최종 제출 (임시 비밀번호 생성)
        Boolean idChecker = (Boolean) session.getAttribute("findPwdIdChecker");
        Boolean nameChecker = (Boolean) session.getAttribute("findPwdNameChecker");
        
        if (request.getParameter("SubmitName") != null && Boolean.TRUE.equals(nameChecker) && Boolean.TRUE.equals(idChecker)) {
            user.setUserLoginID(userLoginID);
            user.setUserName(request.getParameter(this.constantDoFindPassword.getUName()));
            
            // 임시 비밀번호 생성 및 업데이트
            String newPwd = userService.generateAndUpdateTemporaryPassword(user);
            model.addAttribute("UserLoginPwd", newPwd);
            
            session.removeAttribute("findPwdIdChecker");
            session.removeAttribute("findPwdNameChecker");

            return this.constantDoFindPassword.getSSPUrl();
        }
        
        return this.constantDoFindPassword.getFPUrl();
   }

   /* 수정하기 전 비밀번호 확인 */
    @PostMapping("/checkPassword.do")
    public String checkPassword(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userLoginID = principal.getName();
        boolean checker = userService.SelectForPwdCheckBeforeModify(userLoginID,
                request.getParameter(this.constantDoFindPassword.getPwd()));
        
        if (!checker) {
            showAlert(response, "비밀번호를 다시 입력해주세요.");
            return this.constantDoFindPassword.getCPUrl();
        }
        
        // 역할에 따른 리다이렉트 URL 반환
        return userService.getRedirectUrlByRole(userLoginID, 
                this.constantDoFindPassword.getSRole(), 
                this.constantDoFindPassword.getPRole(),
                this.constantDoFindPassword.getRMS(),
                this.constantDoFindPassword.getRMP());
   }

   // 비밀번호 변경할 때 현재 비번 체크
    @GetMapping("/checkPassword3")
   public String checkPassword3() {
        return this.constantDoFindPassword.getCPUrl3();
   }

    @PostMapping("/checkPassword3.do")
    public String checkPassword3(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userID = principal.getName();
        boolean checker = userService.SelectForPwdCheckBeforeModify(userID,
                request.getParameter(this.constantDoFindPassword.getULPWD()));
        
        if (!checker) {
            showAlert(response, "비밀번호를 다시 입력해주세요.");
            return this.constantDoFindPassword.getCPUrl3();
        }
        
        return this.constantDoFindPassword.getRMPWD();
   }

   // 비밀번호 수정
    @PostMapping("/modifyPassword.do")
    public String modifyPassword(HttpServletResponse response, HttpServletRequest request,
                                 Principal principal) throws IOException {
        String userLoginID = principal.getName();
        String newPassword = request.getParameter(this.constantDoFindPassword.getUNPWD());
        String confirmPassword = request.getParameter(this.constantDoFindPassword.getUNPWDC());
        
      // 새로입력한 비밀번호와 확인 비밀번호가 일치하면
        if (newPassword.equals(confirmPassword)) {
            userService.updatePassword(userLoginID, newPassword);
        }

        return this.constantDoFindPassword.getMPUrl();
   }

    // 이메일 로그인 화면
    @GetMapping("/email/emailLogin")
    public String emailLogin() {
        return this.constantEmail.getEMURL();
   }

   @PostMapping("/email/emailList")
    public String doEmailLogin(HttpServletRequest request, Model model, Principal principal, User user,
         RedirectAttributes rttr) {
      // 유저 정보
        getUserInformation(principal, user, model);

        // 이메일 로그인 확인
        String id = request.getParameter("EmailLoginID") + this.constantDoEmail.getEmailAdress();
        boolean checkId = emailService.CheckEmailLogin(id, request.getParameter(this.constantDoEmail.getEPwd()));

        if (!checkId) {
         rttr.addFlashAttribute("Checker", "LoginFail");
            return this.constantDoEmail.getRELURL();
      }
        
        return this.constantDoEmail.getREURL();
   }

    // 이메일 리스트 화면
    @GetMapping("/email/emailList")
    public String emailList(HttpServletRequest request, Model model, Principal principal, User user) {
      // 유저 정보
        getUserInformation(principal, user, model);
        
        // 이메일 리스트 조회
        List<UserEmail> emailList = emailService.PrintEmailList();
        
        if (!emailList.isEmpty()) {
            model.addAttribute("EmailList", emailList);
        }
        
        return this.constantDoEmail.getEURL();
   }

    // 이메일 리스트에서 제목 클릭 시 해당 이메일 내용 출력
    // 이메일 내용 화면
    @GetMapping("/email/emailContent")
    public String emailContent(HttpServletRequest request, Model model, Principal principal, User user) {
        getUserInformation(principal, user, model);

        // 이메일 번호 파라미터 처리
        String num = request.getParameter("no");
        int intNum = Integer.parseInt(num) - 1;
        
        // 이메일 내용 조회
        UserEmail emailContent = emailService.getEmailContentByIndex(intNum);
        
        if (emailContent != null) {
            model.addAttribute("EmailSender", emailContent.getFrom());
            model.addAttribute("EmailTitle", emailContent.getTitle());
            model.addAttribute("EmailDate", emailContent.getDate());
            model.addAttribute("EmailContent", emailContent.getContent());
        }
        
        return this.constantDoEmail.getECURL();
    }

    private void getUserInformation(Principal principal, User user, Model model) {
        String loginID = principal.getName();// 로그인 한 아이디
        ArrayList<String> selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
        user.setUserLoginID(loginID);

        if (selectUserProfileInfo.get(2).equals(this.constantHome.getSRole())) {
            ArrayList<String> studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));
         userInfoMethod.studentInfo(model, selectUserProfileInfo, studentInfo);
        } else if (selectUserProfileInfo.get(2).equals(this.constantHome.getPRole())) {
            ArrayList<String> professorInfo = professorService.selectProfessorProfileInfo(selectUserProfileInfo.get(1));
         userInfoMethod.professorInfo(model, selectUserProfileInfo, professorInfo);
        } else if (selectUserProfileInfo.get(2).equals(this.constantHome.getARole())) {
         userInfoMethod.administratorInfo(model, selectUserProfileInfo);
      }
   }

    // 헬퍼 메서드: 알럿 메시지 출력
    private void showAlert(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "');</script>");
        out.flush();
    }

    // 헬퍼 메서드: ID 중복 체크 처리
    private String handleIdCheck(HttpServletRequest request, HttpServletResponse response, 
                                 HttpSession session, User user, String returnUrl) throws IOException {
        String localUserLoginID = request.getParameter("UserLoginID");
        String validationMessage = userService.validateUserLoginID(localUserLoginID, user);
        
        showAlert(response, validationMessage);
        
        boolean isValid = "등록 가능한 계정 입니다.".equals(validationMessage);
        session.setAttribute("idChecker", isValid);
        
        return returnUrl;
    }

    // 헬퍼 메서드: 회원가입 세션 정리
    private void clearSignUpSession(HttpSession session) {
        session.removeAttribute("idChecker");
        session.removeAttribute("userEmail");
    }

    // 헬퍼 메서드: 학생 회원가입 모델 속성 추가
    private void addStudentAttributesToModel(Model model, HttpServletRequest request,
                                            String userLoginID, String studentGender, 
                                            String studentGrade, String studentColleges,
                                            String studentMajor, String studentDoubleMajor) {
        if (request.getParameter("UserLoginID") != null) {
            model.addAttribute("UserLoginID", userLoginID);
        }
        if (request.getParameter(this.constantDoSignUp.getPwd()) != null) {
            model.addAttribute(this.constantDoSignUp.getPwd(), request.getParameter(this.constantDoSignUp.getPwd()));
        }
        if (request.getParameter(this.constantDoSignUp.getSName()) != null) {
            model.addAttribute(this.constantDoSignUp.getSName(), request.getParameter(this.constantDoSignUp.getSName()));
        }
        if (request.getParameter("StudentGender") != null) {
            model.addAttribute("StudentGender", studentGender);
        }
        if (request.getParameter("UserPhoneNum") != null) {
            model.addAttribute(this.constantDoSignUp.getPhoneNum(),
                    request.getParameter(this.constantDoSignUp.getPhoneNum()));
        }
        if (request.getParameter(this.constantDoSignUp.getSNum()) != null) {
            model.addAttribute(this.constantDoSignUp.getSNum(), request.getParameter(this.constantDoSignUp.getSNum()));
        }
        if (request.getParameter("StudentGrade") != null) {
            model.addAttribute("StudentGrade", studentGrade);
        }
        if (request.getParameter("UserColleges") != null) {
            model.addAttribute("UserColleges", studentColleges);
        }
        if (request.getParameter("UserMajor") != null) {
            model.addAttribute("UserMajor", studentMajor);
        }
        if (request.getParameter("StudentDoubleMajor") != null) {
            model.addAttribute("StudentDoubleMajor", studentDoubleMajor);
        }
    }

    // 헬퍼 메서드: 교수 회원가입 모델 속성 추가
    private void addProfessorAttributesToModel(Model model, HttpServletRequest request, 
                                              String userLoginID, String professorColleges, 
                                              String professorMajor, String professorRoom, 
                                              String professorRoomNum) {
        if (request.getParameter("UserLoginID") != null) {
            model.addAttribute("UserLoginID", userLoginID);
        }
        if (request.getParameter(this.constantDoSignUp.getPwd()) != null) {
            model.addAttribute(this.constantDoSignUp.getPwd(), request.getParameter(this.constantDoSignUp.getPwd()));
        }
        if (request.getParameter(this.constantDoSignUp.getPName()) != null) {
            model.addAttribute(this.constantDoSignUp.getPName(), request.getParameter(this.constantDoSignUp.getPName()));
        }
        if (request.getParameter("UserPhoneNum") != null) {
            model.addAttribute(this.constantDoSignUp.getPhoneNum(),
                    request.getParameter(this.constantDoSignUp.getPhoneNum()));
        }
        if (request.getParameter(this.constantDoSignUp.getPNum()) != null) {
            model.addAttribute(this.constantDoSignUp.getPNum(), request.getParameter(this.constantDoSignUp.getPNum()));
        }
        if (request.getParameter("UserColleges") != null) {
            model.addAttribute("UserColleges", professorColleges);
        }
        if (request.getParameter("UserMajor") != null) {
            model.addAttribute("UserMajor", professorMajor);
        }
        if (request.getParameter("ProfessorRoom") != null) {
            model.addAttribute("ProfessorRoom", professorRoom);
        }
        if (request.getParameter("ProfessorRoomNum") != null) {
            model.addAttribute("ProfessorRoomNum", professorRoomNum);
      }
   }

}