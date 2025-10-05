package com.mju.groupware.service;

import com.mju.groupware.constant.ConstantAdmin;
import com.mju.groupware.dao.UserDao;
import com.mju.groupware.dao.UserListDao;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserList;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserListDao userListDao;
    private final ConstantAdmin constantAdmin;
    private final UserService userService;
    private final UserDao userDao;
    private final StudentService studentService;
    private final ProfessorService professorService;

    @Override
    public List<UserList> SelectUserlist() {
        return userListDao.SelectUserlist();
    }

    @Override
    public List<UserList> SelectDormantUserList() {
        return userListDao.SelectDormantUserList();
    }

    @Override
    public List<UserList> SelectWithdrawalUserList() {
        return userListDao.SelectWithdrawalUserList();
    }

    @Override
    public boolean detailStudent(Model model, String no) {
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
        String Result = SelectUserProfileInfo.get(4) + "," + SelectUserProfileInfo.get(5);
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
        model.addAttribute("UserEmail", SelectUserProfileInfo.get(3));
        return true;
    }

    @Override
    public boolean changeUserRole(HttpServletRequest request, String ARole, String URole) {
        try {
            String OptionValue = request.getParameter("OptionValue");
            String[] AjaxMsg = request.getParameterValues("CheckArr");

            if (OptionValue.equals(this.constantAdmin.getROLE_SUSER()))
                OptionValue = URole;
            else if (OptionValue.equals(this.constantAdmin.getROLE_PUSER()))
                OptionValue = URole;
            else if (OptionValue.equals(this.constantAdmin.getROLE_ADMIN()))
                OptionValue = ARole;

            for (String adminId : AjaxMsg) {
                if (OptionValue.equals(ARole))
                    userDao.UpdateAdminRole(adminId, OptionValue);
                else if (OptionValue.equals(URole))
                    userDao.UpdateUserRole(adminId, OptionValue);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserInfo(HttpServletRequest request) {
        try {
            String[] AjaxMsg = request.getParameterValues("CheckArr");
            for (String userId : AjaxMsg) {
                User userInfo = userDao.SelectUserInfo(userId);

                if (userInfo != null) {
                    User userToUpdate = new User();
                    userToUpdate.setUserLoginID(userInfo.getUserLoginID());
                    userToUpdate.setEnabled(false);
                    userToUpdate.setWithdrawal(true);
                    // 탈퇴한 날짜 set
                    Date Now = new Date();
                    SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");
                    userToUpdate.setDate(Date.format(Now));
                    // 탈퇴 업데이트
                    userDao.UpdateWithdrawalUser(userToUpdate);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getDetailPage(HttpServletRequest request, RedirectAttributes rttr) {
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

    @Override
    public boolean detailProfessor(Model model, String no) {
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
        String Result = SelectUserProfileInfo.get(4);
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
        return true;
    }

    @Override
    public void modifyStudentList(HttpServletRequest request, Model model) {
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
    }

    @Override
    public void modifyStudent(Principal principal, HttpServletRequest request, String no) {
        User user = new User();
        Student student = new Student();

        user.setUserLoginID(principal.getName());
        student.setUserID(Integer.parseInt(no));
        if (request.getParameter("ModifyComplete") != null) {
            if (!request.getParameter("UserName").isEmpty()) {
                // 이름바꾸기
                user.setUserName(request.getParameter("UserName"));
                userService.UpdateUserName(user);
            }
            if (!request.getParameter("StudentGender").equals(" ")) {
                // 성별바꾸기
                student.setStudentGender(request.getParameter("StudentGender"));
                studentService.UpdateStudentGender(student);
            }
            if (!request.getParameter("UserPhoneNum").isEmpty()) {
                // 전화번호바꾸기
                user.setUserPhoneNum(request.getParameter("UserPhoneNum"));
                userService.updateUserPhoneNumber(user);
            }
            if (!request.getParameter("StudentGrade").equals(" ")) {
                // 학년
                student.setStudentGrade(request.getParameter("StudentGrade"));
                studentService.updateStudentGrade(student);
            }
            if (!request.getParameter("StudentColleges").isEmpty()) {
                // 단과대학
                student.setStudentColleges(request.getParameter("StudentColleges"));
                studentService.UpdateStudentColleges(student);
            }
            if (!request.getParameter("StudentMajor").isEmpty()) {
                // 전공
                student.setStudentMajor(request.getParameter("StudentMajor"));
                studentService.UpdateStudentMajor(student);
            }

            if (request.getParameter("member").equals("Y")
                    && !request.getParameter("StudentDoubleMajor").equals(" ")) {
                // 부전공 있다
                student.setStudentDoubleMajor(request.getParameter("StudentDoubleMajor"));
                studentService.UpdateStudentDobuleMajor(student);
            } else if (request.getParameter("member").equals("N")) {
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
    }

    @Override
    public void modifyProfessorList(Model model, HttpServletRequest request, String loginID) {
        User UserInfo = userService.SelectModifyUserInfo(loginID);
        model.addAttribute("UserLoginID", loginID);
        String UserEmail = UserInfo.getUserEmail();
        int Location = UserEmail.indexOf("@");
        UserEmail = UserEmail.substring(0, Location);
        model.addAttribute("Email", UserEmail);
        // 연락처 공개
        model.addAttribute("OpenPhoneNum", UserInfo.getOpenPhoneNum());
    }

    @Override
    public void modifyProfessor(Principal principal, HttpServletRequest request, String no) {
        User user = new User();
        Professor professor = new Professor();

        user.setUserLoginID(principal.getName());
        professor.setUserID(Integer.parseInt(no));
        if (request.getParameter("ModifyCompleteP") != null) {
            if (!request.getParameter("UserName").isEmpty()) {
                // 이름바꾸기
                user.setUserName(request.getParameter("UserName"));
                userService.UpdateUserName(user);
            }
            if (!request.getParameter("UserPhoneNum").isEmpty()) {
                // 전화번호바꾸기
                user.setUserPhoneNum(request.getParameter("UserPhoneNum"));
                userService.updateUserPhoneNumber(user);
            }
            if (!request.getParameter("ProfessorColleges").isEmpty()) {
                // 단과대학
                professor.setProfessorColleges(request.getParameter("ProfessorColleges"));
                professorService.UpdateProfessorColleges(professor);
            }
            if (!request.getParameter("ProfessorMajor").isEmpty()) {
                // 전공
                professor.setProfessorMajor(request.getParameter("ProfessorMajor"));
                professorService.UpdateProfessorMajor(professor);
            }
            if (!request.getParameter("ProfessorRoom").isEmpty()) {
                // 교수실
                professor.setProfessorRoom(request.getParameter("ProfessorRoom"));
                professorService.UpdateProfessorRoom(professor);
            }
            if (!request.getParameter("ProfessorRoomNum").isEmpty()) {
                // 교수실 전화번호
                professor.setProfessorRoomNum(request.getParameter("ProfessorRoomNum"));
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
    }

    @Override
    public void dormantUserDelete(HttpServletRequest request) {
        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (String msg : AjaxMsg) {
            userDao.UpdateWithdrawalByDormant(msg);
        }
    }

    @Override
    public void deleteUserRollback(HttpServletRequest request) {
        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (String msg : AjaxMsg) {
            userService.UpdateDoWithdrawalRecoveryByAdmin(msg);
        }
    }

    @Override
    public void manageSession(Model model) {

    }

    @Override
    public void dormantUserRollback(HttpServletRequest request) {
        String[] AjaxMsg = request.getParameterValues("CheckArr");
        for (int i = 0; i < AjaxMsg.length; i++) {
            userDao.UpdateDormantOneToZero(AjaxMsg[i]);
        }
    }
}
