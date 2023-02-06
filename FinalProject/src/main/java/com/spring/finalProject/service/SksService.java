package com.spring.finalProject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.finalProject.model.CommuteVO_SKS;
import com.spring.finalProject.model.EmployeeVO_SKS;
import com.spring.finalProject.model.InterSksDAO;
import com.spring.finalProject.model.VacationVO_SKS;
import com.spring.finalProject.model.WorkemployeeVO_SKS;
@Service
public class SksService implements InterSksService {
	
	// === #34. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private InterSksDAO dao; // 다형성
	
	@Override
	public List<Map<String, String>> WorkCntByWork() {
		List<Map<String, String>> WorkPercentageList = dao.WorkCntByWork();
		return WorkPercentageList;
	}

	/*	
	// 특정 부서명에 근무하는 직원들의 성별 인원수 및 퍼센티지 가져오기
	@Override
	public List<Map<String, String>> genderCntSpecialDeptname(Map<String, String> paraMap) {
		List<Map<String, String>> genderPercentageList = dao.genderCntSpecialDeptname(paraMap); 
		return genderPercentageList;
	}
*/	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int vacationInsert(VacationVO_SKS vctvo) {
		
		int n=0,  result=0;
		
		 n = dao.vacationInsert(vctvo);
		
		if(n==1) {
			// tbl_member 테이블에 point 컬럼의 값을 50증가(update)
			 Map<String, String> paraMap = new HashMap<>();
			paraMap.put("vacation", vctvo.getVacation()); 
			paraMap.put("employeeid",vctvo.getFk_employeeid());
			paraMap.put("dayoff", vctvo.getAnnual());
			
			
				result = dao.updatedayoff(paraMap);
			
			
				
		}
		
		return result;
		
	//	return result;
	}


	@Override
	public EmployeeVO_SKS getLoginMember(Map<String, String> paraMap) {
		EmployeeVO_SKS loginuser = dao.getLoginMember(paraMap);
		return loginuser;
	}
/*
	@Override
	public List<VacationVO> vacationlistListNoSearch() {
		List<VacationVO> vacationlists = dao.vacationlistListNoSearch();
		return vacationlists;
	}
*/	
	@Override
	public List<VacationVO_SKS> vacationlistListNoSearch(Map<String, String> paraMap) {
		List<VacationVO_SKS> vacationlists = dao.vacationlistListNoSearch(paraMap);
		return vacationlists;
	}
	

	@Override
	public List<VacationVO_SKS> vacationlistsListSearch() {
		
		List<VacationVO_SKS> vacationlistess = dao.vacationlistsListSearch();
		return vacationlistess;
	}
	
	@Override
	public List<VacationVO_SKS> vacationlistListSearch(Map<String, String> paraMap) {
		List<VacationVO_SKS> vacationlistes = dao.vacationlistListSearch(paraMap);
		return vacationlistes;
	}
	
	@Override
	public VacationVO_SKS getvacationViewNocount(Map<String, String> paraMap) {
		VacationVO_SKS vacvo = dao.getvacationViewNocount(paraMap);
		return vacvo;
	}

	@Override
	public List<VacationVO_SKS> vacationlisteListSearch() {
		List<VacationVO_SKS> vacationlistse = dao.vacationlisteListSearch();
		return vacationlistse;
	}
	
	@Override
	public List<VacationVO_SKS> vacationlistesListSearch() {
		List<VacationVO_SKS> vacationlistses = dao.vacationlistesListSearch();
		return vacationlistses;
	}

	@Override
	public int vacationedit(VacationVO_SKS vacvo) {
		int n = dao.vacationedit(vacvo);
		return n;
	}


	@Override
	public int vacationdel(Map<String, String> paraMap) {
		int n = dao.vacationdel(paraMap);
		return n;
	}


	@Override
	public List<CommuteVO_SKS> CommutelistListSearch(Map<String, String> paraMap) {
		List<CommuteVO_SKS> Commutelist = dao.CommutelistListSearch(paraMap);
		return Commutelist;
	}
	
	@Override
	public List<VacationVO_SKS> vacationListSearchWithPaging(Map<String, String> paraMap) {
		List<VacationVO_SKS> vacationlist = dao.vacationListSearchWithPaging(paraMap);
		return vacationlist;
	}
/*
	@Override
	public void pointPlus(Map<String, String> paraMap) {
		// TODO Auto-generated method stub
		
	}

*/

	

	@Override
	public int commutegtw(CommuteVO_SKS ctvo) {
		int n = dao.commutegtw(ctvo);
		return n;
	}
	
