INSERT INTO test_db_rollbook.tb_role (active,created_at,created_by,name) VALUES
    (100,'2025-09-30 11:52:21.028934','admin','ROLE_ADMIN'),
    (100,'2025-09-30 11:52:43.310287','admin','ROLE_MANAGER'),
    (100,'2025-09-30 11:52:53.755401','admin','ROLE_USER'),
    (100,'2025-09-30 11:52:54.984641','admin','ROLE_COMMON');

INSERT INTO test_db_rollbook.tb_tag (active,created_at,created_by,updated_at,updated_by,name) VALUES
    (100,'2025-09-30 11:56:13.825637','admin','2025-09-30 11:56:13.825637','admin','tag01'),
    (100,'2025-09-30 11:56:28.644948','admin','2025-09-30 11:56:28.644948','admin','tag02');

INSERT INTO test_db_rollbook.tb_terminal (active,created_at,created_by,updated_at,updated_by,name) VALUES
    (100,'2025-09-30 11:59:02.186687','admin','2025-09-30 11:59:02.186687','admin','terminal00'),
    (100,'2025-09-30 11:59:13.551680','admin','2025-09-30 11:59:13.551680','admin','terminal01');

INSERT INTO test_db_rollbook.tb_user (active,created_at,created_by,updated_at,updated_by,email,name,password,passworded_at,phone,username,tag_id) VALUES
    (100,'2025-09-30 11:53:28.115607','system_booted','2025-09-30 11:53:28.115607','system_booted','','system_booting','$2a$10$fsbRc/dTWbbrGNsxOVSUHuYDfJKY1FChW9Z.dTWzwdYnEfvOcMmqG',NULL,'','system_booting',NULL),
    (100,'2025-09-30 11:54:01.651349','system_booted','2025-09-30 11:54:01.651349','system_booted','','system_booted','$2a$10$sBm.QF3nin61nT8GkED.7OkIFJtcZGqBw4Gz/gtDPT1Fq/yAT9.qW',NULL,'','system_booted',NULL),
    (100,'2025-09-30 11:54:19.346114','system_booted','2025-09-30 11:54:19.346114','system_booted','','admin','$2a$10$zi9mqlCCW38AxaEP4xlIXuSktkojA95enFwLPtHdicDoZ//b.Wu0e',NULL,'','admin',NULL),
    (100,'2025-09-30 11:54:53.257327','system_booted','2025-09-30 11:54:53.257327','system_booted','','manager00','$2a$10$tD9CT.11TXw7njBKJ/UdGO8bVtOzkQOqMob/P.1AxaTviFmzUyc4W',NULL,'','manager00',NULL),
    (100,'2025-09-30 11:55:05.719286','system_booted','2025-09-30 11:55:05.719286','system_booted','','manager01','$2a$10$mFb8sNxJCEJPo1Q5/Uat2.zVdt/nDhqSslWmPfGrHPSRmpJOMQcZG',NULL,'','manager01',NULL),
    (100,'2025-09-30 11:55:30.940468','system_booted','2025-09-30 11:55:30.940468','system_booted','','user00','$2a$10$LY2pvN73tD2zkguD..aR2.4hAM8s89qVo1oYDUt4WV/BW0X2inZhO',NULL,'','user00',NULL),
    (100,'2025-09-30 11:56:14.073155','system_booted','2025-09-30 11:56:14.073155','system_booted','','user01','$2a$10$kZVP0/GVLJVMDWGD63C5yOjKtpNYo70jTJwbC92m7O3GBOyjpue1q',NULL,'','user01',1),
    (100,'2025-09-30 11:56:28.819710','system_booted','2025-09-30 11:56:28.819710','system_booted','','user02','$2a$10$.AGKWPsjn7eTA20ToLu1LOOrwcdfI./0vCjbPVdgSwiADJZlh8QZS',NULL,'','user02',2);

INSERT INTO test_db_rollbook.tb_user_role (user_id,role_id) VALUES
    (1,1),
    (2,1),
    (3,1),
    (4,2),
    (4,4),
    (5,2),
    (5,4),
    (6,3),
    (6,4),
    (7,3),
    (7,4),
    (8,3),
    (8,4);

