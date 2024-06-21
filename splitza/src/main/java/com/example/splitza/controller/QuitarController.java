package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Grupo;
import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.impl.LerDespesas;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuitarController extends ControllerAbstrato {

    @FXML
    private ListView<String> dividasListView;

    @FXML
    private Label divLbl;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        LerDespesas lerDespesas = new LerDespesas();
        List<Despesa> despesas = lerDespesas.lerArquivo("despesas-grupo-" + this.grupoValue + ".xml");
        if (Objects.nonNull(despesas)) {
            exibirDividas(despesas);
        }
        dividasListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    protected void onQuitarButtonClick(ActionEvent event) throws IOException {
        List<String> selectedItems = new ArrayList<>(dividasListView.getSelectionModel().getSelectedItems());
        if (selectedItems.isEmpty()) {
            divLbl.setVisible(true);
        } else {
            LerDespesas lerDespesas = new LerDespesas();
            List<Despesa> despesas = lerDespesas.lerArquivo("despesas-grupo-" + this.grupoValue + ".xml");

            if (Objects.nonNull(despesas)) {
                for (Despesa despesa : despesas) {
                    String devedores = despesa.getDevedores().stream()
                            .map(Usuario::getNome)
                            .filter(nome -> !nome.equals(despesa.getPagante().getNome()))
                            .collect(Collectors.joining(", "));
                    String dividaString = despesa.getPagante().getNome() + " recebe R$" + String.format("%.2f", Math.abs(despesa.getDevedores().getFirst().getSaldo())) + " de: " + devedores;
                    if (selectedItems.contains(dividaString)) {
                        dividasListView.getItems().remove(dividaString);
                        despesa.setQuitada(true);
                    }
                }
                if (lerDespesas.deletarArquivo("despesas-grupo-" + this.grupoValue + ".xml")) {
                    for (Despesa d : despesas) {
                        lerDespesas.gravarArquivo("despesas-grupo-" + this.grupoValue + ".xml", d);
                    }
                }
            }
        }

    }


    @FXML
    protected void onVoltarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/saldos.fxml");
    }

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        loader.setControllerFactory(c -> {
            switch (path) {
                case "/com/example/splitza/view/saldos.fxml":
                    SaldosController saldosController = new SaldosController();
                    saldosController.setGrupoValue(this.grupoValue);
                    return saldosController;
                default:
                    return null;
            }
        });
        Parent redirect = loader.load();
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setResizable(false);
        janela.setMaximized(false);
        janela.setScene(scene);
        janela.show();
    }

    private void exibirDividas(List<Despesa> despesas) {
        for (Despesa despesa : despesas) {
            String devedores = despesa.getDevedores().stream()
                    .map(Usuario::getNome)
                    .filter(nome -> !nome.equals(despesa.getPagante().getNome()))
                    .collect(Collectors.joining(", "));

            dividasListView.getItems().add(despesa.getPagante().getNome() + " recebe R$" + String.format("%.2f", Math.abs(despesa.getDevedores().getFirst().getSaldo())) + " de: " + devedores);
        }

    }

}
