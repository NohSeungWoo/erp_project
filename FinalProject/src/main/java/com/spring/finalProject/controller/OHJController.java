package com.spring.finalProject.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.spring.finalProject.common.FileManager;
import com.spring.board.model.BoardCategoryVO_OHJ;
import com.spring.board.model.BoardCommentVO_OHJ;
import com.spring.board.model.BoardVO_OHJ;
import com.spring.finalProject.common.MyUtil_KGH;
import com.spring.finalProject.model.EmployeeVO_KGH;
import com.spring.finalProject.service.InterOHJService;

//==== #30. 컨트롤러 선언 ====
@Component
@Controller
public class OHJController {
	
	@RequestMapping(value="/test1.gw")
	public String test1(HttpServletRequest request) {
		
		String name = "이순신"; //데베에서 얻어온거라고 가정함.
		request.setAttribute("name", name);
		
		return "test1"; //뷰단을 넘김.
	//  /WEB-INF/views/test1.jsp 페이지를 만들어야 한다.
	}
	
	@RequestMapping(value="/test4.gw")
	public String test4(HttpServletRequest request) {
		
		String name = "Lee SunSin";
		request.setAttribute("name", name);
		
		return "test4.tiles1";
	//  /WEB-INF/views/tiles1/test4.jsp 파일을 생성한다.
	}
	
