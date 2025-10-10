package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.search")
public class SearchProperties {

    private Roles roles = new Roles();
    private Params params = new Params();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();

    @Getter
    @Setter
    public static class Roles {
        private String student;
        private String professor;
        private String administrator;
    }

    @Getter
    @Setter
    public static class Params {
        private String userName;
        private String userEmail;
        private String phoneNum;
    }

    @Getter
    @Setter
    public static class Urls {
        private String searchUser;
        private String reviewList;
    }

    @Getter
    @Setter
    public static class Redirects {
        private String searchUser;
    }
}

