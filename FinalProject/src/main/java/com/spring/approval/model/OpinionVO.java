package com.spring.approval.model;

public class OpinionVO {
	
	private String opno;          	// 결재의견번호
	private String fk_apno; 		// 결재문서번호
	private String fk_employeeid; 	// 사원번호(의견작성자)
	private String opdate;     		// 결재의견작성일 
	private String opinion;       	// 글내용
	private String apstatus;       	// 결재 시 1 반려 시 2
	
	private String apprEmp;       	// 다음 결재자가 있는지 확인용
	
	public OpinionVO() {}
	
	public OpinionVO(String opno, String fk_apno, String fk_employeeid, String opdate, String opinion,
			String apstatus) {
		super();
		this.opno = opno;
		this.fk_apno = fk_apno;
		this.fk_employeeid = fk_employeeid;
		this.opdate = opdate;
		this.opinion = opinion;
		this.apstatus = apstatus;
	}

	public String getOpno() {
		return opno;
	}

	public void setOpno(String opno) {
		this.opno = opno;
	}

	public String getFk_apno() {
		return fk_apno;
	}

	public void setFk_apno(String fk_apno) {
		this.fk_apno = fk_apno;
	}

	public String getFk_employeeid() {
		return fk_employeeid;
	}

	public void setFk_employeeid(String fk_employeeid) {
		this.fk_employeeid = fk_employeeid;
	}

	public String getOpdate() {
		return opdate;
	}

	public void setOpdate(String opdate) {
		this.opdate = opdate;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getApstatus() {
		return apstatus;
	}

	public void setApstatus(String apstatus) {
		this.apstatus = apstatus;
	}

	public String getApprEmp() {
		return apprEmp;
	}

	public void setApprEmp(String apprEmp) {
		this.apprEmp = apprEmp;
	}
	
	
}
