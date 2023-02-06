package com.spring.finalProject.model;

import org.springframework.web.multipart.MultipartFile;

public class EmployeeVO_KGH {
	
	private String employeeid;		// 사원번호
	private String fk_departNo;		// 부서번호(foreign key)
	private String fk_positionNo;	// 직급번호(foreign key)
	private String name;			// 사원명
	private String password;		// 비밀번호
	private String mobile;			// 연락처
	private String email;			// 이메일
	private String hiredate;		// 입사일자
	private String retiredate;		// 퇴사일자
	private String retire;			// 퇴사여부(0: 재직중 / 1: 퇴사)
	private String profilename;		// WAS(톰캣)에 저장될 파일명(2021110809271535243254235235234.png)
	private String orgProfilename;	// 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
	private String fileSize;		// 파일크기
	private String salary;			// 급여
	private String dayoff;			// 연차 개수
	private String admin;			// 관리자 권한 여부(0: 관리자권한 X, 1: 관리자권한 O)
	private String postcode;		// 우편번호
	private String address;			// 주소
	private String detailAddress;	// 상세주소
	private String extraAddress;	// 추가주소
	
	private MultipartFile attach;
    /* form 태그에서 type="file" 인 파일을 받아서 저장되는 필드이다. 
              진짜파일 ==> WAS(톰캣) 디스크에 저장됨.
              조심할것은 MultipartFile attach 는 오라클 데이터베이스 tbl_board 테이블의 컬럼이 아니다.   
  	   /Board/src/main/webapp/WEB-INF/views/tiles1/board/add.jsp 파일에서 input type="file" 인 name 의 이름(attach)과 
              동일해야만 파일첨부가 가능해진다.!!!!
    */
	
	EmployeeVO_KGH() {}	// 기본생성자

	public EmployeeVO_KGH(String employeeid, String fk_departNo, String fk_positionNo, String name, String password,
			String mobile, String email, String hiredate, String retiredate, String retire, String profilename,
			String orgProfilename, String fileSize, String salary, String dayoff, String admin, String postcode,
			String address, String detailAddress, String extraAddress) {
		
		this.employeeid = employeeid;
		this.fk_departNo = fk_departNo;
		this.fk_positionNo = fk_positionNo;
		this.name = name;
		this.password = password;
		this.mobile = mobile;
		this.email = email;
		this.hiredate = hiredate;
		this.retiredate = retiredate;
		this.retire = retire;
		this.profilename = profilename;
		this.orgProfilename = orgProfilename;
		this.fileSize = fileSize;
		this.salary = salary;
		this.dayoff = dayoff;
		this.admin = admin;
		this.postcode = postcode;
		this.address = address;
		this.detailAddress = detailAddress;
		this.extraAddress = extraAddress;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFk_departNo() {
		return fk_departNo;
	}

	public void setFk_departNo(String fk_departNo) {
		this.fk_departNo = fk_departNo;
	}

	public String getFk_positionNo() {
		return fk_positionNo;
	}

	public void setFk_positionNo(String fk_positionNo) {
		this.fk_positionNo = fk_positionNo;
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

	public String getOrgProfilename() {
		return orgProfilename;
	}

	public void setOrgProfilename(String orgProfilename) {
		this.orgProfilename = orgProfilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
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

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getExtraAddress() {
		return extraAddress;
	}

	public void setExtraAddress(String extraAddress) {
		this.extraAddress = extraAddress;
	}
	
	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}
}
