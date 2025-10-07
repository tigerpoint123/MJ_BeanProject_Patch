package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantTeamController;
import com.mju.groupware.dto.*;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class TeamController {

	private final UserService userService;
	private final StudentService studentService;
	private final ProfessorService professorService;
	private final UserInfoMethod userInfoMethod;
	private final TeamService teamService;
	private final BoardService boardService;

	private final ConstantTeamController constant;

	// 문서 메뉴 선택시 팀 리스트 출력
	@RequestMapping(value = "/team/myTeamList", method = RequestMethod.GET)
	public String myTeamList(Principal principal, User user, Model model, RedirectAttributes rttr) {
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		String loginID = principal.getName();

		// 서비스에서 내 팀 리스트 조회
		List<MergeTeam> mergeInfo = teamService.getMyTeamList(loginID);
		
		if (mergeInfo == null) {
			rttr.addFlashAttribute("Checker", "NoTeamList");
			return this.constant.getRRHome();
		}
		
		model.addAttribute("teamList", mergeInfo);
		return this.constant.getRMyTeamList();
	}

	// 팀 선택 시 문서 리스트 출력
	@RequestMapping(value = "/team/documentList", method = RequestMethod.GET)
	public String documentList(User user, Principal principal, HttpServletRequest request, Model model, Team team) {
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		// teamFile에서 이름주고 teamFileList가져오기
		// TFileName
		// 제목,작성자,작성일
		String TeamID = request.getParameter("no");
		List<TeamBoard> TeamBoardInfo = teamService.SelectTeamBoardListInfo(TeamID);
		model.addAttribute("documentList", TeamBoardInfo);
		model.addAttribute("TeamID", TeamID);

		return this.constant.getRDocumentList();
	}

	// 문서 내용
	@RequestMapping(value = "/team/documentContent", method = RequestMethod.GET)
	public String documentContent(User user, HttpServletRequest request, Model model, TeamBoard teamBoard,
			Principal principal) {
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());

		// TBoardID select
		String loginID = principal.getName();
		String tBoardID = request.getParameter("no");
		String teamID = request.getParameter("num");
		
		// 서비스에서 문서 내용 데이터를 Model에 설정
		teamService.setDocumentContentAttributes(loginID, tBoardID, teamID, model);

		return this.constant.getRDocumentContent();
	}

	// 문서 작성
	@RequestMapping(value = "/team/documentWrite", method = RequestMethod.GET)
	public String documentWrite(Locale locale, Model model, TeamBoard teamBoard, Team team, HttpServletRequest request,
			Principal principal, User user) {
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());

		model.addAttribute("TeamID", request.getParameter("TeamID"));
		String UserLoginID = principal.getName();
		String UserName = userService.SelectUserName(UserLoginID);

		Date Now = new Date();
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");

		model.addAttribute("BoardWriter", UserName);
		model.addAttribute("BoardDate", Date.format(Now));

		return this.constant.getRDocumentWrite();
	}

	@RequestMapping(value = "/team/documentWrite", method = RequestMethod.POST)
	public String doDocumentContent(HttpServletRequest request, Principal principal) {
		// 요청 파라미터 추출
		String loginID = principal.getName();
		String teamID = request.getParameter("TeamID");
		String documentSubject = request.getParameter("BoardSubject");
		String documentContent = request.getParameter("BoardContent");

		// 서비스에서 문서 작성 처리
		teamService.postDocumentWrite(loginID, teamID, documentSubject, documentContent, request);

		return this.constant.getRRDocumentListNO() + teamID;
	}

	// 문서 수정
	@RequestMapping(value = "/team/documentModify", method = RequestMethod.GET)
	public String documentModify(Principal principal, User user, Model model, HttpServletRequest request) {
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());

		String tBoardID = request.getParameter("tBoardID");
		String teamID = request.getParameter("TeamID");
		
		// 서비스에서 문서 수정 화면 데이터를 Model에 설정
		teamService.setDocumentModifyAttributes(tBoardID, teamID, model);

		return this.constant.getRDocumentModify();
	}

	// 문서 수정
	@RequestMapping(value = "/team/documentModify.do", method = RequestMethod.POST)
	public String doDocumentModify(HttpServletRequest request, Principal principal,
			@RequestParam(value = "FileDeleteList[]") String[] fileDeleteList,
			@RequestParam(value = "FileDeleteNameList[]") String[] fileDeleteNameList,
			@RequestParam(value = "TBoardID") String tBoardID) {
		// 요청 파라미터 추출
		String loginID = principal.getName();
		String title = request.getParameter("BoardSubject");
		String content = request.getParameter("BoardContent");
		String teamID = request.getParameter("TeamID");

		// 서비스에서 문서 수정 처리
		teamService.updateDocumentModify(loginID, tBoardID, title, content, fileDeleteList, fileDeleteNameList, request);

		return this.constant.getRRDocumentListNO() + teamID;
	}

	// 파일 다운로드
	@RequestMapping(value = "/TeamBoardFileDown")
	public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
		// 서비스에서 파일 다운로드 데이터 조회
		HashMap<String, Object> fileData = teamService.getFileDownloadData(map, this.constant.getFilePath());
		
		byte[] fileBytes = (byte[]) fileData.get("fileBytes");
		String originalFileName = (String) fileData.get("originalFileName");
		
		// Response 설정
		response.setContentType("application/octet-stream");
		response.setContentLength(fileBytes.length);
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
		response.getOutputStream().write(fileBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	// 문서 삭제
	@RequestMapping(value = "/team/DocumentDelete.do", method = RequestMethod.POST)
	public String documentDelete(HttpServletRequest request) {
		String teamID = request.getParameter("teamID");
		int tBoardID = Integer.parseInt(request.getParameter("tBoardID"));
		
		// 서비스에서 문서 삭제 처리
		teamService.deleteDocument(teamID, tBoardID);
		
		return this.constant.getRRDocumentListNO() + teamID;
	}

	// 팀 생성하기 - 강의 검색 화면
	@RequestMapping(value = "/team/searchLecture", method = RequestMethod.GET)
	public String searchLecture(User user, Model model, Principal principal, HttpServletRequest request) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		
		String lectureName = request.getParameter("LectureName");
		
		// 서비스에서 강의 검색 화면 데이터를 Model에 설정
		teamService.setSearchLectureAttributes(lectureName, model);
		
		return this.constant.getRSearchLecture();
	}

	// 팀원 생성 화면
	@RequestMapping(value = "/team/createTeam", method = RequestMethod.GET)
	public String createTeam(User user, Model model, Principal principal, HttpServletRequest request,
			RedirectAttributes rttr) {
		String loginID = principal.getName();
		user.setUserLoginID(loginID);
		
		String lectureName = request.getParameter("LectureName");
		
		// 서비스에서 팀 생성 화면 데이터를 Model에 설정
		boolean hasLecture = teamService.setCreateTeamAttributes(
			loginID, 
			lectureName, 
			model,
			this.constant.getSTUDENT(),
			this.constant.getPROFESSOR(),
			this.constant.getADMINISTRATOR()
		);
		
		if (!hasLecture) {
			rttr.addFlashAttribute("Checker", "NoLecture");
			return this.constant.getRRSearchLecture();
		}
		
		return this.constant.getRCreateTeam();
	}

	@RequestMapping(value = "/team/createTeam", method = RequestMethod.POST)
	public String createTeamDO(HttpServletRequest request, Principal principal, RedirectAttributes rttr) {
		// 요청 파라미터 추출
		String loginID = principal.getName();
		String lectureWithProfessor = request.getParameter("Lecture");
		String teamName = request.getParameter("TeamName");
		String teamLeaderName = request.getParameter("TeamLeaderName");
		String[] studentIDs = request.getParameterValues("StudentID");
		String[] studentNames = request.getParameterValues("StudentName");
		
		// 서비스에서 팀 생성 처리
		String result = teamService.createTeam(loginID, lectureWithProfessor, teamName, teamLeaderName, studentIDs, studentNames);
		
		if ("UserNotFound".equals(result)) {
			rttr.addFlashAttribute("Checker", "UserNotFound");
			return this.constant.getRRSearchLecture();
		}
		
		rttr.addFlashAttribute("Contain", "true");
		return this.constant.getRRTeamList();
	}

	// 전체 팀 리스트 조회
	@RequestMapping(value = "/team/teamList", method = RequestMethod.GET)
	public String teamList(User user, Model model, Principal principal) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		
		// 서비스에서 전체 팀 리스트 데이터를 Model에 설정
		teamService.setTeamListAttributes(model);
		
		return this.constant.getRTeamList();
	}

	// 팀 리스트 화면에서 팀 선택 시 소속된 팀 출력 화면
	@RequestMapping(value = "/team/checkTeam", method = RequestMethod.GET)
	public String checkTeam(User user, Model model, Principal principal, HttpServletRequest request,
			RedirectAttributes rttr) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());

		// 팀 아이디를 넘겨 받음
		int teamID = Integer.parseInt(request.getParameter("no"));
		String loginID = principal.getName();

		// 서비스에서 팀 정보를 Model에 설정
		teamService.setCheckTeamAttributes(loginID, teamID, model);
		
		// 팀 소속 여부 확인
		boolean isMember = teamService.isTeamMember(loginID, teamID);
		
		if (!isMember) {
			rttr.addFlashAttribute("Contain", "Nothing");
			return this.constant.getRRTeamList();
		}
		
		return this.constant.getRCheckTeam();
	}

	@RequestMapping(value = "/team/modifyTeam", method = RequestMethod.GET)
	public String modifyTeam(Principal principal, HttpServletRequest request, Model model, User user) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		
		int teamID = Integer.parseInt(request.getParameter("no"));
		
		// 서비스에서 팀 수정 화면 데이터를 Model에 설정
		teamService.setModifyTeamAttributes(teamID, model);
		
		return this.constant.getRModifyTeam();
	}

	@RequestMapping(value = "/team/modifyTeam", method = RequestMethod.POST)
	public String modifyTeamDO(HttpServletRequest request) {
		int teamID = Integer.parseInt(request.getParameter("no"));
		String[] studentIDs = request.getParameterValues("StudentID");
		String[] studentNames = request.getParameterValues("StudentName");
		
		// 서비스에서 팀 멤버 수정 처리
		teamService.updateTeamMembers(teamID, studentIDs, studentNames);
		
		return this.constant.getRRTeamList();
	}

	// 후기 작성 선택 시 팀 선택 화면
	@RequestMapping(value = "/team/searchMyTeam", method = RequestMethod.GET)
	public String searchMyTeam(Principal principal, Model model, User user, HttpServletRequest request) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		
		String loginID = principal.getName();
		
		// 서비스에서 내 팀 검색 화면 데이터를 Model에 설정
		teamService.setSearchMyTeamAttributes(loginID, model);
		
		return this.constant.getRSearchMyTeam();
	}

	// 후기 작성
	@RequestMapping(value = "/team/reviewWrite", method = RequestMethod.GET)
	public String reviewWrite(Principal principal, Model model, User user, HttpServletRequest request) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, this.constant.getSTUDENT(), this.constant.getPROFESSOR(), this.constant.getADMINISTRATOR());
		
		String selectedTeam = request.getParameter("Team");
		
		// 서비스에서 후기 작성 화면 데이터를 Model에 설정
		teamService.setReviewWriteAttributes(selectedTeam, user.getUserName(), model);

		return this.constant.getRReviewWrite();
	}

	@RequestMapping(value = "/team/reviewWrite", method = RequestMethod.POST)
	public String reviewWriteDO(Principal principal, HttpServletRequest request, RedirectAttributes rttr) {
		// 요청 파라미터 추출
		String loginID = principal.getName();
		String selectedTeam = request.getParameter("SelectedTeam");
		String teamMember = request.getParameter("TeamUser");
		String positive = request.getParameter("Positive");
		String contribute = request.getParameter("Contribute");
		String respect = request.getParameter("Respect");
		String flexible = request.getParameter("Flexible");

		// 서비스에서 후기 작성 처리
		String result = teamService.writeReview(loginID, selectedTeam, teamMember, positive, contribute, respect, flexible);

		rttr.addFlashAttribute("Checker", result);

		return this.constant.getRRSearchMyTeam();
	}

	@RequestMapping(value = "/team/DeleteTeam", method = RequestMethod.POST)
	public String deleteTeamDo(Principal principal, HttpServletRequest request, Model model, TeamUser teamUser,
			User user, Team team) {
		String TeamID = request.getParameter("no");
		teamService.DeleteTeam(TeamID);

		return this.constant.getRRTeamList();
	}

}