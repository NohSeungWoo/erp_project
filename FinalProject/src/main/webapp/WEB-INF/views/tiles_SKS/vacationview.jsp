<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">휴가내용보기</h2>
		
			<table style="width: 1024px;" class="table table-bordered">
				<tr>
					<th style="width: 15%;">글번호</th>
					<td>${vacvo.seq}</td>
				</tr>
				<tr>	
					<th>성명</th>
					<td>${vacvo.name}</td>
				</tr>
				<tr>	
					<th>연차 정보</th>
					<td>${vacvo.annual}</td>
				</tr>
				<tr>	
					<th>종류</th>
					<td>${vacvo.vacation}</td>
				</tr>
				<tr>
					<th>시작일</th>
					<td>${vacvo.start_date}</td>
				</tr>
				<tr>
					<th>끝나는일</th>
					<td>${vacvo.end_date}</td>
				</tr>
			</table>
			<button type="button" class="btn btn-secondary btn-sm mr-3" onclick="javascript:location.href='<%= request.getContextPath()%>/vacationlist.gw'">전체목록보기</button>
			<button type="button" class="btn btn-secondary btn-sm mr-3" onclick="javascript:location.href='<%= request.getContextPath()%>/vacationedit.gw?seq=${vacvo.seq}'">휴가수정하기</button>
			<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()}">
				<button type="button" class="btn btn-secondary btn-sm mr-3" onclick="javascript:location.href='<%= request.getContextPath()%>/vacationdel.gw?seq=${vacvo.seq}'">휴가취소하기</button>
			</c:if>	
		
	</div>
</div>
				