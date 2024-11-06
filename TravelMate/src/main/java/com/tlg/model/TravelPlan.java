package com.tlg.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TravelPlan {

	String tr_idx;
	String id;
	String tr_title;
	Date tr_st_dt;
	Date tr_ed_dt;
	String tr_desc;
	String tr_ticket;

	public TravelPlan(String id, String tr_title, Date tr_st_dt, Date tr_ed_dt) {
		super();
		this.id = id;
		this.tr_title = tr_title;
		this.tr_st_dt = tr_st_dt;
		this.tr_ed_dt = tr_ed_dt;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return tr_title + " - " + dateFormat.format(tr_st_dt) + " ~ " + dateFormat.format(tr_ed_dt);
	}

	// 날짜를 "yyyy-MM-dd" 형식으로 반환하는 메소드 추가
	public String getFormattedStartDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(tr_st_dt);
	}

	public String getFormattedEndDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(tr_ed_dt);
	}

}
