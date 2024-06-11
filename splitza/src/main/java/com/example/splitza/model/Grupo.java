package com.example.splitza.model;

import java.util.List;

public class Grupo {
    private String nome;
    private List<String> membros;
    private List<Despesa> despesas;

    public Grupo(String nome, List<String> membros, List<Despesa> despesas) {
        this.nome = nome;
        this.membros = membros;
        this.despesas = despesas;
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
}
