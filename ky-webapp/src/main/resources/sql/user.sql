
INSERT INTO user_privilege(id, privilege, privilege_name, description, create_time, update_time, is_default) VALUES
  ('25190948786929100', 'USER_CONFIG',   '用户管理', '拥有此权限的角色可以管理用户',           now(),  NULL, 1),
  ('25190948786929101', 'VIDEO_RESULT',  '视频查看', '拥有此权限的角色可以查看视频分析结果',     now(),  NULL, 1),
  ('25190948786929102', 'MSG_REGISTER',  '信息注册', '拥有此权限的角色可以注册视频需检索的信息',  now(),  NULL, 1),
  ('25190948786929103', 'SYSTEM_CONFIG', '系统配置', '拥有此权限的角色可以配置系统基础属性',      now(),  NULL, 1)
ON DUPLICATE KEY UPDATE privilege= VALUES(privilege);


INSERT INTO user_role(id, role, role_name, description, create_time, update_time, is_default) VALUES
  ('25190948786929200', 'ADMIN_ROLE',  '管理员角色',   '拥有用户管理、系统配置、信息注册、视频查看权限', now(),  NULL, 1),
  ('25190948786929201', 'USER_ROLE', '用户角色', '拥有信息注册、视频查看权限',  now(),  NULL, 1),
  ('25190948786929202', 'USER_CONFIG_ROLE', '用户管理角色', '拥有用户管理权限', now(),  NULL, 1),
  ('25190948786929203', 'VIDEO_RESULT_ROLE', '视频查看角色', '拥有视频查看权限', now(),  NULL, 1),
  ('25190948786929204', 'MSG_REGISTER_ROLE',  '信息注册角色', '拥有信息注册权限', now(),  NULL, 1),
  ('25190948786929205', 'SYSTEM_CONFIG_ROLE',  '系统配置角色', '拥有系统配置权限', now(),  NULL, 1)
ON DUPLICATE KEY UPDATE role= VALUES(role);


INSERT INTO user_role_user_privilege(user_role_id, privilege_id) VALUES
  ('25190948786929200', '25190948786929100'),
  ('25190948786929200', '25190948786929101'),
  ('25190948786929200', '25190948786929102'),
  ('25190948786929200', '25190948786929103'),
  ('25190948786929201', '25190948786929101'),
  ('25190948786929201', '25190948786929102'),
  ('25190948786929202', '25190948786929100'),
  ('25190948786929203', '25190948786929101'),
  ('25190948786929204', '25190948786929102'),
  ('25190948786929205', '25190948786929103')
ON DUPLICATE KEY UPDATE privilege_id= VALUES(privilege_id);


INSERT INTO user_login_ctrl(id, authentic_secret, consecutive_auth_failures_count, is_need_to_login_aas, last_attempt_login_time, last_pwd_modify_time, system_init_password) VALUES
  ('25190948786929300', '', 0, 1,  NULL, NULL,'super'),
  ('25190948786929301', '', 0, 1,  NULL, NULL,'admin')
ON DUPLICATE KEY UPDATE system_init_password = VALUES(system_init_password);


INSERT INTO user(id, userLoginCtrl_id, login_name, nick_name, real_name, email, pass_word, cell_phone_num, tele_phone_num, active, deleted, description, create_time, login_time) VALUES
  ('25190948786929400','25190948786929300', 'super', '超级管理员', '', 'super@163.com', '$2a$10$TJEhN.0MANHFGPd5ux6oB.3ffkiRzGiMkyYY/iGAq39qp5xpoKn8G', '13000000000', '8000000', 1, 0, '系统内置用户', now(), NULL),
  ('25190948786929401','25190948786929301', 'admin', '管理员', '', 'admin@163.com', '$2a$10$5T9W/kJK9G00KxanX3XCruxDXzrtesV3Hh0ClXvf9IgzqccNKKo42', '13000000001', '8000001', 1, 0, '系统内置用户', now(), NULL)
ON DUPLICATE KEY UPDATE login_name = VALUES(login_name);


INSERT INTO user_user_role(user_id, role_id) VALUES
  ('25190948786929400', '25190948786929202'),
  ('25190948786929401', '25190948786929200')
ON DUPLICATE KEY UPDATE role_id=  VALUES(role_id);