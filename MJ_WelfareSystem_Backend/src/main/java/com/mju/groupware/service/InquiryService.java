package com.mju.groupware.service;

import java.util.List;

import com.mju.groupware.dto.Inquiry;
import jakarta.servlet.http.HttpServletRequest;

public interface InquiryService {

	void InsertInquiry(Inquiry inquiry, HttpServletRequest request);
	
	List<Inquiry> SelectInquiryList();
	
	Inquiry SelectOneInquiryContent(String iboardID);

	String SelectLoginUserIDForInquiry(String loginID);

	void UpdateIBoardDelete(int iboardID);
	
	void InsertInquiryAnswer(Inquiry inquiry, HttpServletRequest request);
	
	void DeleteInquiryAnswer(int iboardID);
	
	List<Inquiry> SelectMyInquiryList(String loginID);
	
}
