package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.user")
public class UserFunctionProperties {

    private Roles roles = new Roles();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();
    private Formats formats = new Formats();
    private Email email = new Email();
    private Params params = new Params();
    private Attributes attributes = new Attributes();

    @Getter
    @Setter
    public static class Roles {
        private String student;
        private String professor;
        private String administrator;
    }

    @Getter
    @Setter
    public static class Urls {
        private String withdrawal;
        private Signup signup = new Signup();
        private Signin signin = new Signin();
        private Mypage mypage = new Mypage();
        private EmailUrls email = new EmailUrls();
        private String home;

        @Getter
        @Setter
        public static class Signup {
            private String student;
            private String professor;
            private String select;
        }

        @Getter
        @Setter
        public static class Signin {
            private String login;
            private String findPassword;
            private String showPassword;
        }

        @Getter
        @Setter
        public static class Mypage {
            private String postList;
            private String inquiryList;
            private String checkPassword;
            private String checkPassword2;
            private String checkPassword3;
            private String modifyPassword;
            private String checkMyTeam;
        }

        @Getter
        @Setter
        public static class EmailUrls {
            private String authentication;
            private String list;
            private String content;
            private String login;
        }
    }

    @Getter
    @Setter
    public static class Redirects {
        private String withdrawal;
        private String modifyStudent;
        private String modifyProfessor;
        private String modifyPassword;
        private String signupStudent;
        private String emailList;
        private String emailLogin;
        private String mypageStudent;
        private String mypageProfessor;
        private String home;
    }

    @Getter
    @Setter
    public static class Formats {
        private String date;
        private String datetime;
    }

    @Getter
    @Setter
    public static class Email {
        private String domain;
    }

    @Getter
    @Setter
    public static class Params {
        private Withdrawal withdrawal = new Withdrawal();
        private String authNum;
        private String studentName;
        private String professorName;
        private String userName;
        private String userLoginPwd;
        private String userPhoneNum;
        private String studentNum;
        private String professorNum;
        private String emailLoginPwd;
        private String emailCheck;
        private String emailValid;
        private String btnAgree;
        private String email;
        private String userNewPwd;
        private String userNewPwdCheck;

        @Getter
        @Setter
        public static class Withdrawal {
            private String parameter1;
            private String parameter2;
        }
    }

    @Getter
    @Setter
    public static class Attributes {
        private String noticeList;
        private String communityList;
        private String myBoardList;
        private String myInquiryList;
    }
}

