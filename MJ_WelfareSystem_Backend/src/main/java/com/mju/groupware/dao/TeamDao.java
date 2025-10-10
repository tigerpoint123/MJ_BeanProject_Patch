package com.mju.groupware.dao;

import java.util.List;

import com.mju.groupware.dto.Class;
import com.mju.groupware.dto.Team;
import com.mju.groupware.dto.TeamBoard;
import com.mju.groupware.dto.TeamUser;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReview;

public interface TeamDao {

	void insertTeamInfo(Team team);

	int selectClassId(Class classInfo);

	int selectUserIdForTeamUser(User user);

	void insertTeamUserInfo(TeamUser teamUser);

	List<Class> selectLectureInfo(String lectureName);

	int selectTeamLeaderUserId(String name);

	List<Team> selectTeamList();

	Class selectClassList(int classID);

	int selectClassIdForCheckTeam(int teamID);

	List<Class> selectClassInfoForCheckTeam(int classID);

	String selectTeamName(int teamID);

	List<TeamUser> selectTeamMemberInfo(int teamID);

	String selectLeaderName(int userID);

	String selectLeaderLoginId(int userID);

	List<TeamUser> selectMyTeamList(String loginID);

	void deleteTeamMemberInfo(int teamID);

	List<Team> selectMyTeamInfo(int teamID);

	List<Class> selectClassInfo(int classID);

	List<TeamBoard> selectTeamBoardListInfo(String teamID);

	String selectTeamIdForDocument(String userID);

	String selectTeamIdForDelete(String tUserID);

	Integer selectClassIdFromTeam(Integer teamID);

	List<Integer> selectTeamNameWithLoginUser(String name);

	String selectTeamIdForReview(String teamName);

	List<TeamUser> selectTeamMember(String teamID);

	String selectTeamUserId(String userLoginID);

	void insertUserReview(UserReview userReview);

	String selectTeamLeaderLoginId(String teamID);

	void deleteTeam(String teamID);

	String selectWriterUserId(String name);

	int selectColumnCount(UserReview userReview);

	String selectTeamNameWithTeamId(int teamID);

}
