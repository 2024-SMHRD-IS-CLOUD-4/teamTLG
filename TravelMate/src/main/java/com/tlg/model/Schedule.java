package com.tlg.model;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Schedule {
	
	
	int sche_idx;
	String sche_type;
	Date sche_tm;
	String vote_result;
	String sche_place;
	int col_idx;
	int card_idx;
	String sche_desc;
	String sche_title;
	
	public Schedule(int scheduleId, String scheType, String schePlace, String scheDesc, Date scheTime) {
		// TODO Auto-generated constructor stub
	}

	public Schedule(String cardTitle, String scheType, String schePlace, String scheDesc, Date scheTime) {
		// TODO Auto-generated constructor stub
	}

}


