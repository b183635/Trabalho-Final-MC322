package com.example.splitza.controller;

import com.example.splitza.model.Grupo;
import com.example.splitza.model.Usuario;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PainelController {

    @FXML
    private ChoiceBox<String> myChoiceBox;

    @FXML
    private Label olaLbl;

    @FXML
    public void initialize() {
        LerGrupos lerGrupos = new LerGrupos();
        List<Grupo> grupos = lerGrupos.lerArquivo("grupos.xml");
        LerUsuarios lerUsuarios = new LerUsuarios();
        List<UsuarioAbstrato> usuarios = lerUsuarios.lerArquivo("usuarios.xml");
        UsuarioLogado usuarioLogado = usuarios.stream()
                .filter(u -> u instanceof UsuarioLogado && ((UsuarioLogado) u).isLogado())
                .map(u -> (UsuarioLogado) u)
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(usuarioLogado)) {
            olaLbl.setText("Olá, " + usuarioLogado.getNome());
        }

        if (Objects.nonNull(grupos)) {
            grupos.forEach(grupo -> myChoiceBox.getItems().add(grupo.getNome()));
        }

        myChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    Stage janela = (Stage) myChoiceBox.getScene().getWindow();
                    redirectWindow("/com/example/splitza/view/grupo.fxml", janela, t1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    protected void onCriarGrupoButtonClick(ActionEvent event) throws IOException {
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        redirectWindow("/com/example/splitza/view/criar_grupo.fxml", janela, null);
    }

    private void redirectWindow(String path, Stage janela, String chosenValue) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        if (Objects.nonNull(chosenValue)){
            loader.setControllerFactory(c -> {
                GrupoController controller = new GrupoController();
                controller.setGrupoValue(chosenValue);
                return controller;
            });
        }
        Parent redirect = loader.load();
        Scene scene = new Scene(redirect);
        janela.setScene(scene);
        janela.show();
    }
}