<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String ctxPath = request.getContextPath(); 
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
 
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		
		  
		
        
	});
	 function  goWorkView(seq) {
		 location.href="<%= ctxPath%>/workview.gw?seq="+seq; 
		 
  	};
</script>	
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
	<c:if test="${ empty sessionScope.loginuser }">
		
	</c:if>
	
	<c:if test="${sessionScope.loginuser.name eq '이순신'.toString() }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">근무 내역 </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">
 	 
	
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 200px;  text-align: center;">글번호</th>
					<th style="width: 200px;  text-align: center;">성명</th>
					<th style="width: 200px;  text-align: center;">날짜</th>
					<th style="width: 250px; text-align: center;">기본 근무 시간</th>
					<th style="width: 250px; text-align: center;">하루 근무 </th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklists}">
					<tr>
						<td align="center">
							<span class=seq onclick="">${WorkemployeeVO.seq}</span>
						</td>
						<td align="center">
							<span class=work_name onclick="">${WorkemployeeVO.name}</span>
						</td>
						<td align="center">
							<span class="work_day" onclick="">${WorkemployeeVO.work_day}</span>
						</td>
						<td align="center">
							<span class="work_time" onclick="">${WorkemployeeVO.work_time}</span>
						</td>
						<td align="center">
							
							<span class="todaycount" onclick="">${WorkemployeeVO.todaycount}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		
	<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString() }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">근무 내역 </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">
 	 
	
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 200px;  text-align: center;">글번호</th>
					<th style="width: 200px;  text-align: center;">성명</th>
					<th style="width: 200px;  text-align: center;">날짜</th>
					<th style="width: 250px; text-align: center;">기본 근무 시간</th>
					<th style="width: 250px; text-align: center;">하루 근무 </th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklistes}">
					<tr>
						<td align="center">
							<span class=seq onclick="">${WorkemployeeVO.seq}</span>
						</td>
						<td align="center">
							<span class=work_name onclick="">${WorkemployeeVO.name}</span>
						</td>
						<td align="center">
							<span class="work_day" onclick="">${WorkemployeeVO.work_day}</span>
						</td>
						<td align="center">
							<span class="work_time" onclick="">${WorkemployeeVO.work_time}</span>
						</td>
						<td align="center">
							
							<span class="todaycount" onclick="">${WorkemployeeVO.todaycount}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		
		</c:if>
			
	<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">근무 내역(관리자) </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">
 	 <div style="float: left;" >총 내역 수  : ${totalCount}건</div>

	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 200px;  text-align: center;">글번호</th>
					<th style="width: 200px;  text-align: center;">성명</th>
					<th style="width: 200px;  text-align: center;">날짜</th>
					<th style="width: 250px; text-align: center;">기본 근무 시간</th>
					<th style="width: 250px; text-align: center;">하루 근무 </th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklist}">
					<tr>
						<td align="center">
							<span class=seq onclick="">${WorkemployeeVO.seq}</span>
						</td>
						<td align="center">
							<span class=work_name onclick="">${WorkemployeeVO.name}</span>
						</td>
						<td align="center">
							<span class="work_day" onclick="">${WorkemployeeVO.work_day}</span>
						</td>
						<td align="center">
							<span class="work_time" onclick="">${WorkemployeeVO.work_time}</span>
						</td>
						<td align="center">
							
							<span class="todaycount" onclick="goWorkView('${WorkemployeeVO.seq}')">${WorkemployeeVO.todaycount}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		
		</c:if>
		<c:if test="${ empty sessionScope.loginuser }">
				<h2 style=" margin-bottom: 30px; margin-left: 20px;"> 근무 조회</h2>
			<hr width = "97%" color = "gray" style="margin-bottom: 30px;">
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 200px;  text-align: center;">글번호</th>
					<th style="width: 200px;  text-align: center;">성명</th>
					<th style="width: 200px;  text-align: center;">날짜</th>
					<th style="width: 250px; text-align: center;">근무 시간</th>
					<th style="width: 250px; text-align: center;">총 근무 시간</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlist}">
					<tr>
						<td align="center"></td>
						<td align="center">
							<span class="name" onclick="govacationView('${VacationVO.seq}')"></span>
						</td>
						<td align="center">
							<span class="annual" onclick="govacationView('${VacationVO.seq}')"></span>
						</td>
						<td align="center">
							<span class="vacation" onclick="govacationView('${VacationVO.seq}')"></span>
						</td>
						<td align="center">
							<span class="start_date" onclick="govacationView('${VacationVO.seq}')"></span>
						</td>
						<td align="center">
							<span class="end_date" onclick="govacationView('${VacationVO.seq}')"></span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		
		
</div>		
</div>		