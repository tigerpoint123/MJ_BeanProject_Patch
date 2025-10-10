package com.mju.groupware.dao;

import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.TeamBoard;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Repository
@RequiredArgsConstructor
public class BoardDaoImpl implements BoardDao {
	private final SqlSessionTemplate sqlSession;

	@Override
	public void insertBoardInfo(Board board) {
		sqlSession.insert("InsertBoard", board);
	}

	@Override
	public void insertTeamFile(Map<String, Object> map) {
		sqlSession.insert("InsertTeamFile", map);
	}

	@Override
	public void insertTeamFileInfo(Map<String, Object> map) {
		sqlSession.insert("InsertTeamFileInfo", map);
	}

	@Override
	public List<Board> selectCommunityBoardList() {
		List<Board> CommunityOutput = sqlSession.selectList("SelectCommunityBoardList");
		return CommunityOutput;
	}

	@Override
	public List<TeamBoard> selectTeamBoardList() {
		List<TeamBoard> TeamBoardOutput = sqlSession.selectList("SelectTeamBoardList");
		return TeamBoardOutput;
	}

	@Override
	public List<Board> selectNoticeBoardList() {
		List<Board> NoticeOutput = sqlSession.selectList("SelectNoticeBoardList");
		return NoticeOutput;
	}

	@Override
	public void updateHitCount(String boardID) {
		sqlSession.update("UpdateHitCount", boardID);
	}

	@Override
	public void insertFile(Map<String, Object> map) {
		sqlSession.insert("InsertFile", map);
	}

	@Override
	public int SelectBoardID(Board board) {
		int Bno = sqlSession.selectOne("SelectBoardID", board);
		return Bno;
	}

	@Override
	public Board selectOneCommunityContent(String boardID) {
		return sqlSession.selectOne("SelectOneCommunityContent", boardID);
	}

	@Override
	public Board SelectOneNoticeContent(String boardID) {
		return sqlSession.selectOne("SelectOneNoticeContent", boardID);
	}

	@Override
	public String selectLoginUserID(String loginID) {
		return sqlSession.selectOne("SelectLoginUserID", loginID);
	}

	@Override
	public void updateModifiedContent(Board board) {
		sqlSession.update("UpdateModifiedContent", board);
	}

	@Override
	public void updateTeamBoardModifiedContent(TeamBoard teamBoard) {
		sqlSession.update("UpdateTeamBoardModifiedContent", teamBoard);
	}

	@Override
	public void deleteCommunity(int boardID) {
		sqlSession.delete("DeleteCommunity", boardID);
	}

	@Override
	public void deleteNotice(int boardID) {
		sqlSession.delete("DeleteNotice", boardID);
	}

	public List<Map<String, Object>> selectCommunityFileList(int BNo) {
		List<Map<String, Object>> SelectCommunityFileList = sqlSession.selectList("SelectCommunityFileList", BNo);
		return SelectCommunityFileList;
	}

	@Override
	public Map<String, Object> selectCommunityFileInfo(Map<String, Object> map) {
		Map<String, Object> SelectCommunityFileInfo = sqlSession.selectOne("SelectCommunityFileInfo", map);
		return SelectCommunityFileInfo;
	}

	public List<Map<String, Object>> selectNoticeFileList(int BNo) {
		List<Map<String, Object>> SelectNoticeFileList = sqlSession.selectList("SelectNoticeFileList", BNo);

		return SelectNoticeFileList;
	}

	@Override
	public Map<String, Object> selectNoticeFileInfo(Map<String, Object> map) {
		Map<String, Object> SelectNoticeFileInfo = sqlSession.selectOne("SelectNoticeFileInfo", map);
		return SelectNoticeFileInfo;
	}

	@Override
	public void updateFile(Map<String, Object> map) {
		// 파일 삭제버튼을 누르면 작동하게된다.
		sqlSession.update("UpdateFile", map);
	}

	@Override
	public void updateTeamFile(Map<String, Object> map) {
		// 파일 삭제버튼을 누르면 작동하게된다.
		sqlSession.update("UpdateTeamFile", map);
	}

	@Override
	public void updateBoardDelete(int boardID) {
		sqlSession.update("UpdateBoardDelete", boardID);
	}

	@Override
	public void insertTeamDocument(TeamBoard teamBoard) {
		sqlSession.insert("InsertTeamDocument", teamBoard);
	}

	@Override
	public int SelectTBoardID(TeamBoard teamBoard) {
		int Output = sqlSession.selectOne("SelectTBoardID", teamBoard);
		return Output;
	}

	@Override
	public TeamBoard selectTeamBoardContent(String tBoardID) {
		return sqlSession.selectOne("SelectTeamBoardContent", tBoardID);
	}

	@Override
	public List<Map<String, Object>> selectTeamBoardFileList(int bNo) {
		List<Map<String, Object>> SelectTeamBoardFileList = sqlSession.selectList("SelectTeamBoardFileList", bNo);
		return SelectTeamBoardFileList;
	}

	@Override
	public void updateTBoardDelete(int tBoardID) {
		sqlSession.update("UpdateTBoardDelete", tBoardID);
	}

	@Override
	public String selectWriterID(TeamBoard teamBoard) {
		return sqlSession.selectOne("SelectWriterID", teamBoard);
	}

	@Override
	public Map<String, Object> selectTeamBoardFileInfo(Map<String, Object> map) {
		Map<String, Object> SelectCommunityFileInfo = sqlSession.selectOne("SelectTeamBoardFileInfo", map);
		return SelectCommunityFileInfo;
	}

	@Override
	public List<Board> selectMyBoardList(String login) {
		return sqlSession.selectList("SelectMyBoardList", login);
	}

}