INSERT INTO test_db_rollbook.tb_endpoint (created_at,created_by,active,`method`,name,`parameter`,`path`) VALUES
    ('2025-09-30 12:25:15.166847','system_booting',100,'POST','POST /v1/user/signup (ControllerUser#signUp(body:RequestSignUp))','(ControllerUser#signUp(body:RequestSignUp))','/v1/user/signup'),
    ('2025-09-30 12:25:15.341473','system_booting',100,'POST','POST /v1/user/signin (ControllerUser#signIn(body:RequestSignIn))','(ControllerUser#signIn(body:RequestSignIn))','/v1/user/signin'),
    ('2025-09-30 12:25:15.678869','system_booting',100,'POST','POST /v1/admin/role/{roleName} (ControllerRole#registerRole(path:roleName))','(ControllerRole#registerRole(path:roleName))','/v1/admin/role/{roleName}'),
    ('2025-09-30 12:25:16.503705','system_booting',100,'DELETE','DELETE /v1/admin/role/{roleId} (ControllerRole#removeRole(path:roleId))','(ControllerRole#removeRole(path:roleId))','/v1/admin/role/{roleId}'),
    ('2025-09-30 12:25:16.842410','system_booting',100,'POST','POST /v1/admin/security/endpoint/{endpointId}/role/{roleId} (ControllerSecurity#addRoleToEndpoint(path:endpointId, path:roleId))','(ControllerSecurity#addRoleToEndpoint(path:endpointId, path:roleId))','/v1/admin/security/endpoint/{endpointId}/role/{roleId}'),
    ('2025-09-30 12:25:16.986516','system_booting',100,'DELETE','DELETE /v1/admin/security/endpoint/{endpointId}/role/{roleId} (ControllerSecurity#removeRoleFromEndpoint(path:endpointId, path:roleId))','(ControllerSecurity#removeRoleFromEndpoint(path:endpointId, path:roleId))','/v1/admin/security/endpoint/{endpointId}/role/{roleId}'),
    ('2025-09-30 12:25:17.010209','system_booting',100,'POST','POST /v1/admin/security/user/{userId}/role/{roleId} (ControllerSecurity#addRoleToUser(path:userId, path:roleId))','(ControllerSecurity#addRoleToUser(path:userId, path:roleId))','/v1/admin/security/user/{userId}/role/{roleId}'),
    ('2025-09-30 12:25:17.195147','system_booting',100,'DELETE','DELETE /v1/admin/security/user/{userId}/role/{roleId} (ControllerSecurity#removeRoleFromUser(path:userId, path:roleId))','(ControllerSecurity#removeRoleFromUser(path:userId, path:roleId))','/v1/admin/security/user/{userId}/role/{roleId}'),
    ('2025-09-30 12:25:17.726300','system_booting',100,'POST','POST /v1/admin/endpoint/scan (ControllerEndpoint#scanEnpoint())','(ControllerEndpoint#scanEnpoint())','/v1/admin/endpoint/scan'),
    ('2025-09-30 12:25:17.960625','system_booting',100,'POST','POST /v1/admin/endpoint/refresh (ControllerEndpoint#refreshCache())','(ControllerEndpoint#refreshCache())','/v1/admin/endpoint/refresh'),
    ('2025-09-30 12:25:18.654787','system_booting',100,'POST','POST /v1/admin/logging/aop (ControllerLoggingConfig#setLogingAop(param:request, param:response, param:database))','(ControllerLoggingConfig#setLogingAop(param:request, param:response, param:database))','/v1/admin/logging/aop'),
    ('2025-09-30 12:25:18.703902','system_booting',100,'GET','GET /v1/admin/logging/aop (ControllerLoggingConfig#getLoggingAop())','(ControllerLoggingConfig#getLoggingAop())','/v1/admin/logging/aop'),
    ('2025-09-30 12:25:19.445472','system_booting',100,'POST','POST /v1/admin/logging/level/{loggerName} (ControllerLoggingConfig#setLoggingLevel(path:loggerName, param:level))','(ControllerLoggingConfig#setLoggingLevel(path:loggerName, param:level))','/v1/admin/logging/level/{loggerName}'),
    ('2025-09-30 12:25:19.510861','system_booting',100,'GET','GET /v1/admin/logging/level/{loggerName} (ControllerLoggingConfig#getLoggingLevel(path:loggerName))','(ControllerLoggingConfig#getLoggingLevel(path:loggerName))','/v1/admin/logging/level/{loggerName}'),
    ('2025-09-30 12:25:19.545880','system_booting',100,'POST','POST /v1/admin/logging/levels (ControllerLoggingConfig#setAllLoggingLevels(body:List))','(ControllerLoggingConfig#setAllLoggingLevels(body:List))','/v1/admin/logging/levels'),
    ('2025-09-30 12:25:19.675296','system_booting',100,'GET','GET /v1/admin/logging/levels (ControllerLoggingConfig#getAllLoggingLevels())','(ControllerLoggingConfig#getAllLoggingLevels())','/v1/admin/logging/levels'),
    ('2025-09-30 12:25:20.175285','system_booting',100,'GET','GET /v1/admin/user (ControllerUser#getUsers(param:username, param:name, param:tag))','(ControllerUser#getUsers(param:username, param:name, param:tag))','/v1/admin/user');
