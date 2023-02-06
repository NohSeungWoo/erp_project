package com.spring.finalProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.board.model.BoardCategoryVO_OHJ;
import com.spring.finalProject.model.ScheduleVO_NSW;
import com.spring.finalProject.service.InterKGHService;
import com.spring.finalProject.service.InterNSWService;

//==== #30. 컨트롤러 선언 ====
@Component
@Controller
public class NSWController {
	
	@Autowired
	private InterNSWService service;
	
	// 일정 클릭 시 보여주기
	@ResponseBody
	@RequestMapping(value="/schedule.gw")
	public ModelAndView schedule(ModelAndView mav /*, HttpServletRequest request*/) throws Exception {
		//HttpSession session = request.getSession();
		//String loginuser = (String)session.getAttribute("loginuser");
		//System.out.println("#####");
		//System.out.println(loginuser);
		
		
		mav.setViewName("schedule/schedule.tiles_NSW");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/scheduleView.gw", produces="text/plain;charset=UTF-8")
	public String scheduleView(ScheduleVO_NSW VO) throws Exception {
		VO.setFk_employeeID("211019001");
		List<ScheduleVO_NSW> schedule = service.schedule(VO);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(schedule != null) {
			for(ScheduleVO_NSW List : schedule) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("seq", List.getSeq());
				jsonObj.put("title", List.getSubject());
				jsonObj.put("start", List.getStartDate());
				jsonObj.put("end", List.getEndDate());
				jsonObj.put("memo", List.getMemo());
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getSchedule.gw", produces="text/plain;charset=UTF-8")
	public String getSchedule(ScheduleVO_NSW VO, HttpServletRequest request) throws Exception {
		VO.setSeq(request.getParameter("seq"));
		List<ScheduleVO_NSW> schedule = service.getSchedule(VO);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(schedule != null) {
			for(ScheduleVO_NSW List : schedule) {
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("title", List.getSubject());
				jsonObj.put("start", List.getStartDate());
				jsonObj.put("end", List.getEndDate());
				jsonObj.put("memo", List.getMemo());
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	// 일정 등록 클릭시 팝업창 띄우기
	@RequestMapping(value="/schedulePopup.gw")
	public ModelAndView schedulePopup(ModelAndView mav) {

		mav.setViewName("schedule/schedulePopup.tiles_NSW");
		return mav;
	}
	
	
	//일정 추가 버튼 클릭 Ajax
	@ResponseBody
	@RequestMapping(value = "/addSchedule.gw", method = RequestMethod.POST)
	public Map<Object,Object> addSchedule(@RequestBody ScheduleVO_NSW VO /*, HttpServletRequest request*/) throws Exception{
		//HttpSession session = request.getSession();
		Map<Object,Object>map = new HashMap<Object,Object>();
		//String loginuser = (String)session.getAttribute("loginuser");
		//System.out.println("#####");
		//System.out.println(loginuser);
		VO.setFk_employeeID("211019001");
		VO.setFk_departNo("104");
		service.addSchedule(VO);
	
		return map;
	}
	
	
	// 일정 글수정 페이지 요청 === //
	@RequestMapping(value="/scheduleEdit.gw")
	public ModelAndView scheduleEdit(ModelAndView mav) {
			
		mav.setViewName("schedule/scheduleEdit.tiles_NSW");
		return mav;
	}
	
}