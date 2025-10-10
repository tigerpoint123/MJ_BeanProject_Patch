package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.board")
public class BoardProperties {

    private Roles roles = new Roles();
    private Urls urls = new Urls();
    private Redirects redirects = new Redirects();
    private Params params = new Params();
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
        private Inquiry inquiry = new Inquiry();
        private Notice notice = new Notice();
        private Community community = new Community();

        @Getter
        @Setter
        public static class Inquiry {
            private String list;
            private String content;
            private String write;
        }

        @Getter
        @Setter
        public static class Notice {
            private String list;
            private String write;
            private String modify;
            private String content;
        }

        @Getter
        @Setter
        public static class Community {
            private String list;
            private String write;
            private String modify;
            private String content;
        }
    }

    @Getter
    @Setter
    public static class Redirects {
        private String inquiryList;
        private String noticeList;
        private String communityList;
    }

    @Getter
    @Setter
    public static class Params {
        private Inquiry inquiry = new Inquiry();
        private Notice notice = new Notice();
        private Community community = new Community();
        private Board board = new Board();
        private User user = new User();

        @Getter
        @Setter
        public static class Inquiry {
            private String title;
            private String writer;
            private String date;
            private String content;
            private String email;
            private String phoneNum;
            private String list;
            private String type;
            private String answer;
        }

        @Getter
        @Setter
        public static class Notice {
            private String title;
            private String writer;
            private String list;
            private String file;
            private String content;
        }

        @Getter
        @Setter
        public static class Community {
            private String title;
            private String writer;
            private String list;
            private String file;
            private String content;
        }

        @Getter
        @Setter
        public static class Board {
            private String id;
            private String date;
            private String type;
        }

        @Getter
        @Setter
        public static class User {
            private String id;
            private String idFromWriter;
        }
    }
}

