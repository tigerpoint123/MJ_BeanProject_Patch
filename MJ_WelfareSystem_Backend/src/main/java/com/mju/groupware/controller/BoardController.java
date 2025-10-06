package com.mju.groupware.controller;

import com.mju.groupware.constant.ConstantAdminBoardController;
import com.mju.groupware.dto.Board;
import com.mju.groupware.dto.Inquiry;
import com.mju.groupware.dto.User;
import com.mju.groupware.service.BoardService;
import com.mju.groupware.service.InquiryService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController { // TODO : 중복 코드 제거.
    private final ConstantAdminBoardController constantAdminBoardController;
    private final BoardService boardService;
    private final InquiryService inquiryService;
    private final UserInfoMethod userInfoMethod;

    // 문의 리스트
    @GetMapping("/inquiryList")
    public String inquiryList(User user, Principal principal, Model model) {
        if (principal != null) {
            userInfoMethod.GetUserInformation(
                    principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR()
            );
        }
        List<Inquiry> InquiryList = inquiryService.SelectInquiryList();
        model.addAttribute("inquiryList", InquiryList);

        return this.constantAdminBoardController.getRInquiryList();
    }

    // 문의 글 내용
    @GetMapping("/inquiryContent")
    public String inquiryContent(User user, Principal principal, HttpServletRequest request, Model model) {
        if (principal != null) {
            userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        }
        inquiryService.getInquiryContent(principal, request, model);

        return this.constantAdminBoardController.getRInquiryContent();
    }

    // 문의 글 작성
    @GetMapping("/inquiryWrite")
    public String inquiryWrite(Locale locale, User user, Principal principal, Model model) {
        if (principal != null) {
            userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        }
        inquiryService.getInquiryWrite(principal, model);

        return this.constantAdminBoardController.getRInquiryWrite();
    }

    @PostMapping("/InquiryWrite")
    public String DoInquiryeWrite(Principal principal, HttpServletRequest request, User user,
                                  Model model, HttpServletResponse response) {
        if (principal != null) {
            userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        }
        return inquiryService.postInquiryWrite(principal, request, response);
    }

    @PostMapping("/InquiryDelete.do")
    public String deleteInquiry(HttpServletRequest request) {
        inquiryService.postInquiryDelete(request);

        return this.constantAdminBoardController.getRInquiryList();
    }

    @PostMapping("/Answer.do")
    public String DoInquiryAnswer(Principal principal, HttpServletRequest request, User user, Model model) {
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        inquiryService.postInquiryAnswer(request);

        return this.constantAdminBoardController.getRRInquiryList();
    }

    @PostMapping("/AnswerDelete.do")
    public String deleteInquiryAnswer(HttpServletRequest request) {
        int IBoardID = Integer.parseInt(request.getParameter("boardID"));
        inquiryService.postDeleteInquiryAnswer(IBoardID);

        return this.constantAdminBoardController.getRRInquiryList();
    }

    // 공지사항 리스트
    @GetMapping("/noticeList")
    public String noticeList(User user, HttpServletRequest request, Model model, Principal principal) {
        if (principal != null) {
            userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        }
        List<Board> NoticeList = boardService.SelectNoticeBoardList();
        model.addAttribute("noticeList", NoticeList);

        return this.constantAdminBoardController.getRNoticeList();
    }

    // 공지사항 글 작성
    @GetMapping("/noticeWrite")
    public String noticeWrite(User user, HttpServletRequest request, Model model, Principal principal) {
        // 유저 정보
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        boardService.getNoticeWrite(principal, request, model);

        return this.constantAdminBoardController.getRNoticeWrite();
    }

    @PostMapping("/noticeWrite")
    public String DoNoticeWrite(Principal principal, HttpServletRequest request, User user, Model model, HttpServletResponse response)
            throws Exception {
        // 유저 정보
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        return boardService.postNoticeWrite(principal, request, response);
    }

    // 공지사항 글 수정
    @GetMapping("/noticeModify")
    public String noticeModify(User user, Model model, Principal principal, HttpServletRequest request) {
        // 유저 정보
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
        boardService.getNoticeModify(model, request);

        return this.constantAdminBoardController.getRNoticeModify();
    }

    @PostMapping("/NoticeModify")
    public String noticeModifyDO(Model model, Board board, HttpServletRequest request, RedirectAttributes rttr,
                                 Principal principal, @RequestParam(value = "FileDeleteList[]") String[] FileList,
                                 @RequestParam(value = "FileDeleteNameList[]") String[] FileNameList,
                                 @RequestParam(value = "BoardID") String BoardID) {
        boardService.postNoticeModify(request, principal, FileList, FileNameList);

        return this.constantAdminBoardController.getRRNoticeList();
    }

    // 공지사항 리스트에서 제목 선택시 내용 출력
    @GetMapping("/noticeContent")
    public String noticeContent(User user, Principal principal, HttpServletRequest request, Model model, Board board) {
        if (principal != null) {
            GetUserInformation(principal, user, model);
        }
        boardService.getNoticeContent(principal, model, request);

        return this.constantAdminBoardController.getRNoticeContent();
    }

    @PostMapping("/NoticeDelete.do")
    public String deleteNotice(HttpServletRequest request) {
        boardService.postDeleteNotice(request);

        return this.constantAdminBoardController.getRRNoticeList();
    }

    // 커뮤니티 리스트
    @GetMapping("/communityList")
    public String communityList(User user, HttpServletRequest request, Model model, Principal principal) {
        if (principal != null) {
            GetUserInformation(principal, user, model);
        }
        List<Board> CommunityList = boardService.getCommunityList();
        model.addAttribute("communityList", CommunityList);

        return this.constantAdminBoardController.getRCommunityList();
    }

    // 커뮤니티 글 작성
    @GetMapping("/communityWrite")
    public String communityWrite(User user, HttpServletRequest request, Model model, Principal principal) {
        GetUserInformation(principal, user, model);
        boardService.getCommunityWrite(principal, model);

        return this.constantAdminBoardController.getRCommunityWrite();
    }

    @PostMapping("/communityWrite")
    public String communityWrite(Principal principal, HttpServletRequest request, User user, Board board,
                                 Model model, HttpServletResponse response) throws IOException {
        GetUserInformation(principal, user, model);
        return boardService.postCommunityWrite(request, principal, response);
    }

    // 커뮤니티 글 수정
    @GetMapping("/communityModify")
    public String communityModify(User user, Model model, Board board, Principal principal,
                                  HttpServletRequest request) {
        GetUserInformation(principal, user, model);
        boardService.getCommunityModify(model, request);
        return this.constantAdminBoardController.getRCommunityModify();
    }

    @PostMapping("/CommunityModify.do")
    public String communityModifyDO(Model model, Board board, HttpServletRequest request, RedirectAttributes rttr,
                                    Principal principal, @RequestParam(value = "FileDeleteList[]") String[] FileList,
                                    @RequestParam(value = "FileDeleteNameList[]") String[] FileNameList,
                                    @RequestParam(value = "BoardID") String BoardID) {
        boardService.postCommunityModify(request, FileList, FileNameList, principal);
        return this.constantAdminBoardController.getRRCommunityList();
    }

    @GetMapping("/FileDown")
    public void fileDown(@RequestParam Map<String, Object> map, HttpServletResponse response) throws Exception {
        boardService.getFileDown(response, map);
    }

    // 커뮤니티 리스트에서 제목 선택시 내용 출력
    @GetMapping("/communityContent")
    public String communityContent(User user, Principal principal, HttpServletRequest request, Model model) {
        GetUserInformation(principal, user, model);
        boardService.getCommunityContent(principal, request, model);

        return this.constantAdminBoardController.getRCommunityContent();
    }

    @PostMapping("/CommunityDelete.do")
    public String deleteCommunity(HttpServletRequest request) {
        int BoardID = Integer.parseInt(request.getParameter("boardID"));
        boardService.UpdateBoardDelete(BoardID);

        return this.constantAdminBoardController.getRRCommunityList();
    }

    private void GetUserInformation(Principal principal, User user, Model model) {
        userInfoMethod.GetUserInformation(principal, user, model, this.constantAdminBoardController.getSTUDENT(), this.constantAdminBoardController.getPROFESSOR(), this.constantAdminBoardController.getADMINISTRATOR());
    }
}