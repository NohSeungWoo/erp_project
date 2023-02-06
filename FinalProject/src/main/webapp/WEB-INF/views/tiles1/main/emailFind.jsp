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
		
		// 초기화면에서 이메일 찾기 결과 숨기기
		$("div#afterFind").hide();
		$("div#noFind").hide();
		
		// 닫기 누르면 팝업창 닫기
		$("span#closeBtn").click(function() {
			window.close();
		}); // end of $("span#closeBtn").click(function() { })
		
		// === 찾기 버튼 클릭시 유효성 검사하고 넘겨주기 === //
		$("span#findBtn").click(function() {
			var employeeid = $("input#employeeid").val().trim();
			var name = $("input#name").val().trim();
			
			if(employeeid == "") {
				alert("사원번호를 입력하세요.");
				return;
			}
			
			if(name == "") {
				alert("이름을 입력하세요.");
				return;
			}
			
			$.ajax({
				url:"<%= ctxPath%>/emailFindEnd.gw",
				type:"POST",
				data:{"employeeid":employeeid,
					  "name":name},
				dataType:"JSON",
				success:function(json) {
					if(json.email != null) {
						var email = json.email;
						var index = email.indexOf("@");
						
						var change_email = email.substring(0, index - 2) + "**" + email.substring(index);	
						
						html = "<span>회원님의 이메일은 <span style='font-weight: bold;'>" + change_email + "</span>입니다.</span>";
						$("div#beforeFind").hide();
						$("div#noFind").hide();
						
						$("div#findResult").html(html);
						
						$("div#afterFind").show();
					}
					else {
						$("div#beforeFind").hide();
						$("div#afterFind").hide();
						$("div#noFind").show();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        	}
			})
			
			$(".closeBtn").click(function() {
				window.close();
			});
			
			
		});// end of $("span#findBtn").click(function() { })
		
		
	});// end of $(document).ready(function() { })

</script>

</head>
<body>
	<div class="row mt-4 mb-4">
		<div class="col-8 offset-1">
			<span class="h4" style="font-weight: bold;">이메일 찾기</span>
		</div>
	</div>
	
	<div id="beforeFind">
		<form name="emailFindFrm">
		    <div class="form-row">
		    	<div class="col-2 col-lg-3"></div>
			    <div class="col-8 col-lg-6">
			       <label>사원번호</label>
			       <input type="text" class="form-control required mb-1" id="employeeid" placeholder="Enter EmployeeID" name="employeeid">
			    </div>
			    <div class="w-100 my-3"></div>
			    <div class="col-2 col-lg-3"></div>
		        <div class="col-8 col-lg-6">
		      	   <label>이름</label>
		           <input type="text" class="form-control required mb-1" id="name" placeholder="Enter Name" name="name">
		        </div>
		        <div class="w-100 my-3"></div>
		        <div class="col-2 col-lg-3"></div>
		    </div>
		    <div class="row justify-content-center mt-2">
			    <div class="col-6 offset-1">
			       <span id="findBtn" class="btn btn-info mr-3" style="float:left; width: 80px; justify-content: center;">찾기</span>
			       <span id="closeBtn" class="btn btn-dark" style="float:left; width: 80px; justify-content: center;">취소</span>
			    </div>
		    </div>
	    </form>
    </div>
    
    <div id="afterFind">
    	<div id="findResult" class="row justify-content-center mt-5 mb-5 pt-4">
		       
	    </div>
	    <div class="col-6 offset-4">
		    <span id="closeBtn2" class="closeBtn btn btn-dark ml-3" style="float:left; width: 80px; justify-content: center;">닫기</span>
	    </div>
    </div>
    
    <div id="noFind">
    	<div class="row justify-content-center mt-5 mb-5 pt-4">
		     <span style="font-weight: bold;">조회된 이메일이 없습니다.</span>
	    </div>
	    <div class="col-6 offset-4">
		    <span id="closeBtn3" class="closeBtn btn btn-dark ml-3" style="float:left; width: 80px; justify-content: center;">닫기</span>
	    </div>
    </div>
</body>
</html>