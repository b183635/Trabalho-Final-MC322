package com.example.splitza.controller;

import com.example.splitza.model.UsuarioLogado;
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

public class CadastrarController extends ControllerAbstrato{

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
        UsuarioLogado usuario = UsuarioLogado.getInstance();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setSaldo(0.0);
        usuario.setLogado(false);
        LerUsuarios lerUsuarios = new LerUsuarios();
        lerUsuarios.gravarArquivo("usuarios.xml", usuario);
        redirectWindow(event, "/com/example/splitza/view/tela_entrar.fxml");
    }

    @Override
    public void initialize() {
        return;
    }

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}