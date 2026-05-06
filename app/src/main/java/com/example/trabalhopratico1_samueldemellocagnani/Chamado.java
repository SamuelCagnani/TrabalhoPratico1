package com.example.trabalhopratico1_samueldemellocagnani;

import java.io.Serializable;

public class Chamado implements Serializable {
    private int id;
    private String titulo;
    private String descricao;
    private String local;
    private String tipo;
    private String status;
    private long dataCriacao;
    private String solucao;

    public Chamado() {}

    public Chamado(int id, String titulo, String descricao, String local, String tipo, String status, long dataCriacao, String solucao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.solucao = solucao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(long dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getSolucao() { return solucao; }
    public void setSolucao(String solucao) { this.solucao = solucao; }
}
