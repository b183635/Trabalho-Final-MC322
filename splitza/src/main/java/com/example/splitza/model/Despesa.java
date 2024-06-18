package com.example.splitza.model;

import java.util.List;

public class Despesa {

    private String nome;
    private String nomeGrupo;
    private String data;
    private double valor;
    private Usuario pagante;
    private List<Usuario> devedores;

    private Despesa(DespesaBuilder builder) {
        this.nome = builder.getNome();
        this.nomeGrupo = builder.getNomeGrupo();
        this.data = builder.getData();
        this.valor = builder.getValor();
        this.pagante = builder.getPagante();
        this.devedores = builder.getDevedores();
    }

    public static DespesaBuilder getBuilder() {
        return new DespesaBuilder();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public static class DespesaBuilder {
        private String nome;
        private String nomeGrupo;
        private String data;
        private double valor;
        private Usuario pagante;
        private List<Usuario> devedores;

        public DespesaBuilder setNome(String nome) {
            this.nome = nome;
            return this;
        }

        public DespesaBuilder setNomeGrupo(String nomeGrupo) {
            this.nomeGrupo = nomeGrupo;
            return this;
        }

        public DespesaBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public DespesaBuilder setValor(double valor) {
            this.valor = valor;
            return this;
        }

        public DespesaBuilder setPagante(Usuario pagante) {
            this.pagante = pagante;
            return this;
        }

        public DespesaBuilder setDevedores(List<Usuario> devedores) {
            this.devedores = devedores;
            return this;
        }

        public String getNome() {
            return nome;
        }

        public double getValor() {
            return valor;
        }

        public Usuario getPagante() {
            return pagante;
        }
        public List<Usuario> getDevedores() {
            return devedores;
        }

        public String getNomeGrupo() {
            return nomeGrupo;
        }
        public String getData() {
            return data;
        }

        public Despesa build() {
            return new Despesa(this);
        }
    }

}
