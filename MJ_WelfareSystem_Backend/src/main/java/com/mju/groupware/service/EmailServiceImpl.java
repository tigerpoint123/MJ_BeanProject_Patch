package com.mju.groupware.service;

import com.mju.groupware.dao.UserDao;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.UserEmail;
import com.mju.groupware.email.Email;
import com.mju.groupware.email.EmailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final UserDao emailDao;
	private final EmailImpl emailImpl;
	private final Email email;

	private String Email;
	private int Num;

	@Override
	public int sendEmail(User user) {
		Random RandomNum = new Random();
		this.Num = RandomNum.nextInt(888888) + 111111; // 인증번호 생성
		this.Email = user.getUserEmail();
		System.out.println(Num);
		if (this.Email.equals("@mju.ac.kr")) {
		} else {
			emailImpl.sendEmail(Email, Num);
		}
		return Num;
	}

	@Override
	public boolean AuthNum(String authNum) {// 입력한 인증번호 가져오기
		boolean Checker;
		Checker = emailImpl.AuthNum(Integer.parseInt(authNum), this.Num);
		return Checker;

	}

	// 이메일 중복확인
	@Override
	public boolean SelectForEmailDuplicateCheck(User user) {
		// 이메일 중복
		boolean EmailChecker;
		EmailChecker = emailDao.selectForEmailDuplicateCheck(user);
		return EmailChecker;
	}

	@Override
	public List<UserEmail> PrintEmailList() {
		return email.printEmailList();
	}

	@Override
	public boolean CheckEmailLogin(String id, String pw) {
		boolean CheckEmailLogin = email.CheckEmailLogin(id, pw);
		return CheckEmailLogin;
	}

	@Override
	public List<UserEmail> GetEmailList() {
		return email.GetEmailList();
	}

	@Override
	public String sendEmailForPasswordReset(String userEmail) {
		User user = new User();
		user.setUserEmail(userEmail);
		
		// 이메일 중복검사 (등록된 이메일인지 확인)
		boolean emailCheck = emailDao.selectForEmailDuplicateCheck(user);
		
		if (!emailCheck) {
			return "등록되지 않은 이메일입니다.";
		}
		
		// 이메일 발송
		sendEmail(user);
		return "성공적으로 이메일 발송이 완료되었습니다.";
	}

	@Override
	public UserEmail getEmailContentByIndex(int index) {
		List<UserEmail> emailList = email.GetEmailList();
		
		if (emailList.isEmpty() || index < 0 || index >= emailList.size()) {
			return null;
		}
		
		return emailList.get(index);
	}
}