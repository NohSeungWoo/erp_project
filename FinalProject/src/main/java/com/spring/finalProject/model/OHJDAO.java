package com.spring.finalProject.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.board.model.BoardCategoryVO_OHJ;
import com.spring.board.model.BoardCommentVO_OHJ;
import com.spring.board.model.BoardVO_OHJ;

//==== #32. DAO 선언 ====
@Repository // 원래 bean에 올려주기 위해서 @Component를 써야하는데 @Repository를 쓰기 때문에 자동적으로 bean에 올라간다.
public class OHJDAO implements InterOHJDAO {
	
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

	
	/////////////////////////////////////////////////////////////////////////////////
	// 기본셋팅 끝이다. 여기서부터 개발 시작이다! //
	/////////////////////////////////////////////////////////////////////////////////

	// 카테고리명 중복체크하기(Ajax 로 처리)
	@Override
	public int cNameDuplicateCheck(String bCategoryName) {
		int n = sqlsession.selectOne("ohhj.cNameDuplicateCheck", bCategoryName);
		return n;
	}
	
	// === 게시판 만들기 === //
	@Override
	public int makeBCategory(BoardCategoryVO_OHJ bCategoryvo) {
		int n = sqlsession.insert("ohhj.makeBCategory", bCategoryvo);
		return n;
	}
	
	// === 게시판 종류 목록 가져오기(Ajax 로 처리) === //
	@Override
	public List<BoardCategoryVO_OHJ> viewCategoryList() {
		List<BoardCategoryVO_OHJ> bcategoryList = sqlsession.selectList("ohhj.viewCategoryList");
		return bcategoryList;
	}
	
	// === 해당하는 게시판 카테고리명 알아오기 === //
	@Override
	public String getBCategoryName(String bCategory) {
		String bCategoryName = sqlsession.selectOne("ohhj.getBCategoryName", bCategory);
		return bCategoryName;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// === &56. 글쓰기(파일첨부가 없는 글쓰기) === //
	@Override
	public int boardWrite(BoardVO_OHJ boardvo) {
		int n = sqlsession.insert("ohhj.boardWrite", boardvo);
		return n;
	}


	// === &60. 페이징 처리를 안한 검색어가 없는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO_OHJ> boardListNoSearch() {
		List<BoardVO_OHJ> boardList = sqlsession.selectList("ohhj.boardListNoSearch");
		return boardList;
	}


	// === &64. 글1개 조회하기 === //
	@Override
	public BoardVO_OHJ getView(Map<String, String> paraMap) {
		BoardVO_OHJ boardvo = sqlsession.selectOne("ohhj.getView", paraMap);
		return boardvo;
	}

	// === &66. 글조회수 1증가 하기 === //
	@Override
	public void addReadCount(String boardSeq) {
		sqlsession.update("ohhj.addReadCount", boardSeq);
	}


	// === &74. 1개글 수정하기 === //
	@Override
	public int boardEdit(BoardVO_OHJ boardvo) {
		int n = sqlsession.update("ohhj.boardEdit", boardvo);
		return n;
	}


	// === &79. 1개글 삭제하기 === //
	@Override
	public int boardDel(Map<String, String> paraMap) {
		int n = sqlsession.delete("ohhj.boardDel", paraMap);
		return n;
	}

	
	// === &86. 댓글쓰기(tbl_boardComment 테이블에 insert) === //
	@Override
	public int boardCommentWrite(BoardCommentVO_OHJ commentvo) {
		int n = sqlsession.insert("ohhj.boardCommentWrite", commentvo);
		return n;
	}
	// === &87. tbl_board 테이블에 commentCount 컬럼이 1증가(update) === //
	@Override
	public int updateCommentCount(String fk_boardSeq) {
		int m = sqlsession.update("ohhj.updateCommentCount", fk_boardSeq);
		return m;
	}


	// === &92. 원게시물에 딸린 댓글들을 조회해오는 것 === //
	@Override
	public List<BoardCommentVO_OHJ> getCommentList(String fk_boardSeq) {
		List<BoardCommentVO_OHJ> commentList = sqlsession.selectList("ohhj.getCommentList", fk_boardSeq);
		return commentList;
	}


	// === &104. 페이징 처리를 안한, 검색어가 있는 전체 글목록 보여주기 === //
	@Override
	public List<BoardVO_OHJ> boardListSearch(Map<String, String> paraMap) {
		List<BoardVO_OHJ> boardList = sqlsession.selectList("ohhj.boardListSearch", paraMap);
		return boardList;
	}


	// === &116. 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다. === //
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int totalCount = sqlsession.selectOne("ohhj.getTotalCount", paraMap);
		return totalCount;
	}


	// === &119. 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것) === //
	@Override
	public List<BoardVO_OHJ> boardListSearchWithPaging(Map<String, String> paraMap) {
		List<BoardVO_OHJ> boardList = sqlsession.selectList("ohhj.boardListSearchWithPaging", paraMap);
		return boardList;
	}

	
	
	
	// === 검색어의 빈도를 나타내기 위해, 검색어가 있으면 '검색키워드 기록'테이블에 insert하기 === //
	@Override
	public void registerSearchKeyword(String searchWord) {
		sqlsession.insert("ohhj.registerSearchKeyword", searchWord);
	}

	// === '검색어키워드기록'을 가져와서 하나의 문자열로 만들기 === //
	@Override
	public List<String> getKeywordHistory() {
		List<String> keywordList = sqlsession.selectList("ohhj.getKeywordHistory");
		return keywordList;
	}

	
	
	// === &158. 글쓰기(파일첨부가 있는 글쓰기) === //
	@Override
	public int boardWrite_withFile(BoardVO_OHJ boardvo) {
		int n = sqlsession.insert("ohhj.boardWrite_withFile", boardvo);
		return n;
	}
	

	
	



	

	
	
	
	
	
	
}
