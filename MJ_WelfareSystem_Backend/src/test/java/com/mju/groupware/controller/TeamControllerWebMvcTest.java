package com.mju.groupware.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import global.properties.TeamProperties;
import com.mju.groupware.dto.Class;
import com.mju.groupware.dto.Team;
import com.mju.groupware.dto.TeamBoard;
import com.mju.groupware.dto.TeamUser;
import com.mju.groupware.service.BoardService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.TeamService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.util.UserInfoMethod;

@WebMvcTest(controllers = TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        properties = {
                "spring.main.allow-bean-definition-overriding=true",
                "app.team.roles.student=STUDENT",
                "app.team.roles.professor=PROFESSOR",
                "app.team.roles.administrator=ADMINISTRATOR",
                "app.team.urls.my-team-list=/team/myTeamList",
                "app.team.urls.document.list=/team/documentList",
                "app.team.urls.document.content=/team/documentContent",
                "app.team.urls.document.write=/team/documentWrite",
                "app.team.urls.document.modify=/team/documentModify",
                "app.team.urls.search.lecture=/team/searchLecture",
                "app.team.urls.search.my-team=/team/searchMyTeam",
                "app.team.urls.create-team=/team/createTeam",
                "app.team.urls.team-list=/team/teamList",
                "app.team.urls.check-team=/team/checkTeam",
                "app.team.urls.modify-team=/team/modifyTeam",
                "app.team.urls.review-write=/team/reviewWrite",
                "app.team.redirects.home=redirect:/home",
                "app.team.redirects.document-list=redirect:/team/documentList?no=",
                "app.team.redirects.search-lecture=redirect:/team/searchLecture",
                "app.team.redirects.team-list=redirect:teamList",
                "app.team.redirects.team-team-list=redirect:/team/teamList",
                "app.team.redirects.search-my-team=redirect:/team/searchMyTeam",
                "app.team.file-path=C:\\\\mju\\\\"
        }
)
@Import({TestMvcSharedConfig.class, TeamProperties.class})
class TeamControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private TeamService teamService;
    @MockBean private BoardService boardService;
    
    @Autowired
    private TeamProperties teamProps;

    @BeforeEach
    void setupProfile() {
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<String>() {{ add("Name"); add("UID123"); add("UNKNOWN_ROLE"); }});
    }

    @Test
    @DisplayName("GET /team/documentList returns 200")
    void documentListReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(teamService.selectTeamBoardListInfo("10")).willReturn(Collections.<TeamBoard>emptyList());

        mockMvc.perform(get("/team/documentList").param("no", "10").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /team/documentWrite returns 200")
    void documentWriteReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.selectUserName("testUser")).willReturn("Name");

        mockMvc.perform(get("/team/documentWrite").param("TeamID", "10").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /team/documentWrite redirects")
    void documentWritePostRedirects() throws Exception {
        Principal principal = () -> "testUser";
        given(userService.selectWriter("testUser")).willReturn("writerId");

        mockMvc.perform(post("/team/documentWrite")
                        .principal(principal)
                        .param("TeamID", "10")
                        .param("BoardSubject", "subject")
                        .param("BoardContent", "content"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /team/teamList returns 200")
    void teamListReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(teamService.selectTeamList()).willReturn(Collections.<Team>emptyList());

        mockMvc.perform(get("/team/teamList").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /team/modifyTeam returns 200")
    void modifyTeamReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(teamService.selectTeamMemberInfo(100)).willReturn(Collections.<TeamUser>emptyList());
        given(teamService.selectClassIdForCheckTeam(100)).willReturn(1);
        Class c = new Class(); c.setClassName("CN"); c.setClassProfessorName("PN");
        given(teamService.selectClassInfoForCheckTeam(1)).willReturn(new ArrayList<Class>() {{ add(c); }});
        given(teamService.selectTeamName(100)).willReturn("TName");

        mockMvc.perform(get("/team/modifyTeam").param("no", "100").principal(principal))
                .andExpect(status().isOk());
    }
}


