package com.mju.groupware.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
import global.properties.SearchProperties;
import com.mju.groupware.dto.SearchKeyWord;
import com.mju.groupware.dto.UserReview;

@WebMvcTest(controllers = SearchController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.search.roles.student=STUDENT",
        "app.search.roles.professor=PROFESSOR",
        "app.search.roles.administrator=ADMINISTRATOR",
        "app.search.params.user-name=UserName",
        "app.search.params.user-email=UserEmail",
        "app.search.params.phone-num=PhoneNum",
        "app.search.urls.search-user=/search/searchUser",
        "app.search.urls.review-list=/search/reviewList",
        "app.search.redirects.search-user=redirect:/search/searchUser"
})
@Import({TestMvcSharedConfig.class, SearchProperties.class})
class SearchControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private SearchService searchService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @Autowired private SearchProperties searchProps;

    @BeforeEach
    void setupCommon() {
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "STUDENT")));
        
        // SearchProperties는 @TestPropertySource를 통해 자동으로 설정됨
    }

    @Test
    @DisplayName("GET /search/searchUser returns 200")
    void searchUserReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        mockMvc.perform(get("/search/searchUser").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /search/searchUser.do returns 200")
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
    @DisplayName("GET /search/reviewList returns 200 or 3xx (reviews exist)")
    void reviewListReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.selectIDForReview("email@example.com")).willReturn("1");
        List<UserReview> reviews = Collections.singletonList(new UserReview());
        given(searchService.SelectUserReview("1")).willReturn(reviews);

        mockMvc.perform(get("/search/reviewList").principal(principal).param("no", "email@example.com"))
                .andExpect(status().isOk());
    }
}

