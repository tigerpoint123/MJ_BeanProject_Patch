package com.mju.groupware.service;

import com.mju.groupware.dao.TeamDao;
import com.mju.groupware.dto.*;
import com.mju.groupware.dto.Class;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void insertTeamInfo(Team team) {
		teamDao.insertTeamInfo(team);
	}

	@Override
	public int selectClassId(Class classInfo) {
		return teamDao.selectClassId(classInfo);
	}

	@Override
	public int selectUserIdForTeamUser(User user) {
		return teamDao.selectUserIdForTeamUser(user);
	}

	@Override
	public void insertTeamUserInfo(TeamUser teamUser) {
		teamDao.insertTeamUserInfo(teamUser);
	}

	@Override
	public List<Class> selectLectureInfo(String lectureName) {
        return teamDao.selectLectureInfo(lectureName);
	}

	@Override
	public int selectTeamLeaderUserId(String name) {
		return teamDao.selectTeamLeaderUserId(name);
	}

	@Override
	public List<Team> selectTeamList() {
        return teamDao.selectTeamList();
	}

	@Override
	public Class selectClassList(int classID) {
        return teamDao.selectClassList(classID);
	}

	@Override
	public int selectClassIdForCheckTeam(int teamID) {
		return teamDao.selectClassIdForCheckTeam(teamID);
	}

	@Override
	public List<Class> selectClassInfoForCheckTeam(int classID) {
		return teamDao.selectClassInfoForCheckTeam(classID);
	}

	@Override
	public String selectTeamName(int teamID) {
		return teamDao.selectTeamName(teamID);
	}

	@Override
	public List<TeamUser> selectTeamMemberInfo(int teamID) {
		return teamDao.selectTeamMemberInfo(teamID);
	}

	@Override
	public String selectLeaderName(int userID) {
		return teamDao.selectLeaderName(userID);
	}

	@Override
	public String selectLeaderLoginId(int userID) {
		return teamDao.selectLeaderLoginId(userID);
	}

	@Override
	public List<TeamUser> selectMyTeamList(String loginID) {
		return teamDao.selectMyTeamList(loginID);
	}

	@Override
	public void deleteTeamMemberInfo(int teamID) {
		teamDao.deleteTeamMemberInfo(teamID);
	}

	@Override
	public List<Team> selectMyTeamInfo(int teamID) {
		return teamDao.selectMyTeamInfo(teamID);
	}

	@Override
	public List<Class> selectClassInfo(int classID) {
		return teamDao.selectClassInfo(classID);
	}
	
	@Override
	public List<TeamBoard> selectTeamBoardListInfo(String teamID) {
		return teamDao.selectTeamBoardListInfo(teamID);
	}

	@Override
	public Integer selectClassIdFromTeam(Integer teamID) {
		return teamDao.selectClassIdFromTeam(teamID);
	}

	@Override
	public List<Integer> selectTeamNameWithLoginUser(String name) {
		return teamDao.selectTeamNameWithLoginUser(name);
	}

	@Override
	public String selectTeamIdForReview(String teamName) {
		return teamDao.selectTeamIdForReview(teamName);
	}

	@Override
	public List<TeamUser> selectTeamMember(String teamID) {
		return teamDao.selectTeamMember(teamID);
	}

	@Override
	public String selectTeamUserId(String userLoginID) {
		return teamDao.selectTeamUserId(userLoginID);
	}

	@Override
	public void insertUserReview(UserReview userReview) {
		teamDao.insertUserReview(userReview);
	}

	@Override
	public String selectTeamLeaderLoginId(String teamID) {
		return teamDao.selectTeamLeaderLoginId(teamID);
	}

	@Override
	public void deleteTeam(String teamID) {
		teamDao.deleteTeam(teamID);
	}

	@Override
	public String selectWriterUserId(String name) {
		return teamDao.selectWriterUserId(name);
	}

	@Override
	public int selectColumnCount(UserReview userReview) {
		return teamDao.selectColumnCount(userReview);
	}

	@Override
	public String selectTeamNameWithTeamId(int teamID) {
		return teamDao.selectTeamNameWithTeamId(teamID);
	}

	@Override
	public List<MergeTeam> getMyTeamList(String loginID) {
		// teamID를 통해 classID가져오기
		List<TeamUser> teamUserListInfo = selectMyTeamList(loginID);
		
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
		List<Team> teamListInfo2 = new ArrayList<>();
		for (int i = 0; i < teamUserListInfo.size(); i++) {
			List<Team> teamListInfo = selectMyTeamInfo(teamUserListInfo.get(i).getTeamID());
			teamListInfo2.add(teamListInfo.get(0));
		}
		
		List<Class> classInfo2 = new ArrayList<>();
		for (int i = 0; i < teamListInfo2.size(); i++) {
			List<Class> classInfo = selectClassInfo(teamListInfo2.get(i).getClassID());
			classInfo2.add(classInfo.get(0));
		}
		
		List<MergeTeam> mergeInfo = new ArrayList<>();
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
	public HashMap<String, Object> getDocumentContent(String loginID, String tBoardID, String teamID) {
		HashMap<String, Object> result = new HashMap<>();
		
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
		String tWriterID = userService.selectTWriterID(tWriter);
		
		result.put("TUserID", tUserID);
		result.put("TUserIDFromWriter", tWriterID);
		
		// 파일 리스트
		List<Map<String, Object>> teamBoardFile = boardService.SelectTeamBoardFileList(Integer.parseInt(tBoardID));
		result.put("TeamBoardFile", teamBoardFile);
		result.put("TeamIDFinal", teamID);
		
		return result;
	}

	@Override
	public void setDocumentContentAttributes(String loginID, String tBoardID, String teamID, Model model) {
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
	public void postDocumentWrite(String loginID, String teamID, String boardSubject, String boardContent, HttpServletRequest request) {
		// DB에 정보저장하기
		TeamBoard teamBoard = new TeamBoard();
		
		// 작성자 select
		String documentWriter = userService.selectWriter(loginID);
		teamBoard.setTBoardSubject(boardSubject);
		teamBoard.setTBoardContent(boardContent);
		teamBoard.setTBoardWriter(documentWriter);
		teamBoard.setTUserLoginID(loginID);
		teamBoard.setTeamID(teamID);
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		List<Map<String, Object>> teamBoardFile = boardService.SelectTeamBoardFileList(Integer.parseInt(tBoardID));
		result.put("TeamBoardFile", teamBoardFile);
		result.put("TeamID", teamID);
		
		return result;
	}

	@Override
	public void setDocumentModifyAttributes(String tBoardID, String teamID, Model model) {
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
	                                 String[] fileDeleteList, String[] fileDeleteNameList, HttpServletRequest request) {
		TeamBoard teamBoard = new TeamBoard();
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int tBoardIDInt = Integer.parseInt(tBoardID);
		
		// 로그인 한 아이디
		int userID = userService.selectUserIDFromBoardController(loginID);
		String userName = userService.selectUserName(loginID);
		
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
	public HashMap<String, Object> getFileDownloadData(Map<String, Object> fileInfoMap, String filePath) {
		HashMap<String, Object> result = new HashMap<>();
		
		Map<String, Object> resultMap = boardService.SelectTeamBoardFileInfo(fileInfoMap);
		String storedFileName = (String) resultMap.get("TStoredFileName");
		String originalFileName = (String) resultMap.get("TOriginalFileName");
		
		try {
			// 파일을 저장했던 위치에서 첨부파일을 읽어 byte[]형식으로 변환한다.
			byte[] fileBytes = org.apache.commons.io.FileUtils
					.readFileToByteArray(new File(filePath + storedFileName));
			
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
	public void setSearchLectureAttributes(String lectureName, Model model) {
		// 유저 정보
		model.addAttribute("LectureName", lectureName);
	}

	@Override
	public boolean setCreateTeamAttributes(String loginID, String lectureName, Model model,
	                                       String studentRole, String professorRole, String administratorRole) {
		// 유저 정보
		// 로그인 한 아이디
		ArrayList<String> selectUserProfileInfo = new ArrayList<>();
		selectUserProfileInfo = userService.selectUserProfileInfo(loginID);
		
		if (selectUserProfileInfo.get(2).equals(studentRole)) {
			ArrayList<String> studentInfo = new ArrayList<>();
			studentInfo = studentService.selectStudentProfileInfo(selectUserProfileInfo.get(1));
			
			userInfoMethod.studentInfo(model, selectUserProfileInfo, studentInfo);
		} else if (selectUserProfileInfo.get(2).equals(professorRole)) {
			ArrayList<String> professorInfo = new ArrayList<>();
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
		
		List<Class> lectureInfo = selectLectureInfo(lectureName);
		
		if (lectureInfo.isEmpty()) {
			return false;
		}
		
		List<String> allInfo = new ArrayList<>();
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
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Class classInfo = new Class();
		classInfo.setClassName(words[0]);
		classInfo.setClassProfessorName(words[1]);
		int classID = selectClassId(classInfo);
		int leaderUserID = selectTeamLeaderUserId(loginID);
		String leaderName = selectLeaderName(leaderUserID);
		String leaderLoginID = selectLeaderLoginId(leaderUserID);
		
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
			int userID = selectUserIdForTeamUser(user);
			
			if (userID == 0) {
				return "UserNotFound";
			}
			
			if (!checker) {
				insertTeamInfo(team);
				TeamUser teamUser = new TeamUser();
				teamUser.setUserLoginID(leaderLoginID);
				teamUser.setTeamID(team.getTeamID());
				teamUser.setUserID(leaderUserID);
				teamUser.setUserName(leaderName);
				insertTeamUserInfo(teamUser);
				checker = true;
			}
			
			String memberName = selectLeaderName(userID);
			String memberLoginID = selectLeaderLoginId(userID);
			
			TeamUser teamUser = new TeamUser();
			teamUser.setTeamID(team.getTeamID());
			teamUser.setUserID(userID);
			teamUser.setUserLoginID(memberLoginID);
			teamUser.setUserName(memberName);
			insertTeamUserInfo(teamUser);
		}
		
		return null;
	}

	@Override
	public void setCheckTeamAttributes(String loginID, int teamID, Model model) {
		// 팀 아이디를 넘겨 받음
		String teamLeaderID = selectTeamLeaderLoginId(Integer.toString(teamID));
		model.addAttribute("UserLoginID", loginID);
		model.addAttribute("TeamLeaderID", teamLeaderID);
		
		int classID = selectClassIdForCheckTeam(teamID);
		// 과목명, 교수
		List<Class> lectureList = selectClassInfoForCheckTeam(classID);
		// 팀이름
		String teamName = selectTeamName(teamID);
		model.addAttribute("LectureName", lectureList.get(0).getClassName());
		model.addAttribute("LectureProfessor", lectureList.get(0).getClassProfessorName());
		model.addAttribute("TeamName", teamName);
		
		List<TeamUser> memberList = selectTeamMemberInfo(teamID);
		model.addAttribute("teamList", memberList);
		model.addAttribute("TeamID", teamID);
	}

	@Override
	public boolean isTeamMember(String loginID, int teamID) {
		List<TeamUser> memberList = selectTeamMemberInfo(teamID);
		
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
		List<Team> teamList = selectTeamList();
		
		if (teamList.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<MergeTeam> allInfo = new ArrayList<>();
		List<Class> classInfo = new ArrayList<>();
		
		for (int i = 0; i < teamList.size(); i++) {
			Class dtoClass = selectClassList(teamList.get(i).getClassID());
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
	public void setTeamListAttributes(Model model) {
		List<MergeTeam> teamList = getAllTeamList();
		
		if (!teamList.isEmpty()) {
			model.addAttribute("teamList", teamList);
		}
	}

	@Override
	public void setModifyTeamAttributes(int teamID, Model model) {
		List<TeamUser> memberList = selectTeamMemberInfo(teamID);
		int classID = selectClassIdForCheckTeam(teamID);
		// 과목명, 교수
		List<Class> lectureList = selectClassInfoForCheckTeam(classID);
		// 팀이름
		String teamName = selectTeamName(teamID);
		
		model.addAttribute("LectureName", lectureList.get(0).getClassName());
		model.addAttribute("LectureProfessor", lectureList.get(0).getClassProfessorName());
		model.addAttribute("TeamName", teamName);
		model.addAttribute("teamList", memberList);
		model.addAttribute("TeamID", teamID);
	}

	@Override
	public void updateTeamMembers(int teamID, String[] studentIDs, String[] studentNames) {
		deleteTeamMemberInfo(teamID);
		
		for (int i = 0; i < studentIDs.length; i++) {
			User user = new User();
			user.setUserLoginID(studentIDs[i]);
			user.setUserName(studentNames[i]);
			int userID = selectUserIdForTeamUser(user);
			String memberName = selectLeaderName(userID);
			String memberLoginID = selectLeaderLoginId(userID);
			
			TeamUser teamUser = new TeamUser();
			teamUser.setTeamID(teamID);
			teamUser.setUserID(userID);
			teamUser.setUserLoginID(memberLoginID);
			teamUser.setUserName(memberName);
			
			insertTeamUserInfo(teamUser);
		}
	}

	@Override
	public void setSearchMyTeamAttributes(String loginID, Model model) {
		List<String> teamList = new ArrayList<>();
		// teamUser
		List<Integer> teamIDList = selectTeamNameWithLoginUser(loginID);
		ArrayList<String> teamNameList = new ArrayList<>();
		ArrayList<Integer> classList = new ArrayList<>();
		
		for (int i = 0; i < teamIDList.size(); i++) {
			String teamName = selectTeamNameWithTeamId(teamIDList.get(i));
			// team
			Integer classID = selectClassIdFromTeam(teamIDList.get(i));
			if (classID != null) {
				classList.add(classID);
			}
			teamNameList.add(teamName);
		}
		
		for (int i = 0; i < classList.size(); i++) {
			// class 정보 전체
			List<Class> classInfo = selectClassInfo(classList.get(i));
			String className = classInfo.get(0).getClassName();
			String classProfessorName = classInfo.get(0).getClassProfessorName();
			List<String> tempList = new ArrayList<>();
			tempList.add(teamNameList.get(i));
			tempList.add(className);
			tempList.add(classProfessorName);
			String separatedToSpace = String.join(" ", tempList);
			teamList.add(separatedToSpace);
		}
		
		model.addAttribute("TeamList", teamList);
	}

	@Override
	public void setReviewWriteAttributes(String selectedTeam, String userName, Model model) {
		String[] teamName = selectedTeam.split("\\s");
		String teamID = selectTeamIdForReview(teamName[0]);
		List<TeamUser> memberList = selectTeamMember(teamID);
		List<String> teamMemberList = new ArrayList<>();
		
		for (int i = 0; i < memberList.size(); i++) {
			List<String> teamMember = new ArrayList<>();
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
		String writerUserID = selectWriterUserId(loginID);
		String[] teamName = selectedTeam.split("\\s");
		String[] teamMemberInfo = teamMember.split("\\s");
		String userLoginID = teamMemberInfo[1];
		
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		UserReview userReview = new UserReview();
		userReview.setReviewDate(dateFormat.format(now));
		userReview.setUserID(selectTeamUserId(userLoginID));
		userReview.setPositive(positive);
		userReview.setContribute(contribute);
		userReview.setRespect(respect);
		userReview.setFlexible(flexible);
		userReview.setClassName(teamName[1]);
		userReview.setClassProfessorName(teamName[2]);
		userReview.setWriterUserID(writerUserID);
		userReview.setTeamName(teamName[0]);
		
		int count = selectColumnCount(userReview);
		
		if (count == 0) {
			insertUserReview(userReview);
			return "Complete";
		}
		
		return "Fail";
	}

}