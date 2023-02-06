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
		$("button#btnInsert").click(function(){
			var frm = document.InsertFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/commutegtw.gw";
			frm.submit();
		});
		$("button#btnInsert2").click(function(){
			var frm = document.InsertFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/commuteow.gw";
			frm.submit();
		});
		
		  $("button#goSearch").click(function() {
	    		var frm = document.searchFrm;
	    		frm.method = "GET";
	    		frm.action = "<%= request.getContextPath()%>/commutelist.gw";
	    		frm.submit();
	        });	
        
	});
		
	 function goCommuteView(seq) {
		 location.href="<%= ctxPath%>/commuteview.gw?seq="+seq; 
		 
  	};
	
</script>	
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
	<c:if test="${ empty sessionScope.loginuser }">
		
	</c:if>
	
	<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()  }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">출퇴근 내역 </h3>
	 <hr width = "97%" color = "gray" >
	 	<form name="InsertFrm">
    	 	<input type="hidden" name="name" id="name" value="${sessionScope.loginuser.name }" >
			<button style="float: right; margin : 20px;" id="btnInsert2">퇴&nbsp;근</button>
			<button style="float: right; margin : 20px;" id="btnInsert"> 출&nbsp;근</button>
		</form>	
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px;  text-align: center;">날짜</th>					
					<th style="width: 150px; text-align: center;">출/퇴근 여부</th>
					<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
					<th style="width: 200px;  text-align: center;">확인자</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="CommuteVO" items="${requestScope.Commutelists}">
					<tr>
						<td align="center">
							<span class="commute_day">${CommuteVO.name}</span>
						</td>
						<td align="center">
							<span class="commute_day">${CommuteVO.commute_day}</span>
						</td>
						<td align="center">
							<span class="commute_status">${CommuteVO.commute_status}</span>
						</td>
						<td align="center">
							
							<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
						</td>
						<td align="center">
							<span class="commute_ok" >${CommuteVO.commute_ok}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%-- === #122. 페이지바 보여주기  === --%>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		<c:if test="${sessionScope.loginuser.name eq '이순신'.toString()}">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">출퇴근 내역 </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">   
		<form name="InsertFrm">
    	 	<input type="hidden" name="name" id="name" value="${sessionScope.loginuser.name }" >
			<button style="float: right; margin : 20px;" id="btnInsert2">퇴&nbsp;근</button>
			<button style="float: right; margin : 20px;" id="btnInsert"> 출&nbsp;근</button>
		</form>	
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px;  text-align: center;">날짜</th>
					<th style="width: 150px; text-align: center;">출/퇴근 여부</th>
					<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
					<th style="width: 200px;  text-align: center;">확인자</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="CommuteVO" items="${requestScope.Commutelist}">
					<tr>
						<td align="center">${CommuteVO.seq}</td>
						<td align="center">
							<span class="commute_day">${CommuteVO.name}</span>
						</td>
						<td align="center">
							<span class="commute_day">${CommuteVO.commute_day}</span>
						</td>
					
						<td align="center">
							<span class="commute_status">${CommuteVO.commute_status}</span>
						</td>
						<td align="center">
							
							<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
						</td>
						<td align="center">
							<span class="commute_ok" onclick="goCommuteView('${CommuteVO.seq}')">${CommuteVO.commute_ok}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%-- === #122. 페이지바 보여주기  === --%>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">출퇴근 내역(관리자) </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">   
		<div style="float: left;" >총 내역 수  : ${totalCount}건</div>
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px;  text-align: center;">날짜</th>
					<th style="width: 150px; text-align: center;">출/퇴근 여부</th>
					<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
					<th style="width: 200px;  text-align: center;">확인자</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="CommuteVO" items="${requestScope.Commutelistes}">
					<tr>
						<td align="center">${CommuteVO.seq}</td>
						<td align="center">
							<span class="commute_day">${CommuteVO.name}</span>
						</td>
						<td align="center">
							<span class="commute_day">${CommuteVO.commute_day}</span>
						</td>
					
						<td align="center">
							<span class="commute_status">${CommuteVO.commute_status}</span>
						</td>
						<td align="center">
							
							<span class="commute_gtw">${CommuteVO.commute_gtw}</span>
						</td>
						<td align="center">
							<span class="commute_ok" onclick="goCommuteView('${CommuteVO.seq}')">${CommuteVO.commute_ok}</span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%-- === #122. 페이지바 보여주기  === --%>
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		
	<c:if test="${ empty sessionScope.loginuser }">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">출퇴근 내역 </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">   
	
	 <table style=" margin-left: 20px; width: 97%;" class="table table-bordered">
	 		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px;  text-align: center;">날짜</th>
					<th style="width: 150px; text-align: center;">출/퇴근 여부</th>
					<th style="width: 200px;  text-align: center;">출/퇴근 시간</th>
					<th style="width: 200px;  text-align: center;">관리자확인</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="CommuteVO" items="${requestScope.Commutelist}">
					<tr>
						<td align="center"></td>
						<td align="center">
							<span class="commute_day"></span>
						</td>
						<td align="center">
							<span class="commute_day"></span>
						</td>
					
						<td align="center">
							<span class="commute_status"></span>
						</td>
						<td align="center">
							
							<span class="commute_gtw"></span>
						</td>
						<td align="center">
							<span class="commute_ok" onclick="goCommuteView('${CommuteVO.seq}')"></span>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		
</div>
</div>		