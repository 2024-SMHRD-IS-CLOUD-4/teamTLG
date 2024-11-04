package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {

	int cmt_idx;
	int c_idx;
	String cmt_content;
	String created_at;
	int cmt_heart;
	String id;
	int tr_idx;
	
	public Comment(int c_idx, String cmt_content, String created_at, int cmt_heart, String id, int tr_idx) {
		super();
		this.c_idx = c_idx;
		this.cmt_content = cmt_content;
		this.created_at = created_at;
		this.cmt_heart = cmt_heart;
		this.id = id;
		this.tr_idx = tr_idx;
	}
	
	
	

}
