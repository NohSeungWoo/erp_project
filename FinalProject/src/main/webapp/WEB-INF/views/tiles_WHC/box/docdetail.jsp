<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<% String ctxPath = request.getContextPath(); %>

<style type="text/css">
	.contentTbody th {
		background-color : #fafafa;
	}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		
		goReadOpinion();
		
		var authority = "${requestScope.authority}";
		
		if(authority == "a1"){
			
			$("button#agrBtn").click(function(){
				
				var apstatusLength = $("input:radio[name=apstatus]:checked").length;
				
				if(apstatusLength==0){
					alert("결재상태를 선택하세요!");
					return;
				}
				if($("textarea#opinion").val().trim() == ""){
					alert("결재의견을 작성하세요!");
					return;
				}
				
				var yn = confirm("결재 하시겠습니까?");
				
				if(yn == true){
					goAddOpinion();
				}
				
			});
			
		}
		
	});
	
	// Function Declaration
	function goReadOpinion() {
		
		$.ajax({
			url:"<%= ctxPath%>/readOpinion.gw",
			data:{"fk_apno":"${requestScope.approval.apno}"},
			dataType:"JSON",
			success:function(json){
				
				var html = "";
				
				if(json.length > 0) {
					
					$("div#opinionContent").show();
					
					$.each(json, function(index, item){
						html += "<tr>";
						html += "<td style='text-align:center;'>"+item.apprEmp+"</td>";
						if(item.apstatus == 1){
							html += "<td style='text-align:center; color:blue;'>승인</td>";
						}
						else {
							html += "<td style='text-align:center; color:red;'>반려</td>";
						}
						html += "<td>"+item.opinion+"</td>";
						html += "<td style='text-align:center;'>"+item.opdate+"</td>";
						html += "</tr>";
						
						var html2 = "";
						
						if(item.apstatus == 1){
							html2 += "<span style='color:blue;'>승인 " + item.opdate;
							
						}
						else {
							html2 += "<span style='color:red;'>반려 " + item.opdate;
						}
						html2 += "</span>";
						
						$("div#"+item.fk_employeeid).html(html2);
						
					});
				}
				else {
					$("div#opinionContent").hide();
				}
				
				$("tbody#opinionDisplay").html(html);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		 	}
		}); 
		
	}// end of function goReadOpinion() {}------------------------------
	
	function goAddOpinion() {
		
		var form_data = $("form[name=agrFrm]").serialize();
		// form 태그에 name 을 써줘야 사용할 수 있다.
		
		$("form[name=agrFrm]").ajaxForm({
			url:"<%= ctxPath%>/addOpinion.gw",
			data:form_data,
			type:"POST",
			dataType:"JSON",
			success:function(json){  //      {"n":1, "name":"이순신"}
			                         // 또는    {"n":0, "name":"이순신"}
			    var n = json.n; 
			    
			    if(n==1) {
			    	alert("결재의견 작성 완료");
			    	
					$("button.hiddenBtn").trigger("click");
			    	// 모달창을 닫아준다.
			    	
			    	$("button#moadlAgr").hide();
			    	
			    	goReadOpinion();  // 결재의견 보여주기
			    
			    }
			    
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		 	}
		});
		
		$("form[name=agrFrm]").submit();
		
	}
</script>

