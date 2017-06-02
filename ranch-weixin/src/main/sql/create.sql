DROP TABLE IF EXISTS t_weixin;
CREATE TABLE t_weixin
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '引用key',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_app_id VARCHAR(255) DEFAULT NULL COMMENT 'APP ID',
  c_secret VARCHAR(255) DEFAULT NULL COMMENT '密钥',
  c_token VARCHAR(255) DEFAULT NULL COMMENT '验证Token',
  c_mch_id VARCHAR(255) DEFAULT NULL COMMENT '商户ID',
  c_mch_key VARCHAR(255) DEFAULT NULL COMMENT '商户密钥',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_key(c_key) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;