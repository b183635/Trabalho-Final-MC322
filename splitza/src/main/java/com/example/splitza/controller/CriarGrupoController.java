package com.example.splitza.controller;

import com.example.splitza.model.Grupo;
import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CriarGrupoController {
    @FXML
    private TextField nomeGrupoTxt;

    @FXML
    private TextField nomeMembroTxt;

    @FXML
    private ListView<String> membrosListView;


    @FXML
    protected void onCriarButtonClick(ActionEvent event) throws IOException {
        Grupo grupo = new Grupo(nomeGrupoTxt.getText(), membrosListView.getItems());
        LerGrupos lerGrupos = new LerGrupos();
        lerGrupos.gravarArquivo("grupos.xml", grupo);
        redirectWindow(event, "/com/example/splitza/view/painel.fxml");
    }

    @FXML
    protected void onAdicionarButtonClick(ActionEvent event) throws IOException {
        String nome = nomeMembroTxt.getText();
        membrosListView.getItems().add(nome);
        nomeMembroTxt.clear();
    }

    private void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}