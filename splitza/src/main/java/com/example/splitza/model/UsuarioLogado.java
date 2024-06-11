package com.example.splitza.model;

public class UsuarioLogado extends UsuarioAbstrato {
    private String email;
    private String senha;
    boolean logado;

    private static UsuarioLogado instance;

    private UsuarioLogado() {
    }

    public static UsuarioLogado getInstance() {
        if (instance == null) {
            instance = new UsuarioLogado();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isLogado() {
        return logado;
    }

    public void setLogado(boolean logado) {
        this.logado = logado;
    }


}
