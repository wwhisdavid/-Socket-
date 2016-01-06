package com.wwhisdavid.entity;

public class ProjectListEntity {
	private int project_id;
	private String name;
	private String description;
	private String child_table;
	
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChild_table() {
		return child_table;
	}
	public void setChild_table(String chile_table) {
		this.child_table = chile_table;
	}
}
