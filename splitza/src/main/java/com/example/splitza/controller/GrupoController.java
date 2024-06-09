package com.example.splitza.controller;

import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GrupoController {
    @FXML
    private Label nomeGrupoLabel;

    @FXML
    private ListView<String> membrosListView;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        nomeGrupoLabel.setText(grupoValue);
        LerGrupos lerGrupos = new LerGrupos();
        lerGrupos.lerArquivo("grupos.xml").stream()
                .filter(grupo -> grupo.getNome().equals(grupoValue))
                .findFirst()
                .ifPresent(grupo -> membrosListView.getItems().addAll(grupo.getMembros()));
    }

    @FXML
    protected void onAdicionarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/adicionar_despesa.fxml");
    }

    @FXML
    protected void onVoltarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/painel.fxml");
    }

    @FXML
    protected void onTotaisButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/totais.fxml");
    }

    @FXML
    protected void onSaldosButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/saldos.fxml"); // fazer tela ainda
    }

    private void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}