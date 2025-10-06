package com.mju.groupware.service;

import com.mju.groupware.constant.ConstantAdminBoardController;
import com.mju.groupware.dao.BoardDao;
import com.mju.groupware.dao.UserDao;
import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.TeamBoard;
import com.mju.groupware.util.BFileUtils;
import com.mju.groupware.util.TeamFileUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService { //TODO : 예외처리 추가. 로깅 추가. Transactional 추가. 입력 검증 부재. 보얀 취약점 확인
//	@Resource(name = "fileUtils")
	private final BFileUtils BfileUtils;
//	@Resource(name = "TfileUtils")
	private final TeamFileUtils TeamFileUtils;
	private final BoardDao boardDao;
    private final ConstantAdminBoardController constantAdminBoardController;
	private final UserDao userDao;

	@Override
	public List<Board> getCommunityList() {
		return boardDao.SelectCommunityBoardList();
	}

	@Override
	public List<Board> SelectNoticeBoardList() {
		return boardDao.SelectNoticeBoardList();
	}

	@Override
	public void UpdateHitCount(String boardID) {
		boardDao.UpdateHitCount(boardID);
	}

	@Override
	public void InsertBoard(Board board, HttpServletRequest request) {
		boardDao.InsertBoardInfo(board);
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		try {

			int BNo = board.getBoardID();
			board.setBno(BNo);
			List<Map<String, Object>> List = BfileUtils.InsertFileInfo(board, multipartHttpServletRequest);
			for (int i = 0, size = List.size(); i < size; i++) {
				boardDao.insertFile(List.get(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void InsertTeamDocument(TeamBoard teamBoard, HttpServletRequest request) {
		boardDao.InsertTeamDocument(teamBoard);
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		try {

			int TeamBoardID = teamBoard.getTBoardID();
			teamBoard.setTBno(TeamBoardID);
			List<Map<String, Object>> List = TeamFileUtils.InsertTeamFileInfo(teamBoard, multipartHttpServletRequest);
			for (int i = 0, Size = List.size(); i < Size; i++) {
				boardDao.InsertTeamFileInfo(List.get(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Board SelectOneCommunityContent(String boardID) {
		return boardDao.SelectOneCommunityContent(boardID);
	}

	@Override
	public Board SelectOneNoticeContent(String boardID) {
		return boardDao.SelectOneCommunityContent(boardID);
	}

	@Override
	public String SelectLoginUserID(String loginID) {
		return boardDao.SelectLoginUserID(loginID);
	}

	@Override
	public void DeleteCommunity(int boardID) {
		boardDao.DeleteCommunity(boardID);
	}

	@Override
	public void DeleteNotice(int boardID) {
		boardDao.DeleteNotice(boardID);
	}

	@Override
	public void UpdateModifiedContent(Board board, String[] FileList, String[] fileNameList,
			HttpServletRequest request) {
		boardDao.UpdateModifiedContent(board);
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

		List<Map<String, Object>> List;
		try {
			List = BfileUtils.UpdateFileInfo(board, FileList, fileNameList, multipartHttpServletRequest);
			Map<String, Object> TempMap;
			int Size = List.size();
			for (int i = 0; i < Size; i++) {
				TempMap = List.get(i);
				// 여기일단조심
				if (TempMap.get("IsNew").equals("1")) {
					boardDao.InsertFile(TempMap);
				} else {
					boardDao.UpdateFile(TempMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void UpdateTeamBoardModifiedContent(TeamBoard teamBoard, String[] fileList, String[] fileNameList,
			HttpServletRequest request) {

		boardDao.UpdateTeamBoardModifiedContent(teamBoard);

		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

		List<Map<String, Object>> List;
		try {
			List = TeamFileUtils.UpdateTeamBoardFileInfo(teamBoard, fileList, fileNameList,
					multipartHttpServletRequest);
			Map<String, Object> TempMap = null;
			int Size = List.size();
			for (int i = 0; i < Size; i++) {
				TempMap = List.get(i);
				// 여기일단조심
				if (TempMap.get("IsNew").equals("1")) {
					boardDao.InsertTeamFile(TempMap);
				} else {
					boardDao.UpdateTeamFile(TempMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Map<String, Object>> SelectCommunityFileList(int BNo) {
		List<Map<String, Object>> SelectCommunityFileList = boardDao.SelectCommunityFileList(BNo);
		return SelectCommunityFileList;
	}

	@Override
	public List<Map<String, Object>> SelectTeamBoardFileList(int BNo) {
		List<Map<String, Object>> SelectTeamBoardFileList = boardDao.SelectTeamBoardFileList(BNo);
		return SelectTeamBoardFileList;
	}

	@Override
	public Map<String, Object> SelectCommunityFileInfo(Map<String, Object> map) {
		Map<String, Object> SelectCommunityFileInfo = boardDao.SelectCommunityFileInfo(map);
		return SelectCommunityFileInfo;
	}

	@Override
	public List<Map<String, Object>> SelectNoticeFileList(int BNo) {
		return boardDao.SelectNoticeFileList(BNo);
	}

	@Override
	public Map<String, Object> SelectNoticeFileInfo(Map<String, Object> map) {
		Map<String, Object> SelectNoticeFileInfo = boardDao.SelectNoticeFileInfo(map);
		return SelectNoticeFileInfo;
	}

	@Override
	public void UpdateBoardDelete(int boardID) {
		boardDao.UpdateBoardDelete(boardID);
	}

	@Override
	public List<TeamBoard> SelectTeamBoardList() {
		return boardDao.SelectTeamBoardList();
	}

	@Override
	public TeamBoard SelectTeamBoardContent(String tBoardID) {
		return boardDao.SelectTeamBoardContent(tBoardID);
	}

	@Override
	public void UpdateTBoardDelete(int tBoardID) {
		boardDao.UpdateTBoardDelete(tBoardID);
	}

	@Override
	public String SelectWriterID(TeamBoard teamBoard) {
		return boardDao.SelectWriterID(teamBoard);
	}

	@Override
	public Map<String, Object> SelectTeamBoardFileInfo(Map<String, Object> map) {
        return boardDao.SelectTeamBoardFileInfo(map);
	}

	@Override
	public List<Board> SelectMyBoardList(String login) {
		return boardDao.SelectMyBoardList(login);
	}

	@Override
	public void communityDelete(int boardID) {
		UpdateBoardDelete(boardID);
	}

	// BoardController 메서드별 서비스 메서드 구현 (void 타입으로 통일)
	@Override
	public void getInquiryContent(Principal principal, HttpServletRequest request, Model model) {


    }
	
	@Override
	public void getNoticeWrite(Principal principal, HttpServletRequest request, Model model) {
		// 작성자 이름 자동 세팅 (disabled)
		String UserLoginID = principal.getName();
		String UserName = userDao.SelectUserName(UserLoginID);
		Date Now = new Date();
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");

		model.addAttribute(this.constantAdminBoardController.getNoticeWriter(), UserName);
		model.addAttribute(this.constantAdminBoardController.getBoardDate(), Date.format(Now));

		List<Board> NoticeList = boardDao.SelectNoticeBoardList();
		model.addAttribute("noticeList", NoticeList);
	}
	
	@Override
	public void getNoticeModify(Model model, HttpServletRequest request) {
		Board board = new Board();

		String BoardID = request.getParameter("boardID");
		board = boardDao.SelectOneCommunityContent(BoardID);
		model.addAttribute(this.constantAdminBoardController.getNoticeTitle(), board.getBoardSubject());
		model.addAttribute(this.constantAdminBoardController.getNoticeWriter(), board.getBoardWriter());
		model.addAttribute("Date", board.getBoardDate());
		model.addAttribute(this.constantAdminBoardController.getNoticeContent(), board.getBoardContent());
		model.addAttribute(this.constantAdminBoardController.getBoardID(), board.getBoardID());
		model.addAttribute(this.constantAdminBoardController.getBoardType(), board.getBoardType());

		// 수정된 file을 보여주는곳
		List<Map<String, Object>> NoticeFileList = boardDao.SelectNoticeFileList(Integer.parseInt(BoardID));
		model.addAttribute("NoticeFile", NoticeFileList);
	}
	
	@Override
	public void getNoticeContent(Principal principal, Model model, HttpServletRequest request) {
		String LoginID = principal.getName();// 로그인 한 아이디

		// 누르면 조회수 증가하는 로직
		String BoardID = request.getParameter("no");
		boardDao.UpdateHitCount(BoardID);

		/*-----------------------------------*/
		Board board = boardDao.SelectOneCommunityContent(BoardID); // 선택한 게시글을 쓴 userID가 들어감.
		model.addAttribute("NoticeTitle", board.getBoardSubject());
		model.addAttribute("NoticeWriter", board.getBoardWriter());
		model.addAttribute("BoardDate", board.getBoardDate());
		model.addAttribute("NoticeContent", board.getBoardContent());
		model.addAttribute("BoardID", BoardID);
		model.addAttribute("BoardType", board.getBoardType());

		String UserID = boardDao.SelectLoginUserID(LoginID);// 로그인한 사람의 userID를 가져오기 위함
		model.addAttribute("UserID", UserID);
		model.addAttribute("UserIDFromWriter", board.getUserID());

		List<Map<String, Object>> NoticeFileList = boardDao.SelectNoticeFileList(Integer.parseInt(BoardID));
		model.addAttribute("NoticeFile", NoticeFileList);
	}

	@Override
	public void getCommunityWrite(Principal principal, Model model) {
		List<Board> CommunityList = boardDao.SelectCommunityBoardList();

		// 작성자 이름 자동 세팅 (disabled)
		String UserLoginID = principal.getName();
		String UserName = userDao.SelectUserName(UserLoginID);
		Date Now = new Date();
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd");

		model.addAttribute(this.constantAdminBoardController.getCommunityWriter(), UserName);
		model.addAttribute(this.constantAdminBoardController.getBoardDate(), Date.format(Now));
		model.addAttribute("communityList", CommunityList);
	}
	
	@Override
	public void getCommunityModify(Model model, HttpServletRequest request) {
		String BoardID = request.getParameter("no");
		Board board = boardDao.SelectOneCommunityContent(BoardID);
		model.addAttribute(this.constantAdminBoardController.getCommunityTitle(), board.getBoardSubject());
		model.addAttribute(this.constantAdminBoardController.getCommunityWriter(), board.getBoardWriter());
		model.addAttribute("Date", board.getBoardDate());
		model.addAttribute(this.constantAdminBoardController.getCommunityContent(), board.getBoardContent());
		model.addAttribute(this.constantAdminBoardController.getBoardID(), board.getBoardID());

		// 수정된 file을 보여주는곳
		List<Map<String, Object>> CommunityFile = boardDao.SelectCommunityFileList(Integer.parseInt(BoardID));
		model.addAttribute("CommunityFile", CommunityFile);
	}
	
	@Override
	public void getCommunityContent(Principal principal, HttpServletRequest request, Model model) {
		String BoardID = request.getParameter("no");
		String LoginID = principal.getName();// 로그인 한 아이디
		boardDao.UpdateHitCount(BoardID);
		/*-----------------------------------*/
		Board board = boardDao.SelectOneCommunityContent(BoardID); // 선택한 게시글을 쓴 userID가 들어감.
		model.addAttribute(this.constantAdminBoardController.getCommunityTitle(), board.getBoardSubject());
		model.addAttribute(this.constantAdminBoardController.getCommunityWriter(), board.getBoardWriter());
		model.addAttribute(this.constantAdminBoardController.getBoardDate(), board.getBoardDate());
		model.addAttribute(this.constantAdminBoardController.getCommunityContent(), board.getBoardContent());
		model.addAttribute(this.constantAdminBoardController.getBoardID(), BoardID);

		String UserID = boardDao.SelectLoginUserID(LoginID);// 로그인한 사람의 userID를 가져오기 위함
		model.addAttribute(this.constantAdminBoardController.getUserID(), UserID);
		model.addAttribute(this.constantAdminBoardController.getUserIDFromWriter(), board.getUserID());

		List<Map<String, Object>> CommunityFile = boardDao.SelectCommunityFileList(Integer.parseInt(BoardID));
		model.addAttribute("CommunityFile", CommunityFile);
	}
	
	@Override
	public void getFileDown(HttpServletResponse response, Map<String, Object> map) throws Exception {
		Map<String, Object> ResultMap = boardDao.SelectCommunityFileInfo(map);
		String StoredFileName = (String) ResultMap.get("BStoredFileName");
		String OriginalFileName = (String) ResultMap.get("BOriginalFileName");

		byte FileByte[] = org.apache.commons.io.FileUtils
				.readFileToByteArray(new File(this.constantAdminBoardController.getFilePath() + StoredFileName));
		response.setContentType("application/octet-stream");
		response.setContentLength(FileByte.length);
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(OriginalFileName, "UTF-8") + "\";");
		response.getOutputStream().write(FileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	@Override
	public String postNoticeWrite(Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Date Now = new Date();
		String Title = request.getParameter("NoticeTitle");
		String Content = request.getParameter("NoticeContent");
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String UserLoginID = principal.getName();
		int UserID = userDao.SelectUserIDFromBoardController(UserLoginID);
		String UserName = userDao.SelectUserName(UserLoginID);

		if (Title.isEmpty()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter Out = response.getWriter();
			Out.println("<script>alert('제목을 입력해주세요. ');</script>");
			Out.flush();

			return this.constantAdminBoardController.getRNoticeWrite();
		} else if (Content.isEmpty()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter Out = response.getWriter();
			Out.println("<script>alert('내용을 입력해주세요. ');</script>");
			Out.flush();

			return this.constantAdminBoardController.getRNoticeWrite();
		} else {
			Board board = new Board();

			board.setBoardSubject(Title);
			board.setBoardContent(Content);
			board.setBoardWriter(UserName);
			board.setBoardDate(Date.format(Now));
			board.setUserID(UserID);
			board.setBoardType("공지사항");

			InsertBoard(board, request);
			return this.constantAdminBoardController.getRRNoticeList();
		}
	}
	
	@Override
	public void postNoticeModify(HttpServletRequest request, Principal principal, String[] fileList, String[] fileNameList) {
		Date Now = new Date();
		Board board = new Board();
		String Title = request.getParameter("NoticeTitle");
		String Content = request.getParameter("NoticeContent");
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String UserLoginID = principal.getName();
		int BoardID2 = Integer.parseInt(request.getParameter("BoardID"));
		String UserName = userDao.SelectUserName(UserLoginID);

		board.setBno(BoardID2);
		board.setBoardSubject(Title);
		board.setBoardContent(Content);
		board.setBoardWriter(UserName);
		board.setBoardDate(Date.format(Now));
		board.setBoardID(BoardID2);
		board.setBoardType("공지사항");

		UpdateModifiedContent(board, fileList, fileNameList, request);
	}
	
	@Override
	public void postDeleteNotice(HttpServletRequest request) {
		int BoardID = Integer.parseInt(request.getParameter("boardID"));
		boardDao.UpdateBoardDelete(BoardID);
	}
	
	@Override
	public String postCommunityWrite(HttpServletRequest request, Principal principal, HttpServletResponse response) throws IOException {
		Date Now = new Date();
		String Title = request.getParameter("CommunityTitle");
		String Content = request.getParameter("CommunityContent");
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String UserLoginID = principal.getName();
		int UserID = userDao.SelectUserIDFromBoardController(UserLoginID);
		String UserName = userDao.SelectUserName(UserLoginID);

		if (Title.isEmpty()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter Out = response.getWriter();
			Out.println("<script>alert('제목을 입력해주세요. ');</script>");
			Out.flush();

			return this.constantAdminBoardController.getRCommunityWrite();
		} else if (Content.isEmpty()) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter Out = response.getWriter();
			Out.println("<script>alert('내용을 입력해주세요. ');</script>");
			Out.flush();

			return this.constantAdminBoardController.getRCommunityWrite();
		} else {
			Board board = new Board();

			board.setBoardSubject(Title);
			board.setBoardContent(Content);
			board.setBoardWriter(UserName);
			board.setBoardDate(Date.format(Now));
			board.setUserID(UserID);
			board.setBoardType("커뮤니티");

			InsertBoard(board, request);

			return this.constantAdminBoardController.getRRCommunityList();
		}
	}
	
	@Override
	public void postCommunityModify(HttpServletRequest request, String[] fileList, String[] fileNameList, Principal principal) {
		Date Now = new Date();
		Board board = new Board();

		String Title = request.getParameter("CommunityTitle");
		String Content = request.getParameter("CommunityContent");
		SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int BoardID2 = Integer.parseInt(request.getParameter("BoardID"));
		String UserLoginID = principal.getName();// 로그인 한 아이디
		String UserName = userDao.SelectUserName(UserLoginID);

		board.setBno(BoardID2);
		board.setBoardSubject(Title);
		board.setBoardContent(Content);
		board.setBoardWriter(UserName);
		board.setBoardDate(Date.format(Now));
		board.setBoardID(BoardID2);

		UpdateModifiedContent(board, fileList, fileNameList, request);
	}
	
}
