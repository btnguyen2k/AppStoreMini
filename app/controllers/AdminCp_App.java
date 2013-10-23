package controllers;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.ApplicationBo;
import bo.AsmDao;
import bo.PlatformBo;

import com.github.ddth.plommon.utils.IdGenerator;

public class AdminCp_App extends Controller {

    private final static String FLASH_APP_LIST = "app_list";

    /*
     * Handles GET:/admin/apps
     */
    public static Result appList() {
        String msg = flash(FLASH_APP_LIST);
        ApplicationBo[] allApps = AsmDao.getAllApplications();
        return Results.ok(views.html.admin.apps.render(msg, allApps));
    }

    /*
     * Handles GET:/admin/createPlatform
     */
    public static Result createPlatform() {
        return Results.ok(views.html.admin.platform_create.render(null));
    }

    /*
     * Handles POST:/admin/createPlatform
     */
    public static Result createPlatformSubmit() {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        PlatformBo platformBo = Form.form(PlatformBo.class).bindFromRequest().get();
        platformBo.setId(idGen.generateIdTinyHex());
        String msg = Messages.get("msg.platform.create.done", platformBo.getTitle());
        flash(FLASH_APP_LIST, msg);
        AsmDao.create(platformBo);
        return Results.redirect(routes.AdminCp_Platform.platformList());
    }

    /*
     * Handles GET:/admin/editPlatform
     */
    public static Result editPlatform(String id) {
        PlatformBo platform = AsmDao.getPlatform(id);
        return Results.ok(views.html.admin.platform_edit.render(platform));
    }

    /*
     * Handles POST:/admin/editPlatform
     */
    public static Result editPlatformSubmit(String id) {
        PlatformBo platformBo = AsmDao.getPlatform(id);
        if (platformBo == null) {
            return null;
        } else {
            platformBo = Form.form(PlatformBo.class).bindFromRequest().get();
            platformBo = AsmDao.update(platformBo);
            String msg = Messages.get("msg.platform.edit.done", platformBo.getTitle());
            flash(FLASH_APP_LIST, msg);
            return Results.redirect(routes.AdminCp_Platform.platformList());
        }
    }

    /*
     * Handles GET:/admin/deletePlatform?id=xxx
     */
    public static Result deletePlatform(String id) {
        PlatformBo platform = AsmDao.getPlatform(id);
        return Results.ok(views.html.admin.platform_delete.render(platform));
    }

    /*
     * Handles POST:/admin/deletePlatform?id=xxx
     */
    public static Result deletePlatformSubmit(String id) {
        PlatformBo platformBo = AsmDao.getPlatform(id);
        if (platformBo == null) {
            return null;
        } else {
            AsmDao.delete(platformBo);
            String msg = Messages.get("msg.platform.delete.done", platformBo.getTitle());
            flash(FLASH_APP_LIST, msg);
            return Results.redirect(routes.AdminCp_Platform.platformList());
        }
    }
}
