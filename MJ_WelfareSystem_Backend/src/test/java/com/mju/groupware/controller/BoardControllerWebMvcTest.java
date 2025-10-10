package com.mju.groupware.controller;

import global.properties.BoardProperties;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.board.roles.student=STUDENT",
        "app.board.roles.professor=PROFESSOR",
        "app.board.roles.administrator=ADMINISTRATOR",
        "app.board.urls.inquiry.list=/board/inquiryList",
        "app.board.urls.inquiry.content=/board/inquiryContent",
        "app.board.urls.inquiry.write=/board/inquiryWrite",
        "app.board.urls.notice.list=/board/noticeList",
        "app.board.urls.notice.write=/board/noticeWrite",
        "app.board.urls.notice.modify=/board/noticeModify",
        "app.board.urls.notice.content=/board/noticeContent",
        "app.board.urls.community.list=/board/communityList",
        "app.board.urls.community.write=/board/communityWrite",
        "app.board.urls.community.modify=/board/communityModify",
        "app.board.urls.community.content=/board/communityContent",
        "app.board.redirects.inquiry-list=redirect:/inquiryList",
        "app.board.redirects.notice-list=redirect:/noticeList",
        "app.board.redirects.community-list=redirect:/communityList",
        "app.board.file-path=D:\\\\groupware\\\\"
})
@Import({TestMvcSharedConfig.class, BoardProperties.class})
class BoardControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean private BoardService boardService;
    @MockBean private InquiryService inquiryService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @Autowired private BoardProperties boardProps;

    private Principal testPrincipal;
    
    @BeforeEach
    void setupUserProfile() {
        // 테스트용 Principal 설정
        testPrincipal = () -> "testUser";
        
        // GetUserInformation 호출 시 필요한 최소 사용자 프로필 정보 주입
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("name", "someId", "UNKNOWN_ROLE")));
        
        // 공통 사용자 서비스 Mock 설정
        given(userService.selectUserIDFromBoardController("testUser")).willReturn(1);
        given(userService.selectUserName("testUser")).willReturn("name");
        given(userService.selectEmailForInquiry("testUser")).willReturn("email");
        given(userService.selectPhoneNumForInquiry("testUser")).willReturn("010");
        
        // BoardProperties는 @TestPropertySource를 통해 자동으로 설정됨
    }

    @Test
    @DisplayName("GET /inquiryList returns 200")
    void inquiryListReturnsOk() throws Exception {
        given(inquiryService.SelectInquiryList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/inquiryList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /noticeList returns 200")
    void noticeListReturnsOk() throws Exception {
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/noticeList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /communityList returns 200")
    void communityListReturnsOk() throws Exception {
        given(boardService.getCommunityList()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/communityList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /inquiryWrite returns 200")
    void inquiryWriteReturnsOk() throws Exception {
        given(inquiryService.SelectInquiryList()).willReturn(Collections.emptyList());
        given(userService.selectUserName("testUser")).willReturn("name");
        given(userService.selectEmailForInquiry("testUser")).willReturn("email@example.com");
        given(userService.selectPhoneNumForInquiry("testUser")).willReturn("010-0000-0000");

        mockMvc.perform(get("/inquiryWrite").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /noticeWrite returns 200")
    void noticeWriteReturnsOk() throws Exception {
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());
        given(userService.selectUserName("testUser")).willReturn("name");

        mockMvc.perform(get("/noticeWrite").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /inquiryContent returns 200")
    void inquiryContentReturnsOk() throws Exception {

        com.mju.groupware.dto.Inquiry inquiry = new com.mju.groupware.dto.Inquiry();
        inquiry.setIBoardSubject("title");
        inquiry.setIBoardWriter("writer");
        inquiry.setIBoardDate("2025-01-01");
        inquiry.setIBoardContent("content");
        inquiry.setIBoardAnswer("");
        given(inquiryService.SelectOneInquiryContent("1")).willReturn(inquiry);
        given(inquiryService.SelectLoginUserIDForInquiry("testUser")).willReturn("uid");

        mockMvc.perform(get("/inquiryContent").param("no", "1").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /noticeModify returns 200")
    void noticeModifyReturnsOk() throws Exception {
        com.mju.groupware.dto.Board board = new com.mju.groupware.dto.Board();
        board.setBoardSubject("s");
        board.setBoardWriter("w");
        board.setBoardDate("d");
        board.setBoardContent("c");
        board.setBoardID(1);
        board.setBoardType("공지사항");
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectOneNoticeContent("1")).willReturn(board);
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectNoticeFileList(1)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/noticeModify").param("boardID", "1").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /noticeContent returns 200")
    void noticeContentReturnsOk() throws Exception {
        com.mju.groupware.dto.Board board = new com.mju.groupware.dto.Board();
        board.setBoardSubject("s");
        board.setBoardWriter("w");
        board.setBoardDate("d");
        board.setBoardContent("c");
        board.setBoardID(1);
        board.setBoardType("공지사항");
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectOneCommunityContent("1")).willReturn(board);
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectLoginUserID("testUser")).willReturn("uid");
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectNoticeFileList(1)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/noticeContent").param("no", "1").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /communityModify returns 200")
    void communityModifyReturnsOk() throws Exception {
        com.mju.groupware.dto.Board board = new com.mju.groupware.dto.Board();
        board.setBoardSubject("s");
        board.setBoardWriter("w");
        board.setBoardDate("d");
        board.setBoardContent("c");
        board.setBoardID(1);
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectOneCommunityContent("1")).willReturn(board);
        given(com.mju.groupware.controller.BoardControllerWebMvcTest.this.boardService.SelectCommunityFileList(1)).willReturn(Collections.emptyList());

        mockMvc.perform(get("/communityModify").param("no", "1").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /communityContent returns 200")
    void communityContentReturnsOk() throws Exception {
        com.mju.groupware.dto.Board board = new com.mju.groupware.dto.Board();
        board.setBoardSubject("s");
        board.setBoardWriter("w");
        board.setBoardDate("d");
        board.setBoardContent("c");
        board.setBoardID(1);
        given(this.boardService.SelectOneCommunityContent("1")).willReturn(board);
        given(this.boardService.SelectLoginUserID("testUser")).willReturn("uid");

        mockMvc.perform(get("/communityContent").param("no", "1").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /InquiryWrite returns 3xx")
    void inquiryWritePostReturns3xx() throws Exception {
        // 핵심 Mock 설정 추가
        given(inquiryService.postInquiryWrite(any(), any(), any())).willReturn("redirect:/inquiryList");

        mockMvc.perform(post("/InquiryWrite")
                        .principal(testPrincipal)
                        .param("InquiryTitle", "t")
                        .param("InquiryContent", "c")
                        .param("InquiryType", "type"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /noticeWrite returns 3xx")
    void noticeWritePostReturns3xx() throws Exception {
        // 핵심 Mock 설정 추가
        given(boardService.postNoticeWrite(any(), any(), any())).willReturn("redirect:/noticeList");

        mockMvc.perform(post("/noticeWrite")
                        .principal(testPrincipal)
                        .param("NoticeTitle", "t")
                        .param("NoticeContent", "c"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /communityWrite returns 3xx")
    void communityWritePostReturns3xx() throws Exception {
        // 핵심 Mock 설정 추가
        given(boardService.postCommunityWrite(any(), any(), any())).willReturn("redirect:/communityList");

        mockMvc.perform(post("/communityWrite")
                        .principal(testPrincipal)
                        .param("CommunityTitle", "t")
                        .param("CommunityContent", "c"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /InquiryDelete.do returns 3xx")
    void inquiryDeletePostReturns3xx() throws Exception {
        mockMvc.perform(post("/InquiryDelete.do")
                        .param("boardID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /NoticeDelete.do returns 3xx")
    void noticeDeletePostReturns3xx() throws Exception {
        mockMvc.perform(post("/NoticeDelete.do")
                        .param("boardID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /CommunityDelete.do returns 3xx")
    void communityDeletePostReturns3xx() throws Exception {
        mockMvc.perform(post("/CommunityDelete.do")
                        .param("boardID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /Answer.do returns 3xx")
    void answerPostReturns3xx() throws Exception {
        mockMvc.perform(post("/Answer.do")
                        .principal(testPrincipal)
                        .param("InquiryAnswer", "answer")
                        .param("BoardID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /AnswerDelete.do returns 3xx")
    void answerDeletePostReturns3xx() throws Exception {
        mockMvc.perform(post("/AnswerDelete.do")
                        .param("boardID", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /CommunityModify.do returns 3xx")
    void communityModifyPostReturns3xx() throws Exception {
        mockMvc.perform(post("/CommunityModify.do")
                        .principal(testPrincipal)
                        .param("CommunityTitle", "t")
                        .param("CommunityContent", "c")
                        .param("BoardID", "1")
                        .param("FileDeleteList[]", "")
                        .param("FileDeleteNameList[]", ""))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /NoticeModify returns 3xx")
    void noticeModifyPostReturns3xx() throws Exception {
        mockMvc.perform(post("/NoticeModify")
                        .principal(testPrincipal)
                        .param("NoticeTitle", "t")
                        .param("NoticeContent", "c")
                        .param("BoardID", "1")
                        .param("FileDeleteList[]", "")
                        .param("FileDeleteNameList[]", ""))
                .andExpect(status().is3xxRedirection());
    }
}
