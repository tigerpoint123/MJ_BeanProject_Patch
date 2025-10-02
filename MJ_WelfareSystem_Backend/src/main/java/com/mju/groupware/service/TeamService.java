package com.mju.groupware.service;

import java.util.List;

import com.mju.groupware.dto.Class;
import com.mju.groupware.dto.Team;
import com.mju.groupware.dto.TeamBoard;
import com.mju.groupware.dto.TeamUser;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserReview;

public interface TeamService {

    void InsertTeamInfo(Team team);

    int SelectClassID(Class classInfo);

    int SelectUserIDForTeamUser(User user);

    void InsertTeamUserInfo(TeamUser teamUser);

    List<Class> SelectLectureInfo(String lectureName);

    int SelectTeamLeaderUserID(String name);

    List<Team> SelectTeamList();

    Class SelectClassList(int classID);

    int SelectClassIDForCheckTeam(int teamID);

    List<Class> SelectClassInfoForCheckTeam(int classID);

    String SelectTeamName(int teamID);

    List<TeamUser> SelectTeamMemberInfo(int teamID);

    String SelectLeaderName(int userID);

    String SelectLeaderLoginID(int userID);

    List<TeamUser> SelectMyTeamList(String loginID);

    void DeleteTeamMemberInfo(int teamID);

    List<Team> SelectMyTeamInfo(int teamID);

    List<Class> SelectClassInfo(int classID);

    List<TeamBoard> SelectTeamBoardListInfo(String teamID);

    String SelectTeamIDForDocument(String userID);

    String SelectTeamIDForDelete(String tUserID);

    List<Integer> SelectTeamNameWithLoginUser(String name);

    Integer SelectClassIDFromTeam(Integer teamID);

    String SelectTeamIDForReview(String string);

    List<TeamUser> SelectTeamMember(String teamID);

    String SelectTeamUserID(String userLoginID);

    void InsertUserReview(UserReview userReview);

    String SelectTeamLeaderLoginID(String teamID);

    void DeleteTeam(String teamID);

    String SelectWriterUserID(String name);

    int SelectColumnCount(UserReview userReview);

    String SelectTeamNameWithTeamID(int teamID);

}
