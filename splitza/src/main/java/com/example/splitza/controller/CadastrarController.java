package com.example.splitza.controller;

import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CadastrarController {

    @FXML
    private TextField nomeText;

    @FXML
    private TextField emailText;

    @FXML
    private PasswordField senhaText;

    @FXML
    protected void onCadastrarButtonClick(ActionEvent event) throws IOException {
        String nome = nomeText.getText();
        String email = emailText.getText();
        String senha = senhaText.getText();
        Usuario usuario = new Usuario(nome, email, senha, 0);
        LerUsuarios lerUsuarios = new LerUsuarios();
        lerUsuarios.gravarArquivo("usuarios.xml", usuario);
        redirectWindow(event, "/com/example/splitza/view/painel.fxml");
    }

    private void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}