package com.wwhisdavid.entity;

/*
 * 快变节点实体 javabean
 */
public class ANodeEntity extends NodeEntity{
	private int node_id;
	private String name;
	private float logitude;
	private float latitude;
	private String child_table;
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getLogitude() {
		return logitude;
	}
	public void setLogitude(float logitude) {
		this.logitude = logitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public String getChild_table() {
		return child_table;
	}
	public void setChild_table(String child_table) {
		this.child_table = child_table;
	}
	
}
