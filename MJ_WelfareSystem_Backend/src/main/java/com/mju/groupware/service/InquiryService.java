package com.mju.groupware.service;

import com.mju.groupware.dto.Inquiry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

public interface InquiryService {

	void InsertInquiry(Inquiry inquiry, HttpServletRequest request);
	
	List<Inquiry> SelectInquiryList();
	
	Inquiry SelectOneInquiryContent(String iboardID);

	String SelectLoginUserIDForInquiry(String loginID);

	void UpdateIBoardDelete(int iboardID);
	
	void InsertInquiryAnswer(Inquiry inquiry, HttpServletRequest request);
	
	void postDeleteInquiryAnswer(int iboardID);
	
	List<Inquiry> SelectMyInquiryList(String loginID);

	void getInquiryContent(Principal principal, HttpServletRequest request, Model model);

	void getInquiryWrite(Principal principal, Model model);

	String postInquiryWrite(Principal principal, HttpServletRequest request, HttpServletResponse response);

	void postInquiryDelete(HttpServletRequest request);

	void postInquiryAnswer(HttpServletRequest request);
}
