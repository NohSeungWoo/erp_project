package com.spring.board.model;

public class BoardCategoryVO_OHJ {
	
	private String bCategorySeq; 	// 카테고리번호
	private String bCategoryName; 	// 카테고리명
	private String userType; 		// 글쓴이타입 (public/secret)
	private String writeAccess; 	// 일반사용자의 글쓰기허용 (y/n)
	private String commentAccess; 	// 댓글허용 (y/n)
	
	private String header;			// 말머리설정 (y/n)
	
	public BoardCategoryVO_OHJ() {}
	
	public BoardCategoryVO_OHJ(String bCategorySeq, String bCategoryName, String userType, String writeAccess,
			String commentAccess, String header) {
		super();
		this.bCategorySeq = bCategorySeq;
		this.bCategoryName = bCategoryName;
		this.userType = userType;
		this.writeAccess = writeAccess;
		this.commentAccess = commentAccess;
		this.header = header;
	}

	
	public String getbCategorySeq() {
		return bCategorySeq;
	}

	public void setbCategorySeq(String bCategorySeq) {
		this.bCategorySeq = bCategorySeq;
	}

	public String getbCategoryName() {
		return bCategoryName;
	}

	public void setbCategoryName(String bCategoryName) {
		this.bCategoryName = bCategoryName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getWriteAccess() {
		return writeAccess;
	}

	public void setWriteAccess(String writeAccess) {
		this.writeAccess = writeAccess;
	}

	public String getCommentAccess() {
		return commentAccess;
	}

	public void setCommentAccess(String commentAccess) {
		this.commentAccess = commentAccess;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
	
}
