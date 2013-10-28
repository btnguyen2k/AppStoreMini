package controllers;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AppCategoryBo;
import bo.AppReleaseBo;
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
     * Handles GET:/admin/createApp
     */
    public static Result createApp() {
        AppCategoryBo[] allCategories = AsmDao.getAllAppCategories();
        return Results.ok(views.html.admin.app_create.render(null, allCategories));
    }

    /*
     * Handles POST:/admin/createApp
     */
    public static Result createAppSubmit() {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        ApplicationBo application = Form.form(ApplicationBo.class).bindFromRequest().get();
        application.setId(idGen.generateIdTinyHex());
        application.setPosition((int) (System.currentTimeMillis() / 1000));
        String msg = Messages.get("msg.app.create.done", application.getTitle());
        flash(FLASH_APP_LIST, msg);
        AsmDao.create(application);
        return Results.redirect(routes.AdminCp_App.appList());
    }

    /*
     * Handles GET:/admin/editApp?id=xxx
     */
    public static Result editApp(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        AppCategoryBo[] allCategories = AsmDao.getAllAppCategories();
        return Results.ok(views.html.admin.app_edit.render(application, allCategories));
    }

    /*
     * Handles POST:/admin/editApp?id=xxx
     */
    public static Result editAppSubmit(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        if (application == null) {
            return null;
        } else {
            Integer oldPosition = application.getPosition();
            application = Form.form(ApplicationBo.class).bindFromRequest().get();
            application.setPosition(oldPosition);
            application = AsmDao.update(application);
            String msg = Messages.get("msg.app.edit.done", application.getTitle());
            flash(FLASH_APP_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());
        }
    }

    /*
     * Handles GET:/admin/deleteApp?id=xxx
     */
    public static Result deleteApp(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        return Results.ok(views.html.admin.app_delete.render(application));
    }

    /*
     * Handles POST:/admin/deleteApp?id=xxx
     */
    public static Result deleteAppSubmit(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        if (application == null) {
            return null;
        } else {
            AsmDao.delete(application);
            String msg = Messages.get("msg.app.delete.done", application.getTitle());
            flash(FLASH_APP_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());
        }
    }

    /*
     * Handles GET:/admin/releaseApp
     */
    public static Result releaseApp(String appId, String platformId) {
        ApplicationBo app = AsmDao.getApplication(appId);
        PlatformBo platform = AsmDao.getPlatform(platformId);
        PlatformBo[] allPlatforms = AsmDao.getAllPlatforms();
        AppReleaseBo latestAppRelease = AsmDao.getLatestAppRelease(appId, platformId);
        return Results.ok(views.html.admin.app_release.render(app, platform, latestAppRelease,
                allPlatforms));
    }

    /*
     * Handles POST:/admin/releaseApp
     */
    public static Result releaseAppSubmit(String appId, String platformId) {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        ApplicationBo application = Form.form(ApplicationBo.class).bindFromRequest().get();
        application.setId(idGen.generateIdTinyHex());
        application.setPosition((int) (System.currentTimeMillis() / 1000));
        String msg = Messages.get("msg.app.create.done", application.getTitle());
        flash(FLASH_APP_LIST, msg);
        AsmDao.create(application);
        return Results.redirect(routes.AdminCp_App.appList());
    }
}
