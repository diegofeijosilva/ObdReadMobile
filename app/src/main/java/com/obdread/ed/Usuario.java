package com.obdread.ed;

import java.io.Serializable;

public class Usuario implements Serializable {

	private Long id;
	private String ticket;
	private String email;
	private String senha;

	public Usuario(){}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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
