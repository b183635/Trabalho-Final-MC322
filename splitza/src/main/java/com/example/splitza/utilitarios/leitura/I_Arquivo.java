package com.example.splitza.utilitarios.leitura;

import java.util.List;

public interface I_Arquivo<T> {
    List<T> lerArquivo(String path);
    void gravarArquivo(String path, T usuario);
}
