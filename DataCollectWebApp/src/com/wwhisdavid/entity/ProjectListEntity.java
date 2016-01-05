package com.wwhisdavid.entity;

public class ProjectListEntity {
	private int project_id;
	private String name;
	private String description;
	private String chile_table;
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
	public String getChile_table() {
		return chile_table;
	}
	public void setChile_table(String chile_table) {
		this.chile_table = chile_table;
	}
}
