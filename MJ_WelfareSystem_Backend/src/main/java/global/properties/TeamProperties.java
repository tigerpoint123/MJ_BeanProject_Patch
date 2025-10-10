package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.team")
public class TeamProperties {

    private Roles roles = new Roles();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();
    private String filePath;

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
        private String myTeamList;
        private Document document = new Document();
        private Search search = new Search();
        private String createTeam;
        private String teamList;
        private String checkTeam;
        private String modifyTeam;
        private String reviewWrite;

        @Getter
        @Setter
        public static class Document {
            private String list;
            private String content;
            private String write;
            private String modify;
        }

        @Getter
        @Setter
        public static class Search {
            private String lecture;
            private String myTeam;
        }
    }

    @Getter
    @Setter
    public static class Redirects {
        private String home;
        private String documentList;
        private String searchLecture;
        private String teamList;
        private String teamTeamList;
        private String searchMyTeam;
    }
}

