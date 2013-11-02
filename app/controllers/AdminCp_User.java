package controllers;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AppCategoryBo;
import bo.AppReleaseBo;
import bo.ApplicationBo;
import bo.AsmDao;
import bo.PlatformBo;
import bo.UserBo;
import bo.UsergroupBo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
        return Results.ok(views.html.admin.user_create.render(null, allUsergroups));
    }

    /*
     * Handles POST:/admin/createApp
     */
    @AuthRequired
    public static Result createUserSubmit() {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        UserBo user = Form.form(UserBo.class).bindFromRequest().get();
        if (true) {
            UsergroupBo[] allUsergroups = AsmDao.getAllUsergroups();
            return Results.ok(views.html.admin.user_create.render(null, allUsergroups));
        }
        // usergroup.setId(idGen.generateIdTinyHex());
        // application.setPosition((int) (System.currentTimeMillis() / 1000));
        // String msg = Messages.get("msg.user.create.done",
        // application.getTitle());
        // flash(FLASH_USER_LIST, msg);
        // AsmDao.create(application);
        return Results.redirect(routes.AdminCp_User.userList());
    }

    /*
     * Handles GET:/admin/editApp?id=xxx
     */
    @AuthRequired
    public static Result editApp(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        AppCategoryBo[] allCategories = AsmDao.getAllAppCategories();
        return Results.ok(views.html.admin.app_edit.render(application, allCategories));
    }

    /*
     * Handles POST:/admin/editApp?id=xxx
     */
    @AuthRequired
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
            flash(FLASH_USER_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());
        }
    }

    /*
     * Handles GET:/admin/deleteApp?id=xxx
     */
    @AuthRequired
    public static Result deleteApp(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        return Results.ok(views.html.admin.app_delete.render(application));
    }

    /*
     * Handles POST:/admin/deleteApp?id=xxx
     */
    @AuthRequired
    public static Result deleteAppSubmit(String id) {
        ApplicationBo application = AsmDao.getApplication(id);
        if (application == null) {
            return null;
        } else {
            AsmDao.delete(application);
            String msg = Messages.get("msg.app.delete.done", application.getTitle());
            flash(FLASH_USER_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());
        }
    }

    /*
     * Handles: GET:/admin/latestRelease?app=xxx&platform=yyy
     */
    @AuthRequired
    public static Result latestRelease(String appId, String platformId) {
        AppReleaseBo latestAppRelease = AsmDao.getLatestAppRelease(appId, platformId);
        ObjectNode result = Json.newObject();
        if (latestAppRelease == null) {
            result.put("status", 404);
        } else {
            result.put("status", 200);
            ObjectMapper mapper = new ObjectMapper();
            result.put("message", mapper.convertValue(latestAppRelease, JsonNode.class));
        }
        return ok(result);
    }

    /*
     * Handles GET:/admin/releaseApp?app=xxx&platform=yyy
     */
    @AuthRequired
    public static Result releaseApp(String appId, String platformId) {
        ApplicationBo app = AsmDao.getApplication(appId);
        if (app == null) {
            return null;
        }
        PlatformBo platform = AsmDao.getPlatform(platformId);
        PlatformBo[] allPlatforms = AsmDao.getAllPlatforms();
        AppReleaseBo latestAppRelease = AsmDao.getLatestAppRelease(appId, platformId);
        return Results.ok(views.html.admin.app_release.render(null, app, platform,
                latestAppRelease, allPlatforms));
    }

    private static Result releaseAppSubmitError(String msg, ApplicationBo app, PlatformBo platform,
            AppReleaseBo appRelease) {
        PlatformBo[] allPlatforms = AsmDao.getAllPlatforms();
        return Results.ok(views.html.admin.app_release.render(msg, app, platform, appRelease,
                allPlatforms));
    }

    /*
     * Handles POST:/admin/releaseApp?app=xxx
     */
    @AuthRequired
    public static Result releaseAppSubmit(String appId) {
        ApplicationBo app = AsmDao.getApplication(appId);
        if (app == null) {
            return null;
        }
        AppReleaseBo submittedAppRelease = Form.form(AppReleaseBo.class).bindFromRequest().get();

        String platformId = submittedAppRelease.getPlatformId();
        platformId = platformId != null ? platformId.trim() : null;
        PlatformBo platform = AsmDao.getPlatform(platformId);
        if (platform == null) {
            return releaseAppSubmitError(Messages.get("error.invalid.platform"), app, platform,
                    submittedAppRelease);
        }

        String version = submittedAppRelease.getVersion();
        version = version != null ? version.trim() : null;
        if (StringUtils.isBlank(version)) {
            return releaseAppSubmitError(Messages.get("error.invalid.version"), app, platform,
                    submittedAppRelease);
        }

        AppReleaseBo exitingAppRelease = AsmDao.getAppRelease(appId, platformId, version);
        if (exitingAppRelease == null) {
            submittedAppRelease.setAppId(appId);
            submittedAppRelease.setTimestamp(new Date());
            submittedAppRelease = AsmDao.create(submittedAppRelease);
            String msg = Messages.get("msg.app_release.done", app.getTitle(), platform.getTitle(),
                    version);
            flash(FLASH_USER_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());

        } else {
            exitingAppRelease.setEnabled(submittedAppRelease.isEnabled());
            exitingAppRelease.setUrlDownload(submittedAppRelease.getUrlDownload());
            exitingAppRelease.setReleaseNotes(submittedAppRelease.getReleaseNotes());
            exitingAppRelease = AsmDao.update(exitingAppRelease);
            String msg = Messages.get("msg.app_release.done", app.getTitle(), platform.getTitle(),
                    version);
            flash(FLASH_USER_LIST, msg);
            return Results.redirect(routes.AdminCp_App.appList());
        }
    }

    /*
     * Handles GET:/admin/moveDownApp?id=xxx
     */
    @AuthRequired
    public static Result moveDownApp(String id) {
        ApplicationBo app = AsmDao.getApplication(id);
        if (app != null) {
            /* Note: applications are order descendingly */
            ApplicationBo[] allApps = AsmDao.getAllApplications();
            if (allApps != null && allApps.length > 1) {
                for (int i = 0; i < allApps.length - 1; i++) {
                    if (StringUtils.equals(allApps[i].getId(), id)) {
                        Integer oldPosition = allApps[i].getPosition();
                        if (oldPosition == null) {
                            oldPosition = (int) (System.currentTimeMillis() / 1000);
                        }
                        allApps[i + 1].setPosition(oldPosition);
                        AsmDao.update(allApps[i + 1]);
                        allApps[i].setPosition(oldPosition.intValue() - 1);
                        AsmDao.update(allApps[i]);
                        break;
                    }
                }
            }
            String msg = Messages.get("msg.app.edit.done", app.getTitle());
            flash(FLASH_USER_LIST, msg);
        }
        return Results.redirect(routes.AdminCp_App.appList());
    }

    /*
     * Handles GET:/admin/moveUpApp?id=xxx
     */
    @AuthRequired
    public static Result moveUpApp(String id) {
        ApplicationBo app = AsmDao.getApplication(id);
        if (app != null) {
            /* Note: applications are order descendingly */
            ApplicationBo[] allApps = AsmDao.getAllApplications();
            if (allApps != null && allApps.length > 1) {
                for (int i = 1; i < allApps.length; i++) {
                    if (StringUtils.equals(allApps[i].getId(), id)) {
                        Integer oldPosition = allApps[i].getPosition();
                        if (oldPosition == null) {
                            oldPosition = (int) (System.currentTimeMillis() / 1000);
                        }
                        allApps[i - 1].setPosition(oldPosition);
                        AsmDao.update(allApps[i - 1]);
                        allApps[i].setPosition(oldPosition.intValue() + 1);
                        AsmDao.update(allApps[i]);
                        break;
                    }
                }

            }
            String msg = Messages.get("msg.app.edit.done", app.getTitle());
            flash(FLASH_USER_LIST, msg);
        }
        return Results.redirect(routes.AdminCp_App.appList());
    }
}
