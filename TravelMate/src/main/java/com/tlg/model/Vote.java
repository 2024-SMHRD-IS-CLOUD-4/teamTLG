package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vote {

	int vote_idx;
	int sche_idx;
	int vote_score;
	String create_at;
	String id;
	String vote_result;
	int tr_idx;
	
	public Vote(int sche_idx, String id, int tr_idx) {
		super();
		this.sche_idx = sche_idx;
		this.id = id;
		this.tr_idx = tr_idx;
	}
	
	
	
	
	
}
