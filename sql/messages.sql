CREATE TABLE messages (
    id VARCHAR(20) PRIMARY KEY,
    content VARCHAR(2000),
    userId VARCHAR(20) NOT NULL,
    userTag VARCHAR(37)
);
