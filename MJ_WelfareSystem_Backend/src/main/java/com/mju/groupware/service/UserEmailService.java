package com.mju.groupware.service;

import com.mju.groupware.dto.UserEmail;

public interface UserEmailService {

	// 메일 인증 정보 저장
	 void InsertCertification(UserEmail userEmail);

	// 디비에 저장된 인증번호랑 비교
	 boolean SelectForCheckCertification(String authNum);

	// 일정시간 후 인증번호 삭제
	 void DeleteInfo(UserEmail userEmail);

	// 이메일 인증번호 발송 및 저장
	String processEmailCertification(String userEmail, String dateFormat);

	// 인증번호 확인
	boolean verifyCertification(String authNum);

}
