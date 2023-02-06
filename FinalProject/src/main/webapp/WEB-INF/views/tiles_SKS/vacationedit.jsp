<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <% String ctxPath = request.getContextPath(); %>
<script type="text/javascript">

	$(document).ready(function(){
		$("button#btnedit").click(function(){
			var frm = document.vacationeditFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/vacationeditEnd.gw";
			frm.submit();
		});
	});

</script>    
    
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">휴가수정</h2>
		
		<form name="vacationeditFrm">
			<table style="width: 1024px;" class="table table-bordered">
				<tr>
					<th style="width: 15%; background-color: #DDD;">성명</th>
					<td>
						<input type="hidden" name="seq" value="${requestScope.vacvo.seq}" />
						${requestScope.vacvo.name}
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD;">연차 정보</th>
					<td>
						<input type="text" name="annual" id="annual"  value="${vacvo.annual}" />
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">종류</th>
					<td>
						<input  name="vacation" id="vacation" value="${vacvo.vacation}"/>
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">시작일</th>
					<td>
						<input  name="start_date" id="start_date" value="${vacvo.start_date}"/>
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">끝나는일</th>
					<td>
						<input  name="end_date" id="end_date" value="${vacvo.end_date}"/>
					</td>
				</tr>
			</table>
			
			<div style="margin: 20px;">
				<button type="button" class="btn btn-secondary btn-sm mr-3" id="btnedit">완료</button>
				<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:history.back()">뒤로가기</button>
			</div>
		</form>
		
	</div>
</div>