CREATE TABLE `user` (
    id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(30),
    age INT,
    email VARCHAR(50),
    status TINYINT NOT NULL DEFAULT 1
);
