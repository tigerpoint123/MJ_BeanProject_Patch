package com.mju.groupware.controller;

import global.properties.SearchProperties;
import com.mju.groupware.dto.User;
import com.mju.groupware.dto.SearchKeyWord;
import com.mju.groupware.dto.UserReview;
import com.mju.groupware.service.SearchService;
import com.mju.groupware.util.UserInfoMethod;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchProperties searchProps;
	private final UserInfoMethod userInfoMethod;
	private final SearchService searchService;

	// review 사용자 검색
	@RequestMapping(value = "/search/searchUser", method = RequestMethod.GET)
	public String searchUser(Principal principal, Model model, User user) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, 
			searchProps.getRoles().getStudent(), 
			searchProps.getRoles().getProfessor(), 
			searchProps.getRoles().getAdministrator());
		return searchProps.getUrls().getSearchUser();
	}

	@ResponseBody
	@RequestMapping(value = "/search/searchUser.do", method = { RequestMethod.GET, RequestMethod.POST })
	public List<HashMap<String, Object>> doSearchUser(Principal principal, Model model, HttpServletRequest request,
			@RequestBody SearchKeyWord searchKeyWord) {
		return searchService.searchUserInfoList(
			searchKeyWord, 
			searchProps.getRoles().getStudent(), 
			searchProps.getRoles().getProfessor(),
			searchProps.getParams().getUserName(),
			searchProps.getParams().getUserEmail(),
			searchProps.getParams().getPhoneNum()
		);
	}

	// review list 검색
	@RequestMapping(value = "/search/reviewList", method = RequestMethod.GET)
	public String reviewList(Principal principal, Model model, User user, HttpServletRequest request, RedirectAttributes rttr) {
		// 유저 정보
		userInfoMethod.getUserInformation(principal, user, model, 
			searchProps.getRoles().getStudent(), 
			searchProps.getRoles().getProfessor(), 
			searchProps.getRoles().getAdministrator());
		
		String userEmail = request.getParameter("no");
		List<UserReview> reviewList = searchService.getUserReviewListByEmail(userEmail);
		
		if (reviewList == null) {
			rttr.addFlashAttribute("checker", "NoReiveiwList");
			return searchProps.getRedirects().getSearchUser();
		}
		
		model.addAttribute("list", reviewList);
		return searchProps.getUrls().getReviewList();
	}

}
