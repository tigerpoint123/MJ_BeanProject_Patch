package global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.dao.user")
public class UserDaoProperties {

    private Roles roles = new Roles();
    private Methods methods = new Methods();

    @Getter
    @Setter
    public static class Roles {
        private String roleUser;
        private String roleAdmin;
    }

    @Getter
    @Setter
    public static class Methods {
        private String insertUser;
        private String selectByLoginId;
        private String selectForIdConfirm;
        private String selectPwdForConfirmToFindPwd;
        private String selectUserId;
        private String selectForShowPassword;
        private String selectForEmailDuplicateCheck;
        private String updateLoginDate;
        private String selectCurrentPwd;
        private String updatePwd;
        private String selectForPwdCheckBeforeModify;
        private String selectUserInfo;
        private String updateUserPhoneNum;
        private String updateUserMajor;
        private String updateUserColleges;
        private String selectUserInformation;
        private String updateTemporaryPwd;
        private String updateWithdrawal;
        private String updateDoWithdrawalRecoveryByAdmin;
        private String updateDormantOneToZero;
        private String updateUserRole;
        private String updateAdminRole;
        private String selectMyPageInfo;
        private String selectMyPageInfoById;
        private String updateUserName;
        private String updateOpenName;
        private String updateOpenEmail;
        private String updateOpenPhoneNum;
        private String updateOpenMajor;
        private String updateOpenGrade;
        private String selectUserInfoForWithdrawal;
        private String insertWithdrawalUser;
        private String deleteWithdrawalUser;
        private String deleteWithdrawalUserList;
        private String selectOpenInfo;
        private String selectUserIdFromBoardController;
        private String selectUserRole;
    }
}

