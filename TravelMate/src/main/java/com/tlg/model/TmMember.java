package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class TmMember {
	
	String id;
	String pw;
	String name;
	String nick;
	String gender;
	String email;

	public TmMember(String id, String pw) {
		this.id = id;
		this.pw = pw;
	}

	public TmMember(String id, String pw, String nick) {
		this.id = id;
		this.pw = pw;
		this.nick = nick;
	}
	
}
