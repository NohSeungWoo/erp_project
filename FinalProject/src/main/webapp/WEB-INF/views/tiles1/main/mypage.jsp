<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<% String ctxPath = request.getContextPath(); %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<style type="text/css">

	.filebox .upload-name {
	    display: inline-block;
	    height: 40px;
	    padding: 0 10px;
	    vertical-align: middle;
	    border: 1px solid #dddddd;
	    width: 70%;
	    color: #999999;
	}
	
	.filebox label {
	    display: inline-block;
	    padding: 6px 15px;
	    color: #fff;
	    vertical-align: middle;
	    background-color: #336699;
	    cursor: pointer;
	    height: 30px;
	    margin-left: 10px;
	    border-radius: 5px;
	    font-size: 10pt;
	}
	
	.filebox input[type="file"] {
	    position: absolute;
	    width: 0;
	    height: 0;
	    padding: 0;
	    overflow: hidden;
	    border: 0;
	}

</style>

<script type="text/javascript">

	$(document).ready(function() {
		
		$("span#searchAddress").click(function(){
			new daum.Postcode({
               oncomplete: function(data) {
                  // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                  // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                  // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                  var addr = ''; // 주소 변수
                  var extraAddr = ''; // 참고항목 변수

                  //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                  if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                      addr = data.roadAddress;
                  } else { // 사용자가 지번 주소를 선택했을 경우(J)
                      addr = data.jibunAddress;
                  }

                  // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                  if(data.userSelectedType === 'R'){
                      // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                      // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                      if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                          extraAddr += data.bname;
                      }
                      // 건물명이 있고, 공동주택일 경우 추가한다.
                      if(data.buildingName !== '' && data.apartment === 'Y'){
                          extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                      }
                      // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                      if(extraAddr !== ''){
                          extraAddr = ' (' + extraAddr + ')';
                      }
                      // 조합된 참고항목을 해당 필드에 넣는다.
                      document.getElementById("extraAddress").value = extraAddr;
                      
                      // alert(extraAddr);
                  
                  } else {
                      document.getElementById("extraAddress").value = '';
                  }

                  // 우편번호와 주소 정보를 해당 필드에 넣는다.
                  document.getElementById('postcode').value = data.zonecode;
                  document.getElementById("address").value = addr;
                  // 커서를 상세주소 필드로 이동한다.
                  document.getElementById("detailAddress").focus();
               }
           }).open();               
		});// end of $("button#searchAddress").click(function(){})
		
		// 파일 input text 값에 파일명 추가
		$("input#file").on('change',function(){
			var fileName = $("#file").val();
			$(".upload-name").val(fileName);
		});
		
		
		// === 수정 버튼을 클릭하였을 때 이벤트 처리 === //
		$("span#editBtn").click(function() {
			var employeeid = $("input#employeeid").val();
			var password = $("input#password").val().trim();
			var name = $("input#name").val().trim();
			var mobile = $("input#mobile").val().trim();
			var postcode = $("input#postcode").val().trim();
			var address = $("input#address").val().trim();
			var detailAddress = $("input#detailAddress").val().trim();
			
			if(password == "") {
				alert("비밀번호를 입력하세요");
				return;
			}
			
			if(name == "") {
				alert("이름을 입력하세요");
				return;
			}
			
			if(mobile == "") {
				alert("연락처를 입력하세요");
				return;
			}
			
			if(postcode == "" || address == "" || detailAddress == "") {
				alert("주소를 입력하세요");
				return;
			}
			
			var mobileRegExp = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/;; ; 
			var mobileBool = mobileRegExp.test(mobile);
			
			if(!mobileBool) {
				alert("연락처를 다시 입력하세요.");
				return;
			}
			
			var regExp = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
			var bool = regExp.test(password);
			
			if(!bool) {
				$.ajax({
					url:"<%= ctxPath%>/passwordCheck.gw",
					type:"POST",
					data:{"password":password,
						  "employeeid":employeeid},
					async:false,
					dataType:"JSON",
					success:function(json) {
						if(!json.isExists) {
							alert("암호는 8글자 이상 15글자 이하에 영문자,숫자,특수기호가 혼합되어야만 합니다.");
				            $("input#password").val("");
				            return; // 종료
						}
						else {
							var frm = document.myPageFrm;
							frm.method = "POST";
							frm.action = "<%= ctxPath%>/mypageEnd.gw";
							frm.submit();
						}
					},
					error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
				});
			}
			else {
				var frm = document.myPageFrm;
				frm.method = "POST";
				frm.action = "<%= ctxPath%>/mypageEnd.gw";
				frm.submit();
			}
		});
		
		
		// 취소버튼 클릭시 main 페이지 이동
		$("span#closeBtn").click(function() {
			location.href = "<%= request.getContextPath()%>/index.gw";
		});
		
	});// end of $(document).ready(function() { })

