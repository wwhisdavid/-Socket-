SELECT * FROM DataCollectWebApp.a_node_detail;
delete from DataCollectWebApp.a_node_detail where node_id=1;
select count(*) from DataCollectWebApp.a_node_detail;
set GLOBAL max_connections=10000;
show variables like '%max_connections%';
show global status like 'Max_used_connections';