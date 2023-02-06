package com.spring.finalProject.model;

public class WorkemployeeVO_SKS {
	private String seq;
	private String name;
	private String work_day;
	private String work_time;
	private String todaycount;
	
	public WorkemployeeVO_SKS() {}
	
	public WorkemployeeVO_SKS(String seq, String name, String work_day, String work_time, String todaycount) {
		this.seq = seq;
		this.name = name;
		this.work_day = work_day;
		this.work_time = work_time;
		this.todaycount = todaycount;
	}
	
	
	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWork_day() {
		return work_day;
	}
	public void setWork_day(String work_day) {
		this.work_day = work_day;
	}
	public String getWork_time() {
		return work_time;
	}
	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}
	public String getTodaycount() {
		return todaycount;
	}
	public void setTodaycount(String todaycount) {
		this.todaycount = todaycount;
	}
	
	
	
}
