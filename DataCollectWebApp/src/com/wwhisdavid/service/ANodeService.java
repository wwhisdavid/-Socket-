package com.wwhisdavid.service;

import com.wwhisdavid.entity.ANodeEntity;
import com.wwhisdavid.util.PageBean;

public interface ANodeService {
	public void getAll(PageBean<ANodeEntity> pb);
}
