<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.net.InetAddress"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ======= #27. tile1 중 header 페이지 만들기 (#26.번은 없다 샘이 장난침.) ======= --%>
<%
   String ctxPath = request.getContextPath();

   // === #172. (웹채팅관련3) === 
   // === 서버 IP 주소 알아오기(사용중인 IP주소가 유동IP 이라면 IP주소를 알아와야 한다.) ===
   InetAddress inet = InetAddress.getLocalHost(); 
   String serverIP = inet.getHostAddress();
   
 // System.out.println("serverIP : " + serverIP);
 // serverIP : 211.238.142.72
   
   // String serverIP = "211.238.142.72"; 만약에 사용중인 IP주소가 고정IP 이라면 IP주소를 직접입력해주면 된다.
   
   // === 서버 포트번호 알아오기   ===
   int portnumber = request.getServerPort();
 // System.out.println("portnumber : " + portnumber);
 // portnumber : 9090
   
   String serverName = "http://"+serverIP+":"+portnumber; 
 // System.out.println("serverName : " + serverName);
 // serverName : http://211.238.142.72:9090 
%>
	
<style type="text/css">
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		$(".hovmenu").hover(/* header부분에서 각자 파트는 기본 gray이며, hover를 하면 lightgray로 색상이 변함*/
			function(){
				$(this).css("color","lightgray");
			},
			function(){
				$(this).css("color","gray");
		});
		
	});

</script>

</head>
<body>
	<div class="container-fluid" style="max-width:1600px">
		<nav class="navbar navbar-expand-lg navbar-dark pt-2" style="background-color: black;"> <!-- navbar-dark와 bg-dark로 네비게이션바 색상줌. -->
		
			<!-- Brand/logo --> <!-- Font Awesome 5 Icons -->
			<%-- <a class="navbar-brand" href="<%= ctxPath %>/index.gw" style="color: white;">그룹<i class="fab fa-google-wallet fa-lg"></i></a> --%>
			<a href="<%= ctxPath %>/" style="margin-right: 10%; font-size:20pt; font-weight: bold; color: white;"><i class="fab fa-gofore"></i>roup<i class="fab fa-google-wallet fa-lg"></i>are</a>
			
			<!-- 아코디언 같은 Navigation Bar 만들기 -->
		    <button class="navbar-toggler" style="border-color: white; " type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
		      <!-- <span class="navbar-toggler-icon"></span> -->
		      <i class="fas fa-bars" style="color: white;"></i>
		    </button>
			
			<div class="collapse navbar-collapse" id="collapsibleNavbar">
			  <ul class="navbar-nav mx-auto" style="font-size:14pt;">
			  <%-- 본인 페이지에  hovmenu 클래스 없애고 style="color:gray;" 지우면 스타일적용됩니다 => 지금은 '게시판'에 적용함. --%>
			     <li class="nav-item active mx-2" style="text-align: center;">
			        <a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>"><i class="fas fa-home fa-lg"></i><br>홈</a>
			     </li>
			     <li class="nav-item active mx-2" style="text-align: center;">
			     	<a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>/timemanager.gw"><i class="fas fa-stopwatch fa-lg"></i><br>근태관리</a>
			     </li>
			     <li class="nav-item active mx-2" style="text-align: center;">
			     	<a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>/approval.gw"><i class="far fa-file-alt fa-lg"></i><br>전자결재</a>
			     </li>
			     <li class="nav-item active mx-2" style="text-align: center;">
			     	<a class="nav-link menufont_size" href="<%= ctxPath %>/recentList.gw"><i class="fas fa-chalkboard fa-lg"></i><br>게시판</a>
			     </li>
			     <li class="nav-item active mx-2" style="text-align: center;">
			     	<a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>"><i class="fas fa-user-friends fa-lg"></i><br>조직도</a>
			     </li>
			     <li class="nav-item active mx-2" style="text-align: center;">          
	             	<a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>"><i class="far fa-calendar-alt fa-lg"></i><br>일정</a>
	          	 </li>
			     <li class="nav-item active mx-2" style="text-align: center;">
	             	<a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%= ctxPath %>/chat.gw"><i class="far fa-envelope fa-lg"></i><br>웹채팅</a>
	          	 </li>
	          	 <li class="nav-item active mx-2" style="text-align: center;">  
                   <a class="nav-link menufont_size hovmenu" style="color:gray;" href="<%=ctxPath%>/survey.gw"><i class="fas fa-book-reader"></i><br>설문조사</a>
                 </li>
			     <c:if test="${sessionScope.loginuser != null and sessionScope.loginuser.admin == '1' }"> <%-- admin 으로 로그인 했으면 --%>
					 <li class="nav-item dropdown">
				        <a class="nav-link dropdown-toggle menufont_size text-info" href="#" id="navbarDropdown" data-toggle="dropdown"> 
				          	 관리자전용   	                           
				        </a>
				        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
				           <%-- <a class="dropdown-item text-info" href="<%= ctxPath %>">사원등록</a> --%>
				           <a class="dropdown-item text-info" href="<%= ctxPath %>">사원관리</a>
				           <div class="dropdown-divider"></div>
				           <a class="dropdown-item text-info" href="<%= ctxPath %>">추가기능있으면 넣기</a>
				        </div>
				     </li>
		     	</c:if>
			  </ul>
			  <ul class="navbar-nav list-group-horizontal mt-sm-0 mt-2 ml-auto nav_text">
		    <%--
		    	<li class="nav-item ml-2" ><a class="nav-link fas fa-search fa-lg" style="color:white" href="<%= ctxPath%>"></a></li>
		    	<li class="nav-item ml-2" ><a class="nav-link far fa-bell fa-lg" style="color:white" href="<%= ctxPath%>"></a></li>
		    --%>	
		    	<c:if test="${sessionScope.loginuser == null}">
		    		<li class="nav-item ml-2" ><a class="nav-link fas fa-sign-in-alt hovmenu" style="color:gray" href="<%= ctxPath%>/login.gw">로그인</a></li>
		    	</c:if>
		    	<c:if test="${sessionScope.loginuser != null}">
			    	<li class="nav-item ml-2" ><a class="nav-link far fa-user fa-lg hovmenu" style="color:gray" href="<%= ctxPath%>/mypage.gw"></a></li>
			    	<li class="nav-item ml-2" ><a class="nav-link fas fa-sign-out-alt hovmenu" style="color:gray" href="<%= ctxPath%>/logout.gw">로그아웃</a></li>
		    	</c:if>
	   		  </ul>	  
			</div>
			
		</nav>
	</div>
	<hr style="margin:0">