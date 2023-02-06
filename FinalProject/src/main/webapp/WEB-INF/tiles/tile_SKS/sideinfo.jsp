<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ======= #28. tile2 중 sideinfo 페이지 만들기  ======= --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath = request.getContextPath();
%>

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
		<a href="<%= ctxPath%>/index.gw" class="list-group-item list-group-item-action ">홈</a>
		<a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">출퇴근 관리</a>
	  		<ul class="collapse sidesubmenu" id="homeSubmenu" >
	            <li>
	                <a href="<%= ctxPath%>/commutelist.gw">출퇴근 내역</a>
	            </li>
	            
	       </ul>
	    <a href="#pageSubmenu1" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">근태 관리</a>
	  		<ul class="collapse sidesubmenu" id="pageSubmenu1" >
	  			<li>
	                <a href="<%= ctxPath%>/workadd.gw">근무 관리</a>
	            </li>
	          	<li>
	                <a href="<%= ctxPath%>/worklist.gw">근무 내역</a>
	            </li>
	            
	       </ul>   
	    <a href="#pageSubmenu2" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">휴가 관리</a>
	  		<ul class="collapse sidesubmenu" id="pageSubmenu2" >
	            <li>
	                <a href="<%= ctxPath%>/vacation.gw">휴가 관리</a>
	            </li>
	            <li>
	                <a href="<%= ctxPath%>/vacationlist.gw">휴가 조회</a>
	            </li>
	        </ul>    
	  
	</div>
</nav>
   