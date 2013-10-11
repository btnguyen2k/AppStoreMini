package controllers;

import models.FormLogin;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AsmDao;
import bo.PlatformBo;
import bo.UserBo;

import com.github.ddth.plommon.utils.IdGenerator;

public class AdminCp extends Controller {

    private final static String FLASH_APP_LIST = "app_list";
    private final static String FLASH_APP_PLATFORM_LIST = "app_platform_list";

    /*
     * Handles GET:/admin/index
     */
    public static Result index() {
        return Results.ok(views.html.admin.index.render());
    }

    /*
     * Handles GET:/admin/login
     */
    public static Result login() {
        String msg = flash("login");
        return Results.ok(views.html.admin.login.render(msg));
    }

    /*
     * Handles POST:/admin/login
     */
    public static Result loginSubmit() {
        FormLogin frmLogin = Form.form(FormLogin.class).bindFromRequest().get();
        UserBo user = AsmDao.getUserByLoginname(frmLogin != null ? frmLogin.loginname : null);
        boolean authed = user != null ? user.authenticate(frmLogin.password) : false;
        if (!authed) {
            String msg = Messages.get("error.login");
            flash("login", msg);
            return Results.redirect(routes.AdminCp.login());
        } else {
            return Results.redirect(routes.AdminCp.index());
        }
    }

    /*
     * Handles GET:/admin/appList
     */
    public static Result appList() {
        String msg = flash(FLASH_APP_LIST);
        return Results.ok(views.html.admin.app_list.render(msg));
    }

    /*
     * Handles GET:/admin/appPlatforms
     */
    public static Result appPlatformList() {
        String msg = flash(FLASH_APP_PLATFORM_LIST);
        PlatformBo[] allPlatforms = AsmDao.getAllPlatforms();
        return Results.ok(views.html.admin.app_platforms.render(msg, allPlatforms));
    }

    /*
     * Handles GET:/admin/createAppPlatform
     */
    public static Result createAppPlatform() {
        return Results.ok(views.html.admin.app_platform_create.render(null));
    }

    /*
     * Handles POST:/admin/createAppPlatform
     */
    public static Result createAppPlatformSubmit() {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        PlatformBo platformBo = Form.form(PlatformBo.class).bindFromRequest().get();
        platformBo.setId(idGen.generateIdTinyHex());
        String msg = Messages.get("msg.app_platform.create.done", platformBo.getTitle());
        flash(FLASH_APP_PLATFORM_LIST, msg);
        AsmDao.create(platformBo);
        return Results.redirect(routes.AdminCp.appPlatformList());
    }

    /*
     * Handles GET:/admin/editAppPlatform
     */
    public static Result editAppPlatform(String id) {
        PlatformBo platform = AsmDao.getPlatform(id);
        return Results.ok(views.html.admin.app_platform_edit.render(platform));
    }

    /*
     * Handles POST:/admin/editAppPlatform
     */
    public static Result editAppPlatformSubmit(String id) {
        PlatformBo platformBo = AsmDao.getPlatform(id);
        if (platformBo == null) {
            return null;
        } else {
            platformBo = Form.form(PlatformBo.class).bindFromRequest().get();
            platformBo = AsmDao.update(platformBo);
            String msg = Messages.get("msg.app_platform.edit.done", platformBo.getTitle());
            flash(FLASH_APP_PLATFORM_LIST, msg);
            return Results.redirect(routes.AdminCp.appPlatformList());
        }
    }

    /*
     * Handles GET:/admin/deleteAppPlatform?id=xxx
     */
    public static Result deleteAppPlatform(String id) {
        return null;
    }

    /*
     * Handles POST:/admin/deleteAppPlatform?id=xxx
     */
    public static Result deleteAppPlatformSubmit(String id) {
        return null;
    }
}
