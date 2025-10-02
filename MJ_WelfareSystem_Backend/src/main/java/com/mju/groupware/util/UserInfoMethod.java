package com.mju.groupware.util;

import java.util.ArrayList;
import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.mju.groupware.dto.User;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;

@Component
@RequiredArgsConstructor
public class UserInfoMethod {
    private final UserService userService;
    private final StudentService studentService;
    private final ProfessorService professorService;

    public void StudentInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> studentInfo) {
        // 학생 이름
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
        // 학생 소속
        model.addAttribute("Colleges", studentInfo.get(0));
        // 학생 전공
        model.addAttribute("UserMajor", studentInfo.get(1));
        // user role
        model.addAttribute("UserRole", selectUserProfileInfo.get(2));
    }

    public void ProfessorInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> professorInfo) {
        // 교수 이름
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
        // 교수 소속
        model.addAttribute("Colleges", professorInfo.get(0));
        // 교수 전공
        model.addAttribute("UserMajor", professorInfo.get(1));
        // user role
        model.addAttribute("UserRole", selectUserProfileInfo.get(2));
    }

    public void AdministratorInfo(Model model, ArrayList<String> selectUserProfileInfo) {
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
    }

    public void GetUserInformation(
            Principal principal, User user, Model model, String sRole, String pRole, String aRole
    ) {
        String LoginID = principal.getName();
        ArrayList<String> SelectUserProfileInfo = userService.SelectUserProfileInfo(LoginID);
        user.setUserLoginID(LoginID);
        if (!SelectUserProfileInfo.isEmpty()) {
            user.setUserName(SelectUserProfileInfo.get(0));
        }
        if (SelectUserProfileInfo.get(2).equals(sRole)) {
            ArrayList<String> StudentInfo = studentService.SelectStudentProfileInfo(SelectUserProfileInfo.get(1));
            StudentInfo(model, SelectUserProfileInfo, StudentInfo);
        } else if (SelectUserProfileInfo.get(2).equals(pRole)) {
            ArrayList<String> ProfessorInfo = professorService.SelectProfessorProfileInfo(SelectUserProfileInfo.get(1));
            ProfessorInfo(model, SelectUserProfileInfo, ProfessorInfo);
        } else if (SelectUserProfileInfo.get(2).equals(aRole)) {
            AdministratorInfo(model, SelectUserProfileInfo);
        }
    }
}
