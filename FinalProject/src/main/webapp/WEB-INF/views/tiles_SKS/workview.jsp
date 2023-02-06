<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">근무 조회</h2>
		
			<table style="width: 1024px;" class="table table-bordered">
				
				<tr>
					<th style="width: 15%;">글번호</th>
					<td>${wpvo.seq}</td>
				</tr>
				<tr>	
					<th>성명</th>
					<td>${wpvo.name}</td>
				</tr>
				<tr>	
					<th>날짜</th>
					<td>${wpvo.work_day}</td>
				</tr>
				<tr>	
					<th>기본 근무 시간</th>
					<td>${wpvo.work_time}</td>
				</tr>
				<tr>	
					<th>하루 근무</th>
					<td>${wpvo.todaycount}</td>
				</tr>
				
			</table>
			<button type="button" class="btn btn-secondary btn-sm mr-3" onclick="javascript:location.href='<%= request.getContextPath()%>/workedit.gw?seq=${wpvo.seq}'">근무수정하기</button>
		
	</div>
</div>
				