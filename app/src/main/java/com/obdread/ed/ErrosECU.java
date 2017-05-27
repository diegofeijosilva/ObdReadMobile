package com.obdread.ed;

import java.io.Serializable;

/**
 * Created by Diego Feijó on 21/05/2017.
 */

public class ErrosECU implements Serializable {

    /// Hash do usuário
    private String hashUser;
    private Long idVeiculo;
    private String data;
    private String codigo;
    private String descricao;
    private Integer level;


    public String getHashUser() {
        return hashUser;
    }

    public void setHashUser(String hashUser) {
        this.hashUser = hashUser;
    }

    public Long getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(Long idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
