package com.example.splitza.controller;

import com.example.splitza.model.Usuario;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class EntrarController extends ControllerAbstrato{
    @FXML
    private TextField emailText;

    @FXML
    private PasswordField senhaText;

    @FXML
    private Label incorretoLbl;

    @FXML
    protected void onEntrarButtonClick(ActionEvent event) throws IOException {
        LerUsuarios lerUsuarios = new LerUsuarios();
        var usuarios = lerUsuarios.lerArquivo("usuarios.xml");

        boolean emailExiste = usuarios.stream()
                .filter(UsuarioLogado.class::isInstance)
                .anyMatch(usuario -> ((UsuarioLogado) usuario).getEmail().equals(emailText.getText()));

        if (emailExiste) {
            UsuarioLogado usuario = usuarios.stream()
                    .filter(u -> u instanceof UsuarioLogado && ((UsuarioLogado) u).getEmail().equals(emailText.getText()))
                    .map(u -> (UsuarioLogado) u)
                    .findFirst()
                    .orElse(null);

            if (usuario != null && usuario.getSenha().equals(senhaText.getText())) {
                usuario.setLogado(true);
                lerUsuarios.gravarArquivo("usuarios.xml", usuario);
                redirectWindow(event, "/com/example/splitza/view/painel.fxml");
            } else {
                incorretoLbl.setVisible(true);
            }
        } else {
            incorretoLbl.setVisible(true);
        }

    }

    @FXML
    protected void onCadastrarClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/cadastro.fxml");
    }

    @Override
    public void initialize() {
        return;
    }

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setResizable(false);
        janela.setMaximized(false);
        janela.setScene(scene);
        janela.show();
    }
}