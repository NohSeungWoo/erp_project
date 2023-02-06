<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    <% String ctxPath = request.getContextPath(); %>
<script type="text/javascript">

	$(document).ready(function(){
		$("button#btnedit").click(function(){
			var frm = document.commuteeditFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/commuteeditEnd.gw";
			frm.submit();
		});
	});

</script>    
    
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
		
		<h2 style="margin-bottom: 30px;">근무수정</h2>
		
		<form name="commuteeditFrm">
			<table style="width: 1024px;" class="table table-bordered">
				<tr>
					<th style="width: 15%; background-color: #DDD;">성병</th>
					<td>
						${requestScope.ctvo.name}
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD;">날짜</th>
					<td>
						<input type="hidden" name="seq" value="${requestScope.ctvo.seq}" />
						${requestScope.ctvo.commute_day}
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">출/퇴근 여부</th>
					<td>
						<input  name="commute_status" id="commute_status" value="${ctvo.commute_status}"/>
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">출/퇴근 시간</th>
					<td>
						${requestScope.ctvo.commute_gtw}	
					</td>
				</tr>
				<tr>
					<th style="width: 15%; background-color: #DDD; vertical-align: middle;">확인자</th>
					<td>
						<input  name="commute_ok" id="commute_ok" value="${ctvo.commute_ok}"/>
					</td>
				</tr>
			</table>
			
			<div style="margin: 20px;">
				<button type="button" class="btn btn-secondary btn-sm mr-3" id="btnedit">완료</button>
			</div>
		</form>
		
	</div>
</div>