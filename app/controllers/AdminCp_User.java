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

    /*
     * Handles POST:/admin/createApp
     */
    @AuthRequired
    public static Result createUserSubmit() {
        final Pattern PATTERN_LOGIN_NAME = Pattern.compile("^\\w+$");
        final Pattern PATTERN_EMAIL = Pattern.compile("^[^@]+@[^@]+$");

        Form<UserBo> form = Form.form(UserBo.class).bindFromRequest();
        UserBo user = form.get();

        String loginName = user.getLoginName();
        loginName = loginName != null ? loginName.toLowerCase().trim() : null;
        if (!PATTERN_LOGIN_NAME.matcher(loginName).matches()) {
            return createUserSubmitError(Messages.get("error.invalid.loginName", loginName), user);
        }
        UserBo existingUser = AsmDao.getUserByLoginname(loginName);
        if (existingUser != null) {
            return createUserSubmitError(Messages.get("error.alreadyExist.loginName", loginName),
                    user);
        }

        String email = user.getEmail();
        email = email != null ? email.trim() : null;
        if (!PATTERN_EMAIL.matcher(email).matches()) {
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

    /*
     * Handles GET:/admin/editUser?id=xxx
     */
    @AuthRequired
    public static Result editUser(String id) {
        UserBo user = AsmDao.getUser(id);
        if (user == null) {
            String msg = Messages.get("error.404.user");
            String url = routes.AdminCp_User.userList().url();
            return Results.ok(views.html.error.render(msg, url));
        }

        UsergroupBo[] allUsergroups = AsmDao.getAllUsergroups();
        return Results.ok(views.html.admin.user_edit.render(null, user, allUsergroups));
    }

    /*
     * Handles POST:/admin/editUser?id=xxx
     */
    @AuthRequired
    public static Result editUserSubmit(String id) {
        // ApplicationBo application = AsmDao.getApplication(id);
        // if (application == null) {
        // return null;
        // } else {
        // Integer oldPosition = application.getPosition();
        // application = Form.form(ApplicationBo.class).bindFromRequest().get();
        // application.setPosition(oldPosition);
        // application = AsmDao.update(application);
        // String msg = Messages.get("msg.app.edit.done",
        // application.getTitle());
        // flash(FLASH_USER_LIST, msg);
        // return Results.redirect(routes.AdminCp_App.appList());
        // }
        return null;
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
