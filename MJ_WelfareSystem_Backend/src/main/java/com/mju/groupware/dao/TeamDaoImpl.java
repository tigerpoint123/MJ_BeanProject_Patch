package com.mju.groupware.dao;

import java.util.List;

import global.properties.TeamDaoProperties;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mju.groupware.dto.Class;
import com.mju.groupware.dto.Team;
import com.mju.groupware.dto.TeamBoard;
import com.mju.groupware.dto.TeamUser;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReview;

@Service
@Repository
@RequiredArgsConstructor
public class TeamDaoImpl implements TeamDao {

	private final TeamDaoProperties teamDaoProps;
	private final SqlSessionTemplate sqlSession;

	@Override
	public void insertTeamInfo(Team team) {
		sqlSession.insert(teamDaoProps.getMethods().getInsertTeamInfo(), team);
	}

	@Override
	public int selectClassId(Class classInfo) {
		Integer output = sqlSession.selectOne(teamDaoProps.getMethods().getSelectClassId(), classInfo);
		return output != null ? output : 0;
	}

	@Override
	public int selectUserIdForTeamUser(User user) {
		Integer output = sqlSession.selectOne(teamDaoProps.getMethods().getSelectUserIdForTeamUser(), user);
		return output != null ? output : 0;
	}

	@Override
	public void insertTeamUserInfo(TeamUser teamUser) {
		sqlSession.insert(teamDaoProps.getMethods().getInsertTeamUserInfo(), teamUser);
	}

	@Override
	public List<Class> selectLectureInfo(String lectureName) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectLectureInformation(), lectureName);
	}

	@Override
	public int selectTeamLeaderUserId(String name) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamLeaderUserId(), name);
	}

	@Override
	public List<Team> selectTeamList() {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectTeamList());
	}

	@Override
	public Class selectClassList(int classID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectClassList(), classID);
	}

	@Override
	public int selectClassIdForCheckTeam(int teamID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectClassIdForCheckTeam(), teamID);
	}

	@Override
	public List<Class> selectClassInfoForCheckTeam(int classID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectClassInfoForCheckTeam(), classID);
	}

	@Override
	public String selectTeamName(int teamID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamName(), teamID);
	}

	@Override
	public List<TeamUser> selectTeamMemberInfo(int teamID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectTeamMemberInfo(), teamID);
	}

	@Override
	public String selectLeaderName(int userID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectLeaderName(), userID);
	}

	@Override
	public String selectLeaderLoginId(int userID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectLeaderLoginId(), userID);
	}

	@Override
	public List<TeamUser> selectMyTeamList(String loginID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectMyTeamList(), loginID);
	}

	@Override
	public void deleteTeamMemberInfo(int teamID) {
		sqlSession.delete(teamDaoProps.getMethods().getDeleteTeamMemberInfo(), teamID);
	}

	@Override
	public List<Team> selectMyTeamInfo(int teamID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectMyTeamInfo(), teamID);
	}

	@Override
	public List<Class> selectClassInfo(int classID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectClassInfo(), classID);
	}

	@Override
	public List<TeamBoard> selectTeamBoardListInfo(String teamID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectTeamBoardListInfo(), teamID);
	}

	@Override
	public String selectTeamIdForDocument(String userID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamIdForDocument(), userID);
	}

	@Override
	public String selectTeamIdForDelete(String tUserID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamIdForDelete(), tUserID);
	}

	@Override
	public List<Integer> selectTeamNameWithLoginUser(String name) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectTeamNameWithLoginUser(), name);
	}

	@Override
	public String selectTeamIdForReview(String teamName) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamIdForReview(), teamName);
	}

	@Override
	public List<TeamUser> selectTeamMember(String teamID) {
		return sqlSession.selectList(teamDaoProps.getMethods().getSelectTeamMember(), teamID);
	}

	@Override
	public String selectTeamUserId(String userLoginID) {
		return sqlSession.selectOne(teamDaoProps.getMethods().getSelectTeamUserId(), userLoginID);
	}

	@Override
	public void insertUserReview(UserReview userReview) {
		sqlSession.insert(teamDaoProps.getMethods().getInsertUserReview(), userReview);
	}

	@Override
	public String selectTeamLeaderLoginId(String teamID) {
		return sqlSession.selectOne("SelectTeamLeaderLoginID", teamID);
	}

	@Override
	public void deleteTeam(String teamID) {
		sqlSession.delete("DeleteTeam", teamID);
	}

	@Override
	public String selectWriterUserId(String name) {
		return sqlSession.selectOne("SelectWriterUserID", name);
	}

	@Override
	public int selectColumnCount(UserReview userReview) {
		return sqlSession.selectOne("SelectColumnCount", userReview);
	}

	@Override
	public String selectTeamNameWithTeamId(int teamID) {
		return sqlSession.selectOne("SelectTeamNameWithTeamID", teamID);
	}

	@Override
	public Integer selectClassIdFromTeam(Integer teamID) {
		return sqlSession.selectOne("SelectClassIDFromTeam", teamID);
	}

}
