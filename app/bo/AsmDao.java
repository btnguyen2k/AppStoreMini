package bo;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.github.ddth.plommon.bo.BaseMysqlDao;

public class AsmDao extends BaseMysqlDao {

    public final static String TABLE_PLATFORM = "asm_platform";
    public final static String TABLE_USERGROUP = "asm_usergroup";
    public final static String TABLE_USER = "asm_user";
    public final static String TABLE_APP_CATEGORY = "asm_appcategory";
    public final static String TABLE_APPLICATION = "asm_app";
    public final static String TABLE_APP_PLATFORM = "asm_appplatform";

    /*----------------------------------------------------------------------*/
    private static String cacheKeyPlatform(String id) {
        return "PLATFORM_" + id;
    }

    private static String cacheKeyUsergroup(String id) {
        return "USERGROUP_" + id;
    }

    private static String cacheKeyUser(String id) {
        return "USER_" + id;
    }

    private static String cacheKeyAppCategory(String id) {
        return "APPCAT_" + id;
    }

    private static String cacheKeyApplication(String id) {
        return "APP_" + id;
    }

    private static String cacheKeyAppPlatform(String appId, String platformId) {
        return "APPPLATFORM_" + appId + "_" + platformId;
    }

    private static String cacheKey(PlatformBo platform) {
        return cacheKeyPlatform(platform.getId());
    }

    private static String cacheKey(UsergroupBo usergroup) {
        return cacheKeyUsergroup(usergroup.getId());
    }

    private static String cacheKey(UserBo user) {
        return cacheKeyUser(user.getId());
    }

    private static String cacheKey(AppCategoryBo appCat) {
        return cacheKeyAppCategory(appCat.getId());
    }

    private static String cacheKey(ApplicationBo app) {
        return cacheKeyApplication(app.getId());
    }

