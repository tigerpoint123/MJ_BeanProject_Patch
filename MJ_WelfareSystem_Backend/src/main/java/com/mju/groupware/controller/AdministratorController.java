package com.mju.groupware.controller;

import global.properties.AdminProperties;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserList;
import com.mju.groupware.service.AdminService;
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
    private final AdminProperties adminProps;

    // 관리자메뉴 - user list
    @GetMapping("/manageList")
    public String manageList(Model model, User user, Principal principal) {
        userInfoMethod.getUserInformation(principal, user, model, 
            adminProps.getRoles().getStudent(), 
            adminProps.getRoles().getProfessor(), 
            adminProps.getRoles().getAdministrator());
        List<UserList> SelectUserList = adminService.SelectUserlist();
        model.addAttribute("list", SelectUserList);
        return adminProps.getUrls().getList();
    }

    // 관리자메뉴 - 관리자 권한으로 user 권한 변경
    @ResponseBody
    @RequestMapping(value = "/manageList.do")
    public String changeAuth(
            RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) {
        String aRole = adminProps.getRoles().getAdministrator();
        String uRole = adminProps.getRoles().getRoleUser();
        boolean result = this.adminService.changeUserRole(request, aRole, uRole);

        return adminProps.getUrls().getList();
//        return result ? "1" : "0"; 서비스 결과에 따라 view 파일을 다르게 할 경우 사용할 것.
    }

    // 관리자 메뉴 - 관리자 권한으로 user 탈퇴
    @ResponseBody
    @RequestMapping(value = "/withdrawal.do")
    public String DoWithdrawlByAdmin(HttpServletRequest request, User user, Student student, Professor professor) {
        boolean result = this.adminService.deleteUserInfo(request);

        return adminProps.getRedirects().getList();
//        return result ? "1" : "0"; 서비스 결과에 따라 view 파일을 다르게 할 경우 사용할 것.
    }

    // 관리자 휴면 메뉴 - 관리자 권한으로 휴면 계정 탈퇴
    @ResponseBody
    @RequestMapping(value = "/dormantWithdrawal.do")
    public String DoDormantWithdrawalByAdmin(HttpServletRequest request) {
        adminService.dormantUserDelete(request);
        return adminProps.getRedirects().getSleep();
    }

    /* 관리자 메뉴-휴면 계정 관리 화면 */
    @GetMapping("/manageSleep")
    public String manageSleep(Model model, Principal principal, User user) {
        userInfoMethod.getUserInformation(
                principal, user, model, 
                adminProps.getRoles().getStudent(), 
                adminProps.getRoles().getProfessor(), 
                adminProps.getRoles().getAdministrator());

        List<UserList> SelectDormantUserList = adminService.SelectDormantUserList();
        model.addAttribute("DormantList", SelectDormantUserList);
        return adminProps.getUrls().getSleepList();
    }

    // 관리자 휴면 메뉴 - 관리자 권한으로 휴면 계정 복구
    @ResponseBody
    @RequestMapping(value = "/dormantRecovery.do")
    public String DoDormantRecoveryByAdmin(HttpServletRequest request) {
        adminService.dormantUserRollback(request);

        return adminProps.getRedirects().getSleep();
    }

    /* 관리자 메뉴-탈퇴 계정 관리 화면 */
    @GetMapping("/manageSecession")
    public String manageSecession(Model model, Principal principal, User user) {
        userInfoMethod.getUserInformation(principal, user, model, 
            adminProps.getRoles().getStudent(), 
            adminProps.getRoles().getProfessor(), 
            adminProps.getRoles().getAdministrator());

        List<UserList> SelectWithdrawalUserList = adminService.SelectWithdrawalUserList();
        model.addAttribute("SelectWithdrawalUserList", SelectWithdrawalUserList);
        return adminProps.getUrls().getSecessionList();
    }

    // 관리자 탈퇴 메뉴 - 관리자 권한으로 탈퇴 계정 복구
    @ResponseBody
    @RequestMapping(value = "/withdrawalRecovery.do")
    public String DoWithdrawalRecoveryByAdmin(HttpServletRequest request) {
        adminService.deleteUserRollback(request);
        return adminProps.getUrls().getSecessionList();
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
        userInfoMethod.getUserInformation(
                principal, user, model, 
                adminProps.getRoles().getStudent(), 
                adminProps.getRoles().getProfessor(), 
                adminProps.getRoles().getAdministrator());

        boolean result = adminService.detailStudent(model, no);

        return adminProps.getUrls().getStudent().getDetail();
    }

    // 관리자 메뉴에서 회원 아이디, 이름 클릭 시 교수 정보 출력
    @GetMapping("/detailProfessor")
    public String detailProfessor(HttpServletRequest request, Model model, Principal principal, User user, @RequestParam("no") String no) {
        userInfoMethod.getUserInformation(principal, user, model, 
            adminProps.getRoles().getStudent(), 
            adminProps.getRoles().getProfessor(), 
            adminProps.getRoles().getAdministrator());

        boolean result = adminService.detailProfessor(model, no);

        return adminProps.getUrls().getProfessor().getDetail();
    }

    @PostMapping("/ModifyStudent")
    public String UpdateStudentInfo() {
        return adminProps.getUrls().getStudent().getModify();
    }

    @PostMapping("/ModifyProfessor")
    public String UpdateProfessorInfo() {
        return adminProps.getUrls().getProfessor().getModify();
    }

    /* 관리자 메뉴-회원 목록 클릭 시 정보 출력 화면 */
    @GetMapping("/manageStudent")
    public String manageStudent() {
        return adminProps.getUrls().getStudent().getManage();
    }

    @GetMapping("/manageProfessor")
    public String manageProfessor() {
        return adminProps.getUrls().getProfessor().getManage();
    }

    @GetMapping("/manageModifyStudent")
    public String manageModifyStudent(HttpServletRequest request, Model model) {
        adminService.modifyStudentList(request, model);

        return adminProps.getUrls().getStudent().getManageModify();
    }

    @PostMapping("/manageModifyStudent")
    public String DoManageModifyStudent(Principal principal, HttpServletRequest request, User user, Student student, @RequestParam("no") String no) {
        adminService.modifyStudent(principal, request, no);

        return adminProps.getUrls().getStudent().getManageModify();
    }

    @GetMapping("/manageModifyProfessor")
    public String manageModifyProfessor(Model model, HttpServletRequest request, @RequestParam("no") String loginID) {
        adminService.modifyProfessorList(model, request, loginID);

        return adminProps.getUrls().getProfessor().getManageModify();
    }

    @PostMapping("/manageModifyProfessor")
    public String DoManageModifyProfessor(Principal principal, HttpServletRequest request, User user, Professor professor, @RequestParam("no") String no) {
        adminService.modifyProfessor(principal, request, no);

        return adminProps.getUrls().getProfessor().getManageModify();
    }
}