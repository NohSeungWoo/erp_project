<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<style type="text/css">

	.findMyInfo:hover {
		cursor: pointer;
		text-decoration: underline;
	}

</style>

<script type="text/javascript">

	$(document).ready(function() {
		
		// === 로컬 스토리지에 저장된 이메일 값이 있으면 값 불러오기 === //
		var saveEmail = localStorage.getItem("saveEmail");
		
		if(saveEmail != null) {
			$("input#email").val(saveEmail);
			$("input#saveEmailCheckbox").prop("checked", true);
		}
		
		$(".error").hide();
		
		// 로그인 버튼 클릭 이벤트
		$("button#loginBtn").click(function() {
			
			goLogin();
			
		});// end of $("button#loginBtn").click(function() {}
		
		
		// === 이메일 찾기 클릭 이벤트 === //
		$("span#findMyEmail").click(function() {
			
			var url = "<%= request.getContextPath()%>/emailFind.gw;"
			
			var pop_width = 500;
		    var pop_height = 400;
		    var pop_left = Math.ceil( (window.screen.width - pop_width) / 2 ); 		// 정수로 형변환
		    var pop_top  = Math.ceil( (window.screen.height - pop_height) / 2 );	// 정수로 형변환
			
		    window.open(url, "emailFind", 
		    			"left=" + (pop_left-100) + ", top=" + (pop_top-100) + ", width=" + pop_width + ", height=" + pop_height);
			
		});// end of $("span#findMyEmail").click(function() { })
		
		
		// === 비밀번호 찾기 팝업창 이벤트 === //
		$("span#findMyPassword").click(function() {
			var url = "<%= request.getContextPath()%>/passwordFind.gw;"
				
			var pop_width = 500;
		    var pop_height = 550;
		    var pop_left = Math.ceil( (window.screen.width - pop_width) / 2 ); 		// 정수로 형변환
		    var pop_top  = Math.ceil( (window.screen.height - pop_height) / 2 );	// 정수로 형변환
			
		    window.open(url, "emailFind", 
		    			"left=" + (pop_left-100) + ", top=" + (pop_top-100) + ", width=" + pop_width + ", height=" + pop_height);
		});
		
	});// end of $(document).ready(function() {}

	
	// === Function Declaration === //
	function goLogin() {
		
		// 로그인 유효성 검사
		var email = $("input#email").val();
		var password = $("input#password").val();
		
		if(email.trim() == "") {
			alert("이메일을 입력하세요!!");
			$("input#email").val("");
			$("input#email").focus();
			return;	// 종료
		}
		
		if(password.trim() == "") {
			alert("비밀번호를 입력하세요!!");
			$("input#password").val("");
			$("input#password").focus();
			return;	// 종료
		}
		
		// === 이메일 저장 유무 체크 확인 === //
		if( $("input:checkbox[id=saveEmailCheckbox]").prop("checked") ) {
			var saveEmail = $("input#email").val();
			// alert(saveEmail);
			
			localStorage.setItem("saveEmail", saveEmail);
		}
		else {
			localStorage.removeItem("saveEmail", saveEmail);
		}
		
		
		var frm = document.loginFrm;
		frm.action = "<%= request.getContextPath()%>/loginEnd.gw";
		frm.method = "POST";
		frm.submit();
		
	}
	
</script>

<div class="container-fluid container-lg" style="margin: 50px auto; width: 80%;">
	<div class="row">
		<div class="col-2 col-lg-3"></div>
		<div class="col-8 col-lg-6">
			<span class="h2" style="font-weight: bold;">로그인</span>
		</div>
	</div>
	<br>
	<form name="loginFrm">
	    <div class="form-row">
	    	<div class="col-2 col-lg-3"></div>
		    <div class="col-8 col-lg-6">
		       <label>이메일</label>
		       <input type="text" class="form-control mb-1" id="email" placeholder="Enter email" name="email">
		       <span class="error" style="color: red;">이메일을 입력하세요</span>
		    </div>
		    <div class="w-100 my-3"></div>
		    <div class="col-2 col-lg-3"></div>
	        <div class="col-8 col-lg-6">
	      	   <label>비밀번호</label>
	           <input type="password" class="form-control mb-1" id="password" placeholder="Enter password" name="password">
	           <span class="error" style="color: red;">비밀번호를 입력하세요</span>
	        </div>
	        <div class="w-100 my-3"></div>
	        <div class="col-2 col-lg-3"></div>
	    </div>
	    <div class="row justify-content-center">
		    <div class="col-8 col-lg-6 offset-lg-3">
		       <input id="saveEmailCheckbox" type="checkbox" />
		       <label id="saveEmail" for="saveEmailCheckbox" class="mr-4" style="font-size: 10pt;">이메일 저장</label>
		       <span class="findMyInfo mr-1" id="findMyEmail" style="font-size: 10pt;">이메일</span>&middot;
		       <span class="findMyInfo" id="findMyPassword" style="font-size: 10pt;">비밀번호 찾기</span>
		    </div>
	    </div>
	    <div class="row justify-content-center mt-1">
		    <div class="col-11 col-lg-5">
		       <button id="loginBtn" class="btn btn-primary btn-lg" style="display:flex; margin: auto; width: 70%; justify-content: center;">로그인</button>
		    </div>
	    </div>
    </form>
</div>
