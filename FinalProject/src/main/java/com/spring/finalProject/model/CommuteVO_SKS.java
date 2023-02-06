package com.spring.finalProject.model;

public class CommuteVO_SKS {
	private String seq;
	private String name;
	private String commute_day;
	private String comute_time;
	private String commute_status;
	private String commute_gtw;
	private String commute_ok;

	public CommuteVO_SKS() {}
	
	public CommuteVO_SKS(String seq, String name, String commute_day, String comute_time, String commute_status
			,String commute_gtw,  String commute_ok) {
		this.seq = seq;
		this.name = name;
		this.commute_day = commute_day;
		this.comute_time = comute_time;
		this.commute_status = commute_status;
		this.commute_gtw = commute_gtw;
		this.commute_ok = commute_ok;
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

	public String getCommute_day() {
		return commute_day;
	}

	public void setCommute_day(String commute_day) {
		this.commute_day = commute_day;
	}

	public String getComute_time() {
		return comute_time;
	}

	public void setComute_time(String comute_time) {
		this.comute_time = comute_time;
	}

	public String getCommute_status() {
		return commute_status;
	}

	public void setCommute_status(String commute_status) {
		this.commute_status = commute_status;
	}

	public String getCommute_gtw() {
		return commute_gtw;
	}

	public void setCommute_gtw(String commute_gtw) {
		this.commute_gtw = commute_gtw;
	}

	public String getCommute_ok() {
		return commute_ok;
	}

	public void setCommute_ok(String commute_ok) {
		this.commute_ok = commute_ok;
	}
	
	
}	