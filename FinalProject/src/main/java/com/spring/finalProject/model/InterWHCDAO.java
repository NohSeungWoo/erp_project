package com.spring.finalProject.model;

import java.util.List;
import java.util.Map;

import com.spring.approval.model.ApcategoryVO;
import com.spring.approval.model.ApprovalVO;
import com.spring.approval.model.OpinionVO;

public interface InterWHCDAO {

	// 기안양식 카테고리 얻어오기
	List<ApcategoryVO> getApcategoryList();

	// 부서명, 부서번호 받아오기
	List<DepartmentVO_KGH> getDepartment();

	// 결제선에 이용할 사원 리스트
	List<Map<String, String>> getEmployeeList(Map<String, String> paraMap);

	// 결재문서번호 채번하기
	String getnewApno();
	// 전자결재테이블에 insert 하기
	int addAP(ApprovalVO approvalvo);
	// 카테고리에 맞는 문서에 insert 하기
	int addAPSortByCategory(Map<String, String> paraMap);
	// 결재차례테이블에 결재자 insert 하기
	int addApturn(Map<String, String> apturnMap);

	// 페이징처리를 위한 총게시물 수
	int getTotalCount(Map<String, String> searchmap);

	// 페이징처리를 한 기안목록 받아오기
	List<Map<String, String>> approvalListSearchPaging(Map<String, String> searchmap);

	// 페이징처리를 한 받은기안목록 받아오기
	List<Map<String, String>> receiveDocapproval(Map<String, String> searchmap);
	
	// 페이징처리를 위한 협조,수신 총게시물 수
	int getCooDocTotalCount(Map<String, String> searchmap);
	
	// 페이징처리를 한 협조목록 받아오기
	List<Map<String, String>> cooDocapproval(Map<String, String> searchmap);

	// 문서상세 조회하기
	ApprovalVO getDocdetail(Map<String, String> paraMap);
	
	// 본인이 결재자에 포함되었으며 문서가 진행중이라면 본인의 결재차례인지 확인
	String isApYN(Map<String, String> paraMap);
	
	// 직원번호로 직책, 부서 알아오기
	Map<String, String> getDeptNPos(Map<String, String> empidMap);
	
	// 문서종류에 따른 상세내용 select
	Map<String, String> cateApdetail(Map<String, String> cateMap);

	// 결재의견 테이블에 insert 하기
	int addOpnion(OpinionVO opnvo);

	// 결재차례 테이블 update
	int updateYN(Map<String, String> nextEmp);

	// 결재테이블 문서 상태 변경
	int updateApStatus(Map<String, String> nextEmp);
	
	// 결재의견 읽어오기
	List<OpinionVO> readOpinion(Map<String, String> paraMap);

}
