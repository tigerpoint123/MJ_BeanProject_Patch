package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {
    private Roles roles = new Roles();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();
    private Params params = new Params();
    private String temporaryPwd;

    @Getter
    @Setter
    public static class Roles {
        private String student;
        private String professor;
        private String administrator;
        private String roleUser;
        private String roleSuser;
        private String rolePuser;
        private String roleAdmin;
    }

    @Getter
    @Setter
    public static class Urls {
        private String home;
        private String list;
        private String sleepList;
        private String secessionList;
        private String detail;
        private Student student = new Student();
        private Professor professor = new Professor();

        @Getter
        @Setter
        public static class Student {
            private String detail;
            private String modify;
            private String manage;
            private String manageModify;
        }

        @Getter
        @Setter
        public static class Professor {
            private String detail;
            private String modify;
            private String manage;
            private String manageModify;
        }
    }

    @Getter
    @Setter
    public static class Redirects {
        private String list;
        private String sleep;
        private String secessionList;
        private String studentDetail;
        private String professorDetail;
    }

    @Getter
    @Setter
    public static class Params {
        private String userPhoneNum;
        private String userEmail;
        private String professorRoomNum;
        private String email;
    }
}
