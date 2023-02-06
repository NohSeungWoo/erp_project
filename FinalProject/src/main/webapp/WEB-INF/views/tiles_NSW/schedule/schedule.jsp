<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% String ctxPath = request.getContextPath(); %>

<div id='calendar'></div>

<link href='<%= request.getContextPath()%>/resources/fullcalendar/main.css' rel='stylesheet' />
<script src='<%= request.getContextPath()%>/resources/fullcalendar/main.js'></script>
<script>

//var scheduleData = JSON.stringify($('form#scheduleData').serializeObject());
	var events = [];
	$.ajax({
		url : "/finalProject/scheduleView.gw",
		type : "POST",
		dataType : "json",
		contentType : "application/json; charset=UTF-8",
		success : function(data) {
			console.log(data);
			events = data;
			  var calendarEl = document.getElementById('calendar');
				
			  var calendar = new FullCalendar.Calendar(calendarEl, {
			    headerToolbar: {
			      left: 'prev,next today',
			      center: 'title',
			      right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
			    },
			    navLinks: true, // can click day/week names to navigate views
			    businessHours: true, // display business hours
			    editable: true,
			    selectable: true,
			    locale:"ko",
			    events: events,
			   	droppable : false,
			   	eventClick:function(event) {
			   		console.log(event.event._def.extendedProps.seq);
			   		location.href = "/finalProject/scheduleEdit.gw?seq=" + event.event._def.extendedProps.seq;
	            }
			  });
			
			  calendar.render();
			
		}
	});
</script>
<style>

  body {
    margin: 40px 10px;
    padding: 0;
    font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
    font-size: 14px;
  }

  #calendar {
    max-width: 1100px;
    margin: 0 auto;
  }

</style>
