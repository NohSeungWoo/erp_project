package com.spring.finalProject.model;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SksDAO implements InterSksDAO {

		@Resource
		private SqlSessionTemplate sqlsession;  
		
		@Resource
		private SqlSessionTemplate sqlsession2; // 원격DB 연결
		// Type 에 따라 Spring 컨테이너가 알아서 root-context.xml 에 생성된 org.mybatis.spring.SqlSessionTemplate 의 sqlsession2 bean 을  sqlsession2 에 주입시켜준다. 
	    // 그러므로 sqlsession2 는 null 이 아니다.
		
		@Resource
		private SqlSessionTemplate sqlsession3;

		@Override
		public List<Map<String, String>> WorkCntByWork() {
			List<Map<String, String>> WorkPercentageList = sqlsession2.selectList("Sks.WorkCntByWork");
			return WorkPercentageList;
		}
	/*	
		// 특정 부서명에 근무하는 직원들의 성별 인원수 및 퍼센티지 가져오기
		@Override
		public List<Map<String, String>> genderCntSpecialDeptname(Map<String, String> paraMap) {
			List<Map<String, String>> genderPercentageList = sqlsession2.selectList("Vacation.genderCntSpecialDeptname", paraMap);
			return genderPercentageList;
		}	
*/		
		@Override
		public int vacationInsert(VacationVO_SKS vctvo) {
			int n = sqlsession2.insert("Sks.vacationInsert",vctvo);
			return n;
		}
		
		@Override
		public int updatedayoff(Map<String, String> paraMap) {
			int n = sqlsession2.update("Sks.updatedayoff", paraMap);
			return n;
		}
		
		
		@Override
		public EmployeeVO_SKS getLoginMember(Map<String, String> paraMap) {
			EmployeeVO_SKS loginuser = sqlsession2.selectOne("Sks.getLoginMember", paraMap);
			return loginuser;
		}		
/*
		@Override
		public List<VacationVO> vacationlistListNoSearch() {
			List<VacationVO> vacationlists = sqlsession2.selectList("Vacation.vacationlistListNoSearch");
			return vacationlists;
		}
*/			
		@Override
		public List<VacationVO_SKS> vacationListSearchWithPaging(Map<String, String> paraMap) {
			List<VacationVO_SKS> vacationlist = sqlsession2.selectList("Sks.vacationListSearchWithPaging",paraMap);
			return vacationlist;
		}
		@Override
		public List<VacationVO_SKS> vacationlistListNoSearch(Map<String, String> paraMap) {
			List<VacationVO_SKS> vacationlists = sqlsession2.selectList("Sks.vacationlistListNoSearch",paraMap);
			return vacationlists;
		}
		
		@Override
		public List<VacationVO_SKS> vacationlistListSearch(Map<String, String> paraMap) {
			List<VacationVO_SKS> vacationlistes = sqlsession2.selectList("Sks.vacationlistListSearch",paraMap);
			return vacationlistes;
		}	
		
		@Override
		public List<VacationVO_SKS> vacationlistsListSearch() {
			List<VacationVO_SKS> vacationlistess = sqlsession2.selectList("Sks.vacationlistsListSearch");
			return vacationlistess;
		}
		
		@Override
		public List<VacationVO_SKS> vacationlisteListSearch() {
			List<VacationVO_SKS> vacationlistse = sqlsession2.selectList("Sks.vacationlisteListSearch");
			return vacationlistse;
		}
		
		@Override
		public List<VacationVO_SKS> vacationlistesListSearch() {
			List<VacationVO_SKS> vacationlistses = sqlsession2.selectList("Sks.vacationlistesListSearch");
			return vacationlistses;
		}
		
		@Override
		public VacationVO_SKS getvacationViewNocount(Map<String, String> paraMap) {
			VacationVO_SKS vacvo = sqlsession2.selectOne("Sks.getvacationViewNocount",paraMap);
			return vacvo;
		}

		@Override
		public int vacationedit(VacationVO_SKS vacvo) {
			int n = sqlsession2.update("Sks.vacationedit",vacvo);
			return n;
		}

		@Override
		public int vacationdel(Map<String, String> paraMap) {
			int n = sqlsession2.delete("Sks.vacationdel",paraMap);
			return n;
		}

		@Override
		public List<CommuteVO_SKS> CommutelistListSearch(Map<String, String> paraMap) {
			List<CommuteVO_SKS> Commutelist = sqlsession2.selectList("Sks.CommutelistListSearch",paraMap);
			return Commutelist;
		}
		
		
		
		@Override
		public int commutegtw(CommuteVO_SKS ctvo) {
			int n = sqlsession2.insert("Sks.commutegtw",ctvo);
			return n;
		}
		
		@Override
		public int commuteow(CommuteVO_SKS ctvo) {
			int n = sqlsession2.insert("Sks.commuteow",ctvo);
			return n;
		}
		
		@Override
		public List<CommuteVO_SKS> CommutelistListNoSearch(Map<String, String> paraMap) {
			List<CommuteVO_SKS> Commutelists = sqlsession2.selectList("Sks.CommutelistListNoSearch",paraMap);
			return Commutelists;
		}
	/*	
		@Override
		public List<CommuteVO> CommutelistListNoSearch() {
			List<CommuteVO> Commutelists = sqlsession2.selectList("Commute.CommutelistListNoSearch");
			return Commutelists;
		}
	/*
		@Override
		public List<CommuteVO> CommutelistListSearch(Map<String, String> paraMap) {
			List<CommuteVO> Commutelist = sqlsession2.selectList("Commute.CommutelistListSearch");
			return Commutelist;
		}
		*/	
		@Override
		public int getTotalCount(Map<String, String> paraMap) {
			int n = sqlsession2.selectOne("Sks.getTotalCount", paraMap);
			return n;
		}
		
		@Override
		public int getTotalCountS(Map<String, String> paraMap) {
			int n = sqlsession2.selectOne("Sks.getTotalCountS", paraMap);
			return n;
		}

		@Override
		public int getworkTotalCount(Map<String, String> paraMap) {
			int n = sqlsession2.selectOne("Sks.getworkTotalCount", paraMap);
			return n;
		}
		
		@Override
		public int workEnd(WorkemployeeVO_SKS wpvo) {
			int n = sqlsession2.insert("Sks.workEnd",wpvo);
			return n;
		}

		@Override
		public List<WorkemployeeVO_SKS> worklistSearch(Map<String, String> paraMap) {
			List<WorkemployeeVO_SKS> Employeeworklist = sqlsession2.selectList("Sks.worklistSearch",paraMap);
			return Employeeworklist;
		}
		@Override
		public List<WorkemployeeVO_SKS> worklistSearchs(Map<String, String> paraMap) {
			List<WorkemployeeVO_SKS> Employeeworklists = sqlsession2.selectList("Sks.worklistSearchs",paraMap);
			return Employeeworklists;
		}
		@Override
		public List<WorkemployeeVO_SKS> worklistnoSearchs(Map<String, String> paraMap) {
			List<WorkemployeeVO_SKS> Employeeworklistes = sqlsession2.selectOne("Sks.worklistnoSearchs",paraMap);
			return Employeeworklistes;
		}
		
		@Override
		public List<CommuteVO_SKS> CommuteListSearchWithPaging(Map<String, String> paraMap) {
			List<CommuteVO_SKS> Commutelist =sqlsession2.selectList("Sks.CommuteListSearchWithPaging",paraMap);
			return Commutelist;
		}
			
		@Override
		public List<WorkemployeeVO_SKS> EmployeeworklistSearchWithPaging(Map<String, String> paraMap) {
			List<WorkemployeeVO_SKS> Employeeworklist =sqlsession2.selectList("Sks.EmployeeworklistSearchWithPaging",paraMap);
			return Employeeworklist;
		}
		@Override
		public CommuteVO_SKS getCommuteViewNocount(Map<String, String> paraMap) {
			CommuteVO_SKS ctvo  = sqlsession2.selectOne("Sks.getCommuteViewNocount",paraMap);
			return ctvo;
		}
		@Override
		public int commuteedit(CommuteVO_SKS ctvo) {
			int n = sqlsession2.update("Sks.commuteedit",ctvo);
			return n;
		}
		@Override
		public WorkemployeeVO_SKS getworkviewNocount(Map<String, String> paraMap) {
			WorkemployeeVO_SKS wpvo = sqlsession2.selectOne("Sks.getworkviewNocount",paraMap);
			return wpvo;
		}
		@Override
		public int workeditEnd(WorkemployeeVO_SKS wpvo) {
			int n = sqlsession2.update("Sks.workeditEnd",wpvo);
			return n;
		}
		@Override
		public List<WorkemployeeVO_SKS> worklistsSearch() {
			List<WorkemployeeVO_SKS> Employeeworklistt =sqlsession2.selectList("Sks.worklistsSearch");
			return Employeeworklistt;
		}
		@Override
		public List<WorkemployeeVO_SKS> worklistseSearch() {
			List<WorkemployeeVO_SKS> Employeeworklistts =sqlsession2.selectList("Sks.worklistseSearch");
			return Employeeworklistts;
		}
		@Override
		public List<WorkemployeeVO_SKS> worklistsesSearch() {
			List<WorkemployeeVO_SKS> Employeeworklisttss =sqlsession2.selectList("Sks.worklistsesSearch");
			return Employeeworklisttss;
		}
		@Override
		public List<CommuteVO_SKS> CommutelistListSearchs() {
			List<CommuteVO_SKS> Commutelistt = sqlsession2.selectList("Sks.CommutelistListSearchs");
			return Commutelistt;
		}
		@Override
		public List<CommuteVO_SKS> CommutelistListewSearchs() {
			List<CommuteVO_SKS> Commutelistts = sqlsession2.selectList("Sks.CommutelistListewSearchs");
			return Commutelistts;
		}
		@Override
		public List<CommuteVO_SKS> CommutelistListsewSearchs() {
			List<CommuteVO_SKS> Commutelistst = sqlsession2.selectList("Sks.CommutelistListsewSearchs");
			return Commutelistst;
		}
		@Override
		public List<CommuteVO_SKS> CommutelistLisstNoSearch(Map<String, String> paraMap) {
			List<CommuteVO_SKS> Commutelistes = sqlsession2.selectList("Sks.CommutelistLisstNoSearch",paraMap);
			return Commutelistes;
		}
		
		
		
}
