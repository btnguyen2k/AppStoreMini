-- AppStoreMini tables --

DROP TABLE IF EXISTS asm_platform;
CREATE TABLE asm_platform (
    pid                                     INT                 NOT NULL AUTO_INCREMENT,
    pis_enabled                             TINYINT             NOT NULL DEFAULT 1,
    ptitle                                  VARCHAR(16)         NOT NULL,
    picon16                                 VARCHAR(255),
    picon24                                 VARCHAR(255),
    picon32                                 VARCHAR(255),
    picon48                                 VARCHAR(255),
    picon64                                 VARCHAR(255),
    PRIMARY KEY (pid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

DROP TABLE IF EXISTS asm_usergroup;
DROP TABLE IF EXISTS asm_user;

CREATE TABLE asm_usergroup (
    gid                                     VARCHAR(32)         NOT NULL,
    gtitle                                  VARCHAR(50)         NOT NULL,
    PRIMARY KEY (gid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

CREATE TABLE asm_user (
    uid                                     VARCHAR(32)         NOT NULL,
    ulogin_name                             VARCHAR(64)         NOT NULL,
    upassword                               VARCHAR(64)         NOT NULL,
    ugroup                                  VARCHAR(32),
    utimestamp_create                       TIMESTAMP           NOT NULL DEFAULT NOW(),
    PRIMARY KEY (uid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

--------------------------------------------------
-- OLD SCHEMA
--------------------------------------------------

DROP TABLE IF EXISTS mypages_account;
CREATE TABLE mypages_account (
    uemail                            VARCHAR(128),
    utimestamp_create                 TIMESTAMP,
        INDEX (utimestamp_create),
    PRIMARY KEY (uemail)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

DROP TABLE IF EXISTS mypages_page;
CREATE TABLE mypages_page (
    pid                                VARCHAR(32),
    padmin_email                       VARCHAR(128),
    ptimestamp_create                  TIMESTAMP,
        INDEX (ptimestamp_create),
    ptimestamp_lastactive              DATETIME,
        INDEX (ptimestamp_lastactive),
    pstatus                            INT                        NOT NULL DEFAULT 0,
    psettings                          TEXT,
    PRIMARY KEY (pid, padmin_email)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

DROP TABLE IF EXISTS mypages_master_feed;
CREATE TABLE mypages_master_feed (
    fid                                VARCHAR(64),
    feed_id                            VARCHAR(64),
    feed_type                          INT                        NOT NULL DEFAULT 0,
    fmetainfo                          TEXT,
    fuser_email                        VARCHAR(128),
        INDEX (fuser_email),
    fpage_id                           VARCHAR(32),
        INDEX (fpage_id),
    ftimestamp                         DATETIME,
        INDEX (ftimestamp),
    fnum_likes                         INT                        NOT NULL DEFAULT 0,
    fnum_shares                        INT                        NOT NULL DEFAULT 0,
    fnum_comments                      INT                        NOT NULL DEFAULT 0,
    PRIMARY KEY (fid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
