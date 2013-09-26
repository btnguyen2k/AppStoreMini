package bo;

import com.github.ddth.plommon.bo.BaseBo;

public class PlatformBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_ID = "platform_id";
    public final static String COL_IS_ENABLED = "is_enabled";
    public final static String COL_TITLE = "platform_title";
    public final static String COL_ICON_16 = "icon_16";
    public final static String COL_ICON_24 = "icon_24";
    public final static String COL_ICON_32 = "icon_32";
    public final static String COL_ICON_48 = "icon_48";
    public final static String COL_ICON_64 = "icon_64";

    private final static Integer INT_0 = Integer.valueOf(0), INT_1 = Integer.valueOf(0);

    public String getId() {
        return getAttribute(COL_ID, String.class);
    }

    public PlatformBo setId(String id) {
        return (PlatformBo) setAttribute(COL_ID, id);
    }

    public boolean isEnabled() {
        Integer result = getAttribute(COL_IS_ENABLED, Integer.class);
        return result != null ? result.intValue() != 0 : false;
    }

    public PlatformBo setEnabled(boolean isEnabled) {
        return (PlatformBo) setAttribute(COL_IS_ENABLED, isEnabled ? INT_1 : INT_0);
    }

    public String getTitle() {
        return getAttribute(COL_TITLE, String.class);
    }

    public PlatformBo setTitle(String title) {
        return (PlatformBo) setAttribute(COL_TITLE, title);
    }

    public String getIcon16() {
        return getAttribute(COL_ICON_16, String.class);
    }

    public PlatformBo setIcon16(String icon16) {
        return (PlatformBo) setAttribute(COL_ICON_16, icon16);
    }

    public String getIcon24() {
        return getAttribute(COL_ICON_24, String.class);
    }

    public PlatformBo setIcon24(String icon24) {
        return (PlatformBo) setAttribute(COL_ICON_24, icon24);
    }

    public String getIcon32() {
        return getAttribute(COL_ICON_32, String.class);
    }

    public PlatformBo setIcon32(String icon32) {
        return (PlatformBo) setAttribute(COL_ICON_32, icon32);
    }

    public String getIcon48() {
        return getAttribute(COL_ICON_48, String.class);
    }

    public PlatformBo setIcon48(String icon48) {
        return (PlatformBo) setAttribute(COL_ICON_48, icon48);
    }

    public String getIcon64() {
        return getAttribute(COL_ICON_64, String.class);
    }

    public PlatformBo setIcon64(String icon64) {
        return (PlatformBo) setAttribute(COL_ICON_64, icon64);
    }
}
