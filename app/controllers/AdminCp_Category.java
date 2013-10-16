package controllers;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AppCategoryBo;
import bo.AsmDao;

import com.github.ddth.plommon.utils.IdGenerator;

public class AdminCp_Category extends Controller {

    private final static String FLASH_APP_CATEGORY_LIST = "app_category_list";

    /*
     * Handles GET:/admin/appCategories
     */
    public static Result appCategoryList() {
        String msg = flash(FLASH_APP_CATEGORY_LIST);
        AppCategoryBo[] allCategories = AsmDao.getAllAppCategories();
        return Results.ok(views.html.admin.app_categories.render(msg, allCategories));
    }

    /*
     * Handles GET:/admin/createAppCategory
     */
    public static Result createAppCategory() {
        return Results.ok(views.html.admin.app_category_create.render(null));
    }

    /*
     * Handles POST:/admin/createAppCategory
     */
    public static Result createAppCategorySubmit() {
        IdGenerator idGen = IdGenerator.getInstance(IdGenerator.getMacAddr());
        AppCategoryBo categoryBo = Form.form(AppCategoryBo.class).bindFromRequest().get();
        categoryBo.setId(idGen.generateIdTinyHex());
        categoryBo.setPosition((int) (System.currentTimeMillis() / 1000));
        String msg = Messages.get("msg.app_category.create.done", categoryBo.getTitle());
        flash(FLASH_APP_CATEGORY_LIST, msg);
        AsmDao.create(categoryBo);
        return Results.redirect(routes.AdminCp_Category.appCategoryList());
    }

    // /*
    // * Handles GET:/admin/editAppPlatform
    // */
    // public static Result editAppPlatform(String id) {
    // PlatformBo platform = AsmDao.getPlatform(id);
    // return Results.ok(views.html.admin.app_platform_edit.render(platform));
    // }
    //
    // /*
    // * Handles POST:/admin/editAppPlatform
    // */
    // public static Result editAppPlatformSubmit(String id) {
    // PlatformBo platformBo = AsmDao.getPlatform(id);
    // if (platformBo == null) {
    // return null;
    // } else {
    // platformBo = Form.form(PlatformBo.class).bindFromRequest().get();
    // platformBo = AsmDao.update(platformBo);
    // String msg = Messages.get("msg.app_platform.edit.done",
    // platformBo.getTitle());
    // flash(FLASH_APP_CATEGORY_LIST, msg);
    // return Results.redirect(routes.AdminCp_Platform.appPlatformList());
    // }
    // }

    /*
     * Handles GET:/admin/deleteAppCategory?id=xxx
     */
    public static Result deleteAppPlatform(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        return Results.ok(views.html.admin.app_category_delete.render(category));
    }

    /*
     * Handles POST:/admin/deleteAppCategory?id=xxx
     */
    public static Result deleteAppCategorySubmit(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        if (category == null) {
            return null;
        } else {
            // AsmDao.delete(category);
            String msg = Messages.get("msg.app_category.delete.done", category.getTitle());
            flash(FLASH_APP_CATEGORY_LIST, msg);
            return Results.redirect(routes.AdminCp_Category.appCategoryList());
        }
    }
}
