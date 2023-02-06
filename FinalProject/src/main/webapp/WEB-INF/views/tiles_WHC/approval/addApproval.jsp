<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style type="text/css">
	ul.departments {
		list-style: none;
		padding-left: 0;
	}
	
	.selectEmp {
		padding-left: 50px;
	}
	.selectEmp:hover {
		background-color: lightblue;
		cursor: pointer;
	}
	
	.selectedEmp {
		background-color: lightblue;
	}
</style>
<script type="text/javascript">
	
	$(document).ready(function(){
		
		// 모든직원목록 받아오기
		getEmployeeList("");
		
		<%-- === 스마트 에디터 구현 시작 === --%>
        //전역변수
        var obj = [];
        
        //스마트에디터 프레임생성
        nhn.husky.EZCreator.createInIFrame({
            oAppRef: obj,
            elPlaceHolder: "content",
            sSkinURI: "<%= request.getContextPath() %>/resources/smarteditor/SmartEditor2Skin.html",
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
		// 결재선 설정 모달창에서 직원을 클릭 했을때
		$(document).on("click", ".selectEmp", function(){
			$(".selectEmp").removeClass("selectedEmp");
			$(this).addClass("selectedEmp");
		});
		
		// 결재선 설정 모달창에서 결재 협조 수신 버튼을 클릭했을때 
		$(document).on("click", ".approvalTypeBtn", function(){
			
			var selectedEmpSeq = $("li.selectedEmp > input[name=employeeid]").val();
			var selectedEmpDP = $("li.selectedEmp > input[name=departmentname]").val();
			var selectedEmpName = $("li.selectedEmp > span.name").text();
			var selectedEmpPO = $("li.selectedEmp > input[name=positionname]").val();
			var loginID = ${requestScope.loginMap.employeeid};
			
			if( selectedEmpSeq == null ) {
				alert("직원을 선택하세요!");
			}
			else if(selectedEmpSeq == loginID ) {
				alert("기안자는 결재, 협조, 수신자에 포함될 수 없습니다!");
			}
			else {
				
				var flag = false;
				
				$("tbody.approvalPerson > tr.showedEmp > td.empSeq").each(function(index,item){
					if ($(item).text() == selectedEmpSeq) {
						flag = true;
						alert("결재선 내 중복 설정은 불가능 합니다!");
					}
				});
				
				if(!flag){
				
					var $target = $(event.target);
					var i = $("button.approvalTypeBtn").index($target);
					
					var html = "<tr class='emps notsaved showedEmp'>" +
									"<td class='empDP'>"+selectedEmpDP+"</td>" +
									"<td class='empName'>"+selectedEmpName+"</td>" +
									"<td class='empPO'>"+selectedEmpPO+"</td>" +
									"<td class='deleteEmp'><button class='deleteEmpBtn btn btn-light btn-sm'>x</button></td>" +
									"<td class='empSeq' style='display:none'>"+selectedEmpSeq+"</td>" +
							   "</tr>";
					
					$("tbody.approvalPerson:eq("+i+")").append(html);
				}
			}
		});// end of $(".approvalTypeBtn").click(function(){}----------------
				
		// 결재선 설정 모달창에서 선택한 직원 취소하기
		$(document).on("click", "button.deleteEmpBtn", function(){
			$(this).parent().parent().hide();	// 삭제를 선택한 버튼의 tr 태그를 숨김
			$(this).parent().parent().addClass("deleteNotSaved"); // 숨긴태그가 아직 저장되지않았음을 표시 하는 클래스 추가
			$(this).parent().parent().removeClass("showedEmp"); // 보여주고 있는 직원 표시인 showedEmp 태그 삭제
		});	
				
		// 결재선 설정 모달창에서 닫기, 취소 버튼을 클릭했을때 
		$("button.cancle").click(function(){
			$(".selectEmp").removeClass("selectedEmp"); // 모달창에서 좌측 선택된 직원에 css 효과 주는 클래스 삭제
			$("tr.notsaved").remove();	// 저장되지 않고 보여주기만 한 직원들 다시 삭제
			$("tr.deleteNotSaved").show(); // 삭제 버튼을 눌렀던 직원들 다시 보여주기
			$("tr.emps").addClass("showedEmp"); // 보여주고 있는 직원 표시인 showedEmp 태그 추가
			$("tr.emps").removeClass("deleteNotSaved"); // 삭제버튼을 눌러서 숨겨두었다는 표시를 다시 제거 
		});	
		
		// 결재선 설정 모달창에서 적용 버튼을 클릭했을때 
		$(document).on("click", "button.applyBtn", function(){
			
			// 결재에 인원이 있는지 확인
			var isExist = false;
			$("tbody.approvalEmp tr.showedEmp").each(function(index,item){
				isExist = true; // 삭제버튼을 누르지 않은 보여지고 있는 직원 (showedEmp) 이 있다면 true;
				return false;
			});
			
			if(!isExist){ // 결재자가 아무도 없다면
				alert("최소 1명의 결재자를 설정해주세요.");
			}
			else { // 결재자를 선택했다면
				
				// 모달창에서 좌측 선택된 직원에 css 효과 주는 클래스 삭제
				$(".selectEmp").removeClass("selectedEmp");
			
				// 삭제버튼을 누른 직원들 제거
				$("tr.deleteNotSaved").remove();
			
				// 결재선을 새로 설정한 경우 기존에 입력한 내용물 제거
				$(".addedApprovalEmps").remove();
				
				// 선택된 결재자, 협조자, 수신자에 맞게 넣어주기
				$(".approvalPerson").each(function(index,item){
					
					var html = "";
					var employeeID = "";	// DB에 넣을 직원 아이디
					
					// 삭제버튼을 누르지 않고 보여지고 있는 직원들만
					$(item).children(".showedEmp").each(function(i,t){
						
						// 적용전 이라는 표시인 notsaved 클래스 제거해서 저장되었음
						$(t).removeClass("notsaved");
						
						html += "<div class='col-md-2 mb-2 addedApprovalEmps'>" +
									"<div class='card'>";
									if(index == 0){ // 결재인 경우
								    	html += "<div class='card-header text-center py-1'>결재</div>"; 
									}
							  	html += "<div class='card-body text-center py-1'>" +
								    		"<p class='card-text'>"+$(t).children(".empName").text()+"</p>" +
								    	"</div>" +
								    	"<div class='text-center'><small>"+$(t).children(".empPO").text()+"<br>"+$(t).children(".empDP").text()+"</small></div>" +
					    			"</div>" +
					    		"</div>";
					    
					    if(i==0){
						  	employeeID += $(t).children(".empSeq").text();
					    }
					    else {
					    	employeeID += ","+$(t).children(".empSeq").text();
					    }
					  	
					});
					
					if(index==0) { // 결재인 경우
						$("div.selemps:eq("+index+")").append(html);
						$("input[name=apprEmp]").val(employeeID);
					}
					else if(index==1) { // 협조인 경우
						$("div.selemps:eq("+index+")").html(html);
						$("input[name=coopEmp]").val(employeeID);
					}
					else if(index==2) { // 수신인 경우
						$("div.selemps:eq("+index+")").html(html);
						$("input[name=reciEmp]").val(employeeID);
					}
					
				});
				
	    		// 모달창을 닫아준다.
				$("button.hiddenBtn").trigger("click");
			}
		});	
		
		// 결재선 설정 모달에서 검색어 입력시 해당하는 직원들 보여주기
		// 검색어를 빠르게 입력시 해당하는 직원이 두번씩 출력되는 오류가 있어서 keyup 이벤트에 delay를 줌
		var timeout = null;

	    $('input#searchWord').keyup(function () {        
	        clearTimeout(timeout);

	        timeout = setTimeout(function () {

	        	var wordLength = $('input#searchWord').val().trim().length;
				// 검색어의 길이를 알아온다.
							
				$("ul.departments").empty();
				if(wordLength == 0) {
					getEmployeeList("");
					// 검색어가 공백이거나 검색어 입력후 백스페이스키를 눌러서 검색어를 모두 지우면 모든직원이 보이게 한다.
				}
				else {
					getEmployeeList($("input#searchWord").val());
					
				}	
				$("ul.departments").addClass("show");
	            
	        }, 200);
	    });
		// end of $('input#searchWord').keyup(function () {})--------------------
		
		// 상신하기 버튼 클릭시
		$("button#btnWrite").click(function(){
			
			
			<%-- === 스마트 에디터 구현 시작 === --%>
          	//id가 content인 textarea에 에디터에서 대입
           	obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
	        <%-- === 스마트 에디터 구현 끝 === --%>	
	        
	        ////////////////////////////////////////////////////////////////
	        // 유효성 검사  //
	        
			// 글제목 유효성 검사
			var subjectVal = $("input#subject").val().trim();
			if(subjectVal == "") {
				alert("글제목을 입력하세요!!");
				return;
			}
			
			// 결재자를 추가 했는지 검사
			var apprEmpVal = $("input[name=apprEmp]").val().trim();
			if(apprEmpVal == "") {
				alert("결재자는 필수입력사항입니다!!");
				return;
			}
			
			// 회의록인 경우
			if("${requestScope.apcano}" == 50) {
				
				var sdate = $("input[name=sdate]").val();
				var edate = $("input[name=edate]").val();
				
				var stime = $("input[name=stime]").val();
				var etime = $("input[name=etime]").val();
			//	console.log(sdate);
				
				if(!sdate || !edate) { // 회의일을 입력하지 않은경우
					alert("회의 날짜는 필수입력사항입니다!!");
					return;
				}
				else {
					if( Number(sdate.replace(/\-/g,'')) > Number(edate.replace(/\-/g,'')) ){
						alert("종료일은 시작일보다 빠른 날짜로 설정할 수 없습니다!!");
						return;
					}
					else if( Number(sdate.replace(/\-/g,'')) == Number(edate.replace(/\-/g,'')) && Number(stime.replace(":","")) > Number(etime.replace(":","")) ){
						alert("종료 시간은 시작시간보다 빠른 시간으로 설정할 수 없습니다!!");
						return;
					}
				}
				
				
				var purposeVal = $("textarea[name=purpose]").val().trim();
				
				if(purposeVal == "") {
					alert("회의목적을 입력하세요!!");
					return;
				}
				
			}
			
			// 업무보고인 경우
			if("${requestScope.apcano}" == 51) {
				
				var sdate = $("input[name=tsdate]").val();
				var edate = $("input[name=tedate]").val();
				
				var stime = $("input[name=tstime]").val();
				var etime = $("input[name=tetime]").val();
			//	console.log(sdate);
				
				if(!sdate || !edate) { // 회의일을 입력하지 않은경우
					alert("업무기간은 필수입력사항입니다!!");
					return;
				}
				else {
					if( Number(sdate.replace(/\-/g,'')) > Number(edate.replace(/\-/g,'')) ){
						alert("종료일은 시작일보다 빠른 날짜로 설정할 수 없습니다!!");
						return;
					}
					else if( Number(sdate.replace(/\-/g,'')) == Number(edate.replace(/\-/g,'')) && Number(stime.replace(":","")) > Number(etime.replace(":","")) ){
						alert("종료 시간은 시작시간보다 빠른 시간으로 설정할 수 없습니다!!");
						return;
					}
				}
				
				
				var issueVal = $("textarea[name=issue]").val().trim();
				
				if(issueVal == "") {
					alert("이슈를 입력하세요!!");
					return;
				}
				
			}
			
			// 지출결의서인 경우
			if("${requestScope.apcano}" == 53) {
				
				var spdate = $("input[name=spdate]").val();
				
				
				if(!spdate) { // 회의일을 입력하지 않은경우
					alert("지출예정일은 필수입력사항입니다!!");
					return;
				}
				
				var amountVal = $("input[name=amount]").val().trim();
				
				if(amountVal == "") {
					alert("금액을 입력하세요!!");
					return;
				}
			}
			
			<%-- === 스마트에디터 유효성 검사 시작 === --%>
	        //스마트에디터 사용시 무의미하게 생기는 p태그 제거
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
	      	<%-- === 스마트에디터 유효성 검사 끝 === --%>
			////////////////////////////////////////////////////////////////
			
			var go = confirm("기안을 보내시겠습니까?");
			
			if(go == true) {
				
				var frm = document.addApprFrm;
				
				if(frm.fk_apcano.value == 50){
					frm.midate.value = frm.sdate.value +" "+ frm.stime.value +" ~ "+ frm.edate.value +" "+ frm.etime.value
				}
				else if (frm.fk_apcano.value == 51){
					frm.taskdate.value = frm.tsdate.value +" "+ frm.tstime.value +" ~ "+ frm.tedate.value +" "+ frm.tetime.value
				}			
				
				frm.method = "POST";
				frm.action="<%= ctxPath%>/addApprovalEnd.gw"; 
				frm.submit();
				
			} 
			
		});
		
	});
	
	// Function Declaration
	// 검색어가 있는 직원 목록 받아오기 
	function getEmployeeList(searchWord) {
		
		if(searchWord == null) {
			searchWord = "";
		}
		
		$.ajax({
			url:"<%= request.getContextPath()%>/approval/getEmployeeList.gw",
			data:{"searchWord":searchWord},
			type:"GET",
			dataType:"JSON",
			success:function(json){ 
				if( json.length > 0) {
					// 데이터가 존재하는 경우
					
					$("a.deptn").hide();
					
					$.each(json,function(index, item){
						var html = "";	
						html += "<li class='selectEmp'>" +
					            	"<span class='name'>"+item.name+"</span>" + "<span>&nbsp;(" + item.positionname + ")</span>" +
					                "<input type='hidden' name='employeeid' value='"+item.employeeid+"' />" +
					                "<input type='hidden' name='departmentname' value='"+item.departmentname+"' />" +
					                "<input type='hidden' name='positionname' value='"+item.positionname+"' />" +
					            "</li>";
						
			           	$('ul#department'+item.departno).append(html);
			           	$("a#"+item.departno).show();
					}); // end of $.each(json,function(index, item){})---------
				}
				else {
					$("a.deptn").hide();
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		 	}
		});
		
	}// end of function getEmployeeList() {}------------------------------
	
</script>
<div class="container-fluid">

	<%-- 선택한 기안종류에 맞게 --%>
	<h4 class="mb-3"><b>${requestScope.apcaname}</b></h4>
	<div class="mb-1" style="display: flex; border-bottom: 1px solid black;">
		<div style="font-weight: bold;">결재선</div>
		<button class="btn btn-secondary btn-sm" data-toggle="modal" data-target="#myModal" style="margin-left:auto;">결재선 설정</button>
	</div>
	
	<%-- 결재선 선택 모달 시작 --%>
	<!-- The Modal -->
    <div class="modal fade" id="myModal" data-backdrop="static">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
        
          <!-- Modal Header -->
          <div class="modal-header">
            <h4 class="modal-title">결제선 설정</h4>
            <button type="button" class="close cancle" data-dismiss="modal">&times;</button>
          </div>
          
          <!-- Modal body -->
          <div class="modal-body">
            <div class="row">
            	<div class="col-lg-5">
            		<div class="card shadow mb-4">
						<div class="card-header py-3">
							<input type="text" id="searchWord" placeholder="사원명 ,부서명,사원번호" style="width: 100%; padding-left:10px;" autocomplete="off">
						</div>
						<div class="card-body">
							<div class="list-group-flush">
								<c:forEach var="item" items="${requestScope.departList}">
									<a href="#department${item.departno}" id="${item.departno}" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle deptn">${item.departmentname}</a>
							  		<ul class="collapse departments show" id="department${item.departno}" >
							            
							        </ul>
								</c:forEach>
								<%--
								<a href="#department1" data-toggle="collapse" aria-expanded="false" class="list-group-item list-group-item-action dropdown-toggle">마케팅부</a>
							  		<ul class="collapse departments" id="department1" >
							            <li class="selectEmp">
							                	<span class="name">우희철</span>
							                	<input type="hidden" name="seq" value="211059005" />
							                	<input type="hidden" name="department" value="부서명" />
							                	<input type="hidden" name="position" value="직책" />
							            </li>
							            <li class="selectEmp">
							                	<span class="name">이순신</span>
							                	<input type="hidden" name="seq" value="211059004" />
							                	<input type="hidden" name="department" value="부서명" />
							                	<input type="hidden" name="position" value="직책" />
							            </li>
							            <li class="selectEmp">
							                	<span class="name">엄정화</span>
							                	<input type="hidden" name="seq" value="211059006" />
							                	<input type="hidden" name="department" value="부서명" />
							                	<input type="hidden" name="position" value="직책" />
							            </li>
							        </ul>
							     --%>    
							</div>
	                  	</div>
	                </div>  	
            	</div>
			            	
            	<div class="col-lg-2" style="display:flex; align-items: center; justify-content: center;">
            		<div class="btn-group-vertical approvalTypeBtns">
					  <button type="button" class="approvalTypeBtn btn btn-light mb-2">결재</button>
					  <button type="button" class="approvalTypeBtn btn btn-light mb-2">협조</button>
					  <button type="button" class="approvalTypeBtn btn btn-light mb-2">수신</button>
					</div>
            	</div>
            	
            	<div class="col-lg-5">
	            	<div class="card shadow mb-4">
						<div class="card-header py-3">
							<h6 class="m-0 font-weight-bold">결재자 선택</h6>
						</div>
						<div class="card-body">
							<table class="table">
								<thead>
									<tr>
										<th colspan="4">결재</th>
									</tr>
								</thead>
								<tbody class="approvalPerson approvalEmp">
								</tbody>
							</table>
							<table class="table">
								<thead>
									<tr>
										<th colspan="4">협조</th>
									</tr>
								</thead>
								<tbody class="approvalPerson">
									
								</tbody>
							</table>
							<table class="table">
								<thead>
									<tr>
										<th colspan="4">수신</th>
									</tr>
								</thead>
								<tbody class="approvalPerson">
									
								</tbody>
							</table>
						</div>
					</div>
            	</div>
            </div>
          </div>
          
          <!-- Modal footer -->
          <div class="modal-footer">
            <button type="button" class="cancle btn btn-light" data-dismiss="modal">취소</button>
            <button type="button" class="applyBtn btn btn-primary">적용</button>
            <button type="button" class="hiddenBtn btn btn-light" data-dismiss="modal" style="display:none;"></button>
          </div>
          
        </div>
      </div>
    </div>
    <%-- 결재선 선택 모달 끝 --%>
    
	<div class="row selemps appr">
		<div class="col-md-2 mb-2">
	  		<div class="card">
		    	<div class="card-header text-center py-1">기안</div>
		    	<div class="card-body text-center py-1">
		    		<p class="card-text">${requestScope.loginMap.name}</p>
		    	</div>
		    	<div class="text-center"><small>${requestScope.loginMap.positionname}<br>${requestScope.loginMap.departmentname}</small></div>
	    	</div>
	  	</div>
	</div>
	<div class="my-3">
		<div class="mb-1 pb-1" style="display: flex; border-bottom: 1px solid black;">
			<div style="font-weight: bold;">협조</div>
		</div>
		<div class="row selemps cone">
			
		</div>
	</div>
	<div class="mt-3 mb-4">
		<div class="mb-1 pb-1" style="display: flex; border-bottom: 1px solid black;">
			<div style="font-weight: bold;">수신</div>
		</div>
		<div class="row selemps receiver">
			
	  	</div>
	</div>
	<hr>
	<div class="my-3">
		<div class="mb-1 pb-1" style="display: flex; border-bottom: 1px solid black;">
			<div style="font-weight: bold;">기안내용</div>
		</div>
		<form name="addApprFrm" enctype="multipart/form-data">
		
			<input type="hidden" name="apprEmp" value="" />
			<input type="hidden" name="coopEmp" value="" />
			<input type="hidden" name="reciEmp" value="" />
			<input type="hidden" name="fk_apcano" value="${requestScope.apcano}" />
			<input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}" />
			
			<table class="table " style="width:100%; margin-bottom:5px;">
				<tbody class="border-bottom" style="background-color: #f7f7f7;" >
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">기안제목</th>
						<td>
							<input type="text" name="subject" id="subject" style="width: 60%" />
						</td>
					</tr>
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">파일첨부</th>
						<td>
							<input type="file" name="attach" />
						</td>
					</tr>
					<%-- 회의록 --%>
					<c:if test="${requestScope.apcano == 50}">
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">회의일시</th>
							<td>
								<input type="date" name="sdate" />
								<input type="time" name="stime"/>
								<div>~</div>
								<input type="date" name="edate" />
								<input type="time" name="etime"/>
								<input type="hidden" name="midate"/>
							</td>
						</tr>
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">회의목적</th>
							<td>
								<textarea style="width: 100%; height:80px; resize: none;" name="purpose"></textarea>
							</td>
						</tr>
					</c:if>
					
					<%-- 업무보고 --%>
					<c:if test="${requestScope.apcano == 51}">
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">업무기간</th>
							<td>
								<input type="date" name="tsdate" />
								<input type="time" name="tstime"/>
								<div>~</div>
								<input type="date" name="tedate" />
								<input type="time" name="tetime"/>
								<input type="hidden" name="taskdate"/>
							</td>
						</tr>
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">이슈</th>
							<td>
								<textarea style="width: 100%; height:80px; resize: none;" name="issue"></textarea>
							</td>
						</tr>
					</c:if>
					<%-- 지출결의서 --%>
					<c:if test="${requestScope.apcano == 53}">
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">지출예정일</th>
							<td>
								<input type="date" name="spdate"/>
							</td>
						</tr>
						<tr>
							<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">금액</th>
							<td>
								<input type="text" name="amount" id="amount" style="width: 40%" />원
							</td>
						</tr>
					</c:if>
					
				</tbody>
			</table>
			<div>
				<textarea style="width: 100%; height:612px;" name="content" id="content" ></textarea>
			</div>
			<div class="" style="text-align: center;">
				<button type="button" class="btn btn-secondary" onclick="javascript:history.back()">취소</button>
		        <button type="button" id="btnWrite" class="btn btn-primary">상신하기</button>
	        </div>
		</form>
	</div>	
</div>	