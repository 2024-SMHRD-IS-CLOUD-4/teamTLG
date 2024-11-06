package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Partner {
	
	int partner_idx;
	int tr_idx;
	String partner_id;
	String partner_name;
	
	// 여행순번과 동행자이름을 매개변수로 가지는 생성자
	public Partner(int tr_idx, String partner_name) {
		super();
		this.tr_idx = tr_idx;
		this.partner_name = partner_name;
	}
	

	
}
