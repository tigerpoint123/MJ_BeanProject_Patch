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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Principal testPrincipal;

    @BeforeEach
    void setUp() {
        // Principal 설정
        testPrincipal = () -> "testUser";

        // 사용자 프로필 Mock
        given(userService.selectUserProfileInfo("testUser"))
                .willReturn(new ArrayList<>(Arrays.asList("name", "someId", "UNKNOWN_ROLE")));

        // Constant Mock - View 이름들
        given(constantLecture.getRLectureRoomList()).willReturn("lecture/lectureRoomList");
        given(constantLecture.getRReservation()).willReturn("lecture/reservation");
        given(constantLecture.getRRLectureRoomList()).willReturn("redirect:/lectureRoom/lectureRoomList");
        given(constantLecture.getRReservationConfirm()).willReturn("lecture/reservationConfirm");
        given(constantLecture.getRReservationModify()).willReturn("lecture/reservationModify");
        given(constantLecture.getRConfirmMyReservation()).willReturn("lecture/confirmMyReservation");

        // Constant Mock - 시간 상수들
        given(constantLecture.getNine()).willReturn("9");
        given(constantLecture.getEleven()).willReturn("11");
        given(constantLecture.getThirteen()).willReturn("13");
        given(constantLecture.getFifteen()).willReturn("15");
        given(constantLecture.getSeventeen()).willReturn("17");
        given(constantLecture.getNineteen()).willReturn("19");
    }

    @Test
    @DisplayName("GET /lectureRoom/lectureRoomList returns 200")
    void lectureRoomListReturnsOk() throws Exception {
        given(lectureRoomService.selectLectureRoomList()).willReturn(Collections.<LectureRoom>emptyList());

        mockMvc.perform(get("/lectureRoom/lectureRoomList").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservation returns 200")
    void reservationReturnsOk() throws Exception {
        given(lectureRoomService.selectMaxNumOfPeople("101")).willReturn(20);
        given(lectureRoomService.selectStartTime("101")).willReturn(Collections.<UserReservation>emptyList());

        mockMvc.perform(get("/lectureRoom/reservation")
                        .param("no", "101")
                        .param("ReservationDate", "2025-01-01")
                        .principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /lectureRoom/LectureRoomReservation returns 3xx")
    void lectureRoomReservationPostReturns3xx() throws Exception {
        given(lectureRoomService.isExceedingCapacity(101, 2)).willReturn(false);
        given(lectureRoomService.hasDuplicateReservation("testUser")).willReturn(false);
        given(lectureRoomService.hasTimeConflict("09:00")).willReturn(false);

        mockMvc.perform(post("/lectureRoom/LectureRoomReservation")
                        .principal(testPrincipal)
                        .param("ReservationStartTime", "09:00~11:00")
                        .param("roomNum", "101")
                        .param("ReservationNumOfPeople", "2")
                        .param("ReservationDate", "2025-01-01"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservationConfirm returns 200 (success case)")
    void reservationConfirmReturnsOk() throws Exception {
        given(lectureRoomService.selectUserIdForReservationConfirm("testUser")).willReturn("123");
        given(lectureRoomService.selectLectureRoomNo("123")).willReturn(101);
        given(lectureRoomService.getReservationConfirm(101, any(), "123"))
                .willReturn("lecture/reservationConfirm");

        mockMvc.perform(get("/lectureRoom/reservationConfirm").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /lectureRoom/ReservationConfirm returns 3xx")
    void reservationConfirmPostReturns3xx() throws Exception {
        given(lectureRoomService.cancelReservation("testUser")).willReturn(true);

        mockMvc.perform(post("/lectureRoom/ReservationConfirm").principal(testPrincipal))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /lectureRoom/reservationModify returns 200")
    void reservationModifyReturnsOk() throws Exception {
        mockMvc.perform(get("/lectureRoom/reservationModify").principal(testPrincipal))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /confirmMyReservation returns 200 (success case)")
    void confirmMyReservationReturnsOk() throws Exception {
        given(lectureRoomService.selectUserIdForReservationConfirm("testUser")).willReturn("123");
        given(lectureRoomService.selectLectureRoomNo("123")).willReturn(101);
        given(lectureRoomService.getMyReservation(any(), 101, "123"))
                .willReturn("lecture/confirmMyReservation");

        mockMvc.perform(get("/confirmMyReservation").principal(testPrincipal))
                .andExpect(status().isOk());
    }
}