</script>

<div class="container-fluid container-lg" style="margin: 0px auto; width: 90%;">
	<div class="row">
		<div class="col-8 col-lg-6 offset-0 offset-lg-2">
			<span class="h3" style="font-weight: bold;">개인 정보 수정</span>
		</div>
	</div>
	<br>
	<form name="myPageFrm" enctype="multipart/form-data"> 
	    <div class="form-inline">
	    	<input type="hidden" id="employeeid" name="employeeid" value="${sessionScope.loginuser.employeeid}"/>
	    	<div class="col-6 col-lg-8 offset-3 offset-lg-2 text-center mt-3">
	    		<c:if test="${not empty sessionScope.loginuser.profilename}">
	    			<img src="<%= request.getContextPath()%>/resources/empIMG/${sessionScope.loginuser.profilename}" class="rounded-circle" style="width: 190px; height: 200px;"><br>
	    		</c:if>
	    		<c:if test="${empty sessionScope.loginuser.profilename}">
			    	<img src="<%= request.getContextPath()%>/resources/images/기본프로필_kh.jpg" class="rounded-circle" style="width: 190px; height: 200px;"><br>
	    		</c:if>
	    	</div>
	    	<div class="w-100 my-4"></div>
	    	
	    	<div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">사원번호</label>
	    	</div>
		    <div class="col-8 col-lg-4">
				<input type="text" class="form-control mt-1" id="employeeid" name="employeeid" value="${sessionScope.loginuser.employeeid}" disabled style="width: 100%;" />
		    </div>
		    <div class="w-100 my-4"></div>
	    	
	    	<div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">이메일</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="email" name="email" value="${sessionScope.loginuser.email}" disabled style="width: 100%;">
		    </div>
		    <div class="w-100 my-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">비밀번호</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="password" class="form-control mt-1" id="password" name="password" style="width: 100%;">
		    </div>
		    <div class="w-100 my-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">이름</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="name" name="name" value="${sessionScope.loginuser.name}" style="width: 100%;">
		    </div>
		    <div class="w-100 mb-5"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">연락처</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="mobile" name="mobile" value="${sessionScope.loginuser.mobile}" style="width: 100%;">
		    </div>
		    <div class="w-100 mb-5"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">입사일자</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="hiredate" name="hiredate" value="${sessionScope.loginuser.hiredate}" disabled style="width: 100%;">
		    </div>
		    <div class="w-100 mb-5"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">우편번호</label>
	    	</div>
		    <div class="col-9 col-lg-4">
		       <input type="text" class="form-control mt-1" id="postcode" value="${sessionScope.loginuser.postcode}"  name="postcode" style="width: 40%;">
		       <span id="searchAddress" class="btn btn-secondary btn-sm mt-1 ml-3" style="border-radius: 10px; display: inline;">주소찾기</span>
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="address" value="${sessionScope.loginuser.address}" placeholder="주소" name="address" style="width: 100%;">
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="detailAddress" value="${sessionScope.loginuser.detailAddress}" placeholder="상세주소" name="detailAddress" style="width: 100%;">
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="extraAddress" value="${sessionScope.loginuser.extraAddress}" placeholder="주소옵션" name="extraAddress" style="width: 100%;">
		    </div>
		    <div class="w-100 my-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">프로필사진</label>
	    	</div>
		    <div class="col-8 col-lg-4 filebox">
		    	<input class="upload-name form-control"  disabled placeholder="첨부파일">
			    <label for="file">파일찾기</label> 
			    <input type="file" id="file" name="attach">
			    
		       <!-- <input type="file" class="mt-1 btn" id="attach" name="attach" style="width: 100%;"> -->
		    </div>
		    <div class="w-100 my-3"></div>
	    </div>
	 
	    <br>
	    <div class="row justify-content-center mt-2 pb-5">
		    <div class="col-9 col-lg-5 offset-3 offset-lg-3">
		       <span id="editBtn" class="btn btn-primary mr-3" style="float:left; width: 80px; justify-content: center;">수정</span>
		       <span id="closeBtn" class="btn btn-dark"  style="float:left; width: 80px; justify-content: center;">취소</span>
		    </div>
	    </div>
    
    </form> 
    
   
</div>