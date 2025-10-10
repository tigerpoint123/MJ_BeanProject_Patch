package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.dao.team")
public class TeamDaoProperties {

    private Methods methods = new Methods();

    @Getter
    @Setter
    public static class Methods {
        private String insertTeamInfo;
        private String selectClassId;
        private String selectUserIdForTeamUser;
        private String insertTeamUserInfo;
        private String selectLectureInformation;
        private String selectTeamLeaderUserId;
        private String selectTeamList;
        private String selectClassList;
        private String selectClassIdForCheckTeam;
        private String selectClassInfoForCheckTeam;
        private String selectTeamName;
        private String selectTeamMemberInfo;
        private String selectLeaderName;
        private String selectLeaderLoginId;
        private String selectMyTeamList;
        private String deleteTeamMemberInfo;
        private String selectMyTeamInfo;
        private String selectClassInfo;
        private String selectTeamBoardListInfo;
        private String selectTeamIdForDocument;
        private String selectTeamIdForDelete;
        private String selectClassIdFromTeam;
        private String selectTeamNameWithLoginUser;
        private String selectTeamIdForReview;
        private String selectTeamMember;
        private String selectTeamUserId;
        private String insertUserReview;
    }
}

