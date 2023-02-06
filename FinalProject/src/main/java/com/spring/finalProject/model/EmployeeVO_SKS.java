package com.spring.finalProject.model;

import java.util.Calendar;

public class EmployeeVO_SKS {

	private String employeeid;             // 회원아이디
	private String fk_departno;                // 비밀번호 (SHA-256 암호화 대상)
	private String fk_positionno;               // 회원명
	private String name;              // 이메일 (AES-256 암호화/복호화 대상)
	private String password;             // 연락처 (AES-256 암호화/복호화 대상) 
	private String mobile;           // 우편번호
	private String email;            // 주소
	private String hiredate;      // 상세주소
	private String retiredate;       // 참고항목
	private String retire;             // 성별   남자:1  / 여자:2
	private String profilename;           // 생년월일   
	private String orgprofilename;                  // 코인액
	private String filesize;                 // 포인트 
	private String salary;        // 가입일자 
	private String dayoff;  // 마지막으로 암호를 변경한 날짜  
	private String admin;                // 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
	private String address;                  // 휴면유무         0: 활동중  /  1 : 휴면중 
	private String detailaddress;
	private String extraaddress;
	private String postcode;
	
	public EmployeeVO_SKS() {}
	
	public EmployeeVO_SKS(String employeeid, String fk_departno, String fk_positionno, String name, 
			String password, String mobile, String email, String hiredate, String retiredate, 
			String retire, String profilename, String orgprofilename, String filesize, 
			String salary, String dayoff, String admin, String address, String detailaddress, 
			String extraaddress, String postcode) {
		
		this.employeeid = employeeid;
		this.fk_departno = fk_departno;
		this.fk_positionno = fk_positionno;
		this.name = name;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.hiredate = hiredate;
		this.retiredate = retiredate;
		this.retire = retire;
		this.profilename = profilename;
		this.orgprofilename = orgprofilename;
		this.filesize = filesize;
		this.salary = salary;
		this.dayoff = dayoff;
		this.admin = admin;
		this.address = address;
		this.detailaddress = detailaddress;
		this.extraaddress = extraaddress;
		this.postcode = postcode;
		
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFk_departno() {
		return fk_departno;
	}

	public void setFk_departno(String fk_departno) {
		this.fk_departno = fk_departno;
	}

	public String getFk_positionno() {
		return fk_positionno;
	}

	public void setFk_positionno(String fk_positionno) {
		this.fk_positionno = fk_positionno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHiredate() {
		return hiredate;
	}

	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}

	public String getRetiredate() {
		return retiredate;
	}

	public void setRetiredate(String retiredate) {
		this.retiredate = retiredate;
	}

	public String getRetire() {
		return retire;
	}

	public void setRetire(String retire) {
		this.retire = retire;
	}

	public String getProfilename() {
		return profilename;
	}

	public void setProfilename(String profilename) {
		this.profilename = profilename;
	}

	public String getOrgprofilename() {
		return orgprofilename;
	}

	public void setOrgprofilename(String orgprofilename) {
		this.orgprofilename = orgprofilename;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDayoff() {
		return dayoff;
	}

	public void setDayoff(String dayoff) {
		this.dayoff = dayoff;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public String getExtraaddress() {
		return extraaddress;
	}

	public void setExtraaddress(String extraaddress) {
		this.extraaddress = extraaddress;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	

	
	
	
	
}
