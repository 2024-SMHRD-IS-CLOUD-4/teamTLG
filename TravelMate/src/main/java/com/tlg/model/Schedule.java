package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Schedule {
	
	int sche_idx;
	int tr_idx;
	String sche_type;
	String sche_dt;
	String sche_tm;
	String id;
	int sche_order;
	String vote_result;

}


