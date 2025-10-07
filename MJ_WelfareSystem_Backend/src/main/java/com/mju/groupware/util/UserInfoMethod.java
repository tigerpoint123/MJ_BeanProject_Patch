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

    public void studentInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> studentInfo) {
        // 학생 이름
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
        // 학생 소속
        model.addAttribute("Colleges", studentInfo.get(0));
        // 학생 전공
        model.addAttribute("UserMajor", studentInfo.get(1));
        // user role
        model.addAttribute("UserRole", selectUserProfileInfo.get(2));
    }

    public void professorInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> professorInfo) {
        // 교수 이름
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
        // 교수 소속
        model.addAttribute("Colleges", professorInfo.get(0));
        // 교수 전공
        model.addAttribute("UserMajor", professorInfo.get(1));
        // user role
        model.addAttribute("UserRole", selectUserProfileInfo.get(2));
    }

    public void administratorInfo(Model model, ArrayList<String> selectUserProfileInfo) {
        model.addAttribute("UserName", selectUserProfileInfo.get(0));
    }

    public void getUserInformation(
            Principal principal, User user, Model model, String sRole, String pRole, String aRole
    ) {
        String loginID = principal.getName();
        ArrayList<String> selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
        user.setUserLoginID(loginID);
        if (!selectUserProfileInfo.isEmpty()) {
            user.setUserName(selectUserProfileInfo.get(0));
        }
        if (selectUserProfileInfo.get(2).equals(sRole)) {
            ArrayList<String> studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));
            studentInfo(model, selectUserProfileInfo, studentInfo);
        } else if (selectUserProfileInfo.get(2).equals(pRole)) {
            ArrayList<String> professorInfo = professorService.selectProfessorProfileInfo(selectUserProfileInfo.get(1));
            professorInfo(model, selectUserProfileInfo, professorInfo);
        } else if (selectUserProfileInfo.get(2).equals(aRole)) {
            administratorInfo(model, selectUserProfileInfo);
        }
    }
}
