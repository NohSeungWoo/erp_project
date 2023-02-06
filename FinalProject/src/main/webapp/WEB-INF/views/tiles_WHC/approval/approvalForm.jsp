<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style type="text/css">
</style>
<script type="text/javascript">
	
	$(document).ready(function(){
		
	});
	
</script>
<div class="container-fluid">

	<h4 style="mb-3"><b>기안 양식함</b></h4>
	<div class="row">
		<c:forEach var="apc" items="${requestScope.apcList}">
			<div class="col-lg-2 mb-2">
		  	<div class="card">
			    <div class="card-header text-center">${apc.apcaname}</div>
			    <div class="card-body text-center">
			    	<p class="card-text"><small>${apc.apcaname} 입니다.</small></p>
			    </div>
			    <a href="<%= ctxPath%>/addApproval.gw?apcano=${apc.apcano}&apcaname=${apc.apcaname}" class="stretched-link"></a>
		    </div>
		  </div>
		</c:forEach>
	</div>
</div>	