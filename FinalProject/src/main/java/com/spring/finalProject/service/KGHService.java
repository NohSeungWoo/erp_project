package com.spring.finalProject.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.finalProject.common.AES256;
import com.spring.finalProject.model.DepartmentVO_KGH;
import com.spring.finalProject.model.EmployeeVO_KGH;
import com.spring.finalProject.model.InterKGHDAO;
import com.spring.finalProject.model.PositionVO_KGH;

@Service
public class KGHService implements InterKGHService {

	// === #34. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private InterKGHDAO dao;
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.model.BoardDAO 의 bean 을  dao 에 주입시켜준다. 
    // 그러므로 dao 는 null 이 아니다.
	
	// === #45. 양방향 암호화 알고리즘인 AES256 를 사용하여 복호화 하기 위한 클래스 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private AES256 aes;
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.board.common.AES256 의 bean 을  aes 에 주입시켜준다. 
    // 그러므로 aes 는 null 이 아니다.
    // com.spring.board.common.AES256 의 bean 은 /webapp/WEB-INF/spring/appServlet/servlet-context.xml 파일에서 bean 으로 등록시켜주었음.
	
	@Override
	public List<Map<String, String>> getEmpList() {
		// === 직원 목록 가져오기(select) 메서드 === //
		List<Map<String, String>> empList = dao.getEmpList();
		
//		for (int i = 0; i < empList.size(); i++) {
//			String email = empList.get(i).get("email");
//			String mobile = empList.get(i).get("mobile");
//			
//			Map<String, String> paraMap = new HashMap<>();
//			try {
//				paraMap.put("email", aes.decrypt(email));
//				paraMap.put("mobile", aes.decrypt(mobile));
//			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
//				e.printStackTrace();
//			}
//			
//			empList.add(i, paraMap);
//			System.out.println(paraMap.get("email"));
//			System.out.println(paraMap.get("mobile"));
//			
//			System.out.println(empList.get(i).get("email"));
//			System.out.println(empList.get(i).get("mobile"));
//		}
//		
		return empList;
	}

	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		// === 총 게시물 건수(totalCount) 가져오기(select) === //
		int totalCount = dao.getTotalCount(paraMap);
		return totalCount;
	}

	@Override
	public List<Map<String, String>> getEmpListWithPaging(Map<String, String> paraMap) {
		// === 페이징 처리한 직원 목록 가져오기(검색이 있든지, 검색이 없든지 다 포함된 것) === //
		List<Map<String, String>> empList = dao.getEmpListWithPaging(paraMap);

		return empList;
	}

	@Override
	public List<DepartmentVO_KGH> getDepartmentName() {
		// === 부서목록 가져오기(select) === //
		List<DepartmentVO_KGH> departList = dao.getDepartmentName();
		return departList;
	}

	@Override
	public List<PositionVO_KGH> getPosition() {
		// === 직급 목록 가져오기(select) === //
		List<PositionVO_KGH> positionList = dao.getPosition();
		return positionList;
	}

	@Override
	public List<String> employeeSearch(Map<String, String> paraMap) {
		// === 검색어 결과 조회하기(select) === //
		List<String> searchList = dao.employeeSearch(paraMap);
		return searchList;
	}

	@Override
	public EmployeeVO_KGH getLogin(Map<String, String> paraMap) {
		// === 로그인 처리 메서드(select) === //
		EmployeeVO_KGH empvo = dao.getLogin(paraMap);
		return empvo;
	}

	@Override
	public String emailFindEnd(Map<String, String> paraMap) {
		// === 이메일 찾기 완료 메서드(select) === //
		String email = dao.emailFindEnd(paraMap);
		return email;
	}
	
	@Override
	public boolean sendCodeEmail(Map<String, String> paraMap) {
		// === 해당하는 이메일과 이름에 존재하는 사원 정보 찾기(select) === //
		boolean isExists = dao.sendCodeEmail(paraMap);
		return isExists;
	}

	@Override
	public int newPasswordUpdate(Map<String, String> paraMap) {
		// === 새비밀번호 업데이트 메서드(update) === //
		int n = dao.newPasswordUpdate(paraMap);
		return n;
	}
	
	@Override
	public boolean emailDuplicateCheck(String email) {
		// === 이메일 중복 여부 검사하기(select) === //
		boolean isExists = dao.emailDuplicateCheck(email);
		return isExists;
	}

	@Override
	public String selectEmpId(String departmentno) {
		// === 새로 생성될 사원번호 조회하기(select) === //
		String empid = dao.selectEmpId(departmentno);
		return empid;
	}

	
	@Override
	public int empRegister(EmployeeVO_KGH emp) {
		// === 직원 정보 등록하기(insert) === //
		int n = dao.empRegister(emp);
		return n;
	}

	@Override
	public int empRegisterWithProfile(EmployeeVO_KGH emp) {
		// === 첨부파일과 함께 직원 정보 등록하기(insert) === //
		int n = dao.empRegisterWithProfile(emp);
		return n;
	}

	@Override
	public boolean passwordCheck(Map<String, String> paraMap) {
		// === 기존 비밀번호와 같은지 체크 메서드(select) === //
		boolean isExists = dao.passwordCheck(paraMap);
		return isExists;
	}
	
	@Override
	public String getprofileName(String employeeid) {
		// === 해당하는 사원의 파일이 존재하는 경우 해당하는 파일명 가져오기(select) === //
		String profileName = dao.getprofileName(employeeid);
		return profileName;
	}
	
	@Override
	public int mypageEnd(EmployeeVO_KGH empvo) {
		// === 해당하는 사원의 정보 update해주기 === //
		int n = dao.mypageEnd(empvo);
		return n;
	}
	
	@Override
	public int mypageEndNoFile(EmployeeVO_KGH empvo) {
		// === 파일이 없는 사원의 정보 update 해주기 === //
		int n = dao.mypageEndNoFile(empvo);
		return n;
	}
	
	@Override
	public Map<String, String> empListEdit(String employeeID) {
		// === 특정 회원에 대한 정보 가져오기(select) === //
		Map<String, String> map = dao.empListEdit(employeeID);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class})
	public int updateDepart(Map<String, String> paraMap) {
		// === 직급 변경에 대한 부서 매니저 변경 및 부서장 직급 변경에 대한 트랜잭션 처리 === //
		int n = 0, result = 0;
		
		// === 먼저 기존에 있던 부서의 팀장 사원번호 가져오기(select) === //
		String managerId = dao.getManagerId(paraMap);
		
		if(managerId != null) {
			// === 기존의 팀장에 대한 사원 테이블 update === //
			n = dao.updateEmployee(managerId);
		}
		
		if(n == 1) {
			// === 새로운 팀장에 대한 부서 테이블 update === //
			result = dao.updateDepartManager(paraMap);
		}
		
		return result;
	}
	
	@Override
	public int empEdit(EmployeeVO_KGH emp) {
		// === 직원 정보 수정하기(update) === //
		int n = dao.empEdit(emp);
		return n;
	}

	@Override
	public List<String> getDepartempCnt() {
		// === 부서별 인원 가져오기(select) === //
		List<String> departEmpCnt = dao.getDepartempCnt();
		return departEmpCnt;
	}

	@Override
	public List<Map<String, String>> excelEmpList(Map<String, String> paraMap) {
		// === 엑셀에 입력할 직원 정보 가져오기 === //
		List<Map<String, String>> excelEmpList = dao.excelEmpList(paraMap);
		return excelEmpList;
	}

	@Override
	public int getEmpCnt() {
		// === 직원수 가져오기 메서드 === //
		int empCnt = dao.getEmpCnt();
		return empCnt;
	}

	@Override
	public boolean departDuplicate(String newDepart) {
		// === 부서 추가 시 이미 존재하는 부서인지 중복확인 하는 메서드 === //
		boolean isExists = dao.departDuplicate(newDepart);
		return isExists;
	}

	@Override
	public boolean isExistsEmpID(String employeeid) {
		// === 해당하는 사원의 번호 존재여부 확인하는 메서드 === //
		boolean isExists = dao.isExistsEmpID(employeeid);
		return isExists;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int newDepartAddEnd(Map<String, String> paraMap) {
		int n = 0, result = 0;
		
		// === 부서 새로 추가하기 메서드 === //
		n = dao.newDepartAddEnd(paraMap);
		
		if(n == 1) {
			// === 해당하는 부서번호 select 하기 === //
			String departno = dao.getdepartno(paraMap);
			
			if(departno != "") {
				// === 해당하는 사번의 직책 update하기 === //
				paraMap.put("departno", departno);
				
				result = dao.updateManager(paraMap);
			}
		}
		
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class})
	public int departDelEnd(String departno) {
		int n = 0, result = 0;
		
		// === 부서 삭제 및 삭제 부서에 대한 사원정보 변경(update) === //
		n = dao.delDepartEmpUpdate(departno);
		
		if(n >= 0) {
			// === 해당하는 부서에 대한 사원 정보 변경이 성공한 경우 해당 부서 삭제 === //
			result = dao.delDepart(departno);
		}
		else {
			result = 0;
		}
		
		return result;
	}

	@Override
	public int departEditEnd(Map<String, String> paraMap) {
		// === 부서명 수정하기 메서드 === //
		int n = dao.departEditEnd(paraMap);
		return n;
	}

	@Override
	public int changeDepartment(Map<String, Object> paraMap) {
		// === 체크박스에 체크된 사원에 대한 부서변경(update) === //
		int n = dao.changeDepartment(paraMap);
		return n;
	}

	@Override
	public List<Map<String, String>> getAdminList(Map<String, String> paraMap) {
		// === 관리자 List 가져오기(select) === //
		List<Map<String, String>> adminList = dao.getAdminList(paraMap);
		return adminList;
	}

	@Override
	public int getTotalAdminCount() {
		// === 관리자수 가져오기 메서드(select) === //
		int n = dao.getTotalAdminCount();
		return n;
	}

	@Override
	public List<EmployeeVO_KGH> adminListSearch(Map<String, String> paraMap) {
		// === 관리자 메뉴 검색어 결과 조회하기(select) === //
		List<EmployeeVO_KGH> searchList = dao.adminListSearch(paraMap);
		return searchList;
	}

	@Override
	public int adminAddEnd(String employeeid) {
		// === 관리자 추가 메서드(update) === //
		int n = dao.adminAddEnd(employeeid);
		return n;
	}

	@Override
	public int adminDelEnd(String employeeid) {
		// === 관리자 권한 삭제 메서드(update) === //
		int n = dao.adminDelEnd(employeeid);
		return n;
	}

	@Override
	public void delManagerId(Map<String, String> paraMap) {
		// === 삭제하고자 하는 직원의 정보가 팀장일 경우 부서 테이블 managerid null처리 (update) === //
		dao.delManagerId(paraMap);
	}

	@Override
	public int empDelEnd(Map<String, String> paraMap) {
		// === 삭제하고자 하는 직원의 정보 update(admin, retire, retiredate) === //
		int n = dao.empDelEnd(paraMap);
		return n;
	}

	@Override
	public List<Map<String, String>> getOrganization() {
		// === 조직도 리스트 가져오기(select) === //
		List<Map<String, String>> organizationList = dao.getOrganization();
		return organizationList;
	}

	

	
}
