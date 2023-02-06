<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style>
	
	th{border-right: solid 1px #dee2e6;} /* class가 table이니까 내가 원하는 table-bordered 처럼 보이도록 함. */
	
	.star { /* 필수 입력 사항 표시 */
		color: red;
        font-weight: bold;
        font-size: 15pt;
	}
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
		
		<%-- === &167. 스마트 에디터 구현 시작 === --%>
		//전역변수
		var obj = [];
		
		//스마트에디터 프레임생성
		nhn.husky.EZCreator.createInIFrame({
		    oAppRef: obj,
		    elPlaceHolder: "content",
		    sSkinURI: "<%= ctxPath%>/resources/smarteditor/SmartEditor2Skin.html",
		    htParams : {
		        // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
		        bUseToolbar : true,            
		        // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
		        bUseVerticalResizer : true,    
		        // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
		        bUseModeChanger : true,
		    }
		});
		<%-- === 스마트 에디터 구현 끝 === --%>
		
		
		// input태그에 글제목 한글자씩 입력할때마다, 총글자수 나타내기
		$("input#subject").keyup(function(){
			
		//	console.log("확인용 $(this).val() : " + $(this).val());
			
			var subjectLen = $(this).val().length;
			// 입력된 글제목의 길이를 알아온다.
		//	console.log("확인용 subjectLen : " + subjectLen);
			
		/*
			// 한글이든 영어이든 상관없다.
			console.log("확인용 '안녕'.length : " + "안녕".length); // 2
			console.log("확인용 'ab'.length : " + "ab".length); // 2
			console.log("확인용 '12'.length : " + "12".length); // 2
		*/
			
			$("span#subjectLen").text(subjectLen);
		}); // end of $("input#subject").keyup(function(){})------------
		
		
		// 글쓰기 버튼 누르면, 유효성 검사 후 전송함.
		$("button#btnWrite").click(function(){
			
			<%-- === 스마트 에디터 구현 시작 === --%>
			//id가 content인 textarea에 에디터에서 대입
			 obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
			<%-- === 스마트 에디터 구현 끝 === --%>
			
			
			// 게시판 종류 유효성 검사
			var boardTypeVal = $("select#boardType").val();
		//	console.log("확인용 boardTypeVal : " + boardTypeVal);
			
			if(boardTypeVal == "0"){ // "-[필수]옵션을 선택해주세요-"를 클릭한 경우
				alert("게시판 종류를 선택하세요!!");
				return;
			}
			
			
			// 글제목 유효성 검사
			var subjectVal = $("input#subject").val().trim();
			if(subjectVal == ""){
				alert("글제목을 입력하세요!!");
				return;
			}
			
			
			// 글내용 유효성 검사(스마트에디터 사용 안할시)
		<%-- 
			var contentVal = $("textarea#content").val().trim();
			if(contentVal == ""){
				alert("글내용을 입력하세요!!");
				return;
			}
		--%>
		<%-- === 스마트에디터 구현 시작 === --%>
	       	// 스마트에디터 사용시 무의미하게 생기는 p태그 제거
			var contentval = $("textarea#content").val();
			       
			// === 확인용 ===
			// alert(contentval); // content에 내용을 아무것도 입력치 않고 쓰기할 경우 알아보는것.
			// "<p>&nbsp;</p>" 이라고 나온다.
			 
			// 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기전에 먼저 유효성 검사를 하도록 한다.
			// 글내용 유효성 검사 
			if(contentval == "" || contentval == "<p>&nbsp;</p>") {
				alert("글내용을 입력하세요!!");
				return;
			}
			    
			// 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기
			contentval = $("textarea#content").val().replace(/<p><br><\/p>/gi, "<br>"); //<p><br></p> -> <br>로 변환
			/*    
			            대상문자열.replace(/찾을 문자열/gi, "변경할 문자열");
			    ==> 여기서 꼭 알아야 될 점은 나누기(/)표시안에 넣는 찾을 문자열의 따옴표는 없어야 한다는 점입니다. 
			                 그리고 뒤의 gi는 다음을 의미합니다.
			
			       g : 전체 모든 문자열을 변경 global
			       i : 영문 대소문자를 무시, 모두 일치하는 패턴 검색 ignore
			*/    
		    contentval = contentval.replace(/<\/p><p>/gi, "<br>"); //</p><p> -> <br>로 변환  
		    contentval = contentval.replace(/(<\/p><br>|<p><br>)/gi, "<br><br>"); //</p><br>, <p><br> -> <br><br>로 변환
		    contentval = contentval.replace(/(<p>|<\/p>)/gi, ""); //<p> 또는 </p> 모두 제거시
		
		    $("textarea#content").val(contentval);
		 
		    // alert(contentval);
		<%-- === 스마트에디터 구현 끝 === --%>
			
			// 폼(form)을 전송(submit)
			var frm = document.writeFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/boardWriteEnd.gw";
			frm.submit();
			
		});// end of $("button#btnWrite").click(function(){})-------------
		
	});// end of $(document).ready(function(){})--------------------------
	
