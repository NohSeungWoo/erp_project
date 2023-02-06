<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% 
	String ctxPath = request.getContextPath(); 
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
 
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		
		
		// === jQuery UI 의 datepicker === //
		$("input#start_date").datepicker({
	                 dateFormat: 'yy-mm-dd'  //Input Display Format 변경
	                ,showOtherMonths: true   //빈 공간에 현재월의 앞뒤월의 날짜를 표시
	                ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
	                ,changeYear: true        //콤보박스에서 년 선택 가능
	                ,changeMonth: true       //콤보박스에서 월 선택 가능                
	                ,showOn: "both"          //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
	                ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
	                ,buttonImageOnly: true   //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
	                ,buttonText: "선택"       //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
	                ,yearSuffix: "년"         //달력의 년도 부분 뒤에 붙는 텍스트
	                ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
	                ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
	                ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
	                ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
	              //,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
	              //,maxDate: "+1M" //최대 선택일자(+1D:하루후, +1M:한달후, +1Y:일년후)                
		});
			//초기값을 오늘 날짜로 설정
			$('input#start_date').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후) 
			
		
		  
		/////////////////////////////////////////////////////
		// === 전체 datepicker 옵션 일괄 설정하기 ===  
        //     한번의 설정으로 $("input#fromDate"), $('input#toDate')의 옵션을 모두 설정할 수 있다.
        $(function() {
              //모든 datepicker에 대한 공통 옵션 설정
              $.datepicker.setDefaults({
                  dateFormat: 'yy-mm-dd' //Input Display Format 변경
                  
                  ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
                  ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
                  ,changeYear: true //콤보박스에서 년 선택 가능
                  ,changeMonth: true //콤보박스에서 월 선택 가능                
                  ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
                  ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
               	  ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
                  ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
                  ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
                  ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
                  ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
                  ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
                  ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
               // ,minDate: "-1M" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
               // ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
              });
        //input을 datepicker로 선언
        $("input#end_date").datepicker();                    
              
        //From의 
        $('input#end_date').datepicker('setDate', '+3D'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
         
             
         });
		
        var start_date = $(".datepicker[name=start_date]").val();
        var end_date = $(".datepicker[name=end_date]").val();
        var name = $("input:hidden[id=name]").val();
		var fk_employeeid = $("input:hidden[id=fk_employeeid]").val();
      	var seq = $("input:hidden[id=fk_employeeid]").val();
    	$("button#btnInsert").click(function(){
			var frm = document.InsertFrm;
			
			frm.method = "POST";
			frm.action = "<%= ctxPath%>/vacationInsert.gw";
			frm.submit();
		});
	});
	
</script>
<div style="display: flex;">
	<div style="margin: auto; padding-left: 3%;">
	 <h3 style=" margin-bottom: 30px; margin-left: 20px;">휴가 신청 </h3>
	 <hr width = "97%" color = "gray" style="margin-bottom: 30px;">

	  <form name="InsertFrm" style=" width : 1000px; margin-top: 50px;">
	  	<table style="width:97%; margin-left:20px; margin-top: 50px; " class="table table-bordered" >
          <tr>
              <th style="width: 25%; background-color: #DDD;">신청자</th>
              <td>
              	<!--  <input  style="width:90%;  " type="text" id="name" name="name" placeholder="사원을 작성해주세요." /> -->
              	<input type="hidden" name="name" id="name" value="${sessionScope.loginuser.name }" ><label for="a" >본인(${sessionScope.loginuser.name }) </label>		
              	<input type="hidden" name="fk_employeeid" id="fk_employeeid" value="${sessionScope.loginuser.employeeid}" />
              
              </td>	
          </tr>
          <tr>
            <th style="width: 25%; background-color: #DDD;">연차 정보</th>
            <td>
            	<label>전체 : ${sessionScope.user.dayoff}일   </label><br>
            	
            		<label>사용 : </label>&nbsp;<input style="width:50px;" name="annual" id="annual" />
            		
            </td>	
          </tr>
          <tr>
            <th style="width: 25%; background-color: #DDD;">연차 종류</th>
            <td>
            	<!-- <input style="width:90%;"type="text" id="vacation" name="vacation" placeholder="휴가를 적어주세요.(ex.2021년 휴가)" />-->
            	<select id="vacation" class="form-control" name="vacation" >                      
				  <option value="" disabled selected>분류</option>
				  <option value="반차">반차</option>
			      <option value="연차">연차</option>    
				  <option value="경조사">경조사</option>
			      <option value="병가">병가</option>
			      <option value="공과">공가</option>
     			</select>
            </td>	
          </tr>
          <tr>
            <th style="width: 25%; background-color: #DDD;">기간</th>
            <td>
            	<!--  <input style="width:90%;" type="text" id="timemanager" name="timemanager" placeholder="근태(일/시간)을 적어주세요.(ex.7.0)"  ></input> -->
            	  <input type="text" id="start_date" name="start_date">  ~ <input type="text" id="end_date" name="end_date"> 
            	
            </td>	
          </tr>
          </table>
          
          
          
          <div style="margin-left:20px; margin-bottom : 20px;">
		        <button type="submit" class="btn btn-info" id="btnInsert">등록하기</button>
		        <button type="button" class="btn btn-secondary" onclick="javascript:history.back()">뒤로가기</button>
          </div>
    	</form>
    	
    	
    	
</div>
</div>

     
      
  
	