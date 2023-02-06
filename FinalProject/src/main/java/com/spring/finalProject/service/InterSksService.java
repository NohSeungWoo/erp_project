package com.spring.finalProject.service;

import java.util.List;
import java.util.Map;

import com.spring.finalProject.model.CommuteVO_SKS;
import com.spring.finalProject.model.EmployeeVO_SKS;
import com.spring.finalProject.model.VacationVO_SKS;
import com.spring.finalProject.model.WorkemployeeVO_SKS;

public interface InterSksService {

	int vacationInsert(VacationVO_SKS vctvo);
	
	EmployeeVO_SKS getLoginMember(Map<String, String> paraMap);

//	List<VacationVO> vacationlistListNoSearch();
	
	List<VacationVO_SKS> vacationlistListSearch(Map<String, String> paraMap);
	
	VacationVO_SKS getvacationViewNocount(Map<String, String> paraMap);
	
	List<VacationVO_SKS> vacationlistsListSearch();
	
	int vacationedit(VacationVO_SKS vacvo);

	int vacationdel(Map<String, String> paraMap);

	int getTotalCount(Map<String, String> paraMap);

	List<CommuteVO_SKS> CommutelistListSearch(Map<String, String> paraMap);
/*
	List<Map<String, String>> genderCntSpecialDeptname(Map<String, String> paraMap);
*/

	List<Map<String, String>> WorkCntByWork();

	List<VacationVO_SKS> vacationListSearchWithPaging(Map<String, String> paraMap);

	List<WorkemployeeVO_SKS> worklistSearch(Map<String, String> paraMap);

	List<VacationVO_SKS> vacationlistListNoSearch(Map<String, String> paraMap);

	int commutegtw(CommuteVO_SKS ctvo);
	
	int commuteow(CommuteVO_SKS ctvo);
	
//	List<CommuteVO> CommutelistListNoSearch();

//	List<CommuteVO> CommutelistListSearch(Map<String, String> paraMap);

//	int getTotalCount(Map<String, String> paraMap);

	int getworkTotalCount(Map<String, String> paraMap);

//	List<WorkemployeeVO> worklistSearch(Map<String, String> paraMap);

	int workEnd(WorkemployeeVO_SKS wpvo);
/*
	List<Map<String, String>> employeeCntByDeptname();

	List<Map<String, String>> genderCntSpecialDeptname(Map<String, String> paraMap);
*/

	List<CommuteVO_SKS> CommuteListSearchWithPaging(Map<String, String> paraMap);

	List<WorkemployeeVO_SKS> EmployeeworklistSearchWithPaging(Map<String, String> paraMap);

	CommuteVO_SKS getCommuteViewNocount(Map<String, String> paraMap);

	int commuteedit(CommuteVO_SKS ctvo);

	WorkemployeeVO_SKS getworkviewNocount(Map<String, String> paraMap);

	int workeditEnd(WorkemployeeVO_SKS wpvo);

	List<CommuteVO_SKS> CommutelistListNoSearch(Map<String, String> paraMap);

	int getTotalCountS(Map<String, String> paraMap);

	List<WorkemployeeVO_SKS> worklistSearchs(Map<String, String> paraMap);

	List<WorkemployeeVO_SKS> worklistnoSearchs(Map<String, String> paraMap);

	List<VacationVO_SKS> vacationlisteListSearch();

	List<VacationVO_SKS> vacationlistesListSearch();

	List<WorkemployeeVO_SKS> worklistsSearch();

	List<WorkemployeeVO_SKS> worklistseSearch();

	List<WorkemployeeVO_SKS> worklistsesSearch();

	List<CommuteVO_SKS> CommutelistListSearchs();

	List<CommuteVO_SKS> CommutelistListewSearchs();

	List<CommuteVO_SKS> CommutelistListsewSearchs();

	List<CommuteVO_SKS> CommutelistLisstNoSearch(Map<String, String> paraMap);



	

	

	

	

//	void pointPlus(Map<String, String> paraMap);
	
	


	

	

	

	

	

	

	

	

	 

}
