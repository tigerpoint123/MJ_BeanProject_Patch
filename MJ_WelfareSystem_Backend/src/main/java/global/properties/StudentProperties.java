package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.student")
public class StudentProperties {

    private Params params = new Params();
    private Urls urls = new Urls();

    @Getter
    @Setter
    public static class Params {
        private String userName;
        private String userNameForOpen;
        private String userLoginId;
        private String userPhoneNum;
        private String userPhone;
        private String userEmail;
        private String userMajor;
        private String userGrade;
        private String grade;
        private String email;
    }

    @Getter
    @Setter
    public static class Urls {
        private String signup;
        private String mypage;
        private String modify;
    }
}

