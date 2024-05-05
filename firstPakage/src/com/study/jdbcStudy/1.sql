CREATE TABLE actor (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       sex VARCHAR(10),
                       score FLOAT,
                       birthday DATE
);


INSERT INTO actor (name, sex, score, birthday)
VALUES ('张三', '男', 8.5, '1990-05-15');

