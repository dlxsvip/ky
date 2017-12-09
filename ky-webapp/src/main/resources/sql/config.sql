
INSERT INTO config(config_id, config_name, config_key, config_value, value_type, description) VALUES
  ('25190948786910101', '推送缓存最大条数', 'live.broadcast.result', 100, 'int', '推送缓存最大条数'),
  ('25190948786910102', '超过推送最大条数后清理超时', 'live.broadcast.result.timeout', 1, 'float', '超过推送最大条数后清理超时的 ：单位小时'),
  ('25190948786910103', '密码有效期', 'password.valid.days', 90, 'int', '密码有效期 ：单位天'),
  ('25190948786910104', '初始密码长度', 'password.init.length', 8, 'int', '初始密码长度'),
  ('25190948786910105', '登录超时时间', 'login.session.timeout', 600, 'int', '登录超时时间 ：单位秒'),
  ('25190948786910106', '允许登录失败次数', 'login.fail.times', 3, 'int', '允许登录失败次数'),
  ('25190948786910107', '密码错误后禁闭时间', 'login.forbid.time', 30, 'int', '密码错误后禁闭时间 ：单位分钟'),
  ('25190948786910108', '删除本地 N天前的历史告警数据', 'delete.alarmHistoryData.day', 3, 'int', '删除本地 N天前的历史告警数据 ：单位天'),
  ('25190948786910109', '删除OSS上 N天前的视频', 'delete.OssVideo.day', 7, 'int', '删除OSS上 N天前的视频 ：单位天'),
  ('25190948786910110', '验证密码的密钥', 'aes.key', '0123456789abcdef', 'string', '验证密码的密钥，必须16位')
ON DUPLICATE KEY UPDATE config_id= VALUES(config_id);

