<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- 막대그래프  -->
<script src="https://code.highcharts.com/highcharts.js"></script>

<script src="https://code.highcharts.com/modules/data.js"></script>
<script src="https://code.highcharts.com/modules/drilldown.js"></script>
<style type="text/css">
	
	.highcharts-figure,
	.highcharts-data-table table {
	    min-width: 120px;
	    max-width: 500px;
	    margin: 1em auto;
	}
	
	.highcharts-data-table table {
	    font-family: Verdana, sans-serif;
	    border-collapse: collapse;
	    border: 1px solid #ebebeb;
	    margin: 10px auto;
	    text-align: center;
	    width: 30%;
	    min-width: 120px;
	    max-width: 500px;
	}
	
	.highcharts-data-table caption {
	    padding: 1em 0;
	    font-size: 1.2em;
	    color: #555;
	}
	
	.highcharts-data-table th {
	    font-weight: 200;
	    padding: 0.5em;
	}
	
	.highcharts-data-table td,
	.highcharts-data-table th,
	.highcharts-data-table caption {
	    padding: 0.5em;
	}
	
	.highcharts-data-table thead tr,
	.highcharts-data-table tr:nth-child(even) {
	    background: #f8f8f8;
	}
	
	.highcharts-data-table tr:hover {
	    background: #f1f7ff;
	}
	
	input[type="number"] {
	    min-width: 50px;
	}
	
	div#table_container table {width: 100%}
	div#table_container th, div#table_container td {border: solid 1px gray; text-align: center;} 
	div#table_container th {background-color: #595959; color: white;}

</style>

<script type="text/javascript">
$(document).ready(function(){
	
	$("select#searchType").bind("change", function(){
		func_choice($(this).val());
		// $(this).val() 은 "" 또는 "deptname" 또는 "gender" 또는 "deptnameGender" 이다. 
	});
	
	
	
});// end of $(document).ready(function(){})---------------------

///////////////////////////////////////////////////////////////////////////////////////////

</script>

