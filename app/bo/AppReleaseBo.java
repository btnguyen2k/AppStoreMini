package bo;

import java.util.Date;

import com.github.ddth.plommon.bo.BaseBo;

public class AppReleaseBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_APP_ID = "app_id";
    public final static String COL_PLATFORM_ID = "platform_id";
    public final static String COL_IS_ENABLED = "is_enabled";
    public final static String COL_VERSION = "release_version";
    public final static String COL_TIMESTAMP = "timestamp_release";
    public final static String COL_RELEASE_NOTES = "release_notes";
    public final static String COL_URL_DOWNLOAD = "url_download";

    private final static Integer INT_0 = Integer.valueOf(0), INT_1 = Integer.valueOf(1);

    public String getAppId() {
        return getAttribute(COL_APP_ID, String.class);
    }

    public AppReleaseBo setAppId(String appId) {
        return (AppReleaseBo) setAttribute(COL_APP_ID, appId);
    }

    public String getPlatformId() {
        return getAttribute(COL_PLATFORM_ID, String.class);
    }

    public AppReleaseBo setPlatformId(String platformId) {
        return (AppReleaseBo) setAttribute(COL_PLATFORM_ID, platformId);
    }

    public boolean isEnabled() {
        Integer result = getAttribute(COL_IS_ENABLED, Integer.class);
        return result != null ? result.intValue() != 0 : false;
    }

    public AppReleaseBo setEnabled(boolean isEnabled) {
        return (AppReleaseBo) setAttribute(COL_IS_ENABLED, isEnabled ? INT_1 : INT_0);
    }

    public String getVersion() {
        return getAttribute(COL_VERSION, String.class);
    }

    public AppReleaseBo setVersion(String version) {
        return (AppReleaseBo) setAttribute(COL_VERSION, version);
    }

    public Date getTimestamp() {
        return getAttribute(COL_TIMESTAMP, Date.class);
    }

    public AppReleaseBo setTimestamp(Date timestamp) {
        return (AppReleaseBo) setAttribute(COL_TIMESTAMP, timestamp);
    }

    public String getReleaseNotes() {
        return getAttribute(COL_RELEASE_NOTES, String.class);
    }

    public AppReleaseBo setReleaseNotes(String releaseNotes) {
        return (AppReleaseBo) setAttribute(COL_RELEASE_NOTES, releaseNotes);
    }

    public String getUrlDownload() {
        return getAttribute(COL_URL_DOWNLOAD, String.class);
    }

    public AppReleaseBo setUrlDownload(String urlDownload) {
        return (AppReleaseBo) setAttribute(COL_URL_DOWNLOAD, urlDownload);
    }
}
