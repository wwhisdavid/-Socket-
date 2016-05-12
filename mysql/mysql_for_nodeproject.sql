show tables;
use DataCollectWebAPP;
create table user(id int primary key AUTO_INCREMENT,
username varchar(20),
password varchar(20)
) DEFAULT CHARSET=utf8;
drop table user;
use DataCollectWebApp;
insert into user(username,password) values("王",2345);
SELECT * FROM DataCollectWebApp.user;
create table a_node(node_id int primary key,
name varchar(20),
logitude float,
latitude float
);
desc a_node;
drop table a_node_detail;
select * from a_node limit 0,10;
create table project_list(project_id int primary key AUTO_INCREMENT,
name varchar(20),
decription text,
child_table varchar(20)
);
select FROM_UNIXTIME(1452179297);
SELECT UNIX_TIMESTAMP();

create table a_node_detail(
node_id int,
record_time int,
temperature float,
humidity float, -- 湿度
stress_x float,
stress_y float,
stress_z float,
primary key(node_id,record_time),
CONSTRAINT anode_nodedetil_fk FOREIGN KEY(node_id) REFERENCES a_node(node_id) 
);
show global variables like 'wait_timeout';
set global wait_timeout=100;
select VARIABLE_VALUE from information_schema.GLOBAL_VARIABLES where VARIABLE_NAME='MAX_CONNECTIONS'; 
set global max_connections=5000;
-- 16:36:25	create table a_node_detail( node_id int, record_time long, temperature float, humidity float, -- 湿度 stress_x float, stress_y float, stress_z float, primary key(node_id,record_time), CONSTRAINT anode_nodedetil_fk FOREIGN KEY(node_id) REFERENCES a_node(node_id)  )	Error Code: 1170. BLOB/TEXT column 'record_time' used in key specification without a key length	0.00055 sec
create table node_command(
node_id int,
command_time int,
username varchar(20),
command int, -- -1:stop 1:1h 2:1d 3:1w
primary key(node_id,command_time,username),
CONSTRAINT node_command_fk FOREIGN KEY(node_id) REFERENCES a_node(node_id) 
); -- 00:43:12	create table node_command( node_id int, command_time int, username varchar(20), command int, -- -1:stop 1:1h 2:1d 3:1w primary key(node_id,command_time,username), CONSTRAINT anode_nodedetil_fk FOREIGN KEY(node_id) REFERENCES a_node(node_id)  )	Error Code: 1046. No database selected Select the default DB to be used by double-clicking its name in the SCHEMAS list in the sidebar.	0.068 sec
