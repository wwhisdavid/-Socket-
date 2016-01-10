package com.wwhisdavid.service;

import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.util.PageBean;

public interface NodeDetailService {
	public void getAll(PageBean<?> pb, QueryNodeDetailEntity queryNodeDetailEntity);
}
