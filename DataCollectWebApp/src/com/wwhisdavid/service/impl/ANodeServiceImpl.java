package com.wwhisdavid.service.impl;

import com.sun.corba.se.impl.orbutil.graph.Node;
import com.wwhisdavid.dao.ANodeDao;
import com.wwhisdavid.dao.impl.ANodeDaoImpl;
import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.service.ANodeService;
import com.wwhisdavid.util.PageBean;

public class ANodeServiceImpl implements ANodeService {
	private ANodeDao anode = new ANodeDaoImpl();
	
	@Override
	public void getAll(PageBean<ANodeEntity> pb) {
		try {
			anode.getAll(pb);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
