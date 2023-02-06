package com.spring.finalProject.model;

public class DepartmentVO_KGH {
	
	private String departno;		// 부서번호
	private String departmentname;	// 부서명
	private String managerid;		// 부서담당자 사원번호
	
	DepartmentVO_KGH() {}	// 기본 생성자

	public DepartmentVO_KGH(String departno, String departmentname, String managerid) {
		this.departno = departno;
		this.departmentname = departmentname;
		this.managerid = managerid;
	}

	
	public String getDepartno() {
		return departno;
	}

	public void setDepartno(String departno) {
		this.departno = departno;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}
	
	
	
}
