package com.mju.groupware.controller;

import global.properties.UserFunctionProperties;
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

    private final UserFunctionProperties userProps;

   @GetMapping("/findPassword")
   public String findPassword() {
        return userProps.getUrls().getSignin().getFindPassword();
   }

   /* 이메일 인증 후 비밀번호 보여주기 */
    @GetMapping("/showPassword")
    public String showPassword() {
        return userProps.getUrls().getSignin().getShowPassword();
   }

   // home 로그인 완료 화면 + 날짜 업데이트
     @GetMapping("/home")
     public String home(User user, Principal principal, Model model) {
      if (principal != null) { // 지우면 오류(로그인 안해서 principal 못가져오는)납니다
             // 유저 정보 조회 및 모델 설정
             userInfoMethod.getUserInformation(principal, user, model, 
                 userProps.getRoles().getStudent(), 
                 userProps.getRoles().getProfessor(), 
                 userProps.getRoles().getAdministrator());

             // 로그인 날짜 업데이트 및 휴먼계정 복구
             userService.processLoginDateUpdate(principal.getName(), userProps.getFormats().getDate());
      }

      // 공지사항 리스트 띄우기
         List<Board> noticeList = userService.getNoticeBoardList();
         model.addAttribute(userProps.getAttributes().getNoticeList(), noticeList);

      // 커뮤니티 리스트 띄우기
         List<Board> communityList = userService.getCommunityBoardList();
         model.addAttribute(userProps.getAttributes().getCommunityList(), communityList);

         return userProps.getUrls().getHome();
     }

    @GetMapping("/")
    public String blankHome(User user, Principal principal, Model model) {
      if (principal != null) {
            // 유저 정보 조회 및 모델 설정
            userInfoMethod.getUserInformation(principal, user, model, 
                userProps.getRoles().getStudent(), 
                userProps.getRoles().getProfessor(), 
                userProps.getRoles().getAdministrator());

            // 로그인 날짜 업데이트 및 휴먼계정 복구
            userService.processLoginDateUpdate(principal.getName(), userProps.getFormats().getDate());
      }

      // 공지사항 리스트 띄우기
        List<Board> noticeList = userService.getNoticeBoardList();
        model.addAttribute("noticeList", noticeList);

      // 커뮤니티 리스트 띄우기
        List<Board> communityList = userService.getCommunityBoardList();
        model.addAttribute(userProps.getAttributes().getCommunityList(), communityList);

        return userProps.getUrls().getHome();
   }

   // home에서 마이페이지 클릭 시 회원 role에 따라 페이지 리턴
    @GetMapping("/myPage")
   public String myPageByRole(HttpServletRequest request, Model model) throws IOException {
        String mysqlRole = request.getParameter("R");

        if (mysqlRole.equals(userProps.getRoles().getStudent()))
            return userProps.getRedirects().getMypageStudent();

        if (mysqlRole.equals(userProps.getRoles().getProfessor()))
            return userProps.getRedirects().getMypageProfessor();

        if (mysqlRole.equals(userProps.getRoles().getAdministrator()))
            return userProps.getRedirects().getHome();

        return userProps.getRedirects().getHome();
   }

   // 마이페이지 - 내 게시글 조회
    @GetMapping("/myPostList")
   public String myPostList(Model model, Principal principal, User user) {
        // 유저 정보 조회
        getUserInformation(principal, user, model);
        
        // 내 게시글 리스트 조회
        String loginID = principal.getName();
        List<Board> myBoardList = userService.getMyBoardList(loginID);
        model.addAttribute(userProps.getAttributes().getMyBoardList(), myBoardList);

        return userProps.getUrls().getMypage().getPostList();
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
            model.addAttribute(userProps.getAttributes().getMyInquiryList(), myInquiryList);
        }
        return userProps.getUrls().getMypage().getInquiryList();
   }

   /* 마이페이지 - 내 팀 조회 */
    @GetMapping("/checkMyTeam")
   public String checkMyTeam(Model model, Principal principal, User user) {
      // 유저 정보
        getUserInformation(principal, user, model);
      //
        return userProps.getUrls().getMypage().getCheckMyTeam();
   }

   /* 정보 수정 버튼 클릭 시 비밀번호 확인 */
    @GetMapping("/checkPassword")
   public String checkPassword() {
        return userProps.getUrls().getMypage().getCheckPassword();
   }

   /* 비밀번호 변경 화면 */
   @GetMapping("/modifyPassword")
   public String modifyPassword() {
        return userProps.getUrls().getMypage().getModifyPassword();
   }

   // 탈퇴 매뉴얼
   @GetMapping("/withdrawal")
   public String withdrawal() {
        return userProps.getUrls().getWithdrawal();
    }

    @PostMapping("/withdrawal")
    public String doWithdrawal(HttpServletRequest request, Principal principal) {
        if (request.getParameter(userProps.getParams().getWithdrawal().getParameter1()) == null) {
            return userProps.getUrls().getWithdrawal();
        }
        
        String userLoginID = principal.getName();
        userService.processWithdrawal(userLoginID, userProps.getParams().getWithdrawal().getParameter2());
        return userProps.getUrls().getWithdrawal();
   }

   // 이거는 탈퇴 전에 비밀번호를 확인하기 위함. 수정하기 눌렀을때의 화면, 로직 다 똑같음.
    @GetMapping("/checkPassword2")
   public String checkPassword2() {
        return userProps.getUrls().getMypage().getCheckPassword2();
   }

    @PostMapping("/checkPassword2.do")
    public String checkPassword2(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userID = principal.getName();

        boolean checker = userService.selectForPwdCheckBeforeModify(userID,
                request.getParameter(userProps.getParams().getUserLoginPwd()));
        if (checker) {
            return userProps.getRedirects().getWithdrawal();
      } else {
         response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('비밀번호를 다시 입력해주세요.' );</script>");
            out.flush();
            return userProps.getUrls().getMypage().getCheckPassword2();
      }
   }

    @GetMapping("/emailAuthentication")
   public String emailAuthentication() {
        return userProps.getUrls().getEmail().getAuthentication();
   }

   // 이메일 중복확인, 이메일 발송
   @PostMapping("/email.do")
    public String doEmail(RedirectAttributes redirectAttributes, Model model,
                          HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String userEmail = request.getParameter(userProps.getParams().getEmail());

        // 이메일 값이 있으면 모델에 추가
        if (request.getParameter(userProps.getParams().getEmail()) != null) {
            model.addAttribute(userProps.getParams().getEmail(), userEmail);
            String address = userProps.getEmail().getDomain();
            userEmail = userEmail + address;
        }
        
        // 인증번호 값이 있으면 모델에 추가
        if (request.getParameter(userProps.getParams().getAuthNum()) != null) {
            model.addAttribute(userProps.getParams().getAuthNum(),
                    request.getParameter(userProps.getParams().getAuthNum()));
        }

        // 이메일 체크 (이메일 발송)
        if (request.getParameter(userProps.getParams().getEmailCheck()) != null && !userEmail.isEmpty()) {
            String result = userEmailService.processEmailCertification(userEmail, userProps.getFormats().getDatetime());
            
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            
            if ("duplicate".equals(result)) {
                out.println("<script>alert('이미 등록된 이메일 입니다.' );</script>");
         } else {
                out.println("<script>alert('성공적으로 이메일 발송이 완료되었습니다.' );</script>");
            }
            out.flush();
            return userProps.getUrls().getEmail().getAuthentication();
        }
        
        // 이메일이 비어있는 경우
        if (userEmail.isEmpty()) {
            return userProps.getUrls().getEmail().getAuthentication();
        }
        
        // 인증번호 확인
        if (request.getParameter(userProps.getParams().getEmailValid()) != null
                && !request.getParameter(userProps.getParams().getAuthNum()).isEmpty()) {
            boolean checker = userEmailService.verifyCertification(request.getParameter(userProps.getParams().getAuthNum()));
            
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
                return userProps.getUrls().getEmail().getAuthentication();
            }
        }

        // 동의 버튼 클릭
        Boolean emailChecker = (Boolean) session.getAttribute("emailChecker");
        if (request.getParameter(userProps.getParams().getBtnAgree()) != null && Boolean.TRUE.equals(emailChecker)) {
            session.removeAttribute("emailChecker");
            return userProps.getUrls().getSignup().getSelect();
        }
        
        return userProps.getUrls().getEmail().getAuthentication();
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
            return handleIdCheck(request, response, session, user, userProps.getUrls().getSignup().getStudent());
        }
        
        // 회원가입 제출
        Boolean idChecker = (Boolean) session.getAttribute("idChecker");
        if (request.getParameter("submitName") != null && Boolean.TRUE.equals(idChecker)) {
            // 입력값 검증
            String validationError = userService.validateStudentSignUp(studentColleges, studentMajor);
            if (validationError != null) {
                showAlert(response, validationError);
                return userProps.getUrls().getSignup().getStudent();
            }
            
            // 세션에서 저장된 이메일 가져오기
            String userEmail = (String) session.getAttribute("userEmail");
            user.setUserRole(userProps.getRoles().getStudent());
            
            // 회원가입 처리
            boolean isDoubleMajor = "Y".equals(request.getParameter("member"));
            userService.registerStudent(user, student, studentColleges, studentMajor, 
                                       studentDoubleMajor, userEmail, isDoubleMajor);
            
            clearSignUpSession(session);

            redirectAttributes.addFlashAttribute("msg", "signupERED");
            showAlert(response, "회원가입이 완료 되었습니다.");
            return userProps.getUrls().getSignin().getLogin();
        }
        
        return userProps.getUrls().getSignup().getStudent();
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
            return handleIdCheck(request, response, session, user, userProps.getUrls().getSignup().getProfessor());
        }
        
        // 회원가입 제출
        Boolean idChecker = (Boolean) session.getAttribute("idChecker");
        if (request.getParameter("submitName") != null && Boolean.TRUE.equals(idChecker)) {
            // 입력값 검증
            String validationError = userService.validateProfessorSignUp(professorColleges, professorMajor);
            if (validationError != null) {
                showAlert(response, validationError);
               return userProps.getUrls().getSignup().getProfessor();
            }
            
            // 세션에서 저장된 이메일 가져오기
            String userEmail = (String) session.getAttribute("userEmail");
            user.setUserRole(userProps.getRoles().getProfessor());
            
            // 회원가입 처리
            userService.registerProfessor(user, professor, professorColleges, professorMajor,
                                        professorRoom, professorRoomNum, userEmail);
            
            clearSignUpSession(session);

            redirectAttributes.addFlashAttribute("msg", "signupERED");
            showAlert(response, "회원가입이 완료 되었습니다.");
            return userProps.getUrls().getSignin().getLogin();
         }
        
         return userProps.getUrls().getSignup().getProfessor();
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
            user.setUserName(request.getParameter(userProps.getParams().getUserName()));
            
            if (userLoginID.isEmpty()) {
                showAlert(response, "계정을 입력하지 않으셨습니다.");
                return userProps.getUrls().getSignin().getFindPassword();
            }
            
            if (request.getParameter(userProps.getParams().getUserName()).isEmpty()) {
                model.addAttribute("UserLoginID", userLoginID);
                showAlert(response, "이름을 입력하지 않으셨습니다.");
                return userProps.getUrls().getSignin().getFindPassword();
            }
            
            boolean localIdChecker = this.userService.verifyUserForPasswordReset(user);
            
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(userProps.getParams().getUserName(),
                    request.getParameter(userProps.getParams().getUserName()));
            
            if (localIdChecker) {
                showAlert(response, "계정이 확인되었습니다.");
                session.setAttribute("findPwdIdChecker", true);
         } else {
                showAlert(response, "등록된 사용자가 아닙니다.");
                session.setAttribute("findPwdIdChecker", false);
            }
            return userProps.getUrls().getSignin().getFindPassword();
        }
        
        // 이메일 체크
        if (request.getParameter("EmailCheck") != null) {
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(userProps.getParams().getUserName(),
                    request.getParameter(userProps.getParams().getUserName()));
            
            if (userEmail.isEmpty()) {
                showAlert(response, "이메일을 입력하지 않으셨습니다.");
                return userProps.getUrls().getSignin().getFindPassword();
            }
            
            model.addAttribute("UserEmail", userEmail);
            String fullEmail = userEmail + "@mju.ac.kr";
            
            // 이메일 발송 처리
            String emailResult = emailService.sendEmailForPasswordReset(fullEmail);
            showAlert(response, emailResult);
            
            return userProps.getUrls().getSignin().getFindPassword();
        }
        
        // 인증번호 확인
        if (request.getParameter("EmailValid") != null) {
            model.addAttribute("UserLoginID", userLoginID);
            model.addAttribute(userProps.getParams().getUserName(),
                    request.getParameter(userProps.getParams().getUserName()));
            model.addAttribute("UserEmail", userEmail);
            
            boolean localNameChecker = emailService.AuthNum(request.getParameter(userProps.getParams().getAuthNum()));
            
            if (localNameChecker) {
                model.addAttribute(userProps.getParams().getAuthNum(),
                        request.getParameter(userProps.getParams().getAuthNum()));
                showAlert(response, "인증번호가 일치합니다.");
                session.setAttribute("findPwdNameChecker", true);
            } else {
                showAlert(response, "인증번호가 일치하지 않습니다.");
                session.setAttribute("findPwdNameChecker", false);
            }
            return userProps.getUrls().getSignin().getFindPassword();
        }
        
        // 최종 제출 (임시 비밀번호 생성)
        Boolean idChecker = (Boolean) session.getAttribute("findPwdIdChecker");
        Boolean nameChecker = (Boolean) session.getAttribute("findPwdNameChecker");
        
        if (request.getParameter("SubmitName") != null && Boolean.TRUE.equals(nameChecker) && Boolean.TRUE.equals(idChecker)) {
            user.setUserLoginID(userLoginID);
            user.setUserName(request.getParameter(userProps.getParams().getUserName()));
            
            // 임시 비밀번호 생성 및 업데이트
            String newPwd = userService.generateAndUpdateTemporaryPassword(user);
            model.addAttribute("UserLoginPwd", newPwd);
            
            session.removeAttribute("findPwdIdChecker");
            session.removeAttribute("findPwdNameChecker");

            return userProps.getUrls().getSignin().getShowPassword();
        }
        
        return userProps.getUrls().getSignin().getFindPassword();
   }

   /* 수정하기 전 비밀번호 확인 */
    @PostMapping("/checkPassword.do")
    public String checkPassword(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userLoginID = principal.getName();
        boolean checker = userService.selectForPwdCheckBeforeModify(userLoginID,
                request.getParameter(userProps.getParams().getUserLoginPwd()));
        
        if (!checker) {
            showAlert(response, "비밀번호를 다시 입력해주세요.");
            return userProps.getUrls().getMypage().getCheckPassword();
        }
        
        // 역할에 따른 리다이렉트 URL 반환
        return userService.getRedirectUrlByRole(userLoginID, 
                userProps.getRoles().getStudent(), 
                userProps.getRoles().getProfessor(),
                userProps.getRedirects().getMypageStudent(),
                userProps.getRedirects().getMypageProfessor());
   }

   // 비밀번호 변경할 때 현재 비번 체크
    @GetMapping("/checkPassword3")
   public String checkPassword3() {
        return userProps.getUrls().getMypage().getCheckPassword3();
   }

    @PostMapping("/checkPassword3.do")
    public String checkPassword3(HttpServletResponse response, HttpServletRequest request, Principal principal)
         throws IOException {
        String userID = principal.getName();
        boolean checker = userService.selectForPwdCheckBeforeModify(userID,
                request.getParameter(userProps.getParams().getUserLoginPwd()));
        
        if (!checker) {
            showAlert(response, "비밀번호를 다시 입력해주세요.");
            return userProps.getUrls().getMypage().getCheckPassword3();
        }
        
        return userProps.getRedirects().getModifyPassword();
   }

   // 비밀번호 수정
    @PostMapping("/modifyPassword.do")
    public String modifyPassword(HttpServletResponse response, HttpServletRequest request,
                                 Principal principal) throws IOException {
        String userLoginID = principal.getName();
        String newPassword = request.getParameter(userProps.getParams().getUserNewPwd());
        String confirmPassword = request.getParameter(userProps.getParams().getUserNewPwdCheck());
        
      // 새로입력한 비밀번호와 확인 비밀번호가 일치하면
        if (newPassword.equals(confirmPassword)) {
            userService.updatePassword(userLoginID, newPassword);
        }

        return userProps.getUrls().getMypage().getModifyPassword();
   }

    // 이메일 로그인 화면
    @GetMapping("/email/emailLogin")
    public String emailLogin() {
        return userProps.getUrls().getEmail().getList();
   }

   @PostMapping("/email/emailList")
    public String doEmailLogin(HttpServletRequest request, Model model, Principal principal, User user,
         RedirectAttributes rttr) {
      // 유저 정보
        getUserInformation(principal, user, model);

        // 이메일 로그인 확인
        String id = request.getParameter("EmailLoginID") + userProps.getEmail().getDomain();
        boolean checkId = emailService.CheckEmailLogin(id, request.getParameter(userProps.getParams().getEmailLoginPwd()));

        if (!checkId) {
         rttr.addFlashAttribute("Checker", "LoginFail");
            return userProps.getRedirects().getEmailLogin();
      }
        
        return userProps.getRedirects().getEmailList();
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
        
        return userProps.getUrls().getEmail().getList();
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
        
        return userProps.getUrls().getEmail().getContent();
    }

    private void getUserInformation(Principal principal, User user, Model model) {
        String loginID = principal.getName();// 로그인 한 아이디
        ArrayList<String> selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
        user.setUserLoginID(loginID);

        if (selectUserProfileInfo.get(2).equals(userProps.getRoles().getStudent())) {
            ArrayList<String> studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));
         userInfoMethod.studentInfo(model, selectUserProfileInfo, studentInfo);
        } else if (selectUserProfileInfo.get(2).equals(userProps.getRoles().getProfessor())) {
            ArrayList<String> professorInfo = professorService.selectProfessorProfileInfo(selectUserProfileInfo.get(1));
         userInfoMethod.professorInfo(model, selectUserProfileInfo, professorInfo);
        } else if (selectUserProfileInfo.get(2).equals(userProps.getRoles().getAdministrator())) {
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
        if (request.getParameter(userProps.getParams().getUserLoginPwd()) != null) {
            model.addAttribute(userProps.getParams().getUserLoginPwd(), request.getParameter(userProps.getParams().getUserLoginPwd()));
        }
        if (request.getParameter(userProps.getParams().getStudentName()) != null) {
            model.addAttribute(userProps.getParams().getStudentName(), request.getParameter(userProps.getParams().getStudentName()));
        }
        if (request.getParameter("StudentGender") != null) {
            model.addAttribute("StudentGender", studentGender);
        }
        if (request.getParameter("UserPhoneNum") != null) {
            model.addAttribute(userProps.getParams().getUserPhoneNum(),
                    request.getParameter(userProps.getParams().getUserPhoneNum()));
        }
        if (request.getParameter(userProps.getParams().getStudentNum()) != null) {
            model.addAttribute(userProps.getParams().getStudentNum(), request.getParameter(userProps.getParams().getStudentNum()));
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
        if (request.getParameter(userProps.getParams().getUserLoginPwd()) != null) {
            model.addAttribute(userProps.getParams().getUserLoginPwd(), request.getParameter(userProps.getParams().getUserLoginPwd()));
        }
        if (request.getParameter(userProps.getParams().getProfessorName()) != null) {
            model.addAttribute(userProps.getParams().getProfessorName(), request.getParameter(userProps.getParams().getProfessorName()));
        }
        if (request.getParameter("UserPhoneNum") != null) {
            model.addAttribute(userProps.getParams().getUserPhoneNum(),
                    request.getParameter(userProps.getParams().getUserPhoneNum()));
        }
        if (request.getParameter(userProps.getParams().getProfessorNum()) != null) {
            model.addAttribute(userProps.getParams().getProfessorNum(), request.getParameter(userProps.getParams().getProfessorNum()));
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
