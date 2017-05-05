package com.obdread.ed;

import java.io.Serializable;

/*
 * Essa Classe serve para grava��o de logs no sistema.
 * Os logs s�o enviados para o webservice*/

public class LogTriade implements Serializable {
	
	private int id;
	private String usuario;
	private String dispositivo;
	private String operacao;
	
	public LogTriade(){}
	
	public LogTriade(String usuario, String disp, String op){
		this.usuario = usuario;
		this.dispositivo = disp;
		this.operacao = op;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getDispositivo() {
		return dispositivo;
	}
	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	public String getOperacao() {
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

}
