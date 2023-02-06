package com.spring.finalProject.common;

import javax.servlet.http.HttpServletRequest;

public class MyUtil_KGH {

	// *** '?' 다음의 데이터까지 포함한 현재 URL 주소를 알려주는 메소드를 생성 *** //
	public static String getCurrentURL(HttpServletRequest request) {
		
		String currentURL = request.getRequestURL().toString();
		//	System.out.println("~~~ 확인용 currentURL => " + currentURL);
		//	~~~ 확인용 currentURL => http://localhost:9090/MyMVC/member/memberList.up
		
		String queryString = request.getQueryString();
		// System.out.println("~~~ 확인용 queryString => " + queryString);
		// ~~~ 확인용 queryString => searchType=name&searchWord=%EC%9C%A0&sizePerPage=5(GET방식일때)
		// ~~~ 확인용 queryString => null (POST방식일 경우)
		
		if(queryString != null) {	// GET 방식일 경우
			currentURL += "?" + queryString;
		}
		
		// System.out.println("~~~ 확인용 currentURL => " + currentURL);
		// ~~~ 확인용 currentURL => http://localhost:9090/MyMVC/member/memberList.up?currentShowPageNo=5&sizePerPage=5&searchType=name&searchWord=%EC%9C%A0

		String ctxPath = request.getContextPath();
		//		/MyMVC
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		//		27	   =		21				+		 6
		
		currentURL = currentURL.substring(beginIndex);
		// 	/member/memberList.up?currentShowPageNo=5&sizePerPage=5&searchType=name&searchWord=%EC%9C%A0
		
		return currentURL;
	}

	
	// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // 
    public static String secureCode(String str) {
    
    	
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
      
        return str;
    }
	
}
