package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.schedule")
public class ScheduleProperties {

    private Roles roles = new Roles();
    private Urls urls = new Urls();
    private Params params = new Params();

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
        private String schedule;
    }

    @Getter
    @Setter
    public static class Params {
        private String userId;
        private String scheduleId;
        private String start;
        private String end;
    }
}
