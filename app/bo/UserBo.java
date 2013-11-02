package bo;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.ddth.plommon.bo.BaseBo;

public class UserBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_ID = "user_id";
    public final static String COL_LOGIN_NAME = "login_name";
    public final static String COL_PASSWORD = "user_password";
    public final static String COL_EMAIL = "user_email";
    public final static String COL_GROUP_ID = "group_id";
    public final static String COL_TIMESTAMP_CREATE = "timestamp_create";

    /**
     * Hashes a password using MD5.
     * 
     * @param rawPassword
     * @return
     */
    public static String hashPassword(String rawPassword) {
        return DigestUtils.md5Hex(rawPassword).toLowerCase();
    }

    public UsergroupBo getGroup() {
        return AsmDao.getUsergroup(getGroupId());
    }

    /**
     * Authenticates a user's password.
     * 
     * @param rawPassword
     * @return
     */
    public boolean authenticate(String rawPassword) {
        String hashedPwd = hashPassword(rawPassword);
        return StringUtils.equalsIgnoreCase(hashedPwd, getPassword());
    }

    public String getId() {
        return getAttribute(COL_ID, String.class);
    }

    public UserBo setId(String id) {
        return (UserBo) setAttribute(COL_ID, id);
    }

    public String getLoginName() {
        return getAttribute(COL_LOGIN_NAME, String.class);
    }

    public UserBo setLoginName(String loginName) {
        return (UserBo) setAttribute(COL_LOGIN_NAME, loginName);
    }

    public String getPassword() {
        return getAttribute(COL_PASSWORD, String.class);
    }

    public UserBo setPassword(String password) {
        return (UserBo) setAttribute(COL_PASSWORD, password);
    }

    public String getEmail() {
        return getAttribute(COL_EMAIL, String.class);
    }

    public UserBo setEmail(String email) {
        return (UserBo) setAttribute(COL_EMAIL, email);
    }

    public String getGroupId() {
        return getAttribute(COL_GROUP_ID, String.class);
    }

    public UserBo setGroupId(String groupId) {
        return (UserBo) setAttribute(COL_GROUP_ID, groupId);
    }

    public Date getTimestampCreate() {
        return getAttribute(COL_TIMESTAMP_CREATE, Date.class);
    }

    public UserBo setTimestampCreate(Date timestamp) {
        return (UserBo) setAttribute(COL_TIMESTAMP_CREATE, timestamp);
    }
}
