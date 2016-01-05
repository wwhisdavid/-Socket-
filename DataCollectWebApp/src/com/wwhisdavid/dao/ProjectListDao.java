package com.wwhisdavid.dao;

import com.wwhisdavid.entity.ProjectListEntity;
import com.wwhisdavid.util.PageBean;

public interface ProjectListDao {
	/*
	 * 获得本页数据
	 */
	public void getAll(PageBean<ProjectListEntity> pb);
	
	/*
	 * 查询总记录
	 */
	public int getTotalCount();
}
