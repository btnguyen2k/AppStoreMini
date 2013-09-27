package bo;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.github.ddth.plommon.bo.BaseMysqlDao;

public class AsmDao extends BaseMysqlDao {

    public final static String TABLE_PLATFORM = "asm_platform";

    /*----------------------------------------------------------------------*/
    private static String cacheKeyPlatform(String platformId) {
        return "PLATFORM_" + platformId;
    }

    private static String cacheKey(PlatformBo platform) {
        return cacheKeyPlatform(platform.getId());
    }

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new platform info entry.
     * 
     * @param platformBo
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
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static PlatformBo get(String id, Class<PlatformBo> clazz) {
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
}
