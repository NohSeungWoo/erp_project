package com.spring.finalProject.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spring.finalProject.common.MyUtil_KGH;
import com.spring.finalProject.common.Sha256;
import com.spring.finalProject.model.CommuteVO_SKS;
import com.spring.finalProject.model.EmployeeVO_SKS;
import com.spring.finalProject.model.VacationVO_SKS;
import com.spring.finalProject.model.WorkemployeeVO_SKS;
import com.spring.finalProject.service.InterSksService;


//==== #30. 컨트롤러 선언 ====
@Controller
public class SksController {
	
	@Autowired
	private InterSksService service;
	
	@RequestMapping(value="/timemanager.gw")
	public ModelAndView timemanger(HttpServletRequest request,HttpServletResponse response,VacationVO_SKS adevo, ModelAndView mav) {
		getCurrentURL(request);
		
		Map<String ,String> paraMap = new HashMap<>();
		List<VacationVO_SKS> vacationlistess = null;
		List<VacationVO_SKS> vacationlistse = null;
		List<VacationVO_SKS> vacationlistses = null;
		List<CommuteVO_SKS> Commutelistt = null;
		List<CommuteVO_SKS> Commutelistts = null;
		List<CommuteVO_SKS> Commutelistst = null;
		List<WorkemployeeVO_SKS> Employeeworklistt = null;
		List<WorkemployeeVO_SKS> Employeeworklistts = null;
		List<WorkemployeeVO_SKS> Employeeworklisttss = null;	
		
		Commutelistt = service.CommutelistListSearchs();
		Commutelistts = service.CommutelistListewSearchs();	
		Commutelistst = service.CommutelistListsewSearchs();
		vacationlistess = service.vacationlistsListSearch();
		vacationlistse = service.vacationlisteListSearch();
		vacationlistses = service.vacationlistesListSearch();
		Employeeworklistt = service.worklistsSearch();
		Employeeworklistts = service.worklistseSearch();
		Employeeworklisttss = service.worklistsesSearch();
		
		mav.addObject("Commutelistt", Commutelistt);
		mav.addObject("Commutelistts", Commutelistts);
		mav.addObject("Commutelistst", Commutelistst);
		mav.addObject("vacationlistse", vacationlistse);
		mav.addObject("vacationlistses", vacationlistses);
		mav.addObject("vacationlistess", vacationlistess);
		mav.addObject("Employeeworklistt",Employeeworklistt);
		mav.addObject("Employeeworklistts",Employeeworklistts);
		mav.addObject("Employeeworklisttss",Employeeworklisttss);
		mav.setViewName("timemanager.tiles_SKS");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/WorkCntByWork.gw", produces="text/plain;charset=UTF-8")
	public String WorkCntBymonthWork() {
		
		List<Map<String,String>> WorkPercentageList = service.WorkCntByWork();
		
		Gson gson = new Gson();
		JsonArray jsonArr = new JsonArray();
		
		for(Map<String,String> map : WorkPercentageList) {
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("Work_name", map.get("Work_name"));
			jsonObj.addProperty("cnt", map.get("cnt"));
			jsonObj.addProperty("percentage", map.get("percentage"));
			
			jsonArr.add(jsonObj);
		}
		
		return gson.toJson(jsonArr);
	}

	
	
	
	@ResponseBody
	@RequestMapping(value="/vacation.gw")
	public ModelAndView vacation(ModelAndView mav,HttpServletRequest request) {
		
		getCurrentURL(request); // 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 호출
		
		//System.out.println("확인용 : " + fk_dayoff);
		
		mav.setViewName("vacation.tiles_SKS");
		
		return mav;
	}


	@ResponseBody
	@RequestMapping(value="/vacationInsert.gw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String vacationInsert(ModelAndView mav,VacationVO_SKS vctvo, HttpServletRequest request) {
		
	//	int n = service.vacationInsert(vctvo);
		
		int n = 0;
		
		try {
			n = service.vacationInsert(vctvo);
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObj = new JSONObject();
 		jsonObj.put("n", n);
		jsonObj.put("name", vctvo.getName());
		
	//	mav.setViewName("test4.tiles1");
	//	mav.setViewName("redirect:/vacationlist.gw");
		
	//	return mav;
		return "/vactionlist.gw";
	}

	@RequestMapping(value="/vacationlist.gw")
	public ModelAndView vacationlist(ModelAndView mav, HttpServletRequest request) {
		
		 
		getCurrentURL(request);
		
		Map<String,String> paraMap = new HashMap<>();
		List<VacationVO_SKS> vacationlist = null;
		List<VacationVO_SKS> vacationlists = null;
		List<VacationVO_SKS> vacationlistes = null;
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(searchType == null || (!"vacation".equals(searchType) && !"name".equals(searchType)) ) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
			searchWord = "";
		}
		
		
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다. 
		int totalCount = 0;        // 총 게시물 건수 
		int sizePerPage = 5;      // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;         // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바) 
		
		int startRno = 0;          // 시작 행번호 
		int endRno = 0;            // 끝 행번호 
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getTotalCount(paraMap);
	//	System.out.println("~~~~ 확인용 totalCount : " + totalCount);
		
		// 만약에 총 게시물 건수(totalCount)가 127개 이라면 
		// 총 페이지수(totalPage)는 13개가 되어야 한다.
		
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage); // (double)127/10 ==> 12.7 ==> Math.ceil(12.7) ==> 13.0 ==> (int)13.0 ==> 13 
		
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
			} catch(NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
		endRno = startRno + sizePerPage - 1;
		
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		
		
		// 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것)
		
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		
		// === #121. 페이지바 만들기 === //
		int blockSize = 2;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수 이다.
		
		int loop = 1;
		
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "vacationlist.gw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage) ) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
			
		}// end of while------------------------
		
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		
			
		
		vacationlist = service.vacationListSearchWithPaging(paraMap);
		vacationlistes = service.vacationlistListSearch(paraMap);	
		
		
		String gobackURL = MyUtil_KGH.getCurrentURL(request);
		vacationlists = service.vacationlistListNoSearch(paraMap);
		mav.addObject("vacationlists",vacationlists);
		mav.addObject("gobackURL", gobackURL);
		
