package compositions;

import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import utils.Utils;
import bo.UserBo;
import controllers.routes;

public class AuthRequiredAction extends Action.Simple {
    public Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        UserBo user = Utils.getCurrentUser();
        if (user == null) {
            Promise<SimpleResult> result = Promise.promise(new Function0<SimpleResult>() {
                public SimpleResult apply() {
                    return redirect(routes.AdminCp.login());
                }
            });
            return result;
        }
        return delegate.call(ctx);
    }
}
