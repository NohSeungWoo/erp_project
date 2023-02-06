<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일정 추가</title>
  <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="//code.jquery.com/jquery-3.2.1.min.js"></script>
	<!-- datepicker -->
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<!-- custom -->
	<link href='<%= request.getContextPath()%>/resources/fullcalendar/schedule.css' rel='stylesheet' />
	<script src='<%= request.getContextPath()%>/resources/fullcalendar/schedule.js'></script>
	<script type="text/javascript">
		var seq = "<%=request.getParameter("seq")%>"; 
		console.log(seq);
		$.ajax({
			url : "/finalProject/getSchedule.gw?seq="+ seq,
			type : "GET",
			dataType : "json",
			contentType : "application/json; charset=UTF-8",
			success : function(data) {
				console.log(data);
				$("#subject").val(data[0].title);
				$("#startDate").val(data[0].start);
				$("#endDate").val(data[0].end)
				$("#memo").val(data[0].memo)
			}
		});
	</script>
</head>
<body>
<div class = "group" id = "popupGroup">	
	<div class = "group-head">
		<h1 class = "zTree-h1">일정 수정</h1>
	</div>
	<div class = "group-body">
	<form id = "scheduleData">
		<div class = "top">
			<input class = "subject" id = "subject" type = "text" name = "subject" placeholder="제목을 입력해주세요">
		</div>
		<div class = "domain">
			<h3 class = "zTree-h3"> 시작 </h3>
		</div>
		<div class = "domain">
			<input class = "date" id = "startDate" type = "text" name = "startDate" id = "startDate">
		</div>
		<div class = "domain">
			<h3 class = "zTree-h3"> 종료 </h3>
		</div>
		<div class = "domain">
			<input class = "date" id = "endDate" type = "text" name = "endDate" id = "endDate">
		</div>
		<div class = "domain">
			<h3 class = "zTree-h3"> 메모 </h3>
		</div>
		<div class = "domain">
			<textarea class = "memo" id = "memo" name = "memo" rows = "5" cols = "20" placeholder="100글자까지 입력 가능합니다"></textarea> 
		</div>
	</form>
		<button class = "ok-button" type= "button" onclick="click_ok();">수정</button>
		<button class = "ok-button" type= "button" onclick="click_ok();">삭제</button>
	</div>	
	
	
</div>
</body>
</html>