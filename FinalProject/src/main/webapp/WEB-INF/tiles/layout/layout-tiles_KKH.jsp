<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- === #25. tiles 를 사용하는 레이아웃5(게시판) 페이지 만들기 === --%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<%
   String ctxPath = request.getContextPath();
%>    
    
<!DOCTYPE html>
<html>
<head>
<title>Final(1조)-5</title>
  <!-- Required meta tags -->
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> 
  
  <!-- Bootstrap CSS -->
  <link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/bootstrap-4.6.0-dist/css/bootstrap.min.css" > 
  
  <!-- Font Awesome 5 Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  
  <!-- 직접 만든 CSS 2 -->
  <%-- <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/style2.css" /> --%>
  <style type="text/css">
	body {font-family: Arial, Helvetica, sans-serif, 돋움; }

	#mycontainer   { padding: 0; }
	#myheader      { display: flex; position: relative; background-color: black;}
														/* bg-color : 헤더가 양옆으로 꽉 차게 보이게 하는 용도 */	
	#mysideinfo    { min-height:900px; position: relative; padding-top: 40px;
	                 float:left; width:19%; }
	                 
	#mycontent     { min-height:900px; position: relative; padding-top: 40px; /* background-color:#f4f5f7; */ border-left: solid 1px gray;
	                 float:right; width:80%; }
	                 
	#myfooter      { display: flex; position: relative; background-color:#555555; clear:both; }
	 
	#myheader a {text-decoration:none;} /* a태그는 밑줄제거 */
  </style>
  
  <!-- Optional JavaScript -->
  <script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery-3.3.1.min.js"></script>
  <script type="text/javascript" src="<%= ctxPath%>/resources/bootstrap-4.6.0-dist/js/bootstrap.bundle.min.js" ></script>
  <script type="text/javascript" src="<%= ctxPath%>/resources/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script> 
  
  <%--  ===== 스피너를 사용하기 위해  jquery-ui 사용하기 ===== --%>
  <link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.css" /> 
  <script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
    
  <%-- *** ajax로 파일을 업로드할때 가장 널리 사용하는 방법 ==> ajaxForm *** --%>
  <script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery.form.min.js"></script>
  
</head>
<body>
   <div id="mycontainer">
      <div id="myheader">
         <tiles:insertAttribute name="header" />
      </div>
      <div class="container-fluid" style="max-width:1600px">
	      <div id="mysideinfo">
	         <tiles:insertAttribute name="sideinfo" />
	      </div>
	      <div id="mycontent">
	         <tiles:insertAttribute name="content" />
	      </div>
      </div>
      
      <div id="myfooter">
         <tiles:insertAttribute name="footer" />
      </div>
   </div>
</body>
</html>    