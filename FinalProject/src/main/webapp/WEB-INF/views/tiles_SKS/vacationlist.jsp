<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>    

<script type="text/javascript">
$(document).ready(function(){
	
	
	
	}); // end of $(document).ready(function(){})----------------------------	
	function  govacationView(seq) {
		 location.href="<%= ctxPath%>/vacationview.gw?seq="+seq; 
		 
		// frm.method = "GET";
		// frm.action = "<%= ctxPath%>/vacationview.gw";
		// frm.submit();
   }
	
	// Function Declaration
	function goSearch() {
		var frm = document.searchFrm;
		frm.method = "GET";
		frm.action = "<%= request.getContextPath()%>/vacationlist.gw";
		frm.submit();
	}// end of function goSearch()------------------------------
</script>
    
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
	
		<c:if test="${sessionScope.loginuser.name eq '이순신3'.toString()  }">
			<h2 style=" margin-bottom: 30px; margin-left: 20px;">개인 휴가 조회</h2>
			<hr width = "97%" color = "gray" style="margin-bottom: 30px;">
			
			
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차사용날짜</th>
					<th style="width: 200px;  text-align: center;">연차종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlistes}">
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
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
	<c:if test="${sessionScope.loginuser.name eq '이순신'.toString()  }">
			<h2 style=" margin-bottom: 30px; margin-left: 20px;">개인 휴가 조회</h2>
			<hr width = "97%" color = "gray" style="margin-bottom: 30px;">
			
			
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차사용날짜</th>
					<th style="width: 200px;  text-align: center;">연차종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlists}">
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
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		
		<c:if test="${sessionScope.loginuser.name ne '이순신'.toString() && sessionScope.loginuser.name ne '이순신3'.toString() && not empty sessionScope.loginuser }">
			<h2 style=" margin-bottom: 30px; margin-left: 20px;">전체 휴가 조회(관리자)</h2>
			<hr width = "97%" color = "gray" style="margin-bottom: 30px;">
			
			<div style="float: left; margin-left: 20px; margin-bottom: 20px;">총 내역 수  : ${totalCount}건</div>
			<input type="hidden" name="hiddenname" id="hiddenname" value="${sessionScope.loginuser.name }" >
		<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차사용날짜</th>
					<th style="width: 200px;  text-align: center;">연차종류</th>
					<th style="width: 200px; text-align: center;">시작일</th>
					<th style="width: 200px;  text-align: center;">끝나는일</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach var="VacationVO" items="${requestScope.vacationlist}">
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
		<div align="center" style="width: 97%; border: solid 0px gray; margin: 20px auto;">
			${requestScope.pageBar}
		</div>
		</c:if>
		<c:if test="${ empty sessionScope.loginuser }">
				<h2 style=" margin-bottom: 30px; margin-left: 20px;"> 휴가 조회</h2>
			<hr width = "97%" color = "gray" style="margin-bottom: 30px;">
			<table style="width: 97%; margin-left:20px; margin-top: 50px; " class="table table-bordered">
		
			<thead>
				<tr style="background-color: #DDD;">
					<th style="width: 150px;  text-align: center;">글번호</th>
					<th style="width: 150px;  text-align: center;">성명</th>
					<th style="width: 150px; text-align: center;">연차사용날짜</th>
					<th style="width: 200px;  text-align: center;">연차종류</th>
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
		</c:if>
		
	
		
		<%-- ##################################################### --%>
		<%-- 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
		           사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해 
		           현재 페이지 주소를 View단으로 넘겨준다. --%>
		           
		<form name="goViewFrm">
			<input type="hidden"  name="seq" 	  />
			<input type="hidden"  name="gobackURL" />
			<input type="hidden"  name="searchType" />
			<input type="hidden"  name="searchWord" />
		</form>            
		
	</div>
</div>