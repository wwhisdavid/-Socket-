SELECT * FROM DataCollectWebApp.a_node_detail;
select *
from a_node_detail d
left outer join a_node a
on a.node_id=d.node_id 
where record_time > 1 and record_time < 14444444444444; -- 联合查询
desc a_node_detail;

delete from a_node_detail where node_id=2;
select count(*) from a_node_detail;
show processlist;
INSERT INTO a_node_detail(node_id,record_time,temperature,humidity,stress_x,stress_y,stress_z) VALUES (2,1444444444,23.5,22,1,2,3);
alter table a_node_detail change column record_time record_time long;