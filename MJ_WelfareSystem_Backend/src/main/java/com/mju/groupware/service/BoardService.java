package com.mju.groupware.service;

import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.TeamBoard;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface BoardService {

	void InsertBoard(Board board, HttpServletRequest request);

	List<Board> getCommunityList();
	
	List<Board> SelectNoticeBoardList();

	void UpdateHitCount(String boardID);

	Board SelectOneCommunityContent(String boardID);
	
	Board SelectOneNoticeContent(String boardID);

	void UpdateModifiedContent(Board board, String[] fileList, String[] fileNameList, HttpServletRequest request);

	String SelectLoginUserID(String loginID);

	void DeleteCommunity(int boardID);
	
	void DeleteNotice(int boardID);

	void communityDelete(int boardID);

	List<Map<String, Object>> SelectCommunityFileList(int parseInt);
	
	List<Map<String, Object>> SelectNoticeFileList(int parseInt);
	
	Map<String, Object> SelectCommunityFileInfo(Map<String, Object> map);
			
	Map<String, Object> SelectNoticeFileInfo(Map<String, Object> map);

	void UpdateBoardDelete(int boardID);

	void InsertTeamDocument(TeamBoard teamBoard, HttpServletRequest request);

	List<TeamBoard> SelectTeamBoardList();

	TeamBoard SelectTeamBoardContent(String tBoardID);

	List<Map<String, Object>> SelectTeamBoardFileList(int parseInt);

	void UpdateTBoardDelete(int tBoardID);

	String SelectWriterID(TeamBoard teamBoard);

	void UpdateTeamBoardModifiedContent(TeamBoard teamBoard, String[] fileList, String[] fileNameList,
			HttpServletRequest request);

	Map<String, Object> SelectTeamBoardFileInfo(Map<String, Object> map);

	List<Board> SelectMyBoardList(String loginID);

	void getInquiryContent(Principal principal, HttpServletRequest request, Model model);
	
	// BoardController 메서드별 서비스 메서드들 (void 타입으로 통일)
	// GET 메서드들
	void getNoticeWrite(Principal principal, HttpServletRequest request, Model model);
	void getNoticeModify(Model model, HttpServletRequest request);
	void getNoticeContent(Principal principal, Model model, HttpServletRequest request);
	void getCommunityWrite(Principal principal, Model model);
	void getCommunityModify(Model model, HttpServletRequest request);
	void getCommunityContent(Principal principal, HttpServletRequest request, Model model);
	void getFileDown(HttpServletResponse response, Map<String, Object> map) throws Exception;
	
	// POST 메서드들
	String postNoticeWrite(Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException;
	void postNoticeModify(HttpServletRequest request, Principal principal, String[] fileList, String[] fileNameList);
	void postDeleteNotice(HttpServletRequest request);
	String postCommunityWrite(HttpServletRequest request, Principal principal, HttpServletResponse response) throws IOException;
	void postCommunityModify(HttpServletRequest request, String[] fileList, String[] fileNameList, Principal principal);
}
