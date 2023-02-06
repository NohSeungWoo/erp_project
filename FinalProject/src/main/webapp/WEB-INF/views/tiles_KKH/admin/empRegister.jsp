<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script type="text/javascript">
	
	$(document).ready(function() {
		// 부서목록 보여주는 함수
		getBoardList();
		getPositionList();
		
		var b_emailDuplicate = false;
		
		$(".error").hide();
		
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
		
		// === 이메일 중복 확인 === //
		$("span#emailDuplicateCheck").click(function() {
			b_emailDuplicate = true;
			// 가입하기 버튼을 클릭시 "이메일중복확인" 을 클릭했는지 클릭안했는지를 알아보기위한 용도임.
			
			if($("input#email").val().trim() == "") {
				alert("이메일을 입력하세요!");
				return;
			}
			
			var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
		    // 이메일 정규표현식 객체 생성
		    
			var useremail = $("input#email").val();
		    
		    console.log(useremail);
		    
			var bool = regExp.test(useremail);
		    
			if(!bool) {
				alert("이메일 형식에 맞지 않습니다.");
				$("input#email").val("");
				return;
			}
			else {
				$.ajax({
					url:"<%= request.getContextPath()%>/emailDuplicateCheck.gw",
					type:"POST",
		     		data:{"email":$("input#email").val()},
		     		dataType:"json",
		     		success:function(json) {
		     			
		     			var html = ""
		     			
		     			if(json.isExists) {
		     				// 입력한 email 가 이미 사용중 이라면
		     				alert("해당 이메일은 사용불가합니다.");
		     				$("input#email").val("");
		     			}
		     			else {
		     				// 입력한 email 가 DB 테이블에 존재하지 않는 경우라면 
		     				$("input#email").val(useremail);
		     				html += "<span style='color:blue;'>해당 이메일은 사용 가능합니다.</span>";
		     			}
		     			
		     			$("div#emailCheckOK").html(html);
		     		},
		     		error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
				});
			}
		});
		
		
		$("span#registerBtn").click(function() {
			
			if(!b_emailDuplicate) {
		    	// "이메일중복확인" 을 클릭했는지 클릭안했는지를 알아보기위한 용도임.
		    	alert("이메일중복확인 클릭하여 이메일중복검사를 하세요!!");
		    	return; // 종료
		    }
			
			if($("input#name").val().trim() == "") {
				alert("이름을 입력하세요");
				return;
			}
			
			var regExp = /^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/;; 
		    // 숫자 4자리만 들어오도록 검사해주는 정규표현식 객체 생성
		    
			var mobile = $("input#mobile").val();
		    
			var bool = regExp.test(mobile);
		    
			if(!bool) {
				alert("휴대폰 번호를 정확하게 입력하세요.");
				return;
			}
			
			if( !($("input#salary").val() > 0) ) {
				alert("급여를 숫자로만 정확히 입력하세요.");
				return;
			}
		
			if($("input#address").val().trim() == "") {
				alert("주소를 입력하세요");
				return;
			}
			
			if($("input#detailAddress").val().trim() == "") {
				alert("상세주소를 입력하세요");
				return;
			}
			
			var frm = document.registerFrm;
			frm.method = "POST";
			frm.action = "<%= request.getContextPath()%>/empRegisterEnd.gw";
			frm.submit();
			
		});
		
	});// end of $(document).ready(function() {})
	
	
	// === Function Declaration === //
	// === 부서정보 가져오기 === //
	function getBoardList() {
		$.ajax({
			url:"<%= request.getContextPath()%>/getDepartmentName.gw",
			type:"GET",
			dataType:"JSON",
			success:function(json) {
				if(json.length > 0) {
					
					html = "";
					
					$.each(json, function(index, item) {
						var departmentname = item.depart;
						var departno = item.departno;
						
						html += "<option id='" + departno + "' name='department' value='" + departno + "'>" + departmentname + "</option>";
					});
					
					$("select#fk_departNo").html(html);
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        	}
		});
	}
	
	// === 직급 정보 가져오기
	function getPositionList() {
		$.ajax({
			url:"<%= request.getContextPath()%>/getPositionName.gw",
			type:"GET",
			dataType:"JSON",
			success:function(json) {
				if(json.length > 0) {
					
					html = "";
					
					$.each(json, function(index, item) {
						var positionname = item.position;
						var positionNo = item.positionno;
						
						html += "<option id='" + positionNo + "' name='position' value='" + positionNo + "'>" + positionname + "</option>";
					});
					
					$("select#fk_positionNo").html(html);
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        	}
		});
	}
	
	
</script>    
    
<div class="container-fluid container-lg" style="margin: 50px auto; width: 100%;">
	<div class="row">
		<div class="col-8 col-lg-6 offset-0 offset-lg-2">
			<span class="h3" style="font-weight: bold;">직원등록</span>
		</div>
	</div>
	<br>
	<form name="registerFrm" enctype="multipart/form-data"> 
	    <div class="form-inline">
	    	<div class="col-6 col-lg-8 offset-3 offset-lg-2 text-center">
		    	<img src="<%= request.getContextPath()%>/resources/images/기본프로필_kh.jpg" class="rounded-circle" style="width: 190px; height: 200px;"><br>
	    	</div>
	    	<div class="w-100 my-3"></div>
	    	
	    	<div class="col-3 col-lg-1 offset-lg-3">
		       <label class="float-right">이메일<span class="ml-1" style="color: red;">*</span></label>
	    	</div>
		    <div class="col-7 col-lg-4">
		       <input type="text" class="form-control mt-1" id="email" placeholder="로그인 시 ID로 사용되는 이메일" name="email" style="width: 100%;">
		    </div>
		    <div class="col-2 col-lg-2">
		       <span class="btn btn-sm btn-info" id="emailDuplicateCheck">중복확인</span>
		    </div>
		    <div class="w-100"></div>
		    <div id="emailCheckOK" class="col-8 col-lg-3 offset-3 offset-lg-4 mt-2">
		       
		    </div>
		    <div class="w-100 my-3"></div>
		    
		    <div class="col-3 col-lg-1 offset-lg-3">
		       <label class="float-right">이름<span class="ml-1" style="color: red;">*</span></label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="name" placeholder="이름" name="name" style="width: 100%;">
		    </div>
		    <div class="w-100 mb-4"></div>
		    
		    <div class="col-3 col-lg-1 offset-lg-3">
		       <label class="float-right">연락처<span class="ml-1" style="color: red;">*</span></label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="mobile" placeholder="01012345678" name="mobile" style="width: 100%;">
		    </div>
		    <div class="w-100 mb-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">근무부서</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <select id="fk_departNo" name="fk_departNo" class='selectpicker' data-width='auto' style='width: 100%; height: 35px; border-radius: 3px;'>
		       </select>
		    </div>
		    <div class="w-100 mb-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">직책</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <select id="fk_positionNo" name="fk_positionNo" class='selectpicker' data-width='auto' style='width: 100%; height: 35px; border-radius: 3px;'>
		       </select>
		    </div>
		    <div class="w-100 mb-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">급여</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="text" class="form-control mt-1" id="salary" placeholder="0000만원" name="salary" style="width: 100%;">
		    </div>
		    <div class="w-100 mb-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">우편번호</label>
	    	</div>
		    <div class="col-9 col-lg-4">
		       <input type="text" class="form-control mt-1" id="postcode" placeholder="우편번호" name="postcode" style="width: 40%;">
		       <span id="searchAddress" class="btn btn-secondary btn-sm mt-1 ml-3" style="border-radius: 10px; display: inline;">주소찾기</span>
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="address" placeholder="주소" name="address" style="width: 100%;">
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="detailAddress" placeholder="상세주소" name="detailAddress" style="width: 100%;">
		    </div>
		    <div class="w-100 my-1"></div>
		    
		    <div class="col-9 col-lg-4 offset-3 offset-lg-4">
		       <input type="text" class="form-control mt-1" id="extraAddress" placeholder="주소옵션" name="extraAddress" style="width: 100%;">
		    </div>
		    <div class="w-100"></div>
		       <span class="error col-8 col-lg-3 offset-3 offset-lg-4 mt-2" style="color: red;">주소를 입력하세요</span>
		    <div class="w-100 my-4"></div>
		    
		    <div class="col-3 col-lg-2 offset-lg-2 pull-right">
		       <label class="float-right">프로필사진</label>
	    	</div>
		    <div class="col-8 col-lg-4">
		       <input type="file" class="mt-1" id="attach" name="attach" style="width: 100%;">
		    </div>
		    
	    </div>
	 
	    <br>
	    <div class="row justify-content-center mt-2">
		    <div class="col-10 col-lg-8 offset-2 offset-lg-4">
		       <span id="registerBtn" class="btn btn-primary mr-4" style="float:left; width: 120px; justify-content: center;">등록</span>
		     
		       <button type="reset" class="btn btn-dark" style="float:left; width: 120px; justify-content: center;">취소</button>
		    
		    </div>
	    </div>
	    
	    </form> 
	    
    
</div>