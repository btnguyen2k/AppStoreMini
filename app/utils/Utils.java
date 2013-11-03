package utils;

import asm.Constants;
import bo.AsmDao;
import bo.UserBo;

import com.github.ddth.plommon.utils.SessionUtils;

public class Utils {
    public static UserBo getCurrentUser() {
        Object userId = SessionUtils.getSession(Constants.SESSION_USER_ID);
        UserBo user = userId != null ? AsmDao.getUser(userId.toString()) : null;
        return user;
    }
}
