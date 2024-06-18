package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Grupo;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CriarGrupoController extends ControllerAbstrato{
    @FXML
    private TextField nomeGrupoTxt;

    @FXML
    private TextField nomeMembroTxt;

    @FXML
    private ListView<String> membrosListView;

    @FXML
    public void initialize() {
        LerUsuarios lerUsuarios = new LerUsuarios();
        List<UsuarioAbstrato> usuarios = lerUsuarios.lerArquivo("usuarios.xml");
        UsuarioLogado usuarioLogado = usuarios.stream()
                .filter(u -> u instanceof UsuarioLogado && ((UsuarioLogado) u).isLogado())
                .map(u -> (UsuarioLogado) u)
                .findFirst()
                .orElse(null);
        if (Objects.nonNull(usuarioLogado)) {
            membrosListView.getItems().add(usuarioLogado.getNome());
        }

    }

    @FXML
    protected void onCriarButtonClick(ActionEvent event) throws IOException {
        Grupo grupo = Grupo.getBuilder()
                .setNome(nomeGrupoTxt.getText())
                .setMembros(membrosListView.getItems())
                .setDespesas(new ArrayList<>())
                .build();
        LerGrupos lerGrupos = new LerGrupos();
        List<Grupo> grupos = lerGrupos.lerArquivo("grupos.xml");
        if(Objects.nonNull(grupos) && (grupos.stream().anyMatch(g -> g.getNome().equals(grupo.getNome())))){
                throw new IllegalArgumentException("Grupo já existe");
        }
        lerGrupos.gravarArquivo("grupos.xml", grupo);
        redirectWindow(event, "/com/example/splitza/view/painel.fxml");
    }

    @FXML
    protected void onAdicionarButtonClick(ActionEvent event) {
        String nome = nomeMembroTxt.getText();
        membrosListView.getItems().add(nome);
        nomeMembroTxt.clear();
    }

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
        Parent redirect = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
        Scene scene = new Scene(redirect);
        Stage janela = (Stage) ((Node) event.getSource()).getScene().getWindow();
        janela.setScene(scene);
        janela.show();
    }
}