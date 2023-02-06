package com.spring.finalProject.model;

public class ScheduleVO_NSW {
	
	private String fk_employeeID;  // 사원번호
	private String subject;		   // 일정 제목
	private String startDate;	   // 시작 날짜
	private String endDate;		   // 종료 날짜
	private String memo;		   // 일정 메모
	private String seq;			   // 
	private String fk_departNo;    // 부서번호
	

	public ScheduleVO_NSW() {}
	
	public String getSeq() {
		return seq;
	}


	public void setSeq(String seq) {
		this.seq = seq;
	}


	public String getFk_departNo() {
		return fk_departNo;
	}


	public void setFk_departNo(String fk_departNo) {
		this.fk_departNo = fk_departNo;
	}

	public String getFk_employeeID() {
		return fk_employeeID;
	}
	
	public void setFk_employeeID(String fk_employeeID) {
		this.fk_employeeID = fk_employeeID;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getMemo() {
		return memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}	