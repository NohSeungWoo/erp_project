<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>  

<style type="text/css">
	.st {
		display: inline-block;
		font-weight: bold;
		width: 100px;
	}
	.fst {
		display: inline-block;
		width: 60%;
	}
	.fsd {
		display: inline-block;
		width: 180px;
	}
	.listtr:hover {
   		background-color: #f2f6ff;
   	}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		
		var date = new Date();
		var yyyy = date.getFullYear();
		var mm = date.getMonth()+1;
		
		
		if(mm < 10) {
			mm = "0" + mm;
		}
		
		var dd = date.getDate();
		if(dd < 10) {
			dd = "0" + dd;
		}
		
		$("input#makeFromDate").val((yyyy-1)+"-"+mm+"-"+dd);
		$("input#makeToDate").val(yyyy+"-"+mm+"-"+dd); // 2021-11-17
		
		$("button#searchBtn").click(function(){
			
			var frm = document.searchFrm;
			frm.method = "GET";
			frm.action = "receiveDoc.gw"; // 상대경로
			frm.submit();
		});
		
		
		// === 검색 유지시키기 시작 === //
		var makeFromDate = "${requestScope.searchmap.makeFromDate}";
		if( makeFromDate != "" ) {
			$("input#makeFromDate").val(makeFromDate);
		}
		
		var makeToDate = "${requestScope.searchmap.makeToDate}";
		if( makeToDate != "" ) {
			$("input#makeToDate").val(makeToDate);
		}
		
		var fk_apcano = "${requestScope.searchmap.fk_apcano}";
		if( fk_apcano != "" ) {
			$("select#fk_apcano").val(fk_apcano);
		}
		
		var searchWord = "${requestScope.searchmap.searchWord}";
		if( searchWord != "" ) {
			$("input#searchWord").val(searchWord);
		}
		// === 검색 유지시키기 끝 === //
		
	});
</script>

<div class="container-fluid">
	<c:if test="${requestScope.searchmap.apstatus == 0 && requestScope.searchmap.yn == 1}">
		<h4 class="mb-3"><b>결재전</b></h4>
	</c:if>
	<c:if test="${requestScope.searchmap.apstatus == 0 && requestScope.searchmap.yn == 0}">
		<h4 class="mb-3"><b>진행중</b></h4>
	</c:if>
	<c:if test="${requestScope.searchmap.apstatus == 1 && requestScope.searchmap.yn == 0}">
		<h4 class="mb-3"><b>완료된</b></h4>
	</c:if>
	<c:if test="${requestScope.searchmap.apstatus == 2 && requestScope.searchmap.yn == 0}">
		<h4 class="mb-3"><b>반려된</b></h4>
	</c:if>
	<hr>
	<form name="searchFrm">
		<input type="hidden" name="apstatus" value="${requestScope.searchmap.apstatus}">
		<input type="hidden" name="yn" value="${requestScope.searchmap.yn}">
		<div class="mb-3 px-4">
			<div class="mb-4">
				<div class="st mr-1">상신일</div><input class="form-control fsd" id="makeFromDate" name="makeFromDate" type="date"> ~ <input class="form-control fsd" id="makeToDate" name="makeToDate" type="date">
			</div>
			<div class="row mb-4">
				<div class="col-lg-6 form-group">
					<span class="st">기안양식</span>
					<select class="form-control fst" id="fk_apcano" name="fk_apcano">
						<option value="">전체</option>
						<c:forEach var="apc" items="${requestScope.apcList}">
							    <option value="${apc.apcano}">${apc.apcaname}</option>
						</c:forEach>
					</select>
				</div>	
				<div class="col-lg-6">
					<span class="st mr-1">검색</span><input class="form-control fst" id="searchWord" name="searchWord" type="text">
				</div>
			</div>
		</div>
		<hr>
		<div class="mb-5" style="display:flex;">
			<button type="button" style="margin:0 auto;" class="btn btn-primary px-5" name="searchBtn" id="searchBtn" >조회</button>
		</div>
	</form>
	<div class="mb-1">총 ${requestScope.searchmap.totalCount}건</div>
	<c:if test="${requestScope.searchmap.apstatus == 0}">
		<div class="table-responsive">
			<table class="table table-bordered">
				<thead>
					<tr style="text-align: center; background-color: #fafafa;">
						<th style="width: 160px;  text-align: center">기안양식</th>
						<th style="text-align: center">기안제목</th>
						<th style="width: 200px;  text-align: center">기안자</th>
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
	</c:if>
	
	<c:if test="${requestScope.searchmap.apstatus == 1}">
		<div class="table-responsive">
			<table class="table table-bordered">
				<thead>
					<tr style="text-align: center; background-color: #fafafa;">
						<th style="width: 100px;  text-align: center">문서번호</th>
						<th style="width: 160px;  text-align: center">기안양식</th>
						<th style="text-align: center">기안제목</th>
						<th style="width: 200px; text-align: center">상신일시</th>
						<th style="width: 200px;  text-align: center">완료일시</th>
					</tr>
				</thead>
				<tbody>
						<c:if test="${not empty requestScope.approvalList}">
							<c:forEach var="apList" items="${requestScope.approvalList}">
							<tr class="listtr">
								<td class="" align="center">${apList.apno}</td>
								<td class="" align="center">${apList.apcaname}</td>
								<td class="">
									<a href="<%= ctxPath%>/docdetail.gw?apno=${apList.apno}">${apList.subject}</a>
								</td>
								<td class="" align="center">${apList.apdate}</td>
								<td class="" align="center">${apList.eddate}</td>
							</tr>
							</c:forEach>
						</c:if>
						<c:if test="${empty requestScope.approvalList}">
						<tr style="height: 150px;">
							<td colspan="5" align="center" >
								<div class="my-5"><i class="fas fa-info-circle" style="color:#d9e4fe;"></i><br>조회 결과가 존재하지 않습니다.</div>
							</td>
						</tr>
						</c:if>
				</tbody>
			</table>
		</div>
	</c:if>
	
	<c:if test="${requestScope.searchmap.apstatus == 2}">
		<div class="table-responsive">
			<table class="table table-bordered">
				<thead>
					<tr style="text-align: center; background-color: #fafafa;">
						<th style="width: 160px;  text-align: center">기안양식</th>
						<th style="text-align: center">기안제목</th>
						<th style="width: 200px; text-align: center">상신일시</th>
						<th style="width: 200px;  text-align: center">반려일시</th>
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
								<td class="" align="center">${apList.apdate}</td>
								<td class="" align="center">${apList.eddate}</td>
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
	</c:if>
	<div align="center" style="width: 70%; border: solid 0px gray; margin: 20px auto;">
		${requestScope.searchmap.pageBar}
	</div>
</div>