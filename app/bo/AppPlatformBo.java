package bo;

import java.util.Date;

import com.github.ddth.plommon.bo.BaseBo;

public class AppPlatformBo extends BaseBo {
    /* virtual db columns */
    public final static String COL_APP_ID = "app_id";
    public final static String COL_PLATFORM_ID = "platform_id";
    public final static String COL_IS_ENABLED = "is_enabled";
    public final static String COL_VERSION = "release_version";
    public final static String COL_TIMESTAMP_RELEASE = "timestamp_release";
    public final static String COL_RELEASE_NOTES = "release_notes";
    public final static String COL_URL_DOWNLOAD = "url_download";

    private final static Integer INT_0 = Integer.valueOf(0), INT_1 = Integer.valueOf(0);

    public String getAppId() {
        return getAttribute(COL_APP_ID, String.class);
    }

    public AppPlatformBo setAppId(String appId) {
        return (AppPlatformBo) setAttribute(COL_APP_ID, appId);
    }

    public String getPlatformId() {
        return getAttribute(COL_PLATFORM_ID, String.class);
    }

    public AppPlatformBo setPlatformId(String platformId) {
        return (AppPlatformBo) setAttribute(COL_PLATFORM_ID, platformId);
    }

    public boolean isEnabled() {
        Integer result = getAttribute(COL_IS_ENABLED, Integer.class);
        return result != null ? result.intValue() != 0 : false;
    }

    public AppPlatformBo setEnabled(boolean isEnabled) {
        return (AppPlatformBo) setAttribute(COL_IS_ENABLED, isEnabled ? INT_1 : INT_0);
    }

    public String getVersion() {
        return getAttribute(COL_VERSION, String.class);
    }

    public AppPlatformBo setVersion(String version) {
        return (AppPlatformBo) setAttribute(COL_VERSION, version);
    }

    public Date getTimestampRelease() {
        return getAttribute(COL_TIMESTAMP_RELEASE, Date.class);
    }

    public AppPlatformBo setTimestampRelease(Date timestamp) {
        return (AppPlatformBo) setAttribute(COL_TIMESTAMP_RELEASE, timestamp);
    }

    public String getReleaseNotes() {
        return getAttribute(COL_RELEASE_NOTES, String.class);
    }

    public AppPlatformBo setReleaseNotes(String releaseNotes) {
        return (AppPlatformBo) setAttribute(COL_RELEASE_NOTES, releaseNotes);
    }

    public String geturlDownload() {
        return getAttribute(COL_URL_DOWNLOAD, String.class);
    }

    public AppPlatformBo setUrlDownload(String urlDownload) {
        return (AppPlatformBo) setAttribute(COL_URL_DOWNLOAD, urlDownload);
    }
}
