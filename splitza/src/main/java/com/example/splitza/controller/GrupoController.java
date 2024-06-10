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
        redirectWindow(event, "/com/example/splitza/view/adicionar_despesa.fxml", 1);
    }

    @FXML
    protected void onVoltarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/painel.fxml", 4);
    }

    @FXML
    protected void onTotaisButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/totais.fxml", 2);
    }

    @FXML
    protected void onSaldosButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/saldos.fxml", 3); // fazer tela ainda
    }

    private void redirectWindow(ActionEvent event, String path, int botao) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        loader.setControllerFactory(c -> {
            switch (botao){
                case 1:
                    AdicionarDespesaController controller = new AdicionarDespesaController();
                    controller.setGrupoValue(this.grupoValue);
                    return controller;
//                case 2:
//                    TotaisController controller = new TotaisController();
//                    controller.setGrupoValue(this.grupoValue);
//                    return controller;
//                case 3:
//                    SaldosController controller = new SaldosController();
//                    controller.setGrupoValue(this.grupoValue);
//                    return controller;
                case 4:
                    return new PainelController();
                default:
                    return null;
            }
        });
        Parent redirect = loader.load();
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}