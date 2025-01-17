package com.wwhisdavid.entity;

import java.util.ArrayList;

public class QueryNodeDetailEntity {
	private String node_id;
	private long fromTime;
	private long toTime;
	private ArrayList<String> params;
	private String node_name;
	
	public ArrayList<String> getParams() {
		return params;
	}
	public void setParams(ArrayList<String> params) {
		this.params = params;
	}
	public String getNode_id() {
		return node_id;
	}
	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}
	public long getFromTime() {
		return fromTime;
	}
	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}
	public long getToTime() {
		return toTime;
	}
	public void setToTime(long toTime) {
		this.toTime = toTime;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
}

