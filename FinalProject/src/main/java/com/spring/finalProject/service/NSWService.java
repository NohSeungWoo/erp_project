package com.spring.finalProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.board.model.BoardCategoryVO_OHJ;
import com.spring.finalProject.model.InterNSWDAO;
import com.spring.finalProject.model.ScheduleVO_NSW;

//==== #31. Service 선언 ====
//트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단
@Service // 원래 bean에 올려주기 위해서 @Component를 써야하는데 @Service를 쓰기 때문에 자동적으로 bean에 올라간다.
public class NSWService implements InterNSWService {
	
	// === #34. 의존객체 주입하기(DI: Dependency Injection) ===
	@Autowired
	private InterNSWDAO dao; // 다형성 // 원래는 dao가 아니라 boardDAO라고 써줘야하는데 지금은 BoardDAO가 한개밖에 없으므로 @Autowired에 의해 타입만 맞으면 되니까 dswefwf라고 써도 된다.


	@Override
	public void addSchedule(ScheduleVO_NSW VO) throws Exception {
		dao.addSchedule(VO);
	}


	@Override
	public List<ScheduleVO_NSW> schedule(ScheduleVO_NSW VO) throws Exception {
		List<ScheduleVO_NSW> schedule = dao.schedule(VO);
		return schedule;
	}
		
	@Override
	public List<ScheduleVO_NSW> getSchedule(ScheduleVO_NSW VO) throws Exception {
		List<ScheduleVO_NSW> schedule = dao.getSchedule(VO);
		return schedule;
	}
	
		
	
}
