package bo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.github.ddth.plommon.bo.BaseMysqlDao;
import com.github.ddth.plommon.utils.DPathUtils;

public class AsmDao extends BaseMysqlDao {

    public final static String TABLE_PLATFORM = "asm_platform";
    public final static String TABLE_USERGROUP = "asm_usergroup";
    public final static String TABLE_USER = "asm_user";
    public final static String TABLE_APP_CATEGORY = "asm_appcategory";
    public final static String TABLE_APPLICATION = "asm_app";
    public final static String TABLE_APP_RELEASE = "asm_app_release";

    private final static ApplicationBo[] EMPTY_ARR_APPLICATION_BO = new ApplicationBo[0];
    private final static AppReleaseBo[] EMPTY_ARR_APP_RELEASE_BO = new AppReleaseBo[0];
    private final static PlatformBo[] EMPTY_ARR_PLATFORM_BO = new PlatformBo[0];
    private final static AppCategoryBo[] EMPTY_ARR_APP_CATEGORY_BO = new AppCategoryBo[0];
    private final static UserBo[] EMPTY_ARR_USER_BO = new UserBo[0];
    private final static UsergroupBo[] EMPTY_ARR_USERGROUP_BO = new UsergroupBo[0];

    /*----------------------------------------------------------------------*/
    private static String cacheKeyPlatform(String id) {
        return "PLATFORM_" + id;
    }

    private static String cacheKeyAllPlatforms() {
        return "ALL_PLATFORMS";
    }

    private static String cacheKey(PlatformBo platform) {
        return cacheKeyPlatform(platform.getId());
    }

    private static void invalidate(PlatformBo platform) {
        removeFromCache(cacheKey(platform));
        removeFromCache(cacheKeyAllPlatforms());
    }

    private static String cacheKeyUsergroup(String id) {
        return "USERGROUP_" + id;
    }

    private static String cacheKey(UsergroupBo usergroup) {
        return cacheKeyUsergroup(usergroup.getId());
    }

    private static String cacheKeyAllUsergroups() {
        return "ALL_USERGROUPS";
    }

    private static void invalidate(UsergroupBo usergroup) {
        removeFromCache(cacheKey(usergroup));
        removeFromCache(cacheKeyAllUsergroups());
    }

    private static String cacheKeyUser(String id) {
        return "USER_" + id;
    }

    private static String cacheKey(UserBo user) {
        return cacheKeyUser(user.getId());
    }

    private static String cacheKeyAllUsers() {
        return "ALL_USERS";
    }

    private static void invalidate(UserBo user) {
        removeFromCache(cacheKey(user));
        removeFromCache(cacheKeyAllUsers());
    }

    private static String cacheKeyAppCategory(String id) {
        return "APPCAT_" + id;
    }

    private static String cacheKeyAllAppCategories() {
        return "ALL_APPCATS";
    }

    private static String cacheKey(AppCategoryBo appCat) {
        return cacheKeyAppCategory(appCat.getId());
    }

    private static void invalidate(AppCategoryBo appCat) {
        removeFromCache(cacheKey(appCat));
        removeFromCache(cacheKeyAllAppCategories());
    }

    private static String cacheKeyApplication(String id) {
        return "APP_" + id;
    }

    private static String cacheKeyAllApplications() {
        return "ALL_APPS";
    }

    private static String cacheKey(ApplicationBo app) {
        return cacheKeyApplication(app.getId());
    }

    private static void invalidate(ApplicationBo app) {
        removeFromCache(cacheKey(app));
        removeFromCache(cacheKeyAllApplications());
    }

    private static String cacheKeyAppRelease(String appId, String platformId, String version) {
        return "APPRELEASE_" + appId + "_" + platformId + "_" + version;
    }

    private static String cacheKeyLatestAppRelease(String appId, String platformId) {
        return "LATEST_APPRELEASE_" + appId + "_" + platformId;
    }

    private static String cacheKey(AppReleaseBo appRelease) {
        return cacheKeyAppRelease(appRelease.getAppId(), appRelease.getPlatformId(),
                appRelease.getVersion());
    }

