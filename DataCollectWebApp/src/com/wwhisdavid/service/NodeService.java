package com.wwhisdavid.service;

import com.wwhisdavid.util.PageBean;

public interface NodeService {
	public void getAll(PageBean<?> pb, String table);
}
