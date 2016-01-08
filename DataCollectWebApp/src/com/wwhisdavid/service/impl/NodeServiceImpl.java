package com.wwhisdavid.service.impl;

import com.wwhisdavid.service.ANodeService;
import com.wwhisdavid.service.NodeService;
import com.wwhisdavid.util.PageBean;

public class NodeServiceImpl implements NodeService {
	private ANodeService a_service = new ANodeServiceImpl();
	@Override
	public void getAll(PageBean pb, String table) {
		if ("a_node".equals(table)) {
			a_service.getAll(pb);
		}
	}
}