</script>

<div class="container">

	<h3>글쓰기</h3>
	<hr style="border: solid 1px gray;" />
	
	
	<div style="float: right;">
		<span class="star">*</span><span style="font-size: 15px;">&nbsp;필수입력사항</span>
	</div>
	
	<%-- <form name="writeFrm"> --%>
	<%-- === &149. 파일첨부하기 ===
			  먼저 위의 <form name="writeFrm"> 을 주석처리한 이후에 아래와 같이 해야 한다.
			 enctype="multipart/form-data" 를 해주어야만 파일첨부가 되어진다. (파일첨부는 무조건 POST방식임.)
	--%>
	<form name="writeFrm" enctype="multipart/form-data">
		<!-- 글쓰기에 대한 정보 테이블 시작 -->
		<div class="table-responsive">
			<table class="table">
				<tr>
					<th>게시판종류&nbsp;<span class="star">*</span></th>
					<td>
						<select name="fk_bCategorySeq" id="boardType" style="height: 30px;">
							<option value="0">-[필수]옵션을 선택해주세요-</option>
							<optgroup label="전사 게시판">
							
							<c:forEach var="category" items="${requestScope.bcategoryList}">
							
								<c:if test="${sessionScope.loginuser.admin == 1}"> <!-- 관리자일 경우, 모든 카테고리 목록을 다 보여준다. --> 
									<option value="${category.bCategorySeq}">${category.bCategoryName}</option>
								</c:if>
							
							
								<c:if test="${sessionScope.loginuser.admin != 1}"> <!-- 관리자가 아닐 경우, 쓰기권한이 있는 카테고리 목록만 보여준다. -->
									<c:if test='${category.writeAccess == "y"}'> <!-- 일반사용자의 글쓰기허용이 yes -->
										<option value="${category.bCategorySeq}">${category.bCategoryName}</option>
									</c:if>
								</c:if>
								
							</c:forEach>
							
							</optgroup>
							<!-- 
							<optgroup label="그룹 게시판">
								<option>인사팀</option>
								<option>회계팀</option>
							</optgroup>
							 -->
						</select>
					</td>
				</tr>
				<tr>	
					<th>글제목&nbsp;<span class="star">*</span></th>
					<td>
						<input type="text" name="subject" id="subject" size="85" maxlength="50" placeholder="글제목 입력"/>
					<!-- 
						size="85"		: 웹페이지상에 보여주는 길이
						maxlength="50"  : 입력될 수 있는 글자(한글 또는 영문)의 최대글자수 -> 오라클DB의 컬럼길이와 똑같이 맵핑
					-->
						<span id="subjectLen" style="font-weight: bold;">0</span>/50
					</td>
				</tr>
				
				<%-- === &150. 파일첨부 타입 추가하기 === --%>
				<tr style="border-bottom: solid 1px #dee2e6;">
					<th>파일첨부</th>
					<td>
						<!-- <button type="button" id="fileAttach" class="btn btn-outline-secondary btn-sm">+</button> -->
						<input type="file" name="attach" />
					</td>
				</tr>
				
				<!-- 
				<tr style="border-bottom: solid 1px #dee2e6;">
					<th>참조글</th>
					<td><button type="button" id="refBoard" class="btn btn-outline-secondary btn-sm">+</button></td>
				</tr>
			 	-->
			</table>
		</div>
		<!-- 글쓰기에 대한 정보 테이블 종료 -->
		
		<!-- 글내용 쓰기 -->
		<textarea style="width: 100%; height: 500px;" name="content" id="content" ></textarea>
		
		<!-- 글쓰기/취소 버튼 -->
		<div align="center" style="margin-bottom: 20px; margin-top: 20px;">
			<button type="button" class="btn btn-primary btn-lg mr-3" id="btnWrite">글쓰기</button>
			<button type="button" class="btn btn-secondary btn-lg" onclick="javascript:history.back()">취소</button>
		</div>
		
		<!-- boardVO에 자동으로 set해서, board테이블에 insert하기 위한 용도 -->
		<input type="hidden" name="fk_employeeId" value="${sessionScope.loginuser.employeeid}" />
		
		
	</form>
	
</div>