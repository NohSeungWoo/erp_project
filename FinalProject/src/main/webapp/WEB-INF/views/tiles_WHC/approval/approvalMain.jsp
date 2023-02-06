<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>
 
<style type="text/css">
	.tabmenu:hover {
		cursor: pointer;
		font-weight: bold;
	}
	div.tabcontent {
		display:none;
	}
	.active-tab {
      background-color: lightblue;
      color: white;
      font-weight: bold;
   	}
    .listtr:hover {
   		background-color: #f2f6ff;
   	}
</style>
<script type="text/javascript">
	
	$(document).ready(function(){
		
		$("li.tabmenu").click(function(event){
			var $target = $(event.target);
			var i = $("li.tabmenu").index($target);
			$("div.tabcontent").css("display","none");
			$("div.tabcontent:eq("+i+")").css("display","block");
			
			$("li.tabmenu").removeClass("active-tab");
			$target.addClass("active-tab");
		});
		
		$("li.tabmenu").eq(0).trigger('click');
	});
	
</script>
<div class="container-fluid">
	<h4><b>나의 현황</b></h4>
	
	<div class="card-group">
		<div class="card py-lg-3" style="background-color: #fafafa;">
	  		<div class="card-body text-center">
	    		<h5 class="card-title mb-3">상신한</h5>
			    <a href="<%=ctxPath%>/box/sentDoc.gw?apstatus=0" class="stretched-link" style="font-size:18pt; font-weight: bold;">${requestScope.totalSentCount}</a>
		  	</div>
		</div>
		<div class="card py-lg-3" style="background-color: #fafafa;">
		  	<div class="card-body text-center">
		    	<h5 class="card-title mb-3">반려된</h5>
			    <a href="<%=ctxPath%>/box/sentDoc.gw?apstatus=2" class="stretched-link" style="font-size:18pt; font-weight: bold;">${requestScope.totalreturnCount}</a>
	  		</div>
		</div>
		<div class="card py-lg-3" style="background-color: #fafafa;">
		  	<div class="card-body text-center">
		    	<h5 class="card-title mb-3">결재전</h5>
			    <a href="<%=ctxPath%>/box/receiveDoc.gw?apstatus=0&yn=1" class="stretched-link" style="font-size:18pt; font-weight: bold;">${requestScope.totaldoCount}</a>
		  	</div>
		</div>
		<div class="card py-lg-3" style="background-color: #fafafa;">
		  	<div class="card-body text-center">
		    	<h5 class="card-title mb-3">협조/수신</h5>
			    <a href="<%=ctxPath%>/box/cooDoc.gw?apstatus=0" class="" style="font-size:18pt; font-weight: bold;">${requestScope.cototalCount}</a>
			    <span style="font-size: 18pt; color: #495057; font-weight: bold;">/</span>
			    <a href="<%=ctxPath%>/box/rbeDoc.gw" class="" style="font-size:18pt; font-weight: bold;">${requestScope.retotalCount}</a>
		  	</div>
		</div>
	</div>
	
	<ul class="list-group list-group-horizontal mt-5">
	  	<li class="list-group-item tabmenu">내가 상신한문서</li>
	  	<li class="list-group-item tabmenu">내가 결재할 문서</li>
	  	<%-- <li class="list-group-item tabmenu">최근 결재 의견</li> --%>
	</ul>
	
	<div class="mt-2 tabcontent">
		<table class="table table-bordered">
			<thead>
				<tr style="background-color: #fafafa;">
					<th style="width: 160px;  text-align: center">기안양식</th>
					<th style="text-align: center">기안제목</th>
					<th style="width: 200px;  text-align: center">결제대기자</th>
					<th style="width: 200px; text-align: center">상신일시</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty requestScope.approvalList}">
					<c:forEach var="apList" items="${requestScope.approvalList}">
					<tr class="listtr">
						<td class="" align="center">${apList.apcaname}</td>
						<td class="">
							<a href="<%= ctxPath%>/docdetail.gw?apno=${apList.apno}">${apList.subject}</a>
						</td>
						<td class="" align="center">${apList.departmentname}부&nbsp;${apList.positionname}&nbsp;${apList.name}</td>
						<td class="" align="center">${apList.apdate}</td>
					</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty requestScope.approvalList}">
					<tr style="height: 150px;">
						<td colspan="4" align="center" >
							<div class="my-5"><i class="fas fa-info-circle" style="color:#d9e4fe;"></i><br>조회 결과가 존재하지 않습니다.</div>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	
	<div class="mt-2 tabcontent">
		<table class="table table-bordered">
			<thead>
				<tr style="background-color: #fafafa;" >
					<th style="width: 100px;  text-align: center">기안양식</th>
					<th style="text-align: center">기안제목</th>
					<th style="width: 200px;  text-align: center">기안자</th>
					<th style="width: 200px; text-align: center">상신일시</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${not empty requestScope.approvaldoList}">
					<c:forEach var="apdList" items="${requestScope.approvaldoList}">
					<tr class="listtr">
						<td class="" align="center">${apdList.apcaname}</td>
						<td class="">
							<a href="<%= ctxPath%>/docdetail.gw?apno=${apdList.apno}">${apdList.subject}</a>
						</td>
						<td class="" align="center">${apdList.departmentname}부&nbsp;${apdList.positionname}&nbsp;${apdList.name}</td>
						<td class="" align="center">${apdList.apdate}</td>
					</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty requestScope.approvaldoList}">
					<tr style="height: 150px;">
						<td colspan="4" align="center" >
							<div class="my-5"><i class="fas fa-info-circle" style="color:#d9e4fe;"></i><br>조회 결과가 존재하지 않습니다.</div>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<%-- 
	<div class="mt-2 tabcontent">
		<table class="table table-bordered">
			<thead>
				<tr>
					<th style="width: 100px;  text-align: center">결재구분</th>
					<th style="width: 100px;  text-align: center">결재처리</th>
					<th style="text-align: center">결재의견</th>
					<th style="width: 200px;  text-align: center">결재자</th>
					<th style="width: 200px; text-align: center">상신일시</th>
				</tr>
			</thead>
		</table>
	</div>
	--%>
</div>