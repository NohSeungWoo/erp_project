package com.spring.approval.model;

import org.springframework.web.multipart.MultipartFile;

public class ApprovalVO {
	
	private String apno;          // 결재문서번호
	private String fk_employeeid; // 사원번호(기안자)
	private String fk_apcano;     // 결재카테고리 
	private String subject;       // 글제목
	private String content;       // 글내용   -- clob (최대 4GB까지 허용) 
	                             
	private String apprEmp;       // 결재자
	private String coopEmp;       // 협조자
	private String reciEmp;       // 수신자
	                             
	private String apdate;        // 기안일
	private String eddate;        // 결재일
	                             
	private String apstatus;      // 결재상태 상신 - 0
	
	private MultipartFile attach;
	private String fileName;      // WAS(톰캣)에 저장될 파일명(2021110809271535243254235235234.png)                                       
	private String orgFilename;   // 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명 
	private String fileSize;      // 파일크기
	
	public ApprovalVO() {}
	
	public ApprovalVO(String apno, String fk_employeeid, String fk_apcano, String subject, String content,
			String apprEmp, String coopEmp, String reciEmp, String apdate, String eddate, String apstatus,
			String fileName, String orgFilename, String fileSize) {
		super();
		this.apno = apno;
		this.fk_employeeid = fk_employeeid;
		this.fk_apcano = fk_apcano;
		this.subject = subject;
		this.content = content;
		this.apprEmp = apprEmp;
		this.coopEmp = coopEmp;
		this.reciEmp = reciEmp;
		this.apdate = apdate;
		this.eddate = eddate;
		this.apstatus = apstatus;
		this.fileName = fileName;
		this.orgFilename = orgFilename;
		this.fileSize = fileSize;
	}

	public String getApno() {
		return apno;
	}

	public void setApno(String apno) {
		this.apno = apno;
	}

	public String getFk_employeeid() {
		return fk_employeeid;
	}

	public void setFk_employeeid(String fk_employeeid) {
		this.fk_employeeid = fk_employeeid;
	}

	public String getFk_apcano() {
		return fk_apcano;
	}

	public void setFk_apcano(String fk_apcano) {
		this.fk_apcano = fk_apcano;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getApprEmp() {
		return apprEmp;
	}

	public void setApprEmp(String apprEmp) {
		this.apprEmp = apprEmp;
	}

	public String getCoopEmp() {
		return coopEmp;
	}

	public void setCoopEmp(String coopEmp) {
		this.coopEmp = coopEmp;
	}

	public String getReciEmp() {
		return reciEmp;
	}

	public void setReciEmp(String reciEmp) {
		this.reciEmp = reciEmp;
	}

	public String getApdate() {
		return apdate;
	}

	public void setApdate(String apdate) {
		this.apdate = apdate;
	}

	public String getEddate() {
		return eddate;
	}

	public void setEddate(String eddate) {
		this.eddate = eddate;
	}

	public String getApstatus() {
		return apstatus;
	}

	public void setApstatus(String apstatus) {
		this.apstatus = apstatus;
	}

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFilename() {
		return orgFilename;
	}

	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
}
