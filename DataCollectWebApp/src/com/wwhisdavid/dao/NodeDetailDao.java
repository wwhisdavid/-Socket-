package com.wwhisdavid.dao;

import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.util.PageBean;

public interface NodeDetailDao {

	public void getAll(PageBean pb, QueryNodeDetailEntity queryNodeDetailEntity);
	
	public int getTotalCount(QueryNodeDetailEntity queryNodeDetailEntity);
}
