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
import java.util.HashMap;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mju.groupware.service.CalenderService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.util.UserInfoMethod;
import com.mju.groupware.constant.ConstantScheduleController;
import com.mju.groupware.dto.Calender;

@WebMvcTest(controllers = ScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
class ScheduleControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private CalenderService calenderService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private ConstantScheduleController constant;

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
        given(calenderService.SelectUserIdForCalender("testUser")).willReturn(1);
        given(constant.getUserId()).willReturn("UserID");
        given(constant.getSRole()).willReturn("STUDENT");
        given(constant.getPRole()).willReturn("PROFESSOR");
        given(constant.getARole()).willReturn("ADMINISTRATOR");
        given(constant.getSchedule()).willReturn("schedule/mySchedule");
        given(constant.getUserID()).willReturn("UserID");
        given(constant.getScheduleID()).willReturn("ScheduleID");
        given(constant.getStart()).willReturn("start");
        given(constant.getEnd()).willReturn("end");
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
        given(calenderService.SelectSchedule(1)).willReturn(list);

        mockMvc.perform(get("/schedule/GetSchedule.do").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /schedule/AddSchedule.do 는 200을 반환한다")
    void addScheduleReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(calenderService.InsertSchedule(org.mockito.ArgumentMatchers.any(Calender.class))).willReturn(1);

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
        given(calenderService.UpdateSchedule(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.any(Calender.class)))
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
        given(calenderService.UpdateTimeInMonth(org.mockito.ArgumentMatchers.any(HashMap.class)))
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
        given(calenderService.UpdateTimeInWeek(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.any(Calender.class)))
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
        given(calenderService.DeleteSchedule(org.mockito.ArgumentMatchers.eq("1"), org.mockito.ArgumentMatchers.eq("1")))
                .willReturn(1);

        String json = "{\"id\":\"1\"}";
        mockMvc.perform(post("/schedule/DeleteSchedule.do").principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
