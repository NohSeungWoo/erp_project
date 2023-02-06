<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ======= #28. tile2 중 sideinfo 페이지 만들기  ======= --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
	a {
		color:#495057;
	}
	.sidesubmenu {
		font-size:11pt; 
		background-color:#fafafa; 
		list-style: none;
		margin-bottom: 0;	
		padding-bottom: 2px;
	}
	.sidesubmenu li {
		margin : 3px 0;
	}
</style>

<script type="text/javascript">

	$(document).ready(function() {
	
	}); // end of ready(); ---------------------------------

</script>

<nav id="sidebar">
	<div class="list-group-flush">
		<a href="<%= request.getContextPath()%>/admin/empList.gw" class="list-group-item list-group-item-action ">직원목록</a>
		<a href="<%= request.getContextPath()%>/admin/empRegister.gw" class="list-group-item list-group-item-action">직원등록</a>
	  	<a href="<%= request.getContextPath()%>/admin/department.gw" class="list-group-item list-group-item-action">부서목록</a>
	  	<a href="<%= request.getContextPath()%>/admin/empChart.gw" class="list-group-item list-group-item-action">직원통계</a>
	  	<a href="<%= request.getContextPath()%>/admin/adminList.gw" class="list-group-item list-group-item-action">관리자목록</a>
	</div>
</nav>
   