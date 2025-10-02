package com.mju.groupware.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mju.groupware.service.SearchService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.util.UserInfoMethod;
import com.mju.groupware.constant.ConstantSearchController;
import com.mju.groupware.dto.SearchKeyWord;
import com.mju.groupware.dto.UserReview;

@WebMvcTest(controllers = SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
class SearchControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SearchService searchService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private ConstantSearchController constant;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "testInternalResourceViewResolver")
        ViewResolver testViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/WEB-INF/views/");
            resolver.setSuffix(".jsp");
            return resolver;
        }
    }

    @BeforeEach
    void setupCommon() {
        given(userService.SelectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "STUDENT")));
        given(constant.getSRole()).willReturn("STUDENT");
        given(constant.getPRole()).willReturn("PROFESSOR");
        given(constant.getARole()).willReturn("ADMINISTRATOR");
        given(constant.getRSearchUser()).willReturn("search/searchUser");
        given(constant.getRRSearchUser()).willReturn("redirect:/search/searchUser");
        given(constant.getRReviewList()).willReturn("search/reviewList");
        given(constant.getUName()).willReturn("UName");
        given(constant.getUserEmail()).willReturn("UserEmail");
        given(constant.getPhoneNum()).willReturn("PhoneNum");
    }

    @Test
    @DisplayName("GET /search/searchUser 는 200을 반환한다")
    void searchUserReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        mockMvc.perform(get("/search/searchUser").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /search/searchUser.do 는 200을 반환한다")
    void doSearchUserReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        // 빈 결과
        given(searchService.SelectKeyWord(org.mockito.ArgumentMatchers.any(SearchKeyWord.class)))
                .willReturn(Collections.emptyList());
        String json = "{\"keyWord\":\"name\"}";
        mockMvc.perform(post("/search/searchUser.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /search/reviewList 는 200 또는 3xx를 반환한다(리뷰 존재 케이스)")
    void reviewListReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.SelectIDForReview("email@example.com")).willReturn("1");
        List<UserReview> reviews = Collections.singletonList(new UserReview());
        given(searchService.SelectUserReview("1")).willReturn(reviews);

        mockMvc.perform(get("/search/reviewList").principal(principal).param("no", "email@example.com"))
                .andExpect(status().isOk());
    }
}

