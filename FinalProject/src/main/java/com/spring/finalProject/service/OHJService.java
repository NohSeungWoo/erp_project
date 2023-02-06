package com.spring.finalProject.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.board.model.BoardCategoryVO_OHJ;
import com.spring.board.model.BoardCommentVO_OHJ;
import com.spring.board.model.BoardVO_OHJ;
import com.spring.finalProject.model.InterOHJDAO;

//==== #31. Service 선언 ====
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
@Service // 원래 bean에 올려주기 위해서 @Component를 써야하는데 @Service를 쓰기 때문에 자동적으로 bean에 올라간다.
public class OHJService implements InterOHJService {
	
	// === #34. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private InterOHJDAO dao; // 다형성 // 원래는 dao가 아니라 boardDAO라고 써줘야하는데 지금은 BoardDAO가 한개밖에 없으므로 @Autowired에 의해 타입만 맞으면 되니까 dswefwf라고 써도 된다.
	// Type 에 따라 Spring 컨테이너가 알아서 bean 으로 등록된 com.spring.model.BoardDAO 의 bean 을  dao 에 주입시켜준다. 
    // 그러므로 dao 는 null 이 아니다.

	
	/////////////////////////////////////////////////////////////////////////////////
	// 기본셋팅 끝이다. 여기서부터 개발 시작이다! //
	/////////////////////////////////////////////////////////////////////////////////
		
	// 카테고리명 중복체크하기(Ajax 로 처리)
	@Override
	public boolean cNameDuplicateCheck(String bCategoryName) {
		boolean isExists = false;
		
		int n = dao.cNameDuplicateCheck(bCategoryName);
		
		if(n==1) {
			isExists = true;
		}
		
		return isExists;
	}
	
	// === 게시판 만들기 === //
	@Override
	public int makeBCategory(BoardCategoryVO_OHJ bCategoryvo) {
		int n = dao.makeBCategory(bCategoryvo);
		return n;
	}
	
	// === 게시판 종류 목록 가져오기(Ajax 로 처리) === //
	@Override
	public List<BoardCategoryVO_OHJ> viewCategoryList() {
		List<BoardCategoryVO_OHJ> bcategoryList = dao.viewCategoryList();
		return bcategoryList;
	}
		
	// === 해당하는 게시판 카테고리명 알아오기 === //
	@Override
	public String getBCategoryName(String bCategory) {
		String bCategoryName = dao.getBCategoryName(bCategory);
		return bCategoryName;
	}
	
	
	
	
	
	
	
	
	
	
	
		
		
		
		
	
	
	
	// === &55. 글쓰기(파일첨부가 없는 글쓰기) === //
	@Override
	public int boardWrite(BoardVO_OHJ boardvo) {
		int n = dao.boardWrite(boardvo);
		return n;
	}


	// === &59. 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO_OHJ> boardListNoSearch() {
		List<BoardVO_OHJ> boardList = dao.boardListNoSearch();
		return boardList;
	}


	// === &63. 글1개를 보여주는 페이지 요청 === //
	@Override
	public BoardVO_OHJ getView(Map<String, String> paraMap, String login_employeeId) {
			// login_employeeId 는 로그인을 한 상태이라면 사용자의 employeeId 이고,
			// 로그인을 하지 않은 상태이라면 login_employeeId 는 null 이다.
		
		BoardVO_OHJ boardvo = dao.getView(paraMap); // 글1개 조회하기
		
		// 글조회수 증가는 로그인을 한 상태에서 다른 사람의 글을 읽을 때만 증가하도록 해야 한다.
		if(login_employeeId != null &&
		   boardvo != null &&
		  !login_employeeId.equals(boardvo.getFk_employeeId())) {
			
			dao.addReadCount(boardvo.getBoardSeq()); // 글조회수 1증가 하기
			boardvo = dao.getView(paraMap); // 증가한 조회수때문에 글1개를 다시 조회해와야한다!!
		}
		
		return boardvo;
	}


	// === &70. 글조회수 증가는 없고 단순히 글1개 조회 === //
	@Override
	public BoardVO_OHJ getViewWithNoAddCount(Map<String, String> paraMap) {
		BoardVO_OHJ boardvo = dao.getView(paraMap); // 글1개 조회하기
		return boardvo;
	}

	
	// === &73. 1개글 수정하기 === //
	@Override
	public int boardEdit(BoardVO_OHJ boardvo) {
		int n = dao.boardEdit(boardvo);
		return n;
	}


	// === &78. 1개글 삭제하기 === //
	@Override
	public int boardDel(Map<String, String> paraMap) {
		int n = dao.boardDel(paraMap);
		return n;
	}


	// === &85. 댓글쓰기(transaction 처리) === //
	// tbl_boardComment 테이블에 insert 된 다음에
	// tbl_board 테이블에 commentCount 컬럼이 1증가(update) 하도록 요청한다.
	// 즉, 2개의 DML 처리를 해야하므로 Transaction 처리를 해야 한다.
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public int boardCommentWrite(BoardCommentVO_OHJ commentvo) throws Throwable {
		
		int n=0, m=0;
		
		// 댓글쓰기(tbl_boardComment 테이블에 insert)
		n = dao.boardCommentWrite(commentvo);
		
		if(n==1) {
			// tbl_board 테이블에 commentCount 컬럼이 1증가(update)
			m = dao.updateCommentCount(commentvo.getFk_boardSeq());
		}
		
		return m;
	}


	// === &91. 원게시물에 딸린 댓글들을 조회해오는 것 === //
	@Override
	public List<BoardCommentVO_OHJ> getCommentList(String fk_boardSeq) {
		List<BoardCommentVO_OHJ> commentList = dao.getCommentList(fk_boardSeq);
		return commentList;
	}


	// === &103. 페이징 처리를 안한, 검색어가 있는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO_OHJ> boardListSearch(Map<String, String> paraMap) {
		List<BoardVO_OHJ> boardList = dao.boardListSearch(paraMap);
		return boardList;
	}


	// === &115. 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다. === //
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int totalCount = dao.getTotalCount(paraMap);
		return totalCount;
	}


	// === &118. 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것) === //
	@Override
	public List<BoardVO_OHJ> boardListSearchWithPaging(Map<String, String> paraMap) {
		List<BoardVO_OHJ> boardList = dao.boardListSearchWithPaging(paraMap);
		return boardList;
	}
	
	
	
	
	
	
	
	
	// === 검색어의 빈도를 나타내기 위해, 검색어가 있으면 '검색키워드 기록'테이블에 insert하기 === //
	@Override
	public void registerSearchKeyword(String searchWord) {
		dao.registerSearchKeyword(searchWord);
	}

	// === '검색어키워드기록'을 가져와서 하나의 문자열로 만들기 === //
	@Override
	public List<String> getKeywordHistory() {
		List<String> keywordList = dao.getKeywordHistory();
		return keywordList;
	}

	
	
	
	
	
	// === &157. 글쓰기(파일첨부가 있는 글쓰기) === //
	@Override
	public int boardWrite_withFile(BoardVO_OHJ boardvo) {
		int n = dao.boardWrite_withFile(boardvo); // 첨부파일이 있는 경우
		return n;
	}

	

	


	

	
	

	
	
	
	
}
