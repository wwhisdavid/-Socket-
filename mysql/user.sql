use DataCollectWebApp;
desc user;
show variables like 'char%';
set character_set_server=utf8;
show create database DataCollectWebApp;
alter database DataCollectWebApp default character set utf8;
delete from user;

SELECT * FROM DataCollectWebApp.user;
delete from DataCollectWebApp.user;
show processlist;
use DataCollectWebApp;
