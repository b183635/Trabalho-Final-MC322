package com.example.splitza.controller;

import com.example.splitza.model.Usuario;
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

public class EntrarController {
    @FXML
    private TextField emailText;

    @FXML
    private PasswordField senhaText;

    @FXML
    private Label incorretoLbl;

    @FXML
    protected void onEntrarButtonClick(ActionEvent event) throws IOException {
        // verificar existencia do usuario, se existir ir para painel
        LerUsuarios lerUsuarios = new LerUsuarios();
        var usuarios = lerUsuarios.lerArquivo("usuarios.xml");

        boolean emailExiste = usuarios.stream()
                .anyMatch(usuario -> usuario.getEmail().equals(emailText.getText()));

        if (emailExiste) {
            Usuario usuario = usuarios.stream()
                    .filter(u -> u.getEmail().equals(emailText.getText()))
                    .findFirst()
                    .orElse(null);

            if (usuario != null && usuario.getSenha().equals(senhaText.getText())) {
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

    private void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}