		mav.addObject("totalCount",totalCount);
		mav.addObject("vacationlist", vacationlist);
		mav.addObject("vacationlistes", vacationlistes);
	    mav.setViewName("vacationlist.tiles_SKS");
		
		return mav;
	}
	
	@RequestMapping(value="/vacationview.gw")
	public ModelAndView vacationview(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		VacationVO_SKS vacvo = null;
		vacvo = service.getvacationViewNocount(paraMap);
		
		mav.addObject("vacvo",vacvo);
		
		mav.setViewName("vacationview.tiles_SKS");
		
		return mav;
	}
	
	@RequestMapping(value="/vacationedit.gw")
	public ModelAndView edit(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		VacationVO_SKS vacvo = service.getvacationViewNocount(paraMap);
		
		mav.addObject("vacvo", vacvo);
		mav.setViewName("vacationedit.tiles_SKS");
		
		return mav;
	}
	
 	
	
	@RequestMapping(value="/vacationeditEnd.gw", method= {RequestMethod.POST})
	public ModelAndView vacationeditEnd(ModelAndView mav, VacationVO_SKS vacvo, HttpServletRequest request) {
		
		int n = service.vacationedit(vacvo);
		
		
		mav.setViewName("redirect:/vacationlist.gw");
		return mav;
		
	}
	
	@RequestMapping(value="/vacationdel.gw")
	public ModelAndView vacationdel(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 글 삭제해야할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		// 삭제해야할 글1개 내용 가져와서 로그인한 사람이 쓴 글이라면 글삭제가 가능하지만
		// 다른 사람이 쓴 글은 삭제가 불가하도록 해야 한다.
		Map<String, String> paraMap = new HashMap<>(); 
		paraMap.put("seq", seq);
		
		VacationVO_SKS vacvo = service.getvacationViewNocount(paraMap); 
		
		mav.addObject("seq", seq);
		mav.setViewName("vacationdel.tiles_SKS");
		
		return mav;
	
	}//end of public ModelAndView requiredLogin_del(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
