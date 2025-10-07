package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdminBoardController;
import com.mju.groupware.service.*;
import com.mju.groupware.util.UserInfoMethod;
import global.config.AdminXmlConfig;
import global.config.BoardXmlConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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

@WebMvcTest(
        controllers = BoardController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        AdminXmlConfig.class,
                        BoardXmlConfig.class
                }),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
class BoardControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean private BoardService boardService;
    @MockBean private InquiryService inquiryService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private ConstantAdminBoardController constant;

    private Principal testPrincipal;
    
    @BeforeEach
    void setupUserProfile() {
        // 테스트용 Principal 설정
        testPrincipal = () -> "testUser";
        
        // GetUserInformation 호출 시 필요한 최소 사용자 프로필 정보 주입
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("name", "someId", "UNKNOWN_ROLE")));
        
        // 공통 사용자 서비스 Mock 설정
        given(userService.SelectUserIDFromBoardController("testUser")).willReturn(1);
        given(userService.SelectUserName("testUser")).willReturn("name");
        given(userService.SelectEmailForInquiry("testUser")).willReturn("email");
        given(userService.SelectPhoneNumForInquiry("testUser")).willReturn("010");
        
        // 공통 상수 Mock 설정
        given(constant.getSTUDENT()).willReturn("STUDENT");
        given(constant.getPROFESSOR()).willReturn("PROFESSOR");
        given(constant.getADMINISTRATOR()).willReturn("ADMINISTRATOR");
        
        // 리다이렉트 상수 Mock 설정
        given(constant.getRRInquiryList()).willReturn("redirect:/inquiryList");
        given(constant.getRRNoticeList()).willReturn("redirect:/noticeList");
        given(constant.getRRCommunityList()).willReturn("redirect:/communityList");
        given(constant.getRInquiryList()).willReturn("redirect:/inquiryList");
        given(constant.getRNoticeList()).willReturn("redirect:/noticeList");
        given(constant.getRCommunityList()).willReturn("redirect:/communityList");
        
        // 뷰 상수 Mock 설정
        given(constant.getRInquiryList()).willReturn("/board/inquiryList");
        given(constant.getRNoticeList()).willReturn("/board/noticeList");
        given(constant.getRCommunityList()).willReturn("/board/communityList");
        given(constant.getRInquiryContent()).willReturn("/board/inquiryContent");
        given(constant.getRInquiryWrite()).willReturn("/board/inquiryWrite");
        given(constant.getRNoticeWrite()).willReturn("/board/noticeWrite");
        given(constant.getRNoticeModify()).willReturn("/board/noticeModify");
        given(constant.getRNoticeContent()).willReturn("/board/noticeContent");
        given(constant.getRCommunityWrite()).willReturn("/board/communityWrite");
        given(constant.getRCommunityModify()).willReturn("/board/communityModify");
        given(constant.getRCommunityContent()).willReturn("/board/communityContent");
        
        // 모델 속성 상수 Mock 설정
        given(constant.getInquiryTitle()).willReturn("InquiryTitle");
        given(constant.getInquiryWriter()).willReturn("InquiryWriter");
        given(constant.getIBoardDate()).willReturn("IBoardDate");
        given(constant.getInquiryContent()).willReturn("InquiryContent");
        given(constant.getInquiryEmail()).willReturn("InquiryEmail");
        given(constant.getInquiryPhoneNum()).willReturn("InquiryPhoneNum");
        given(constant.getInquiryAnswer()).willReturn("InquiryAnswer");
        given(constant.getBoardID()).willReturn("BoardID");
        given(constant.getBoardDate()).willReturn("BoardDate");
        given(constant.getBoardType()).willReturn("BoardType");
        given(constant.getUserID()).willReturn("UserID");
        given(constant.getUserIDFromWriter()).willReturn("UserIDFromWriter");
        given(constant.getNoticeTitle()).willReturn("NoticeTitle");
        given(constant.getNoticeWriter()).willReturn("NoticeWriter");
        given(constant.getNoticeContent()).willReturn("NoticeContent");
        given(constant.getCommunityTitle()).willReturn("CommunityTitle");
        given(constant.getCommunityWriter()).willReturn("CommunityWriter");
        given(constant.getCommunityContent()).willReturn("CommunityContent");
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
        given(constant.getInquiryWriter()).willReturn("InquiryWriter");
        given(constant.getInquiryEmail()).willReturn("InquiryEmail");
        given(constant.getInquiryPhoneNum()).willReturn("InquiryPhoneNum");
        given(inquiryService.SelectInquiryList()).willReturn(Collections.emptyList());
        given(userService.SelectUserName("testUser")).willReturn("name");
        given(userService.SelectEmailForInquiry("testUser")).willReturn("email@example.com");
        given(userService.SelectPhoneNumForInquiry("testUser")).willReturn("010-0000-0000");

        mockMvc.perform(get("/inquiryWrite").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /noticeWrite returns 200")
    void noticeWriteReturnsOk() throws Exception {
        given(constant.getNoticeWriter()).willReturn("NoticeWriter");
        given(constant.getBoardDate()).willReturn("BoardDate");
        given(boardService.SelectNoticeBoardList()).willReturn(Collections.emptyList());
        given(userService.SelectUserName("testUser")).willReturn("name");

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

        given(constant.getInquiryTitle()).willReturn("InquiryTitle");
        given(constant.getInquiryWriter()).willReturn("InquiryWriter");
        given(constant.getIBoardDate()).willReturn("IBoardDate");
        given(constant.getInquiryContent()).willReturn("InquiryContent");
        given(constant.getBoardID()).willReturn("BoardID");
        given(constant.getInquiryAnswer()).willReturn("InquiryAnswer");
        given(constant.getUserID()).willReturn("UserID");
        given(constant.getUserIDFromWriter()).willReturn("UserIDFromWriter");

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
        given(constant.getNoticeTitle()).willReturn("NoticeTitle");
        given(constant.getNoticeWriter()).willReturn("NoticeWriter");
        given(constant.getNoticeContent()).willReturn("NoticeContent");
        given(constant.getBoardID()).willReturn("BoardID");
        given(constant.getBoardType()).willReturn("BoardType");

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
        given(constant.getCommunityTitle()).willReturn("CommunityTitle");
        given(constant.getCommunityWriter()).willReturn("CommunityWriter");
        given(constant.getCommunityContent()).willReturn("CommunityContent");
        given(constant.getBoardID()).willReturn("BoardID");

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
        given(constant.getCommunityTitle()).willReturn("CommunityTitle");
        given(constant.getCommunityWriter()).willReturn("CommunityWriter");
        given(constant.getBoardDate()).willReturn("BoardDate");
        given(constant.getCommunityContent()).willReturn("CommunityContent");
        given(constant.getBoardID()).willReturn("BoardID");
        given(constant.getUserID()).willReturn("UserID");
        given(constant.getUserIDFromWriter()).willReturn("UserIDFromWriter");

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
