package controllers;

import models.FormLogin;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.AsmDao;
import bo.UserBo;

public class AdminCp extends Controller {
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
}