//======================================================================================
	// === #77. 글삭제 페이지 완료하기  === //
	@RequestMapping(value="/vacationdelEnd.gw", method= {RequestMethod.POST})
	public ModelAndView vacationdelEnd(ModelAndView mav, HttpServletRequest request) {
		
		// 글 삭제를 하려면 원본글의 글암호와 삭제시 입력해준 암호가 일치할때만
		// 글 삭제가 가능하도록 해야 한다.
		String seq = request.getParameter("seq");
		String name = request.getParameter("name");
		
		Map<String, String> paraMap = new HashMap<>(); 
		paraMap.put("name", name);
		paraMap.put("seq", seq);
		
		// 글 삭제하기
		int n = service.vacationdel(paraMap);
		
	
		mav.setViewName("redirect:/vacationlist.gw");
		
		return mav; 
	
	}//end of public ModelAndView delEnd(ModelAndView mav, HttpServletRequest request) {

	
	
	@RequestMapping(value="/commuteadd.gw")
	public ModelAndView commuteadd(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		
		mav.setViewName("commuteadd.tiles_SKS");
		//  /WEB-INF/views/tiles1/board/add.jsp 파일을 생성한다.
		
		return mav;
	}
	
	@RequestMapping(value="/commutegtw.gw")
	public ModelAndView commutegtw(HttpServletRequest request, ModelAndView mav, CommuteVO_SKS ctvo) {
		
		
	//	commute_gtw = request.getParameter("commute_gtw");
	//	System.out.println("~~~~ 확인용 commute_gtw : " + commute_gtw);
		
		String name = request.getParameter("name");
		String commute_day = request.getParameter("commute_day");
			
	
		int n = service.commutegtw(ctvo);
	
		//	mav.setViewName("test4.tiles1");
	//	mav.addObject("commute_gtw",commute_gtw);
		mav.addObject("name",name);
		mav.setViewName("redirect:/commutelist.gw");
			
			
		return mav;
		
	}
	
	@RequestMapping(value="/commuteow.gw")
	public ModelAndView commuteow(HttpServletRequest request, ModelAndView mav, CommuteVO_SKS ctvo) {
		
		int n = service.commuteow(ctvo);
		
		//	mav.setViewName("test4.tiles1");
		mav.setViewName("redirect:/commutelist.gw");
			
			
		return mav;
		
	}
	
	@RequestMapping(value="/commutelist.gw" ,method= {RequestMethod.GET})
	public ModelAndView commutelist(HttpServletRequest request,HttpServletResponse response, ModelAndView mav) {
		
		List<CommuteVO_SKS> Commutelist = null;
		List<CommuteVO_SKS> Commutelists = null;
		List<CommuteVO_SKS> Commutelistes = null;
		Map<String,String> paraMap = new HashMap<>();
		
		
		String searchType = request.getParameter("searchType");		
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		if(searchType == null || (!"vacation".equals(searchType) && !"name".equals(searchType)) ) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
			searchWord = "";
		}
		
		
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다. 
		int totalCount = 0;        // 총 게시물 건수 
		int sizePerPage = 5;      // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;         // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바) 
		
		int startRno = 0;          // 시작 행번호 
		int endRno = 0;            // 끝 행번호 
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getTotalCountS(paraMap);
	//	System.out.println("~~~~ 확인용 totalCount : " + totalCount);
		
		// 만약에 총 게시물 건수(totalCount)가 127개 이라면 
		// 총 페이지수(totalPage)는 13개가 되어야 한다.
		
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage); // (double)127/10 ==> 12.7 ==> Math.ceil(12.7) ==> 13.0 ==> (int)13.0 ==> 13 
		
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
			} catch(NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
		endRno = startRno + sizePerPage - 1;
		
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		
		
		// 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것)
		
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		
		
		int blockSize = 2;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수 이다.
		
		int loop = 1;
		
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "commutelist.gw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage) ) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
			
		}// end of while------------------------
		
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		String gobackURL = MyUtil_KGH.getCurrentURL(request);

		Commutelist = service.CommuteListSearchWithPaging(paraMap);
		Commutelistes = service.CommutelistLisstNoSearch(paraMap);
		Commutelists = service.CommutelistListNoSearch(paraMap);
		
		mav.addObject("gobackURL", gobackURL);
		mav.addObject("Commutelistes",Commutelistes);
		mav.addObject("Commutelists",Commutelists);
		mav.addObject("Commutelist", Commutelist);
		mav.addObject("totalCount",totalCount);
	    mav.setViewName("commutelist.tiles_SKS");
		
		return mav;
		
	}
	
	@RequestMapping(value="/workadd.gw")
	public ModelAndView workadd(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {

		
		mav.setViewName("workadd.tiles_SKS");
		//  /WEB-INF/views/tiles1/board/add.jsp 파일을 생성한다.
		
		return mav;
	}
	
	@RequestMapping(value="/WorkEnd.gw")
	public ModelAndView workEnd(HttpServletRequest request, ModelAndView mav, WorkemployeeVO_SKS wpvo) {
		
		int n = service.workEnd(wpvo);
		
		//	mav.setViewName("test4.tiles1");
		mav.setViewName("redirect:/worklist.gw");
			
			
		return mav;
		
	}
	
	@RequestMapping(value="/worklist.gw")
	public ModelAndView worklist(HttpServletRequest request, ModelAndView mav, WorkemployeeVO_SKS wpvo) {
		List<WorkemployeeVO_SKS> Employeeworklist = null;
		List<WorkemployeeVO_SKS> Employeeworklists = null;
		List<WorkemployeeVO_SKS> Employeeworklistes = null;
		
		String searchType = request.getParameter("searchType");		
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		if(searchType == null || (!"vacation".equals(searchType) && !"name".equals(searchType)) ) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty() ) {
			searchWord = "";
		}
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
		// 총 게시물 건수(totalCount)는 검색조건이 있을 때와 없을 때로 나뉘어진다. 
		int totalCount = 0;        // 총 게시물 건수 
		int sizePerPage = 5;      // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;         // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바) 
		
		int startRno = 0;          // 시작 행번호 
		int endRno = 0;            // 끝 행번호 
		
		// 총 게시물 건수(totalCount)
		totalCount = service.getworkTotalCount(paraMap);
	//	System.out.println("~~~~ 확인용 totalCount : " + totalCount);
		
		// 만약에 총 게시물 건수(totalCount)가 127개 이라면 
		// 총 페이지수(totalPage)는 13개가 되어야 한다.
		
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage); // (double)127/10 ==> 12.7 ==> Math.ceil(12.7) ==> 13.0 ==> (int)13.0 ==> 13 
		
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
			} catch(NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ( (currentShowPageNo - 1) * sizePerPage ) + 1;
		endRno = startRno + sizePerPage - 1;
		
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		
		Employeeworklist = service.EmployeeworklistSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한것)
		
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		
		// === #121. 페이지바 만들기 === //
		int blockSize = 2;
		// blockSize 는 1개 블럭(토막)당 보여지는 페이지번호의 개수 이다.
		
		int loop = 1;
		
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "commutelist.gw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage) ) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px gray; color:red; padding:2px 4px;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
			
		}// end of while------------------------
		
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		
		
		String gobackURL = MyUtil_KGH.getCurrentURL(request);

		mav.addObject("gobackURL", gobackURL);
		
		
		Employeeworklists = service.worklistSearchs(paraMap);
		Employeeworklistes = service.worklistnoSearchs(paraMap);
		
		mav.addObject("Employeeworklistes",Employeeworklistes);
		mav.addObject("Employeeworklists",Employeeworklists);
		mav.addObject("Employeeworklist",Employeeworklist);
		mav.addObject("totalCount",totalCount);
		mav.setViewName("worklist.tiles_SKS");
		
		return mav;
	}
	
	@RequestMapping(value="/workview.gw")
	public ModelAndView workview(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		WorkemployeeVO_SKS wpvo= null;
		wpvo = service.getworkviewNocount(paraMap);
		
		mav.addObject("wpvo",wpvo);
		
		mav.setViewName("workview.tiles_SKS");
		
		return mav;
	}
	@RequestMapping(value="/workedit.gw")
	public ModelAndView workedit(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		WorkemployeeVO_SKS wpvo = service.getworkviewNocount(paraMap);
		
		mav.addObject("wpvo", wpvo);
		mav.setViewName("workedit.tiles_SKS");
		
		return mav;
	}
	@RequestMapping(value="/workeditEnd.gw", method= {RequestMethod.POST})
	public ModelAndView workeditEnd(ModelAndView mav, WorkemployeeVO_SKS wpvo, HttpServletRequest request) {
		
		int n = service.workeditEnd(wpvo);
		
		mav.setViewName("redirect:/worklist.gw");
		return mav;
		
	}
	@RequestMapping(value="/commuteview.gw")
	public ModelAndView commuteview(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		CommuteVO_SKS ctvo= null;
		ctvo = service.getCommuteViewNocount(paraMap);
		
		mav.addObject("ctvo",ctvo);
		
		mav.setViewName("commuteview.tiles_SKS");
		
		return mav;
	}
	
	@RequestMapping(value="/commuteedit.gw")
	public ModelAndView commuteedit(ModelAndView mav, HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		
		CommuteVO_SKS ctvo = service.getCommuteViewNocount(paraMap);
		
		mav.addObject("ctvo", ctvo);
		mav.setViewName("commuteedit.tiles_SKS");
		
		return mav;
	}
	
	@RequestMapping(value="/commuteeditEnd.gw", method= {RequestMethod.POST})
	public ModelAndView commuteeditEnd(ModelAndView mav, CommuteVO_SKS ctvo, HttpServletRequest request) {
		
		int n = service.commuteedit(ctvo);
		
		mav.setViewName("redirect:/commutelist.gw");
		return mav;
		
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	//  === 로그인 또는 로그아웃을 했을 때 현재 보이던 그 페이지로 그대로 돌아가기 위한 메소드 생성 === 	
	public void getCurrentURL(HttpServletRequest request) {
	HttpSession session = request.getSession();
	session.setAttribute("goBackURL", MyUtil_KGH.getCurrentURL(request));
	}
	
}

