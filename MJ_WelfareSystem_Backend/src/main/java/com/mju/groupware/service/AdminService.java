package com.mju.groupware.service;

import com.mju.groupware.dto.UserList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

public interface AdminService {

    List<UserList> SelectUserlist() ;

    List<UserList> SelectDormantUserList();

    List<UserList> SelectWithdrawalUserList();

    boolean detailStudent(Model model, String no);

    boolean changeUserRole(HttpServletRequest request, String ARole, String URole);

    boolean deleteUserInfo(HttpServletRequest request);

    String getDetailPage(HttpServletRequest request, RedirectAttributes rttr);

    boolean detailProfessor(Model model, String no);

    void modifyStudentList(HttpServletRequest request, Model model);

    void modifyStudent(Principal principal, HttpServletRequest request, String no);

    void modifyProfessorList(Model model, HttpServletRequest request, String loginID);

    void modifyProfessor(Principal professor, HttpServletRequest request, String no);

    void dormantUserDelete(HttpServletRequest request);

    void deleteUserRollback(HttpServletRequest request);

    void manageSession(Model model);

    void dormantUserRollback(HttpServletRequest request);
}
