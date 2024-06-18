package com.example.splitza.model;

import java.util.List;

public class Grupo {
    private String nome;
    private List<String> membros;
    private List<Despesa> despesas;

    public Grupo(GrupoBuilder builder) {
        this.nome = builder.getNome();
        this.membros = builder.getMembros();
        this.despesas = builder.getDespesas();
    }

    public static GrupoBuilder getBuilder() {
        return new GrupoBuilder();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }

    public List<Despesa> getDespesas() {
        return despesas;
    }

    public void setDespesas(List<Despesa> despesas) {
        this.despesas = despesas;
    }

    public static class GrupoBuilder {
        private String nome;
        private List<String> membros;
        private List<Despesa> despesas;

        public GrupoBuilder setNome(String nome) {
            this.nome = nome;
            return this;
        }

        public GrupoBuilder setMembros(List<String> membros) {
            this.membros = membros;
            return this;
        }

        public GrupoBuilder setDespesas(List<Despesa> despesas) {
            this.despesas = despesas;
            return this;
        }
        public String getNome() {
            return nome;
        }

        public List<String> getMembros() {
            return membros;
        }

        public List<Despesa> getDespesas() {
            return despesas;
        }

        public Grupo build() {
            return new Grupo(this);
        }
    }
}
