package com.spring.finalProject.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.finalProject.common.FileManager;
import com.spring.finalProject.common.GoogleMail_KGH;
import com.spring.finalProject.common.MyUtil_KGH;
import com.spring.finalProject.common.Sha256;
import com.spring.finalProject.model.DepartmentVO_KGH;
import com.spring.finalProject.model.EmployeeVO_KGH;
import com.spring.finalProject.model.PositionVO_KGH;
import com.spring.finalProject.service.InterKGHService;

@Controller
public class KGHController {
	
	@Autowired
	private InterKGHService service;
	
	@Autowired
	private FileManager FileManager;
	
	@RequestMapping(value = "/login.gw")
	public ModelAndView login(ModelAndView mav) {
		
		mav.setViewName("main/login.tiles1");
		
		return mav;
	}

	
	// === 로그인 처리 메서드 === //
	@RequestMapping(value = "/loginEnd.gw", method = {RequestMethod.POST})
	public ModelAndView loginEnd(HttpServletRequest request, ModelAndView mav) {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("email", email);
		paraMap.put("password", Sha256.encrypt(password));
		
		// === 로그인 처리 메서드 === //
		EmployeeVO_KGH loginuser = service.getLogin(paraMap);
		
		if(loginuser == null) {
			// 로그인에 실패한 경우
			String message = "이메일 또는 암호가 틀립니다.";
			String loc = "javascript:history.back()";
			
			mav.addObject("message", message);	// request.setAttribute("message", message);
			mav.addObject("loc", loc);			// request.setAttribute("loc", loc);
			
			mav.setViewName("msg");				// return "msg";	
		}
		else {
			if("1".equals(loginuser.getRetire())) {	// 로그인 한 지 1년이 경과한 경우
				String message = "귀하는 퇴사한 직원으로 로그인이 불가합니다.";
	            String loc = request.getContextPath() + "/index.gw";
	            // 원래는 위와같이 index.action 이 아니라 휴면인 계정을 풀어주는 페이지로 잡아주어야 한다.
	            
	            mav.addObject("message", message);
	            mav.addObject("loc", loc);
	            
	            mav.setViewName("msg");
	        //  /WEB-INF/views/msg.jsp 파일을 생성한다.
			}
			else {
				HttpSession session = request.getSession();
				// 메모리에 생성되어져 있는 session을 불러오는 것이다.
				
				session.setAttribute("loginuser", loginuser);
				// session(세션)에 로그인 되어진 사용자 정보인 loginuser 을 키이름을 "loginuser" 으로 저장시켜두는 것이다.
				
				String goBackURL = (String)session.getAttribute("goBackURL");
				// 예를 들면 goBackURL은 shop/prodView.up?pnum=58 이거나
				// null 이다.
				
				if(goBackURL != null) {
					mav.setViewName("redirect:/" + goBackURL);	// return "redirect:/" + goBackURL;
					
					session.removeAttribute("goBackURL");	// 세션에서 반드시 제거해주어야 한다.
				}
				else {
					mav.setViewName("redirect:/index.gw");	// return "redirect:/index.gw"
				}
				
			}
		}
		return mav;
	}
	
	
	// === #50. 로그아웃 처리하기 === //
	@RequestMapping(value="/logout.gw")
	public ModelAndView logout(ModelAndView mav, HttpServletRequest request) {
	   
	   HttpSession session = request.getSession();
	   
	   String goBackURL = (String) session.getAttribute("goBackURL");
	   
	   session.invalidate();
	   
	   String message = "로그아웃 되었습니다.";
	   
	   String loc = "";
	   if(goBackURL != null) {
	      loc = request.getContextPath() + goBackURL;
	   }
	   else {
	      loc = request.getContextPath() + "/index.gw";
	   }
	   
	   mav.addObject("message", message); 
	   mav.addObject("loc", loc);         
	   mav.setViewName("msg");
	   
	   return mav;
	}
	
	
	// === 이메일 찾기 팝업창 메서드 === //
	@RequestMapping(value = "emailFind.gw")
	public ModelAndView emailFind(ModelAndView mav) {
		mav.setViewName("tiles1/main/emailFind");
		return mav;
	}
	
	
	// === 이메일 찾기 완료 메서드 === //
	@ResponseBody
	@RequestMapping(value = "/emailFindEnd.gw", method = {RequestMethod.POST}, produces = "text/plain;charset=UTF-8")
	public String emailFindEnd(HttpServletRequest request) {
		String employeeid = request.getParameter("employeeid");
		String name = request.getParameter("name");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("name", name);
		
		// === 이메일 찾기 완료 메서드(select) === //
		String email = service.emailFindEnd(paraMap);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("email", email);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 비밀번호 찾기 팝업창 메서드 === //
	@RequestMapping(value = "passwordFind.gw")
	public ModelAndView passwordFind(ModelAndView mav) {
		mav.setViewName("tiles1/main/passwordFind");
		return mav;
	}

	
	// === 비밀번호 찾기 인증번호 발송 메서드 === //
	@ResponseBody
	@RequestMapping(value = "/sendCodeEmail.gw", method = {RequestMethod.POST}, produces = "text/plain;charset=UTF-8")
	public String sendCodeEmail(HttpServletRequest request) {
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("email", email);
		paraMap.put("name", name);
		
		// === 해당하는 이메일과 이름에 존재하는 사원 정보 찾기(select) === //
		boolean isExists = service.sendCodeEmail(paraMap);
		
		boolean sendEmail = false;
		
		if(isExists) {
			// 회원으로 존재하는 경우
			
			// 인증키를 랜덤하게 생성하도록 한다.
			Random rnd = new Random();
			
			String certificationCode = "";
			// 인증키는 영문소문자 5글자 + 숫자 7글자 로 만들겠습니다.
			// 예: certificationCode = awkfk7274003
			
			char randchar = ' ';
			for(int i=0; i < 5; i++) {
			/*
			    min 부터 max 사이의 값으로 랜덤한 정수를 얻으려면 
			    int rndnum = rnd.nextInt(max - min + 1) + min;
			       영문 소문자 'a' 부터 'z' 까지 랜덤하게 1개를 만든다.  	
			 */		
				
			randchar = (char) (rnd.nextInt('z' - 'a' + 1) + 'a');
			certificationCode += randchar;
			
			}
			
			int randnum = 0;
			for(int i=0; i < 5; i++) {
				randnum = rnd.nextInt(9 - 0 + 1) + 0;
				certificationCode += randnum;
			}
			
			// 랜덤하게 생성한 인증코드(certificationCode)를 비밀번호 찾기를 하고자 하는 사용자의 email로 전송시킨다.
			GoogleMail_KGH mail = new GoogleMail_KGH();
			
			try {
				mail.sendmail(email, certificationCode);
				sendEmail = true; // 메일 전송 성공했음을 기록함.
				
				// 세션불러오기
				HttpSession session = request.getSession();
				session.setAttribute("certificationCode", certificationCode);
				// 발급한 인증코드를 세션에 저장시킴.
				
			} catch(Exception e) { // 메일 전송이 실패한 경우
				e.printStackTrace();
				sendEmail = false; // 메일 전송 실패했음을 기록함.
			}
		}

		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("sendEmail", sendEmail);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 인증번호 체크 메서드 === ///
	@ResponseBody
	@RequestMapping(value = "/checkCode.gw", method = {RequestMethod.POST}, produces = "text/plain;charset=UTF-8")
	public String checkCode(HttpServletRequest request) {
		// 입력한 인증번호 값 가져오기
		String codeNo = request.getParameter("codeNo");
		
		// 세션불러오기
		HttpSession session = request.getSession();
		String certificationCode = (String)session.getAttribute("certificationCode");
		
		String message = "";
		boolean isSuccess = false;
		
		if(certificationCode.equals(codeNo)) {
			message = "인증 성공되었습니다.";
			isSuccess = true;
		}
		else {
			message = "인증코드가 일치하지 않습니다. 다시 시도하세요.";
			isSuccess = false;
		}
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("message", message);
		jsonObj.put("isSuccess", isSuccess);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 새비밀번호 업데이트 메서드(update) === //
	@ResponseBody
	@RequestMapping(value = "/newPasswordUpdate.gw", method = {RequestMethod.POST})
	public String newPasswordUpdate(HttpServletRequest request) {
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String newPassword = request.getParameter("newPassword");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("email", email);
		paraMap.put("name", name);
		paraMap.put("newPassword", Sha256.encrypt(newPassword));
		
		int n = service.newPasswordUpdate(paraMap);
		
		boolean isSuccess = false;
		JSONObject jsonObj = new JSONObject(); // {}
		
		if(n == 1) {
			isSuccess = true;
			jsonObj.put("isSuccess", isSuccess);
		}
		else {
			isSuccess = false;
			jsonObj.put("isSuccess", isSuccess);
		}
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 마이페이지 이동 메서드 === //
	@RequestMapping(value = "mypage.gw")
	public ModelAndView requiredLogin_mypage(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		mav.setViewName("main/mypage.tiles1");
		return mav;
	}
	
	
	// === 기존 비밀번호와 같은지 체크 메서드(select) === //
	@ResponseBody
	@RequestMapping(value = "/passwordCheck.gw", method = {RequestMethod.POST})
	public String passwordCheck(HttpServletRequest request) {
		String employeeid = request.getParameter("employeeid");
		String password = request.getParameter("password");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("password", Sha256.encrypt(password));
		
		boolean isExists = service.passwordCheck(paraMap);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isExists", isExists);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 마이페이지 수정 완료 메서드(update) === //
	@RequestMapping(value = "/mypageEnd.gw", method = {RequestMethod.POST})
	public ModelAndView mypageEnd(ModelAndView mav, MultipartHttpServletRequest mrequest, EmployeeVO_KGH empvo) {
		
		String employeeid = empvo.getEmployeeid();
		String password = empvo.getPassword();
		
		empvo.setPassword(Sha256.encrypt(password));
		
		// === 해당하는 사원의 파일이 존재하는 경우 해당하는 파일명 가져오기(select) === //
		String fileName = service.getprofileName(employeeid);
		
		if(fileName != null && !"".equals(fileName)) {
			String path = "C:\\git\\FinalProject\\FinalProject\\src\\main\\webapp\\resources\\empIMG";
          
			try {
				// 기존의 프로필 파일명에 대한 삭제 처리하기
				FileManager.doFileDelete(fileName, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		MultipartFile attach = empvo.getAttach();
		
		int n = 0;
		
		if(!attach.isEmpty()) {
			// === 정보 수정에 해당하는 프로필 사진 있을 경우 새로 저장하기 === //
			String path = "C:\\git\\FinalProject\\FinalProject\\src\\main\\webapp\\resources\\empIMG";
			
			String profilename = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				// 새로운 프로필 사진 업로드하기
				profilename = FileManager.doFileUpload(bytes, attach.getOriginalFilename(), path);
				
				empvo.setProfilename(profilename);
				empvo.setOrgProfilename(attach.getOriginalFilename());
				
				fileSize = attach.getSize();
				empvo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(attach.isEmpty()) {
			// === 파일이 없는 사원의 정보 update 해주기 === //
			n = service.mypageEndNoFile(empvo);
		}
		else {
			// === 해당하는 사원의 정보 update해주기 === //
			n = service.mypageEnd(empvo);
		}
		
		if(n == 1) {
			HttpSession session = mrequest.getSession();
			EmployeeVO_KGH loginuser = (EmployeeVO_KGH)session.getAttribute("loginuser");
			loginuser.setName(empvo.getName());
			loginuser.setMobile(empvo.getMobile());
			loginuser.setProfilename(empvo.getProfilename());
			loginuser.setOrgProfilename(empvo.getOrgProfilename());
			loginuser.setFileSize(empvo.getFileSize());
			session.setAttribute("loginuser", loginuser);
			
			String msg = "정보 수정이 완료되었습니다.";
			String loc = mrequest.getContextPath() + "/index.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "정보 수정이 실패하였습니다.";
			String loc = mrequest.getContextPath() + "/index.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		return mav;
	}
	
	
	// === 직원 목록 보기 메서드 === //
	@RequestMapping(value = "admin/empList.gw")
	public ModelAndView requiredAdmin_empList(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		List<Map<String, String>> empList = null;
		
		String department = request.getParameter("departmentname");
		String position = request.getParameter("positionname");
		String searchEmp = request.getParameter("searchEmp");
		
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if("전체".equals(department) || department == null) {
			department = "";
		}
		
		if("전체".equals(position) || position == null) {
			position = "";
		}
		
		if(searchEmp == null || "".equals(searchEmp) || searchEmp.trim().isEmpty() ) {
			searchEmp = "";
	    }
		
		if(!"".equals(searchEmp)) {
			department = "";
			position = "";
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("searchEmp", searchEmp);
		}
		
		Map<String,String> paraMap = new HashMap<>();
	    paraMap.put("department", department);
	    paraMap.put("position", position);
	    paraMap.put("searchEmp", searchEmp);
	    
	    // 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.
	    int totalCount = 0;			// 총 게시물 건수
	    int sizePerPage = 5;		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0;	// 현재 보여주는 페이지 번호로서, 초기값은 1 페이지로 설정해야 한다.
	    int totalPage = 0;			// 총 페이지 수(웹브라우저 상에서 보여줄 총 페이지 개수, 페이지바)
	    
	    int startRno = 0;			// 시작 행번호
	    int endRno = 0;				// 끝 행번호
		
		// === 총 게시물 건수(totalCount) 가져오기(select) === //
		totalCount = service.getTotalCount(paraMap);
		// System.out.println("~~~ 확인용 totalCount : " + totalCount);
		// ~~~ 확인용 totalCount : 6
		
		// 총 게시물 건수(totalCount)가 127개 일 경우
	    // 총 페이지 수(totalPage)는 13개 되어야 한다.
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);	// (double)127 / 10 => 12.7 => Math.ceil(12.7) => 13.0 => (int)13.0
	    
	    if(str_currentShowPageNo == null) {
	    	// 게시판에 보여지는 초기화면
	    	currentShowPageNo = 1; 
	    }
	    else {
	    	try {
	    		currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
			
	    		if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
	    			currentShowPageNo = 1;
	    		}
	    	} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
	    }
		
		// **** 가져올 게시글의 범위를 구한다.(공식임!!!) **** 
        /*
            currentShowPageNo      startRno     endRno
           --------------------------------------------
               1 page        ===>    1           10
               2 page        ===>    11          20
               3 page        ===>    21          30
               4 page        ===>    31          40
               ......                ...         ...
        */
		
	    startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
	    endRno = startRno + sizePerPage - 1;

	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
		
		// 페이징 처리한 직원 목록 가져오기(검색이 있든지, 검색이 없든지 다 포함된 것)
		empList = service.getEmpListWithPaging(paraMap);
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		mav.addObject("paraMap", paraMap);
		
		// === 페이지바 만들기 === //
		int blockSize = 10;
		// blockSize는 1개 블럭(토막)당 보여지는 페이지 번호의 개수이다.
		/*
			  	  		  1  2  3  4  5  6  7  8  9 10 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  11 12 13 14 15 16 17 18 19 20 [다음][마지막]  -- 1개블럭
			[맨처음][이전]  21 22 23
		*/
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
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
	            ...
	            21                    21 = ((21 - 1)/10) * 10 + 1
	            22                    21 = ((22 - 1)/10) * 10 + 1
	            23                    21 = ((23 - 1)/10) * 10 + 1
	            ..                    ..
	            29                    21
	            30                    21 = ((30 - 1)/10) * 10 + 1
	    */
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "/finalProject/admin/empList.gw";
		
		// === [맨처음][이전] 만들기 === //
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?department=" + department + "&position=" + position + "&searchEmp=" + searchEmp + "&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?department=" + department + "&position=" + position + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + (pageNo-1) + "'>[이전]</a></li>";
		}
		
		while(!(loop > blockSize || pageNo > totalPage)) {
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; background-color: #666666; font-weight:bold; color:white; padding:2px 4px;'>" + pageNo + "</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url + "?department=" + department + "&position=" + position + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + pageNo + "'>" + pageNo + "</a></li>";
			}
			
			loop++;
			pageNo++;
		}
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?department=" + department + "&position=" + position + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + pageNo + "'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?department=" + department + "&position=" + position + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		mav.addObject("empList", empList);
		mav.setViewName("admin/empList.tiles_KKH");
		
		return mav;
	}
	
	
	// === 직원등록 페이지 이동 === //
	@RequestMapping(value = "admin/empRegister.gw")
	public ModelAndView requiredAdmin_empRegister(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		mav.setViewName("admin/empRegister.tiles_KKH");
		return mav;
	}
	
	
	// === 부서목록 가져오기(ajax) === //
	@ResponseBody
	@RequestMapping(value = "/getDepartmentName.gw", method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
	public String getDepartmentName(HttpServletRequest request) {
		
		// === 부서목록 가져오기(select) === //
		List<DepartmentVO_KGH> departList = service.getDepartmentName();
		
		JSONArray jsonArr = new JSONArray();	// []
		
		if(departList != null) {
			for(DepartmentVO_KGH department : departList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("depart", department.getDepartmentname());
				jsonObj.put("departno", department.getDepartno());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	// === 직급 목록 가져오기(ajax) === //
	@ResponseBody
	@RequestMapping(value = "/getPositionName.gw", method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
	public String getPositionName(HttpServletRequest request) {
		
		// === 직급 목록 가져오기(select) === //
		List<PositionVO_KGH> positionList = service.getPosition();
		
		JSONArray jsonArr = new JSONArray();
		
		if(positionList != null) {
			for(PositionVO_KGH position : positionList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("position", position.getPositionname());
				jsonObj.put("positionno", position.getPositionno());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	// === 검색어 자동생성하기 === //
	@ResponseBody
	@RequestMapping(value = "/employeeSearch.gw", method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
	public String employeeSearch(HttpServletRequest request) {
		
		String searchEmployee = request.getParameter("searchEmployee");
		
		Map<String, String> paraMap = new HashedMap<>();
		paraMap.put("searchEmployee", searchEmployee);
		
		// === 검색어 결과 조회하기(select) === //
		List<String> searchList = service.employeeSearch(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(searchList != null) {
			for(String empName : searchList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("empName", empName);
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// === 이메일 중복 여부 체크 메서드 === //
	@ResponseBody
	@RequestMapping(value = "/emailDuplicateCheck.gw", method = {RequestMethod.POST})
	public String emailDuplicateCheck(HttpServletRequest request) {
		String email = request.getParameter("email");
		
		// === 이메일 중복 여부 검사하기(select) === //
		boolean isExists = service.emailDuplicateCheck(email);

		// System.out.println(isExists);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isExists", isExists);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	// === 직원 등록 완료 메서드 === //
	@RequestMapping(value = "/empRegisterEnd.gw", method = {RequestMethod.POST})
	public ModelAndView empRegisterEnd(ModelAndView mav, EmployeeVO_KGH emp, MultipartHttpServletRequest mrequest) {
		
		MultipartFile attach = emp.getAttach();
		
		if(!attach.isEmpty()) {
			// String root = mrequest.getContextPath();
			
			// System.out.println(root);
			// C:\git\FinalProject\FinalProject\src\main\webapp\resources\images
			
			String path = "C:\\git\\FinalProject\\FinalProject\\src\\main\\webapp\\resources\\empIMG";
			
			String profilename = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				// 새로운 프로필 사진 업로드하기
				profilename = FileManager.doFileUpload(bytes, attach.getOriginalFilename(), path);
				
				emp.setProfilename(profilename);
				emp.setOrgProfilename(attach.getOriginalFilename());
				
				fileSize = attach.getSize();
				emp.setFileSize(String.valueOf(fileSize));
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 새로 생성될 사원번호 조회하기
		String employeeId = service.selectEmpId(emp.getFk_departNo());
		emp.setEmployeeid(employeeId);

		// 새로 생성된 사원번호에 암호화처리하여 비밀번호 값에 넣어주기
		emp.setPassword(Sha256.encrypt(employeeId));
		
		int n = 0;
		
		if(attach.isEmpty()) {
			// 프로필 사진이 없는 경우
			// 직원 정보 insert 하기
			n =  service.empRegister(emp);
		}
		else {
			// 프로필 사진이 있는 경우
			// 프로필사진과 함께직원 정보 insert 하기
			n = service.empRegisterWithProfile(emp);
			
		}
		
		if(n == 1) {
			String msg = "직원 등록이 완료되었습니다.";
			String loc = mrequest.getContextPath() + "/index.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "직원 등록에 실패하였습니다.";
			String loc = mrequest.getContextPath() + "/index.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 멤버 상세 정보 및 수정 페이지 이동 === //
	@RequestMapping(value = "admin/empListEdit.gw")
	public ModelAndView requiredAdmin_empListEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String employeeID = request.getParameter("empId");
		
		// === 특정 회원에 대한 정보 가져오기(select) === //
		Map<String, String> map = service.empListEdit(employeeID);
		
		mav.addObject("map", map);
		mav.setViewName("admin/empListEdit");
		
		return mav;
	}
	
	
	// === 멤버 정보 수정 완료 === //
	@RequestMapping(value = "/empEditEnd.gw", method = {RequestMethod.POST})
	public ModelAndView empEditEnd(ModelAndView mav, HttpServletRequest request, EmployeeVO_KGH emp) {
		
		String departno = request.getParameter("selectDepart");
		String positionno = request.getParameter("selectPosition");
		
		emp.setFk_departNo(departno);
		emp.setFk_positionNo(positionno);;
		
		int result = 1;
		
		// 정보 수정하려는 직급이 팀장일 경우 tbl_department의 managerid도 update 한다
		if(Integer.parseInt(positionno) == 2) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("departno", departno);
			paraMap.put("positionno", positionno);
			paraMap.put("employeeid", emp.getEmployeeid());
			
			result = service.updateDepart(paraMap);
		}
		
		if(result == 1) {
			// === 직원 정보 수정하기(update) === //
			int n = service.empEdit(emp);
			
			if(n == 1) {
				String msg = "수정이 완료되었습니다.";
				String loc = request.getContextPath() + "/admin/empList.gw";
				
				mav.addObject("message", msg);
				mav.addObject("loc", loc);
			}
			else {
				String msg = "수정이 실패되었습니다.";
				String loc = request.getContextPath() + "/admin/empList.gw";
				
				mav.addObject("message", msg);
				mav.addObject("loc", loc);
			}
		}
		else {
			String msg = "수정이 실패되었습니다.";
			String loc = request.getContextPath() + "/admin/empList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 직원정보 삭제하기 === //
	@RequestMapping(value = "/empDelEnd.gw", method = {RequestMethod.POST})
	public ModelAndView empDelEnd(ModelAndView mav, HttpServletRequest request, EmployeeVO_KGH emp) {
		String positionno = request.getParameter("selectPosition");
		String employeeid = emp.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("positionno", positionno);
		
		// === 삭제하고자 하는 직원의 정보가 팀장일 경우 부서 테이블 managerid null처리 (update) === //
		if(Integer.parseInt(positionno) == 2) {
			service.delManagerId(paraMap);
		}
		
		// === 삭제하고자 하는 직원의 정보 update(admin, retire, retiredate) === //
		int n = service.empDelEnd(paraMap);
		
		if(n == 1) {
			String msg = "직원 삭제가 성공하였습니다.";
			String loc = request.getContextPath() + "/admin/empList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "직원 삭제가 실패되었습니다.";
			String loc = request.getContextPath() + "/admin/empList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		return mav;
	}
	
	
	// === 직원 통계 차트 보여주는 페이지 === //
	@RequestMapping(value = "/admin/empChart.gw")
	public ModelAndView requiredAdmin_empChart(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// === 부서목록 가져오기(select) === //
		List<DepartmentVO_KGH> departList = service.getDepartmentName();
		
		String departToString = "";
		
		for (int i = 0; i < departList.size(); i++) {
			if(i == departList.size() - 1) {
				departToString += departList.get(i).getDepartmentname();
			}
			else {
				departToString += departList.get(i).getDepartmentname() + ", ";
			}
		}
		
		// === 부서별 인원 가져오기(select) === //
		List<String> departEmpCnt = service.getDepartempCnt();
		
		String departEmpCntToString = "";
		
		for (int i = 0; i < departEmpCnt.size(); i++) {
			if(i == departList.size() - 1) {
				departEmpCntToString += departEmpCnt.get(i);
			}
			else {
				departEmpCntToString += departEmpCnt.get(i) + ", ";
			}
		}
		
		mav.addObject("departToString", departToString);
		mav.addObject("departEmpCntToString", departEmpCntToString);
		
		mav.setViewName("admin/empChart.tiles_KKH");
		
		return mav;
	}
	
	
	// === 직원목록 엑셀파일 다운로드 받기 === //
	@RequestMapping(value = "/admin/excelEmpList.gw", method = {RequestMethod.POST})
	public String excelEmpList(HttpServletRequest request, Model model) {
		
		String depart = request.getParameter("excelDepart");
		String position = request.getParameter("excelPosition");
		String searchEmp = request.getParameter("excelSearchEmp");
		
		if("전체".equals(depart)) {
			depart = "";
		}
		
		if("전체".equals(position)) {
			position = "";
		}
		
		if(searchEmp != "") {
			depart = "";
			position = "";
		}
		
		Map<String, String> paraMap = new HashedMap<>();
		paraMap.put("department", depart);
		paraMap.put("position", position);
		paraMap.put("searchEmp", searchEmp);
		
		List<Map<String, String>> excelEmpList = null;
		
		// === 엑셀에 입력할 직원 정보 가져오기 === //
		excelEmpList = service.excelEmpList(paraMap);
		
		// === 조회결과물인 empList 를 가지고 엑셀 시트 생성하기 ===
		// 시트를 생성하고, 행을 생성하고, 셀을 생성하고, 셀안에 내용을 넣어주면 된다.
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		
		SXSSFSheet sheet = workbook.createSheet("코코아워크 사원정보");
		
		// 시트 열 너비 설정(ex:8열)
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 2000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 2000);
		sheet.setColumnWidth(7, 3500);
		
		// 행의 위치를 나타내는 변수
		int rowLocation = 0;
		
		////////////////////////////////////////////////////////////////////////////////////////
		// CellStyle 정렬하기(Alignment)
		// CellStyle 객체를 생성하여 Alignment 세팅하는 메소드를 호출해서 인자값을 넣어준다.
		// 아래는 HorizontalAlignment(가로)와 VerticalAlignment(세로)를 모두 가운데 정렬 시켰다.
		
		CellStyle mergeRowStyle = workbook.createCellStyle();
		mergeRowStyle.setAlignment(HorizontalAlignment.CENTER);
		mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		
		// CellStyle 배경색(ForegroundColor)만들기
        // setFillForegroundColor 메소드에 IndexedColors Enum인자를 사용한다.
        // setFillPattern은 해당 색을 어떤 패턴으로 입힐지를 정한다.
	    mergeRowStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
	    mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
	    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    
	    Font mergeRowFont = workbook.createFont();
	    mergeRowFont.setFontName("돋움");
	    mergeRowFont.setFontHeight((short)500);
	    mergeRowFont.setColor(IndexedColors.WHITE.getIndex());
	    mergeRowFont.setBold(true);
	    
	    mergeRowStyle.setFont(mergeRowFont);
	    
	    // CellStyle 테두리 Border
	    // 테두리는 각 셀마다 상하좌우 모두 설정해준다.
	    // setBorderTop, Bottom, Left, Right 메서드와 인자로 POI 라이브러리의 BorderStyle 인자를 넣어서 적용한다.
	    mergeRowStyle.setBorderTop(BorderStyle.THICK);
	    mergeRowStyle.setBorderBottom(BorderStyle.THICK);
	    mergeRowStyle.setBorderLeft(BorderStyle.THICK);
	    mergeRowStyle.setBorderRight(BorderStyle.THICK);;
	    
	    headerStyle.setBorderTop(BorderStyle.THICK);
	    headerStyle.setBorderBottom(BorderStyle.THICK);
	    headerStyle.setBorderLeft(BorderStyle.THICK);
	    headerStyle.setBorderRight(BorderStyle.THICK);
	    
	    // Cell Merge 셀 병합시키기
	    /*
	              셀병합은 시트의 addMergeRegion 메소드에 CellRangeAddress 객체를 인자로 하여 병합시킨다.
           CellRangeAddress 생성자의 인자로(시작 행, 끝 행, 시작 열, 끝 열) 순서대로 넣어서 병합시킬 범위를 정한다. 배열처럼 시작은 0부터이다.  
	    */
	    // 병합할 행 만들기
	    Row mergeRow = sheet.createRow(rowLocation);
	    
	    // 병합할 행에 "회사 사원정보"로 셀을 만들어 셀에 스타일 주기
	    for (int i = 0; i < 8; i++) {
	    	// 셀 8개 만들기
			Cell cell = mergeRow.createCell(i);
			cell.setCellStyle(mergeRowStyle);
			cell.setCellValue("회사 사원정보");
		}
	    
	    // 셀 병합하기
	    sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, 7));
		
	    // CellStyle 천단위 쉼표, 금액
	    CellStyle moneyStyle = workbook.createCellStyle();
	    moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
	    
	    // 헤더 행 생성
	    // 엑셀에서 행의 시작은 0부터 시작
	    Row headerRow = sheet.createRow(++rowLocation);
	    
	    // 해당 행의 첫번째 열에 대한 셀 생성
	    Cell headerCell = headerRow.createCell(0);	// 엑셀에서 열의 시작은 0부터 시작
	    headerCell.setCellValue("사원번호");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 두번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(1);
	    headerCell.setCellValue("부서명");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 세번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(2);
	    headerCell.setCellValue("직급");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 네번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(3);
	    headerCell.setCellValue("이름");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 다섯번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(4);
	    headerCell.setCellValue("연락처");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 여섯번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(5);
	    headerCell.setCellValue("이메일");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 일곱번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(6);
	    headerCell.setCellValue("급여");
	    headerCell.setCellStyle(headerStyle);
	    
	    // 해당 행의 여덟번째 열에 대한 셀 생성
	    headerCell = headerRow.createCell(7);
	    headerCell.setCellValue("입사일자");
	    headerCell.setCellStyle(headerStyle);
	    
	    // === 사원정보에 해당하는 행 및 셀 생성하기 === //
	    Row bodyRow = null;
	    Cell bodyCell = null;
	    
	    for (int i = 0; i < excelEmpList.size(); i++) {
			Map<String, String> empMap = excelEmpList.get(i);
		
			// 행 생성
			bodyRow = sheet.createRow(i + (rowLocation + 1));
			
			// 데이터 사원번호 표시
			bodyCell = bodyRow.createCell(0);
			bodyCell.setCellValue(empMap.get("employeeid"));

			// 데이터 부서명 표시
			bodyCell = bodyRow.createCell(1);
			bodyCell.setCellValue(empMap.get("departmentname"));

			// 데이터 직급 표시
			bodyCell = bodyRow.createCell(2);
			bodyCell.setCellValue(empMap.get("positionname"));

			// 데이터 이름 표시
			bodyCell = bodyRow.createCell(3);
			bodyCell.setCellValue(empMap.get("name"));
			
			// 데이터 연락처 표시
			bodyCell = bodyRow.createCell(4);
			bodyCell.setCellValue(empMap.get("mobile"));
			
			// 데이터 이메일 표시
			bodyCell = bodyRow.createCell(5);
			bodyCell.setCellValue(empMap.get("email"));
						
			// 데이터 급여 표시
			bodyCell = bodyRow.createCell(6);
			bodyCell.setCellValue(Integer.parseInt(empMap.get("salary")));
			
			// 데이터 입사일자 표시
			bodyCell = bodyRow.createCell(7);
			bodyCell.setCellValue(empMap.get("hiredate"));
									
	    }
	    
	    model.addAttribute("locale", Locale.KOREA);
	    model.addAttribute("workbook", workbook);
	    model.addAttribute("workbookName", "사원정보");
	    
	    return "excelDownloadView";
	}
	
	
	// 부서 관리 페이지 이동
	@RequestMapping(value = "admin/department.gw")
	public ModelAndView requiredAdmin_department(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		List<Map<String, String>> empDepartList = null;
		
		String department = request.getParameter("department");
		String searchEmp = request.getParameter("searchEmp");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(department == null || "".equals(department) || department.trim().isEmpty() || "전체".equals(department)) {
			department = "";
		}
		
		if(searchEmp == null || "".equals(searchEmp) || searchEmp.trim().isEmpty() ) {
			searchEmp = "";
	    }
		
		Map<String,String> paraMap = new HashMap<>();
	    paraMap.put("department", department);
	    paraMap.put("position", "");
	    paraMap.put("searchEmp", searchEmp);
	    
	    // 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.
	    int totalCount = 0;			// 총 게시물 건수
	    int sizePerPage = 5;		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0;	// 현재 보여주는 페이지 번호로서, 초기값은 1 페이지로 설정해야 한다.
	    int totalPage = 0;			// 총 페이지 수(웹브라우저 상에서 보여줄 총 페이지 개수, 페이지바)
	    
	    int startRno = 0;			// 시작 행번호
	    int endRno = 0;				// 끝 행번호
		
	    // === 직원수 가져오기 메서드 === //
		totalCount = service.getTotalCount(paraMap);
		// System.out.println("~~~ 확인용 totalCount : " + totalCount);
		// ~~~ 확인용 totalCount : 6
		mav.addObject("empCnt", totalCount);
		
		// 총 게시물 건수(totalCount)가 127개 일 경우
	    // 총 페이지 수(totalPage)는 13개 되어야 한다.
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);	// (double)127 / 10 => 12.7 => Math.ceil(12.7) => 13.0 => (int)13.0
	    
	    if(str_currentShowPageNo == null) {
	    	// 게시판에 보여지는 초기화면
	    	currentShowPageNo = 1; 
	    }
	    else {
	    	try {
	    		currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
			
	    		if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
	    			currentShowPageNo = 1;
	    		}
	    	} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
	    }
		
	    startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
	    endRno = startRno + sizePerPage - 1;

	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
		
		// 페이징 처리한 직원 목록 가져오기(검색이 있든지, 검색이 없든지 다 포함된 것)
	    empDepartList = service.getEmpListWithPaging(paraMap);
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		mav.addObject("paraMap", paraMap);
		
		// === 페이지바 만들기 === //
		int blockSize = 10;
		// blockSize는 1개 블럭(토막)당 보여지는 페이지 번호의 개수이다.
	    
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "/finalProject/admin/department.gw";
		
		// === [맨처음][이전] 만들기 === //
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?department=" + department + "&searchEmp=" + searchEmp + "&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?department=" + department + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + (pageNo-1) + "'>[이전]</a></li>";
		}
		
		while(!(loop > blockSize || pageNo > totalPage)) {
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; background-color: #666666; font-weight:bold; color:white; padding:2px 4px;'>" + pageNo + "</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url + "?department=" + department + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + pageNo + "'>" + pageNo + "</a></li>";
			}
			
			loop++;
			pageNo++;
		}
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?department=" + department + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + pageNo + "'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?department=" + department + "&searchEmp=" + searchEmp + "&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		mav.addObject("empDepartList", empDepartList);
		
		mav.setViewName("admin/department.tiles_KKH");
		
		return mav;
	}
	
	
	// === 부서 추가 시 이미 존재하는 부서인지 중복확인 하는 메서드
	@ResponseBody
	@RequestMapping(value = "/departDuplicate.gw")
	public String departDuplicate(HttpServletRequest request) {
		String newDepart = request.getParameter("newDepart");
		
		boolean isExists = service.departDuplicate(newDepart);
		
		JSONObject jsonObj = new JSONObject(); // {}
		jsonObj.put("isExists", isExists);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	// === 해당하는 사원의 번호 존재여부 확인하는 메서드 === //
	@ResponseBody
	@RequestMapping(value = "isExistsEmpID.gw")
	public String isExistsEmpID(HttpServletRequest request) {
		String employeeid = request.getParameter("newDepartempID");
		
		boolean isExists = service.isExistsEmpID(employeeid);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("isExists", isExists);
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 부서 새로 추가하기 메서드 === //
	@RequestMapping(value = "/newDepartAddEnd.gw", method = {RequestMethod.POST})
	public ModelAndView newDepartAddEnd(HttpServletRequest request, ModelAndView mav, DepartmentVO_KGH departVO) {
		String newDepartname = request.getParameter("newDepartname");
		String newDepartempID = request.getParameter("newDepartempID");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("newDepartname", newDepartname);
		paraMap.put("newDepartempID", newDepartempID);
		
		int result = service.newDepartAddEnd(paraMap);
		
		if(result == 1) {
			String msg = "부서 등록이 완료되었습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "부서 등록이 실패하였습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		return mav;
	}
	
	
	// === 특정 부서 삭제하기 메서드 === //
	@RequestMapping(value = "/departDelEnd.gw", method = {RequestMethod.POST})
	public ModelAndView departDelEnd(ModelAndView mav, HttpServletRequest request) {
		String departno = request.getParameter("delDepart");
		
		int result = service.departDelEnd(departno);
		
		if(result == 1) {
			String msg = "부서 삭제가 완료되었습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "부서 삭제가 실패하였습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 부서명 수정하기 메서드 === //
	@RequestMapping(value = "/departEditEnd.gw", method = {RequestMethod.POST})
	public ModelAndView departEditEnd(ModelAndView mav, HttpServletRequest request) {
		String departno = request.getParameter("editDepart");
		String newDepartName = request.getParameter("eidtDepartName");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("departno", departno);
		paraMap.put("newDepartName", newDepartName);
				
		int n = service.departEditEnd(paraMap);
		
		if(n == 1) {
			String msg = "부서 수정이 완료되었습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "부서 수정이 실패하였습니다.";
			String loc = request.getContextPath() + "/admin/department.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 체크박스에 체크된 사원에 대한 부서변경(update) === //
	@ResponseBody
	@RequestMapping(value = "/changeDepartment.gw", method = {RequestMethod.POST})
	public String changeDepartment(HttpServletRequest request) {
		String str_checkempID = request.getParameter("str_checkempID");
		String departno = request.getParameter("changeDepart");

		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put("departno", departno);
		
		if(str_checkempID != null && !"".equals(str_checkempID)) {
			String[] checkEmpArr = str_checkempID.split(",");
			paraMap.put("checkEmpArr", checkEmpArr);
		}
		
		JSONObject jsonObj = new JSONObject();
		
		int n = service.changeDepartment(paraMap);
		
		if(n >= 1) {
			jsonObj.put("isSuccess", true);
		}
		else {
			jsonObj.put("isSuccess", false);
		}
		
		String json = jsonObj.toString();
		
		return json;
	}
	
	
	// === 관리자 목록 페이지로 이동 === //
	@RequestMapping(value = "admin/adminList.gw")
	public ModelAndView requiredAdmin_adminList(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		List<Map<String, String>> adminList = null;
		
		Map<String,String> paraMap = new HashMap<>();
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다.
	    int totalCount = 0;			// 총 게시물 건수
	    int sizePerPage = 5;		// 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0;	// 현재 보여주는 페이지 번호로서, 초기값은 1 페이지로 설정해야 한다.
	    int totalPage = 0;			// 총 페이지 수(웹브라우저 상에서 보여줄 총 페이지 개수, 페이지바)
	    
	    int startRno = 0;			// 시작 행번호
	    int endRno = 0;				// 끝 행번호
		
	    // === 관리자수 가져오기 메서드(select) === //
		totalCount = service.getTotalAdminCount();
		
		mav.addObject("adminCnt", totalCount);
		
		// 총 게시물 건수(totalCount)가 127개 일 경우
	    // 총 페이지 수(totalPage)는 13개 되어야 한다.
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);	// (double)127 / 10 => 12.7 => Math.ceil(12.7) => 13.0 => (int)13.0
	    
	    if(str_currentShowPageNo == null) {
	    	// 게시판에 보여지는 초기화면
	    	currentShowPageNo = 1; 
	    }
	    else {
	    	try {
	    		currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
			
	    		if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
	    			currentShowPageNo = 1;
	    		}
	    	} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
	    }
		
	    startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
	    endRno = startRno + sizePerPage - 1;

	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
		
	    // === 관리자 List 가져오기(select) === //
	 	adminList =	service.getAdminList(paraMap);
	    
	    // === 페이지바 만들기 === //
 		int blockSize = 10;
 		// blockSize는 1개 블럭(토막)당 보여지는 페이지 번호의 개수이다.
 	    
 		int loop = 1;
 		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
 		
 		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
 		
 		String pageBar = "<ul style='list-style: none;'>";
 		String url = "/finalProject/admin/adminList.gw";
 		
 		// === [맨처음][이전] 만들기 === //
 		if(pageNo != 1) {
 			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "currentShowPageNo=1'>[맨처음]</a></li>";
 			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "currentShowPageNo=" + (pageNo-1) + "'>[이전]</a></li>";
 		}
 		
 		while(!(loop > blockSize || pageNo > totalPage)) {
 			if(pageNo == currentShowPageNo) {
 				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; background-color: #666666; font-weight:bold; color:white; padding:2px 4px;'>" + pageNo + "</li>";
 			}
 			else {
 				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='" + url + "?&currentShowPageNo=" + pageNo + "'>" + pageNo + "</a></li>";
 			}
 			
 			loop++;
 			pageNo++;
 		}
 		
 		// === [다음][마지막] 만들기 ===
 		if(pageNo <= totalPage) {
 			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='" + url + "?currentShowPageNo=" + pageNo + "'>[다음]</a></li>";
 			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='" + url + "?&currentShowPageNo=" + totalPage + "'>[마지막]</a></li>";
 		}
 		
 		pageBar += "</ul>";
 		
 		mav.addObject("pageBar", pageBar);
	    
		mav.addObject("adminList", adminList);
		mav.setViewName("admin/adminList.tiles_KKH");
		
		return mav;
	}
	
	
	// === 관리자 추가 시 직원 검색할 경우 자동완성 할 직원 목록 select 메서드 === //
	@ResponseBody
	@RequestMapping(value = "/adminListSearch.gw", method = {RequestMethod.GET}, produces = "text/plain;charset=UTF-8")
	public String adminListSearch(HttpServletRequest request) {
		String searchEmployee = request.getParameter("searchEmployee");
		
		Map<String, String> paraMap = new HashedMap<>();
		paraMap.put("searchEmployee", searchEmployee);
		
		// === 관리자 메뉴 검색어 결과 조회하기(select) === //
		List<EmployeeVO_KGH> searchList = service.adminListSearch(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(searchList != null) {
			for(EmployeeVO_KGH employeeVO : searchList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("employeeid", employeeVO.getEmployeeid());
				jsonObj.put("name", employeeVO.getName());
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// === 관리자 추가 메서드(update) === //
	@RequestMapping(value = "/adminAddEnd.gw", method = {RequestMethod.POST})
	public ModelAndView adminAddEnd(ModelAndView mav, HttpServletRequest request) {
		String employeeid = request.getParameter("adminEmpId");
		
		int n = service.adminAddEnd(employeeid);
		
		if(n == 1) {
			String msg = "관리자 추가가 완료되었습니다.";
			String loc = request.getContextPath() + "/admin/adminList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "관리자 추가가 실패하였습니다.";
			String loc = request.getContextPath() + "/admin/adminList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	
	// === 관리자 권한 삭제 메서드(update) === //
	@RequestMapping(value = "/adminDelEnd.gw", method = {RequestMethod.POST})
	public ModelAndView adminDelEnd(ModelAndView mav, HttpServletRequest request) {
		String employeeid = request.getParameter("adminEmpId");
		
		int n = service.adminDelEnd(employeeid);
		
		if(n == 1) {
			String msg = "관리자 권한 삭제가 완료되었습니다.";
			String loc = request.getContextPath() + "/admin/adminList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		else {
			String msg = "관리자 권한 삭제가 실패하였습니다.";
			String loc = request.getContextPath() + "/admin/adminList.gw";
			
			mav.addObject("message", msg);
			mav.addObject("loc", loc);
		}
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	// === 직원 조직도 페이지 이동 === //
	@RequestMapping(value = "/organization-chart.gw")
	public ModelAndView requiredLogin_organization_chart(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		getCurrentURL(request);
		
		// === 부서목록 가져오기(select) === //
		List<DepartmentVO_KGH> departList = service.getDepartmentName();
		
		String departToString = "";
		
		for (int i = 0; i < departList.size(); i++) {
			if(i == departList.size() - 1) {
				departToString += departList.get(i).getDepartmentname();
			}
			else {
				departToString += departList.get(i).getDepartmentname() + ", ";
			}
		}
		
		mav.addObject("departToString", departToString);
		mav.setViewName("organization/organization_chart.tiles_KKH2");
		return mav;
	}
	
	
	// === 관리자 조직도 가져오기(select) === //
	@ResponseBody
	@RequestMapping(value = "/getOrganizationList.gw", produces = "text/plain;charset=UTF-8")
	public String getOrganizationList() {
		List<Map<String, String>> organizationList = service.getOrganization();
		
		Gson gson = new Gson();
		JsonArray jsonArr = new JsonArray();
		
		for(Map<String, String> map : organizationList) {
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("employeeid", map.get("employeeid"));
			jsonObj.addProperty("fk_departno", map.get("fk_departno"));
			jsonObj.addProperty("departmentname", map.get("departmentname"));
			jsonObj.addProperty("fk_positionno", map.get("fk_positionno"));
			jsonObj.addProperty("positionname", map.get("positionname"));
			jsonObj.addProperty("name", map.get("name"));
		
			jsonArr.add(jsonObj);
		}
		
		return gson.toJson(jsonArr);
	}
	
	////////////////////////////////////////////////////////
	// === 로그인 또는 로그아웃을 할 때 현재 페이지로 돌아가는 메서드 생성 === //
	public void getCurrentURL(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("goBackURL", MyUtil_KGH.getCurrentURL(request));
	}
	////////////////////////////////////////////////////////
}
