package com.mju.groupware.service;

import com.mju.groupware.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mju.groupware.dao.UserEmailDao;
import com.mju.groupware.dto.UserEmail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl implements UserEmailService {

	@Autowired
	UserEmailDao userEmailDao;
	
	private final EmailService emailService;
	
	@Override
	public void InsertCertification(UserEmail userEmail) {
		userEmailDao.InsertCertification(userEmail);
	}

	@Override
	public boolean SelectForCheckCertification(String authNum) {
		boolean Checker = userEmailDao.SelectForCheckCertification(Integer.parseInt(authNum));
		return Checker;
	}

	@Override
	public void DeleteInfo(UserEmail userEmail) {
		userEmailDao.DeleteCertification(userEmail);
	}

	@Override
	public String processEmailCertification(String userEmail, String dateFormat) {
		User user = new User();
		user.setUserEmail(userEmail);
		
		// 이메일 중복 확인
		boolean emailCheck = emailService.SelectForEmailDuplicateCheck(user);
		
		if (emailCheck) {
			return "duplicate"; // 이미 등록된 이메일
		}
		
		// 이메일 발송
		int num = emailService.sendEmail(user);
		
		// 현재 시간 계산
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date now = new Date();
		calendar.setTime(now);
		
		// 인증 정보 저장
		UserEmail userEmailDto = new UserEmail();
		userEmailDto.setUserEmail(userEmail);
		userEmailDto.setUserCertificationNum(num);
		userEmailDto.setUserCertificationTime(sdf.format(calendar.getTime()));
		userEmailDao.InsertCertification(userEmailDto);
		
		return "success";
	}

	@Override
	public boolean verifyCertification(String authNum) {
		return userEmailDao.SelectForCheckCertification(Integer.parseInt(authNum));
	}

}
