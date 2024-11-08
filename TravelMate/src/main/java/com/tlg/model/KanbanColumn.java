package com.tlg.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KanbanColumn {
	
	int col_idx;
	String col_title;
	int col_order;
	int tr_idx;
	
	// KANBAN_CARD 정보를 담기 위한 필드 추가
    List<KanbanCard> cards = new ArrayList<>();
	

}
