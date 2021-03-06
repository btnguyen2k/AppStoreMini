-- table to store list of platforms
DROP TABLE IF EXISTS asm_platform;
CREATE TABLE asm_platform (
    pid                                     VARCHAR(32)         NOT NULL,
    pis_enabled                             TINYINT             NOT NULL DEFAULT 1,
    ptitle                                  VARCHAR(16)         NOT NULL,
    picon16                                 VARCHAR(255),
    picon24                                 VARCHAR(255),
    picon32                                 VARCHAR(255),
    picon48                                 VARCHAR(255),
    picon64                                 VARCHAR(255),
    PRIMARY KEY (pid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store website's user groups
DROP TABLE IF EXISTS asm_usergroup;
CREATE TABLE asm_usergroup (
    gid                                     VARCHAR(32)         NOT NULL,
    gtitle                                  VARCHAR(50)         NOT NULL,
    PRIMARY KEY (gid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store website's users
DROP TABLE IF EXISTS asm_user;
CREATE TABLE asm_user (
    uid                                     VARCHAR(32)         NOT NULL,
    ulogin_name                             VARCHAR(64)         NOT NULL,
        UNIQUE INDEX (ulogin_name),
    upassword                               VARCHAR(64)         NOT NULL,
    uemail                                  VARCHAR(100)        NOT NULL,
    ugroup_id                               VARCHAR(32),
        INDEX (ugroup_id),
    utimestamp_create                       TIMESTAMP           NOT NULL DEFAULT NOW(),
        INDEX (utimestamp_create),
    PRIMARY KEY (uid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store app categories
DROP TABLE IF EXISTS asm_appcategory;
CREATE TABLE asm_appcategory (
    cid                                     VARCHAR(32)         NOT NULL,
    cparent_id                              VARCHAR(32),
        INDEX (cparent_id),
    cposition                               INT                 NOT NULL DEFAULT 0,
    ctitle                                  VARCHAR(100)        NOT NULL,
    PRIMARY KEY (cid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store list of applications
DROP TABLE IF EXISTS asm_app;
CREATE TABLE asm_app (
    aid                                     VARCHAR(32)         NOT NULL,
    acategory_id                            VARCHAR(32),
        INDEX (acategory_id),
    aposition                               INT                 NOT NULL DEFAULT 0,
    atitle                                  VARCHAR(100)        NOT NULL,
    aicon                                   VARCHAR(255),
    asummary                                TEXT,
    PRIMARY KEY (aid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store application's releases
DROP TABLE IF EXISTS asm_app_release;
CREATE TABLE asm_app_release (
    app_id                                  VARCHAR(32)         NOT NULL,
    platform_id                             VARCHAR(32)         NOT NULL,
    release_version                         VARCHAR(32)         NOT NULL,
        PRIMARY KEY (app_id, platform_id, release_version),
    is_enabled                              TINYINT             NOT NULL DEFAULT 1,
    release_timestamp                       DATETIME            NOT NULL,
    release_notes                           TEXT,
    url_download                            VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- table to store html pages
DROP TABLE IF EXISTS asm_page;
CREATE TABLE asm_page (
    page_id                                 VARCHAR(16),
    page_title                              VARCHAR(40),
    page_content                            TEXT,
    
    PRIMARY KEY (page_id)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
