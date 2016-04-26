package com.wwhisdavid.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.wwhisdavid.dao.NodeDetailDao;
import com.wwhisdavid.entity.ANodeDetailEntity;
import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.util.JdbcUtil;
import com.wwhisdavid.util.PageBean;

public class ANodeDetailDaoImpl implements NodeDetailDao {

	@Override
	public void getAll(PageBean pb, QueryNodeDetailEntity queryNodeDetailEntity) {
		// test
		if (queryNodeDetailEntity.getFromTime() == 0 || queryNodeDetailEntity.getToTime() == 0) {
			queryNodeDetailEntity.setFromTime(1);
			queryNodeDetailEntity.setToTime(2000000000);
		}
		
		
		int totalCount = this.getTotalCount(queryNodeDetailEntity);
		pb.setTotalCount(totalCount);
		
		// 判断
		if (pb.getCurrentPage() <=0) {
			pb.setCurrentPage(1);					    // 把当前页设置为1
		} else if (pb.getCurrentPage() > pb.getTotalPage() && pb.getTotalPage() != 0){
			pb.setCurrentPage(pb.getTotalPage());		// 把当前页设置为最大页数
		}

		int currentPage = pb.getCurrentPage();
		int index = (currentPage - 1) * pb.getPageCount();
		int count = pb.getPageCount();
		
		String sql = "select * from a_node_detail d left outer join a_node a on a.node_id=d.node_id "
				+ "where d.node_id=? and record_time > ? and record_time < ?";
		QueryRunner queryRunner = JdbcUtil.getRunner();
		try {
			if (pb.getTotalData() == null) {
				System.out.println("查询数据库数据");
				List<ANodeDetailEntity> list = queryRunner.query(
						sql, 
						new BeanListHandler<ANodeDetailEntity>(ANodeDetailEntity.class) ,
						queryNodeDetailEntity.getNode_id(), 
						queryNodeDetailEntity.getFromTime(), 
						queryNodeDetailEntity.getToTime()
						);
				pb.setTotalData(list); // 所有数据
			}
			
			List<ANodeDetailEntity> pageList = new ArrayList<ANodeDetailEntity>();
			ANodeDetailEntity entity = null;
			int remaindedCount = pb.getTotalData().size() - index;
			count = remaindedCount >= count ? index + count : totalCount; 
			for (int i = index; i < count; i++) {
				entity = (ANodeDetailEntity) pb.getTotalData().get(i);
				pageList.add(entity);
			}
			pb.setPageData(pageList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int getTotalCount(QueryNodeDetailEntity queryNodeDetailEntity) {
		String sql = "select count(*) from a_node_detail d left outer join a_node a on a.node_id=d.node_id "
				+ "where d.node_id=? and record_time > ? and record_time < ?";
		QueryRunner queryRunner = JdbcUtil.getRunner();
		try {
			Long count = queryRunner.query(
					sql, 
					new ScalarHandler<Long>(), 
					queryNodeDetailEntity.getNode_id(), 
					queryNodeDetailEntity.getFromTime(), 
					queryNodeDetailEntity.getToTime()
					);
			return count.intValue();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
