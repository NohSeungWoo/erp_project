<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %> 

<script type="text/javascript">

	$(document).ready(function(){
		
		
		// 쓰기버튼
		$("button#btnWrite").click(function(){
			
			
			// 폼(form)을 전송(submit)
			var frm = document.addFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/WorkEnd.gw";
			frm.submit();
		});
		
	});// end of $(document).ready(function(){})--------------------------

</script>   

<div style="display: flex;">
<div style="margin: auto; padding-left: 3%;">
  <%-- 
    <h2 style="margin-bottom: 30px;">글쓰기</h2> 
  --%>
      <h2 style="margin-bottom: 30px;">근무 관리</h2>
	
<%-- <form name="addFrm"> --%>
<%-- === #149. 파일첨부하기 ===
                  먼저 위의 <form name="addFrm"> 을 주석처리 한 이후에 아래와 같이 해야 한다.
         enctype="multipart/form-data" 를 해주어야만 파일첨부가 되어진다.         
 --%>
	<form name="addFrm" enctype="multipart/form-data">
		<table style="width: 1024px" class="table table-bordered">
			<tr>
				<th style="width: 15%; background-color: #DDDDDD">신청자</th>
				<td>
					<input type="hidden" name="name" id="name" value="${sessionScope.loginuser.name}"  />
					<label for="a" >본인(${sessionScope.loginuser.name }) </label> 
				</td>
			</tr>
			<tr>
				<th style="width: 15%; background-color: #DDDDDD">날짜</th>
				<td>
					<input type="text" name="work_day" id="work_day" placeholder="ex)2021-11-28"/>
				</td>
			</tr>
			<tr>
				<th style="width: 15%; background-color: #DDDDDD; vertical-align: middle;">근무시간</th>
				<td>
					<input type="text" name="work_time" id="work_time" placeholder="ex)09:00~18:00"></input> 
				</td>
			</tr>
			
			<tr>
				<th style="width: 15%; background-color: #DDDDDD">총 근무 시간</th>
				<td>
					<input type="text" name="todaycount" id="todaycount" placeholder="ex)8시간"/> 
				</td>
			</tr>
		</table>
		
		<div style="margin: 20px;">
			<button type="button" class="btn btn-secondary btn-sm mr-3" id="btnWrite">글쓰기</button>
			<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:history.back()">취소</button> 
		</div>
	</form>

</div>	
</div>

    