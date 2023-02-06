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
		
		getBoardList();		// select에 부서정보 값 넘기기
		getPositionList();	// select에 직급정보 값 넘기기
		
		var departno = "${requestScope.map.departno}";
		var positionno = "${requestScope.map.positionno}";
		
		if(departno != "") {
			$("select#selectDepart").val(departno);
		}
		
		if(positionno != "") {
			$("select#selectPosition").val(positionno);
		}
		
		
		$("span#closeBtn").click(function() {
			self.close();
		});
		
		
		$("span#editBtn").click(function() {
			
			if($("input#name").val().trim() == "") {
				alert("이름을 입력하세요.");
				return;
			}
			
			var regExp = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/;; 
		    // 숫자 4자리만 들어오도록 검사해주는 정규표현식 객체 생성
		    
			var mobile = $("input#mobile").val();
		    
			var bool = regExp.test(mobile);
		    
			if(!bool) {
				alert("휴대폰 번호를 정확하게 입력하세요.");
				return;
			}
			
			if(!($("input#salary").val().trim() > 0)) {
				alert("급여를 숫자로 정확히 입력하세요.");
				return;
			}
			
			var frm = document.empDetailFrm;
			frm.method = "POST";
			frm.action = "<%= request.getContextPath()%>/empEditEnd.gw";
			frm.submit();
			
		});
		
		
		$("span#delBtn").click(function() {
			var delOK = confirm("해당 사원을 지우시겠습니까?");
			
			if(delOK) {
				var frm = document.empDetailFrm;
				frm.method = "POST";
				frm.action = "<%= request.getContextPath()%>/empDelEnd.gw";
				frm.submit();
			}
			else {
				alert("삭제를 취소하였습니다.");
				return;
			}
		});
		
	});
	
	
	// === Function Declaration === //
	// === 부서정보 가져오기 === //
	function getBoardList() {
		$.ajax({
			url:"<%= request.getContextPath()%>/getDepartmentName.gw",
			type:"GET",
			dataType:"JSON",
			async: false, // 동기처리
			success:function(json) {
				if(json.length > 0) {
					
					html = "";
					
					$.each(json, function(index, item) {
						var departmentname = item.depart;
						var departno = item.departno;
						
						html += "<option id='" + departno + "' name='department' value='" + departno + "'>" + departmentname + "</option>";
					});
					
					$("select#selectDepart").html(html);
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        	}
		});
	}
	
	// === 직급 정보 가져오기
	function getPositionList() {
		$.ajax({
			url:"<%= request.getContextPath()%>/getPositionName.gw",
			type:"GET",
			dataType:"JSON",
			async: false, // 동기처리
			success:function(json) {
				if(json.length > 0) {
					
					html = "";
					
					$.each(json, function(index, item) {
						var positionname = item.position;
						var positionNo = item.positionno;
						
						html += "<option id='" + positionNo + "' name='position' value='" + positionNo + "'>" + positionname + "</option>";
					});
					
					$("select#selectPosition").html(html);
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        	}
		});
	}
	
</script>

</head>
<body>
	<c:if test="${not empty requestScope.map}">
		<div class="container-fluid container-lg" style="margin: 50px auto; width: 90%;">
		<div class="row">
			<div class="col-8 col-lg-6 offset-0 offset-lg-2">
				<span class="h3" style="font-weight: bold;">직원 정보 수정</span>
			</div>
		</div>
		<br>
		<form name="empDetailFrm"> 
		    <div class="form-inline">
		    	<input type="hidden" id="employeeid" name="employeeid" value="${map.employeeid}"/>
		    	<div class="col-6 col-lg-8 offset-3 offset-lg-2 text-center mt-3">
		    		<c:if test="${not empty requestScope.map.profilename}">
		    			<img src="<%= request.getContextPath()%>/resources/empIMG/${map.profilename}" class="rounded-circle" style="width: 190px; height: 200px;"><br>
		    		</c:if>
		    		<c:if test="${empty requestScope.map.profilename}">
				    	<img src="<%= request.getContextPath()%>/resources/images/기본프로필_kh.jpg" class="rounded-circle" style="width: 190px; height: 200px;"><br>
		    		</c:if>
		    	</div>
		    	<div class="w-100 my-4"></div>
		    	
		    	<div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">이메일</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <input type="text" class="form-control mt-1" id="email" name="email" value="${map.email}" disabled style="width: 100%;">
			    </div>
			    <div class="w-100 my-4"></div>
			    
			    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">이름</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <input type="text" class="form-control mt-1" id="name" name="name" value="${map.name}" style="width: 100%;">
			    </div>
			    <div class="w-100 mb-5"></div>
			    
			    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">연락처</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <input type="text" class="form-control mt-1" id="mobile" name="mobile" value="${map.mobile}" style="width: 100%;">
			    </div>
			    <div class="w-100 mb-5"></div>
			    
			    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">근무부서</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <select id="selectDepart" name="selectDepart" class='selectpicker' data-width='auto' style='width: 100%; height: 35px; border-radius: 3px;'>
			       </select>
			    </div>
			    <div class="w-100 mb-5"></div>
			    
			    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">직책</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <select id="selectPosition" name="selectPosition" class='selectpicker' data-width='auto' style='width: 100%; height: 35px; border-radius: 3px;'>
			       </select>
			    </div>
			    <div class="w-100 mb-5"></div>
			    
			    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
			       <label class="float-right">급여</label>
		    	</div>
			    <div class="col-8 col-lg-4">
			       <input type="text" class="form-control mt-1" id="salary" name="salary" value="${map.salary}" style="width: 100%;">
			    </div>
			    <div class="w-100 mb-5"></div>
		    </div>
		 
		    <br>
		    <div class="row justify-content-center mt-2">
			    <div class="col-9 col-lg-8 offset-3 offset-lg-4">
			       <span id="editBtn" class="btn btn-info mr-3" style="float:left; width: 80px; justify-content: center;">수정</span>
			       <span id="delBtn" class="btn btn-danger mr-3" style="float:left; width: 80px; justify-content: center;">삭제</span>
			       <span id="closeBtn" class="btn btn-dark" style="float:left; width: 80px; justify-content: center;">취소</span>
			    
			    </div>
		    </div>
		    
		    </form> 
		    
	    
		</div>
	</c:if>
</body>
</html>