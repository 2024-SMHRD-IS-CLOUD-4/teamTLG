package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CheckList {
	
	int item_idx;
	String check_type;
	String item;
	String is_checked;
	String id;
	String created_at;
	int tr_idx;
	
}