	@RequestMapping(value="/test6.gw")
	public String test6(HttpServletRequest request) {
		
		String name = "Eom JungHwa";
		request.setAttribute("name", name);
		
		return "test6.tiles2";
	//  /WEB-INF/views/tiles2/test6.jsp 파일을 생성한다.
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	// === #35. 의존객체 주입하기(DI: Dependency Injection) ===
	// ※ 의존객체주입(DI : Dependency Injection) 
	//  ==> 스프링 프레임워크는 객체를 관리해주는 컨테이너를 제공해주고 있다.
	//      스프링 컨테이너는 bean으로 등록되어진 BoardController 클래스 객체가 사용되어질때, 
	//      BoardController 클래스의 인스턴스 객체변수(의존객체)인 BoardService service 에 
	//      자동적으로 bean 으로 등록되어 생성되어진 BoardService service 객체를  
	//      BoardController 클래스의 인스턴스 변수 객체로 사용되어지게끔 넣어주는 것을 의존객체주입(DI : Dependency Injection)이라고 부른다. 
	//      이것이 바로 IoC(Inversion of Control == 제어의 역전) 인 것이다.
	//      즉, 개발자가 인스턴스 변수 객체를 필요에 의해 생성해주던 것에서 탈피하여 스프링은 컨테이너에 객체를 담아 두고, 
	//      필요할 때에 컨테이너로부터 객체를 가져와 사용할 수 있도록 하고 있다. 
	//      스프링은 객체의 생성 및 생명주기를 관리할 수 있는 기능을 제공하고 있으므로, 더이상 개발자에 의해 객체를 생성 및 소멸하도록 하지 않고
	//      객체 생성 및 관리를 스프링 프레임워크가 가지고 있는 객체 관리기능을 사용하므로 Inversion of Control == 제어의 역전 이라고 부른다.  
	//      그래서 스프링 컨테이너를 IoC 컨테이너라고도 부른다.
	
	//  ★IOC(Inversion of Control) 란 ?
	//  ==> 스프링 프레임워크는 사용하고자 하는 객체를 빈형태로 이미 만들어 두고서 컨테이너(Container)에 넣어둔후
	//      필요한 객체사용시 컨테이너(Container)에서 꺼내어 사용하도록 되어있다.
	//      이와 같이 객체 생성 및 소멸에 대한 제어권을 개발자가 하는것이 아니라 스프링 Container 가 하게됨으로써 
	//      객체에 대한 제어역할이 개발자에게서 스프링 Container로 넘어가게 됨을 뜻하는 의미가 제어의 역전 
	//      즉, IOC(Inversion of Control) 이라고 부른다.
	
	
	//  === 느슨한 결합 ===
	//      스프링 컨테이너가 BoardController 클래스 객체에서 BoardService 클래스 객체를 사용할 수 있도록 
	//      만들어주는 것을 "느슨한 결합" 이라고 부른다.
	//      느스한 결합은 BoardController 객체가 메모리에서 삭제되더라도 BoardService service 객체는 메모리에서 동시에 삭제되는 것이 아니라 남아 있다.
	
	// ===> 단단한 결합(개발자가 인스턴스 변수 객체를 필요에 의해서 생성해주던 것)
	// private InterBoardService service = new BoardService(); 
	// ===> BoardController 객체가 메모리에서 삭제 되어지면  BoardService service 객체는 멤버변수(필드)이므로 메모리에서 자동적으로 삭제되어진다.
	
	@Autowired
	private InterOHJService service;
	// Type에 따라 알아서 Bean 을 주입해준다.
	
	/////////////////////////////////////////////////////////////////////////////////
						// 기본셋팅 끝이다. 여기서부터 개발 시작이다! //
	/////////////////////////////////////////////////////////////////////////////////
	
	// === &155. 파일업로드 및 다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) ===
	@Autowired
	private FileManager fileManager;
	// Type에 따라 알아서 Bean 을 주입해준다.
	
	
	// === 게시판 만들기 폼페이지 요청 === //
	@RequestMapping(value="/makeBCategory.gw")
	public ModelAndView makeBCategory(ModelAndView mav) { // 뿌잉 : requiredLogin추가하기!
		
	//	getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		mav.setViewName("board/makeBCategory.tiles_OHJ");
		
		return mav;
	}
	
	
	// === 카테고리명 중복체크하기(Ajax 로 처리) === //
	@ResponseBody
	@RequestMapping(value="/cNameDuplicateCheck.gw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String cNameDuplicateCheck(HttpServletRequest request) {
		
		String bCategoryName = request.getParameter("bCategoryName");
	//	System.out.println(">>> 확인용 bCategoryName => " + bCategoryName);
		
		boolean isExists = service.cNameDuplicateCheck(bCategoryName);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isExists", isExists); // {"isExists":true} 또는 {"isExists":false}
			
		return jsonObj.toString();
	}
	
	
	// === 게시판 만들기 완료 요청 === //
	@RequestMapping(value="/makeBCategoryEnd.gw", method= {RequestMethod.POST})
	public ModelAndView makeBCategoryEnd(ModelAndView mav, BoardCategoryVO_OHJ bCategoryvo, HttpServletRequest request) {
		
	//	System.out.println("체크된 라디오 값만 넘어오는지 확인용 userType : " + bCategoryvo.getUserType());
		// 체크된 라디오 값만 넘어오는지 확인용 userType : secret
		
		int n = service.makeBCategory(bCategoryvo);
		
		String message = "";
		
		if(n==1) {
			message = bCategoryvo.getbCategoryName() + "이(가) 생성되었습니다.";
		}
		else {
			message = bCategoryvo.getbCategoryName() + "이(가) 생성이 실패되었습니다.";
		}
		
		String loc = request.getContextPath()+"/recentList.gw";
		
		mav.addObject("message", message);
		mav.addObject("loc", loc);
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 게시판 종류 목록 가져오기(Ajax 로 처리) === //
	@ResponseBody
	@RequestMapping(value="/viewCategoryList.gw", produces="text/plain;charset=UTF-8")
	public String viewCategoryList() {
		
		List<BoardCategoryVO_OHJ> bcategoryList = service.viewCategoryList();
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(bcategoryList != null) {
			for(BoardCategoryVO_OHJ bCategoryvo : bcategoryList) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("bCategorySeq", bCategoryvo.getbCategorySeq());
				jsonObj.put("bCategoryName", bCategoryvo.getbCategoryName());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	
	
	
	
	
	
	
	// === &51. 게시판 글쓰기 폼페이지 요청 === //
	@RequestMapping(value="/boardWrite.gw")
	public ModelAndView requiredLogin_boardWrite(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// == 카테고리 목록 가져오기 시작 == //
		List<BoardCategoryVO_OHJ> bcategoryList = service.viewCategoryList();
		mav.addObject("bcategoryList", bcategoryList);
		// == 카테고리 목록 가져오기 끝 == //
		
		
		mav.setViewName("board/boardWrite.tiles_OHJ");
		//  /WEB-INF/views/tiles_OHJ/board/boardWrite.jsp 파일을 생성한다.
		
		return mav;
	}
	
	
	// === &54. 게시판 글쓰기 완료 요청 === //
	@RequestMapping(value="/boardWriteEnd.gw", method= {RequestMethod.POST})
//	public ModelAndView boardWriteEnd(ModelAndView mav, BoardVO_OHJ boardvo) { // => 파일첨부가 안된 글쓰기
	public ModelAndView boardWriteEnd(ModelAndView mav, BoardVO_OHJ boardvo, MultipartHttpServletRequest mrequest) { // => 파일첨부된 글쓰기
	/*	
		// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // -> 스마트에디터에서 자동으로 해줌
		String content = boardvo.getContent();
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("\r\n", "<br>"); // 입력한 엔터는 <br>처리하기
		boardvo.setContent(content);
	*/	
		
		
	/*
		=== &151. 파일첨부가 된 글쓰기 이므로 
			먼저 위의 public ModelAndView boardWriteEnd(ModelAndView mav, BoardVO_OHJ boardvo) 을
			주석처리 한 이후에 아래와 같이 한다.
			MultipartHttpServletRequest mrequest 를 사용하기 위해서는
			먼저, /FinalProject/src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml 에서
			&21. 파일업로드 및 파일다운로드에 필요한 의존객체 설정하기 를 해두어야 한다.
	*/
		
	/*
		    웹페이지에 요청 form이 enctype="multipart/form-data" 으로 되어있어서 Multipart 요청(파일처리 요청)이 들어올때 
		    컨트롤러에서는 HttpServletRequest 대신 MultipartHttpServletRequest 인터페이스를 사용해야 한다.
		  MultipartHttpServletRequest 인터페이스는 HttpServletRequest 인터페이스와  MultipartRequest 인터페이스를 상속받고있다.
		    즉, 웹 요청 정보를 얻기 위한 getParameter()와 같은 메소드와 Multipart(파일처리) 관련 메소드를 모두 사용가능하다.     
	*/
		
		// === 사용자가 쓴 글에 파일이 첨부되어 있는 것인지, 아니면 파일첨부가 안된 것인지 구분을 지어 주어야 한다. ===
		// === &153. !!! 첨부파일이 있는 경우 작업 시작 !!! ===
		MultipartFile attach = boardvo.getAttach();
		
		if( !attach.isEmpty() ) {
			// attach(첨부파일)가 비어있지 않으면(즉, 첨부파일이 있는 경우라면)
			
			/*
				1. 사용자가 보낸 첨부파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다.
				>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
					우리는 WAS의 webapp/resources/files 라는 폴더로 지정해준다.
					조심할 것은 Package Explorer 에서  files 라는 폴더를 만드는 것이 아니다. (해도 상관은 없지만, 어짜피 .metadate에 자동으로 복제된다.)
			*/
			// WAS의 webapp 의 절대경로를 알아와야 한다.
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
		//	System.out.println("~~~~ 확인용 webapp 의 절대경로 => " + root);
			// ~~~~ 확인용 webapp 의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\
			
			String path = root + "resources" + File.separator + "files";
			/* File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.
			       운영체제가 Windows 이라면 File.separator 는  "\" 이고,
			       운영체제가 UNIX, Linux 이라면  File.separator 는 "/" 이다. 
			*/
			
			// path 는 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
		//	System.out.println("~~~~ 확인용 path => " + path);
			// ~~~~ 확인용 path => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files
			
			
			/*
				2. 파일첨부를 위한 변수의 설정 및 값을 초기화 한 후 파일올리기
			*/
			String newFileName = "";
			// WAS(톰캣)의 디스크에 저장될 파일명 	  ex)2021110809271535243254235235234.png
			
			byte[] bytes = null; // 파일첨부의 InputStream, OutputStream은 1바이트 기반이다.
			// 첨부파일의 내용물을 담는 것
			
			long fileSize = 0;
			// 첨부파일의 크기
			
			try {
				bytes = attach.getBytes();
				// 첨부파일의 내용물을 읽어오는 것
				
				newFileName = fileManager.doFileUpload(bytes, attach.getOriginalFilename(), path); // 파라미터 : 내용물, 확장자 알아오기위한 첨부파일명, 업로드되어질경로명
				// 첨부되어진 파일을 업로드 하도록 하는 것이다. 
	            // attach.getOriginalFilename() 은 첨부파일의 파일명(예: 강아지.png)이다.
				
			//	System.out.println(">>> 확인용 newFileName => " + newFileName);
				// >>> 확인용 newFileName => 2021110811273450592448100700.jpg
			
			/*
				3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
			*/
				boardvo.setFileName(newFileName);
				// WAS(톰캣)에 저장될 파일명(2021110811273450592448100700.jpg)
				
				boardvo.setOrgFilename(attach.getOriginalFilename());
				// 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
				// 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		// === !!! 첨부파일이 있는 경우 작업 끝 !!! ===
		
	//	int n = service.boardWrite(boardvo); // <== 파일첨부가 없는 글쓰기
		
		
		// === &156. 파일첨부가 있는 글쓰기 또는 파일첨부가 없는 글쓰기로 나뉘어서 service 호출하기 === //
		// 먼저 위의 int n = service.boardWrite(boardvo); 부분을 주석처리 하고서 아래와 같이 한다.
		int n = 0;
		
		if( attach.isEmpty() ) {
			// 첨부파일이 없는 경우라면
			n = service.boardWrite(boardvo);
		}
		
		else {
			// 첨부파일이 있는 경우라면
			n = service.boardWrite_withFile(boardvo);
		}
		
		
		mav.setViewName("redirect:/recentList.gw");
		//	/list.gw 페이지로 redirect(페이지이동)해라는 말이다.
		
		return mav;
	}
	
	
	// === &58. 글목록 보기 페이지 요청(최근 게시물) === //
	@RequestMapping(value="/recentList.gw")
//	public ModelAndView recentList(ModelAndView mav, HttpServletRequest request) { /* 뿌잉) 그룹웨어이므로 requiredLogin_을 추가해야한다. */
	public ModelAndView requiredLogin_recentList(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// == 카테고리 목록 가져오기 시작 == //
		List<BoardCategoryVO_OHJ> bcategoryList = service.viewCategoryList();
		mav.addObject("bcategoryList", bcategoryList);
		// == 카테고리 목록 가져오기 끝 == //
		
		
		// == 글목록 가져오기 == //
		List<BoardVO_OHJ> boardList = null;
		
		// == 페이징 처리를 안한, 검색어가 없는, 전체 글목록 보여주기 == //
	//	boardList = service.boardListNoSearch();
/*		
		// === &102. 페이징 처리를 안한, 검색어가 있는, 전체 글목록 보여주기 시작 === //
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		System.out.println("확인용 fromDate : " + fromDate);
		System.out.println("확인용 toDate : " + toDate);
		System.out.println("확인용 bCategory : " + bCategory);
		System.out.println("확인용 searchType : " + searchType);
		System.out.println("확인용 searchWord : " + searchWord);
		
		if(fromDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			fromDate = ""; // 2021-08-23 // '숫자개월수만큼 빼준 날짜인 add_months(sysdate,-3) 로 검색'하려했으나 유효성검사 했으므로 안해도된다.
		}
		if(toDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			toDate = "";   // 2021-11-23 // 'sysdate 로 검색'하려했으나 유효성검사 했으므로 안해도된다.
		}
		////////////////////////////////////////////////////
		if(bCategory == null) { // 맨처음 목록보기를 통해 들어가는 경우
			bCategory = "0";
		}
		if( !"0".equals(bCategory)&&!"1".equals(bCategory)&&!"2".equals(bCategory)&&!"3".equals(bCategory) ) { // 유저가 게시판종류를 장난친 경우
			bCategory = "";
		}
		////////////////////////////////////////////////////
		if(searchType == null || (!"subject".equals(searchType)&&!"name".equals(searchType))) { // 맨처음 목록보기를 통해 들어가는 경우, 유저가 장난친 경우
			searchType = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) { // 맨처음 목록보기를 통해 들어가는 경우, 검색어자체가 ⓐ없거나 ⓑ있는데 공백인 경우
			searchWord = "";
		}
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		boardList = service.boardListSearch(paraMap);
		
		// 아래는 검색시 검색조건 및 값들을 유지시키기 위한 것임.
		if(!"".equals(fromDate) && !"".equals(toDate)) {
			mav.addObject("fromDate", fromDate);
			mav.addObject("toDate", toDate);
		}
//		if(!"0".equals(bCategory) && !"".equals(bCategory)) {
			mav.addObject("bCategory", bCategory); // 주소창에 게시판종류를 장난친경우를 recentList.jsp에서 처리하기위해, if의 조건없이 넘김.
//		}
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("searchType", searchType);
			mav.addObject("searchWord", searchWord);
		}
		// === &102. 페이징 처리를 안한, 검색어가 있는, 전체 글목록 보여주기 끝 === //
*/		
		/////////////////////////////////////////////////////////////////
		/* === &69. 글조회수증가는 반드시 목록보기에 와서 해당 글제목을 클릭했을 경우에만 증가되고,
			웹브라우저에서 새로고침(F5)을 했을 경우에는 증가가 되지 않도록 해야 한다.
			이것을 하기 위해서는 session 을 사용하여 처리하면 된다. === */
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes"); // 조회수 증가를 허락하겠다.
		/*	
			session에 readCountPermission의 value값은 "yes"이다.
			이 값은 웹브라우저에서 주소창에 "/recentList.gw" 이라고 입력해야만 얻어올 수 있다.
		*/
		/////////////////////////////////////////////////////////////////
		
		// === &114. 페이징 처리를 한, 검색어가 있는, 전체 글목록 보여주기 시작 === //
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
	/*	
		System.out.println("확인용 fromDate : " + fromDate);
		System.out.println("확인용 toDate : " + toDate);
		System.out.println("확인용 bCategory : " + bCategory);
		System.out.println("확인용 searchType : " + searchType);
		System.out.println("확인용 searchWord : " + searchWord);
	*/	
		
		if(fromDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			// 2021-08-23 // '숫자개월수만큼 빼준 날짜인 add_months(sysdate,-3) 로 검색'하려했으나 유효성검사 해서 값이 넘어왔다.
			
			// 3달전 날짜 구하기
			fromDate = getDate("threeMonthsAgo");
		}
		if(toDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			// 2021-11-23 // 'sysdate 로 검색'하려했으나 유효성검사 해서 값이 넘어왔다.
			
			// 오늘 날짜 구하기
			toDate = getDate("today");
		}
		////////////////////////////////////////////////////
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		// === 최근 게시물이 아니라 sideinfo.jsp에서 클릭한 게시물을 보여주는 용 START ===
		String bCategorySeq = request.getParameter("bCategorySeq");
		// 최근 게시물은 bCategorySeq 이 null이고, bCategory는 null일수도 null이 아닐수도있는데
		// 									bCategory가 null이면        -> 맨 처음 목록보기로 들어가는 경우. 이 경우에는 bCategory가 0이다.
		// 									bCategory가 null이 아니면 -> 최근게시물이나 기타게시물에서 조회한 경우. 이 경우에는 bCategory가 넘어온 값이다. 
		// 공지사항, 자유게시판, 건의사항은 해당하는 bCategorySeq 가 넘어온다. -> 이 때 bCategory는 null로 넘어오는데, bCategory가 bCategorySeq이다.
		if(bCategorySeq == null && bCategory == null) {
			bCategory = "0";
		}
		if(bCategorySeq == null && bCategory != null) {
			// bCategory는 넘어온 그 자체이다.
		}
		if(bCategorySeq != null && bCategory == null) {
			bCategory = bCategorySeq;
		}
		// === 최근 게시물이 아니라 sideinfo.jsp에서 클릭한 게시물을 보여주는 용 END ===
		/////////////////////////////////////////////////////////////////////////////////////////////
	/*	
		if(bCategory == null) { // 맨처음 목록보기를 통해 들어가는 경우
			bCategory = "0";
		}
	*/	
	/*	
		if( !"0".equals(bCategory)&&!"1".equals(bCategory)&&!"2".equals(bCategory)&&!"3".equals(bCategory) ) { // 유저가 게시판종류를 장난친 경우
			bCategory = "";
		}
	*/	
		boolean flag = false; // 글목록보기로 넘어온 id가 bCategory인 select태그 속 bCategorySeq가, 실제 존재하는 것인지아닌지 여부
		for(BoardCategoryVO_OHJ bcvo : bcategoryList) {
			if(bcvo.getbCategorySeq().equals(bCategory)) {
				flag = true;
				break;// 반복문 종료
			}
		}
		if(!"0".equals(bCategory) && !flag) { // select태그에서 전체를 선택한 것도 아니고, 카테고리분류에 실제 존재하는것도 아니라면
			bCategory = "";
		}
		////////////////////////////////////////////////////
		if(searchType == null || (!"subject".equals(searchType)&&!"name".equals(searchType))) { // 맨처음 목록보기를 통해 들어가는 경우, 유저가 장난친 경우
			searchType = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) { // 맨처음 목록보기를 통해 들어가는 경우, 검색어자체가 ⓐ없거나 ⓑ있는데 공백인 경우
			searchWord = "";
		}
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// ★총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.
		int totalCount = 0; 		// 총 게시물 건수
		int sizePerPage = 5; 		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; 	// 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0; 			// 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바에 쓰임)
		
		int startRno = 0; 			// 시작 행번호
		int endRno = 0; 			// 끝 행번호
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getTotalCount(paraMap);
	//	System.out.println("~~~~ 확인용 totalCount : " + totalCount);
		
		// 만약에 총 게시물 건수(totalCount)가 27개 이라면
		// 27/5 = 5.xx 이므로
		// 총 페이지수(totalPage)는 6개가 되어야 한다.
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage);
		// (double)27/5 ==> 5.4 ==> Math.ceil(5.4) ==> 6.0 ==> (int)6.0 ==> 6
		
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) { // get방식이라 유저가 장난친 경우("-321", "2136351")
					currentShowPageNo = 1;
				}
			} catch(NumberFormatException e) { // get방식이라 유저가 장난친 경우("하하호호")
				currentShowPageNo = 1;
			}
		}
		
		// **** 가져올 게시글의 범위를 구한다.(공식임!!!) **** 
	    /*
	           currentShowPageNo      startRno     endRno
	          --------------------------------------------
	               1 page        ===>     1           5
	               2 page        ===>     6          10
	               3 page        ===>    11          15
	               4 page        ===>    16          20
	               ......                ...         ...
	    */
		
		startRno = ( (currentShowPageNo-1) * sizePerPage ) + 1;
		endRno = startRno + sizePerPage - 1 ;
		
		paraMap.put("startRno", String.valueOf(startRno)); // 사칙연산으로 계산했으니 int인 것을 Map<String,String>에 맞게 string타입으로 바꿔줌.
		paraMap.put("endRno", String.valueOf(endRno));
		
		boardList = service.boardListSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것)
		
		
		// 아래는 검색시 검색조건 및 값들을 유지시키기 위한 것임.
		if(!"".equals(fromDate) && !"".equals(toDate)) {
			mav.addObject("fromDate", fromDate);
			mav.addObject("toDate", toDate);
		}
//		if(!"0".equals(bCategory) && !"".equals(bCategory)) {
			mav.addObject("bCategory", bCategory); // 주소창에 게시판종류를 장난친경우를 recentList.jsp에서 처리하기위해, if의 조건없이 넘김.
//		}
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("searchType", searchType);
			mav.addObject("searchWord", searchWord);
		}
		
		
		// === &121. 페이지바 만들기 === //
		int blockSize = 10;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수 이다.
	    /*
	                       1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
	         [맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
	         [맨처음][이전]  21 22 23
	    */
		
		int loop = 1;
		/*
        	loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		*/
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	    // *** !! 공식이다. !! *** //
		
		/*
	       1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은 1 이다.
	       11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.
	       21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
	       
	       currentShowPageNo         pageNo
	      ----------------------------------
	            1                      1 = ((1 - 1)/10) * 10 + 1
	            2                      1 = ((2 - 1)/10) * 10 + 1
	            3                      1 = ((3 - 1)/10) * 10 + 1
	            4                      1
	            5                      1
	            6                      1
	            7                      1 
	            8                      1
	            9                      1
	            10                     1 = ((10 - 1)/10) * 10 + 1
	           
	            11                    11 = ((11 - 1)/10) * 10 + 1
	            12                    11 = ((12 - 1)/10) * 10 + 1
	            13                    11 = ((13 - 1)/10) * 10 + 1
	            14                    11
	            15                    11
	            16                    11
	            17                    11
	            18                    11 
	            19                    11 
	            20                    11 = ((20 - 1)/10) * 10 + 1
	            
	            21                    21 = ((21 - 1)/10) * 10 + 1
	            22                    21 = ((22 - 1)/10) * 10 + 1
	            23                    21 = ((23 - 1)/10) * 10 + 1
	            ..                    ..
	            29                    21
	            30                    21 = ((30 - 1)/10) * 10 + 1
	    */
		
		String pageBar = "<ul class='pagination justify-content-center' style='margin-top: 50px;'>";
		String url = "recentList.gw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>"; // span태그의 text-dark를 처리안하니 기본으로 파란글씨가 나온다.
			pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage) ){ // !(while문의 탈출조건)
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li class='page-item active'><a class='page-link'>"+pageNo+"<a></li>";
			}
			else {
				pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'><span class='text-dark'>"+pageNo+"</span></a></li>";
			}
			
			loop++;
			pageNo++;
			
		}// end of while-----------------------------
		
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li class='page-item'><a class='page-link' href='"+url+"?fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		
		/* === &123. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후, 
				사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
				현재 페이지 주소를 뷰단으로 넘겨준다. === */
		String gobackURL = MyUtil_KGH.getCurrentURL(request);	
	//	System.out.println("~~~~ 확인용 recentList.gw의 gobackURL : " + gobackURL);
		/*
			~~~~ 확인용 recentList.gw의 gobackURL : /recentList.gw																							// 처음 목록보기로 들어간 경우
			~~~~ 확인용 recentList.gw의 gobackURL : /recentList.gw?fromDate=2021-08-26&toDate=2021-11-26&bCategory=0&searchType=subject&searchWord=			// 바로 검색버튼 누른 경우
			~~~~ 확인용 recentList.gw의 gobackURL : /recentList.gw?fromDate=2021-08-18&toDate=2021-11-24&bCategory=1&searchType=name&searchWord=%EC%9C%A0	// 모든 조건 검색한 누른 경우
		*/
		
		mav.addObject("gobackURL", gobackURL);
		
		// === 페이징 처리를 한, 검색어가 있는, 전체 글목록 보여주기 끝 === //
		
		
		// === 하이차트 - 검색어의 빈도를 나타내기 위해, 검색어가 있으면 테이블에 담는 용도 START === //
		if(!"".equals(searchWord)) { // 검색어가 있는 경우
			// tbl_keywordHistory테이블의 keyword컬럼에 searchWord값을 insert한다.
			service.registerSearchKeyword(searchWord);
		}
		// === 하이차트 - 검색어의 빈도를 나타내기 위해, 검색어가 있으면 테이블에 담는 용도 END === //
		
		
		
		mav.addObject("totalCount", totalCount); // 게시글 총 몇 건인지 보여주는 용도
		
		mav.addObject("boardList", boardList);
		mav.setViewName("board/recentList.tiles_OHJ");
		//  /WEB-INF/views/tiles_OHJ/board/recentList.jsp 파일을 생성한다.
		
		return mav;
	}
	
	// === &62. 글1개를 보여주는 페이지 요청 === //
	@RequestMapping(value="/boardView.gw")
	public ModelAndView boardView(ModelAndView mav, HttpServletRequest request) { /* 뿌잉) 그룹웨어이므로 requiredLogin_을 추가해야한다. */
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 조회하고자 하는 글번호 받아오기
		String boardSeq = request.getParameter("boardSeq");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("boardSeq", boardSeq);
		
		
		// 글목록에서 검색되어진 글내용일 경우
		// 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글, 다음글이 나오도록 하기 위한 것이다. 
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		// get방식으로 장난치게 되면 아래의 boardvo의 결과가 조회되지 않으므로, 해당하는 글이 없다고 알려준다. 따라서 getParameter한 값을 형식에 맞게 처리는 안해줘도된다. 그냥 null인지 아닌지만 판별한다. 글이 보여지지 않으므로 gobackURL이 이상하게 갈 것에 대한 걱정은 안해도 된다.
		if(fromDate == null) {
			fromDate = getDate("threeMonthsAgo");
		}
		if(toDate == null) {
			toDate = getDate("today");
		}
		if(bCategory == null) {
			bCategory = "0";
		}
		if(searchType == null) {
			searchType = "";
		}
		if(searchWord == null) {
			searchWord = "";
		}
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		mav.addObject("fromDate", fromDate);
		mav.addObject("toDate", toDate);
		mav.addObject("bCategory", bCategory);
		mav.addObject("searchType", searchType);
		mav.addObject("searchWord", searchWord);
		
		/////////////////////////////////////////////////////////
		// === &125. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		//           사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해
		//           현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
	//	System.out.println("~~~~ 확인용 boardView.gw의 gobackURL => " + gobackURL);
		/*
			~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw
			~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26&toDate=2021-11-26&bCategory=3&searchType=name&searchWord=%EC%9C%A0
			~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26&toDate=2021-11-26&bCategory=3&searchType=name&searchWord=%EC%9C%A0&currentShowPageNo=10
			
			// &126-4의 확인용
			~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26 toDate=2021-11-24 bCategory=0 searchType=subject searchWord=글 currentShowPageNo=2
		*/
	/*	
		// 이 부분은 그냥 boardView.jsp에서 검색된목록으로가기 버튼을 클릭할때 replace하도록 함.
		// &126-4(강사님은 없음). : 2021-11-05 블로그 참조해서 gobackURL 페이지 주소 올바르게 하기
		if(gobackURL != null && gobackURL.contains(" ")) { // gobackURL에 공백(" ")이 포함되었더라면
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위해서 한 것임.
		//	System.out.println("~~~~ 확인용 gobackURL => " + gobackURL);
			// ~~~~ 확인용 gobackURL => /recentList.gw?fromDate=2021-08-26&toDate=2021-11-24&bCategory=0&searchType=subject&searchWord=글&currentShowPageNo=2
		}
	*/	
		mav.addObject("gobackURL", gobackURL);
		/////////////////////////////////////////////////////////		
		
		BoardVO_OHJ boardvo = null;
		
		try {
			
			Integer.parseInt(boardSeq); // "하하하호호호", "1", "1324654"
			
			HttpSession session = request.getSession();
			EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
			
			String login_employeeId = null;
			
			if(loginuser != null) { // 로그인된 경우
				login_employeeId = loginuser.getEmployeeid();
				// login_employeeId 는 로그인 되어진 사용자의 userid 이다.
			}
			
			/* === &68. 글1개를 보여주는 페이지 요청은 select와 함께
				DML문(지금은 글조회수 증가인 update문)이 포함되어져 있다.
				이럴경우 웹브라우저에서 페이지 새로고침(F5)을 했을때 DML문이 실행되어 매번 글조회수 증가가 발생한다.
				그래서 우리는 웹브라우저에서 페이지 새로고침(F5)을 했을때는 단순히 select만 해주고, 
				DML문(지금은 글조회수 증가인 update문)은 실행하지 않도록 해주어야한다. ===
			*/
			
			// 먼저 &69에서 세션을 저장해야함.
			if("yes".equals(session.getAttribute("readCountPermission"))) {
				// 글목록보기를 클릭한 다음에 특정글을 조회해온 경우이다.
				
				boardvo = service.getView(paraMap, login_employeeId);
				// 글조회수 증가와 함께 글1개를 조회
				
				session.removeAttribute("readCountPermission");
				// session에 저장된 readCountPermission 삭제함.
			}
			else {
				// 웹브라우저에서 새로고침(F5)을 클릭한 경우이다.
				
				boardvo = service.getViewWithNoAddCount(paraMap);
				// 글조회수 증가는 없고 단순히 글1개 조회
			}
			
			
		} catch (NumberFormatException e) {
			// boardvo는 null인 상태 그대로 뷰단으로 넘어간다.
		}
		
		mav.addObject("boardvo", boardvo);
		
		mav.setViewName("board/boardView.tiles_OHJ");
		//  /WEB-INF/views/tiles_OHJ/board/boardView.jsp 파일을 생성한다.
		
		return mav;
	}
	
	
	
	// === &71. 글수정 페이지 요청 === //
	@RequestMapping(value="/boardEdit.gw")
	public ModelAndView requiredLogin_boardEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // AOP에 gobackURL저장하므로 원래는 이 부분 안했는데, 로그인 실패시에 gobackURL을 저장해야하므로 이 부분을 꼭 해줘야한다! // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		
		// == 글1개 조회와 동일함 시작 ============================================
		// 수정하고자 하는 글번호 받아오기
		String boardSeq = request.getParameter("boardSeq");
		
		// 수정완료시 페이지 돌아갈때,
		// 글목록에서 검색되어진 글내용일 경우
		// 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글, 다음글이 나오도록 하기 위한 것이다. 
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		// 수정해야할 글1개 내용 가져오기
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("boardSeq", boardSeq);
		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		mav.addObject("fromDate", fromDate);
		mav.addObject("toDate", toDate);
		mav.addObject("bCategory", bCategory);
		mav.addObject("searchType", searchType);
		mav.addObject("searchWord", searchWord);
		
		/////////////////////////////////////////////////////////
		// 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		// 사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해
		// 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
	//	System.out.println("~~~~ 확인용 boardView.gw의 gobackURL => " + gobackURL);
		// ~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26 toDate=2021-11-24 bCategory=0 searchType=subject searchWord=글 currentShowPageNo=2
		
		mav.addObject("gobackURL", gobackURL);
		/////////////////////////////////////////////////////////	
		// == 글1개 조회와 동일함 끝 ============================================
		
		BoardVO_OHJ boardvo = service.getViewWithNoAddCount(paraMap);
	/*	
		// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 해제하기 **** // -> 스마트에디터에서 자동으로 해줌 
		String content = boardvo.getContent();
		content = content.replaceAll("<br>", "\r\n"); // <br>은 엔터로 처리하기
		boardvo.setContent(content);
	*/	
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		
		if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeId()) ) {
			String message = "다른 사용자의 글은 수정이 불가합니다.";
			String loc = request.getContextPath()+"/boardView.gw?boardSeq="+boardvo.getBoardSeq()+"&fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&gobackURL="+gobackURL; // 로그인실패 후 성공했을때 history.back()하면 로그인창이 뜨므로, 글1개 보기 페이지로 이동하도록 바꿔줘야함!
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);
			mav.setViewName("msg");
		}
		else {
			// 자신의 글을 수정하는 경우 : 가져온 글1개를 글수정 View단에 보내준다.
			mav.addObject("boardvo", boardvo);
			mav.setViewName("board/boardEdit.tiles_OHJ");
		}
		
		return mav;
	}
	
	
	// === &72. 글수정 페이지 완료하기 === //
	@RequestMapping(value="/boardEditEnd.gw", method= {RequestMethod.POST})
	public ModelAndView boardEditEnd(ModelAndView mav, BoardVO_OHJ boardvo, HttpServletRequest request) {
	/*	
		// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // -> 스마트에디터에서 자동으로 해줌 
		String content = boardvo.getContent();
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		content = content.replaceAll("\r\n", "<br>"); // 입력한 엔터는 <br>처리하기
		boardvo.setContent(content);
	*/	
		int n = service.boardEdit(boardvo);
		
		if(n==1) {
			mav.addObject("message", "글 수정 성공!!");
		}
		else {
			mav.addObject("message", "글 수정 실패..");
		}
		
		// == 글1개 조회와 동일함 시작 ============================================
		// 수정완료시 페이지 돌아갈때,
		// 글목록에서 검색되어진 글내용일 경우
		// 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글, 다음글이 나오도록 하기 위한 것이다. 
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		/////////////////////////////////////////////////////////
		// 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		// 사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해
		// 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
	//	System.out.println("~~~~ 확인용 boardView.gw의 gobackURL => " + gobackURL);
		// ~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26 toDate=2021-11-24 bCategory=0 searchType=subject searchWord=글 currentShowPageNo=2
		
		/////////////////////////////////////////////////////////	
		// == 글1개 조회와 동일함 끝 ============================================
		
		mav.addObject("loc", request.getContextPath()+"/boardView.gw?boardSeq="+boardvo.getBoardSeq()+"&fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&gobackURL="+gobackURL);		
		
		mav.setViewName("msg");
		
		return mav;
	}	
	
	
	// === &76. 글삭제 페이지 요청 === //
	@RequestMapping(value="/boardDel.gw")
	public ModelAndView requiredLogin_boardDel(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		
		// == 글1개 조회와 동일함 시작 ============================================
		// 삭제하고자 하는 글번호 받아오기
		String boardSeq = request.getParameter("boardSeq");
		
		// 삭제완료시 페이지 돌아갈때,
		// 글목록에서 검색되어진 글내용일 경우
		// 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글, 다음글이 나오도록 하기 위한 것이다. 
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
		// 삭제해야할 글1개 내용 가져와서, 글쓴이를 비교해서 다른 사람이 쓴 글은 삭제가 불가하도록 해야 한다.
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("boardSeq", boardSeq);
		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		mav.addObject("fromDate", fromDate);
		mav.addObject("toDate", toDate);
		mav.addObject("bCategory", bCategory);
		mav.addObject("searchType", searchType);
		mav.addObject("searchWord", searchWord);
		
		/////////////////////////////////////////////////////////
		// 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		// 사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해
		// 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
	//	System.out.println("~~~~ 확인용 boardView.gw의 gobackURL => " + gobackURL);
		// ~~~~ 확인용 boardView.gw의 gobackURL => /recentList.gw?fromDate=2021-08-26 toDate=2021-11-24 bCategory=0 searchType=subject searchWord=글 currentShowPageNo=2
		
		mav.addObject("gobackURL", gobackURL);
		/////////////////////////////////////////////////////////	
		// == 글1개 조회와 동일함 끝 ============================================
		
		BoardVO_OHJ boardvo = service.getViewWithNoAddCount(paraMap);
		// 글조회수 증가는 없고 단순히 글1개 조회
		
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		
		if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeId()) ) {
			String message = "다른 사용자의 글은 삭제가 불가합니다.";
			String loc = request.getContextPath()+"/boardView.gw?boardSeq="+boardvo.getBoardSeq()+"&fromDate="+fromDate+"&toDate="+toDate+"&bCategory="+bCategory+"&searchType="+searchType+"&searchWord="+searchWord+"&gobackURL="+gobackURL; // 로그인실패 후 성공했을때 history.back()하면 로그인창이 뜨므로, 글1개 보기 페이지로 이동하도록 바꿔줘야함!
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);
		}
		else {
			// 자신의 글을 삭제하는 경우
			int n = service.boardDel(paraMap);
			
			if(n==1) {
				mav.addObject("message", "글 삭제 성공!!");
				mav.addObject("loc", request.getContextPath()+"/recentList.gw");
			}
			else {
				mav.addObject("message", "글 삭제 실패..");
				mav.addObject("loc", request.getContextPath()+"/boardView.gw?boardSeq="+boardvo.getBoardSeq()); // 새로고침을 하게되면 boardDel.gw로 계속 들어오게되므로, 글 1개보기 페이지로 가도록 함.
			}
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	

	// === &84. 댓글쓰기(Ajax 로 처리) === //
	@ResponseBody
	@RequestMapping(value="/boardCommentWrite.gw", method = {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String boardCommentWrite(BoardCommentVO_OHJ commentvo) {
		// 댓글쓰기에 첨부파일이 없는 경우
		
		// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // 
		String content = commentvo.getContent();
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
	//	content = content.replaceAll("\r\n", "<br>"); // 입력한 엔터는 <br>처리하기 -> 댓글쓰기의 내용은 input태그라서 엔터가 안된다.
		commentvo.setContent(content);
		
		int n = 0;
		
		try {
			// 댓글쓰기(insert) 및 원게시물(tbl_board 테이블)의 댓글의 개수 증가(update 1씩 증가)하기
			n = service.boardCommentWrite(commentvo);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}

	
	// === &90. 원게시물에 딸린 댓글들을 조회해오기(Ajax로 처리) === //
	@ResponseBody
	@RequestMapping(value="/readComment.gw", method = {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String readComment(HttpServletRequest request) {
		
		String fk_boardSeq = request.getParameter("fk_boardSeq");
		
		List<BoardCommentVO_OHJ> commentList = service.getCommentList(fk_boardSeq);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(commentList != null) { // 해당하는 댓글이 존재하지 않을 수도 있음. 댓글이 존재할경우 실행함.
			for(BoardCommentVO_OHJ bcmtvo: commentList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("content", bcmtvo.getContent());
				jsonObj.put("positionName", bcmtvo.getPositionName());
				jsonObj.put("name", bcmtvo.getName());
				jsonObj.put("regDate", bcmtvo.getRegDate());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	
	
	
	// === &192. 차트를 보여주는 view단 === //
	@RequestMapping(value="/board/wordCloud.gw")
	public String wordCloud(Model model) {
		
		// '검색어키워드기록'을 가져와서 하나의 문자열로 만들기
		List<String> keywordList = service.getKeywordHistory();
		
		String str_keyword = "";
		for(int i=0; i<keywordList.size(); i++) {
			
			if(i==0) {
				str_keyword += keywordList.get(i);
			}
			else {
				str_keyword += ", " + keywordList.get(i);
			}
			
		}
		
		model.addAttribute("str_keyword", str_keyword);
		
		return "board/wordCloud.tiles_OHJ";
	}
	
	
	// >>> &191. Excel 파일로 다운받기 예제 <<< // 
	@RequestMapping(value="/excel/boardDownloadExcelFile.gw", method= {RequestMethod.POST})
	public String boardDownloadExcelFile(HttpServletRequest request, Model model) { // Model model은 저장기능+뷰단보여주는 ModelAndView와 달리 저장기능만 있음. 그냥 request를 사용해도 된다.
		
		//== recentList.gw에서 가져온 코드 == START ////////////////////////////////////////////////////////////////////////
		
		// == 카테고리 목록 가져오기 시작 == //
		List<BoardCategoryVO_OHJ> bcategoryList = service.viewCategoryList();
		// mav.addObject("bcategoryList", bcategoryList);
		// == 카테고리 목록 가져오기 끝 == //
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String bCategory = request.getParameter("bCategory");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		
	//	String str_currentShowPageNo = request.getParameter("currentShowPageNo");
	/*	
		System.out.println("확인용 fromDate : " + fromDate);
		System.out.println("확인용 toDate : " + toDate);
		System.out.println("확인용 bCategory : " + bCategory);
		System.out.println("확인용 searchType : " + searchType);
		System.out.println("확인용 searchWord : " + searchWord);
	*/	
		
		if(fromDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			// 2021-08-23 // '숫자개월수만큼 빼준 날짜인 add_months(sysdate,-3) 로 검색'하려했으나 유효성검사 해서 값이 넘어왔다.
			
			// 3달전 날짜 구하기
			fromDate = getDate("threeMonthsAgo");
		}
		if(toDate == null) { // 맨처음 목록보기를 통해 들어가는 경우
			// 2021-11-23 // 'sysdate 로 검색'하려했으나 유효성검사 해서 값이 넘어왔다.
			
			// 오늘 날짜 구하기
			toDate = getDate("today");
		}
		
		
		// === 최근 게시물이 아니라 sideinfo.jsp에서 클릭한 게시물을 보여주는 용 START ===
		String bCategorySeq = request.getParameter("bCategorySeq");
		// 최근 게시물은 bCategorySeq 이 null이고, bCategory는 null일수도 null이 아닐수도있는데
		// 									bCategory가 null이면        -> 맨 처음 목록보기로 들어가는 경우. 이 경우에는 bCategory가 0이다.
		// 									bCategory가 null이 아니면 -> 최근게시물이나 기타게시물에서 조회한 경우. 이 경우에는 bCategory가 넘어온 값이다. 
		// 공지사항, 자유게시판, 건의사항은 해당하는 bCategorySeq 가 넘어온다. -> 이 때 bCategory는 null로 넘어오는데, bCategory가 bCategorySeq이다.
		if(bCategorySeq == null && bCategory == null) {
			bCategory = "0";
		}
		if(bCategorySeq == null && bCategory != null) {
			// bCategory는 넘어온 그 자체이다.
		}
		if(bCategorySeq != null && bCategory == null) {
			bCategory = bCategorySeq;
		}
		// === 최근 게시물이 아니라 sideinfo.jsp에서 클릭한 게시물을 보여주는 용 END ===
		
		
		boolean flag = false; // 글목록보기로 넘어온 id가 bCategory인 select태그 속 bCategorySeq가, 실제 존재하는 것인지아닌지 여부
		for(BoardCategoryVO_OHJ bcvo : bcategoryList) {
			if(bcvo.getbCategorySeq().equals(bCategory)) {
				flag = true;
				break;// 반복문 종료
			}
		}
		if(!"0".equals(bCategory) && !flag) { // select태그에서 전체를 선택한 것도 아니고, 카테고리분류에 실제 존재하는것도 아니라면
			bCategory = "";
		}
		
		
		if(searchType == null || (!"subject".equals(searchType)&&!"name".equals(searchType))) { // 맨처음 목록보기를 통해 들어가는 경우, 유저가 장난친 경우
			searchType = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) { // 맨처음 목록보기를 통해 들어가는 경우, 검색어자체가 ⓐ없거나 ⓑ있는데 공백인 경우
			searchWord = "";
		}
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("bCategory", bCategory);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		//== recentList.gw에서 가져온 코드 == END////////////////////////////////////////////////////////////////////////
		
		
		// 페이징처리를 안한, 검색어가 있는 글목록 가져오기
		List<BoardVO_OHJ> boardList = service.boardListSearch(paraMap);
		
		
		// 해당하는 게시판 카테고리명 알아오기
		String bCategoryName = "";
		if("0".equals(bCategory) || "".equals(bCategory)) { // ⓐ전체를 선택한 경우임.  ⓑ해당하지 않는 bCategory로 장난친 경우는 글목록이 안나오는데 1행의 header는 '전체'로 줌.
			bCategoryName = "전체"; // 디폴트는 전체게시판이다.
		}
		if(!"0".equals(bCategory) && !"".equals(bCategory)) { // 제대로된 카테고리번호를 입력한 경우
			bCategoryName = service.getBCategoryName(bCategory);
		}
		
		// == 엑셀 꾸미기 START == // ---------------------------------------------------------------------------------
		
		// === 조회결과물인 boardList 를 가지고 엑셀 시트 생성하기 ===
	    // 시트를 생성하고, 행을 생성하고, 셀을 생성하고, 셀안에 내용을 넣어주면 된다.
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		// 시트생성
		SXSSFSheet sheet = workbook.createSheet("OurCompany게시글목록"); // .createSheet("시트명")
		
		// 시트 열 너비 설정
		sheet.setColumnWidth(0, 1000); 	// No
		sheet.setColumnWidth(1, 14000); // 글제목
		sheet.setColumnWidth(2, 2000); 	// 댓글수
		sheet.setColumnWidth(3, 2000); 	// 작성자
		sheet.setColumnWidth(4, 2000); 	// 조회수
		sheet.setColumnWidth(5, 5000); 	// 게시일
		// 총 6열
		
		// 행의 위치를 나타내는 변수
		int rowLocation = 0;
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// CellStyle 정렬하기(Alignment)
		// CellStyle 객체를 생성하여 Alignment 세팅하는 메소드를 호출해서 인자값을 넣어준다.
		// 아래는 HorizontalAlignment(가로)와 VerticalAlignment(세로)를 모두 가운데 정렬 시켰다. -> 정가운데정렬
		CellStyle mergeRowStyle = workbook.createCellStyle();
	    mergeRowStyle.setAlignment(HorizontalAlignment.CENTER); // 수평기준으로 가운데. justify는 양쪽맞춤
	    mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	                                      // import org.apache.poi.ss.usermodel.VerticalAlignment 으로 해야함.
		
	    CellStyle headerStyle = workbook.createCellStyle();
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
	    // CellStyle 배경색(ForegroundColor)만들기
        // setFillForegroundColor 메소드에 IndexedColors Enum인자를 사용한다.
        // setFillPattern은 해당 색을 어떤 패턴으로 입힐지를 정한다.
	    mergeRowStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex()); // IndexedColors.DARK_BLUE.getIndex() 는 색상(남색)의 인덱스값을 리턴시켜준다. 
	    mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 실선
	    
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex()); // IndexedColors.LIGHT_YELLOW.getIndex() 는 연한노랑의 인덱스값을 리턴시켜준다.
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 실선
	    
	    // Cell 폰트(Font) 설정하기
        // 폰트 적용을 위해 POI 라이브러리의 Font 객체를 생성해준다.
        // 해당 객체의 세터를 사용해 폰트를 설정해준다. 대표적으로 글씨체, 크기, 색상, 굵기만 설정한다.
        // 이후 CellStyle의 setFont 메소드를 사용해 인자로 폰트를 넣어준다.
	    Font mergeRowFont = workbook.createFont(); // import org.apache.poi.ss.usermodel.Font; 으로 한다.
	    mergeRowFont.setFontName("나눔고딕"); // 글씨체
	    mergeRowFont.setFontHeight((short)500); // 글자크기
	    mergeRowFont.setColor(IndexedColors.WHITE.getIndex()); // 글자색
	    mergeRowFont.setBold(true); // 글자굵기는 굵게
	    
	    mergeRowStyle.setFont(mergeRowFont);
	    
	    // CellStyle 테두리 Border
        // 테두리는 각 셀마다 상하좌우 모두 설정해준다.
        // setBorderTop, Bottom, Left, Right 메소드와 인자로 POI라이브러리의 BorderStyle 인자를 넣어서 적용한다.
	    headerStyle.setBorderTop(BorderStyle.THICK); // 굵은border선
	    headerStyle.setBorderBottom(BorderStyle.THICK); 
	    headerStyle.setBorderLeft(BorderStyle.THIN); // 가는border선
	    headerStyle.setBorderRight(BorderStyle.THIN); 
	    
	    
	    // Cell Merge 셀 병합시키기
        /* 셀병합은 시트의 addMergeRegion 메소드에 CellRangeAddress 객체를 인자로 하여 병합시킨다.
           CellRangeAddress 생성자의 인자로(시작 행, 끝 행, 시작 열, 끝 열) 순서대로 넣어서 병합시킬 범위를 정한다. 배열처럼 시작은 0부터이다.  
        */
        // 병합할 행 만들기
	    Row mergeRow = sheet.createRow(rowLocation); // 엑셀에서 행의 시작은 0 부터 시작한다.
	    
	    
	    // 병합할 행에 "우리회사 사원정보" 로 셀을 만들어 셀에 스타일을 주기  
	    for(int i=0; i<6; i++) {
	    	Cell cell = mergeRow.createCell(i); // 셀 생성
	    	cell.setCellStyle(mergeRowStyle);
	    	
	    	cell.setCellValue("우리회사 게시글목록 (" + bCategoryName + ")"); // 예 : 우리회사 게시글목록 (전체), 우리회사 게시글목록 (자유게시판)
	    }
	    
	    // 셀 병합하기
	    sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, 5)); // 0행~0행, 0열~5열까지 셀병합
	    
	    // CellStyle 천단위 쉼표, 금액
    //  CellStyle moneyStyle = workbook.createCellStyle();
    //  moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        ////////////////////////////////////////////////////////////////////////////////////////////////
	    
        
        // 헤더 행 생성
        Row headerRow = sheet.createRow(++rowLocation); // 아까 그 행의 그 다음번째 행 // 엑셀에서 행의 시작은 0 부터 시작한다.
        																	 // ++rowLocation는 전위연산자임. (후위가 아님!)
        
        // 해당 행의 첫번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0); // 엑셀에서 열의 시작은 0 부터 시작한다.
        headerCell.setCellValue("No");
        headerCell.setCellStyle(headerStyle);
        
		// 해당 행의 두번째 열 셀 생성
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("글제목");
        headerCell.setCellStyle(headerStyle);
        
		// 해당 행의 세번째 열 셀 생성
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("댓글수");
        headerCell.setCellStyle(headerStyle);
        
		// 해당 행의 네번째 열 셀 생성
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("작성자");
        headerCell.setCellStyle(headerStyle);
        
		// 해당 행의 다섯번째 열 셀 생성
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("조회수");
        headerCell.setCellStyle(headerStyle);
        
		// 해당 행의 여섯번째 열 셀 생성
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("게시일");
        headerCell.setCellStyle(headerStyle);
        
        
        // ==== 'OurCompany게시글목록' 내용에 해당하는 행 및 셀 생성하기 ==== // -> 항상 ROW를 만든 다음 각각의 CELL을 만드는 것이다!
        Row bodyRow = null;
        Cell bodyCell = null;
        
        for(int i=0; i<boardList.size(); i++) {
        	
        	// 행생성
        	bodyRow = sheet.createRow(i + (rowLocation+1)); // DB데이터가 들어갈 행의 인덱스는 2부터 2,3,4,5.. 이런식이다.
        	
        	// 데이터 글번호 표시(실제 글번호가 아닌 1,2,3 이런식이다.)
        	bodyCell = bodyRow.createCell(0); // 엑셀에서 열의 시작은 0 부터 시작한다.
        	bodyCell.setCellValue(i+1);
        	
        	// 데이터 글제목 표시
        	bodyCell = bodyRow.createCell(1);
        	bodyCell.setCellValue(boardList.get(i).getSubject());
        	
        	// 데이터 댓글수 표시
        	bodyCell = bodyRow.createCell(2);
        	bodyCell.setCellValue(boardList.get(i).getCommentCount());
        	
        	// 데이터 작성자 표시
        	bodyCell = bodyRow.createCell(3);
        	bodyCell.setCellValue(boardList.get(i).getName());
        	
        	// 데이터 조회수 표시
        	bodyCell = bodyRow.createCell(4);
        	bodyCell.setCellValue(boardList.get(i).getReadCount()); // Integer.parseInt(empMap.get("monthsal")) : 월급은 엑셀에서 sum으로 평균을 구할 수 있으므로, String타입이 아닌 number타입으로 바꿔준다.
        	
        	// 데이터 게시일 표시
        	bodyCell = bodyRow.createCell(5);
        	bodyCell.setCellValue(boardList.get(i).getRegDate());
        	
        }// end of for-------------------------
        
        model.addAttribute("locale", Locale.KOREA); // 돈 같은 경우는 원화로 나와야하므로 KOREA
        model.addAttribute("workbook", workbook);
        model.addAttribute("workbookName", "OurCompany게시글목록"); // 엑셀을 다운시 파일명 
		// == 엑셀 꾸미기 END == // ---------------------------------------------------------------------------------
		
		
		return "excelDownloadView"; // ★
	//  "excelDownloadView" 은 
	//	/webapp/WEB-INF/spring/appServlet/servlet-context.xml 파일에서
	//  뷰리졸버(뷰 해결사) 0 순위로 기술된 bean 의 id 값이다.
	}
	
	
	
	
	// === &163. 첨부파일 다운로드 받기 === //
	@RequestMapping(value="/downloadBoardAttach.gw")
	public void requiredLogin_download(HttpServletRequest request, HttpServletResponse response) {
		
		String boardSeq = request.getParameter("boardSeq");
		// 첨부파일이 있는 글번호
		
		/*
			첨부파일이 있는 글번호에서
			2021110812474555403647625200.jpg 처럼
			이러한 fileName 값을 DB 에서 가져와야 한다. (무엇을 다운받을것인지)
			또한 orgFilename 값도 DB 에서 가져와야 한다. (파일명을 뭐라고 다운받을것인지 : berkelekle심플V넥02.jpg)
		*/
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("boardSeq", boardSeq);
		
		paraMap.put("fromDate", ""); /* ohhj.xml에서 id="getView"를 재사용할껀데, 이것들이 paraMap에 있어야 오류가 안난다. */
		paraMap.put("toDate", "");
		paraMap.put("bCategory", "0");
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		response.setContentType("text/html; charset=UTF-8"); // 응답은 어떤형식이니? 웹브라우저에 찍어주겠다.
		PrintWriter out = null;
		
		try {
			Integer.parseInt(boardSeq);
			
			BoardVO_OHJ boardvo = service.getViewWithNoAddCount(paraMap);
			
			if(boardvo == null || (boardvo != null && boardvo.getFileName() == null)) { // ① 존재하지않는글번호 ② 존재하는 글번호이지만, 첨부파일이 없는 경우
				out = response.getWriter();
				// out은 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'> alert('존재하지 않는 글번호 이거나 첨부파일이 없으므로 파일 다운로드가 불가합니다!!'); history.back(); </script>");
				
				return; // 종료
			}
			else {
				String fileName = boardvo.getFileName();
				// 2021110812474555403647625200.jpg  이것이 바로 WAS(톰캣) 디스크에 저장된 파일명이다.
				
				String orgFilename = boardvo.getOrgFilename();
				// berkelekle심플V넥02.jpg  다운로드시 보여줄 파일명
				
				
				// 첨부파일이 저장되어 있는 WAS(톰캣)의 디스크 경로명을 알아와야만 다운로드를 해줄수 있다. 
	            // 이 경로는 우리가 파일첨부를 위해서 /boardWriteEnd.gw 에서 설정해두었던 경로와 똑같아야 한다.
				// WAS의 webapp 의 절대경로를 알아와야 한다.
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				
			//	System.out.println("~~~~ 확인용 webapp 의 절대경로 => " + root);
				// ~~~~ 확인용 webapp 의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\
				
				String path = root + "resources" + File.separator + "files";
				/* File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.
				       운영체제가 Windows 이라면 File.separator 는  "\" 이고,
				       운영체제가 UNIX, Linux 이라면  File.separator 는 "/" 이다. 
				*/
				
				// path 는 첨부파일이 저장될 WAS(톰캣)의 폴더가 된다.
			//	System.out.println("~~~~ 확인용 path => " + path);
				// ~~~~ 확인용 path => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files
				
				// **** file 다운로드 하기 **** //
				boolean flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				// flag 값이 true 로 받아오면 다운로드 성공을 말하고,
				// flag 값이 false 로 받아오면 다운로드 실패를 말한다.
				
				if(flag == false) {
					// 다운로드가 실패할 경우 메시지를 띄워준다.
					
					out = response.getWriter();
					// out은 웹브라우저상에 메시지를 쓰기 위한 객체생성
					
					out.println("<script type='text/javascript'> alert('파일 다운로드가 실패되었습니다!!'); history.back(); </script>");
				}
				
			}
		} catch(NumberFormatException e) {
			
			try {
				out = response.getWriter();
				// out은 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'> alert('존재하지 않는 글번호 이므로 파일 다운로드가 불가합니다!!'); history.back(); </script>");
			} catch(IOException e1) {
				
			}
			
		} catch(IOException e2) {
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// -- 어떤 것을 보여주는 페이지들은 getCurrentURL(request); 를 써줘야한다. --
	////////////////////////////////////////////////////////////////////////////////
	//  === 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 생성 === 
	public void getCurrentURL(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("goBackURL", MyUtil_KGH.getCurrentURL(request));
	}
	////////////////////////////////////////////////////////////////////////////////
	// === fromDate와 toDate를 각각 디폴트인 3달전날짜, 오늘날짜로 구해주는 메소드 ===
	public String getDate(String requiredTime) { // threeMonthsAgo 또는 today // requiredTime에는 3달전인지, 지금인지 알려주는 값이 들어옴.
		
		/* 
		    Date 클래스 보다 조금더 향상시켜서 나온 것이 Calendar 클래스이다.
		        간단한 날짜표현에는 Date 클래스를 사용하는 것이 더 나을 수 있으며,
		        두개 날짜사이의 날짜연산을 할 경우에는 메소드기능이 더 많이 추가된 
		    Calendar 클래스를 사용하는 것이 나을 수 있다.
		*/ 
		Calendar currentDate = Calendar.getInstance(); // 현재날짜와 시간
		
		if("threeMonthsAgo".equals(requiredTime)) {
			currentDate.add(Calendar.MONTH, -3); // 3달 전 날짜와 시간
		}
		
		int year = currentDate.get(Calendar.YEAR);
		
		int month = currentDate.get(Calendar.MONTH)+1; // 1~12
		String strMonth = (month<10)?"0"+month:String.valueOf(month); // 01~12
		
		// 아래의 Calendar.DATE 와 Calendar.DAY_OF_MONTH 는 같은 것이다.
	    int day = currentDate.get(Calendar.DATE);
	    String strDay = day<10?"0"+day:String.valueOf(day);
	    
	    String date = year + "-" + strMonth + "-" + strDay;
	//  System.out.println("확인용(requiredTime 날짜) : " + date);
	    /*
		    확인용(requiredTime 날짜) : 2021-08-27
		    확인용(requiredTime 날짜) : 2021-11-27
	    */
	    
	    return date;
	}
	

	
}
