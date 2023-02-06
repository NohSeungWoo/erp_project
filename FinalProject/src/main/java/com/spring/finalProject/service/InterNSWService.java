package com.spring.finalProject.service;

import java.util.List;

import com.spring.finalProject.model.ScheduleVO_NSW;

public interface InterNSWService {

	void addSchedule(ScheduleVO_NSW VO) throws Exception;

	List<ScheduleVO_NSW> schedule(ScheduleVO_NSW VO) throws Exception;

	List<ScheduleVO_NSW> getSchedule(ScheduleVO_NSW vO) throws Exception;

	

}
