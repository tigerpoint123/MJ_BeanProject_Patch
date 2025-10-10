package com.mju.groupware.dao;

import global.properties.UserDaoProperties;
import com.mju.groupware.dto.Professor;
import com.mju.groupware.dto.Student;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserInfoOpen;
import global.security.UsersDetails;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
	private final UserDaoProperties userDaoProps;
	private final SqlSessionTemplate sqlSession;

	@Override
	public void insertForSignUp(User user) {
		this.sqlSession.insert(userDaoProps.getMethods().getInsertUser(), user);
	}

	@Override
	public UsersDetails selectByLoginID(String userLoginID) {
		UsersDetails users = this.sqlSession.selectOne(userDaoProps.getMethods().getSelectByLoginId(), userLoginID);
		return users;
	}

	@Override
	public boolean selectForIDConfirm(User user) {
		User output = this.sqlSession.selectOne(userDaoProps.getMethods().getSelectForIdConfirm(), user);

		if (output == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean selectPwdForConfirmToFindPwd(User user) {
		User output = this.sqlSession.selectOne(userDaoProps.getMethods().getSelectPwdForConfirmToFindPwd(), user);
		if (output == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public int selectUserID(Student student) {
		return this.sqlSession.selectOne(userDaoProps.getMethods().getSelectUserId(), student);
	}

	@Override
	public int selectUserID(Professor professor) {
		return this.sqlSession.selectOne(userDaoProps.getMethods().getSelectUserId(), professor);
	}

	@Override
	public boolean selectForShowPassword(User user) {
		User output = this.sqlSession.selectOne(userDaoProps.getMethods().getSelectForShowPassword(), user);
		if (output == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean selectForEmailDuplicateCheck(User user) {
		User output = sqlSession.selectOne(userDaoProps.getMethods().getSelectForEmailDuplicateCheck(), user.getUserEmail());
		return output != null;
	}

	@Override
	public void updateLoginDate(User user) {
		this.sqlSession.selectOne(userDaoProps.getMethods().getUpdateLoginDate(), user);
	}

	@Override
	public String selectCurrentPwd(String id) {
		return this.sqlSession.selectOne(userDaoProps.getMethods().getSelectCurrentPwd(), id);
	}

	@Override
	public void updatePwd(User user) {
		this.sqlSession.update(userDaoProps.getMethods().getUpdatePwd(), user);
	}

	@Override
	public boolean selectForPwdCheckBeforeModify(String id, String pwd) {

		// 추후 entity로 이동해야한다.
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String output = this.sqlSession.selectOne(userDaoProps.getMethods().getSelectForPwdCheckBeforeModify(), id);
		if (output == null) {
			return false;
		} else {
			if (encoder.matches(pwd, output)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public ArrayList<String> selectUserProfileInfo(String id) {
		// 정보를 저장하기 위한 ArrayList
		ArrayList<String> info = new ArrayList<>();
		// 학생정보를 가져오는 query문 실행
		List<User> output = this.sqlSession.selectList(userDaoProps.getMethods().getSelectUserInfo(), id);

		if (output != null && !output.isEmpty()) {
			User user = output.get(0);
			String userName = user.getUserName();
			int userId = user.getUserID();
			String userRole = user.getUserRole();
			
			// 이름 0
			info.add(userName);
			// 아이디 1
			info.add(Integer.toString(userId));
			// role 2
			info.add(userRole);
		}
		return info;
	}

	@Override
	public void updateUserPhoneNumber(User user) {
		sqlSession.update(userDaoProps.getMethods().getUpdateUserPhoneNum(), user);
	}

	@Override
	public void updateUserMajor(User user) {
		sqlSession.update(userDaoProps.getMethods().getUpdateUserMajor(), user);
	}

	@Override
	public void updateUserColleges(User user) {
		sqlSession.update(userDaoProps.getMethods().getUpdateUserColleges(), user);
	}

	@Override
	public ArrayList<String> selectUserInformation(String userId) {
		ArrayList<String> userInfo = new ArrayList<>();
		List<User> output = sqlSession.selectList(userDaoProps.getMethods().getSelectUserInformation(), userId);
		
		if (output != null && !output.isEmpty()) {
			User user = output.get(0);
			int userIdValue = user.getUserID();
			String userLoginIdValue = user.getUserLoginID();
			
			// 아이디
			userInfo.add(Integer.toString(userIdValue));
			// 로그인 아이디(학번)
			userInfo.add(userLoginIdValue);
		}
		return userInfo;
	}

	@Override
	public void updateTemporaryPwd(User user) {
		sqlSession.update(userDaoProps.getMethods().getUpdateTemporaryPwd(), user);
	}

	@Override
	public void updateDoWithdrawalRecoveryByAdmin(String ajaxMsg) {
		sqlSession.update(userDaoProps.getMethods().getUpdateDoWithdrawalRecoveryByAdmin(), ajaxMsg);
	}

	@Override
	public void updateDormantOneToZero(String id) {
		sqlSession.update(userDaoProps.getMethods().getUpdateDormantOneToZero(), id);
	}

	@Override
	public void updateUserRole(String id, String role) {
		User user = new User();
		user.setUserLoginID(id);
		user.setUserRole(role);
		user.setAuthority(userDaoProps.getRoles().getRoleUser());
		sqlSession.update(userDaoProps.getMethods().getUpdateUserRole(), user);
	}

	@Override
	public void updateAdminRole(String id, String role) {
		User user = new User();
		user.setUserLoginID(id);
		user.setUserRole(role);
		user.setAuthority(userDaoProps.getRoles().getRoleAdmin());
		sqlSession.update(userDaoProps.getMethods().getUpdateAdminRole(), user);
	}

	@Override
	public ArrayList<String> selectMyPageUserInfo(String userId) {
		ArrayList<String> info = new ArrayList<>();
		List<User> output = this.sqlSession.selectList(userDaoProps.getMethods().getSelectMyPageInfo(), userId);
		
		if (output != null && !output.isEmpty()) {
			User user = output.get(0);
			int userIdValue = user.getUserID();
			String userLoginIdValue = user.getUserLoginID();
			String userNameValue = user.getUserName();
			String userPhoneNumValue = user.getUserPhoneNum();
			String userEmailValue = user.getUserEmail();
			
			info.add(Integer.toString(userIdValue));
			info.add(userLoginIdValue);
			info.add(userNameValue);
			info.add(userPhoneNumValue);
			info.add(userEmailValue);
		}
		return info;
	}

	@Override
	public ArrayList<String> selectMyPageUserInfoByID(String mysqlID) {
		ArrayList<String> info = new ArrayList<>();
		List<User> output = this.sqlSession.selectList(userDaoProps.getMethods().getSelectMyPageInfoById(), mysqlID);
		
		if (output != null && !output.isEmpty()) {
			User user = output.get(0);
			String userLoginIdValue = user.getUserLoginID();
			String userNameValue = user.getUserName();
			String userPhoneNumValue = user.getUserPhoneNum();
			String userEmailValue = user.getUserEmail();
			String openPhoneNumValue = user.getOpenPhoneNum();
			String openGradeValue = user.getOpenGrade();
			
			info.add(userLoginIdValue);
			info.add(userNameValue);
			info.add(userPhoneNumValue);
			info.add(userEmailValue);
			info.add(openPhoneNumValue);
			info.add(openGradeValue);
		}
		return info;
	}

	@Override
	public void updateUserName(User user) {
		this.sqlSession.update(userDaoProps.getMethods().getUpdateUserName(), user);
	}

	@Override
	public void updatePhoneNum(User user) {
		this.sqlSession.update(userDaoProps.getMethods().getUpdateOpenPhoneNum(), user);
	}

	@Override
	public void updateOpenGrade(User user) {
		sqlSession.update(userDaoProps.getMethods().getUpdateOpenGrade(), user);
	}

	@Override
	public User selectUserInfo(String userLoginID) {
        return sqlSession.selectOne(userDaoProps.getMethods().getSelectUserInfoForWithdrawal(), userLoginID);
	}

	@Override
	public List<UserInfoOpen> selectOpenInfo(String userID) {
        return this.sqlSession.selectList(userDaoProps.getMethods().getSelectOpenInfo(), userID);
	}

	@Override
	public int selectUserIDFromBoardController(String userLoginID) {
        return this.sqlSession.selectOne(userDaoProps.getMethods().getSelectUserIdFromBoardController(), userLoginID);
	}

	@Override
	public String selectUserRole(String userLoginID) {
        return this.sqlSession.selectOne(userDaoProps.getMethods().getSelectUserRole(), userLoginID);
	}

	@Override
	public String selectUserName(String userLoginID) {
        return this.sqlSession.selectOne("SelectUserName", userLoginID);
	}

	@Override
	public void updateWithdrawalUser(User user) {
		sqlSession.update("UpdateWithdrawal", user);
	}

	@Override
	public void updateRecoveryWithdrawal(User user) {
		sqlSession.update("UpdateRecoveryWithdrawal", user);
	}

	@Override
	public void updateWithdrawalByDormant(String ajxMsg) {
		sqlSession.update("UpdateWithdrawalByDormant", ajxMsg);
	}

	@Override
	public boolean selectDormant(String loginID) {
		Boolean dormantCheck = sqlSession.selectOne("SelectDormant", loginID);
		return Boolean.TRUE.equals(dormantCheck);
	}

	@Override
	public void updateRecoveryDormant(String loginID) {
		sqlSession.update("UpdateRecoveryDormant", loginID);
	}

	@Override
	public String selectWriter(String userLoginID) {
        return sqlSession.selectOne("SelectWriter", userLoginID);
	}

	@Override
	public String selectUserIDForDocument(String userLoginID) {
        return sqlSession.selectOne("SelectUserIDForDocument", userLoginID);
	}

	@Override
	public String selectTWriterID(String userLoginID) {
		return sqlSession.selectOne("SelectTWriterID", userLoginID);
	}

	@Override
	public String selectUserIDForMyBoard(String loginID) {
        return sqlSession.selectOne("SelectUserIDForMyBoard", loginID);
	}

	@Override
	public String selectEmailForInquiry(String userLoginID) {
        return this.sqlSession.selectOne("SelectUserEmail", userLoginID);
	}

	@Override
	public String selectPhoneNumForInquiry(String userLoginID) {
        return this.sqlSession.selectOne("SelectUserPhoneNum", userLoginID);
	}

	@Override
	public String selectUserIDForDate(String loginID) {
		return sqlSession.selectOne("SelectUserIDForDate", loginID);
	}

	@Override
	public String selectIDForReview(String userLoginID) {
		return sqlSession.selectOne("SelectIDForReview", userLoginID);
	}

	@Override
	public User selectModifyUserInfo(String loginID) {
        return sqlSession.selectOne("SelectModifyUserInfo", loginID);
	}
}