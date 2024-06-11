package com.example.splitza.model;

public class Historico {
    private String data;
    private String despesa;
    private String status;

    public Historico(String data, String despesa, String status) {
        this.data = data;
        this.despesa = despesa;
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDespesa() {
        return despesa;
    }

    public void setDespesa(String despesa) {
        this.despesa = despesa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

