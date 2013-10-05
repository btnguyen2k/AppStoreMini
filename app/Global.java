import play.Application;
import play.GlobalSettings;

import com.github.ddth.plommon.bo.BaseDao;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        BaseDao.init();

        super.onStart(app);
    }

    @Override
    public void onStop(Application app) {
        super.onStop(app);
    }

}
