DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
                        author_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        created_at DATETIME,
                        updated_at DATETIME
);

CREATE TABLE board (
                       board_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       content TEXT NOT NULL,
                       author_id BIGINT,
                       created_at DATETIME,
                       updated_at DATETIME,
                       CONSTRAINT fk_board_author
                           FOREIGN KEY (author_id) REFERENCES author(author_id)
);
