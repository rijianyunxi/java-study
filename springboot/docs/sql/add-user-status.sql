-- 一次性手动执行。不要删除 user 表，也不要重新导入演示数据。
-- 执行前检查：SHOW COLUMNS FROM study.`user` LIKE 'status';
-- 没有 status 列时才执行下面的 ALTER TABLE。
ALTER TABLE study.`user`
    ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '用户状态：1启用，0禁用';
-- 现有记录自动获得默认状态 1。
