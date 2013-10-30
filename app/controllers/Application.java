package controllers;

import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import bo.ApplicationBo;
import bo.AsmDao;

public class Application extends Controller {

    /**
     * Redirect action.
     * 
     * @param url
     * @return
     */
    public static Result appRedirect(String url) {
        return Results.redirect(url);
    }

    /*
     * Handles: GET:/
     */
    public static Result index() {
        ApplicationBo[] allApps = AsmDao.getAllApplications();
        return ok(views.html.index.render(allApps));
    }

    /*
     * Handles: GET:/app/id
     */
    public static Result viewApp(String appId) {
        ApplicationBo app = AsmDao.getApplication(appId);
        if (app == null) {
            return ok(views.html.error.render(Messages.get("error.404.app"), routes.Application
                    .index().url()));
        }
        return ok(views.html.app.render(app));
    }
}
