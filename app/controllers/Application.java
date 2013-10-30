package controllers;

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

    public static Result index() {
        ApplicationBo[] allApps = AsmDao.getAllApplications();
        return ok(views.html.index.render(allApps));
    }

}
