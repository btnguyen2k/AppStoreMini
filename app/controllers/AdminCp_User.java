package controllers;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import play.data.Form;
import play.data.Form.Field;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import utils.Utils;
import asm.Constants;
import bo.AsmDao;
import bo.UserBo;
import bo.UsergroupBo;

import com.github.ddth.plommon.utils.IdGenerator;
import compositions.AuthRequired;

public class AdminCp_User extends Controller {

    private final static String FLASH_USER_LIST = "user_list";

    /*
     * Handles GET:/admin/users
     */
    @AuthRequired
    public static Result userList() {
        String msg = flash(FLASH_USER_LIST);
        UserBo[] allUsers = AsmDao.getAllUsers();
        return Results.ok(views.html.admin.users.render(msg, allUsers));
    }

    /*
     * Handles GET:/admin/createUser
     */
    @AuthRequired
    public static Result createUser() {
        UsergroupBo[] allUsergroups = AsmDao.getAllUsergroups();
        return Results.ok(views.html.admin.user_create.render(null, null, allUsergroups));
    }

    private static Result createUserSubmitError(String errorMsg, UserBo user) {
        UsergroupBo[] allUsergroups = AsmDao.getAllUsergroups();
        return Results.ok(views.html.admin.user_create.render(errorMsg, user, allUsergroups));
    }

    private static boolean checkLoginName(String loginName) {
        final Pattern PATTERN_LOGIN_NAME = Pattern.compile("^\\w+$");
        return PATTERN_LOGIN_NAME.matcher(loginName).matches();
    }

    private static boolean checkEmail(String email) {
        final Pattern PATTERN_EMAIL = Pattern.compile("^[^@]+@[^@]+$");
        return PATTERN_EMAIL.matcher(email).matches();
    }

    /*
     * Handles POST:/admin/createApp
     */
    @AuthRequired
    public static Result createUserSubmit() {
        Form<UserBo> form = Form.form(UserBo.class).bindFromRequest();
        UserBo user = form.get();

        String loginName = user.getLoginName();
        loginName = loginName != null ? loginName.toLowerCase().trim() : null;
        if (!checkLoginName(loginName)) {
            return createUserSubmitError(Messages.get("error.invalid.loginName", loginName), user);
        }
        UserBo existingUser = AsmDao.getUserByLoginname(loginName);
        if (existingUser != null) {
            return createUserSubmitError(Messages.get("error.alreadyExist.loginName", loginName),
                    user);
        }

        String email = user.getEmail();
        email = email != null ? email.trim() : null;
        if (!checkEmail(email)) {
            return createUserSubmitError(Messages.get("error.invalid.email", email), user);
        }

        String password = user.getPassword();
        password = password != null ? password.trim() : null;
        if (StringUtils.isEmpty(password)) {
            return createUserSubmitError(Messages.get("error.invalid.password"), user);
        }
        Field field = form.field("confirmedPassword");
        String confirmedPassword = field != null ? field.value() : null;
        confirmedPassword = confirmedPassword != null ? confirmedPassword.trim() : null;
        if (!StringUtils.equals(password, confirmedPassword)) {
            return createUserSubmitError(Messages.get("error.invalid.passwords"), user);
        }

        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        user.setId(idGen.generateIdTinyHex());
        user.setTimestampCreate(new Date());
        user.setPassword(UserBo.hashPassword(password));
        user.setLoginName(loginName);
        user.setEmail(email);

        AsmDao.create(user);
        flash(FLASH_USER_LIST, Messages.get("msg.user.create.done", loginName));
        return Results.redirect(routes.AdminCp_User.userList());
    }

    private static Result checkEditUser(UserBo user) {
        if (user == null) {
            String msg = Messages.get("error.404.user");
            String url = routes.AdminCp_User.userList().url();
            return Results.ok(views.html.error.render(msg, url));
        }

        if (StringUtils.equals(Constants.USERGROUP_ADMIN, user.getGroupId())) {
            // can not edit Admin account if you are not another Admin
            UserBo currentUser = Utils.getCurrentUser();
            if (!StringUtils.equals(Constants.USERGROUP_ADMIN, currentUser.getId())) {
                String msg = Messages.get("error.403.editUser", user.getLoginName());
                String url = routes.AdminCp_User.userList().url();
                return Results.ok(views.html.error.render(msg, url));
            }
        }

        return null;
    }

    /*
     * Handles GET:/admin/editUser?id=xxx
     */
    @AuthRequired
    public static Result editUser(String id) {
        UserBo user = AsmDao.getUser(id);
        Result checkResult = checkEditUser(user);
        if (checkResult != null) {
            return checkResult;
        }

        UsergroupBo[] allUsergroups = AsmDao.getAllUsergroups();
        return Results.ok(views.html.admin.user_edit.render(null, user, allUsergroups));
    }

    /*
     * Handles POST:/admin/editUser?id=xxx
     */
    @AuthRequired
    public static Result editUserSubmit(String id) {
        UserBo user = AsmDao.getUser(id);
        Result checkResult = checkEditUser(user);
        if (checkResult != null) {
            return checkResult;
        }

        Form<UserBo> form = Form.form(UserBo.class).bindFromRequest();
        UserBo submittedUser = form.get();

        String email = submittedUser.getEmail();
        email = email != null ? email.trim() : null;
        if (!checkEmail(email)) {
            return createUserSubmitError(Messages.get("error.invalid.email", email), submittedUser);
        }

        String password = submittedUser.getPassword();
        password = password != null ? password.trim() : null;
        if (!StringUtils.isEmpty(password)) {
            Field field = form.field("confirmedPassword");
            String confirmedPassword = field != null ? field.value() : null;
            confirmedPassword = confirmedPassword != null ? confirmedPassword.trim() : null;
            if (!StringUtils.equals(password, confirmedPassword)) {
                return createUserSubmitError(Messages.get("error.invalid.passwords"), submittedUser);
            }
        }

        user.setGroupId(submittedUser.getGroupId());
        user.setEmail(submittedUser.getEmail());
        if (!StringUtils.isBlank(password)) {
            user.setPassword(UserBo.hashPassword(password));
        }
        user = AsmDao.update(user);
        flash(FLASH_USER_LIST, Messages.get("msg.edit.create.done", user.getLoginName()));
        return Results.redirect(routes.AdminCp_User.userList());
    }

    /*
     * Handles GET:/admin/deleteUser?id=xxx
     */
    @AuthRequired
    public static Result deleteUser(String id) {
        return null;
    }

    /*
     * Handles POST:/admin/deleteUser?id=xxx
     */
    @AuthRequired
    public static Result deleteUserSubmit(String id) {
        return null;
        // ApplicationBo application = AsmDao.getApplication(id);
        // if (application == null) {
        // return null;
        // } else {
        // AsmDao.delete(application);
        // String msg = Messages.get("msg.app.delete.done",
        // application.getTitle());
        // flash(FLASH_USER_LIST, msg);
        // return Results.redirect(routes.AdminCp_App.appList());
        // }
    }
}
