package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.home")
public class HomeProperties {

    private Urls urls = new Urls();

    @Getter
    @Setter
    public static class Urls {
        private String home;
        private String select;
        private String consent;
        private String login;
        private String adminLogin;
        private String denied;
    }
}

