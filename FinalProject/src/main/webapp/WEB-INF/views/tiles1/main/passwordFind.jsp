<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>    

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 
  
<!-- Bootstrap CSS -->
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 

<!-- Font Awesome 5 Icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<title>Insert title here</title>


<!-- Optional JavaScript -->
<script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%= ctxPath%>/resources/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script>
<script type="text/javascript" src="<%= ctxPath%>/resources/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script> 

<%--  ===== 스피너를 사용하기 위해  jquery-ui 사용하기 ===== --%>
<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.css" /> 
<script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
  
<%-- *** ajax로 파일을 업로드할때 가장 널리 사용하는 방법 ==> ajaxForm *** --%>
<script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery.form.min.js"></script>

<script type="text/javascript">

	$(document).ready(function() {
		
		// === 인증번호 숨기기 === //
		$("div#passwordFind").hide();
		
		// === 스피너 숨기기 === //
		$("div#spinner").hide();
		
		// === 새 비밀번호 숨기기 === //
		$("div#newPassword").hide();
		
		// === 취소 버튼 클릭시 팝업창 닫기 === //
		$("span#closeBtn").click(function() {
			window.close();
		});
		
		// === 확인 버튼 클릭시 이벤트 === //
		$("span#findBtn").click(function() {
			var email = $("input#email").val().trim();
			var name = $("input#name").val().trim();
			
			if(email == "") {
				alert("이메일을 입력하세요");
				return;
			}
			
			if(name == "") {
				alert("이름을 입력하세요");
				return;
			}
			
			// === 스피너 보이기 === //
			$("div#spinner").show();
			
			$.ajax({
				url:"<%= ctxPath%>/sendCodeEmail.gw",
				type:"POST",
				data:{"email":email,
					  "name":name},
				dataType:"JSON",
				success:function(json) {
					if(json.sendEmail) {
						alert("인증메일이 발송되었습니다.");
						$("div#beforeFind").hide();
						$("div#spinner").hide();
						$("div#newPassword").hide();
						
						$("div#passwordFind").show();
					}
					else {
						alert("해당하는 사원은 존재하지않습니다.");
						location.reload();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        	}
			});
		});// end of $("span#findBtn").click(function() { })
		
		
		// === 인증번호 확인 버튼 클릭 이벤트 === //
		$("span#btnCode").click(function() {
			
			var codeNo = $("input#codeNo").val().trim();
			
			if(codeNo == "") {
				alert("인증번호를 입력하세요!");
				return;
			}
			
			$.ajax({
				url:"<%= ctxPath%>/checkCode.gw",
				type:"POST",
				data:{"codeNo":codeNo},
				dataType:"JSON",
				success:function(json) {
					if(json.isSuccess) {
						alert(json.message);
						$("div#beforeFind").hide();
						$("div#spinner").hide();
						$("div#passwordFind").hide();
						
						$("div#newPassword").show();
					}
					else {
						alert(json.message);
						window.close();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        	}
			});
		});// end of $("span#btnCode").click(function() {})
		
		
		// === 새비밀번호 입력할 경우 이벤트 === //
		$("span#newPasswordBtn").click(function() {
			var newPassword = $("input#newPassword").val().trim();
			var email = $("input#email").val().trim();
			var name = $("input#name").val().trim();
			
			if(newPassword == "") {
				alert("비밀번호를 입력하세요.");
				return;
			}
			
			$.ajax({
				url:"<%= ctxPath%>/newPasswordUpdate.gw",
				type:"POST",
				data:{"newPassword":newPassword,
					  "email":email,
					  "name":name},
				dataType:"JSON",
				success:function(json) {
					if(json.isSuccess) {
						alert("비밀번호가 변경되었습니다.");
						window.close();
					}
					else {
						alert("비밀번호가 변경이 실패하였습니다. 다시 시도해주세요.");
						window.close();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        	}
			});
			
		});
		
	});// end of $(document).ready(function() {})
	
</script>

</head>
<body>
	<div class="row mt-4 mb-5">
		<div class="col-8 offset-1">
			<span class="h4" style="font-weight: bold;">비밀번호 찾기</span>
		</div>
	</div>
	
	<div id="beforeFind">
		<form name="emailFindFrm">
		    <div class="form-row">
		    	<div class="col-2 col-lg-3"></div>
			    <div class="col-8 col-lg-6">
			       <label>이메일</label>
			       <input type="text" class="form-control required mb-1" id="email" placeholder="Enter Email" name="email">
			    </div>
			    <div class="w-100 my-3"></div>
			    <div class="col-2 col-lg-3"></div>
		        <div class="col-8 col-lg-6">
		      	   <label>이름</label>
		           <input type="text" class="form-control required mb-1" id="name" placeholder="Enter Name" name="name">
		        </div>
		        <div class="w-100 my-3"></div>
		    </div>
		    <div class="row justify-content-center my-2">
		    	<div id="spinner" class="spinner-border text-primary"></div>
			</div>
		    <div class="row justify-content-center mt-2">
			    <div class="col-6 offset-1">
			       <span id="findBtn" class="btn btn-info mr-3" style="float:left; width: 80px; justify-content: center;">확인</span>
			       <span id="closeBtn" class="btn btn-dark" style="float:left; width: 80px; justify-content: center;">취소</span>
			    </div>
		    </div>
		    
	    </form>
    </div>
    
    <div id="passwordFind" >
	    <div class="mt-5">
	       <div class="form-row">
		    	<div class="col-2 col-lg-3"></div>
			    <div class="col-8 col-lg-6">
			       <label>인증번호</label>
			       <input type="password" class="form-control required mb-1" id="codeNo" placeholder="Enter Code" name="codeNo">
			    </div>
		   </div>
	    </div>
	    <div class="row justify-content-center mt-4">
		    <div class="col-9 col-lg-5">
		       <span id="btnCode" class="btn btn-primary" style="display:flex; margin: auto; width: 30%; justify-content: center;">확인</span>
		    </div>
	    </div>
    </div>
    
    <div id="newPassword" >
	    <div class="mt-5">
	       <div class="form-row">
		    	<div class="col-2 col-lg-3"></div>
			    <div class="col-8 col-lg-6">
			       <label>새 비밀번호</label>
			       <input type="password" class="form-control mb-1" id="newPassword" placeholder="Enter Password" name="newPassword">
			    </div>
		   </div>
	    </div>
	    <div class="row justify-content-center mt-4">
		    <div class="col-9 col-lg-5">
		       <span id="newPasswordBtn" class="btn btn-primary" style="display:flex; margin: auto; width: 30%; justify-content: center;">확인</span>
		    </div>
	    </div>
    </div>
</body>
</html>