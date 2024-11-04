package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelPlan {

	String tr_idx;
	String id;
	String tr_title;
	String tr_st_dt;
	String tr_ed_dt;
	String tr_desc;
	String tr_ticket;
	
	
	public TravelPlan(String id, String tr_title, String tr_st_dt, String tr_ed_dt, String tr_desc) {
		super();
		this.id = id;
		this.tr_title = tr_title;
		this.tr_st_dt = tr_st_dt;
		this.tr_ed_dt = tr_ed_dt;
		this.tr_desc = tr_desc;
	}
	
	
}


