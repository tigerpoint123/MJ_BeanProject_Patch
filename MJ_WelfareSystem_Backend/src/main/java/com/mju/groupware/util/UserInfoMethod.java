package com.mju.groupware.util;

import java.util.ArrayList;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.mju.groupware.dto.User;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;

@Repository
public class UserInfoMethod {

	private String UserName;
	private String StudentColleges;
	private String UserMajorForShow;
	private String ProfessorColleges;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

	public void StudentInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> studentInfo) {
		// 학생 이름
		UserName = selectUserProfileInfo.get(0);
		model.addAttribute("UserName", UserName);
		// 학생 소속
		StudentColleges = studentInfo.get(0);
		model.addAttribute("Colleges", StudentColleges);

		UserMajorForShow = studentInfo.get(1);
		model.addAttribute("UserMajor", UserMajorForShow);

		// user role
		model.addAttribute("UserRole", selectUserProfileInfo.get(2));
	}

	public void ProfessorInfo(Model model, ArrayList<String> selectUserProfileInfo, ArrayList<String> professorInfo) {
		// 교수 이름
		UserName = selectUserProfileInfo.get(0);
		model.addAttribute("UserName", UserName);
		// 교수 소속
		ProfessorColleges = professorInfo.get(0);
		model.addAttribute("Colleges", ProfessorColleges);
		// 교수 전공
		UserMajorForShow = professorInfo.get(1);
		model.addAttribute("UserMajor", UserMajorForShow);

		// user role
		model.addAttribute("UserRole", selectUserProfileInfo.get(2));

	}

	public void AdministratorInfo(Model model, ArrayList<String> selectUserProfileInfo) {
		UserName = selectUserProfileInfo.get(0);
		model.addAttribute("UserName", UserName);
	}

    public void GetUserInformation(Principal principal, User user, Model model, String sRole, String pRole,
            String aRole) {
        String LoginID = principal.getName();
        ArrayList<String> SelectUserProfileInfo = new ArrayList<String>();
        SelectUserProfileInfo = userService.SelectUserProfileInfo(LoginID);
        user.setUserLoginID(LoginID);
        if (!SelectUserProfileInfo.isEmpty()) {
            user.setUserName(SelectUserProfileInfo.get(0));
        }
        if (SelectUserProfileInfo.get(2).equals(sRole)) {
            ArrayList<String> StudentInfo = new ArrayList<String>();
            StudentInfo = studentService.SelectStudentProfileInfo(SelectUserProfileInfo.get(1));
            StudentInfo(model, SelectUserProfileInfo, StudentInfo);
        } else if (SelectUserProfileInfo.get(2).equals(pRole)) {
            ArrayList<String> ProfessorInfo = new ArrayList<String>();
            ProfessorInfo = professorService.SelectProfessorProfileInfo(SelectUserProfileInfo.get(1));
            ProfessorInfo(model, SelectUserProfileInfo, ProfessorInfo);
        } else if (SelectUserProfileInfo.get(2).equals(aRole)) {
            AdministratorInfo(model, SelectUserProfileInfo);
        }
    }
}
