package com.spring.finalProject.model;

import java.util.List;
import java.util.Map;

import com.spring.board.model.BoardCategoryVO_OHJ;


public interface InterNSWDAO {

	void addSchedule(ScheduleVO_NSW VO) throws Exception;
	
	// 스케줄 목록
	List<ScheduleVO_NSW> schedule(ScheduleVO_NSW VO);
	// 스케줄 상세
	List<ScheduleVO_NSW> getSchedule(ScheduleVO_NSW VO);
}
