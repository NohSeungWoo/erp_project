package com.spring.finalProject.model;

import java.util.List;
import java.util.Map;

public interface InterKGHDAO {
	
	// 직원 목록 가져오기(select) 메서드
	List<Map<String, String>> getEmpList();

	// 총 게시물 건수(totalCount) 가져오기(select)
	int getTotalCount(Map<String, String> paraMap);

	// 페이징 처리한 직원 목록 가져오기(검색이 있든지, 검색이 없든지 다 포함된 것) //
	List<Map<String, String>> getEmpListWithPaging(Map<String, String> paraMap);

	// 부서목록 가져오기(select)
	List<DepartmentVO_KGH> getDepartmentName();

	// 직급 목록 가져오기(select)
	List<PositionVO_KGH> getPosition();

	// 검색어 결과 조회하기(select)
	List<String> employeeSearch(Map<String, String> paraMap);

	// 로그인 처리 메서드(select)
	EmployeeVO_KGH getLogin(Map<String, String> paraMap);

	// 이메일 찾기 완료 메서드(select)
	String emailFindEnd(Map<String, String> paraMap);
	
	// 이메일 중복여부 검사하기(select)
	boolean emailDuplicateCheck(String email);

	// 새로 생성될 사원번호 조회하기(select)
	String selectEmpId(String departmentno);

	// 직원 정보 등록하기(insert)
	int empRegister(EmployeeVO_KGH emp);

	// 첨부파일과 함께 직원 정보 등록하기(insert)
	int empRegisterWithProfile(EmployeeVO_KGH emp);

	// 기존 비밀번호와 같은지 체크 메서드(select)
	boolean passwordCheck(Map<String, String> paraMap);
	
	// 해당하는 사원의 파일이 존재하는 경우 해당하는 파일명 가져오기(select)
	String getprofileName(String employeeid);
	
	// 해당하는 사원의 정보 update해주기
	int mypageEnd(EmployeeVO_KGH empvo);
		
	// 파일이 없는 사원의 정보 update 해주기
	int mypageEndNoFile(EmployeeVO_KGH empvo);
	
	// 특정 회원에 대한 정보 가져오기(select)
	Map<String, String> empListEdit(String employeeID);

	// 먼저 기존에 있던 부서의 팀장 사원번호 가져오기(select)
	String getManagerId(Map<String, String> paraMap);

	// 기존의 팀장에 대한 사원 테이블 update
	int updateEmployee(String managerId);

	// 새로운 팀장에 대한 부서 테이블 update
	int updateDepartManager(Map<String, String> paraMap);
	
	// 직원 정보 수정하기(update)
	int empEdit(EmployeeVO_KGH emp);

	// 부서별 인원 가져오기(select)
	List<String> getDepartempCnt();

	// 엑셀에 입력할 직원 정보 가져오기
	List<Map<String, String>> excelEmpList(Map<String, String> paraMap);

	// 직원수 가져오기 메서드
	int getEmpCnt();

	// 부서 추가 시 이미 존재하는 부서인지 중복확인 하는 메서드
	boolean departDuplicate(String newDepart);

	// 해당하는 사원의 번호 존재여부 확인하는 메서드
	boolean isExistsEmpID(String employeeid);

	// 부서 새로 추가하기 메서드
	int newDepartAddEnd(Map<String, String> paraMap);

	// 해당하는 부서의 부서번호 select 하기
	String getdepartno(Map<String, String> paraMap);

	// 해당하는 사번의 직책 update하기
	int updateManager(Map<String, String> paraMap);

	// 부서 삭제 및 삭제 부서에 대한 사원정보 변경(update)
	int delDepartEmpUpdate(String departno);

	// 해당하는 부서에 대한 사원 정보 변경이 성공한 경우 해당 부서 삭제
	int delDepart(String departno);

	// 부서명 수정하기 메서드
	int departEditEnd(Map<String, String> paraMap);

	// 체크박스에 체크된 사원에 대한 부서변경(update)
	int changeDepartment(Map<String, Object> paraMap);

	// 관리자 List 가져오기(select)
	List<Map<String, String>> getAdminList(Map<String, String> paraMap);

	// 관리자수 가져오기 메서드(select)
	int getTotalAdminCount();

	// 관리자 메뉴 검색어 결과 조회하기(select)
	List<EmployeeVO_KGH> adminListSearch(Map<String, String> paraMap);

	// 관리자 추가 메서드(update)
	int adminAddEnd(String employeeid);

	// 관리자 권한 삭제 메서드(update)
	int adminDelEnd(String employeeid);

	// 삭제하고자 하는 직원의 정보가 팀장일 경우 부서 테이블 managerid null처리 (update) 
	void delManagerId(Map<String, String> paraMap);

	// 삭제하고자 하는 직원의 정보 update(admin, retire, retiredate)
	int empDelEnd(Map<String, String> paraMap);

	// 해당하는 이메일과 이름에 존재하는 사원 정보 찾기(select)
	boolean sendCodeEmail(Map<String, String> paraMap);

	// 새비밀번호 업데이트 메서드(update) 
	int newPasswordUpdate(Map<String, String> paraMap);

	// 조직도 리스트 가져오기(select)
	List<Map<String, String>> getOrganization();
	

	

	

	

	

	

	

}
