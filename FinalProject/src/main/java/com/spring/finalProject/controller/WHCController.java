package com.spring.finalProject.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.approval.model.*;
import com.spring.finalProject.common.FileManager;
import com.spring.finalProject.common.MyUtil_KGH;
import com.spring.finalProject.model.DepartmentVO_KGH;
import com.spring.finalProject.model.EmployeeVO_KGH;
import com.spring.finalProject.service.InterWHCService;

//==== #30. 컨트롤러 선언 ====
@Component
@Controller
public class WHCController {
	
	@RequestMapping(value = "/index.gw")
	public String index(HttpServletRequest request) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		return "main/index.tiles1";
		//	/WEB-INF/views/tiles1/main/index.jsp 파일을 생성한다.
	}
	
	// 날씨 받아오기
	@ResponseBody
	@RequestMapping(value = "/getWeather.gw")
	public String getWeather(HttpServletRequest request) throws Exception {
		
		String base_date  = request.getParameter("strNow");
		String base_time = request.getParameter("hour");
		
		// 1. URL을 만들기 위한 StringBuilder.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=qASl3d5b34Bl88c8dulmYnS38qXLGAbrehv/O4eBXoWnJ27o3o4Knqoq5uHMefd62Tp57kGPgSiidAumZDs1/g=="); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*XML 또는 JSON*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(base_date, "UTF-8")); /*날짜*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(base_time, "UTF-8")); /*시간*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("59", "UTF-8")); /*X*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("126", "UTF-8")); /*Y*/
        // 3. URL 객체 생성.
        URL url = new URL(urlBuilder.toString());
        // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 5. 통신을 위한 메소드 SET.
        conn.setRequestMethod("GET");
        // 6. 통신을 위한 Content-type SET. 
        conn.setRequestProperty("Content-type", "application/json");
        // 7. 통신 응답 코드 확인.
     //   System.out.println("Response code: " + conn.getResponseCode());
        // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
        BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        HashMap<String,Object> resultMap = new HashMap<>();
        
        ObjectMapper mapper = new ObjectMapper();
        resultMap = mapper.readValue(sb.toString(), HashMap.class);
        
        // 10. 객체 해제.
        rd.close();
        conn.disconnect();
        // 11. 전달받은 데이터 확인.
   //     System.out.println(sb.toString());
        
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("result", resultMap);
        
		return jsonObj.toString();
		//	/WEB-INF/views/tiles1/main/index.jsp 파일을 생성한다.
	}
	
	@Autowired
	private InterWHCService service;
	// === #155. 파일업로드 및 다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === //

	@Autowired
	private FileManager fileManager;
	
	// === 전자결재 메인 페이지 === //
	@RequestMapping(value="/approval.gw")
	public ModelAndView requiredLogin_approval(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		Map<String,String> paraMap = new HashMap<>();
		
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		String fk_employeeid = loginuser.getEmployeeid();
		
		paraMap.put("apstatus", "0");
		paraMap.put("yn", "");
		paraMap.put("fk_employeeid", fk_employeeid);
		paraMap.put("fk_apcano", "");
		paraMap.put("makeFromDate", "");
		paraMap.put("makeToDate", "");
		paraMap.put("searchWord", "");
		int totalSentCount = service.getTotalCount(paraMap); // 상신중인 문서 갯수 알아오기
		
		List<Map<String, String>> approvalList = null;
		// 메인페이지에서 보여줄 페이징 처리한 "상신한" 목록 5개만 가져오기
		paraMap.put("startRno", "1");
		paraMap.put("endRno", "5");
		approvalList = service.approvalListSearchPaging(paraMap);
		
		paraMap.put("apstatus", "2");
		int totalreturnCount = service.getTotalCount(paraMap); // 반려된 문서 갯수 알아오기
		
		paraMap.put("yn", "1");
		paraMap.put("apstatus", "0");
		int totaldoCount = service.getTotalCount(paraMap); // 결재할 문서 갯수 알아오기
		// 메인페이지에서 보여줄 페이징 처리한 "결재전" 목록 5개만 가져오기
		List<Map<String, String>> approvaldoList = service.receiveDocapproval(paraMap);
		
		paraMap.put("emptype", "c");
		int cototalCount = service.getCooDocTotalCount(paraMap); // 협조 문서 갯수 알아오기
		
		paraMap.put("emptype", "r");
		int retotalCount = service.getCooDocTotalCount(paraMap); // 수신 문서 갯수 알아오기
		
		mav.addObject("approvalList", approvalList);
		mav.addObject("totalSentCount", totalSentCount);
		
		mav.addObject("totalreturnCount", totalreturnCount);
		
		mav.addObject("approvaldoList", approvaldoList);
		mav.addObject("totaldoCount", totaldoCount);
		
		mav.addObject("cototalCount", cototalCount);
		
		mav.addObject("retotalCount", retotalCount);
		
		mav.setViewName("approval/approvalMain.tiles_WHC");
		
		return mav;
		//  /WEB-INF/views/tiles_WHC/approval/approvalMain.jsp 파일을 생성한다.
	}
	
	// === 기안하기 버튼 클릭시 기안양식 페이지 === //
	@RequestMapping(value="/approvalForm.gw")
	public ModelAndView requiredLogin_approvalForm(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {

		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		mav.addObject("apcList", apcList);
		mav.setViewName("approval/approvalForm.tiles_WHC");
		
		return mav;
		
	}
	
	// === 기안양식을 골랐을때 기안작성하는 페이지 === //
	@RequestMapping(value="/addApproval.gw")
	public ModelAndView addApproval(HttpServletRequest request, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 로그인한 유저의 사원번호로  부서,직급 알아오기
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		String searchWord = loginuser.getEmployeeid();
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("searchWord",searchWord);
		List<Map<String,String>> emp = service.getEmployeeList(paraMap);
		
		// 로그인한 유저의 정보 넘겨주기
		Map<String, String> loginMap = new HashMap<>();
		if(emp != null) {
			for(Map<String,String> map: emp) {
				
				loginMap.put("employeeid", map.get("employeeid"));
				loginMap.put("name", map.get("name"));
				loginMap.put("departno", map.get("departno"));
				loginMap.put("departmentname", map.get("departmentname"));
				loginMap.put("positionno", map.get("positionno"));
				loginMap.put("positionname", map.get("positionname"));
			}
		}
		mav.addObject("loginMap", loginMap);
		
		// 선택한 기안양식에 맞는 form을 보여주기 위해 받아온 카테고리 값 넘겨주기
		String apcano = request.getParameter("apcano");
		String apcaname = request.getParameter("apcaname");
		mav.addObject("apcano", apcano);
		mav.addObject("apcaname", apcaname);
		
		// 결제선에 이용할 부서명, 부서번호 받아오기
		List<DepartmentVO_KGH> departList = service.getDepartment();
		mav.addObject("departList", departList);
		
		mav.setViewName("approval/addApproval.tiles_WHC");
		return mav;
		
	}
	
	// 결재선 설정 시 검색어가 있는 직원 목록 받아오기
	@ResponseBody
	@RequestMapping(value="/approval/getEmployeeList.gw", method={RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String getEmployeeList(HttpServletRequest request) {
		
		String searchWord = request.getParameter("searchWord");
		
		if(searchWord == null || searchWord.trim() == "") {
			searchWord = "";
		}
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("searchWord", searchWord);
		
		List<Map<String,String>> empList = service.getEmployeeList(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(empList != null) {
			for(Map<String,String> map: empList) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("employeeid", map.get("employeeid"));
				jsonObj.put("name", map.get("name"));
				jsonObj.put("departno", map.get("departno"));
				jsonObj.put("departmentname", map.get("departmentname"));
				jsonObj.put("positionno", map.get("positionno"));
				jsonObj.put("positionname", map.get("positionname"));
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	// === 스마트에디터. 드래그앤드롭을 사용한 다중 사진 파일 업로드 === //
	@RequestMapping(value="/image/multiplePhotoUpload.gw", method={RequestMethod.POST})
	public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response) {
		
		/*
	      1. 사용자가 보낸 파일을 WAS(톰캣)의 특정 폴더에 저장해주어야 한다.
	      >>>> 파일이 업로드 되어질 특정 경로(폴더)지정해주기
	           우리는 WAS 의 webapp/resources/photo_upload 라는 폴더로 지정해준다.
	    */
		
		// WAS 의 webapp 의 절대경로를 알아와야 한다. 
		HttpSession session = request.getSession();
		String root = session.getServletContext().getRealPath("/"); 
		String path = root + "resources"+File.separator+"photo_upload";
		// path 가 첨부파일들을 저장할 WAS(톰캣)의 폴더가 된다. 
	     
		// System.out.println(">>>> 확인용 path ==> " + path); 
		// >>>> 확인용 path ==> C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\finalProject\resources\photo_upload    
	     
		File dir = new File(path);
		if(!dir.exists())
	      dir.mkdirs();
		
		String strURL = "";
		
		try {
			String filename = request.getHeader("file-name"); // 파일명을 받는다 - 일반 원본파일명
	        // 네이버 스마트에디터를 사용한 파일업로드시 싱글파일업로드와는 다르게 멀티파일업로드는 파일명이 header 속에 담겨져 넘어오게 되어있다. 
			
			/*
		        [참고]
		        HttpServletRequest의 getHeader() 메소드를 통해 클라이언트 사용자의 정보를 알아올 수 있다. 
		
		       request.getHeader("referer");           // 접속 경로(이전 URL)
		       request.getHeader("user-agent");        // 클라이언트 사용자의 시스템 정보
		       request.getHeader("User-Agent");        // 클라이언트 브라우저 정보 
		       request.getHeader("X-Forwarded-For");   // 클라이언트 ip 주소 
		       request.getHeader("host");              // Host 네임  예: 로컬 환경일 경우 ==> localhost:9090    
		    */
			
			InputStream is = request.getInputStream(); // is 는 네이버 스마트 에디터를 사용하여 사진첨부하기 된 이미지 파일임.
			// java.io 를 import
	        /*
				요청 헤더의 content-type이 application/json 이거나 multipart/form-data 형식일 때,
	            name(이름) 없이 값만 전달될 때 이 값은 요청 헤더가 아닌 바디를 통해 전달된다. 
				이러한 형태의 값을 'payload body'라고 하는데 요청 바디에 직접 쓰여진다 하여 'request body post data'라고도 한다.
				
	           	서블릿에서 payload body 의 값을 읽어오려면 request.getParameter()를 사용하는 것이 아니라 
	            request.getInputStream() 또는 request.getReader()를 사용하여 body를 직접 읽는 방식으로 가져온다.   request.getReader() 는 text 파일 request.getInputStream() 은 이진 파일
	        */
			
			String newFilename = fileManager.doFileUpload(is, filename, path);
			
			String ctxPath = request.getContextPath(); // board
            
            strURL += "&bNewLine=true&sFileName=" + newFilename; 
            strURL += "&sWidth=";
            strURL += "&sFileURL="+ctxPath+"/resources/photo_upload/"+newFilename;
            
         // === 웹브라우저상에 사진 이미지를 쓰기 === //
            PrintWriter out = response.getWriter();
            out.print(strURL);
            
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	// === 기안하기 완료 요청 === //
	@RequestMapping(value="/addApprovalEnd.gw", method= {RequestMethod.POST})
	public ModelAndView addApprovalEnd(Map<String,String> paraMap, ModelAndView mav, ApprovalVO approvalvo, MultipartHttpServletRequest mrequest) {
		
		String fk_apcano = approvalvo.getFk_apcano();
		
		if("50".equals(fk_apcano)) { // 회의록
			String midate = mrequest.getParameter("midate");
			String purpose = mrequest.getParameter("purpose");
			
			paraMap.put("midate", midate);
			paraMap.put("purpose", purpose);
			
		}
		else if("51".equals(fk_apcano)) { // 업무보고
			String taskdate = mrequest.getParameter("taskdate");
			String issue = mrequest.getParameter("issue");
			
			paraMap.put("taskdate", taskdate);
			paraMap.put("issue", issue);
		}
		else if("53".equals(fk_apcano)) { // 지출결의서
			String spdate = mrequest.getParameter("spdate");
			String amount = mrequest.getParameter("amount");
			
			paraMap.put("spdate", spdate);
			paraMap.put("amount", amount);
		}
		paraMap.put("fk_apcano",fk_apcano);
		
		// === 사용자가 쓴 글에 파일이 첨부되이 있는 것인지, 아니면 파일첨부가 안된 것인지 구분을 지어야한다.
		// === !!! 첨부파일이 있는 경우 작업시작  !!! ===
		MultipartFile attach = approvalvo.getAttach();
		
		if( !attach.isEmpty() ) {
			// attach(첨부파일)가 비어있지 않으면 (즉, 첨부파일이 있다면)
			/*
			  	1. 사용자가 보낸 첨부파일을 was(톰캣)의 특정폴더에 저장해주어야 한다.
			  	>>> 파일이 업로드 되어질 특정경로(폴더) 지정해주기
			  		우리는 WAS의 webapp/resources/files 라는 폴더로 지정해준다.
			  		조심할 것은 Package Explorer 에서  files 라는 폴더를 만드는 것이 아니다. 
			*/
			// WAS의 webapp 의 절대경로를 알아와야 한다.
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources" + File.separator +"apfiles";
			
			
			// 	2. 파일첨부를 위한 변수의 설정 및 값을 초기화 한 수 파일 올리기
			String newFileName = "";
			//WAS의 디스크에 저장될 파일명
			
			byte[] bytes = null;
			// 첨부파일의 내용물을 담는 것
			
			long fileSize = 0;
			// 첨부파일의 크기
			
			try {
				bytes = attach.getBytes();
				// 첨부파일의 내용물을 읽어오는 것
				
				newFileName = fileManager.doFileUpload(bytes, attach.getOriginalFilename(), path);
				// 첨부되어진 파일을 업로드 하도록 하는 것이다. 
	            // attach.getOriginalFilename() 은 첨부파일의 파일명(예: 강아지.png)이다.
				
				// System.out.println("확인용 newFileName: " + newFileName);
				// 확인용 newFileName: 202111081126441133012762997000.webp
				// 확인용 newFileName: 202111081129331133182003588400.webp
				
			//  3. approvalvo 에 filenName 과 orgFilename, fileSize 에 값을 넣어주기
				
				approvalvo.setFileName(newFileName); 
				// WAS(톰캣)에 저장될 파일명(2021110809271535243254235235234.png)            
				
				approvalvo.setOrgFilename(attach.getOriginalFilename());
				// 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명 
				
				fileSize = attach.getSize(); // 첨부파일의 크기 (단위는 byte)
				approvalvo.setFileSize(String.valueOf(fileSize));
				// 파일크기  
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// === !!! 첨부파일이 있는 경우 작업끝  !!! ===
		
		// === 파일첨부가 있는 기안 또는 파일 첨부가 없는 기안으로 나눠서 service 호출하기 === //
		int n = 0;
		n = service.addAP(approvalvo,paraMap);
		
		if(n == 0) {
			mav.addObject("message", "기안하기 실패..");
		}
		else {
			mav.addObject("message", "기안하기 성공!!");
		}
		
		mav.addObject("loc", mrequest.getContextPath() + "/approval.gw");
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	// === 내가 상신한 문서 === //
	@RequestMapping(value="/box/sentDoc.gw")
	public ModelAndView requiredLogin_sentDoc(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		mav.addObject("apcList", apcList);
		
		// === 페이징 처리를 한 검색어가 있는 목록 보여주기 시작 === //
		List<Map<String, String>> approvalList = null;
		String yn = "";
		Map<String,String> searchmap = getStartandEndRno(request, yn);
		mav.addObject("searchmap", searchmap);
		// System.out.println(searchmap.get("startRno"));
		// System.out.println(searchmap.get("endRno"));
		
		// 페이징 처리한 글 목록 가져오기(검색이 있는경우 없는경우 모두 다 포함)
		approvalList = service.approvalListSearchPaging(searchmap);
		
		// === #123. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		//			  사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
		// 			  현재 페이지 주소를 뷰단으로 넘겨준다. === //
		String goBackURL = MyUtil_KGH.getCurrentURL(request);
		
		mav.addObject("goBackURL", goBackURL);
		// === 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝 === //
		
		mav.addObject("approvalList", approvalList);
		
		mav.setViewName("/box/sentDoc.tiles_WHC");
		
		return mav;
	}
	
	
	// 기안함, 결재함
	public Map<String,String> getStartandEndRno (HttpServletRequest request, String yn) {
		
		
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		String fk_employeeid = loginuser.getEmployeeid();
		String apstatus = request.getParameter("apstatus");
		
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String makeFromDate = request.getParameter("makeFromDate");
		String makeToDate = request.getParameter("makeToDate");
		String fk_apcano = request.getParameter("fk_apcano");
		String searchWord = request.getParameter("searchWord");
		
		if(apstatus == null || (!"0".equals(apstatus) && !"1".equals(apstatus) && !"2".equals(apstatus) ) ) {
			apstatus = "0";
		}
		
		if(yn == null) {
			// 상신한 파일인경우
			//	처음 페이지를 보여줄때
			yn = "";
		}
		
		if(makeFromDate == null) {
			//	처음 페이지를 보여줄때
				makeFromDate = "";
		}
		
		if(makeToDate == null) {
			//	처음 페이지를 보여줄때
			makeToDate = "";
		}
		
		if(fk_apcano == null || (!"50".equals(fk_apcano) && !"51".equals(fk_apcano) && !"52".equals(fk_apcano) && !"53".equals(fk_apcano) ) ) {
			//	처음 페이지를 보여줄때 거나 		유저가 get 방식이기때문에 장난쳐오는 경우 라면
			fk_apcano = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		//	처음 페이지를 보여줄때 거나 	 검색어가 없거나 공백이라면
			searchWord = "";
		}
		
		
		Map<String,String> searchmap = new HashMap<>();
		searchmap.put("fk_employeeid", fk_employeeid);
		searchmap.put("apstatus", apstatus);
		searchmap.put("yn", yn);
		
		searchmap.put("str_currentShowPageNo", str_currentShowPageNo);
		searchmap.put("makeFromDate", makeFromDate);
		searchmap.put("makeToDate", makeToDate);
		searchmap.put("fk_apcano", fk_apcano);
		searchmap.put("searchWord", searchWord);
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.  
		int totalCount = 0; 		// 총 게시물 건수
		int sizePerPage = 10; 		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; 	// 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0 ; 		// 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		
		int startRno = 0;			// 시작 행번호
		int endRno = 0;				// 끝 행번호
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getTotalCount(searchmap);
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage); // (double)127/10 ==> 12.7 ==> Math.ceil(12.7) ==> 13.0 ==> (int)13.0 ==> 13
		
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo<1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
			
		}
		
		startRno = ( ( currentShowPageNo - 1) * sizePerPage ) + 1; 
		endRno = startRno + sizePerPage - 1;  
		
		// === #121. 페이지바 만들기 === //
		int blockSize = 10;
		// blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
	      /*
			        	  1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  21 22 23
		  */
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다. 
		
		// !!! 아래는 pageNo 를 구하는 공식이다. !!! //
		int pageNo = (( currentShowPageNo - 1)/blockSize ) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "";
		if("".equals(yn)) {
			url = "sentDoc.gw?";
		}
		else {
			url = "receiveDoc.gw?yn="+yn;
		}
		
		// **** [맨처음][이전] 만들기  **** //
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; font-size:12pt; width:70px;'><a href='"+url+"currentShowPageNo=1&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; font-size:12pt; width:50px;'><a href='"+url+"currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage ) ) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'><a href='#' style='color:red;'>"+pageNo+"</a></li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>"+pageNo+"</a></li>";
			}
			loop++;   // 1 2 3 4 5 6 7 8 9 10
			
			pageNo++; //  1  2  3  4  5  6  7  8  9 10
						  // 11 12 13 14 15 16 17 18 19 20
					      // 21 22 23 24 25 26 27 28 29 30
					  // 31 32 33 34 35 36 37 38 39 40
					  // 41 42
			
		}// end of while -------------------------
		
		// **** [다음][마지막] 만들기  **** //
		// pageNo ==> 11
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; font-size:12pt; width:50px;'><a href='"+url+"currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; font-size:12pt; width:70px;'><a href='"+url+"currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
				
		searchmap.put("totalCount", String.valueOf(totalCount));
		searchmap.put("startRno", String.valueOf(startRno));
		searchmap.put("endRno", String.valueOf(endRno));
		searchmap.put("pageBar", pageBar);
		
		
		return searchmap;
		
	}
	
	// === 결재함 문서 === //
	@RequestMapping(value="/box/receiveDoc.gw")
	public ModelAndView requiredLogin_receiveDoc(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		mav.addObject("apcList", apcList);
		
		// === 페이징 처리를 한 검색어가 있는 목록 보여주기 시작 === //
		List<Map<String, String>> approvalList = null;
		
		String yn = request.getParameter("yn");
		String apstatus = request.getParameter("apstatus");
		if(yn == null || ("0".equals(apstatus) && (!"0".equals(yn) && !"1".equals(yn)) )) {
			yn = "1";
		}
		if(yn == null || ("1".equals(apstatus) && (!"0".equals(yn) && !"1".equals(yn)) )) {
			yn = "0";
		}
		if(yn == null || ("2".equals(apstatus) && (!"0".equals(yn) && !"1".equals(yn)) )) {
			yn = "0";
		}
		
		Map<String,String> searchmap = getStartandEndRno(request,yn);
		mav.addObject("searchmap", searchmap);
		
		// 페이징 처리한 글 목록 가져오기(검색이 있는경우 없는경우 모두 다 포함)
		approvalList = service.receiveDocapproval(searchmap);
		
		// === #123. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		//			  사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
		// 			  현재 페이지 주소를 뷰단으로 넘겨준다. === //
		String goBackURL = MyUtil_KGH.getCurrentURL(request);
		
		mav.addObject("goBackURL", goBackURL);
		// === 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝 === //
		
		mav.addObject("approvalList", approvalList);
		
		mav.setViewName("/box/receiveDoc.tiles_WHC");
		
		return mav;
	}

	
	// === 협조함 문서 === //
	@RequestMapping(value="/box/cooDoc.gw")
	public ModelAndView requiredLogin_cooDoc(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		mav.addObject("apcList", apcList);
		
		// === 페이징 처리를 한 검색어가 있는 목록 보여주기 시작 === //
		List<Map<String, String>> approvalList = null;
		
		String emptype = "c";
		
		Map<String,String> searchmap = getCooStartandEndRno(request,emptype);
		mav.addObject("searchmap", searchmap);
		
		// 페이징 처리한 글 목록 가져오기(검색이 있는경우 없는경우 모두 다 포함)
		approvalList = service.cooDocapproval(searchmap);
		
		// === #123. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		//			  사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
		// 			  현재 페이지 주소를 뷰단으로 넘겨준다. === //
		String goBackURL = MyUtil_KGH.getCurrentURL(request);
		
		mav.addObject("goBackURL", goBackURL);
		// === 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝 === //
		
		mav.addObject("approvalList", approvalList);
		
		mav.setViewName("/box/cooDoc.tiles_WHC");
		
		return mav;
	}
	
	// === 수신함 문서 === //
	@RequestMapping(value="/box/rbeDoc.gw")
	public ModelAndView requiredLogin_rbeDoc(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		mav.addObject("apcList", apcList);
		
		// === 페이징 처리를 한 검색어가 있는 목록 보여주기 시작 === //
		List<Map<String, String>> approvalList = null;
		
		String emptype = "r";
		
		Map<String,String> searchmap = getCooStartandEndRno(request,emptype);
		mav.addObject("searchmap", searchmap);
		
		// 페이징 처리한 글 목록 가져오기(검색이 있는경우 없는경우 모두 다 포함)
		approvalList = service.cooDocapproval(searchmap);
		
		// === #123. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		//			  사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
		// 			  현재 페이지 주소를 뷰단으로 넘겨준다. === //
		String goBackURL = MyUtil_KGH.getCurrentURL(request);
		
		mav.addObject("goBackURL", goBackURL);
		// === 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝 === //
		
		mav.addObject("approvalList", approvalList);
		
		mav.setViewName("/box/rbeDoc.tiles_WHC");
		
		return mav;
	}
	
	// 협조, 수신
	public Map<String,String> getCooStartandEndRno (HttpServletRequest request, String emptype) {
		
		
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		String fk_employeeid = loginuser.getEmployeeid();
		String apstatus = request.getParameter("apstatus");
		
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String makeFromDate = request.getParameter("makeFromDate");
		String makeToDate = request.getParameter("makeToDate");
		String fk_apcano = request.getParameter("fk_apcano");
		String searchWord = request.getParameter("searchWord");
		
		if(apstatus == null || (!"0".equals(apstatus) && !"1".equals(apstatus) && !"2".equals(apstatus) ) ) {
			apstatus = "0";
		}
		
		if(makeFromDate == null) {
			//	처음 페이지를 보여줄때
				makeFromDate = "";
		}
		
		if(makeToDate == null) {
			//	처음 페이지를 보여줄때
			makeToDate = "";
		}
		
		if(fk_apcano == null || (!"50".equals(fk_apcano) && !"51".equals(fk_apcano) && !"52".equals(fk_apcano) && !"53".equals(fk_apcano) ) ) {
			//	처음 페이지를 보여줄때 거나 		유저가 get 방식이기때문에 장난쳐오는 경우 라면
			fk_apcano = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
		//	처음 페이지를 보여줄때 거나 	 검색어가 없거나 공백이라면
			searchWord = "";
		}
		
		
		Map<String,String> searchmap = new HashMap<>();
		searchmap.put("emptype", emptype);
		
		searchmap.put("fk_employeeid", fk_employeeid);
		searchmap.put("apstatus", apstatus);
		
		searchmap.put("str_currentShowPageNo", str_currentShowPageNo);
		searchmap.put("makeFromDate", makeFromDate);
		searchmap.put("makeToDate", makeToDate);
		searchmap.put("fk_apcano", fk_apcano);
		searchmap.put("searchWord", searchWord);
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.  
		int totalCount = 0; 		// 총 게시물 건수
		int sizePerPage = 10; 		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; 	// 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0 ; 		// 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		
		int startRno = 0;			// 시작 행번호
		int endRno = 0;				// 끝 행번호
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getCooDocTotalCount(searchmap);
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage); // (double)127/10 ==> 12.7 ==> Math.ceil(12.7) ==> 13.0 ==> (int)13.0 ==> 13
		
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo<1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
			
		}
		
		startRno = ( ( currentShowPageNo - 1) * sizePerPage ) + 1; 
		endRno = startRno + sizePerPage - 1;  
		
		// === #121. 페이지바 만들기 === //
		int blockSize = 10;
		// blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
	      /*
			        	  1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  21 22 23
		  */
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수(지금은 10개)까지만 증가하는 용도이다. 
		
		// !!! 아래는 pageNo 를 구하는 공식이다. !!! //
		int pageNo = (( currentShowPageNo - 1)/blockSize ) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "";
		if("c".equals(emptype)){
			url = "cooDoc.gw";
		}
		else if("r".equals(emptype)){
			url = "rbeDoc.gw";
		}
		
		// **** [맨처음][이전] 만들기  **** //
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; font-size:12pt; width:70px;'><a href='"+url+"?currentShowPageNo=1&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; font-size:12pt; width:50px;'><a href='"+url+"?currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage ) ) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; padding:2px 4px;'><a href='#' style='color:red;'>"+pageNo+"</a></li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>"+pageNo+"</a></li>";
			}
			loop++;   // 1 2 3 4 5 6 7 8 9 10
			
			pageNo++; //  1  2  3  4  5  6  7  8  9 10
						  // 11 12 13 14 15 16 17 18 19 20
					      // 21 22 23 24 25 26 27 28 29 30
					  // 31 32 33 34 35 36 37 38 39 40
					  // 41 42
			
		}// end of while -------------------------
		
		// **** [다음][마지막] 만들기  **** //
		// pageNo ==> 11
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; font-size:12pt; width:50px;'><a href='"+url+"?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; font-size:12pt; width:70px;'><a href='"+url+"?currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"&apstatus="+apstatus+"&makeFromDate="+makeFromDate+"&makeToDate="+makeToDate+"&fk_apcano="+fk_apcano+"&searchWord="+searchWord+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
				
		searchmap.put("totalCount", String.valueOf(totalCount));
		searchmap.put("startRno", String.valueOf(startRno));
		searchmap.put("endRno", String.valueOf(endRno));
		searchmap.put("pageBar", pageBar);
		
		
		return searchmap;
		
	}
	
	
	// === 문서상세 조회하기 === //
	@RequestMapping(value="/docdetail.gw")
	public ModelAndView requiredLogin_docdetail(HttpServletRequest request, HttpServletResponse response ,ModelAndView mav) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		HttpSession session = request.getSession();
		EmployeeVO_KGH loginuser = (EmployeeVO_KGH) session.getAttribute("loginuser");
		
		String login_userid = null;
		
		if(loginuser != null) {
			login_userid = loginuser.getEmployeeid();
			mav.addObject("login_userid", login_userid);
		}
		
		// 기안양식 카테고리 얻어오기
		List<ApcategoryVO> apcList = service.getApcategoryList();
		
		String apno = request.getParameter("apno");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("apno", apno);
		
		try {
			Integer.parseInt(apno); // get 방식으로 사용자가 직접 주소창에 잘못입력한 경우에는 exception
			
			String authority = "";  // 문서 권한
			
			// 문서상세 조회하기
			ApprovalVO approval = service.getDocdetail(paraMap);
			
			mav.addObject("approval",approval);
			
			if(approval != null) {
				// 카테고리명 넣기
				for( ApcategoryVO cvo:apcList) {
					if(approval.getFk_apcano().equals(cvo.getApcano())) {
						mav.addObject("apcaname",cvo.getApcaname());
					}
				}
				
				if(approval.getFk_employeeid().contains(login_userid)) {
					authority = "w";
				}
				if(approval.getCoopEmp() != null && approval.getCoopEmp().contains(login_userid)) {
					authority = "c";
				}
				if(approval.getReciEmp() != null &&approval.getReciEmp().contains(login_userid)) {
					authority = "r";
				}
				if(approval.getApprEmp() != null &&approval.getApprEmp().contains(login_userid)) {
					authority = "a";
				}
				
				if( !("w".equals(authority) || "c".equals(authority) || "r".equals(authority) || "a".equals(authority)) ) {
					// 문서조회 권한이 없음
					String message = "조회권한이 없습니다.";
		            String loc = "javascript:history.back()";
		            
		            mav.addObject("message", message);
		            mav.addObject("loc", loc);
		            mav.setViewName("msg");
				}
				else {
					// 문서조회 권한이 있음
					
					// 문서종류에 따른 상세내용 select
					Map<String,String> cateMap = new HashMap<>();
					String apcano = approval.getFk_apcano();
					cateMap.put("apno", apno);
					cateMap.put("apcano", apcano);
					
					Map<String,String> cateApdetail = service.cateApdetail(cateMap);
					mav.addObject("cateApdetail",cateApdetail);
					
					// 본인이 결재자에 포함되었으며 문서가 진행중이라면 본인의 결재차례인지 확인
					if("a".equals(authority) && "0".equals(approval.getApstatus()) ) {
						paraMap.put("userid", login_userid);
						String yn = service.isApYN(paraMap);
						if("1".equals(yn)){ // 본인차례 라면
							authority = "a1";
						}
					}
					
					mav.addObject("authority", authority);
					
					// 기안자, 결재자, 협조자, 수신자 정보(부서,직책) 알아오기
					// 기안자 정보
					String apprW = approval.getFk_employeeid();
					
					Map<String,String> empidMap = new HashMap<>();
					empidMap.put("employeeid", apprW);
					
					Map<String,String> userDandP = service.getDeptNPos(empidMap);
					mav.addObject("sendEmp", userDandP);
					
					// 결재자 정보
					List<Map<String,String>> apprList = new ArrayList<>();
					
					String[] apprA = approval.getApprEmp().split(",");
					for(int i=0; i<apprA.length; i++) {
						String employeeid = apprA[i];
						empidMap.put("employeeid", employeeid);
						
						userDandP = service.getDeptNPos(empidMap);
						apprList.add(userDandP);
					}
					mav.addObject("apprList", apprList);
					
					// 협조자 정보
					List<Map<String,String>> coopList = new ArrayList<>();
					if(approval.getCoopEmp() != null) {
						String[] apprC = approval.getCoopEmp().split(",");
						for(int i=0; i<apprC.length; i++) {
							String employeeid = apprC[i];
							empidMap.put("employeeid", employeeid);
							
							userDandP = service.getDeptNPos(empidMap);
							coopList.add(userDandP);
						}
					}
					mav.addObject("coopList", coopList);
					
					// 기안자 정보
					List<Map<String,String>> reciList = new ArrayList<>();
					if(approval.getReciEmp() != null) {
						String[] apprR = approval.getReciEmp().split(",");
						for(int i=0; i<apprR.length; i++) {
							String employeeid = apprR[i];
							empidMap.put("employeeid", employeeid);
							
							userDandP = service.getDeptNPos(empidMap);
							reciList.add(userDandP);
						}
					}
					mav.addObject("reciList", reciList);
					mav.setViewName("/box/docdetail.tiles_WHC");
				}
			}
		} catch(NumberFormatException e) {
			
		}
		
		return mav;
	}
	
	// 결재의견 작성하기
	@ResponseBody
	@RequestMapping(value="/addOpinion.gw", method={RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String addOpinion(OpinionVO opnvo) {
		
		String apprEmp = opnvo.getApprEmp();
		String[] arrApprs = apprEmp.split(",");
		
		String nextAppEmp = "";
		
		// [0,1,2]
		for(int i=0; i<arrApprs.length;i++){
			if(arrApprs[i].equals(opnvo.getFk_employeeid())) {
				if(i+1 < arrApprs.length) {
					nextAppEmp = arrApprs[i+1];
				}
				break;
			}
		}
		
		Map<String,String> nextEmp = new HashMap<>();
		
		nextEmp.put("nextAppEmp", nextAppEmp);
		nextEmp.put("fk_employeeid", opnvo.getFk_employeeid());
		nextEmp.put("fk_apno", opnvo.getFk_apno());
		nextEmp.put("yn", "0");
		
		int n = service.addOpinion(opnvo,nextEmp);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	// 결재의견 읽어오기
	@ResponseBody
	@RequestMapping(value="/readOpinion.gw", method={RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String readOpinion(HttpServletRequest request) {
		
		String fk_apno = request.getParameter("fk_apno");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_apno", fk_apno);
		
		List<OpinionVO> opinionList = service.readOpinion(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		if(opinionList != null) {
			for(OpinionVO opnvo : opinionList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("apprEmp", opnvo.getApprEmp());
				jsonObj.put("fk_employeeid", opnvo.getFk_employeeid());
				jsonObj.put("opdate", opnvo.getOpdate());
				jsonObj.put("opinion", opnvo.getOpinion());
				jsonObj.put("apstatus", opnvo.getApstatus());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	// === 기안문서 첨부파일 다운로드 받기 === //
	@RequestMapping(value="/downloadApAttach.gw")
	public void requiredLogin_downloadApAttach(HttpServletRequest request, HttpServletResponse response) {
		
		String apno = request.getParameter("apno");
		// 첨부파일이 있는 글번호
		
		/*
		   첨부파일이 있는 글번호에서
		  202111081247281137857237780500.png 처럼
		   이러한 fileName 값을 DB 에서 가져와야 한다.
		   또한 orgFilename 값도 DB 에서 가져와야 한다.
		*/
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("apno", apno);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = null;
		
		try {
			Integer.parseInt(apno);
			
			ApprovalVO approval = service.getDocdetail(paraMap);
			
			if(approval == null || (approval != null && approval.getFileName() == null) ) {
				out = response.getWriter();
				// 웹브라우저상에 메시지를 쓰기 위한 객체생성
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호 또는 첨부파일이 없습니다.'); history.back();</script>");
				
				return; // 종료
			}
			
			else {
				String fileName = approval.getFileName();
				// 202111081247281137857237780500.png was(톰캣) 디스크에 저장된 파일이름
				
				String orgFilename = approval.getOrgFilename();
				// 강아지.png  다운로드시 보여줄 파일명
				approval.getFileSize();
				
				// 첨부파일이 저장되어 있는 WAS(톰캣)의 디스크 경로명을 알아와야만 다운로드를 해줄수 있다. 
	            // 이 경로는 우리가 파일첨부를 위해서 /addEnd.action 에서 설정해두었던 경로와 똑같아야 한다.
				// WAS의 webapp 의 절대경로를 알아와야 한다.
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				// System.out.println("확인용 webapp 의 절대경로 : " + root);
				// 확인용 webapp 의 절대경로 : C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\
				
				String path = root + "resources" + File.separator +"apfiles";
				/* File.separator 는 운영체제에서 사용하는 폴더와 파일의 구분자이다.
			             운영체제가 Windows 이라면 File.separator 는  "\" 이고,
			             운영체제가 UNIX, Linux 이라면  File.separator 는 "/" 이다. 
				 */
				// path 가 첨부파일이 저장될 WAS(톰캣) 의 폴더가 된다.
				// System.out.println("확인용 path : " + path);
				
				// **** file 다운로드 하기 **** //
				boolean flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				// flag 값이 true 로 받아오면 다운로드 성공을 말하고 
				// flag 값이 false 로 받아오면 다운로드 실패를 말한다.
			
				if(flag == false) {
					// 다운로드가 실패할 경우 메시지를 띄워준다.
					out = response.getWriter();
					// 웹브라우저상에 메시지를 쓰기 위한 객체생성
					out.println("<script type='text/javascript'>alert('파일 다운로드 실패.'); history.back();</script>");
				}
				
			}
			
		} catch(NumberFormatException e) {
			
			try {
				out = response.getWriter();
				// 웹브라우저상에 메시지를 쓰기 위한 객체생성
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호 입니다.'); history.back();</script>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			
		}
		
	}
	// >>> #192. Excel 파일로 다운받기 예제 <<< //
	@RequestMapping(value="/box/downloadExcelFile.gw", method= {RequestMethod.POST})
	public String downloadExcelFile(HttpServletRequest request, Model model) {
		
		// === 페이징 처리를 한 검색어가 있는 목록 보여주기 시작 === //
		List<Map<String, String>> approvalList = null;
		String yn = "";
		Map<String,String> searchmap = getStartandEndRno(request, yn);
		
		// 페이징 처리한 글 목록 가져오기(검색이 있는경우 없는경우 모두 다 포함)
		approvalList = service.approvalListSearchPaging(searchmap);
		
		// === 조회결과물인 approvalList 를 가지고 엑셀 시트 생성하기 ===
		// 시트를 생성하고, 행을 생성하고, 셀을 생성하고, 셀안에 내용을 넣어주면 된다.
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		// 시트생성
		SXSSFSheet sheet = workbook.createSheet("기안한문서");
		
		// 시트 열 너비 설정
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 15000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		
		// 행의 위치를 나타내는 변수
		int rowLocation = 0;
		
		////////////////////////////////////////////////////////////////////////////////////////
		// CellStyle 정렬하기(Alignment)
		// CellStyle 객체를 생성하여 Alignment 세팅하는 메소드를 호출해서 인자값을 넣어준다.
		// 아래는 HorizontalAlignment(가로)와 VerticalAlignment(세로)를 모두 가운데 정렬 시켰다.
		CellStyle mergeRowStyle = workbook.createCellStyle(); // 제목(셀병합할거임)
		mergeRowStyle.setAlignment(HorizontalAlignment.CENTER);
		mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
										// import org.apache.poi.ss.usermodel.VerticalAlignment 으로 해야함.
		
		CellStyle headerStyle = workbook.createCellStyle(); // 제목아래 header부분
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    
	    // CellStyle 배경색(ForegroundColor)만들기
        // setFillForegroundColor 메소드에 IndexedColors Enum인자를 사용한다.
        // setFillPattern은 해당 색을 어떤 패턴으로 입힐지를 정한다.
	    mergeRowStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex()); // IndexedColors.DARK_BLUE.getIndex() 는 색상(남색)의 인덱스값을 리턴시켜준다. 
	    mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex()); // IndexedColors.LIGHT_YELLOW.getIndex() 는 연한파랑의 인덱스값을 리턴시켜준다.
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    // Cell 폰트(Font) 설정하기
        // 폰트 적용을 위해 POI 라이브러리의 Font 객체를 생성해준다.
        // 해당 객체의 세터를 사용해 폰트를 설정해준다. 대표적으로 글씨체, 크기, 색상, 굵기만 설정한다.
        // 이후 CellStyle의 setFont 메소드를 사용해 인자로 폰트를 넣어준다.
	    Font mergeRowfont = workbook.createFont(); // import org.apache.poi.ss.usermodel.Font; 으로 한다.
	    mergeRowfont.setFontName("나눔고딕");
	    mergeRowfont.setFontHeight((short)500);
	    mergeRowfont.setColor(IndexedColors.WHITE.getIndex());
	    mergeRowfont.setBold(true);
	    
	    mergeRowStyle.setFont(mergeRowfont);
	    
	    // CellStyle 테두리 Border
        // 테두리는 각 셀마다 상하좌우 모두 설정해준다.
        // setBorderTop, Bottom, Left, Right 메소드와 인자로 POI라이브러리의 BorderStyle 인자를 넣어서 적용한다.
	    headerStyle.setBorderTop(BorderStyle.THICK);
	    headerStyle.setBorderBottom(BorderStyle.THICK);
	    headerStyle.setBorderLeft(BorderStyle.THIN);
	    headerStyle.setBorderRight(BorderStyle.THIN);
	    
	    // Cell Merge 셀 병합시키기
        /* 셀병합은 시트의 addMergeRegion 메소드에 CellRangeAddress 객체를 인자로 하여 병합시킨다.
           CellRangeAddress 생성자의 인자로(시작 행, 끝 행, 시작 열, 끝 열) 순서대로 넣어서 병합시킬 범위를 정한다. 배열처럼 시작은 0부터이다.  
        */
        // 병합할 행 만들기
	    Row mergeRow = sheet.createRow(rowLocation); // 액셀에서 행의 시작은 0부터 시작한다.
	    
	    // 병합할 행에 "내가 기안한 문서" 로 셀을 만들어 셀에 스타일을 주기  
	    for(int i=0; i<5; i++) {
	    	Cell cell = mergeRow.createCell(i);
	    	cell.setCellStyle(mergeRowStyle);
	    	if("0".equals(request.getParameter("apstatus"))) {
	    		cell.setCellValue("내가 기안한 진행중 문서");
	    	}
	    	else if("1".equals(request.getParameter("apstatus"))) {
	    		cell.setCellValue("내가 기안한 완료된 문서");
	    	}
	    	else if("2".equals(request.getParameter("apstatus"))) {
	    		cell.setCellValue("내가 기안한 반려된 문서");
	    	}
	    }
	    
	    // 셀 병합하기
	    sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, 4));
	    
	 // CellStyle 천단위 쉼표, 금액
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        ////////////////////////////////////////////////////////////////////////////////////////////////
        
        // 헤더 행 생성
        Row headerRow = sheet.createRow(++rowLocation); // 엑셀에서 행의 시작은 0 부터 시작한다.
        // ++rowLocation는 전위연산자임. 
        
        // 해당 행의 1번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0); // 액셀에서 열의 시작은 0부터 시작한다.
        headerCell.setCellValue("문서번호");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 2번째 열 셀 생성
        headerCell = headerRow.createCell(1); 
        headerCell.setCellValue("기안양식");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 3번째 열 셀 생성
        headerCell = headerRow.createCell(2); 
        headerCell.setCellValue("기안제목");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 4번째 열 셀 생성
        headerCell = headerRow.createCell(3); 
        headerCell.setCellValue("상신일시");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 5번째 열 셀 생성
        headerCell = headerRow.createCell(4); 
        if("0".equals(request.getParameter("apstatus"))) {
        	headerCell.setCellValue("결재대기자");
    	}
    	else if("1".equals(request.getParameter("apstatus"))) {
    		headerCell.setCellValue("완료일시");
    	}
    	else if("2".equals(request.getParameter("apstatus"))) {
    		headerCell.setCellValue("반려일시");
    	}
        headerCell.setCellStyle(headerStyle);
        
        
        // ==== HR 사원정보 내용에 해당하는 행 및 셀 생성하기 ==== //
        Row bodyRow = null;
        Cell bodyCell = null;
        
        if(approvalList.size() >0 && approvalList != null) {
        
	        for(int i=0; i<approvalList.size(); i++) {
	        	
	        	Map<String,String> apMap = approvalList.get(i);
	        	
	        	// 행생성
	        	bodyRow = sheet.createRow(i+ (rowLocation+1)); // i + 2
	        	
	        	bodyCell = bodyRow.createCell(0);
	        	bodyCell.setCellValue(apMap.get("apno"));
	        	
	        	bodyCell = bodyRow.createCell(1);
	        	bodyCell.setCellValue(apMap.get("apcaname"));
	        	
	        	bodyCell = bodyRow.createCell(2);
	        	bodyCell.setCellValue(apMap.get("subject"));
	        	
	        	bodyCell = bodyRow.createCell(3);
	        	bodyCell.setCellValue(apMap.get("apdate"));
	        	
	        	bodyCell = bodyRow.createCell(4);
	        	if("0".equals(request.getParameter("apstatus"))) {
	             	bodyCell.setCellValue(apMap.get("departmentname")+"부 "+ apMap.get("positionname") +" "+ apMap.get("name"));
	         	}
	         	else {
	         		bodyCell.setCellValue(apMap.get("eddate"));
	         	}
	        	
	        }// end of for---------------------------
	        
        }
        model.addAttribute("locale", Locale.KOREA); // 원화로 하기위해
        model.addAttribute("workbook", workbook); 
        model.addAttribute("workbookName", "기안한 문서");  // 파일명에 사용
        
		return "excelDownloadView";
	//  "excelDownloadView" 은 
	//  /WEB-INF/spring/appServlet/servlet-context.xml 파일에서
	//  뷰리졸버 0 순위로 기술된 bean 의 id 값이다.
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	//  === 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 생성 ===    
	public void getCurrentURL(HttpServletRequest request) {
	HttpSession session = request.getSession();
	session.setAttribute("goBackURL", MyUtil_KGH.getCurrentURL(request));
	}
	////////////////////////////////////////////////////////////////////////////////
}
