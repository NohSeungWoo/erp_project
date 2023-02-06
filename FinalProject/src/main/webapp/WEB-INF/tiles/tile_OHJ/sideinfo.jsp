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

	$(document).ready(function() {
		
		// 문서로딩되자마자, 전사게시판 내부의 li에 게시판 종류 ajax로 넣어줌.
		$.ajax({ // type은 안쓰면 기본이 get방식이고, data는 where절에 넘겨줄거 없으니 생략함.
			url:"<%= ctxPath%>/viewCategoryList.gw",
		//	type:"GET",
			dataType:"JSON",
			success:function(json){ 
				// json ==> [{"bCategoryName":"공지사항","bCategorySeq":"1"},{"bCategoryName":"자유게시판","bCategorySeq":"2"},{"bCategoryName":"건의사항","bCategorySeq":"3"}]
				var html = "";
				
				if(json.length > 0){
					$.each(json,function(index, item){
						html += "<li>";
						html += "<a href='<%= ctxPath%>/recentList.gw?bCategorySeq="+item.bCategorySeq+"'>"+item.bCategoryName+"</a>";
						html += "</li>";
					});
				}
				
				$("ul#allCompany").html(html);
				
			},
			error: function(request, status, error){
            	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});
		
	}); // end of ready(); ---------------------------------
	
	
	// Function Declaration
	
	// 글쓰기로 이동하는 함수
	function goBoardWrite(){
		location.href = "<%= ctxPath%>/boardWrite.gw";
	}
	
</script>

<div>

	<button type="button" id="writeBtn" class="btn btn-outline-secondary btn-lg" onclick="goBoardWrite();" style="margin-bottom: 20px;">글쓰기<i class="fas fa-pen" style="margin-left: 4px;"></i></button>
	
	
	
	<nav id="sidebar">
		<div class="list-group-flush"> <!-- .list-group-flush 클래스를 사용하여 일부 테두리와 둥근 모서리를 제거합니다. --> <!-- 리스트의 양옆 테두리가 사라짐 -->
	
			<a href="<%= ctxPath%>/recentList.gw" class="list-group-item list-group-item-action">최근 게시물</a> <!-- 마우스 오버 시 회색 배경색을 원하면 선택적으로 .list-group-item-action 클래스를 추가합니다. -->
			
			<!-- .collapse 은 내용물을 숨기는 것임. -->
			<a href="#allCompany" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">전사 게시판</a> <!-- dropdown-toggle을 통해 ▽ 모양 보여줌 --> <!-- 원래는 data-target="#allCompany"랑 연결하는데, 여기선 href로 연결해줌. -->
				<ul class="collapse sidesubmenu" id="allCompany" >
		            <!-- ajax로부터 게시판목록 가져옴 -->
		        </ul>
		    <!--   
			<a href="#" class="list-group-item list-group-item-action">환경설정</a>
			 -->
			<a href="<%= ctxPath%>/makeBCategory.gw" class="list-group-item list-group-item-action">게시판 만들기</a>
			
			<a href="<%= ctxPath%>/board/wordCloud.gw" class="list-group-item list-group-item-action">통계</a>
			<!-- <a href="#" class="list-group-item list-group-item-action">게시판 관리</a> -->
	
		</div>
	</nav>
	
	
	
	
</div>
   