use loginDB;


drop table loginDB;

create table loginDB (
                           userId INT UNSIGNED AUTO_INCREMENT,
                           username VARCHAR(32),
                           password VARCHAR(512),
                           primary key (userId)
) charset=utf8;

