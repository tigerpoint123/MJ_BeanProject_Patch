package com.mju.groupware.dao;

import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.TeamBoard;

import java.util.List;
import java.util.Map;

public interface BoardDao {

	void insertBoardInfo(Board board);

	List<Board> selectCommunityBoardList();
	
	List<Board> selectNoticeBoardList();

	void updateHitCount(String boardID);

	void insertFile(Map<String, Object> map);
	
	void insertTeamFileInfo(Map<String, Object> map);

	int SelectBoardID(Board board);

	Board selectOneCommunityContent(String boardID);
	
	Board SelectOneNoticeContent(String boardID);

	String selectLoginUserID(String loginID);

	void updateModifiedContent(Board board);

	void deleteCommunity(int boardID);
	
	void deleteNotice(int boardID);

	Map<String, Object> selectCommunityFileInfo(Map<String, Object> map);

	List<Map<String, Object>> selectCommunityFileList(int bNo);
	
	Map<String, Object> selectNoticeFileInfo(Map<String, Object> map);

	List<Map<String, Object>> selectNoticeFileList(int bNo);

	void updateFile(Map<String, Object> tempMap);

	void updateBoardDelete(int boardID);

	void insertTeamDocument(TeamBoard teamBoard);

	int SelectTBoardID(TeamBoard teamBoard);

	List<TeamBoard> selectTeamBoardList();

	TeamBoard selectTeamBoardContent(String tBoardID);

	List<Map<String, Object>> selectTeamBoardFileList(int bNo);

	void updateTBoardDelete(int tBoardID);

	String selectWriterID(TeamBoard teamBoard);

	void updateTeamBoardModifiedContent(TeamBoard teamBoard);

	void insertTeamFile(Map<String, Object> tempMap);

	void updateTeamFile(Map<String, Object> tempMap);

	Map<String, Object> selectTeamBoardFileInfo(Map<String, Object> map);

	List<Board> selectMyBoardList(String login);

}
