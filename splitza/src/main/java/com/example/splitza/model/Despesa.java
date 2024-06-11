package com.example.splitza.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Despesa {

    private String nome;
    private String nomeGrupo;
    private String data;
    private double valor;
    private Usuario pagante;
    private List<Usuario> devedores;

    public Despesa(String nome, String nomeGrupo, String data, double valor, Usuario pagante, List<Usuario> devedores) {
        this.nome = nome;
        this.nomeGrupo = nomeGrupo;
        this.valor = valor;
        this.pagante = pagante;
        this.devedores = devedores;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Usuario getPagante() {
        return pagante;
    }

    public void setPagante(Usuario pagante) {
        this.pagante = pagante;
    }

    public List<Usuario> getDevedores() {
        return devedores;
    }

    public void setDevedores(List<Usuario> devedores) {
        this.devedores = devedores;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
