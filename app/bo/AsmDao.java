package bo;

import com.github.ddth.plommon.bo.BaseDao;

public class AsmDao extends BaseDao {
    /*----------------------------------------------------------------------*/
    /**
     * Creates a new platform info entry.
     * 
     * @param platformBo
     * @return
     */
    public static PlatformBo createPlatform(PlatformBo platformBo) {
        String SQL = "INSERT IGNORE INTO {0} (pid, pis_enabled, ptitle, picon16, picon24, picon32, picon48, picon64) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // JdbcTemplate jdbcTemplate = jdbcTemplate();
        // jdbcTemplate.update(MessageFormat.format(SQL, TABLE_ACCOUNT), new
        // Object[] { email });
        // removeFromCache(cacheKeyAccount(email));
        return platformBo;
    }
    /*----------------------------------------------------------------------*/
}
