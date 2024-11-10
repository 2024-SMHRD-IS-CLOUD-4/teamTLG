package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardComment {

	int cmt_idx;
	int c_idx;
	String cmt_content;
	String created_at;
	Integer cmt_heart;
	String id;
	Integer tr_idx;
	
	public BoardComment(int c_idx, String cmt_content, String id, String created_at) {
		super();
		this.c_idx = c_idx;
		this.cmt_content = cmt_content;
		this.id = id;
		this.created_at = created_at;
	}

}
