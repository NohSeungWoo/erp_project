<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">출퇴근변경</h2>
		
			<table style="width: 1024px;" class="table table-bordered">
				
				<tr>
					<th style="width: 15%;">글번호</th>
					<td>${ctvo.seq}</td>
				</tr>
				<tr>	
					<th>성명</th>
					<td>${ctvo.name}</td>
				</tr>
				<tr>	
					<th>날짜</th>
					<td>${ctvo.commute_day}</td>
				</tr>
				<tr>	
					<th>출/퇴근 여부</th>
					<td>${ctvo.commute_status}</td>
				</tr>
				<tr>
					<th>출/퇴근 시간</th>
					<td>${ctvo.commute_gtw}</td>
				</tr>
				<tr>
					<th>확인자</th>
					<td>${ctvo.commute_ok}</td>
				</tr>
			</table>
			<button type="button" class="btn btn-secondary btn-sm mr-3" onclick="javascript:location.href='<%= request.getContextPath()%>/commuteedit.gw?seq=${ctvo.seq}'">근무수정하기</button>
		
	</div>
</div>
				