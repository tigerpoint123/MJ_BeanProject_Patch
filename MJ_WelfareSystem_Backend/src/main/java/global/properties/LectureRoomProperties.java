package com.mju.groupware.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.lecture-room")
public class LectureRoomProperties {

    private TimeSlots timeSlots = new TimeSlots();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();

    @Getter
    @Setter
    public static class TimeSlots {
        private String nine;
        private String eleven;
        private String thirteen;
        private String fifteen;
        private String seventeen;
        private String nineteen;
    }

    @Getter
    @Setter
    public static class Urls {
        private String list;
        private String reservation;
        private String reservationConfirm;
        private String reservationModify;
        private String confirmMyReservation;
    }

    @Getter
    @Setter
    public static class Redirects {
        private String reservation;
        private String list;
        private String mypageStudent;
    }
}

