package controllers;

import org.apache.commons.lang3.StringUtils;

import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AppCategoryBo;
import bo.AsmDao;

import com.github.ddth.plommon.utils.IdGenerator;

public class AdminCp_Category extends Controller {

    private final static String FLASH_CATEGORY_LIST = "category_list";

    /*
     * Handles GET:/admin/appCategories
     */
    public static Result appCategoryList() {
        String msg = flash(FLASH_CATEGORY_LIST);
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
        flash(FLASH_CATEGORY_LIST, msg);
        AsmDao.create(categoryBo);
        return Results.redirect(routes.AdminCp_Category.appCategoryList());
    }

    /*
     * Handles GET:/admin/editAppCategory
     */
    public static Result editAppCategory(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        return Results.ok(views.html.admin.app_category_edit.render(category));
    }

    /*
     * Handles POST:/admin/editAppCategory
     */
    public static Result editAppCategorySubmit(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        if (category == null) {
            return null;
        } else {
            AppCategoryBo temp = Form.form(AppCategoryBo.class).bindFromRequest().get();
            category.setTitle(temp.getTitle());
            category = AsmDao.update(category);
            String msg = Messages.get("msg.app_category.edit.done", category.getTitle());
            flash(FLASH_CATEGORY_LIST, msg);
            return Results.redirect(routes.AdminCp_Category.appCategoryList());
        }
    }

    /*
     * Handles GET:/admin/moveDownAppCategory?id=xxx
     */
    public static Result moveDownAppCategory(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        if (category != null) {
            AppCategoryBo[] allCats = AsmDao.getAllAppCategories();
            if (allCats != null && allCats.length > 1) {
                for (int i = 0; i < allCats.length - 1; i++) {
                    if (StringUtils.equals(allCats[i].getId(), id)) {
                        Integer oldPosition = allCats[i].getPosition();
                        if (oldPosition == null) {
                            oldPosition = (int) (System.currentTimeMillis() / 1000);
                        }
                        allCats[i + 1].setPosition(oldPosition);
                        AsmDao.update(allCats[i + 1]);
                        allCats[i].setPosition(oldPosition.intValue() + 1);
                        AsmDao.update(allCats[i]);
                        break;
                    }
                }
            }
            String msg = Messages.get("msg.app_category.edit.done", category.getTitle());
            flash(FLASH_CATEGORY_LIST, msg);
        }
        return Results.redirect(routes.AdminCp_Category.appCategoryList());
    }

    /*
     * Handles GET:/admin/moveUpAppCategory?id=xxx
     */
    public static Result moveUpAppCategory(String id) {
        AppCategoryBo category = AsmDao.getAppCategory(id);
        if (category != null) {
            AppCategoryBo[] allCats = AsmDao.getAllAppCategories();
            if (allCats != null && allCats.length > 1) {
                for (int i = 1; i < allCats.length; i++) {
                    if (StringUtils.equals(allCats[i].getId(), id)) {
                        Integer oldPosition = allCats[i].getPosition();
                        if (oldPosition == null) {
                            oldPosition = (int) (System.currentTimeMillis() / 1000);
                        }
                        allCats[i - 1].setPosition(oldPosition);
                        AsmDao.update(allCats[i - 1]);
                        allCats[i].setPosition(oldPosition.intValue() - 1);
                        AsmDao.update(allCats[i]);
                        break;
                    }
                }

            }
            String msg = Messages.get("msg.app_category.edit.done", category.getTitle());
            flash(FLASH_CATEGORY_LIST, msg);
        }
        return Results.redirect(routes.AdminCp_Category.appCategoryList());
    }

    /*
     * Handles GET:/admin/deleteAppCategory?id=xxx
     */
    public static Result deleteAppCategory(String id) {
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
            AsmDao.delete(category);
            String msg = Messages.get("msg.app_category.delete.done", category.getTitle());
            flash(FLASH_CATEGORY_LIST, msg);
            return Results.redirect(routes.AdminCp_Category.appCategoryList());
        }
    }
}
