package bo;

import com.github.ddth.plommon.bo.BaseBo;

public class UsergroupBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_ID = "group_id";
    public final static String COL_TITLE = "group_title";

    public String getId() {
        return getAttribute(COL_ID, String.class);
    }

    public UsergroupBo setId(String id) {
        return (UsergroupBo) setAttribute(COL_ID, id);
    }

    public String getTitle() {
        return getAttribute(COL_TITLE, String.class);
    }

    public UsergroupBo setTitle(String title) {
        return (UsergroupBo) setAttribute(COL_TITLE, title);
    }
}
