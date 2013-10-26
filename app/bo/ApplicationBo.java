package bo;

import com.github.ddth.plommon.bo.BaseBo;

public class ApplicationBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_ID = "app_id";
    public final static String COL_CAT_ID = "cat_id";
    public final static String COL_POSITION = "app_position";
    public final static String COL_TITLE = "app_title";
    public final static String COL_ICON = "app_icon";
    public final static String COL_SUMMARY = "app_summary";

    public String getId() {
        return getAttribute(COL_ID, String.class);
    }

    public ApplicationBo setId(String id) {
        return (ApplicationBo) setAttribute(COL_ID, id);
    }

    public String getCategoryId() {
        return getAttribute(COL_CAT_ID, String.class);
    }

    public AppCategoryBo getCategory() {
        return AsmDao.getAppCategory(getCategoryId());
    }

    public ApplicationBo setCategoryId(String categoryId) {
        return (ApplicationBo) setAttribute(COL_CAT_ID, categoryId);
    }

    public Integer getPosition() {
        return getAttribute(COL_POSITION, Integer.class);
    }

    public ApplicationBo setPosition(Integer position) {
        return (ApplicationBo) setAttribute(COL_POSITION, position);
    }

    public String getTitle() {
        return getAttribute(COL_TITLE, String.class);
    }

    public ApplicationBo setTitle(String title) {
        return (ApplicationBo) setAttribute(COL_TITLE, title);
    }

    public String getIcon() {
        return getAttribute(COL_ICON, String.class);
    }

    public ApplicationBo setIcon(String icon) {
        return (ApplicationBo) setAttribute(COL_ICON, icon);
    }

    public String getSummary() {
        return getAttribute(COL_SUMMARY, String.class);
    }

    public ApplicationBo setSummary(String summary) {
        return (ApplicationBo) setAttribute(COL_SUMMARY, summary);
    }
}
