package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KanbanCard {
    
	int card_idx;
    String card_title;
    int card_order;
    int col_idx; // 외래키 관계로 사용
}