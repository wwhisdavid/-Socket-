package com.wwhisdavid.dao.impl;

import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.wwhisdavid.dao.ANodeDao;
import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.util.JdbcUtil;
import com.wwhisdavid.util.PageBean;

public class ANodeDaoImpl implements ANodeDao{

	@Override
	public void getAll(PageBean<ANodeEntity> pb) {
		int totalCount = this.getTotalCount();
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
		
		String sql = "select * from a_node limit ?,?";
		try {
			QueryRunner qr = JdbcUtil.getRunner();
			List<ANodeEntity> list = qr.query(sql, new BeanListHandler<ANodeEntity>(ANodeEntity.class), index, count);
			pb.setPageData(list);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getTotalCount() {
		String sql = "select count(*) from a_node";
		try {
			QueryRunner queryRunner = JdbcUtil.getRunner();
			Long count = queryRunner.query(sql, new ScalarHandler<Long>());
			return count.intValue();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
