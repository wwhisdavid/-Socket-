package com.wwhisdavid.service.impl;

import com.wwhisdavid.dao.NodeDetailDao;
import com.wwhisdavid.dao.impl.ANodeDetailDaoImpl;
import com.wwhisdavid.entity.QueryNodeDetailEntity;
import com.wwhisdavid.service.NodeDetailService;
import com.wwhisdavid.util.PageBean;

public class NodeDetailServiceImpl implements NodeDetailService {
	private NodeDetailDao dao = new ANodeDetailDaoImpl(); 
	@Override
	public void getAll(PageBean<?> pb, QueryNodeDetailEntity queryNodeDetailEntity) {
		try {
			dao.getAll(pb, queryNodeDetailEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