	@Override
	public int commuteow(CommuteVO_SKS ctvo) {
		
		int n = dao.commuteow(ctvo);
		return n;
	}
/*	
	@Override
	public List<CommuteVO> CommutelistListNoSearch() {
		List<CommuteVO> Commutelists = dao.CommutelistListNoSearch();
		return Commutelists;
	}
	*/
	@Override
	public List<CommuteVO_SKS> CommutelistListNoSearch(Map<String, String> paraMap) {
		List<CommuteVO_SKS> Commutelists = dao.CommutelistListNoSearch(paraMap);
		return Commutelists;
	}
	
/*
	@Override
	public List<CommuteVO> CommutelistListSearch(Map<String, String> paraMap) {
		List<CommuteVO> Commutelist = dao.CommutelistListSearch(paraMap);
		return Commutelist;
	}
*/	
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int n = dao.getTotalCount(paraMap);
		return n;
	}
	
	@Override
	public int getTotalCountS(Map<String, String> paraMap) {
		int n = dao.getTotalCountS(paraMap);
		return n;
	}
	
	@Override
	public int getworkTotalCount(Map<String, String> paraMap) {
		int n = dao.getworkTotalCount(paraMap);
		return n;
	}
	
	@Override
	public int workEnd(WorkemployeeVO_SKS wpvo) {
		
		int n = dao.workEnd(wpvo);
		return n;
	}
	
	@Override
	public List<WorkemployeeVO_SKS> worklistSearch(Map<String, String> paraMap) {
		List<WorkemployeeVO_SKS> Employeeworklist = dao.worklistSearch(paraMap);
		return Employeeworklist;
	}
	
	@Override
	public List<CommuteVO_SKS> CommuteListSearchWithPaging(Map<String, String> paraMap) {
		List<CommuteVO_SKS> Commutelist = dao.CommuteListSearchWithPaging(paraMap);
		return Commutelist;
	}
	
	@Override
	public List<WorkemployeeVO_SKS> EmployeeworklistSearchWithPaging(Map<String, String> paraMap) {
		List<WorkemployeeVO_SKS> Employeeworklist = dao.EmployeeworklistSearchWithPaging(paraMap);
		return Employeeworklist;
	}
	@Override
	public CommuteVO_SKS getCommuteViewNocount(Map<String, String> paraMap) {
		CommuteVO_SKS ctvo = dao.getCommuteViewNocount(paraMap);
		return ctvo;
	}
	@Override
	public int commuteedit(CommuteVO_SKS ctvo) {
		int n = dao.commuteedit(ctvo);
		return n;
	}
	@Override
	public WorkemployeeVO_SKS getworkviewNocount(Map<String, String> paraMap) {
		WorkemployeeVO_SKS wpvo = dao.getworkviewNocount(paraMap);
		return wpvo;
	}
	@Override
	public int workeditEnd(WorkemployeeVO_SKS wpvo) {
		int n = dao.workeditEnd(wpvo);
		return n;
	}

	@Override
	public List<WorkemployeeVO_SKS> worklistSearchs(Map<String, String> paraMap) {
		List<WorkemployeeVO_SKS> Employeeworklists = dao.worklistSearchs(paraMap);
		return Employeeworklists;
	}

	@Override
	public List<WorkemployeeVO_SKS> worklistnoSearchs(Map<String, String> paraMap) {
		List<WorkemployeeVO_SKS> Employeeworklistes = dao.worklistnoSearchs(paraMap);
		return Employeeworklistes;
	}

	@Override
	public List<WorkemployeeVO_SKS> worklistsSearch() {
		List<WorkemployeeVO_SKS> Employeeworklistt = dao.worklistsSearch();
		return Employeeworklistt;
	}

	@Override
	public List<WorkemployeeVO_SKS> worklistseSearch() {
		List<WorkemployeeVO_SKS> Employeeworklistts = dao.worklistseSearch();
		return Employeeworklistts;
	}

	@Override
	public List<WorkemployeeVO_SKS> worklistsesSearch() {
		List<WorkemployeeVO_SKS> Employeeworklisttss = dao.worklistsesSearch();
		return Employeeworklisttss;
	}


	@Override
	public List<CommuteVO_SKS> CommutelistListSearchs() {
		List<CommuteVO_SKS> Commutelistt =dao.CommutelistListSearchs();
		return Commutelistt;
	}

	@Override
	public List<CommuteVO_SKS> CommutelistListewSearchs() {
		List<CommuteVO_SKS> Commutelistts =dao.CommutelistListewSearchs();
		return Commutelistts;
	}

	@Override
	public List<CommuteVO_SKS> CommutelistListsewSearchs() {
		List<CommuteVO_SKS> Commutelistst =dao.CommutelistListsewSearchs();
		return Commutelistst;
	}

	@Override
	public List<CommuteVO_SKS> CommutelistLisstNoSearch(Map<String, String> paraMap) {
		List<CommuteVO_SKS> Commutelistes = dao.CommutelistLisstNoSearch(paraMap);
		return Commutelistes;
	}






}
