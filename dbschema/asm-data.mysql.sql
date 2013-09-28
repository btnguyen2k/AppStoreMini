-- Default data for platforms

INSERT INTO asm_platform (pid, pis_enabled, ptitle, picon16, picon24, picon32, picon48, picon64)
VALUES
    ('1', 1, 'Android', '/assets/images/icons/platform-android-16x16.png',
        '/assets/images/icons/platform-android-24x24.png',
        '/assets/images/icons/platform-android-32x32.png',
        '/assets/images/icons/platform-android-48x48.png',
        '/assets/images/icons/platform-android-64x64.png')
   ,('2', 1, 'Black Berry', '/assets/images/icons/platform-blackberry-16x16.png',
        '/assets/images/icons/platform-blackberry-24x24.png',
        '/assets/images/icons/platform-blackberry-32x32.png',
        '/assets/images/icons/platform-blackberry-48x48.png',
        '/assets/images/icons/platform-blackberry-64x64.png')
   ,('3', 1, 'iOS', '/assets/images/icons/platform-ios-16x16.png',
        '/assets/images/icons/platform-ios-24x24.png',
        '/assets/images/icons/platform-ios-32x32.png',
        '/assets/images/icons/platform-ios-48x48.png',
        '/assets/images/icons/platform-ios-64x64.png')
   ,('4', 1, 'Windows Phone', '/assets/images/icons/platform-winphone-16x16.png',
        '/assets/images/icons/platform-winphone-24x24.png',
        '/assets/images/icons/platform-winphone-32x32.png',
        '/assets/images/icons/platform-winphone-48x48.png',
        '/assets/images/icons/platform-winphone-64x64.png')
   ;    

INSERT INTO asm_usergroup (gid, gtitle)
VALUES
    ('1', 'Administrator')
   ,('2', 'Team Member')
   ;

INSERT INTO asm_user (uid, ulogin_name, upassword, uemail, ugroup_id, utimestamp_create)
VALUES
    ('1', 'admin', LOWER(MD5('password')), 'admin@localhost', '1', NOW());
