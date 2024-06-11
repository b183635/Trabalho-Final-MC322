package com.example.splitza.model;

public abstract class UsuarioAbstrato {
    protected String nome;
    protected double saldo;

    protected UsuarioAbstrato() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}