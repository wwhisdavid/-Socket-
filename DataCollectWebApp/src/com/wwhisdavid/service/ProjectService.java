package com.wwhisdavid.service;

import com.wwhisdavid.entity.ProjectListEntity;
import com.wwhisdavid.util.PageBean;

public interface ProjectService {
	public void getAll(PageBean<ProjectListEntity> pb);
}
