<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- ======= #28. tile2 중 sideinfo 페이지 만들기  ======= --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style>
	
	button#writeBtn{
		width: 100%;
	}
	
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

	//일정등록으로 이동하는 함수
	function goScheduleWrite(){
		location.href = "<%= ctxPath%>/schedulePopup.gw";
	}
	
</script>

<div>

	<button type="button" id="writeBtn" class="btn btn-outline-secondary btn-lg" onclick="goScheduleWrite();" style="margin-bottom: 20px;">일정 등록<i class="fas fa-pen" style="margin-left: 4px;"></i></button>
	
	
	
	<nav id="sidebar">
		<div class="list-group-flush"> <!-- .list-group-flush 클래스를 사용하여 일부 테두리와 둥근 모서리를 제거합니다. --> <!-- 리스트의 양옆 테두리가 사라짐 -->
	
			<a href="<%= ctxPath%>/recentList.gw" class="list-group-item list-group-item-action">전체 일정</a> <!-- 마우스 오버 시 회색 배경색을 원하면 선택적으로 .list-group-item-action 클래스를 추가합니다. -->
			
			<!-- .collapse 은 내용물을 숨기는 것임. -->
			<a href="#allCompany" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">내 일정</a> <!-- dropdown-toggle을 통해 ▽ 모양 보여줌 --> <!-- 원래는 data-target="#allCompany"랑 연결하는데, 여기선 href로 연결해줌. -->
				<ul class="collapse sidesubmenu" id="allCompany" >
		            <!-- ajax로부터 게시판목록 가져옴 -->
		        </ul>
		</div>
	</nav>
	
	
	
	
</div>
   