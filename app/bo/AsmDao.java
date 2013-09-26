package bo;

import com.github.ddth.plommon.bo.BaseMysqlDao;

public class AsmDao extends BaseMysqlDao {

    public final static String TABLE_PLATFORM = "asm_platform";

    /*----------------------------------------------------------------------*/
    /**
     * Creates a new platform info entry.
     * 
     * @param platformBo
     * @return
     */
    public static PlatformBo createPlatform(PlatformBo platformBo) {
        final String[] COLUMNS = new String[] {};
        final Object[] VALUES = new Object[] {};
        int dbResult = insertIgnore(TABLE_PLATFORM, COLUMNS, VALUES);
        // removeFromCache(cacheKey(platformBo));
        return platformBo;
    }
    /*----------------------------------------------------------------------*/
}