    private static String cacheKey(AppPlatformBo appPlatform) {
        return cacheKeyAppPlatform(appPlatform.getAppId(), appPlatform.getPlatformId());
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new platform info entry.
     * 
     * @param platform
     * @return
     */
    public static PlatformBo create(PlatformBo platform) {
        final String[] COLUMNS = new String[] { "pid", "pis_enabled", "ptitle", "picon16",
                "picon24", "picon32", "picon48", "picon64" };
        final Object[] VALUES = new Object[] { platform.getId(), platform.isEnabled() ? 1 : 0,
                platform.getTitle(), platform.getIcon16(), platform.getIcon24(),
                platform.getIcon32(), platform.getIcon48(), platform.getIcon64() };
        insertIgnore(TABLE_PLATFORM, COLUMNS, VALUES);
        removeFromCache(cacheKey(platform));
        return (PlatformBo) platform.markClean();
    }

    /**
     * Deletes an existing platform.
     * 
     * @param platform
     */
    public static void delete(PlatformBo platform) {
        final String[] COLUMNS = new String[] { "pid" };
        final Object[] VALUES = new Object[] { platform.getId() };
        delete(TABLE_PLATFORM, COLUMNS, VALUES);
        removeFromCache(cacheKey(platform));
    }

    /**
     * Gets a platform by id.
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static PlatformBo getPlatform(String id) {
        final String CACHE_KEY = cacheKeyPlatform(id);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT pid AS {1}, pis_enabled AS {2}, ptitle AS {3}, picon16 AS {4}, picon24 AS {5}, picon32 AS {6}, picon48 AS {7}, picon64 AS {8} FROM {0} WHERE pid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_PLATFORM,
                    PlatformBo.COL_ID, PlatformBo.COL_IS_ENABLED, PlatformBo.COL_TITLE,
                    PlatformBo.COL_ICON_16, PlatformBo.COL_ICON_24, PlatformBo.COL_ICON_32,
                    PlatformBo.COL_ICON_48, PlatformBo.COL_ICON_64);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (PlatformBo) new PlatformBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing platform.
     * 
     * @param platform
     * @return
     */
    public static PlatformBo update(PlatformBo platform) {
        if (platform.isDirty()) {
            final String CACHE_KEY = cacheKey(platform);
            final String[] COLUMNS = new String[] { "pis_enabled", "ptitle", "picon16", "picon24",
                    "picon32", "picon48", "picon64" };
            final Object[] VALUES = new Object[] { platform.isEnabled() ? 1 : 0,
                    platform.getTitle(), platform.getIcon16(), platform.getIcon24(),
                    platform.getIcon32(), platform.getIcon48(), platform.getIcon64() };
            final String[] WHERE_COLUMNS = new String[] { "pid" };
            final Object[] WHERE_VALUES = new Object[] { platform.getId() };
            update(TABLE_PLATFORM, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = platform.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (PlatformBo) platform.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new user group.
     * 
     * @param usergroup
     * @return
     */
    public static UsergroupBo create(UsergroupBo usergroup) {
        final String[] COLUMNS = new String[] { "gid", "gtitle" };
        final Object[] VALUES = new Object[] { usergroup.getId(), usergroup.getTitle() };
        insertIgnore(TABLE_USERGROUP, COLUMNS, VALUES);
        removeFromCache(cacheKey(usergroup));
        return (UsergroupBo) usergroup.markClean();
    }

    /**
     * Deletes an existing usergroup.
     * 
     * @param usergroup
     */
    public static void delete(UsergroupBo usergroup) {
        final String[] COLUMNS = new String[] { "gid" };
        final Object[] VALUES = new Object[] { usergroup.getId() };
        delete(TABLE_USERGROUP, COLUMNS, VALUES);
        removeFromCache(cacheKey(usergroup));
    }

    /**
     * Gets a usergroup by id.
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UsergroupBo getUsergroup(String id) {
        final String CACHE_KEY = cacheKeyUsergroup(id);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT gid AS {1}, gtitle AS {2} FROM {0} WHERE gid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_USERGROUP,
                    UsergroupBo.COL_ID, UsergroupBo.COL_TITLE);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (UsergroupBo) new UsergroupBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing usergroup.
     * 
     * @param usergroup
     * @return
     */
    public static UsergroupBo update(UsergroupBo usergroup) {
        if (usergroup.isDirty()) {
            final String CACHE_KEY = cacheKey(usergroup);
            final String[] COLUMNS = new String[] { "gtitle" };
            final Object[] VALUES = new Object[] { usergroup.getTitle() };
            final String[] WHERE_COLUMNS = new String[] { "gid" };
            final Object[] WHERE_VALUES = new Object[] { usergroup.getId() };
            update(TABLE_USERGROUP, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = usergroup.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (UsergroupBo) usergroup.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new user account.
     * 
     * @param user
     * @return
     */
    public static UserBo create(UserBo user) {
        final String[] COLUMNS = new String[] { "uid", "ulogin_name", "upassword", "uemail",
                "ugroup_id", "utimestamp_create" };
        final Object[] VALUES = new Object[] { user.getId(), user.getLoginName(),
                user.getPassword(), user.getEmail(), user.getGroupId(), user.getTimestampCreate() };
        insertIgnore(TABLE_USER, COLUMNS, VALUES);
        removeFromCache(cacheKey(user));
        return (UserBo) user.markClean();
    }

    /**
     * Deletes an existing user account.
     * 
     * @param user
     */
    public static void delete(UserBo user) {
        final String[] COLUMNS = new String[] { "uid" };
        final Object[] VALUES = new Object[] { user.getId() };
        delete(TABLE_USER, COLUMNS, VALUES);
        removeFromCache(cacheKey(user));
    }

    /**
     * Gets a user account by id.
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UserBo getUser(String id) {
        final String CACHE_KEY = cacheKeyUser(id);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT uid AS {1}, ulogin_name AS {2}, upassword AS {3}, uemail AS {4}, ugroup_id AS {5}, utimestamp_create AS {6} FROM {0} WHERE uid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_USER, UserBo.COL_ID,
                    UserBo.COL_LOGIN_NAME, UserBo.COL_PASSWORD, UserBo.COL_EMAIL,
                    UserBo.COL_GROUP_ID, UserBo.COL_TIMESTAMP_CREATE);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (UserBo) new UserBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing user account.
     * 
     * @param user
     * @return
     */
    public static UserBo update(UserBo user) {
        if (user.isDirty()) {
            final String CACHE_KEY = cacheKey(user);
            final String[] COLUMNS = new String[] { "upassword", "uemail", "ugroup_id" };
            final Object[] VALUES = new Object[] { user.getPassword(), user.getEmail(),
                    user.getGroupId() };
            final String[] WHERE_COLUMNS = new String[] { "uid" };
            final Object[] WHERE_VALUES = new Object[] { user.getId() };
            update(TABLE_USER, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = user.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (UserBo) user.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new app category.
     * 
     * @param appCat
     * @return
     */
    public static AppCategoryBo create(AppCategoryBo appCat) {
        final String[] COLUMNS = new String[] { "cid", "cparent_id", "cposition", "ctitle" };
        final Object[] VALUES = new Object[] { appCat.getId(), appCat.getParentId(),
                appCat.getPosition(), appCat.getTitle() };
        insertIgnore(TABLE_APP_CATEGORY, COLUMNS, VALUES);
        removeFromCache(cacheKey(appCat));
        return (AppCategoryBo) appCat.markClean();
    }

    /**
     * Deletes an existing app category.
     * 
     * @param appCat
     */
    public static void delete(AppCategoryBo appCat) {
        final String[] COLUMNS = new String[] { "cid" };
        final Object[] VALUES = new Object[] { appCat.getId() };
        delete(TABLE_APP_CATEGORY, COLUMNS, VALUES);
        removeFromCache(cacheKey(appCat));
    }

    /**
     * Gets an app category by id.
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AppCategoryBo getAppCategory(String id) {
        final String CACHE_KEY = cacheKeyAppCategory(id);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT cid AS {1}, cparent_id AS {2}, cposition AS {3}, ctitle AS {4} FROM {0} WHERE cid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_CATEGORY,
                    AppCategoryBo.COL_ID, AppCategoryBo.COL_PARENT_ID, AppCategoryBo.COL_POSITION,
                    AppCategoryBo.COL_TITLE);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (AppCategoryBo) new AppCategoryBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing app category.
     * 
     * @param appCat
     * @return
     */
    public static AppCategoryBo update(AppCategoryBo appCat) {
        if (appCat.isDirty()) {
            final String CACHE_KEY = cacheKey(appCat);
            final String[] COLUMNS = new String[] { "cparent_id", "cposition", "ctitle" };
            final Object[] VALUES = new Object[] { appCat.getParentId(), appCat.getPosition(),
                    appCat.getTitle() };
            final String[] WHERE_COLUMNS = new String[] { "cid" };
            final Object[] WHERE_VALUES = new Object[] { appCat.getId() };
            update(TABLE_APP_CATEGORY, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = appCat.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (AppCategoryBo) appCat.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new application.
     * 
     * @param app
     * @return
     */
    public static ApplicationBo create(ApplicationBo app) {
        final String[] COLUMNS = new String[] { "aid", "acategory_id", "aposition", "atitle",
                "asummary" };
        final Object[] VALUES = new Object[] { app.getId(), app.getCategoryId(), app.getPosition(),
                app.getTitle(), app.getSummary() };
        insertIgnore(TABLE_APPLICATION, COLUMNS, VALUES);
        removeFromCache(cacheKey(app));
        return (ApplicationBo) app.markClean();
    }

    /**
     * Deletes an existing application.
     * 
     * @param app
     */
    public static void delete(ApplicationBo app) {
        final String[] COLUMNS = new String[] { "aid" };
        final Object[] VALUES = new Object[] { app.getId() };
        delete(TABLE_APPLICATION, COLUMNS, VALUES);
        removeFromCache(cacheKey(app));
    }

    /**
     * Gets an application by id.
     * 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ApplicationBo getApplication(String id) {
        final String CACHE_KEY = cacheKeyApplication(id);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT aid AS {1}, acategory_id AS {2}, aposition AS {3}, atitle AS {4}, asummary AS {5} FROM {0} WHERE aid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APPLICATION,
                    ApplicationBo.COL_ID, ApplicationBo.COL_CAT_ID, ApplicationBo.COL_POSITION,
                    ApplicationBo.COL_TITLE, ApplicationBo.COL_SUMMARY);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (ApplicationBo) new ApplicationBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing application.
     * 
     * @param app
     * @return
     */
    public static ApplicationBo update(ApplicationBo app) {
        if (app.isDirty()) {
            final String CACHE_KEY = cacheKey(app);
            final String[] COLUMNS = new String[] { "acategory_id", "aposition", "atitle",
                    "asummary" };
            final Object[] VALUES = new Object[] { app.getCategoryId(), app.getPosition(),
                    app.getTitle(), app.getSummary() };
            final String[] WHERE_COLUMNS = new String[] { "aid" };
            final Object[] WHERE_VALUES = new Object[] { app.getId() };
            update(TABLE_APPLICATION, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = app.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (ApplicationBo) app.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new app platform.
     * 
     * @param appPlatform
     * @return
     */
    public static AppPlatformBo create(AppPlatformBo appPlatform) {
        final String[] COLUMNS = new String[] { "app_id", "platform_id", "apis_enabled",
                "apversion", "aptimestamp_release", "aprelease_notes", "apurl_download" };
        final Object[] VALUES = new Object[] { appPlatform.getAppId(), appPlatform.getPlatformId(),
                appPlatform.isEnabled() ? 1 : 0, appPlatform.getVersion(),
                appPlatform.getTimestampRelease(), appPlatform.getReleaseNotes(),
                appPlatform.geturlDownload() };
        insertIgnore(TABLE_APP_PLATFORM, COLUMNS, VALUES);
        removeFromCache(cacheKey(appPlatform));
        return (AppPlatformBo) appPlatform.markClean();
    }

    /**
     * Deletes an existing app platform.
     * 
     * @param appPlatform
     */
    public static void delete(AppPlatformBo appPlatform) {
        final String[] COLUMNS = new String[] { "app_id", "platform_id" };
        final Object[] VALUES = new Object[] { appPlatform.getAppId(), appPlatform.getPlatformId() };
        delete(TABLE_APP_PLATFORM, COLUMNS, VALUES);
        removeFromCache(cacheKey(appPlatform));
    }

    /**
     * Gets an app platform by id.
     * 
     * @param appId
     * @param platformId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AppPlatformBo getAppPlatform(String appId, String platformId) {
        final String CACHE_KEY = cacheKeyAppPlatform(appId, platformId);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT app_id AS {1}, platform_id AS {2}, apis_enabled AS {3}, apversion AS {4}, aptimestamp_release AS {5}, aprelease_notes AS {6}, apurl_download AS {7} FROM {0} WHERE app_id=? AND platform_id=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_PLATFORM,
                    AppPlatformBo.COL_APP_ID, AppPlatformBo.COL_PLATFORM_ID,
                    AppPlatformBo.COL_IS_ENABLED, AppPlatformBo.COL_VERSION,
                    AppPlatformBo.COL_TIMESTAMP_RELEASE, AppPlatformBo.COL_RELEASE_NOTES,
                    AppPlatformBo.COL_URL_DOWNLOAD);
            final Object[] WHERE_VALUES = new Object[] { appId, platformId };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (AppPlatformBo) new AppPlatformBo().fromMap(dbRow) : null;
    }

    /**
     * Updates an existing app platform.
     * 
     * @param appPlatform
     * @return
     */
    public static AppPlatformBo update(AppPlatformBo appPlatform) {
        if (appPlatform.isDirty()) {
            final String CACHE_KEY = cacheKey(appPlatform);
            final String[] COLUMNS = new String[] { "apis_enabled", "apversion",
                    "aptimestamp_release", "aprelease_notes", "apurl_download" };
            final Object[] VALUES = new Object[] { appPlatform.isEnabled() ? 1 : 0,
                    appPlatform.getVersion(), appPlatform.getTimestampRelease(),
                    appPlatform.getReleaseNotes(), appPlatform.geturlDownload() };
            final String[] WHERE_COLUMNS = new String[] { "app_id", "platform_id" };
            final Object[] WHERE_VALUES = new Object[] { appPlatform.getAppId(),
                    appPlatform.getPlatformId() };
            update(TABLE_APP_PLATFORM, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = appPlatform.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (AppPlatformBo) appPlatform.markClean();
    }
}
