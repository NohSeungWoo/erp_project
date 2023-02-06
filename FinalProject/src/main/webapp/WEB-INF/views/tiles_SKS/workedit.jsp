<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <% String ctxPath = request.getContextPath(); %>
<script type="text/javascript">

	$(document).ready(function(){
		$("button#btnedit").click(function(){
			var frm = document.workeditFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/workeditEnd.gw";
			frm.submit();
		});
	});

</script>    
    
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">근무수정</h2>
		
		<form name="workeditFrm">
			<table style="width: 1024px;" class="table table-bordered">
				<tr>
					<th style="width: 15%; background-color: #DDD;">성명</th>
					<td>
						${requestScope.wpvo.name}
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD;">날짜</th>
					<td>
						<input type="hidden" name="seq" value="${requestScope.ctvo.seq}" />
						${requestScope.wpvo.work_day}
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">기본근무시간</th>
					<td>
						<input  name="work_time" id="work_time" value="${wpvo.work_time}"/>
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">출/퇴근 시간</th>
					<td>
						<input  name="todaycount" id="todaycount" value="${wpvo.todaycount}"/>
					</td>
				</tr>
			</table>
			
			<div style="margin: 20px;">
				<button type="button" class="btn btn-secondary btn-sm mr-3" id="btnedit">완료</button>
			</div>
		</form>
		
	</div>
</div>