<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
<!DOCTYPE html>

<style type="text/css">
	
	div#empList {
		margin: 50px auto;
		width: 90%;
	}
	
	div.input-group {
	    width:100%;
	    text-align:center;
	    float: left;
  		display: inline-block;
	}
	
	div#empButton {
		float: right;
  		position: relative;
  		display: inline-block;
	}
	
	
	
</style>

<script type="text/javascript">

	$(document).ready(function() {
		// 자동 검색창 숨기기
		$("div#displayList").hide();
		
		// === 직원명 입력 시 자동글 완성하기 === //
		$("input#searchEmp").keyup(function() {
			
			// 검색어의 길이
			var wordLength = $(this).val().trim().length;
			
			if(wordLength == 0) {
				// 검색어가 공백이나 스페이스로 이루어질 경우
				$("div#displayList").hide();
			}
			else {
				$.ajax({
					url:"<%= ctxPath%>/adminListSearch.gw",
					type:"GET",
					data:{"searchEmployee":$("input#searchEmp").val()},
					dataType:"JSON",
					success:function(json) {
						if(json.length > 0) {
							var html = "";
							
							$.each(json, function(index, item) {
								var empName = item.name;
								var employeeid = item.employeeid;
								
								var index = empName.indexOf($("input#searchEmp").val());
							
								var len = $("input#searchEmp").val().length;
								
								var result = empName.substr(0, index) + "<span style='color:navy; font-weight:bold;'>" + empName.substr(index, len) + "</span>" + empName.substr(index + len);
							
								html += "<span style='cursor:pointer;' class='result'>" + result + "&nbsp;(" + employeeid + ")</span><br>";
							});
							
							// 검색창 크기 알아오기
							var input_width = $("input#searchEmp").css("width");
							
							$("div#displayList").css({"width": input_width});
							
							$("div#displayList").html(html);
							$("div#displayList").show();
						}
					},
					error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	                }
				});
			}
		});// end of $("input#searchEmp").keyup(function() { })
		
		
		// === 자동 검색창에 대한 내용 클릭할 경우 검색창에 대한 결과값 보여주기 === //
		$(document).on("click", ".result", function() {
			var search = $(this).text();
			$("input#searchEmp").val(search);
			$("div#displayList").hide();
		});// end of $(document).on("click", ".result", function() { })
		
		
		// === 관리자 추가 버튼 클릭 시 이벤트 === //
		$("button#addAdminBtn").click(function() {
			var empid = $("input#searchEmp").val().trim();
			
			if(empid == "") {
				alert("사원을 입력하세요");
				return;
			}
			
			var index = empid.indexOf("(");
			
			empid = empid.substr(index + 1, 9);
			
			var addOK = confirm("관리자 추가를 하시겠습니까?");
			
			if(addOK) {
				var frm = document.adminFrm;
				frm.adminEmpId.value = empid;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/adminAddEnd.gw";
				frm.submit();
			}
			else {
				alert("관리자 추가를 취소하였습니다.");
				return;
			}
			
		});// end of $("button#addAdminBtn").click(function() { })
		
		
		// === 쓰레기통 아이콘 클릭시 해당하는 관리자의 관리 권한 삭제 이벤트 === //
		$("i").click(function() {
			var employeeid = $(this).parent().parent().find("td.employeeid").text()

			var delAdmin = confirm("해당하는 관리자의 권한을 삭제하시겠습니까?");
			
			if(delAdmin) {
				var frm = document.adminFrm;
				frm.adminEmpId.value = employeeid;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/adminDelEnd.gw";
				frm.submit();
			}
			else {
				alert("관리자의 권한 삭제를 취소하였습니다.");
				return;
			}
		});
		
		
	});
	
</script>

<div class="container-fluid" id="empList">
  <div class="row mb-2 ml-2">
  	<span class="h3 font-weight-bold">관리자 목록</span>
  </div>
  <div class="row mt-1 input-group mb-3">
  	<div id="empButton" class="col-12 col-lg-4 mt-3 mr-2">
	  <button id="BtnExcelFile" data-toggle="modal" data-target="#myModal" class="btn btn-outline-dark float-right">관리자 추가</button>
  	</div>
  </div>
  
  <!-- The Modal -->
  <div class="mt-5 modal" id="myModal">
     <div class="modal-dialog">
      	<div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title h4">부서 이동</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        
        
        <div class="modal-body">
        	<label class="ml-4 pl-1 mt-5 mb-2 h6">사원명</label>
		    <input id="searchEmp" type="text" class="ml-4 mb-5 mt-1 form-control form-control-sm" placeholder="사원명 입력" style="width: 50%;" />
		    <div id="displayList" style="position:absolute; z-index:2; background-color: white; border: solid 1px #bfbfbf; width: 235px; height: 100px; margin-top:-47px; margin-left:24px; border-top:0px; padding-left: 9px; border-radius: 10px;"></div>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" id="addAdminBtn" class="btn btn-info btn-sm" data-dismiss="modal">확인</button>
          <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">닫기</button>
        </div>
        
        </div>
     </div>
   </div>  
  
  <div class="table-responsive" >
	  <table class="table col-12" style="width: 95%;">
	    <thead class="thead-dark">
	      <tr>
	        <th style="width: 11%;">이름</th>
	        <th style="width: 15%;">사번</th>
	        <th style="width: 12%;">부서</th>
	        <th style="width: 12%;">직급</th>
	        <th>이메일</th>
	        <th>연락처</th>
	        <th style="width: 4%;"></th>
	      </tr>
	    </thead>
	    <tbody>
	    <c:if test="${not empty requestScope.adminList}">
	    	<c:forEach var="map" items="${requestScope.adminList}">
	    		<tr class="empTr">
			        <td class="name">${map.name}</td>
			        <td class="employeeid">${map.employeeid}</td>
			        <td class="departmentname">${map.departmentname}</td>
			        <td class="positionname">${map.positionname}</td>
			        <td class="email">${map.email}</td>
			        <td class="mobile"><span>${map.mobile.substring(0, 3)}-${map.mobile.substring(3, 7)}-${map.mobile.substring(7)}</span></td>
			    	<td class="deleteAdmin"><i class="far fa-trash-alt" style="cursor: pointer;"></i></td>
			    </tr>
	    	</c:forEach>
	    </c:if>
	    </tbody>
	  </table>
  </div>
  
  <%-- === #122. 페이지바 보여주기 --%>
  <div align="center" style="width: 70%; border: solid 0px gray; margin: 20px auto;">
  	  ${requestScope.pageBar}
  </div>
  
  <form name="adminFrm">
  	<input type="hidden" id="adminEmpId" name="adminEmpId"/>
  </form>
</div>