package com.spring.finalProject.service;

import java.util.List;
import java.util.Map;

import com.spring.approval.model.ApcategoryVO;
import com.spring.approval.model.ApprovalVO;
import com.spring.approval.model.OpinionVO;
import com.spring.finalProject.model.DepartmentVO_KGH;

public interface InterWHCService {

	// 기안양식 카테고리 얻어오기
	List<ApcategoryVO> getApcategoryList();

	// 부서명, 부서번호 받아오기
	List<DepartmentVO_KGH> getDepartment();

	// 결제선에 이용할 사원 리스트
	List<Map<String, String>> getEmployeeList(Map<String, String> paraMap);

	// 첨부파일이 없는 기안쓰기
	int addAP(ApprovalVO approvalvo, Map<String, String> paraMap);

	// 첨부파일이 있는 기안쓰기
	int addAP_withFile(ApprovalVO approvalvo, Map<String, String> paraMap);

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

	// 결재의견 작성하기
	int addOpinion(OpinionVO opnvo, Map<String, String> nextEmp);
	
	// 결재의견 읽어오기
	List<OpinionVO> readOpinion(Map<String, String> paraMap);


}
