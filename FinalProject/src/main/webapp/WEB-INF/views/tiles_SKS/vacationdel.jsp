<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <% String ctxPath = request.getContextPath(); %> 

<script type="text/javascript">

	$(document).ready(function(){
		$("button#vacationdelete").click(function(){
			var frm = document.vacationdelFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/vacationdelEnd.gw";
			frm.submit();
		});
	});
</script>	
<div style="display: flex;">
<div style="margin: auto; padding-left: 3%;">

	<h2 style="margin-bottom: 30px;">휴가취소</h2>
	
	<form name="vacationdelFrm">
		<table style="width: 1024px" class="table table-bordered">
			<tr>
				<th style="width: 15%; background-color: #DDDDDD">이름</th>
				<td>
				    <input type="hidden" name="seq" value="${requestScope.seq}" />
					<input type="text" name="name" id="name" value="${vacvo.name}"/> 
				</td>
			</tr>
		</table>
		
		<div style="margin: 20px;">
			<button type="button" class="btn btn-secondary btn-sm mr-3" id="vacationdelete">휴가 취소 완료</button>
			<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:history.back()">뒤로 가기</button> 
		</div>
	</form>

</div>	
</div>