package com.mju.groupware.service;

import com.mju.groupware.dto.*;
import com.mju.groupware.dto.Class;

import java.util.HashMap;
import java.util.List;

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

    /**
     * 내 팀 리스트 조회
     * @param loginID 로그인 ID
     * @return 팀 리스트 (MergeTeam 형태), 없으면 null
     */
    List<MergeTeam> getMyTeamList(String loginID);

    /**
     * 문서 내용 조회
     * @param loginID 로그인 ID
     * @param tBoardID 게시글 ID
     * @param teamID 팀 ID
     * @return 문서 내용 데이터 (HashMap)
     */
    HashMap<String, Object> getDocumentContent(String loginID, String tBoardID, String teamID);

    /**
     * 문서 내용 데이터를 Model에 설정
     * @param loginID 로그인 ID
     * @param tBoardID 게시글 ID
     * @param teamID 팀 ID
     * @param model Spring MVC Model
     */
    void setDocumentContentAttributes(String loginID, String tBoardID, String teamID, org.springframework.ui.Model model);

    /**
     * 문서 작성 처리
     * @param loginID 로그인 ID
     * @param teamID 팀 ID
     * @param boardSubject 게시글 제목
     * @param boardContent 게시글 내용
     * @param request HttpServletRequest (파일 업로드용)
     */
    void postDocumentWrite(String loginID, String teamID, String boardSubject, String boardContent, jakarta.servlet.http.HttpServletRequest request);

    /**
     * 문서 수정 화면 데이터 조회
     * @param tBoardID 게시글 ID
     * @param teamID 팀 ID
     * @return 문서 수정 데이터 (HashMap)
     */
    HashMap<String, Object> getDocumentModify(String tBoardID, String teamID);

    /**
     * 문서 수정 화면 데이터를 Model에 설정
     * @param tBoardID 게시글 ID
     * @param teamID 팀 ID
     * @param model Spring MVC Model
     */
    void setDocumentModifyAttributes(String tBoardID, String teamID, org.springframework.ui.Model model);

    /**
     * 문서 수정 처리
     * @param loginID 로그인 ID
     * @param tBoardID 게시글 ID
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param fileDeleteList 삭제할 파일 리스트
     * @param fileDeleteNameList 삭제할 파일명 리스트
     * @param request HttpServletRequest (파일 업로드용)
     */
    void updateDocumentModify(String loginID, String tBoardID, String title, String content, 
                             String[] fileDeleteList, String[] fileDeleteNameList, jakarta.servlet.http.HttpServletRequest request);

    /**
     * 파일 다운로드 데이터 조회
     * @param fileInfoMap 파일 정보 맵
     * @param filePath 파일 저장 경로
     * @return 파일 데이터 (fileBytes, originalFileName)
     */
    HashMap<String, Object> getFileDownloadData(java.util.Map<String, Object> fileInfoMap, String filePath);

    /**
     * 문서 삭제 처리
     * @param teamID 팀 ID
     * @param tBoardID 게시글 ID
     */
    void deleteDocument(String teamID, int tBoardID);

    /**
     * 강의 검색 화면 데이터를 Model에 설정
     * @param lectureName 강의명
     * @param model Spring MVC Model
     */
    void setSearchLectureAttributes(String lectureName, org.springframework.ui.Model model);

    /**
     * 팀 생성 화면 데이터 조회 및 설정
     * @param loginID 로그인 ID
     * @param lectureName 강의명
     * @param model Spring MVC Model
     * @param studentRole 학생 역할 코드
     * @param professorRole 교수 역할 코드
     * @param administratorRole 관리자 역할 코드
     * @return 강의 정보 존재 여부 (true: 존재, false: 없음)
     */
    boolean setCreateTeamAttributes(String loginID, String lectureName, org.springframework.ui.Model model,
                                    String studentRole, String professorRole, String administratorRole);

    /**
     * 팀 생성 처리
     * @param loginID 로그인 ID
     * @param lectureWithProfessor 강의명과 교수명 (공백으로 구분)
     * @param teamName 팀명
     * @param teamLeaderName 팀장명
     * @param studentIDs 학생 ID 배열
     * @param studentNames 학생 이름 배열
     * @return 팀 생성 결과 (null: 성공, "UserNotFound": 사용자 없음)
     */
    String createTeam(String loginID, String lectureWithProfessor, String teamName, 
                     String teamLeaderName, String[] studentIDs, String[] studentNames);

    /**
     * 팀 확인 화면 데이터를 Model에 설정
     * @param loginID 로그인 ID
     * @param teamID 팀 ID
     * @param model Spring MVC Model
     */
    void setCheckTeamAttributes(String loginID, int teamID, org.springframework.ui.Model model);

    /**
     * 팀 소속 여부 확인
     * @param loginID 로그인 ID
     * @param teamID 팀 ID
     * @return true: 소속됨, false: 소속 안됨
     */
    boolean isTeamMember(String loginID, int teamID);

    /**
     * 전체 팀 리스트 조회
     * @return 전체 팀 리스트 (MergeTeam 형태)
     */
    List<MergeTeam> getAllTeamList();

    /**
     * 전체 팀 리스트 데이터를 Model에 설정
     * @param model Spring MVC Model
     */
    void setTeamListAttributes(org.springframework.ui.Model model);

    /**
     * 팀 수정 화면 데이터를 Model에 설정
     * @param teamID 팀 ID
     * @param model Spring MVC Model
     */
    void setModifyTeamAttributes(int teamID, org.springframework.ui.Model model);

    /**
     * 팀 멤버 수정 처리
     * @param teamID 팀 ID
     * @param studentIDs 학생 ID 배열
     * @param studentNames 학생 이름 배열
     */
    void updateTeamMembers(int teamID, String[] studentIDs, String[] studentNames);

    /**
     * 내 팀 검색 화면 데이터를 Model에 설정 (후기 작성용)
     * @param loginID 로그인 ID
     * @param model Spring MVC Model
     */
    void setSearchMyTeamAttributes(String loginID, org.springframework.ui.Model model);

    /**
     * 후기 작성 화면 데이터를 Model에 설정
     * @param selectedTeam 선택된 팀 정보 (공백으로 구분)
     * @param userName 현재 사용자 이름
     * @param model Spring MVC Model
     */
    void setReviewWriteAttributes(String selectedTeam, String userName, org.springframework.ui.Model model);

    /**
     * 후기 작성 처리
     * @param loginID 로그인 ID
     * @param selectedTeam 선택된 팀 정보 (공백으로 구분)
     * @param teamMember 팀원 정보 (공백으로 구분)
     * @param positive 긍정적 평가
     * @param contribute 기여도 평가
     * @param respect 존중 평가
     * @param flexible 유연성 평가
     * @return 후기 작성 결과 ("Complete": 성공, "Fail": 실패)
     */
    String writeReview(String loginID, String selectedTeam, String teamMember, 
                      String positive, String contribute, String respect, String flexible);
}
