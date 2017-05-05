package com.obdread.ed;

import java.io.Serializable;

public class Usuario implements Serializable {

	private Long id;
	private String ticket;

	public Usuario(){}


	public void setId(Long id) {
		this.id = id;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Long getId() {
		return id;
	}

	public String getTicket() {
		return ticket;
	}
}