<div class="container-fluid container-xl">
		
		<div class="px-3 py-3 mb-4  " style="font-size:x-large; background-color: white;">${sessionScope.loginuser.name}님</div>
			
		<hr style="height:1">
		
		<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()  }">
			<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
			<b>휴가 현황</b>
			<hr style="height:1">
			
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차정보</th>
					<th style="width: 200px;  text-align: center;">종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlistess}">
					<tr>
						<td align="center">${VacationVO.seq}</td>
						<td align="center">
							<span class="name" onclick="govacationView('${VacationVO.seq}')">${VacationVO.name}</span>
						</td>
						<td align="center">
							<span class="annual" onclick="govacationView('${VacationVO.seq}')">${VacationVO.annual}</span>
						</td>
						<td align="center">
							<span class="vacation" onclick="govacationView('${VacationVO.seq}')">${VacationVO.vacation}</span>
						</td>
						<td align="center">
							<span class="start_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.start_date}</span>
						</td>
						<td align="center">
							<span class="end_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.end_date}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		</c:if>
		<c:if test="${sessionScope.loginuser.name eq '이순신'.toString()  }">
			<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
			<b>휴가 현황</b>
			<hr style="height:1">
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차정보</th>
					<th style="width: 200px;  text-align: center;">종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlistse}">
					<tr>
						<td align="center">${VacationVO.seq}</td>
						<td align="center">
							<span class="name" onclick="govacationView('${VacationVO.seq}')">${VacationVO.name}</span>
						</td>
						<td align="center">
							<span class="annual" onclick="govacationView('${VacationVO.seq}')">${VacationVO.annual}</span>
						</td>
						<td align="center">
							<span class="vacation" onclick="govacationView('${VacationVO.seq}')">${VacationVO.vacation}</span>
						</td>
						<td align="center">
							<span class="start_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.start_date}</span>
						</td>
						<td align="center">
							<span class="end_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.end_date}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		</c:if>
		
		<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
		<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">	
		<b>휴가 현황</b>
		<hr style="height:1">	
		<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차정보</th>
					<th style="width: 200px;  text-align: center;">종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlistses}">
					<tr>
						<td align="center">${VacationVO.seq}</td>
						<td align="center">
							<span class="name" onclick="govacationView('${VacationVO.seq}')">${VacationVO.name}</span>
						</td>
						<td align="center">
							<span class="annual" onclick="govacationView('${VacationVO.seq}')">${VacationVO.annual}</span>
						</td>
						<td align="center">
							<span class="vacation" onclick="govacationView('${VacationVO.seq}')">${VacationVO.vacation}</span>
						</td>
						<td align="center">
							<span class="start_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.start_date}</span>
						</td>
						<td align="center">
							<span class="end_date" onclick="govacationView('${VacationVO.seq}')">${VacationVO.end_date}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		</c:if>
		<c:if test="${ empty sessionScope.loginuser }">
		<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
			<b>휴가 현황</b>
			<hr style="height:1">	
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차정보</th>
					<th style="width: 200px;  text-align: center;">종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
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
		</div>	
		</c:if>			
				
				<c:if test="${sessionScope.loginuser.name eq '이순신'.toString()  }">
				<div>
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b>근무 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 200px;  text-align: center;">성명</th>
								<th style="width: 200px;  text-align: center;">날짜</th>
								<th style="width: 250px; text-align: center;">기본 근무 시간</th>
								<th style="width: 250px; text-align: center;">하루 근무 </th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklistt}">
							<tr>
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
				</div>
				</div>
				</c:if>
				
				<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()  }">
				<div>
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b>근무 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 200px;  text-align: center;">성명</th>
								<th style="width: 200px;  text-align: center;">날짜</th>
								<th style="width: 250px; text-align: center;">기본 근무 시간</th>
								<th style="width: 250px; text-align: center;">하루 근무 </th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklistts}">
							<tr>
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
				</div>
				</div>
				</c:if>
				
				<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
				<div>
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b>근무 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 200px;  text-align: center;">성명</th>
								<th style="width: 200px;  text-align: center;">날짜</th>
								<th style="width: 250px; text-align: center;">기본 근무 시간</th>
								<th style="width: 250px; text-align: center;">하루 근무 </th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklisttss}">
							<tr>
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
				</div>
				</div>
				</c:if>
				
				<c:if test="${ empty sessionScope.loginuser }">
				<div>
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b>근무 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 200px;  text-align: center;">성명</th>
								<th style="width: 200px;  text-align: center;">날짜</th>
								<th style="width: 250px; text-align: center;">기본 근무 시간</th>
								<th style="width: 250px; text-align: center;">하루 근무 </th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="WorkemployeeVO" items="${requestScope.Employeeworklisttss}">
							<tr>
								<td align="center">
									<span class=work_name onclick=""></span>
								</td>
								<td align="center">
									<span class="work_day" onclick=""></span>
								</td>
								<td align="center">
									<span class="work_time" onclick=""></span>
								</td>
								<td align="center">
									
									<span class="todaycount" onclick=""></span>
								</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				</div>
				</c:if>
				
				<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()  }">
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b> 출퇴근 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 150px;  text-align: center;">성명</th>
								<th style="width: 150px;  text-align: center;">날짜</th>
								<th style="width: 150px;  text-align: center;">근무 시간</th>
								<th style="width: 200px; text-align: center;">출/퇴근 여부</th>
								<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
								<th style="width: 200px;  text-align: center;">확인자</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="CommuteVO" items="${requestScope.Commutelistt}">
								<tr>
									<td align="center">
										<span class="commute_day" >${CommuteVO.name}</span>
									</td>	
									<td align="center">
										<span class="commute_day">${CommuteVO.commute_day}</span>
									</td>
									<td align="center">
										<span class="comute_time">${CommuteVO.comute_time}</span>
									</td>
									<td align="center">
										<span class="commute_status">${CommuteVO.commute_status}</span>
									</td>
									<td align="center">
										
										<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
									</td>
									<td align="center">
										<span class="commute_ok">${CommuteVO.commute_ok}</span>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
					</c:if>
					<c:if test="${sessionScope.loginuser.name eq '이순신'.toString()  }">
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b> 출퇴근 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 150px;  text-align: center;">성명</th>
								<th style="width: 150px;  text-align: center;">날짜</th>
								<th style="width: 150px;  text-align: center;">근무 시간</th>
								<th style="width: 200px; text-align: center;">출/퇴근 여부</th>
								<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
								<th style="width: 200px;  text-align: center;">확인자</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="CommuteVO" items="${requestScope.Commutelistts}">
								<tr>
									<td align="center">
										<span class="commute_day" >${CommuteVO.name}</span>
									</td>	
									<td align="center">
										<span class="commute_day">${CommuteVO.commute_day}</span>
									</td>
									<td align="center">
										<span class="comute_time">${CommuteVO.comute_time}</span>
									</td>
									<td align="center">
										<span class="commute_status">${CommuteVO.commute_status}</span>
									</td>
									<td align="center">
										
										<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
									</td>
									<td align="center">
										<span class="commute_ok">${CommuteVO.commute_ok}</span>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
					</c:if>
					<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b> 출퇴근 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 150px;  text-align: center;">성명</th>
								<th style="width: 150px;  text-align: center;">날짜</th>
								<th style="width: 150px;  text-align: center;">근무 시간</th>
								<th style="width: 200px; text-align: center;">출/퇴근 여부</th>
								<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
								<th style="width: 200px;  text-align: center;">확인자</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="CommuteVO" items="${requestScope.Commutelistst}">
								<tr>
									<td align="center">
										<span class="commute_day" >${CommuteVO.name}</span>
									</td>	
									<td align="center">
										<span class="commute_day">${CommuteVO.commute_day}</span>
									</td>
									<td align="center">
										<span class="comute_time">${CommuteVO.comute_time}</span>
									</td>
									<td align="center">
										<span class="commute_status">${CommuteVO.commute_status}</span>
									</td>
									<td align="center">
										
										<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
									</td>
									<td align="center">
										<span class="commute_ok">${CommuteVO.commute_ok}</span>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
					</c:if>
					
					<c:if test="${ empty sessionScope.loginuser }">
					<div class="px-3 py-4 my-4 border rounded-lg" style="float: left;">
						<b> 출퇴근 현황</b>
						<hr style="height:1">
						<table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
						<thead>
							<tr style="background-color: #DDD;">
								<th style="width: 150px;  text-align: center;">성명</th>
								<th style="width: 150px;  text-align: center;">날짜</th>
								<th style="width: 150px;  text-align: center;">근무 시간</th>
								<th style="width: 200px; text-align: center;">출/퇴근 여부</th>
								<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
								<th style="width: 200px;  text-align: center;">확인자</th>
							</tr>
						</thead>
						
						<tbody>
							<c:forEach var="CommuteVO" items="${requestScope.Commutelist}">
								<tr>
									<td align="center">
										<span class="commute_day" ></span>
									</td>	
									<td align="center">
										<span class="commute_day"></span>
									</td>
									<td align="center">
										<span class="comute_time"></span>
									</td>
									<td align="center">
										<span class="commute_status"></span>
									</td>
									<td align="center">
										
										<span class="commute_gtw"></span>
									</td>
									<td align="center">
										<span class="commute_ok"></span>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</div>
					</c:if>
					
					
		
		
</div>	
		