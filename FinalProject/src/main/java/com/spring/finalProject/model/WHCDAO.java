package com.spring.finalProject.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.approval.model.ApcategoryVO;
import com.spring.approval.model.ApprovalVO;
import com.spring.approval.model.OpinionVO;

//==== #32. DAO 선언 ====
@Repository // 원래 bean에 올려주기 위해서 @Component를 써야하는데 @Repository를 쓰기 때문에 자동적으로 bean에 올라간다.
public class WHCDAO implements InterWHCDAO {
	
	// === #33. 의존객체 주입하기(DI: Dependency Injection) ===
    // >>> 의존 객체 자동 주입(Automatic Dependency Injection)은
    //     스프링 컨테이너가 자동적으로 의존 대상 객체를 찾아서 해당 객체에 필요한 의존객체를 주입하는 것을 말한다. 
    //     단, 의존객체는 스프링 컨테이너속에 bean 으로 등록되어 있어야 한다. 

    //     의존 객체 자동 주입(Automatic Dependency Injection)방법 3가지 
    //     1. @Autowired ==> Spring Framework에서 지원하는 어노테이션이다. 
    //                       스프링 컨테이너에 담겨진 의존객체를 주입할때 타입을 찾아서 연결(의존객체주입)한다.
   
    //     2. @Resource  ==> Java 에서 지원하는 어노테이션이다.
    //                       스프링 컨테이너에 담겨진 의존객체를 주입할때 필드명(이름)을 찾아서 연결(의존객체주입)한다.
   
    //     3. @Inject    ==> Java 에서 지원하는 어노테이션이다.
    //                       스프링 컨테이너에 담겨진 의존객체를 주입할때 타입을 찾아서 연결(의존객체주입)한다.   
	
	@Resource
	private SqlSessionTemplate sqlsession; // 원격DB remote_finalorauser1 에 연결
	// Type 에 따라 Spring 컨테이너가 알아서 root-context.xml 에 생성된 org.mybatis.spring.SqlSessionTemplate 의 sqlsession bean 을  sqlsession 에 주입시켜준다. 
    // 그러므로 sqlsession 는 null 이 아니다.

	// === 기안양식 카테고리 얻어오기 === //
	@Override
	public List<ApcategoryVO> getApcategoryList() {
		List<ApcategoryVO> apcList = sqlsession.selectList("woohc.getApcategoryList");
		return apcList;
	}

	// === 부서명, 부서번호 받아오기 === //
	@Override
	public List<DepartmentVO_KGH> getDepartment() {
		List<DepartmentVO_KGH> departList = sqlsession.selectList("woohc.getDepartment");
		return departList;
	}

	// 결제선에 이용할 사원 리스트
	@Override
	public List<Map<String, String>> getEmployeeList(Map<String, String> paraMap) {
		List<Map<String, String>> empList = sqlsession.selectList("woohc.getEmployeeList", paraMap);
		return empList;
	}

	// 결재문서번호 채번하기
	@Override
	public String getnewApno() {
		
		String apno = sqlsession.selectOne("woohc.getnewApno");
		return apno;
	}
	
	// 전자결재테이블에 insert 하기
	@Override
	public int addAP(ApprovalVO approvalvo) {
		int n = sqlsession.insert("woohc.addAP", approvalvo);
		return n;
	}

	// 카테고리에 맞는 문서에 insert 하기
	@Override
	public int addAPSortByCategory(Map<String, String> paraMap) {
		int m = sqlsession.insert("woohc.addAPSortByCategory", paraMap);
		return m;
	}
	
	// 결재차례테이블에 결재자 insert 하기
	@Override
	public int addApturn(Map<String, String> apturnMap) {
		int result = sqlsession.insert("woohc.addApturn", apturnMap);
		return result;
	}

	// 페이징처리를 위한 총게시물 수
	@Override
	public int getTotalCount(Map<String, String> searchmap) {
		int getTotalCount = sqlsession.selectOne("woohc.getTotalCount", searchmap);
		return getTotalCount;
	}

	// 페이징처리를 한 기안목록 받아오기
	@Override
	public List<Map<String, String>> approvalListSearchPaging(Map<String, String> searchmap) {
		List<Map<String, String>> approvalListSearchPaging = sqlsession.selectList("woohc.approvalListSearchPaging", searchmap);
		return approvalListSearchPaging;
	}
	
	// 페이징처리를 한 받은기안목록 받아오기
	@Override
	public List<Map<String, String>> receiveDocapproval(Map<String, String> searchmap) {
		List<Map<String, String>> receiveDocapproval = sqlsession.selectList("woohc.receiveDocapproval", searchmap);
		return receiveDocapproval;
	}
	
	// 페이징처리를 위한 협조,수신 총게시물 수
	@Override
	public int getCooDocTotalCount(Map<String, String> searchmap) {
		int getTotalCount = sqlsession.selectOne("woohc.getCooDocTotalCount", searchmap);
		return getTotalCount;
	}
	
	// 페이징처리를 한 협조목록 받아오기
	@Override
	public List<Map<String, String>> cooDocapproval(Map<String, String> searchmap) {
		List<Map<String, String>> cooDocapproval = sqlsession.selectList("woohc.cooDocapproval", searchmap);
		return cooDocapproval;
	}

	// 문서상세 조회하기
	@Override
	public ApprovalVO getDocdetail(Map<String, String> paraMap) {
		ApprovalVO approval = sqlsession.selectOne("woohc.getDocdetail", paraMap);
		return approval;
	}
	
	// 본인이 결재자에 포함되었으며 문서가 진행중이라면 본인의 결재차례인지 확인
	@Override
	public String isApYN(Map<String, String> paraMap) {
		String yn = sqlsession.selectOne("woohc.isApYN", paraMap);
		return yn;
	}
	
	// 직원번호로 직책, 부서 알아오기
	@Override
	public Map<String, String> getDeptNPos(Map<String, String> empidMap) {
		Map<String, String> resultMap = sqlsession.selectOne("woohc.getDeptNPos", empidMap);
		return resultMap;
	}
	
	// 문서종류에 따른 상세내용 select
	@Override
	public Map<String, String> cateApdetail(Map<String, String> cateMap) {
		Map<String, String> resultMap = sqlsession.selectOne("woohc.cateApdetail", cateMap);
		return resultMap;
	}

	// 결재의견 테이블에 insert 하기
	@Override
	public int addOpnion(OpinionVO opnvo) {
		int n = sqlsession.insert("woohc.addOpnion", opnvo);
		return n;
	}
	
	// 결재차례 테이블 update
	@Override
	public int updateYN(Map<String, String> nextEmp) {
		int n = sqlsession.update("woohc.updateYN", nextEmp);
		return n;
	}
	
	// 결재테이블 문서 상태 변경
	@Override
	public int updateApStatus(Map<String, String> nextEmp) {
		int n = sqlsession.update("woohc.updateApStatus", nextEmp);
		return n;
	}
	
	// 결재의견 읽어오기
	@Override
	public List<OpinionVO> readOpinion(Map<String, String> paraMap) {
		
		List<OpinionVO> opinionList = sqlsession.selectList("woohc.readOpinion",paraMap);
		return opinionList;
		
	}
	
	
	
}
