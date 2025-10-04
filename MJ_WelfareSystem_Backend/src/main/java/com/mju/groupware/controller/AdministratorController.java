package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserList;
import com.mju.groupware.service.AdminService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdministratorController { //TODO : 비즈니스 로직을 서비스 코드로 옮길 것.
    private final UserInfoMethod userInfoMethod;
    private final AdminService adminService;
    private final UserService userService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ConstantAdmin constantAdmin;

    // 관리자메뉴 - user list
    @GetMapping("/manageList")
    public String manageList(Model model, User user) {
        // GlobalUserModelAdvice에서 사용자 정보를 주입
        try {
            List<UserList> SelectUserList = adminService.SelectUserlist();
            model.addAttribute("list", SelectUserList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.constantAdmin.getList();
    }

    // 관리자메뉴 - 관리자 권한으로 user 권한 변경
    @ResponseBody
    @RequestMapping(value = "/manageList.do")
    public String changeAuth(
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) {
        String OptionValue = request.getParameter("OptionValue");
        String[] AjaxMsg = request.getParameterValues("CheckArr");

        String ARole = this.constantAdmin.getARole();
        String URole = this.constantAdmin.getROLE_USER();

        if (OptionValue.equals(this.constantAdmin.getROLE_SUSER())) {
            OptionValue = URole;
        } else if (OptionValue.equals(this.constantAdmin.getROLE_PUSER())) {
            OptionValue = URole;
        } else if (OptionValue.equals(this.constantAdmin.getROLE_ADMIN())) {
            OptionValue = ARole;
        }
        for (int i = 0; i < AjaxMsg.length; i++) {
            if (OptionValue.equals(ARole)) {
                userService.UpdateAdminRole(AjaxMsg[i], OptionValue);
            } else if (OptionValue.equals(URole)) {
                userService.UpdateUserRole(AjaxMsg[i], OptionValue);
            }
        }
        return this.constantAdmin.getList();
    }

    // 관리자 메뉴 - 관리자 권한으로 user 탈퇴
    @ResponseBody
    @RequestMapping(value = "/withdrawal.do")
    public String DoWithdrawlByAdmin(HttpServletRequest request, User user, Student student, Professor professor) {

        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (int i = 0; i < AjaxMsg.length; i++) {
            User UserInfo = userService.SelectUserInfo(AjaxMsg[i]);
            user.setUserLoginID(UserInfo.getUserLoginID());
            user.setEnabled(false);
            user.setWithdrawal(true);
            // 탈퇴한 날짜 set
            Date Now = new Date();
            SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");
            user.setDate(Date.format(Now));
            // 탈퇴 업데이트
            userService.UpdateWithdrawal(user);

        }
        return this.constantAdmin.getReList();
    }

    // 관리자 휴면 메뉴 - 관리자 권한으로 휴면 계정 탈퇴
    @ResponseBody
    @RequestMapping(value = "/dormantWithdrawal.do")
    public String DoDormantWithdrawalByAdmin(HttpServletRequest request, User user, Student student,
                                             Professor professor) {
        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (int i = 0; i < AjaxMsg.length; i++) {
            userService.UpdateWithdrawalByDormant(AjaxMsg[i]);
        }
        return this.constantAdmin.getReSleep();
    }

    /* 관리자 메뉴-휴면 계정 관리 화면 */
    @GetMapping("/manageSleep")
    public String manageSleep(Model model, Principal principal, User user) {
//        userInfoMethod.GetUserInformation(
//                principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

        try {
            List<UserList> SelectDormantUserList = adminService.SelectDormantUserList();
            model.addAttribute("DormantList", SelectDormantUserList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.constantAdmin.getSleepList();
    }

    // 관리자 휴면 메뉴 - 관리자 권한으로 휴면 계정 복구
    @ResponseBody
    @RequestMapping(value = "/dormantRecovery.do")
    public String DoDormantRecoveryByAdmin(HttpServletRequest request) {

        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (int i = 0; i < AjaxMsg.length; i++) {
            // 쿼리문 작동
            userService.UpdateDormantOneToZero(AjaxMsg[i]);
        }
        return this.constantAdmin.getReSleep();
    }

    /* 관리자 메뉴-탈퇴 계정 관리 화면 */
    @GetMapping("/manageSecession")
    public String manageSecession(Model model, Principal principal, User user) {
//        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

        try {
            List<UserList> SelectWithdrawalUserList = adminService.SelectWithdrawalUserList();
            model.addAttribute("SelectWithdrawalUserList", SelectWithdrawalUserList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.constantAdmin.getSecessionList();
    }

    // 관리자 탈퇴 메뉴 - 관리자 권한으로 탈퇴 계정 복구
    @ResponseBody
    @RequestMapping(value = "/withdrawalRecovery.do")
    public String DoWithdrawalRecoveryByAdmin(HttpServletRequest request, User user) {
        String[] AjaxMsg = request.getParameterValues("CheckArr");
        int Size = AjaxMsg.length;
        for (int i = 0; i < Size; i++) {
            userService.UpdateDoWithdrawalRecoveryByAdmin(AjaxMsg[i]);
        }
        return this.constantAdmin.getSecessionList();
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 회원 role에 따라 페이지 리턴
    @GetMapping("/detail")
    public String detail(HttpServletRequest request, Model model, HttpServletResponse response, RedirectAttributes rttr) {
        String userId = request.getParameter("no");
        String MysqlRole = request.getParameter("R");
        String UserAuthority = request.getParameter("A");

        String UAuthority = this.constantAdmin.getROLE_USER();
        String AAuthority = this.constantAdmin.getROLE_ADMIN();

        String SRole = this.constantAdmin.getSRole();
        String PRole = this.constantAdmin.getPRole();

        String ReSDetail = this.constantAdmin.getReSDetail();
        String RePDetail = this.constantAdmin.getRePDetail();

        if (MysqlRole.equals(SRole) && UserAuthority.equals(UAuthority)) {
            rttr.addAttribute("no", userId);
            return ReSDetail;
        } else if (MysqlRole.equals(PRole) && UserAuthority.equals(UAuthority)) {
            rttr.addAttribute("no", userId);
            return RePDetail;
        } else if (UserAuthority.equals(AAuthority)) {
            rttr.addFlashAttribute("DONT", "true");
            return this.constantAdmin.getReList();
        } else {
            return this.constantAdmin.getReList();
        }
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 학생 정보 출력
    @RequestMapping(value = "/detailStudent", method = RequestMethod.GET)
    public String detailStudent(
            HttpServletRequest request, Model model, Principal principal, User user, @RequestParam("no") String no) {
        userInfoMethod.GetUserInformation(
                principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());
        ArrayList<String> SelectUserProfileInfo = userService.SelectUserProfileInfoByID(no);
        // 학번추가
        model.addAttribute("UserLoginID", SelectUserProfileInfo.get(0));
        // 이름
        model.addAttribute("SUserName", SelectUserProfileInfo.get(1));
        // 성별
        model.addAttribute("StudentGender", SelectUserProfileInfo.get(10));
        // 연락처
        model.addAttribute("UserPhoneNum", SelectUserProfileInfo.get(2));
        // 학년
        model.addAttribute("StudentGrade", SelectUserProfileInfo.get(8));
        // 단과대학
        model.addAttribute("StudentColleges", SelectUserProfileInfo.get(6));
        // 전공
        model.addAttribute("StudentMajor", SelectUserProfileInfo.get(7));
        // 부전공
        model.addAttribute("StudentDoubleMajor", SelectUserProfileInfo.get(9));
        // 이메일
        model.addAttribute("UserEmail", SelectUserProfileInfo.get(3));

        // 정보공개여부
        String Result = "Error";
        Result = SelectUserProfileInfo.get(4) + "," + SelectUserProfileInfo.get(5);
        if (Result.contains(",비공개") || Result.contains("비공개")) {
            Result = Result.replaceAll(",비공개", "");
            Result = Result.replaceAll("비공개", "");
            boolean startComma = Result.startsWith(",");
            boolean endComma = Result.endsWith(",");
            if (startComma || endComma) {
                Result = Result.substring(Result.length() - (Result.length() - 1), Result.length());
            }
        }
        if (!Result.equals("Error")) {
            model.addAttribute("StudentInfoOpen", Result);
        }
        return this.constantAdmin.getSDetail();

    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 교수 정보 출력
    @RequestMapping(value = "/detailProfessor", method = RequestMethod.GET)
    public String detailProfessor(HttpServletRequest request, Model model, Principal principal, User user, @RequestParam("no") String no) {
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());
        ArrayList<String> SelectUserProfileInfo = userService.SelectUserProfileInfoByID(no);
        // 학번추가
        model.addAttribute("UserLoginID", SelectUserProfileInfo.get(0));
        // 이름
        model.addAttribute("PUserName", SelectUserProfileInfo.get(1));
        // 연락처
        model.addAttribute(this.constantAdmin.getUserPhoneNum(), SelectUserProfileInfo.get(2));
        // 단과대학
        model.addAttribute("ProfessorColleges", SelectUserProfileInfo.get(11));
        // 전공
        model.addAttribute("ProfessorMajor", SelectUserProfileInfo.get(12));
        // 교수실
        model.addAttribute("ProfessorRoom", SelectUserProfileInfo.get(13));
        // 교수실 전화번호
        model.addAttribute(this.constantAdmin.getProfessorRoomNum(), SelectUserProfileInfo.get(14));
        // 이메일
        model.addAttribute(this.constantAdmin.getUserEmail(), SelectUserProfileInfo.get(3));

        // 정보공개여부
        String Result = "Error";
        Result = SelectUserProfileInfo.get(4);
        if (Result.contains(",비공개") || Result.contains("비공개")) {
            Result = Result.replaceAll(",비공개", "");
            Result = Result.replaceAll("비공개", "");
            boolean startComma = Result.startsWith(",");
            boolean endComma = Result.endsWith(",");
            if (startComma || endComma) {
                Result = Result.substring(Result.length() - (Result.length() - 1), Result.length());
            }
        }
        if (!Result.equals("Error")) {
            model.addAttribute("ProfessorInfoOpen", Result);
        }
        return this.constantAdmin.getPDetail();
    }

    @RequestMapping(value = "/ModifyStudent", method = RequestMethod.POST)
    public String UpdateStudentInfo() {
        return this.constantAdmin.getSModify();
    }

    @RequestMapping(value = "/ModifyProfessor", method = RequestMethod.POST)
    public String UpdateProfessorInfo() {
        return this.constantAdmin.getPModify();
    }

    /* 관리자 메뉴-회원 목록 클릭 시 정보 출력 화면 */
    @RequestMapping(value = "/manageStudent", method = RequestMethod.GET)
    public String manageStudent() {
        return this.constantAdmin.getSManage();
    }

    @RequestMapping(value = "/manageProfessor", method = RequestMethod.GET)
    public String manageProfessor() {
        return this.constantAdmin.getPManage();
    }

    @RequestMapping(value = "/manageModifyStudent", method = RequestMethod.GET)
    public String manageModifyStudent(HttpServletRequest request, Model model) {
        String LoginID = request.getParameter("no");

        User user = userService.SelectModifyUserInfo(LoginID);
        model.addAttribute("UserLoginID", LoginID);
        String UserEmail = user.getUserEmail();
        int Location = UserEmail.indexOf("@");
        UserEmail = UserEmail.substring(0, Location);
        model.addAttribute(this.constantAdmin.getEmail(), UserEmail);
        // 연락처 공개
        model.addAttribute("OpenPhoneNum", user.getOpenPhoneNum());
        // 학년 공개
        model.addAttribute("OpenGrade", user.getOpenGrade());
        return this.constantAdmin.getSManageModify();
    }

    @RequestMapping(value = "/manageModifyStudent", method = RequestMethod.POST)
    public String DoManageModifyStudent(Principal principal, HttpServletRequest request, User user, Student student, @RequestParam("no") String no) {
        user.setUserLoginID(principal.getName());
        student.setUserID(Integer.parseInt(no));
        if (request.getParameter("ModifyComplete") != null) {
            if (!((String) request.getParameter("UserName")).isEmpty()) {
                // 이름바꾸기
                user.setUserName(request.getParameter("UserName"));
                userService.UpdateUserName(user);
            }
            if (!((String) request.getParameter("StudentGender")).equals(" ")) {
                // 성별바꾸기
                student.setStudentGender((String) request.getParameter("StudentGender"));
                studentService.UpdateStudentGender(student);
            }
            if (!((String) request.getParameter("UserPhoneNum")).isEmpty()) {
                // 전화번호바꾸기
                user.setUserPhoneNum((String) request.getParameter("UserPhoneNum"));
                userService.updateUserPhoneNumber(user);
            }
            if (!((String) request.getParameter("StudentGrade")).equals(" ")) {
                // 학년
                student.setStudentGrade((String) request.getParameter("StudentGrade"));
                studentService.updateStudentGrade(student);
            }
            if (!((String) request.getParameter("StudentColleges")).isEmpty()) {
                // 단과대학
                student.setStudentColleges((String) request.getParameter("StudentColleges"));
                studentService.UpdateStudentColleges(student);
            }
            if (!((String) request.getParameter("StudentMajor")).isEmpty()) {
                // 전공
                student.setStudentMajor((String) request.getParameter("StudentMajor"));
                studentService.UpdateStudentMajor(student);
            }

            if (((String) request.getParameter("member")).equals("Y")
                    && !request.getParameter("StudentDoubleMajor").equals(" ")) {
                // 부전공 있다
                student.setStudentDoubleMajor((String) request.getParameter("StudentDoubleMajor"));
                studentService.UpdateStudentDobuleMajor(student);
            } else if (((String) request.getParameter("member")).equals("N")) {
                // 부전공 없다
                student.setStudentDoubleMajor("없음");
                studentService.UpdateStudentDobuleMajor(student);
            }

            if (request.getParameter("UserPhone") != null) {
                String OpenPhoneNum = "전화번호";
                user.setOpenPhoneNum(OpenPhoneNum);
                userService.UpdateOpenPhoneNum(user);
            } else if (request.getParameter("UserPhone") == null) {
                String NotOpen = "비공개";
                user.setOpenPhoneNum(NotOpen);
                userService.UpdateOpenPhoneNum(user);
            }

            if (request.getParameter("UserGrade") != null) {
                String OpenGrade = "학년";
                user.setOpenGrade(OpenGrade);
                userService.UpdateOpenGrade(user);
            } else if (request.getParameter("UserGrade") == null) {
                String NotOpen = "비공개";
                user.setOpenGrade(NotOpen);
                userService.UpdateOpenGrade(user);
            }
        }
        return this.constantAdmin.getSManageModify();
    }

    @RequestMapping(value = "/manageModifyProfessor", method = RequestMethod.GET)
    public String manageModifyProfessor(Model model, HttpServletRequest request) {
        String LoginID = request.getParameter("no");

        User UserInfo = userService.SelectModifyUserInfo(LoginID);
        model.addAttribute("UserLoginID", LoginID);
        String UserEmail = UserInfo.getUserEmail();
        int Location = UserEmail.indexOf("@");
        UserEmail = UserEmail.substring(0, Location);
        model.addAttribute("Email", UserEmail);
        // 연락처 공개
        model.addAttribute("OpenPhoneNum", UserInfo.getOpenPhoneNum());

        return this.constantAdmin.getPManageModify();
    }

    @RequestMapping(value = "/manageModifyProfessor", method = RequestMethod.POST)
    public String DoManageModifyProfessor(Principal principal, HttpServletRequest request, User user, Professor professor, @RequestParam("no") String no) {
        user.setUserLoginID(principal.getName());
        professor.setUserID(Integer.parseInt(no));
        if (request.getParameter("ModifyCompleteP") != null) {
            if (!((String) request.getParameter("UserName")).isEmpty()) {
                // 이름바꾸기
                user.setUserName((String) request.getParameter("UserName"));
                userService.UpdateUserName(user);
            }
            if (!((String) request.getParameter("UserPhoneNum")).isEmpty()) {
                // 전화번호바꾸기
                user.setUserPhoneNum((String) request.getParameter("UserPhoneNum"));
                userService.updateUserPhoneNumber(user);
            }
            if (!((String) request.getParameter("ProfessorColleges")).isEmpty()) {
                // 단과대학
                professor.setProfessorColleges((String) request.getParameter("ProfessorColleges"));
                professorService.UpdateProfessorColleges(professor);
            }
            if (!((String) request.getParameter("ProfessorMajor")).isEmpty()) {
                // 전공
                professor.setProfessorMajor((String) request.getParameter("ProfessorMajor"));
                professorService.UpdateProfessorMajor(professor);
            }
            if (!((String) request.getParameter("ProfessorRoom")).isEmpty()) {
                // 교수실
                professor.setProfessorRoom((String) request.getParameter("ProfessorRoom"));
                professorService.UpdateProfessorRoom(professor);
            }
            if (!((String) request.getParameter("ProfessorRoomNum")).isEmpty()) {
                // 교수실 전화번호
                professor.setProfessorRoomNum((String) request.getParameter("ProfessorRoomNum"));
                professorService.UpdateProfessorRoomNum(professor);
            }
            if (request.getParameter("UserPhone") != null) {
                String OpenPhoneNum = "전화번호";
                user.setOpenPhoneNum(OpenPhoneNum);
                userService.UpdateOpenPhoneNum(user);
            } else if (request.getParameter("UserPhone") == null) {
                String NotOpen = "비공개";
                user.setOpenPhoneNum(NotOpen);
                userService.UpdateOpenPhoneNum(user);
            }

        }
        return this.constantAdmin.getPManageModify();

    }
}