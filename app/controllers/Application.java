package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

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

    // public static Result index() {
    // return ok(index.render("Your new application is ready."));
    // }

}
