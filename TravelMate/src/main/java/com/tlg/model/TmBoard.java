package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class TmBoard {
	
	int c_idx;
	String c_title;
	String c_content;
	String c_file;
	String created_at;
	int c_view;
	int c_star;
	String id;
	
	public TmBoard(String c_title, String c_content, String c_file, String created_at, String id) {
		super();
		this.c_title = c_title;
		this.c_content = c_content;
		this.c_file = c_file;
		this.created_at = created_at;
		this.id = id;
	}
	
}
