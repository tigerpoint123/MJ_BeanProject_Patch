package com.mju.groupware.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mju.groupware.service.LectureRoomService;
import com.mju.groupware.service.UserService;
import com.mju.groupware.service.StudentService;
import com.mju.groupware.service.ProfessorService;
import com.mju.groupware.util.UserInfoMethod;
import com.mju.groupware.constant.ConstantLectureRoomController;
import com.mju.groupware.dto.LectureRoom;
import com.mju.groupware.dto.UserReservation;

@WebMvcTest(controllers = LectureRoomController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalUserModelAdvice.class))
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@Import(TestMvcSharedConfig.class)
class LectureRoomControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private LectureRoomService lectureRoomService;
    @MockBean private UserService userService;
    @MockBean private StudentService studentService;
    @MockBean private ProfessorService professorService;
    @MockBean private UserInfoMethod userInfoMethod;
    @MockBean private ConstantLectureRoomController constantLecture;

    @BeforeEach
    void setupUserProfile() {
        given(userService.SelectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("name", "someId", "UNKNOWN_ROLE")));
    }

    @Test
    @DisplayName("GET /lectureRoom/lectureRoomList returns 200")
    void lectureRoomListReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getRLectureRoomList()).willReturn("lecture/lectureRoomList");
        given(lectureRoomService.SelectLectureRoomList()).willReturn(Collections.<LectureRoom>emptyList());

        mockMvc.perform(get("/lectureRoom/lectureRoomList").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservation returns 200")
    void reservationReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getRReservation()).willReturn("lecture/reservation");
        given(lectureRoomService.SelectMaxNumOfPeople("101")).willReturn(20);
        given(lectureRoomService.SelectStartTime("101")).willReturn(Collections.<UserReservation>emptyList());

        mockMvc.perform(get("/lectureRoom/reservation").param("no", "101").param("ReservationDate", "2025-01-01").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /lectureRoom/LectureRoomReservation returns 3xx")
    void lectureRoomReservationPostReturns3xx() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getRRLectureRoomList()).willReturn("redirect:/lectureRoom/lectureRoomList");
        given(lectureRoomService.SelectMaxNumOfPeople("101")).willReturn(10);
        given(lectureRoomService.SelectLoginUserID("testUser")).willReturn("123");
        given(lectureRoomService.SelectReservationUserID(123)).willReturn(0);
        given(lectureRoomService.SelectReservationStartTimeForException("09:00")).willReturn("0");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/lectureRoom/LectureRoomReservation")
                        .principal(principal)
                        .param("ReservationStartTime", "09:00~11:00")
                        .param("roomNum", "101")
                        .param("ReservationNumOfPeople", "2"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservationConfirm returns 200 or 3xx (success case)")
    void reservationConfirmReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getNine()).willReturn("9");
        given(constantLecture.getEleven()).willReturn("11");
        given(constantLecture.getThirteen()).willReturn("13");
        given(constantLecture.getFifteen()).willReturn("15");
        given(constantLecture.getSeventeen()).willReturn("17");
        given(constantLecture.getNineteen()).willReturn("19");
        given(constantLecture.getRReservationConfirm()).willReturn("lecture/reservationConfirm");

        given(lectureRoomService.SelectUserIDForReservationConfirm("testUser")).willReturn("123");
        given(lectureRoomService.SelectLectureRoomNo("123")).willReturn(101);
        given(lectureRoomService.SelectLectureRoomLocation(101)).willReturn("A");
        given(lectureRoomService.SelectRoomFloor(101)).willReturn(1);
        given(lectureRoomService.SelectLectureRoomMaxNumOfPeople(101)).willReturn(20);
        given(lectureRoomService.SelectReservationNumOfPeople("123")).willReturn(2);
        given(lectureRoomService.SelectReservationStartTime("123")).willReturn("9");

        mockMvc.perform(get("/lectureRoom/reservationConfirm").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /lectureRoom/ReservationConfirm returns 3xx")
    void reservationConfirmPostReturns3xx() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getRRLectureRoomList()).willReturn("redirect:/lectureRoom/lectureRoomList");
        given(lectureRoomService.SelectLoginUserID("testUser")).willReturn("123");
        given(lectureRoomService.SelectRoomInfo(org.mockito.ArgumentMatchers.eq("123"), org.mockito.ArgumentMatchers.any(UserReservation.class)))
                .willReturn(new UserReservation());
        given(lectureRoomService.DeleteReservation(org.mockito.ArgumentMatchers.any(UserReservation.class))).willReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/lectureRoom/ReservationConfirm")
                        .principal(principal))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservationModify returns 200")
    void reservationModifyReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getRReservationModify()).willReturn("lecture/reservationModify");

        mockMvc.perform(get("/lectureRoom/reservationModify").principal(principal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /confirmMyReservation returns 200 (success case)")
    void confirmMyReservationReturnsOk() throws Exception {
        Principal principal = () -> "testUser";
        given(constantLecture.getNine()).willReturn("9");
        given(constantLecture.getRConfirmMyReservation()).willReturn("lecture/confirmMyReservation");

        given(lectureRoomService.SelectUserIDForReservationConfirm("testUser")).willReturn("123");
        given(lectureRoomService.SelectLectureRoomNo("123")).willReturn(101);
        given(lectureRoomService.SelectLectureRoomLocation(101)).willReturn("A");
        given(lectureRoomService.SelectRoomFloor(101)).willReturn(1);
        given(lectureRoomService.SelectLectureRoomMaxNumOfPeople(101)).willReturn(20);
        given(lectureRoomService.SelectReservationNumOfPeople("123")).willReturn(2);
        given(lectureRoomService.SelectReservationStartTime("123")).willReturn("9");

        mockMvc.perform(get("/confirmMyReservation").principal(principal))
                .andExpect(status().isOk());
    }
}