<div class="container-fluid">
	
	<div class="mb-2">
	<span style="font-size:15pt;">[${requestScope.apcaname}]</span>
		<c:if test="${requestScope.approval.apstatus == 0}">	
			<span style="font-size:13pt; font-weight: bold;">진행중</span>
		</c:if>
		<c:if test="${requestScope.approval.apstatus == 1}">	
			<span style="font-size:13pt; font-weight: bold; color:blue;">완료</span>
		</c:if>
		<c:if test="${requestScope.approval.apstatus == 2}">	
			<span style="font-size:13pt; font-weight: bold; color:red;">반려</span>
		</c:if>
	</div>	
	<h3 class="mb-4"><b>${requestScope.approval.subject}</b></h3>
	
	<div class="mb-2" style="display: flex; border-bottom: 1px solid black;">
		<div style="font-weight: bold;">결재선</div>
	</div>
    
	<div class="row selemps appr">
		<div class="col-md-2 mb-2">
	  		<div class="card">
		    	<div class="card-header text-center py-1">기안</div>
		    	<div class="card-body text-center py-1">
		    		<p class="card-text">${requestScope.sendEmp.name}</p>
		    	</div>
		    	<div class="text-center"><small>${requestScope.sendEmp.positionname}<br>${requestScope.sendEmp.departmentname}</small></div>
		    	<div class="mt-1" style="text-align:center;"><span>${requestScope.approval.apdate}</span></div>
	    	</div>
	  	</div>
	  	<c:forEach var="arrEmps" items="${requestScope.apprList}">
			<div class="col-md-2 mb-2">
		  		<div class="card">
			    	<div class="card-header text-center py-1">결재</div>
			    	<div class="card-body text-center py-1">
			    		<p class="card-text">${arrEmps.name}</p>
			    	</div>
			    	<div class="text-center"><small>${arrEmps.positionname}<br>${arrEmps.departmentname}</small></div>
			    	<div class="mt-1" id="${arrEmps.employeeid}" style="text-align:center;">-</div>
		    	</div>
		  	</div>
	  	</c:forEach>
	</div>
	<c:if test="${not empty requestScope.coopList}">
		<div class="my-3">
			<div class="mb-2 pb-1" style="display: flex; border-bottom: 1px solid black;">
				<div style="font-weight: bold;">협조</div>
			</div>
			<div class="row selemps cone">
				<c:forEach var="cooEmps" items="${requestScope.coopList}">
					<div class="col-md-2 mb-2">
				  		<div class="card">
					    	<div class="card-body text-center py-1">
					    		<p class="card-text">${cooEmps.name}</p>
					    	</div>
					    	<div class="text-center"><small>${cooEmps.positionname}<br>${cooEmps.departmentname}</small></div>
				    	</div>
				  	</div>
			  	</c:forEach>
			</div>
		</div>
	</c:if>
	<c:if test="${not empty requestScope.reciList}">
		<div class="mt-3 mb-4">
			<div class="mb-2 pb-1" style="display: flex; border-bottom: 1px solid black;">
				<div style="font-weight: bold;">수신</div>
			</div>
			<div class="row selemps receiver">
				<c:forEach var="recEmps" items="${requestScope.reciList}">
					<div class="col-md-2 mb-2">
				  		<div class="card">
					    	<div class="card-body text-center py-1">
					    		<p class="card-text">${recEmps.name}</p>
					    	</div>
					    	<div class="text-center"><small>${recEmps.positionname}<br>${recEmps.departmentname}</small></div>
				    	</div>
				  	</div>
			  	</c:forEach>
		  	</div>
		</div>
	</c:if>
	<hr>
	<div class="my-3">
		<div class="mb-1 pb-1" style="display: flex; border-bottom: 1px solid black;">
			<div style="font-weight: bold;">기안내용</div>
		</div>
		
		<table class="table " style="width:100%; margin-bottom:5px;">
			<tbody class="border-bottom contentTbody">
				<tr>
					<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">기안제목</th>
					<td>
						${requestScope.approval.subject}
					</td>
				</tr>
				<tr>
					<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">작성자</th>
					<td>
						${requestScope.sendEmp.departmentname}부&nbsp;${requestScope.sendEmp.positionname}&nbsp;${requestScope.sendEmp.name}
					</td>
				</tr>
				<tr>
					<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">파일첨부</th>
					<td>
						<a href="<%= request.getContextPath()%>/downloadApAttach.gw?apno=${requestScope.approval.apno}">${requestScope.approval.orgFilename}</a>
					</td>
				</tr>
				<%-- 회의록 --%>
				<c:if test="${requestScope.approval.fk_apcano == 50}">
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">회의일시</th>
						<td>
							${requestScope.cateApdetail.midate}
						</td>
					</tr>
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">회의목적</th>
						<td>
							<p style="width: 100%; min-height:50px; word-break: break-all;">${requestScope.cateApdetail.purpose}</p>
						</td>
					</tr>
				</c:if>
				
				<%-- 업무보고 --%>
				<c:if test="${requestScope.approval.fk_apcano == 51}">
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">업무기간</th>
						<td>
							${requestScope.cateApdetail.taskdate}
						</td>
					</tr>
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">이슈</th>
						<td>
							<p style="width: 100%; min-height:50px; word-break: break-all;">${requestScope.cateApdetail.issue}</p>
						</td>
					</tr>
				</c:if>
				<%-- 지출결의서 --%>
				<c:if test="${requestScope.approval.fk_apcano == 53}">
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">지출예정일</th>
						<td>
							${requestScope.cateApdetail.spdate}
						</td>
					</tr>
					<tr>
						<th class="border-right" style="width: 15%; vertical-align: middle; text-align: center;">금액</th>
						<td>
							<fmt:formatNumber value="${requestScope.cateApdetail.amount}" pattern="#,###" />원
						</td>
					</tr>
				</c:if>
				
			</tbody>
		</table>
		<div class="mb-3" style="border: 1px solid #dee2e6; padding:10px;">
			<p style="width: 100%; min-height:150px; word-break: break-all;">${requestScope.approval.content}</p>
		</div>
		
		<%-- 결재의견 --%>
		<div id="opinionContent">
			<div class="mb-1 pb-1" style="display: flex; border-bottom: 1px solid black;">
				<div style="font-weight: bold;">결재의견</div>
			</div>
			<table class="table table-bordered" style="margin-top: 2%; margin-bottom: 3%;">
				<thead>
				<tr style="background-color: #fafafa;">
				   <th style="width: 15%; text-align: center;">결재자</th>
				   <th style="width: 10%; text-align: center;">결재상태</th>
				   <th style="text-align: center;">결재의견</th>
				   <th style="width: 20%; text-align: center;">일시</th>
				</tr>
				</thead>
				<tbody id="opinionDisplay"></tbody>
			</table>
		</div>
		
		<div class="" style="text-align: center;">
			<input type="button" class="btn btn-secondary" value="목록으로" onclick="javascript:history.back();">
			<c:if test="${requestScope.authority == 'a1'}">
				<button class="btn btn-primary" data-toggle="modal" data-target="#myModal" id="moadlAgr">결재</button>
		        <div class="modal fade" id="myModal" data-backdrop="static">
			    	<div class="modal-dialog modal-dialog-centered">
				    	<div class="modal-content">
				        
				          	<!-- Modal Header -->
				          	<div class="modal-header">
				            	<h4 class="modal-title">결재하기</h4>
				            	<button type="button" class="close cancle" data-dismiss="modal">&times;</button>
				          	</div>
				          
				          	<!-- Modal body -->
				          	<div class="modal-body">
				          		<form name="agrFrm">
				          			<table class="table " style="width:100%; margin-bottom:5px;">
										<tbody class="border-bottom">
											<tr>
												<th class="border-right" style="width: 30%; vertical-align: middle; text-align: center;">결재상태</th>
												<td style="text-align: left; vertical-align: middle;">
													<div class="mt-2">
														<input type="radio" name="apstatus" value="1" id="yagr"><label class="ml-1 mr-3" for="yagr">승인</label>
														<input type="radio" name="apstatus" value="2" id="nagr"><label class="ml-1 "for="nagr">반려</label>
													</div>
												</td>
											</tr>
											<tr>
												<th class="border-right" style="width: 30%; vertical-align: middle; text-align: center;">결재의견</th>
												<td>
													<textarea style="width: 100%; height: 150px; resize: none;" id="opinion" name="opinion" placeholder="내용을 입력해주세요"></textarea>
												</td>
											</tr>
										</tbody>
									</table>
									<input type="hidden" name="fk_employeeid" value="${requestScope.login_userid}">	
									<input type="hidden" name="fk_apno" value="${requestScope.approval.apno}">	
									<input type="hidden" name="apprEmp" value="${requestScope.approval.apprEmp}" />
				          		</form>
				          	</div>
				          	<!-- Modal footer -->
					        <div class="modal-footer">
					            <button type="button" class="btn btn-light" data-dismiss="modal">취소</button>
					            <button type="button" class="applyBtn btn btn-primary" id="agrBtn" >결재</button>
					            <button type="button" class="hiddenBtn btn btn-light" data-dismiss="modal" style="display:none;"></button>
					        </div>
			          	</div>
		          	</div>
	          	</div>
			</c:if>
        </div>
	</div>	
</div>