<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<script type="text/javascript">
	$(document).ready(function(){
		
		var now = new Date();
		   
		var month = now.getMonth() + 1;
		if(month < 10) {
		   month = "0"+month;
		}
		
		var date = now.getDate();
		if(date < 10) {
		   date = "0"+date;
		}
		
		var strNow = now.getFullYear() +""+ month +  date;
		
		var hour = "";
		
	//	console.log(now.getHours());
	//	console.log(now.getMinutes());
		
		if (23 <= now.getHours() || now.getHours() < 2) { // 밤 23 시부터 새벽 2시인 경우
			if(23 == now.getHours() && now.getMinutes() < 30) {
				hour = "20";
			}
			else{
				hour = "23";
			}
		} 
		else{ // 밤 23 시부터 새벽 2시를 제외한 경우
			
			if ( now.getHours()%3 == 2){
				// 3으로 나눠서 나머지가 2인경우  02시 05시 08시 11시 14시 17시 20시(기상정보업데이트시간) 
				
				if( (parseInt(now.getHours()/3) * 3 + 2) == now.getHours() && now.getMinutes() < 30) { 
					// 업데이트 시간 기준 30분 이전이라면
					hour = parseInt(now.getHours()/3) * 3 - 1;
				}
				else {
					hour = parseInt(now.getHours()/3) * 3 + 2;
				}
			}
			else { 
				// 3으로 나눠서 나머지가 0,1 인경우
				hour = parseInt(now.getHours()/3) * 3 - 1;
			}
		}
		
		if(hour < 10) {
			hour = "0"+hour;
		}
		
		hour += "00"; 
	//	console.log(hour);
		
		$.ajax({
		   	url:"<%= request.getContextPath()%>/getWeather.gw",
		   	type:"GET",
		   	timeout:30000,
		   	data:{"strNow":strNow,
		   		  "hour":hour,	
		   	},
		   	dataType:"JSON",
		   	success:function(data, status, xhr){
				var dataHeader = data.result.response.header.resultCode;
				var data1 = data.result.response.body.items.item;
				var sky;
				var pty;
				var pop;
				var tmp;
				if(dataHeader == "00") {
					
					for(var i=0; i<data1.length; i++){
						var item = data1[i];
						switch (item.category) {
						case "TMP": // 기온
							tmp = Number(item.fcstValue);
							break;
						case "POP": // 강수확률
							pop = Number(item.fcstValue);
							break;
						case "SKY": // 하늘상태
							sky = Number(item.fcstValue);
							break;
						case "PTY": // 강수형태
							pty = Number(item.fcstValue);
							break;
						default:
							break;
						}
					}// end of for----------------------------
					var html = "";
					var	html2 = "<span style='font-size:20pt; margin-left:20px;'>";
					
					if(pty == 0) { // 비가 안올때
						html = "<img src='<%= request.getContextPath()%>/resources/images/weather/"+sky+".png' style='width:60px;'/>"
						
						if(sky == 1) {
							html2 += "맑음";
						}
						else if(sky == 3) {
							html2 += "구름많음";
						}
						else if(sky == 4) {
							html2 += "흐림";
						}
						
					}
					else { // 비가 올때
						if(pty == 7 || pty == 3){
							html = "<img src='<%= request.getContextPath()%>/resources/images/weather/p3.png' style='width:60px;'/>"
							html2 += "눈";
						}
						else {
							html = "<img src='<%= ctxPath%>/resources/images/weather/p1.png' style='width:60px;' />"
							html2 += "비";
						}
					}
					
					html2 += "&nbsp;&nbsp;"+tmp +"℃";
					html2 += "</span>";
					html += html2 + "<br><span style='margin-left:8px;'><i class='fas fa-tint' style='color:lightblue'></i> "+pop+"%<span>";
					$("#weathericon").html(html);
					
				}
		   		
		   	},
		   	error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
         	}
	   	});
		
	});
</script>
	<div class="container-fluid container-xl">
		<div class="px-3 py-3 mb-4 border rounded-lg " style="font-size:x-large; background-color: white;">
			<c:if test="${sessionScope.loginuser == null}">
				<a href="<%= ctxPath%>/login.gw"><b>로그인이 필요합니다!</b></a>
			</c:if>
			<c:if test="${sessionScope.loginuser != null }">
				<c:if test="${sessionScope.loginuser.profilename == null }">
					<span><img src="<%= ctxPath%>/resources/images/기본프로필_kh.jpg" style="border-radius: 50%; width:50px;"></span>
				</c:if>
				<c:if test="${sessionScope.loginuser.profilename != null }">
					<span><img src="<%= ctxPath%>/resources/empIMG/${sessionScope.loginuser.profilename}" style="border-radius: 50%; width:50px; height: 50px;"></span>
				</c:if>
				<b>${sessionScope.loginuser.name}님 안녕하세요!</b>
			</c:if>
		</div>
		<div class="row mt-4">
			<div class="col-md-8">
				<div class="card-columns border rounded-lg px-3 pt-4 pb-3" style="background-color: white;">
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white" href="<%= ctxPath %>/timemanager.gw"><i style="" class="fas fa-stopwatch fa-2x"></i><br>근태관리</a>
				    	</div>
				  	</div>
				  	
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/organization-chart.gw"><i style="" class="fas fa-user-friends fa-2x"></i><br>조직도</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/chat.gw"><i style="" class="far fa-envelope fa-2x"></i><br>웹채팅</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/approval.gw"><i style="" class="far fa-file-alt fa-2x"></i><br>전자결재</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/schedule.gw"><i style="" class="far fa-calendar-alt fa-2x"></i><br>일정</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%=ctxPath%>/survey.gw"><i style="" class="fas fa-book-reader fa-2x"></i><br>설문조사</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/recentList.gw"><i style="" class="fas fa-chalkboard fa-2x"></i><br>게시판</a>
				    	</div>
				  	</div>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/mypage.gw"><i class="fas fa-user fa-2x"></i></i><br>마이페이지</a>
				    	</div>
				  	</div>
				  	<c:if test="${sessionScope.loginuser != null and sessionScope.loginuser.admin == '1' }"> <%-- admin 으로 로그인 했으면 --%>
				  	<div class="card bg-primary py-2 shadow">
				    	<div class="card-body text-center">
				      		<a class="stretched-link" style="color:white;" href="<%= ctxPath %>/admin/empList.gw"><i class="far fa-address-card fa-2x"></i><br>직원관리</a>
				    	</div>
				  	</div>
				  	</c:if>
				</div>
				
			</div>
			<div class="col-md-4 " id="rightside">
				<div class="border rounded-lg p-3 mb-3" style="background-color: white;">
					<div><b>오늘 근무</b></div>
					<%-- <div class="btn btn-lg btn-light px-md-3 my-2 w-100">예정 스케줄 <span class="badge badge-secondary">1</span></div> --%>
					<button class="btn btn-success btn-lg px-md-3 my-2 w-100" id="">출근하기</button>
					<button class="btn btn-primary btn-lg px-md-3 w-100" id="">퇴근하기</button>
				</div>
				<div class="border rounded-lg p-3 my-3" style="background-color: white;">
					<div><b>날씨</b></div>
					<div id="weathericon">
					</div>
				</div>
			</div>
		</div>
	</div>