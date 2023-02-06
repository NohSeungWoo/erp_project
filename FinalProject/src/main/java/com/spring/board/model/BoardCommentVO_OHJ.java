package com.spring.board.model;

/* === &82. 댓글용 VO 생성하기
	먼저 오라클에서 tbl_boardComment 테이블을 생성한다.
	또한 tbl_board 테이블에 commentCount 컬럼을 추가한다. */
public class BoardCommentVO_OHJ {
	
	private String commentSeq; 		// 댓글번호
	private String fk_boardSeq; 	// 게시판 글번호
	private String fk_employeeId; 	// 사원번호
	private String content; 		// 댓글내용
	private String regDate; 		// 작성일자
	
	// == select용 ==
	////////////////////////////////////////////
	private String name; 			 // 글쓴이 -> ohhj.xml에서 join한 결과를 자동으로 set해서 담는 용도임.
	private String positionName; 	 // 직급명
	////////////////////////////////////////////
		
	public BoardCommentVO_OHJ() {}
	
	public BoardCommentVO_OHJ(String commentSeq, String fk_boardSeq, String fk_employeeId, String content,
			String regDate) {
		super();
		this.commentSeq = commentSeq;
		this.fk_boardSeq = fk_boardSeq;
		this.fk_employeeId = fk_employeeId;
		this.content = content;
		this.regDate = regDate;
	}

	public String getCommentSeq() {
		return commentSeq;
	}
	
	public void setCommentSeq(String commentSeq) {
		this.commentSeq = commentSeq;
	}
	
	public String getFk_boardSeq() {
		return fk_boardSeq;
	}
	
	public void setFk_boardSeq(String fk_boardSeq) {
		this.fk_boardSeq = fk_boardSeq;
	}
	
	public String getFk_employeeId() {
		return fk_employeeId;
	}
	
	public void setFk_employeeId(String fk_employeeId) {
		this.fk_employeeId = fk_employeeId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	////////////////////////////////////////////

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	
}
