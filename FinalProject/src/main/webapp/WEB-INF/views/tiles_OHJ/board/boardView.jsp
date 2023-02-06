<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% String ctxPath = request.getContextPath(); %>

<style>
	
	.linkStyle{ /* 링크의 디자인처리 */
		color: #37f; 
		font-weight: bold;
	} 
	
	.aStyle{ /* 수정, 삭제링크에 마우스올리면 커서 모양 변경 */
		cursor: pointer;
	}
	
	.tdStyle{ /* 이전글제목,다음글제목에 효과 주기 */
		cursor: pointer;
		color: blue;
		font-size: 1.1rem; /* 기본 폰트에 1.1배 */
	}
	
	.brStyle{border-right: solid 1px #dee2e6;} /* 댓글 테이블에서 부트스트랩으로 table-bordered하면 border가 0이 안됨. 따라서 내가 원하는 디자인처리하려고 class="table"에다가 border-right 디자인처리해줌. */
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		goReadComment(); // 페이징처리 안한 댓글 읽어오기
		
		// 이전글제목에 효과주기
		$(".previous").hover(
			function(){ // mouserover
				$("td#previous1").css({"cursor":"pointer"}); // i태그인 화살표에 css주기
				$("span#previous2").addClass("tdStyle");
			},
			function(){ // mouseout
				$("span#previous2").removeClass("tdStyle");
			}
		);
		
		// 다음글제목에 효과주기
		$(".next").hover(
			function(){ // mouserover
				$("td#next1").css({"cursor":"pointer"}); // i태그인 화살표에 css주기
				$("span#next2").addClass("tdStyle");
			},
			function(){ // mouseout
				$("span#next2").removeClass("tdStyle");
			}
		);
		
	});// end of $(document).ready(function(){})----------------------------
	
	
	// === Functional Declaration === //
	
	// 글수정시에 검색조건과 gobackURL도 넘겨주기
	function goEdit(){
		
		var frm = document.gobackViewFrm;
		
		frm.method = "GET";
		frm.action = "<%= ctxPath%>/boardEdit.gw";
		frm.submit();
		
	}// end of function goEdit(){}----------------------------------------
	
	
	// 정말로 삭제할것인지 아닌지 물어봄. (=== &77. 나는 강사님과 달리 암호확인페이지 없으니 &77 진행안함. ===)
	function delConfirm(){

		var bool = confirm("정말로 글을 삭제하시겠습니까?");
	//	console.log("확인용 bool => " + bool);
		
		if(bool){
			var frm = document.gobackViewFrm;
			
			frm.method = "GET";
			frm.action = "<%= ctxPath%>/boardDel.gw";
			frm.submit();
		}
		// 확인을 누르면 삭제하고, 취소를 누르면 그냥 냅둠.
	}// end of function delConfirm(){}---------------------------------------------
	
		
	
	// 이전글로 이동
	function goPrevious(){
		
		// &126-3(강사님은 없음). : 이전글, 다음글 gobackURL에 적용
		// 강사님
		<%-- <c:set var="v_gobackURL" value='${ fn:replace(requestScope.gobackURL, "&", " ") }' /> --%>
		
		var v_gobackURL = "${requestScope.gobackURL}";
	//	console.log("확인용 v_gobackURL(replace 전) => " + v_gobackURL);
		// 확인용 v_gobackURL(replace 전) => /recentList.gw?fromDate=2021-08-26&toDate=2021-11-24&bCategory=0&searchType=subject&searchWord=
			
		// ★자바스크립트에서는 replaceAll 이 없으므로 replace를 replaceAll처럼 사용한다.
		v_gobackURL = v_gobackURL.replace(/&/gi, " "); // global i(대소문자 ignore하고 모든거를 말함) // &를 " "으로 바꿔줌.
	//	console.log("확인용 v_gobackURL(replace 후) => " + v_gobackURL);
		// 확인용 v_gobackURL(replace 후) => /recentList.gw?fromDate=2021-08-26 toDate=2021-11-24 bCategory=0 searchType=subject searchWord=
		
		location.href = "boardView.gw?boardSeq=${requestScope.boardvo.previousBoardSeq}&fromDate=${requestScope.fromDate}&toDate=${requestScope.toDate}&bCategory=${requestScope.bCategory}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL="+v_gobackURL;
	}
	// 다음글로 이동
	function goNext(){
		
		var v_gobackURL = "${requestScope.gobackURL}";
		v_gobackURL = v_gobackURL.replace(/&/gi, " ");
		
		location.href = "boardView.gw?boardSeq=${requestScope.boardvo.nextBoardSeq}&fromDate=${requestScope.fromDate}&toDate=${requestScope.toDate}&bCategory=${requestScope.bCategory}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL="+v_gobackURL;
	}
	
	
	// 댓글쓰기
	function goCommentWrite(){
		
		var commentContentVal = $("input#commentContent").val().trim();
		if(commentContentVal == ""){
			alert("댓글 내용을 입력하세요!!");
			return; // 종료
		}
		
		// 첨부파일이 없는 댓글쓰기
		goCommentWrite_noAttach();
		
	}// end of function goCommentWrite(){}-------------------------------------
	
	// 첨부파일이 없는 댓글쓰기
	function goCommentWrite_noAttach(){
		
		$.ajax({
			url:"<%= ctxPath%>/boardCommentWrite.gw",
			data:{"fk_boardSeq":"${requestScope.boardvo.boardSeq}",
				  "fk_employeeId":"${sessionScope.loginuser.employeeid}",
				  "content":$("input#commentContent").val()},
			type:"POST",
			dataType:"JSON",
			success:function(json){ // {"n":1} 또는 {"n":0}
				
				var n = json.n;
				
				if(n==0){
					alert("댓글쓰기 실패하였습니다.");
				}
				else{
					alert("댓글이 등록되었습니다.");
					goReadComment(); // 페이징처리 안한 댓글 읽어오기
				}
				
				$("input#commentContent").val("");
				
			},
			error: function(request, status, error){
            	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});
		
	}// end of function goCommentWrite_noAttach(){}-----------------------------
	
	// 페이징처리 안한 댓글 읽어오기
	function goReadComment(){
		
		$.ajax({
			url:"<%= ctxPath%>/readComment.gw",
			data:{"fk_boardSeq":"${requestScope.boardvo.boardSeq}"},
			dataType:"JSON",
			success:function(json){
				var html = "";
				
				if(json.length > 0){
					$.each(json,function(index, item){
						html += "<tr style='border-bottom: solid 1px #dee2e6;'>";
							/* 댓글이 존재하는 경우 */
							html += "<td class='brStyle' align='center'>"+(index+1)+"</td>";
							html += "<td class='brStyle'>"+item.content+"</td>";
							html += "<td class='brStyle' align='center'>"+
										
										"<c:if test='${boardvo.userType == \"secret\"}'>"+
											"***"+
										"</c:if>"+
										"<c:if test='${boardvo.userType == \"public\"}'>"+
											item.positionName+"&nbsp;"+item.name+
										"</c:if>"+
										
									"</td>";
							html += "<td align='center'>"+item.regDate+"</td>";
						html += "</tr>";
					});
				}
				
				else{ // 댓글이 없는 []인 경우
					html += "<tr style='border-bottom: solid 1px #dee2e6;'>";
						/* 댓글이 존재하지 않을 경우 */
						html += "<td colspan='4'>";
						html += "	<div class='my-5' align='center'>";
						html += "		<i class='fas fa-info-circle' style='color: #37f;''></i><br>";
						html += "		조회 결과, 댓글이 존재하지 않습니다.";
						html += "	</div>";
						html += "</td>";
					html += "</tr>";
				}
				
				$("tbody#commentDisplay").html(html);
				
			},
			error: function(request, status, error){
            	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});
		
	}// end of function goReadComment(){}-------------------------------------
	
</script>

<div class="container">


	<!-- 글1개에 대한 정보 보여주기 시작 -->
	
	<!-- 원글이 존재하지 않을 경우 -->
	<c:if test="${empty requestScope.boardvo}"> <!-- get방식이라서 유저가 존재하지 않는 글번호, 숫자타입이 아닌 글번호로 장난칠 수 있다. -->
		<div style="padding: 50px 0; font-size: 16pt; color: red; text-align: center;">장난치지마세요! 해당하는 글은 존재하지 않습니다.</div>
	</c:if>
	
	<!-- 원글이 존재하는 경우 -->
	<c:if test="${not empty requestScope.boardvo}"> 
		
		
		<div class="mb-3 d-flex" style="border: solid 0px red;">
			<!-- 링크 (답글, 수정, 삭제, 게시글종류 이동) -->
			<span class="mr-auto d-flex"> <!-- 바깥에 d-flex를 주고, 내부에 오른쪽margin을 auto로 주면 [*     **] 이런식으로 배치된다. -->
				<a href="#" class="linkStyle mr-2 my-auto">답글</a> <!-- 세로중 가운데에 정렬하기 위해서 바깥에 d-flex를 주고 내부에 y축으로 auto를 줌. -->
				<a onclick="goEdit();" class="linkStyle aStyle mr-2 my-auto">수정</a>
				<a onclick="delConfirm()" class="linkStyle aStyle mr-2 my-auto">삭제</a>
				<!-- <a href="#" class="linkStyle my-auto">이동</a> -->
			</span>
			
			<span>
				<button type="button" class="btn" style="border: solid 1px #dee2e6;" onclick="javascript:location.href='<%= ctxPath%>/recentList.gw'" >전체목록</button>
				
				<%-- === &126. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
				                                 사용자가 목록보기 버튼을 클릭했을 때 돌아갈 페이지를 알려주기 위해
				  	                        현재 페이지 주소를 뷰단으로 넘겨준다. --%>
				<c:set var="v_gobackURL" value='${ fn:replace(requestScope.gobackURL, " ", "&") }' />
				<button type="button" class="btn btn-secondary" onclick="javascript:location.href='<%= ctxPath%>${v_gobackURL}'">검색된목록</button>
			</span>
		</div>
		
		
		<!-- 원글정보 테이블 -->
		<table class="table">
			<tr>
				<th rowspan="2" width="10%" style="padding: 0; text-align: center;">
					
					<c:if test="${empty requestScope.boardvo.profilename}">
					<img alt="기본프로필 이미지.jpg" src="<%= ctxPath%>/resources/images/기본프로필_kh.jpg" width="90" height="100">
					</c:if>
					
					<c:if test="${not empty requestScope.boardvo.profilename}">
						<c:if test='${boardvo.userType == "secret"}'> <!-- 게시판유형이 익명형인 경우 -->
							<img alt="기본프로필 이미지.jpg" src="<%= ctxPath%>/resources/images/기본프로필_kh.jpg" width="90" height="100">
						</c:if>
						<c:if test='${boardvo.userType == "public"}'> <!-- 게시판유형이 일반형인 경우 -->
							<img alt="해당사원의 프로필이미지.jpg" src="<%= ctxPath%>/resources/empIMG/${requestScope.boardvo.profilename}" width="90" height="100">
						</c:if>
					</c:if>
					
				</th>
				<td colspan="4"><strong style="font-size: 18px;">${requestScope.boardvo.subject}</strong></td>
			</tr>
			<tr style="border-bottom: solid 1px #dee2e6;">
				<td>작성자 : 
				
					<c:if test='${boardvo.userType == "secret"}'> <!-- 게시판유형이 익명형인 경우 -->
						***
					</c:if>
					<c:if test='${boardvo.userType == "public"}'> <!-- 게시판유형이 일반형인 경우 -->
						${requestScope.boardvo.positionName} ${requestScope.boardvo.name}
					</c:if>
				
				</td>
				<td>글종류 : ${requestScope.boardvo.bCategoryName}</td>
				<td>조회수 : <span>${requestScope.boardvo.readCount}</span></td>
				<td>작성일자 : ${requestScope.boardvo.regDate}</td>
			</tr>
		</table>
		
		<!-- 글내용 -->
		<div class="p-3" style="border: solid 1px #dee2e6; word-break: break-all; min-height: 300px;">${requestScope.boardvo.content}</div>
		<%-- 
		      style="word-break: break-all; 은 공백없는 긴영문일 경우 width 크기를 뚫고 나오는 것을 막는 것임. 
		             그런데 style="word-break: break-all; 나 style="word-wrap: break-word; 은
		             테이블태그의 <td>태그에는 안되고 <p> 나 <div> 태그안에서 적용되어지므로 <td>태그에서 적용하려면
		      <table>태그속에 style="word-wrap: break-word; table-layout: fixed;" 을 주면 된다.
		--%>
		
		
		
		<!-- 첨부파일 -->
		<c:if test="${not empty requestScope.boardvo.orgFilename}">
		<table class="table mt-3">
			<tr style="border-bottom: solid 1px #dee2e6;">
				<th style="width: 142px;">첨부파일</th>
				<td>
					<c:if test="${sessionScope.loginuser != null}"> <!-- 로그인된 경우 -->
						<i class="fas fa-paperclip mr-2"></i><a href="<%= ctxPath%>/downloadBoardAttach.gw?boardSeq=${requestScope.boardvo.boardSeq}">${requestScope.boardvo.orgFilename}</a><i class="fas fa-download ml-2"></i> <!-- WAS에 있는 실제파일명을 알아오기위해 where절에 글번호를 넘김. -->
					</c:if>
					<c:if test="${sessionScope.loginuser == null}">
						<i class="fas fa-paperclip mr-2"></i>${requestScope.boardvo.orgFilename}<i class="fas fa-download ml-2"></i>
					</c:if>
				</td>
			</tr>
		</table>
		</c:if>
		
	</c:if>
	<!-- 글1개에 대한 정보 보여주기 종료 -->
	
	
	
	<form name="gobackViewFrm"> <!-- 글수정,삭제시 넘겨줄값(성공 또는 취소할때 글1개보는페이지로 들어가기위함) -->
		<input type="hidden" name="boardSeq" value="${requestScope.boardvo.boardSeq}" />
	
		<input type="hidden" name="fromDate" value="${requestScope.fromDate}" />
		<input type="hidden" name="toDate" value="${requestScope.toDate}" />
		<input type="hidden" name="bCategory" value="${requestScope.bCategory}" />
		<input type="hidden" name="searchType" value="${requestScope.searchType}" />
		<input type="hidden" name="searchWord" value="${requestScope.searchWord}" />
		
		<input type="hidden" name="gobackURL" value='${fn:replace(requestScope.gobackURL, "&", " ")}' />
	</form>
	
	
	<!-- &126-1(강사님은 없음). : 이전글, 다음글 보기에 검색조건 추가한 목록을 보도록 하기 위함. -->
<%-- 
	<span>확인용 fromDate : ${requestScope.fromDate}</span><br>
	<span>확인용 toDate : ${requestScope.toDate}</span><br>
	<span>확인용 bCategory : ${requestScope.bCategory}</span><br>
	<span>확인용 searchType : ${requestScope.searchType}</span><br>
	<span>확인용 searchWord : ${requestScope.searchWord}</span><br>
	
	<span>확인용 gobackURL  : ${requestScope.gobackURL}</span>
--%>
	<!-- 이전글, 다음글 보기 -->
	<table class="mt-3">
		<c:if test="${not empty requestScope.boardvo.previousBoardSeq}">
			<tr>
				<th style="font-size: 1.2rem;">이전글</th>
				<td>&nbsp;</td> <!-- 여백을 주기위해 td태그 추가함 -->
				<td class="previous" id="previous1" onclick="goPrevious();"><i class="fas fa-arrow-circle-up fa-2x"></i></td>
				<td>&nbsp;</td>
				<td><span class="previous" id="previous2" onclick="goPrevious();">${requestScope.boardvo.previousSubject}</span></td>
			</tr>
		</c:if>
		<c:if test="${not empty requestScope.boardvo.nextBoardSeq}">
			<tr>
				<th style="font-size: 1.2rem;">다음글</th>
				<td>&nbsp;</td>
				<td class="next" id="next1" onclick="goNext();"><i class="fas fa-arrow-circle-down fa-2x"></i></td>
				<td>&nbsp;</td>
				<td><span class="next" id="next2" onclick="goNext();">${requestScope.boardvo.nextSubject}</span></td>
			</tr>
		</c:if>
	</table>
	
	<br/>
	
	<%-- &83. 댓글쓰기 폼 추가 --%>
	<!--
		카테고리번호가 ${requestScope.boardvo.fk_bCategorySeq}인데, <br>1이면 공지사항, 2이면 자유게시판, 3이면 건의사항임.
		공지사항은 댓글X
		건의사항은 댓글O
		
		해당 boardvo의 fk_bCategorySeq에 해당하는 commentAccess가 y이면 댓글이 있는거다.
	-->
	<c:if test='${requestScope.boardvo.commentAccess eq "y"}'> <%-- ${not empty sessionScope.loginuser} 를 추가안해도 글1개보기를 로그인한 사람만 볼 수 있도록 함. --%>
		
		<!-- 댓글쓰기 시작 -->
		<div class="mt-2" style="border-bottom: solid 1px #dee2e6; display: flex;"> <!-- span태그를 위아래로 꽉 채우기위한 flex -->
			<strong style="border-bottom: solid 2px #37f;">댓글쓰기</strong>
		</div>
		
		<div class="mt-3">
			<form name="commentWriteFrm">
				<input type="text" id="commentContent" style="width: 100%" placeholder="로그인 후 이용하실 수 있습니다."/>
				<input type="text" style="display: none;"> <!-- form태그 속의 input태그가 한 개일 경우, 엔터치면 값이 전송되는것을 방지함.  ★hidden타입은 안된다. -->
			
				<button type="button" class="btn btn-sm mt-1" style="border: solid 1px #dee2e6; color: #37f; float: right;" onclick="goCommentWrite()">등록</button>
				<button type="reset" class="btn btn-sm mt-1 mr-1" style="border: solid 1px #dee2e6; color: #37f; float: right;">취소</button>
			</form>
		</div>
		<!-- 댓글쓰기 끝 -->
		
	
		<!-- === &94. 댓글내용 보여주기 시작 === -->
		<div class="mt-5" style="border-bottom: solid 1px #dee2e6; display: flex;"> <!-- span태그를 위아래로 꽉 채우기위한 flex -->
			<strong style="border-bottom: solid 2px #37f;">댓글내용</strong>
		</div>
		
		<div class="table-responsive mt-3">
			<table class="table table-hover">
				<thead>
					<tr style="text-align: center; background-color: #F7F7F7;"> <!-- 글자 가운데정렬 -->
						<th class="brStyle" width="6%">번호</th>
						<th class="brStyle">댓글내용</th>
						<th class="brStyle" width="13%">작성자</th>
						<th width="17%">작성일자</th>
					</tr>
				</thead>
				<tbody id="commentDisplay">
				</tbody>
			</table>
		</div>
		<!-- 댓글내용 보여주기 종료 -->
			
	</c:if>

	
<%--  
<!-- 가비아 그룹웨어나 네이버 같은 댓글을 하고싶을때 디자인처리 -->
	<!-- 댓글쓰기 및 댓글보여주기 시작 -->
	<div class="mt-5" style="border-bottom: solid 1px #dee2e6; display: flex;"> <!-- span태그를 위아래로 꽉 채우기위한 flex -->
		<strong style="border-bottom: solid 2px #37f;">댓글</strong>
	</div>
	
	<div style="background-color: #f4f5f7">
	
		<span>2개의 댓글</span>
		
		<div class="container">
			<div style="border: solid 1px red;">
				<img alt="기본프로필.jpg" src="<%= request.getContextPath()%>/resources/images/기본프로필.JPG" width="45" height="50">
				<span>서강준</span>
				<span>2021-11-10 14:00:01</span>
				<span>네~~~알겠습니다~~~!</span>
			</div>
		</div>
		
	</div> 
	<!-- 댓글쓰기 및 댓글보여주기 끝 -->
--%>	
	
	
</div>