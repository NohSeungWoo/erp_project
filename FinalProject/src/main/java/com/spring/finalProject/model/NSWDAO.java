package com.spring.finalProject.model;

import java.util.List;

import javax.inject.Inject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.spring.board.model.BoardCategoryVO_OHJ;


@Repository
public class NSWDAO implements InterNSWDAO {

	@Inject
	private SqlSession sqlSession;

	@Override
	public void addSchedule(ScheduleVO_NSW VO) throws Exception {
		sqlSession.insert("nohsw.addSchedule", VO);
	}
	
	@Override
	public List<ScheduleVO_NSW> schedule(ScheduleVO_NSW VO) {
		List<ScheduleVO_NSW> schedule = sqlSession.selectList("nohsw.schedule", VO);
		return schedule;
	}
	
	@Override
	public List<ScheduleVO_NSW> getSchedule(ScheduleVO_NSW VO) {
		List<ScheduleVO_NSW> schedule = sqlSession.selectList("nohsw.getSchedule", VO);
		return schedule;
	}
}
