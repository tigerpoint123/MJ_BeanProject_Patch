package com.mju.groupware.service;

import global.properties.BoardProperties;
import com.mju.groupware.dao.InquiryDao;
import com.mju.groupware.dao.UserDao;
import com.mju.groupware.dto.Inquiry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.PrintWriter;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService { //TODO : 예외처리 추가. 로깅 추가. Transactional 추가. 입력 검증 부재. 보얀 취약점 확인
    private final InquiryDao inquiryDao;
    private final BoardProperties boardProps;
    private final UserDao userDao;

    @Override
    public List<Inquiry> SelectInquiryList() {
        return inquiryDao.SelectInquiryList();
    }

    @Override
    public void InsertInquiry(Inquiry inquiry, HttpServletRequest request) {
        inquiryDao.InsertIBoardInfo(inquiry);
        int Ino = inquiryDao.SelectIBoardID(inquiry);
        inquiry.setIno(Ino);
    }

    @Override
    public Inquiry SelectOneInquiryContent(String iboardID) {
        return inquiryDao.SelectOneInquiryContent(iboardID);
    }

    @Override
    public String SelectLoginUserIDForInquiry(String loginID) {
        return inquiryDao.SelectLoginUserIDForInquiry(loginID);
    }

    @Override
    public void UpdateIBoardDelete(int iboardID) {
        inquiryDao.UpdateIBoardDelete(iboardID);
    }

    @Override
    public void InsertInquiryAnswer(Inquiry inquiry, HttpServletRequest request) {
        inquiryDao.InsertInquiryAnswer(inquiry);
    }

    @Override
    public void postDeleteInquiryAnswer(int iboardID) {
        inquiryDao.DeleteInquiryAnswer(iboardID);
    }

    @Override
    public List<Inquiry> SelectMyInquiryList(String loginID) {
        return inquiryDao.SelectMyInquiryList(loginID);
    }

    @Override // TODO : HttpServletRequest, response 등은 비즈니스 로직에 쓰이지 않도록.
    public void getInquiryContent(Principal principal, HttpServletRequest request, Model model) {
        String LoginID = principal.getName();
        String UserID = inquiryDao.SelectLoginUserIDForInquiry(LoginID);// 로그인한 사람의 userID를 가져오기 위함
        String IBoardID = request.getParameter("no");
        Inquiry inquiry = inquiryDao.SelectOneInquiryContent(IBoardID); // 선택한 게시글 ID가 들어감.

        model.addAttribute(boardProps.getParams().getInquiry().getTitle(), inquiry.getIBoardSubject());
        model.addAttribute(boardProps.getParams().getInquiry().getWriter(), inquiry.getIBoardWriter());
        model.addAttribute(boardProps.getParams().getInquiry().getDate(), inquiry.getIBoardDate());
        model.addAttribute(boardProps.getParams().getInquiry().getContent(), inquiry.getIBoardContent());
        model.addAttribute(boardProps.getParams().getBoard().getId(), IBoardID);
        model.addAttribute(boardProps.getParams().getInquiry().getAnswer(), inquiry.getIBoardAnswer());
        model.addAttribute(boardProps.getParams().getUser().getId(), UserID);
        model.addAttribute(boardProps.getParams().getUser().getIdFromWriter(), inquiry.getUserID());
    }

    @Override
    public void getInquiryWrite(Principal principal, Model model) {
        String UserLoginID = principal.getName();
        String UserName = userDao.selectUserName(UserLoginID);
        String UserEmail = userDao.selectEmailForInquiry(UserLoginID);
        String UserPhoneNum = userDao.selectPhoneNumForInquiry(UserLoginID);

        model.addAttribute(boardProps.getParams().getInquiry().getWriter(), UserName);
        model.addAttribute(boardProps.getParams().getInquiry().getEmail(), UserEmail);
        model.addAttribute(boardProps.getParams().getInquiry().getPhoneNum(), UserPhoneNum);

        List<Inquiry> InquiryList = inquiryDao.SelectInquiryList();
    }

    @Override
    public String postInquiryWrite(Principal principal, HttpServletRequest request, HttpServletResponse response) {
        try {
            Date Now = new Date();
            Inquiry inquiry = new Inquiry();

            String Title = request.getParameter("InquiryTitle");
            String Content = request.getParameter("InquiryContent");
            String InquiryType = request.getParameter("InquiryType");
            SimpleDateFormat Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String UserLoginID = principal.getName();
            int UserID = userDao.selectUserIDFromBoardController(UserLoginID);
            String UserName = userDao.selectUserName(UserLoginID);
            String UserEmail = userDao.selectEmailForInquiry(UserLoginID);
            String UserPhoneNum = userDao.selectPhoneNumForInquiry(UserLoginID);

            if (Title.isEmpty()) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter Out = response.getWriter();
                Out.println("<script>alert('제목을 입력해주세요. ');</script>");
                Out.flush();

                return boardProps.getUrls().getInquiry().getWrite();
            } else if (Content.isEmpty()) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter Out = response.getWriter();
                Out.println("<script>alert('내용을 입력해주세요. ');</script>");
                Out.flush();

                return boardProps.getUrls().getInquiry().getWrite();
            } else {
                inquiry.setIBoardSubject(Title);
                inquiry.setIBoardContent(Content);
                inquiry.setIBoardWriter(UserName);
                inquiry.setIBoardDate(Date.format(Now));
                inquiry.setUserID(UserID);
                inquiry.setIBoardType(InquiryType);
                inquiry.setState("답변 대기");
                inquiry.setUserEmail(UserEmail);
                inquiry.setUserPhoneNum(UserPhoneNum);

                InsertInquiry(inquiry, request);
                return boardProps.getRedirects().getInquiryList();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postInquiryDelete(HttpServletRequest request) {
        int IBoardID = Integer.parseInt(request.getParameter("boardID"));
        inquiryDao.UpdateIBoardDelete(IBoardID);
    }

    @Override
    public void postInquiryAnswer(HttpServletRequest request) {
        String IBoardAnswer = request.getParameter("InquiryAnswer");

        int IBoardID = Integer.parseInt(request.getParameter("BoardID"));

        Inquiry inquiry = new Inquiry();
        inquiry.setIBoardAnswer(IBoardAnswer);
        inquiry.setState("답변 완료");
        inquiry.setIBoardID(IBoardID);

        inquiryDao.InsertInquiryAnswer(inquiry);
    }
}
