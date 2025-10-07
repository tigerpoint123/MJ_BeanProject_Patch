package com.mju.groupware.service;

import com.mju.groupware.dao.TeamDao;
import com.mju.groupware.dto.*;
import com.mju.groupware.dto.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
	@Autowired
	TeamDao teamDao;

	@Autowired
	BoardService boardService;

	@Autowired
	UserService userService;

	@Autowired
	StudentService studentService;

	@Autowired
	ProfessorService professorService;

	@Autowired
	com.mju.groupware.util.UserInfoMethod userInfoMethod;

	@Override
	public void InsertTeamInfo(Team team) {
		teamDao.InsertTeamInfo(team);
	}

	@Override
	public int SelectClassID(Class classInfo) {
		return teamDao.SelectClassID(classInfo);
	}

	@Override
	public int SelectUserIDForTeamUser(User user) {
		return teamDao.SelectUserIDForTeamUser(user);
	}

	@Override
	public void InsertTeamUserInfo(TeamUser teamUser) {
		teamDao.InsertTeamUserInfo(teamUser);
	}

	@Override
	public List<Class> SelectLectureInfo(String lectureName) {
        return teamDao.SelectLectureInfo(lectureName);
	}

	@Override
	public int SelectTeamLeaderUserID(String name) {
		return teamDao.SelectTeamLeaderUserID(name);
	}

	@Override
	public List<Team> SelectTeamList() {
        return teamDao.SelectTeamList();
	}

	@Override
	public Class SelectClassList(int classID) {
        return teamDao.SelectClassList(classID);
	}

	@Override
	public int SelectClassIDForCheckTeam(int teamID) {
		return teamDao.SelectClassIDForCheckTeam(teamID);
	}

	@Override
	public List<Class> SelectClassInfoForCheckTeam(int classID) {
		return teamDao.SelectClassInfoForCheckTeam(classID);
	}

	@Override
	public String SelectTeamName(int teamID) {
		return teamDao.SelectTeamName(teamID);
	}

	@Override
	public List<TeamUser> SelectTeamMemberInfo(int teamID) {
		return teamDao.SelectTeamMemberInfo(teamID);
	}

	@Override
	public String SelectLeaderName(int userID) {
		return teamDao.SelectLeaderName(userID);
	}

	@Override
	public String SelectLeaderLoginID(int userID) {
		return teamDao.SelectLeaderLoginID(userID);
	}

	@Override
	public List<TeamUser> SelectMyTeamList(String loginID) {
		return teamDao.SelectMyTeamList(loginID);
	}

	@Override
	public void DeleteTeamMemberInfo(int teamID) {
		teamDao.DeleteTeamMemberInfo(teamID);
	}

	@Override
	public List<Team> SelectMyTeamInfo(int teamID) {
		return teamDao.SelectMyTeamInfo(teamID);
	}

	@Override
	public List<Class> SelectClassInfo(int classID) {
		return teamDao.SelectClassInfo(classID);
	}
	
	@Override
	public List<TeamBoard> SelectTeamBoardListInfo(String teamID) {
		return teamDao.SelectTeamBoardListInfo(teamID);
	}

	@Override
	public String SelectTeamIDForDocument(String userID) {
		return teamDao.SelectTeamIDForDocument(userID);
	}

	@Override
	public String SelectTeamIDForDelete(String tUserID) {
		return teamDao.SelectTeamIDForDelete(tUserID);
	}

	@Override
	public Integer SelectClassIDFromTeam(Integer teamID) {
		return teamDao.SelectClassIDFromTeam(teamID);
	}

	@Override
	public List<Integer> SelectTeamNameWithLoginUser(String name) {
		return teamDao.SelectTeamNameWithLoginUser(name);
	}

	@Override
	public String SelectTeamIDForReview(String teamName) {
		return teamDao.SelectTeamIDForReview(teamName);
	}

	@Override
	public List<TeamUser> SelectTeamMember(String teamID) {
		return teamDao.SelectTeamMember(teamID);
	}

	@Override
	public String SelectTeamUserID(String userLoginID) {
		return teamDao.SelectTeamUserID(userLoginID);
	}

	@Override
	public void InsertUserReview(UserReview userReview) {
		teamDao.InsertUserReview(userReview);
	}

	@Override
	public String SelectTeamLeaderLoginID(String teamID) {
		return teamDao.SelectTeamLeaderLoginID(teamID);
	}

	@Override
	public void DeleteTeam(String teamID) {
		teamDao.DeleteTeam(teamID);
	}

	@Override
	public String SelectWriterUserID(String name) {
		return teamDao.SelectWriterUserID(name);
	}

	@Override
	public int SelectColumnCount(UserReview userReview) {
		return teamDao.SelectColumnCount(userReview);
	}

	@Override
	public String SelectTeamNameWithTeamID(int teamID) {
		// TODO Auto-generated method stub
		return teamDao.SelectTeamNameWithTeamID(teamID);
	}

	@Override
	public List<MergeTeam> getMyTeamList(String loginID) {
		// teamID를 통해 classID가져오기
		List<TeamUser> teamUserListInfo = SelectMyTeamList(loginID);
		
		// mysql team table에 정보를 받아온다.
		// team table에 정보 중 가장 중요한 것은 teamID이다.
		if (teamUserListInfo.isEmpty()) {
			return null;
		}
		
		// teamID를 통해
		// teamName과 classID가져오기
		// 가져온 classID를 통해서 classTable에서 과목명과 교수명 가져오기
		// 번호 과목명 교수명 필요
		
		// ibatis에서는 reMapResult가 있었지만 mybatis에서는 해당 기능이 지원되지 않아 teamListInfo2에 초기화되는
		// 정보를 저장시킨다.
		List<Team> teamListInfo2 = new java.util.ArrayList<>();
		for (int i = 0; i < teamUserListInfo.size(); i++) {
			List<Team> teamListInfo = SelectMyTeamInfo(teamUserListInfo.get(i).getTeamID());
			teamListInfo2.add(teamListInfo.get(0));
		}
		
		List<Class> classInfo2 = new java.util.ArrayList<>();
		for (int i = 0; i < teamListInfo2.size(); i++) {
			List<Class> classInfo = SelectClassInfo(teamListInfo2.get(i).getClassID());
			classInfo2.add(classInfo.get(0));
		}
		
		List<MergeTeam> mergeInfo = new java.util.ArrayList<>();
		for (int i = 0; i < classInfo2.size(); i++) {
			MergeTeam mergeTeam = new MergeTeam();
			mergeTeam.setTeamID(Integer.toString(teamListInfo2.get(i).getTeamID()));
			mergeTeam.setClassName(classInfo2.get(i).getClassName());
			mergeTeam.setClassProfessorName(classInfo2.get(i).getClassProfessorName());
			mergeTeam.setTeamName(teamListInfo2.get(i).getTeamName());
			mergeInfo.add(mergeTeam);
		}
		
		return mergeInfo;
	}

	@Override
	public java.util.HashMap<String, Object> getDocumentContent(String loginID, String tBoardID, String teamID) {
		java.util.HashMap<String, Object> result = new java.util.HashMap<>();
		
		// TBoardID select
		TeamBoard teamBoard = boardService.SelectTeamBoardContent(tBoardID);
		
		// content set
		result.put("BoardSubject", teamBoard.getTBoardSubject());
		result.put("BoardWriter", teamBoard.getTBoardWriter());
		result.put("BoardDate", teamBoard.getTBoardDate());
		result.put("BoardContent", teamBoard.getTBoardContent());
		result.put("TeamID", teamBoard.getTeamID());
		result.put("TBoardID", tBoardID);
		
		// 로그인한 사람의 userID를 가져오기 위함
		String tUserID = boardService.SelectLoginUserID(loginID);
		teamBoard.setTUserID(Integer.parseInt(tUserID));
		teamBoard.setTeamID(teamID);
		teamBoard.setTBoardID(Integer.parseInt(tBoardID));
		
		// 작성자로그인아이디
		String tWriter = boardService.SelectWriterID(teamBoard);
		String tWriterID = userService.SelectTWriterID(tWriter);
		
		result.put("TUserID", tUserID);
		result.put("TUserIDFromWriter", tWriterID);
		
		// 파일 리스트
		java.util.List<java.util.Map<String, Object>> teamBoardFile = boardService.SelectTeamBoardFileList(Integer.parseInt(tBoardID));
		result.put("TeamBoardFile", teamBoardFile);
		result.put("TeamIDFinal", teamID);
		
		return result;
	}

	@Override
	public void setDocumentContentAttributes(String loginID, String tBoardID, String teamID, org.springframework.ui.Model model) {
		HashMap<String, Object> documentContent = getDocumentContent(loginID, tBoardID, teamID);
		
		// Model에 모든 데이터 설정
		model.addAttribute("BoardSubject", documentContent.get("BoardSubject"));
		model.addAttribute("BoardWriter", documentContent.get("BoardWriter"));
		model.addAttribute("BoardDate", documentContent.get("BoardDate"));
		model.addAttribute("BoardContent", documentContent.get("BoardContent"));
		model.addAttribute("TeamID", documentContent.get("TeamID"));
		model.addAttribute("TBoardID", documentContent.get("TBoardID"));
		model.addAttribute("TUserID", documentContent.get("TUserID"));
		model.addAttribute("TUserIDFromWriter", documentContent.get("TUserIDFromWriter"));
		model.addAttribute("TeamBoardFile", documentContent.get("TeamBoardFile"));
		model.addAttribute("TeamID", documentContent.get("TeamIDFinal"));
	}

	@Override
	public void postDocumentWrite(String loginID, String teamID, String boardSubject, String boardContent, jakarta.servlet.http.HttpServletRequest request) {
		// DB에 정보저장하기
		TeamBoard teamBoard = new TeamBoard();
		
		// 작성자 select
		String documentWriter = userService.SelectWriter(loginID);
		teamBoard.setTBoardSubject(boardSubject);
		teamBoard.setTBoardContent(boardContent);
		teamBoard.setTBoardWriter(documentWriter);
		teamBoard.setTUserLoginID(loginID);
		teamBoard.setTeamID(teamID);
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		teamBoard.setTBoardDate(dateFormat.format(now));
		
		boardService.InsertTeamDocument(teamBoard, request);
	}

	@Override
	public HashMap<String, Object> getDocumentModify(String tBoardID, String teamID) {
		HashMap<String, Object> result = new HashMap<>();
		
		TeamBoard teamBoard = boardService.SelectTeamBoardContent(tBoardID);
		
		// content set
		result.put("BoardSubject", teamBoard.getTBoardSubject());
		result.put("BoardWriter", teamBoard.getTBoardWriter());
		result.put("BoardDate", teamBoard.getTBoardDate());
		result.put("BoardContent", teamBoard.getTBoardContent());
		result.put("TBoardID", tBoardID);
		
		// 수정된 file을 보여주는곳
		List<java.util.Map<String, Object>> teamBoardFile = boardService.SelectTeamBoardFileList(Integer.parseInt(tBoardID));
		result.put("TeamBoardFile", teamBoardFile);
		result.put("TeamID", teamID);
		
		return result;
	}

	@Override
	public void setDocumentModifyAttributes(String tBoardID, String teamID, org.springframework.ui.Model model) {
		HashMap<String, Object> documentModify = getDocumentModify(tBoardID, teamID);
		
		// Model에 모든 데이터 설정
		model.addAttribute("BoardSubject", documentModify.get("BoardSubject"));
		model.addAttribute("BoardWriter", documentModify.get("BoardWriter"));
		model.addAttribute("BoardDate", documentModify.get("BoardDate"));
		model.addAttribute("BoardContent", documentModify.get("BoardContent"));
		model.addAttribute("TBoardID", documentModify.get("TBoardID"));
		// 수정된 file을 보여주는곳
		model.addAttribute("TeamBoardFile", documentModify.get("TeamBoardFile"));
		model.addAttribute("TeamID", documentModify.get("TeamID"));
	}

	@Override
	public void updateDocumentModify(String loginID, String tBoardID, String title, String content, 
	                                 String[] fileDeleteList, String[] fileDeleteNameList, jakarta.servlet.http.HttpServletRequest request) {
		TeamBoard teamBoard = new TeamBoard();
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int tBoardIDInt = Integer.parseInt(tBoardID);
		
		// 로그인 한 아이디
		int userID = userService.SelectUserIDFromBoardController(loginID);
		String userName = userService.SelectUserName(loginID);
		
		// 수정할 내용 set
		teamBoard.setTBno(tBoardIDInt);
		teamBoard.setTBoardSubject(title);
		teamBoard.setTBoardContent(content);
		teamBoard.setTBoardWriter(userName);
		teamBoard.setTBoardDate(dateFormat.format(now));
		teamBoard.setTBoardID(tBoardIDInt);
		teamBoard.setTUserLoginID(loginID);
		teamBoard.setTUserID(userID);
		
		boardService.UpdateTeamBoardModifiedContent(teamBoard, fileDeleteList, fileDeleteNameList, request);
	}

	@Override
	public HashMap<String, Object> getFileDownloadData(java.util.Map<String, Object> fileInfoMap, String filePath) {
		HashMap<String, Object> result = new HashMap<>();
		
		java.util.Map<String, Object> resultMap = boardService.SelectTeamBoardFileInfo(fileInfoMap);
		String storedFileName = (String) resultMap.get("TStoredFileName");
		String originalFileName = (String) resultMap.get("TOriginalFileName");
		
		try {
			// 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
			byte[] fileBytes = org.apache.commons.io.FileUtils
					.readFileToByteArray(new java.io.File(filePath + storedFileName));
			
			result.put("fileBytes", fileBytes);
			result.put("originalFileName", originalFileName);
		} catch (Exception e) {
			throw new RuntimeException("파일 읽기 실패", e);
		}
		
		return result;
	}

	@Override
	public void deleteDocument(String teamID, int tBoardID) {
		// 삭제 boolean update
		boardService.UpdateTBoardDelete(tBoardID);
	}

	@Override
	public void setSearchLectureAttributes(String lectureName, org.springframework.ui.Model model) {
		// 유저 정보
		model.addAttribute("LectureName", lectureName);
	}

	@Override
	public boolean setCreateTeamAttributes(String loginID, String lectureName, org.springframework.ui.Model model,
	                                       String studentRole, String professorRole, String administratorRole) {
		// 유저 정보
		// 로그인 한 아이디
		java.util.ArrayList<String> selectUserProfileInfo = new java.util.ArrayList<>();
		selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
		
		if (selectUserProfileInfo.get(2).equals(studentRole)) {
			java.util.ArrayList<String> studentInfo = new java.util.ArrayList<>();
			studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));
			
			userInfoMethod.studentInfo(model, selectUserProfileInfo, studentInfo);
		} else if (selectUserProfileInfo.get(2).equals(professorRole)) {
			java.util.ArrayList<String> professorInfo = new java.util.ArrayList<>();
			professorInfo = professorService.selectProfessorProfileInfo(selectUserProfileInfo.get(1));
			
			userInfoMethod.professorInfo(model, selectUserProfileInfo, professorInfo);
		} else if (selectUserProfileInfo.get(2).equals(administratorRole)) {
			userInfoMethod.administratorInfo(model, selectUserProfileInfo);
		}
		
		// 학생 이름
		String userName = selectUserProfileInfo.get(0);
		model.addAttribute(userName, userName);
		model.addAttribute("TeamLeaderID", loginID);
		model.addAttribute("TeamLeaderName", userName);
		
		List<com.mju.groupware.dto.Class> lectureInfo = SelectLectureInfo(lectureName);
		
		if (lectureInfo.isEmpty()) {
			return false;
		}
		
		List<String> allInfo = new java.util.ArrayList<>();
		for (int i = 0; i < lectureInfo.size(); i++) {
			String allInformation = lectureInfo.get(i).getClassName() + " "
					+ lectureInfo.get(i).getClassProfessorName();
			allInfo.add(allInformation);
		}
		model.addAttribute("ClassNameList", allInfo);
		
		return true;
	}

	@Override
	public String createTeam(String loginID, String lectureWithProfessor, String teamName, 
	                        String teamLeaderName, String[] studentIDs, String[] studentNames) {
		// class 정보
		String[] words = lectureWithProfessor.split("\\s");
		// 팀원 정보
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		
		com.mju.groupware.dto.Class classInfo = new com.mju.groupware.dto.Class();
		classInfo.setClassName(words[0]);
		classInfo.setClassProfessorName(words[1]);
		int classID = SelectClassID(classInfo);
		int leaderUserID = SelectTeamLeaderUserID(loginID);
		String leaderName = SelectLeaderName(leaderUserID);
		String leaderLoginID = SelectLeaderLoginID(leaderUserID);
		
		Team team = new Team();
		team.setTeamName(teamName);
		team.setTeamLeaderID(leaderLoginID);
		team.setTeamLeaderName(teamLeaderName);
		team.setTeamCreationDate(dateFormat.format(now));
		team.setClassID(classID);
		
		boolean checker = false;
		
		for (int i = 0; i < studentIDs.length; i++) {
			User user = new User();
			user.setUserLoginID(studentIDs[i]);
			user.setUserName(studentNames[i]);
			int userID = SelectUserIDForTeamUser(user);
			
			if (userID == 0) {
				return "UserNotFound";
			}
			
			if (!checker) {
				InsertTeamInfo(team);
				TeamUser teamUser = new TeamUser();
				teamUser.setUserLoginID(leaderLoginID);
				teamUser.setTeamID(team.getTeamID());
				teamUser.setUserID(leaderUserID);
				teamUser.setUserName(leaderName);
				InsertTeamUserInfo(teamUser);
				checker = true;
			}
			
			String memberName = SelectLeaderName(userID);
			String memberLoginID = SelectLeaderLoginID(userID);
			
			TeamUser teamUser = new TeamUser();
			teamUser.setTeamID(team.getTeamID());
			teamUser.setUserID(userID);
			teamUser.setUserLoginID(memberLoginID);
			teamUser.setUserName(memberName);
			InsertTeamUserInfo(teamUser);
		}
		
		return null;
	}

	@Override
	public void setCheckTeamAttributes(String loginID, int teamID, org.springframework.ui.Model model) {
		// 팀 아이디를 넘겨 받음
		String teamLeaderID = SelectTeamLeaderLoginID(Integer.toString(teamID));
		model.addAttribute("UserLoginID", loginID);
		model.addAttribute("TeamLeaderID", teamLeaderID);
		
		int classID = SelectClassIDForCheckTeam(teamID);
		// 과목명, 교수
		List<Class> lectureList = SelectClassInfoForCheckTeam(classID);
		// 팀이름
		String teamName = SelectTeamName(teamID);
		model.addAttribute("LectureName", lectureList.get(0).getClassName());
		model.addAttribute("LectureProfessor", lectureList.get(0).getClassProfessorName());
		model.addAttribute("TeamName", teamName);
		
		List<TeamUser> memberList = SelectTeamMemberInfo(teamID);
		model.addAttribute("teamList", memberList);
		model.addAttribute("TeamID", teamID);
	}

	@Override
	public boolean isTeamMember(String loginID, int teamID) {
		List<TeamUser> memberList = SelectTeamMemberInfo(teamID);
		
		// 팀에 소속되지 않으면 못 들어가게 막기
		// 소속됐는데 팀장일 경우
		if (loginID.contains(memberList.get(0).getUserLoginID())) {
			return true;
		}
		
		// 소속됐는데 팀원일 경우
		for (int i = 0; i < memberList.size(); i++) {
			if (loginID.contains(memberList.get(i).getUserLoginID())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<MergeTeam> getAllTeamList() {
		List<Team> teamList = SelectTeamList();
		
		if (teamList.isEmpty()) {
			return new java.util.ArrayList<>();
		}
		
		List<MergeTeam> allInfo = new java.util.ArrayList<>();
		List<Class> classInfo = new java.util.ArrayList<>();
		
		for (int i = 0; i < teamList.size(); i++) {
			Class dtoClass = SelectClassList(teamList.get(i).getClassID());
			classInfo.add(dtoClass);
			
			MergeTeam mergeTeam = new MergeTeam();
			mergeTeam.setTeamID(Integer.toString(teamList.get(i).getTeamID()));
			mergeTeam.setTeamName(teamList.get(i).getTeamName());
			
			if (i < classInfo.size()) {
				mergeTeam.setClassProfessorName(classInfo.get(i).getClassProfessorName());
				mergeTeam.setClassName(classInfo.get(i).getClassName());
			}
			
			allInfo.add(mergeTeam);
		}
		
		return allInfo;
	}

	@Override
	public void setTeamListAttributes(org.springframework.ui.Model model) {
		List<MergeTeam> teamList = getAllTeamList();
		
		if (!teamList.isEmpty()) {
			model.addAttribute("teamList", teamList);
		}
	}

	@Override
	public void setModifyTeamAttributes(int teamID, org.springframework.ui.Model model) {
		List<TeamUser> memberList = SelectTeamMemberInfo(teamID);
		int classID = SelectClassIDForCheckTeam(teamID);
		// 과목명, 교수
		List<Class> lectureList = SelectClassInfoForCheckTeam(classID);
		// 팀이름
		String teamName = SelectTeamName(teamID);
		
		model.addAttribute("LectureName", lectureList.get(0).getClassName());
		model.addAttribute("LectureProfessor", lectureList.get(0).getClassProfessorName());
		model.addAttribute("TeamName", teamName);
		model.addAttribute("teamList", memberList);
		model.addAttribute("TeamID", teamID);
	}

	@Override
	public void updateTeamMembers(int teamID, String[] studentIDs, String[] studentNames) {
		DeleteTeamMemberInfo(teamID);
		
		for (int i = 0; i < studentIDs.length; i++) {
			User user = new User();
			user.setUserLoginID(studentIDs[i]);
			user.setUserName(studentNames[i]);
			int userID = SelectUserIDForTeamUser(user);
			String memberName = SelectLeaderName(userID);
			String memberLoginID = SelectLeaderLoginID(userID);
			
			TeamUser teamUser = new TeamUser();
			teamUser.setTeamID(teamID);
			teamUser.setUserID(userID);
			teamUser.setUserLoginID(memberLoginID);
			teamUser.setUserName(memberName);
			
			InsertTeamUserInfo(teamUser);
		}
	}

	@Override
	public void setSearchMyTeamAttributes(String loginID, org.springframework.ui.Model model) {
		List<String> teamList = new java.util.ArrayList<>();
		// teamUser
		List<Integer> teamIDList = SelectTeamNameWithLoginUser(loginID);
		java.util.ArrayList<String> teamNameList = new java.util.ArrayList<>();
		java.util.ArrayList<Integer> classList = new java.util.ArrayList<>();
		
		for (int i = 0; i < teamIDList.size(); i++) {
			String teamName = SelectTeamNameWithTeamID(teamIDList.get(i));
			// team
			Integer classID = SelectClassIDFromTeam(teamIDList.get(i));
			if (classID != null) {
				classList.add(classID);
			}
			teamNameList.add(teamName);
		}
		
		for (int i = 0; i < classList.size(); i++) {
			// class 정보 전체
			List<Class> classInfo = SelectClassInfo(classList.get(i));
			String className = classInfo.get(0).getClassName();
			String classProfessorName = classInfo.get(0).getClassProfessorName();
			List<String> tempList = new java.util.ArrayList<>();
			tempList.add(teamNameList.get(i));
			tempList.add(className);
			tempList.add(classProfessorName);
			String separatedToSpace = String.join(" ", tempList);
			teamList.add(separatedToSpace);
		}
		
		model.addAttribute("TeamList", teamList);
	}

	@Override
	public void setReviewWriteAttributes(String selectedTeam, String userName, org.springframework.ui.Model model) {
		String[] teamName = selectedTeam.split("\\s");
		String teamID = SelectTeamIDForReview(teamName[0]);
		List<TeamUser> memberList = SelectTeamMember(teamID);
		List<String> teamMemberList = new java.util.ArrayList<>();
		
		for (int i = 0; i < memberList.size(); i++) {
			List<String> teamMember = new java.util.ArrayList<>();
			if (!memberList.get(i).getUserName().equals(userName)) {
				teamMember.add(memberList.get(i).getUserName());
				teamMember.add(memberList.get(i).getUserLoginID());
				String separatedToSpace = String.join(" ", teamMember);
				teamMemberList.add(separatedToSpace);
			}
		}
		
		model.addAttribute("TeamUserList", teamMemberList);
		model.addAttribute("SelectedTeam", selectedTeam);
		
		// 팀원 띄우기 로직
	}

	@Override
	public String writeReview(String loginID, String selectedTeam, String teamMember, 
	                         String positive, String contribute, String respect, String flexible) {
		String writerUserID = SelectWriterUserID(loginID);
		String[] teamName = selectedTeam.split("\\s");
		String[] teamMemberInfo = teamMember.split("\\s");
		String userLoginID = teamMemberInfo[1];
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		UserReview userReview = new UserReview();
		userReview.setReviewDate(dateFormat.format(now));
		userReview.setUserID(SelectTeamUserID(userLoginID));
		userReview.setPositive(positive);
		userReview.setContribute(contribute);
		userReview.setRespect(respect);
		userReview.setFlexible(flexible);
		userReview.setClassName(teamName[1]);
		userReview.setClassProfessorName(teamName[2]);
		userReview.setWriterUserID(writerUserID);
		userReview.setTeamName(teamName[0]);
		
		int count = SelectColumnCount(userReview);
		
		if (count == 0) {
			InsertUserReview(userReview);
			return "Complete";
		}
		
		return "Fail";
	}

}