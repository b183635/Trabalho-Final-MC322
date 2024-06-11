package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdicionarDespesaController {

    @FXML
    private TextField nomeTxt;

    @FXML
    private TextField valorTxt;

    @FXML
    private ListView<String> membrosListView;

    @FXML
    private ChoiceBox<String> membrosChoiceBox;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        membrosListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        LerGrupos lerGrupos = new LerGrupos();
        lerGrupos.lerArquivo("grupos.xml").stream()
                .filter(grupo -> grupo.getNome().equals(grupoValue))
                .findFirst()
                .ifPresent(grupo -> {
                    membrosListView.getItems().addAll(grupo.getMembros());
                    membrosChoiceBox.getItems().addAll(grupo.getMembros());
                });

    }


    @FXML
    protected void onVoltarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/grupo.fxml");
    }

    @FXML
    protected void onAdicionarButtonClick(ActionEvent event) throws IOException {
        String nome = nomeTxt.getText();
        double valor = Double.parseDouble(valorTxt.getText().replace(",", "."));
        Usuario pagante = new Usuario(membrosChoiceBox.getValue(), null, null, 0);
        List<Usuario> devedores = membrosListView.getSelectionModel().getSelectedItems().stream().map(s -> new Usuario(s, null, null, 0)).toList();
        Despesa despesa = new Despesa(nome, valor, pagante, devedores);
        calcularSaldos(despesa);
        redirectWindow(event, "/com/example/splitza/view/grupo.fxml");
    }

    private void redirectWindow(ActionEvent event, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        loader.setControllerFactory(c -> {
            GrupoController controller = new GrupoController();
            controller.setGrupoValue(this.grupoValue);
            return controller;
        });
        Parent redirect = loader.load();
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }

    private void calcularSaldos(Despesa despesa) {
        double valor = despesa.getValor();
        Usuario pagante = despesa.getPagante();
        List<Usuario> devedores = despesa.getDevedores();
        double valorDividido = valor / (devedores.size());
        if(devedores.stream().anyMatch(usuario -> usuario.getNome().equals(pagante.getNome()))){
            pagante.setSaldo(pagante.getSaldo() + valor - valorDividido);
        }
        else {
            pagante.setSaldo(pagante.getSaldo() + valor);
        }
        devedores.forEach(usuario -> usuario.setSaldo(usuario.getSaldo() - valorDividido));
    }

}