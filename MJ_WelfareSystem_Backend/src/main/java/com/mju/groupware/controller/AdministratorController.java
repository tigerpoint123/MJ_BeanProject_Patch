package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserList;
import com.mju.groupware.service.AdminService;
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
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdministratorController { //TODO : 비즈니스 로직을 서비스 코드로 옮긴 다음
    private final UserInfoMethod userInfoMethod;
    private final AdminService adminService;
    private final UserService userService;
    private final ConstantAdmin constantAdmin;

    // 관리자메뉴 - user list
    @GetMapping("/manageList")
    public String manageList(Model model, User user, Principal principal) {
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

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
        String ARole = this.constantAdmin.getARole();
        String URole = this.constantAdmin.getROLE_USER();
        boolean result = this.adminService.changeUserRole(request, ARole, URole);

        return this.constantAdmin.getList();
//        return result ? "1" : "0"; 서비스 결과에 따라 view 파일을 다르게 할 경우 사용할 것.
    }

    // 관리자 메뉴 - 관리자 권한으로 user 탈퇴
    @ResponseBody
    @RequestMapping(value = "/withdrawal.do")
    public String DoWithdrawlByAdmin(HttpServletRequest request, User user, Student student, Professor professor) {
        boolean result = this.adminService.deleteUserInfo(request);

        return this.constantAdmin.getReList();
//        return result ? "1" : "0"; 서비스 결과에 따라 view 파일을 다르게 할 경우 사용할 것.
    }

    // 관리자 휴면 메뉴 - 관리자 권한으로 휴면 계정 탈퇴
    @ResponseBody
    @RequestMapping(value = "/dormantWithdrawal.do")
    public String DoDormantWithdrawalByAdmin(HttpServletRequest request) {
        adminService.dormantUserDelete(request);
        return this.constantAdmin.getReSleep();
    }

    /* 관리자 메뉴-휴면 계정 관리 화면 */
    @GetMapping("/manageSleep")
    public String manageSleep(Model model, Principal principal, User user) {
        userInfoMethod.GetUserInformation(
                principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

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
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());
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
        for (String msg : AjaxMsg) {
            userService.UpdateDoWithdrawalRecoveryByAdmin(msg);
        }
        return this.constantAdmin.getSecessionList();
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 회원 role에 따라 페이지 리턴
    @GetMapping("/detail")
    public String detail(HttpServletRequest request, Model model, HttpServletResponse response, RedirectAttributes rttr) {
        String returnPage = this.adminService.getDetailPage(request, rttr);

        return returnPage;
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 학생 정보 출력
    @GetMapping("/detailStudent")
    public String detailStudent(Model model, @RequestParam("no") String no, Principal principal, User user) {
        userInfoMethod.GetUserInformation(
                principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

        boolean result = adminService.detailStudent(model, no);

        return this.constantAdmin.getSDetail();
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 교수 정보 출력
    @GetMapping("/detailProfessor")
    public String detailProfessor(HttpServletRequest request, Model model, Principal principal, User user, @RequestParam("no") String no) {
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdmin.getSTUDENT(), this.constantAdmin.getPROFESSOR(), this.constantAdmin.getADMINISTRATOR());

        boolean result = adminService.detailProfessor(model, no);

        return this.constantAdmin.getPDetail();
    }

    @PostMapping("/ModifyStudent")
    public String UpdateStudentInfo() {
        return this.constantAdmin.getSModify();
    }

    @PostMapping("/ModifyProfessor")
    public String UpdateProfessorInfo() {
        return this.constantAdmin.getPModify();
    }

    /* 관리자 메뉴-회원 목록 클릭 시 정보 출력 화면 */
    @GetMapping("/manageStudent")
    public String manageStudent() {
        return this.constantAdmin.getSManage();
    }

    @GetMapping("/manageProfessor")
    public String manageProfessor() {
        return this.constantAdmin.getPManage();
    }

    @GetMapping("/manageModifyStudent")
    public String manageModifyStudent(HttpServletRequest request, Model model) {
        adminService.modifyStudentList(request, model);

        return this.constantAdmin.getSManageModify();
    }

    @PostMapping("/manageModifyStudent")
    public String DoManageModifyStudent(Principal principal, HttpServletRequest request, User user, Student student, @RequestParam("no") String no) {
        adminService.modifyStudent(principal, request, no);

        return this.constantAdmin.getSManageModify();
    }

    @GetMapping("/manageModifyProfessor")
    public String manageModifyProfessor(Model model, HttpServletRequest request, @RequestParam("no") String loginID) {
        adminService.modifyProfessorList(model, request, loginID);

        return this.constantAdmin.getPManageModify();
    }

    @PostMapping("/manageModifyProfessor")
    public String DoManageModifyProfessor(Principal principal, HttpServletRequest request, User user, Professor professor, @RequestParam("no") String no) {
        adminService.modifyProfessor(principal, request, no);

        return this.constantAdmin.getPManageModify();
    }
}