    private static void invalidate(AppReleaseBo appRelease) {
        removeFromCache(cacheKey(appRelease));
        removeFromCache(cacheKeyLatestAppRelease(appRelease.getAppId(), appRelease.getPlatformId()));
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
        invalidate(platform);
        return (PlatformBo) platform.markClean();
    }

    /**
     * Deletes an existing platform.
     * 
     * @param platform
     */
    public static void delete(PlatformBo platform) {
        delete(TABLE_APP_RELEASE, new String[] { "platform_id" }, new Object[] { platform.getId() });
        delete(TABLE_PLATFORM, new String[] { "pid" }, new Object[] { platform.getId() });
        invalidate(platform);
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
     * Gets all platforms as a list.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static PlatformBo[] getAllPlatforms() {
        final String CACHE_KEY = cacheKeyAllPlatforms();
        List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
        if (dbRows == null) {
            final String SQL_TEMPLATE = "SELECT pid AS {1} FROM {0} ORDER BY pid";
            final String SQL = MessageFormat
                    .format(SQL_TEMPLATE, TABLE_PLATFORM, PlatformBo.COL_ID);
            final Object[] PARAM_VALUES = new Object[] {};
            dbRows = select(SQL, PARAM_VALUES);
            putToCache(CACHE_KEY, dbRows);
        }
        List<PlatformBo> result = new ArrayList<PlatformBo>();
        if (dbRows != null) {
            for (Map<String, Object> dbRow : dbRows) {
                String id = DPathUtils.getValue(dbRow, PlatformBo.COL_ID, String.class);
                PlatformBo platform = getPlatform(id);
                if (platform != null) {
                    result.add(platform);
                }
            }
        }
        return result.toArray(EMPTY_ARR_PLATFORM_BO);
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
        invalidate(usergroup);
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
        invalidate(usergroup);
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
     * Gets all usergroups as a list.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UsergroupBo[] getAllUsergroups() {
        final String CACHE_KEY = cacheKeyAllUsergroups();
        List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
        if (dbRows == null) {
            final String SQL_TEMPLATE = "SELECT gid AS {1} FROM {0} ORDER BY gtitle";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_USERGROUP,
                    UsergroupBo.COL_ID);
            final Object[] PARAM_VALUES = new Object[] {};
            dbRows = select(SQL, PARAM_VALUES);
            putToCache(CACHE_KEY, dbRows);
        }
        List<UsergroupBo> result = new ArrayList<UsergroupBo>();
        if (dbRows != null) {
            for (Map<String, Object> dbRow : dbRows) {
                String id = DPathUtils.getValue(dbRow, UsergroupBo.COL_ID, String.class);
                UsergroupBo usergroup = getUsergroup(id);
                if (usergroup != null) {
                    result.add(usergroup);
                }
            }
        }
        return result.toArray(EMPTY_ARR_USERGROUP_BO);
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
        invalidate(user);
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
        invalidate(user);
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
     * Gets all users as a list.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UserBo[] getAllUsers() {
        final String CACHE_KEY = cacheKeyAllUsers();
        List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
        if (dbRows == null) {
            final String SQL_TEMPLATE = "SELECT uid AS {1} FROM {0} ORDER BY ugroup_id, ulogin_name";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_USER, UserBo.COL_ID);
            final Object[] PARAM_VALUES = new Object[] {};
            dbRows = select(SQL, PARAM_VALUES);
            putToCache(CACHE_KEY, dbRows);
        }
        List<UserBo> result = new ArrayList<UserBo>();
        if (dbRows != null) {
            for (Map<String, Object> dbRow : dbRows) {
                String id = DPathUtils.getValue(dbRow, UserBo.COL_ID, String.class);
                UserBo user = getUser(id);
                if (user != null) {
                    result.add(user);
                }
            }
        }
        return result.toArray(EMPTY_ARR_USER_BO);
    }

    /**
     * Gets a user account by login name
     * 
     * @param loginname
     * @return
     */
    public static UserBo getUserByLoginname(String loginname) {
        final String SQL_TEMPLATE = "SELECT uid AS {1}, ulogin_name AS {2} FROM {0} WHERE ulogin_name=?";
        final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_USER, UserBo.COL_ID,
                UserBo.COL_LOGIN_NAME);
        final Object[] WHERE_VALUES = new Object[] { loginname };
        List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
        Map<String, Object> dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0)
                : null;
        return getUser(DPathUtils.getValue(dbRow, UserBo.COL_ID, String.class));
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
        invalidate(appCat);
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
        invalidate(appCat);
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
     * Gets all app categories as a list.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AppCategoryBo[] getAllAppCategories() {
        final String CACHE_KEY = cacheKeyAllAppCategories();
        List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
        if (dbRows == null) {
            final String SQL_TEMPLATE = "SELECT cid AS {1} FROM {0} ORDER BY cposition";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_CATEGORY,
                    AppCategoryBo.COL_ID);
            final Object[] PARAM_VALUES = new Object[] {};
            dbRows = select(SQL, PARAM_VALUES);
            putToCache(CACHE_KEY, dbRows);
        }
        List<AppCategoryBo> result = new ArrayList<AppCategoryBo>();
        if (dbRows != null) {
            for (Map<String, Object> dbRow : dbRows) {
                String id = DPathUtils.getValue(dbRow, AppCategoryBo.COL_ID, String.class);
                AppCategoryBo category = getAppCategory(id);
                if (category != null) {
                    result.add(category);
                }
            }
        }
        return result.toArray(EMPTY_ARR_APP_CATEGORY_BO);
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
            removeFromCache(cacheKeyAllAppCategories());
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
                "aicon", "asummary" };
        final Object[] VALUES = new Object[] { app.getId(), app.getCategoryId(), app.getPosition(),
                app.getTitle(), app.getIcon(), app.getSummary() };
        insertIgnore(TABLE_APPLICATION, COLUMNS, VALUES);
        invalidate(app);
        return (ApplicationBo) app.markClean();
    }

    /**
     * Deletes an existing application.
     * 
     * @param app
     */
    public static void delete(ApplicationBo app) {
        delete(TABLE_APP_RELEASE, new String[] { "app_id" }, new Object[] { app.getId() });
        delete(TABLE_APPLICATION, new String[] { "aid" }, new Object[] { app.getId() });
        invalidate(app);
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
            final String SQL_TEMPLATE = "SELECT aid AS {1}, acategory_id AS {2}, aposition AS {3}, atitle AS {4}, aicon AS {5}, asummary AS {6} FROM {0} WHERE aid=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APPLICATION,
                    ApplicationBo.COL_ID, ApplicationBo.COL_CAT_ID, ApplicationBo.COL_POSITION,
                    ApplicationBo.COL_TITLE, ApplicationBo.COL_ICON, ApplicationBo.COL_SUMMARY);
            final Object[] WHERE_VALUES = new Object[] { id };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (ApplicationBo) new ApplicationBo().fromMap(dbRow) : null;
    }

    /**
     * Gets all applications as a list.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ApplicationBo[] getAllApplications() {
        final String CACHE_KEY = cacheKeyAllApplications();
        List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
        if (dbRows == null) {
            final String SQL_TEMPLATE = "SELECT aid AS {1} FROM {0} ORDER BY aposition DESC";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APPLICATION,
                    ApplicationBo.COL_ID);
            final Object[] PARAM_VALUES = new Object[] {};
            dbRows = select(SQL, PARAM_VALUES);
            putToCache(CACHE_KEY, dbRows);
        }
        List<ApplicationBo> result = new ArrayList<ApplicationBo>();
        if (dbRows != null) {
            for (Map<String, Object> dbRow : dbRows) {
                String id = DPathUtils.getValue(dbRow, ApplicationBo.COL_ID, String.class);
                ApplicationBo application = getApplication(id);
                if (application != null) {
                    result.add(application);
                }
            }
        }
        return result.toArray(EMPTY_ARR_APPLICATION_BO);
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
            final String[] COLUMNS = new String[] { "acategory_id", "aposition", "atitle", "aicon",
                    "asummary" };
            final Object[] VALUES = new Object[] { app.getCategoryId(), app.getPosition(),
                    app.getTitle(), app.getIcon(), app.getSummary() };
            final String[] WHERE_COLUMNS = new String[] { "aid" };
            final Object[] WHERE_VALUES = new Object[] { app.getId() };
            update(TABLE_APPLICATION, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = app.toMap();
            invalidate(app);
            putToCache(CACHE_KEY, dbRow);
        }
        return (ApplicationBo) app.markClean();
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new app release.
     * 
     * @param appRelease
     * @return
     */
    public static AppReleaseBo create(AppReleaseBo appRelease) {
        final String[] COLUMNS = new String[] { "app_id", "platform_id", "release_version",
                "is_enabled", "release_timestamp", "release_notes", "url_download" };
        final Object[] VALUES = new Object[] { appRelease.getAppId(), appRelease.getPlatformId(),
                appRelease.getVersion(), appRelease.isEnabled() ? 1 : 0, appRelease.getTimestamp(),
                appRelease.getReleaseNotes(), appRelease.getUrlDownload() };
        insertIgnore(TABLE_APP_RELEASE, COLUMNS, VALUES);
        invalidate(appRelease);
        return (AppReleaseBo) appRelease.markClean();
    }

    /**
     * Deletes an existing app release.
     * 
     * @param appRelease
     */
    public static void delete(AppReleaseBo appRelease) {
        final String[] COLUMNS = new String[] { "app_id", "platform_id", "release_version" };
        final Object[] VALUES = new Object[] { appRelease.getAppId(), appRelease.getPlatformId(),
                appRelease.getVersion() };
        delete(TABLE_APP_RELEASE, COLUMNS, VALUES);
        invalidate(appRelease);
    }

    /**
     * Gets an app release by id.
     * 
     * @param appId
     * @param platformId
     * @param version
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AppReleaseBo getAppRelease(String appId, String platformId, String version) {
        final String CACHE_KEY = cacheKeyAppRelease(appId, platformId, version);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT app_id AS {1}, platform_id AS {2}, release_version AS {3}, is_enabled AS {4}, release_timestamp AS {5}, release_notes AS {6}, url_download AS {7}"
                    + " FROM {0} WHERE app_id=? AND platform_id=? AND release_version=?";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_RELEASE,
                    AppReleaseBo.COL_APP_ID, AppReleaseBo.COL_PLATFORM_ID,
                    AppReleaseBo.COL_VERSION, AppReleaseBo.COL_IS_ENABLED,
                    AppReleaseBo.COL_TIMESTAMP, AppReleaseBo.COL_RELEASE_NOTES,
                    AppReleaseBo.COL_URL_DOWNLOAD);
            final Object[] WHERE_VALUES = new Object[] { appId, platformId, version };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (AppReleaseBo) new AppReleaseBo().fromMap(dbRow) : null;
    }

    /**
     * Gets app's latest release on a platform.
     * 
     * @param appId
     * @param platformId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AppReleaseBo getLatestAppRelease(String appId, String platformId) {
        final String CACHE_KEY = cacheKeyLatestAppRelease(appId, platformId);
        Map<String, Object> dbRow = getFromCache(CACHE_KEY, Map.class);
        if (dbRow == null) {
            final String SQL_TEMPLATE = "SELECT app_id AS {1}, platform_id AS {2}, release_version AS {3}, is_enabled AS {4}, release_timestamp AS {5}, release_notes AS {6}, url_download AS {7}"
                    + " FROM {0} WHERE app_id=? AND platform_id=? ORDER BY release_timestamp DESC LIMIT 1";
            final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_RELEASE,
                    AppReleaseBo.COL_APP_ID, AppReleaseBo.COL_PLATFORM_ID,
                    AppReleaseBo.COL_VERSION, AppReleaseBo.COL_IS_ENABLED,
                    AppReleaseBo.COL_TIMESTAMP, AppReleaseBo.COL_RELEASE_NOTES,
                    AppReleaseBo.COL_URL_DOWNLOAD);
            final Object[] WHERE_VALUES = new Object[] { appId, platformId };
            List<Map<String, Object>> dbResult = select(SQL, WHERE_VALUES);
            dbRow = dbResult != null && dbResult.size() > 0 ? dbResult.get(0) : null;
            putToCache(CACHE_KEY, dbRow);
        }
        return dbRow != null ? (AppReleaseBo) new AppReleaseBo().fromMap(dbRow) : null;
    }

    /**
     * Gets app's latest releases per each platform.
     * 
     * @param appId
     * @return
     */
    public static AppReleaseBo[] getLatestReleasesPerPlatform(String appId) {
        List<AppReleaseBo> result = new ArrayList<AppReleaseBo>();
        PlatformBo[] allPlatforms = getAllPlatforms();
        if (allPlatforms != null && allPlatforms.length > 0) {
            for (PlatformBo platform : allPlatforms) {
                AppReleaseBo latestRelease = getLatestAppRelease(appId, platform.getId());
                if (latestRelease != null) {
                    result.add(latestRelease);
                }
            }
            Collections.sort(result, new Comparator<AppReleaseBo>() {
                @Override
                public int compare(AppReleaseBo o1, AppReleaseBo o2) {
                    return o2.getTimestamp().compareTo(o1.getTimestamp());
                }
            });
        }
        return result.toArray(EMPTY_ARR_APP_RELEASE_BO);
    }

    // /**
    // * Gets app platform list for an application.
    // *
    // * @param app
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public static AppPlatformBo[] getAppPlatforms(ApplicationBo app) {
    // final String CACHE_KEY = cacheKeyAppPlatforms(app);
    // List<Map<String, Object>> dbRows = getFromCache(CACHE_KEY, List.class);
    // if (dbRows == null) {
    // final String SQL_TEMPLATE =
    // "SELECT app_id AS {1}, platform_id AS {2} FROM {0} ORDER BY platform_id WHERE app_id=?";
    // final String SQL = MessageFormat.format(SQL_TEMPLATE, TABLE_APP_PLATFORM,
    // AppPlatformBo.COL_APP_ID, AppPlatformBo.COL_PLATFORM_ID);
    // final Object[] PARAM_VALUES = new Object[] { app.getId() };
    // dbRows = select(SQL, PARAM_VALUES);
    // putToCache(CACHE_KEY, dbRows);
    // }
    // List<AppPlatformBo> result = new ArrayList<AppPlatformBo>();
    // if (dbRows != null) {
    // for (Map<String, Object> dbRow : dbRows) {
    // String appId = DPathUtils.getValue(dbRow, AppPlatformBo.COL_APP_ID,
    // String.class);
    // String platformId = DPathUtils.getValue(dbRow,
    // AppPlatformBo.COL_PLATFORM_ID,
    // String.class);
    // AppPlatformBo appPlatform = getAppPlatform(appId, platformId);
    // if (appPlatform != null) {
    // result.add(appPlatform);
    // }
    // }
    // }
    // return result.toArray(EMPTY_ARR_APP_PLATFORM_BO);
    // }

    /**
     * Updates an existing app platform.
     * 
     * @param appPlatform
     * @return
     */
    public static AppReleaseBo update(AppReleaseBo appPlatform) {
        if (appPlatform.isDirty()) {
            final String CACHE_KEY = cacheKey(appPlatform);
            final String[] COLUMNS = new String[] { "apis_enabled", "apversion",
                    "aptimestamp_release", "aprelease_notes", "apurl_download" };
            final Object[] VALUES = new Object[] { appPlatform.isEnabled() ? 1 : 0,
                    appPlatform.getVersion(), appPlatform.getTimestamp(),
                    appPlatform.getReleaseNotes(), appPlatform.getUrlDownload() };
            final String[] WHERE_COLUMNS = new String[] { "app_id", "platform_id" };
            final Object[] WHERE_VALUES = new Object[] { appPlatform.getAppId(),
                    appPlatform.getPlatformId() };
            update(TABLE_APP_RELEASE, COLUMNS, VALUES, WHERE_COLUMNS, WHERE_VALUES);
            Map<String, Object> dbRow = appPlatform.toMap();
            putToCache(CACHE_KEY, dbRow);
        }
        return (AppReleaseBo) appPlatform.markClean();
    }
}
