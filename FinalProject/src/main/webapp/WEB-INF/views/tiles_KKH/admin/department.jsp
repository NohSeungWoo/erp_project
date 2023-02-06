<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String ctxPath = request.getContextPath();
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	tr.department:hover {
		background-color: #cccccc;
	}
	
</style>

<script type="text/javascript">

	$(document).ready(function() {
		
		// 부서추가 할 경우 에러 표시 숨기기
		$("span.departError").hide();
		
		// 자동 검색창 숨기기
		$("div#displayList").hide();
		
		// 부서목록 가져오기
		getDepartmentName();
		
		// 부서 클릭했을 때 해당하는 부서의 이벤트 처리
		$(document).on("click", "tr.department", function() {
			// 부서번호 가져오기
			var department = $(this).text();
			
			location.href = "<%= ctxPath%>/admin/department.gw?department=" + department;
		});
		
		// 검색창 버튼 클릭했을 때 내용물 검색 이벤트 처리
		$("i#searchIcon").click(function() {
			searchEmpDepart();
		});
		
		// 검색창 검색어를 입력하고 엔터키를 눌렀을 때 검색 이벤트 처리
		$("input#searchEmp").keyup(function(event) {
			if(event.keyCode == 13) {
				searchEmpDepart();
			}
		});
		
		// === 부서명 입력후 포커스가 나갈 때 중복체크 여부 확인 이벤트 === //
		$("input#newDepartname").blur(function() {
			duplicateDepartmentname();
		});
		
		// === 관리자번호 입력후 포커스가 나갈 때 존재 여부 확인 이벤트 === //
		$("input#newDepartempID").blur(function() {
			isExistsEmpID();
		});
		
		// === 부서명 추가 버튼 클릭할 경우 === //
		$("button#departAddBtn").click(function() {
			var newDepartname = $("input#newDepartname").val();
			var newDepartempID = $("input#newDepartempID").val();
			
			if(newDepartname == "" || newDepartempID == "") {
				alert("입력할 값을 채우세요.");
			} 
			else {
				var frm = document.addDepartFrm;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/newDepartAddEnd.gw";
				frm.submit();
			}
			
		});// end of $("button#departAddBtn").click(function() {})
		
		
		// === 부서명 삭제 버튼 클릭할 경우 === //
		$("button#departDelBtn").click(function() {
			
			var delOK = confirm("해당 부서를 삭제하시겠습니까?");
			
			if(delOK) {
				var frm = document.delFrm;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/departDelEnd.gw";
				frm.submit();
			} 
			else {
				alert("삭제를 취소합니다.");
			}
			
		});
		
		// === 부서명 수정 버튼 클릭할 경우 === //
		$("button#departEditBtn").click(function() {
			var editOK = confirm("해당 부서명을 수정하시겠습니까?");
			
			if(editOK) {
				var frm = document.editFrm;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/departEditEnd.gw";
				frm.submit();
			} 
			else {
				alert("수정을 취소합니다.");
			}
		})// end of $("button#departEditBtn").click(function() { })
		
		
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
					url:"<%= ctxPath%>/employeeSearch.gw",
					type:"GET",
					data:{"searchEmployee":$("input#searchEmp").val()},
					dataType:"JSON",
					success:function(json) {
						if(json.length > 0) {
							var html = "";
							
							$.each(json, function(index, item) {
								var empName = item.empName;
								
								var index = empName.indexOf($("input#searchEmp").val());
							
								var len = $("input#searchEmp").val().length;
								
								var result = empName.substr(0, index) + "<span style='color:navy; font-weight:bold;'>" + empName.substr(index, len) + "</span>" + empName.substr(index + len);
							
								html += "<span style='cursor:pointer;' class='result'>" + result + "</span><br>";
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
		
		
		// === 다른 부서로 이동하기 버튼 클릭할 경우 부서 변경 이벤트 === //
		$("button#departChangeBtn").click(function() {
			var check_empId = [];
			var str_checkempID = "";
			var changeDepart = $("select#changeDepart").val()
			
			if($("input:checkbox[name=checkboxEmp]:checked").length == 0) {
				alert("사원을 선택하세요");
				return;
			}
			
			if($("input.checkboxEmp").is(":checked") == true) {
				$("input.checkboxEmp:checked").each(function() {
					var empid = $(this).val();
					check_empId.push(empid);
				});
				
				str_checkempID = check_empId.join(",");
				
				$.ajax({
					url:"<%= ctxPath%>/changeDepartment.gw",
					type:"POST",
					data:{"str_checkempID":str_checkempID,
						  "changeDepart":changeDepart},
					dataType:"JSON",
					success:function(json) {
						if(json.isSuccess) {
							alert("부서 이동이 성공하였습니다.");
							location.reload();
						}
						else {
							alert("부서 이동이 실패하였습니다.")
							location.reload();
						}
					},
					error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		            }
				});
			}
		});
			
	});// end of $(document).ready(function() {})

	
	// Function Declaration
	// === 부서목록 가져오기(Ajax) === //
	function getDepartmentName() {
		$.ajax({
			url:"<%= ctxPath%>/getDepartmentName.gw",
			type:"GET",
			dataType:"JSON",
			success:function(json) {
				if(json.length > 0) {
							
					var html = "<tr class='department' id=0>"+
									"<td style='border-top: none;'>전체</td>"+
					      		"</tr>";
					var modalhtml = "";
					
					var html2 = "";
					
					$.each(json, function(index, item) {
						var departmentname = item.depart;
						var departno = item.departno
							
						html += "<tr class='department' id=" + departno + ">"+
									"<td style='border-top: none;'>" + departmentname + "</td>"+
					      		"</tr>";
					      		
					    modalhtml += "<label class='ml-3' style='font-weight:bold;'>-&nbsp;" + departmentname + "</label><br/>";
						
					    html2 += "<option id='" + departno + "' name='department' value='" + departno + "'>" + departmentname + "</option>";
					    
					});
					
					$("tbody#departList").html(html);
					$("select#delDepart").html(html2);
					$("select#editDepart").html(html2);
					$("select#changeDepart").html(html2);
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});
	}// end of function getDepartmentName() {}
	
	// === 검색창에 직원명 검색하기 === //
	function searchEmpDepart() {
		var searchemp = $("input#searchEmp").val();
		location.href = "<%= ctxPath%>/admin/department.gw?searchEmp=" + searchemp;
	}
	
	// === 새로 추가할 부서명 중복여부 체크하기 === //
	function duplicateDepartmentname() {
		var newDepartname = $("input#newDepartname").val().trim();
		
		if(newDepartname == "") {
			alert("부서명을 입력하세요");
		}
		else {
			$.ajax({
				url:"<%= ctxPath%>/departDuplicate.gw",
				type:"GET",
				data:{"newDepart":newDepartname},
				dataType:"JSON",
				success:function(json) {
					if(json.isExists) {
						$("input#newDepartname").next().show();
						$("input#newDepartname").val("");
						$("input#newDepartname").focus();
					}
					else {
						$("input#newDepartname").next().hide();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			});
		}
	}
	
	// === 새로 추가할 부서명 관리자 사번 존재여부 체크하기 === //
	function isExistsEmpID() {
		var newDepartempID = $("input#newDepartempID").val().trim();
		
		if(newDepartempID == "") {
			alert("관리자 사원번호를 입력하세요");
		}
		else {
			$.ajax({
				url:"<%= ctxPath%>/isExistsEmpID.gw",
				type:"GET",
				data:{"newDepartempID":newDepartempID},
				dataType:"JSON",
				success:function(json) {
					if(!json.isExists) {
						$("input#newDepartempID").next().show();
						$("input#newDepartempID").val("");
						$("input#newDepartempID").focus();
					}
					else {
						$("input#newDepartempID").next().hide();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	            }
			});
		}
	}
	
</script>

<div class="container-fluid  container-xl" id="groupList">
  <div class="mb-3">
  	<span class="h4 font-weight-bold">부서 관리</span>
  </div>
  <div class="mt-2 mb-3" style="border: solid 1px gray; width: 25%; float: left; border-radius: 5px;">
  	<table class="table">
  		<thead class="thead-light">
	      <tr>
	        <th>부서명</th>
	      </tr>
	    </thead>
	    <tbody id="departList" style="font-size: 11pt; cursor: pointer;">
	    </tbody>
	      <tr>
	        <td>
	        	<div style="margin-top: 10px;">
		        	<a id="departAdd" data-toggle="modal" data-target="#myModalAdd" class="mr-2"><i class="fas fa-plus fa-lg"></i></a>
	        		  <!-- The Modal -->
					  <div class="mt-5 modal" id="myModalAdd">
					    <div class="modal-dialog">
					      <div class="modal-content">
					      
					        <!-- Modal Header -->
					        <div class="modal-header">
					          <h4 class="modal-title h4">부서추가</h4>
					          <button type="button" class="close" data-dismiss="modal">&times;</button>
					        </div>
					        
					        
					        <div class="modal-body">
						        <form name=addDepartFrm>
						        	<label class="ml-3" for="newDepartname">부서명</label>
						        	<input class="ml-2 form-control" id="newDepartname" name="newDepartname" type="text" placeholder="새로 추가할 부서" style="width: 50%;"/>
						        	<span class="departError ml-3" style="color:red;">이미 존재하는 부서입니다.</span>
						        	<br/>
						        	<label class="ml-3 mt-2" for="newDepartempID">관리자 사원번호</label>
						        	<input id="newDepartempID" name="newDepartempID" type="text" class='ml-2 form-control' placeholder="관리자 사번" style="width: 50%;"/>
						        	<span class="departError ml-3" style="color:red;">해당하는 사원번호는 존재하지 않습니다.</span>
						        </form>
					        </div>
					        
					        <!-- Modal footer -->
					        <div class="modal-footer">
					          <button type="button" id="departAddBtn" class="btn btn-primary btn-sm" data-dismiss="modal">추가</button>
					          <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">닫기</button>
					        </div>
					        
					      </div>
					    </div>
					  </div>
					  
		        	<a id="departDel" data-toggle="modal" data-target="#myModalDel" class="mr-2"><i class="fas fa-trash fa-lg"></i></a>
		        		<!-- The Modal -->
					  <div class="mt-5 modal" id="myModalDel">
					    <div class="modal-dialog">
					      <div class="modal-content">
					      
					        <!-- Modal Header -->
					        <div class="modal-header">
					          <h4 class="modal-title h4">부서 삭제</h4>
					          <button type="button" class="close" data-dismiss="modal">&times;</button>
					        </div>
					        
					        
					        <div class="modal-body">
					        	<form name="delFrm">
					        		<label class="ml-4 mt-3 h6">부서</label><br>
							        <select id="delDepart" name="delDepart" class='selectpicker ml-4 mt-2 mb-4' data-width='auto' style='width: 25%; height: 35px; border-radius: 3px;'>
				       				</select>
					        	</form>
					        </div>
					        
					        <!-- Modal footer -->
					        <div class="modal-footer">
					          <button type="button" id="departDelBtn" class="btn btn-danger btn-sm" data-dismiss="modal">삭제</button>
					          <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">닫기</button>
					        </div>
					        
					      </div>
					    </div>
					  </div>
		        	
		        	<a id="departEdit" data-toggle="modal" data-target="#myModalEdit" class="mr-2"><i class="far fa-edit fa-lg"></i></a>
		        	  <!-- The Modal -->
					  <div class="mt-5 modal" id="myModalEdit">
					    <div class="modal-dialog">
					      <div class="modal-content">
					      
					        <!-- Modal Header -->
					        <div class="modal-header">
					          <h4 class="modal-title h4">부서명 변경</h4>
					          <button type="button" class="close" data-dismiss="modal">&times;</button>
					        </div>
					        
					        
					        <div class="modal-body">
					        	<form name="editFrm">
					        		<label class="ml-4 mt-3 h6">부서</label><br>
							        <select id="editDepart" name="editDepart" class='selectpicker ml-4 mt-2 mb-4' data-width='auto' style='width: 25%; height: 35px; border-radius: 3px;'>
				       				</select>
				       				<br/>
				       				<label class="ml-4 mt-2" for="eidtDepartName">새로운 부서명</label>
						        	<input id="eidtDepartName" name="eidtDepartName" type="text" class='ml-3 form-control' placeholder="부서명" style="width: 50%;"/>
					        	</form>
					        </div>
					        
					        <!-- Modal footer -->
					        <div class="modal-footer">
					          <button type="button" id="departEditBtn" class="btn btn-info btn-sm" data-dismiss="modal">수정</button>
					          <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">닫기</button>
					        </div>
					        
					      </div>
					    </div>
					  </div>
	        	</div>
	        </td>
	      </tr>
  	</table>
  </div>
  <div class="mt-2 mb-3" style="border: solid 1px gray; width: 73%; float: right; border-radius: 5px; ">
  	<div class="row">
  		<div class="col-11 col-lg-4">
  		</div>
  	</div>
  	
  	<div class="table-responsive">
	  	<table class="table">
	  		<thead class="thead-light">
		      <tr>
		        <th colspan="5">
		        	<div class="float-left">
		        		1조파이널프로젝트&nbsp;<span style="font-weight: normal; font-size: 10pt;">&nbsp;총 <span style="font-weight: bold;">${requestScope.empCnt}</span>명</span>
		        	</div>
		        	<div class="float-right">
		        		<i id="searchIcon" class="fas fa-search pt-1 ml-2" style="cursor: pointer;"></i>
		        	</div>
		        	<div class="float-right">
		        		<input id="searchEmp" name="searchEmp" type="text" class="form-control rounded-pill" placeholder="직원 검색" style="height: 25px;">
		        	</div>
		        	<div id="displayList" style="position:absolute; z-index:2; background-color: white;  border: solid 1px #bfbfbf; width: 220px; height: 150px; margin-left: 540px; margin-top: 25px; border-top:0px; padding-left: 9px; border-radius: 10px;"></div>
		        </th>
		      </tr>
		    </thead>
	  		<tbody style="font-size: 11pt;">
	  		  <c:if test="${not empty requestScope.empDepartList}">
	  		  	<c:forEach var="map" items="${requestScope.empDepartList}">
			      <tr class="employeeList">
			      	<td style="width: 5%;"><input type="checkbox" name="checkboxEmp" class="checkboxEmp" value="${map.employeeid}" style="margin-left: 15px;"/></td>
			      	<td style="width: 15%">${map.employeeid}</td>
			        <td style="width: 12%;">${map.name}</td>
			        <td style="width: 15%;">${map.departmentname}</td>
			        <td style="width: 15%;">${map.positionname}</td>
			      </tr>
	  		  	</c:forEach>
	  		  </c:if>
		      <tr>
		        <td colspan="5">
					<button id="changeDepartBtn" data-toggle="modal" data-target="#myModalChange" class="btn btn-sm mt-2" style="background-color: #e6f9ff; color: #3377ff; font-weight: bold; font-size: 10pt;">다른 부서로 이동</button>
				</td>
		      </tr>
		    </tbody>
	  	</table>
		<%-- === #122. 페이지바 보여주기 --%>
		  <div align="center" style="width: 70%; border: solid 0px gray; margin: 20px auto;">
		  	  ${requestScope.pageBar}
		  </div>
		
		<!-- The Modal -->
	  	<div class="mt-5 modal" id="myModalChange">
	      <div class="modal-dialog">
	      	<div class="modal-content">
	      
	        <!-- Modal Header -->
	        <div class="modal-header">
	          <h4 class="modal-title h4">부서 이동</h4>
	          <button type="button" class="close" data-dismiss="modal">&times;</button>
	        </div>
	        
	        
	        <div class="modal-body">
	        	<label class="ml-4 mt-3 h6">부서</label><br>
			   <select id="changeDepart" name="changeDepart" class='selectpicker ml-4 mt-2 mb-4' data-width='auto' style='width: 40%; height: 35px; border-radius: 3px;'>
       		   </select>
	        </div>
	        
	        <!-- Modal footer -->
	        <div class="modal-footer">
	          <button type="button" id="departChangeBtn" class="btn btn-warning btn-sm" data-dismiss="modal">확인</button>
	          <button type="button" class="btn btn-dark btn-sm" data-dismiss="modal">닫기</button>
	        </div>
	        
	       </div>
	     </div>
	   </div>  
		  
  	</div>
  </div>
  <div>
  	<form name="departFrm">
  		<input type="hidden" id="departmentname" name="departmentname" />
  		<input type="hidden" id="managerid" name="managerid"/>
  	</form>
  </div>
</div>