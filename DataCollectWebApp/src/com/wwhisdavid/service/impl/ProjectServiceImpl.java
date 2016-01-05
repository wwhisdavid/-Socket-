package com.wwhisdavid.service.impl;

import com.wwhisdavid.dao.ProjectListDao;
import com.wwhisdavid.dao.impl.ProjectListDaoImpl;
import com.wwhisdavid.entity.ProjectListEntity;
import com.wwhisdavid.service.ProjectService;
import com.wwhisdavid.util.PageBean;

public class ProjectServiceImpl implements ProjectService {
	ProjectListDao project = new ProjectListDaoImpl();
	
	@Override
	public void getAll(PageBean<ProjectListEntity> pb) {
		try {
			project.getAll(pb);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
