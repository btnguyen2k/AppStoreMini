package bo;

import com.github.ddth.plommon.bo.BaseBo;

public class AppCategoryBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_ID = "cat_id";
    public final static String COL_PARENT_ID = "parent_id";
    public final static String COL_POSITION = "cat_position";
    public final static String COL_TITLE = "cat_title";

    public String getId() {
        return getAttribute(COL_ID, String.class);
    }

    public AppCategoryBo setId(String id) {
        return (AppCategoryBo) setAttribute(COL_ID, id);
    }

    public String getParentId() {
        return getAttribute(COL_PARENT_ID, String.class);
    }

    public AppCategoryBo setParentId(String parentId) {
        return (AppCategoryBo) setAttribute(COL_PARENT_ID, parentId);
    }

    public Integer getPosition() {
        return getAttribute(COL_POSITION, Integer.class);
    }

    public AppCategoryBo setPosition(Integer position) {
        return (AppCategoryBo) setAttribute(COL_POSITION, position);
    }

    public String getTitle() {
        return getAttribute(COL_TITLE, String.class);
    }

    public AppCategoryBo setTitle(String title) {
        return (AppCategoryBo) setAttribute(COL_TITLE, title);
    }
}
