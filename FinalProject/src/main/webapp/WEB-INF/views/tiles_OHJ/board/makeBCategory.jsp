<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% String ctxPath = request.getContextPath(); %>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		var b_flagDulicateClick = false;
		// 게시판 만들기 버튼을 클릭시 "중복확인" 을 클릭했는지 클릭안했는지를 알아보기위한 용도임.
		
		// 카테고리명 중복검사하기
		$("button#cNameCheck").click(function(){
			b_flagDulicateClick = true; // 중복검사버튼을 클릭했는지 안했는지 여부이며, 클릭했으니 true를 준다.
			
			var bCategoryName = $('input#bCategoryName').val().trim();
			
			if(bCategoryName == ""){
				alert("카테고리명을 입력해주세요!");
				$("input#bCategoryName").val(""); // "      " 이런것들 제거함.
			}
			else{
				// ajax처리
				$.ajax({
					url:"<%= ctxPath%>/cNameDuplicateCheck.gw",
					data:{"bCategoryName":bCategoryName},
					type:"POST",
					dataType:"JSON",
					success:function(json){
						
						if(json.isExists){ // true : 입력한 카테고리명이 이미 사용중이라면
							$("span#cNameCheckResult").html("-> 이미 사용중 이므로 사용불가!").css("color","red");
							$("input#bCategoryName").val("");
							b_flagDulicateClick = false; // 카테고리명을 다시 입력해야하므로, 중복체크를 안한 처리한다.
						}
						else{ // false : 입력한 카테고리명이 DB 테이블에 존재하지 않는 경우
							$("span#cNameCheckResult").html("-> 사용가능~!").css("color","blue");
						}
						
					},
					error: function(request, status, error){
		            	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		            }
				});
			}
		});
		
		// 카테고리명이 변경되면 중복검사여부를 초기화
		$("input#bCategoryName").bind("change",function(){
			b_flagDulicateClick = false;
		});
		
		
		// 게시판 만들기 버튼 누르면, 유효성 검사 후 전송함.
		$("button#btnMake").click(function(){
			
			// 카테고리명 유효성 검사
			if(!b_flagDulicateClick){
				alert("카테고리명 중복확인을 클릭하여 중복검사를 하세요!!");
				return;
			}
			
			// 폼(form)을 전송(submit)
			var frm = document.makeFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/makeBCategoryEnd.gw";
			frm.submit();
		//	alert("값 전송~~");
			
		});// end of $("button#btnMake").click(function(){})----------------
		
	});// end of $(document).ready(function(){})---------------------
	
</script>

<div class="container">

	<h3>게시판 만들기</h3>
	<hr style="border: solid 1px gray;" />
	
	
	<form name="makeFrm">
	
		<div class="table-responsive" style="border-top: solid 1px #dee2e6; border-bottom: solid 1px #dee2e6;">
			<table class="table table-borderless">
				<tr>
					<th>카테고리명</th>
					<td>
						<input type="text" style="display: none;" /> <!-- form태그 속의 input태그가 한개뿐이므로 엔터가 적용되는거 막는 용도 -->
						<input type="text" name="bCategoryName" id="bCategoryName" maxlength="100" placeholder="게시판 제목을 입력하세요" style="width: 70%;"/>
						<button id="cNameCheck" type="button" class="btn btn-outline-primary btn-sm">중복확인</button><br>
						<span id="cNameCheckResult"></span>
					</td>
				</tr>
				<tr>	
					<th>게시판 유형</th>
					<td>
						<input type="radio" name="userType" id="public" value="public" checked class="mr-1"/><label for="public" style="width: 35%;">일반형</label>
						<input type="radio" name="userType" id="secret" value="secret" class="mr-1"/><label for="secret">익명형</label>
					</td>
				</tr>
				<tr>	
					<th>말머리 설정</th>
					<td>
						<input type="radio" name="header" id="headerY" value="y" class="mr-1"/><label for="headerY" style="width: 35%;">사용</label>
						<input type="radio" name="header" id="headerN" value="n" checked class="mr-1"/><label for="headerN">미사용</label>
					</td>
				</tr>
				<tr>	
					<th>글쓰기 권한</th>
					<td>
						<input type="radio" name="writeAccess" id="writeY" value="y" checked class="mr-1"/><label for="writeY" style="width: 35%;">누구나</label>
						<input type="radio" name="writeAccess" id="writeN" value="n" class="mr-1"/><label for="writeN">관리자만</label>
					</td>
				</tr>
				<tr>	
					<th>댓글 사용</th>
					<td>
						<input type="radio" name="commentAccess" id="commentY" value="y" class="mr-1"/><label for="commentY" style="width: 35%;">사용</label>
						<input type="radio" name="commentAccess" id="commentN" value="n" checked class="mr-1"/><label for="commentN">사용 안함</label>
					</td>
				</tr>
				<!-- 
				<tr>	
					<th>답변 사용</th>
					<td>
						<input type="radio" name="replyAccess" id="replyY" value="" />사용
						<input type="radio" name="replyAccess" id="replyN" value="" />사용 안함
					</td>
				</tr>
				 -->
			</table>
		</div>
		
		
		
		<!-- 만들기/취소 버튼 -->
		<div align="center" style="margin-bottom: 20px; margin-top: 20px;">
			<button type="button" class="btn btn-primary btn-lg mr-3" id="btnMake">게시판 만들기</button>
			<button type="button" class="btn btn-secondary btn-lg" onclick="javascript:history.back()">취소</button>
		</div>
	
	</form>
	
</div>	
