package com.wwhisdavid.entity;

public class ANodeDetailEntity {
	private int node_id;
	private int record_time;
	private float temperature;
	private float humidity;
	private float stress_x;
	private float stress_y;
	private float stress_z;
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public int getRecord_time() {
		return record_time;
	}
	public void setRecord_time(int record_time) {
		this.record_time = record_time;
	}
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	public float getStress_x() {
		return stress_x;
	}
	public void setStress_x(float stress_x) {
		this.stress_x = stress_x;
	}
	public float getStress_y() {
		return stress_y;
	}
	public void setStress_y(float stress_y) {
		this.stress_y = stress_y;
	}
	public float getStress_z() {
		return stress_z;
	}
	public void setStress_z(float stress_z) {
		this.stress_z = stress_z;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+node_id+record_time+temperature+humidity+stress_x+stress_y+stress_z;
	}
}
