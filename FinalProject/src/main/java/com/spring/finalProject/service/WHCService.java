package com.spring.finalProject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.approval.model.ApcategoryVO;
import com.spring.approval.model.ApprovalVO;
import com.spring.approval.model.OpinionVO;
import com.spring.finalProject.model.DepartmentVO_KGH;
import com.spring.finalProject.model.InterWHCDAO;

//==== #31. Service 선언 ====
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
@Service // 원래 bean에 올려주기 위해서 @Component를 써야하는데 @Service를 쓰기 때문에 자동적으로 bean에 올라간다.
public class WHCService implements InterWHCService {
	
	// === #34. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private InterWHCDAO whcdao; // 다형성 // 원래는 dao가 아니라 boardDAO라고 써줘야하는데 지금은 BoardDAO가 한개밖에 없으므로 @Autowired에 의해 타입만 맞으면 되니까 dswefwf라고 써도 된다.
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.model.BoardDAO 의 bean 을  dao 에 주입시켜준다. 
    // 그러므로 dao 는 null 이 아니다.

	// === 기안양식 카테고리 얻어오기 === // 
	@Override
	public List<ApcategoryVO> getApcategoryList() {
		
		List<ApcategoryVO> apcList = whcdao.getApcategoryList();
		return apcList;
	}

	// === 부서명, 부서번호 받아오기 === //
	@Override
	public List<DepartmentVO_KGH> getDepartment() {
		List<DepartmentVO_KGH> departList = whcdao.getDepartment();
		return departList;
	}
	
	// 결제선에 이용할 사원 리스트
	@Override
	public List<Map<String, String>> getEmployeeList(Map<String, String> paraMap) {
		List<Map<String, String>> empList = whcdao.getEmployeeList(paraMap);
		return empList;
	}

	// 기안쓰기
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int addAP(ApprovalVO approvalvo, Map<String, String> paraMap) {
		
		int n=0,m=0,result=0;
		
		// 결재문서번호 채번하기
		String apno = whcdao.getnewApno();
		if(apno != null) {
			approvalvo.setApno(apno);
			n = whcdao.addAP(approvalvo);
			
			if(n==1) {
				paraMap.put("fk_apno",apno);
				m = whcdao.addAPSortByCategory(paraMap);
				
				if(m==1) {
					String[] arrAppr = approvalvo.getApprEmp().split(",");
					Map<String, String> apturnMap = new HashMap<>();
					
					for(int i=0; i<arrAppr.length; i++) {
						
						apturnMap.put("fk_apno", apno);
						apturnMap.put("fk_employeeid", arrAppr[i]);
						if(i==0) {
							apturnMap.put("yn", "1");
						}
						else {
							apturnMap.put("yn", "0");
						}
						
						result = whcdao.addApturn(apturnMap);
						
					}// end of for--------------------------------------------
				}
			}
		}
		
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int addAP_withFile(ApprovalVO approvalvo, Map<String, String> paraMap) {
		// TODO Auto-generated method stub
		return 0;
	}

	// 페이징처리를 위한 총게시물 수
	@Override
	public int getTotalCount(Map<String, String> searchmap) {
		int getTotalCount = whcdao.getTotalCount(searchmap);
		return getTotalCount;
	}

	// 페이징처리를 한 기안목록 받아오기
	@Override
	public List<Map<String, String>> approvalListSearchPaging(Map<String, String> searchmap) {
		List<Map<String, String>> approvalListSearchPaging = whcdao.approvalListSearchPaging(searchmap);
		return approvalListSearchPaging;
	}
	
	// 페이징처리를 한 받은기안목록 받아오기
	@Override
	public List<Map<String, String>> receiveDocapproval(Map<String, String> searchmap) {
		List<Map<String, String>> receiveDocapproval = whcdao.receiveDocapproval(searchmap);
		return receiveDocapproval;
	}
	
	// 페이징처리를 위한 협조,수신 총게시물 수
	@Override
	public int getCooDocTotalCount(Map<String, String> searchmap) {
		int getTotalCount = whcdao.getCooDocTotalCount(searchmap);
		return getTotalCount;
	}
	
	// 페이징처리를 한 협조목록 받아오기
	@Override
	public List<Map<String, String>> cooDocapproval(Map<String, String> searchmap) {
		List<Map<String, String>> cooDocapproval = whcdao.cooDocapproval(searchmap);
		return cooDocapproval;
	}
	
	// 문서상세 조회하기
	@Override
	public ApprovalVO getDocdetail(Map<String, String> paraMap) {
		ApprovalVO approval = whcdao.getDocdetail(paraMap);
		return approval;
	}
	
	// 본인이 결재자에 포함되었으며 문서가 진행중이라면 본인의 결재차례인지 확인
	@Override
	public String isApYN(Map<String, String> paraMap) {
		String yn = whcdao.isApYN(paraMap);
		return yn;
	}
	
	// 직원번호로 직책, 부서 알아오기
	@Override
	public Map<String, String> getDeptNPos(Map<String, String> empidMap) {
		Map<String, String> resultMap = whcdao.getDeptNPos(empidMap);
		return resultMap;
	}
	
	// 문서종류에 따른 상세내용 select
	@Override
	public Map<String, String> cateApdetail(Map<String, String> cateMap) {
		Map<String, String> resultMap = whcdao.cateApdetail(cateMap);
		return resultMap;
	}

	// 결재의견 작성하기
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int addOpinion(OpinionVO opnvo, Map<String, String> nextEmp) {
		
		int n=0,m=0,result=0; 
		
		// 결재의견 테이블에 insert 하기
		n = whcdao.addOpnion(opnvo);
		
		if(n==1) {
			
			m = whcdao.updateYN(nextEmp); // 본인 yn 0 으로 변경
			
			if(m==1) {
				
				if("1".equals(opnvo.getApstatus()) && !"".equals(nextEmp.get("nextAppEmp")) ) { // 결재이면서 다음결재자가 있다면
					nextEmp.put("yn", "1");
					nextEmp.put("fk_employeeid", nextEmp.get("nextAppEmp"));
					result = whcdao.updateYN(nextEmp); 		// 다음사람 yn 1로 변경
				}
				else { // 결재이면서 다음결재자가 없거나 반려 라면
					nextEmp.put("apstatus", opnvo.getApstatus());
					result = whcdao.updateApStatus(nextEmp);// 문서상태 변경
				}
			}
		}
		return result;
	}
	
	// 결재의견 읽어오기
	@Override
	public List<OpinionVO> readOpinion(Map<String, String> paraMap) {
		List<OpinionVO> opnionList = whcdao.readOpinion(paraMap);
		return opnionList;
	}
	
	
	
	
}
