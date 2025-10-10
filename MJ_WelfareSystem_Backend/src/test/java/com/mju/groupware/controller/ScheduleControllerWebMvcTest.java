package com.mju.groupware.controller;

import global.properties.ScheduleProperties;
import com.mju.groupware.dto.Calender;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.service.ScheduleService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.util.UserInfoMethod;
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
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "app.schedule.roles.student=STUDENT",
        "app.schedule.roles.professor=PROFESSOR",
        "app.schedule.roles.administrator=ADMINISTRATOR",
        "app.schedule.urls.schedule=/schedule/schedule",
        "app.schedule.params.user-id=UserId",
        "app.schedule.params.schedule-id=scheduleID",
        "app.schedule.params.start=start",
        "app.schedule.params.end=end"
})
@Import({TestMvcSharedConfig.class, ScheduleProperties.class})
class ScheduleControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private ScheduleService scheduleService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @Autowired private ScheduleProperties scheduleProps;

    @BeforeEach
    void setupCommon() {
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("Name", "UID123", "STUDENT")));
        given(scheduleService.selectUserIdForCalender("testUser")).willReturn(1);
        
        // ScheduleProperties는 @TestPropertySource를 통해 자동으로 설정됨
    }

    @Test
    @DisplayName("GET /schedule/mySchedule 는 200을 반환한다")
    void myScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        mockMvc.perform(get("/schedule/mySchedule").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/mySchedule 는 200을 반환한다")
    void mySchedulePostReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        mockMvc.perform(post("/schedule/mySchedule").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /schedule/GetSchedule.do 는 200을 반환한다")
    void getScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        List<HashMap<String, Object>> list = Collections.emptyList();
        given(scheduleService.selectSchedule(1)).willReturn(list);

        mockMvc.perform(get("/schedule/GetSchedule.do").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/AddSchedule.do 는 200을 반환한다")
    void addScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(scheduleService.insertSchedule(org.mockito.ArgumentMatchers.any(Calender.class))).willReturn(1);

        String json = "{\"id\":\"1\",\"title\":\"t\",\"start\":\"2025-01-01\",\"end\":\"2025-01-01\"}";
        mockMvc.perform(post("/schedule/AddSchedule.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/ModifySchedule.do 는 200을 반환한다")
    void modifyScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(scheduleService.updateSchedule(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.any(Calender.class)))
                .willReturn(1);

        String json = "{\"id\":\"1\",\"title\":\"t\"}";
        mockMvc.perform(post("/schedule/ModifySchedule.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/modifyScheduleFromMonth.do 는 200을 반환한다")
    void modifyFromMonthReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(scheduleService.updateTimeInMonth(org.mockito.ArgumentMatchers.any(HashMap.class)))
                .willReturn(1);

        String json = "{\"id\":\"1\",\"start\":\"2025-01-01\",\"end\":\"2025-01-02\"}";
        mockMvc.perform(post("/schedule/modifyScheduleFromMonth.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/ModifyScheduleFromWeek.do 는 200을 반환한다")
    void modifyFromWeekReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(scheduleService.updateTimeInWeek(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.any(Calender.class)))
                .willReturn(1);

        String json = "{\"id\":\"1\",\"start\":\"2025-01-01\",\"end\":\"2025-01-02\"}";
        mockMvc.perform(post("/schedule/ModifyScheduleFromWeek.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/DeleteSchedule.do 는 200을 반환한다")
    void deleteScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(scheduleService.deleteSchedule(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1")))
                .willReturn(1);

        String json = "{\"id\":\"1\"}";
        mockMvc.perform(post("/schedule/DeleteSchedule.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
