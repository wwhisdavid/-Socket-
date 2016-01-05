package com.wwhisdavid.dao;

import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.util.PageBean;

public interface ANodeDao {
	/*
	 * 获得本页数据
	 */
	public void getAll(PageBean<ANodeEntity> pb);
	
	/*
	 * 查询总记录
	 */
	public int getTotalCount();
}
