package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Grupo;
import com.example.splitza.model.Historico;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerDespesas;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GrupoController extends ControllerAbstrato{
    @FXML
    private Label nomeGrupoLabel;

    @FXML
    private ListView<String> membrosListView;

    @FXML
    private TableView<Historico> historicoTableView;

    @FXML
    private TableColumn<Historico, Date> dataTableColumn;

    @FXML
    private TableColumn<Historico, Despesa> despesaTableColumn;

    @FXML
    private TableColumn<Historico, String> statusTableColumn;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        nomeGrupoLabel.setText(grupoValue);
        LerGrupos lerGrupos = new LerGrupos();
        List<Grupo> grupos = lerGrupos.lerArquivo("grupos.xml");
        LerDespesas lerDespesas = new LerDespesas();
        List<Despesa> despesas = lerDespesas.lerArquivo("despesas-grupo-" + this.grupoValue + ".xml");
        LerUsuarios lerUsuarios = new LerUsuarios();
        List<UsuarioAbstrato> usuarios = lerUsuarios.lerArquivo("usuarios.xml");
        UsuarioLogado usuarioLogado = usuarios.stream()
                .filter(u -> u instanceof UsuarioLogado && ((UsuarioLogado) u).isLogado())
                .map(u -> (UsuarioLogado) u)
                .findFirst()
                .orElse(null);
        grupos.stream()
                .filter(grupo -> grupo.getNome().equals(grupoValue))
                .findFirst()
                .ifPresent(grupo -> {
                    membrosListView.getItems().addAll(grupo.getMembros());
                    if (Objects.nonNull(despesas)) {
                        grupo.getDespesas().addAll(despesas);
                        atualizarTableHistorico(grupo, usuarioLogado);
                    }
                });
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

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(path)));
        loader.setControllerFactory(c -> {
            switch (path){
                case "/com/example/splitza/view/adicionar_despesa.fxml":
                    AdicionarDespesaController controller = new AdicionarDespesaController();
                    controller.setGrupoValue(this.grupoValue);
                    return controller;
                case "/com/example/splitza/view/totais.fxml":
                    TotaisController totaisController = new TotaisController();
                    totaisController.setGrupoValue(this.grupoValue);
                    return totaisController;
                case "/com/example/splitza/view/saldos.fxml":
                    SaldosController saldosController = new SaldosController();
                    saldosController.setGrupoValue(this.grupoValue);
                    return saldosController;
                case "/com/example/splitza/view/painel.fxml":
                    return new PainelController();
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

    private void atualizarTableHistorico(Grupo grupo, UsuarioLogado usuario) {
        ObservableList<Historico> data = historicoTableView.getItems();

        despesaTableColumn.setCellValueFactory(new PropertyValueFactory<>("despesa"));
        dataTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        grupo.getDespesas().forEach(despesa -> {
            if (despesa.getPagante().getNome().equals(usuario.getNome())) {
                data.add(new Historico(despesa.getData(), " Você pagou R$" + despesa.getValor(), "Você emprestou " + despesa.getPagante().getSaldo()));
            } else if (despesa.getDevedores().stream().anyMatch(u -> u.getNome().equals(usuario.getNome()))) {
                final double saldo = -(despesa.getDevedores().stream().filter(u -> u.getNome().equals(usuario.getNome())).findFirst().get().getSaldo());
                if(despesa.isQuitada()){
                    data.add(new Historico(despesa.getData(), despesa.getPagante().getNome() + " pagou R$" + despesa.getValor(), "quitada"));
                }
                else
                    data.add(new Historico(despesa.getData(), despesa.getPagante().getNome() + " pagou R$" + despesa.getValor(), "Você pegou emprestado R$ " + saldo));
            }
            else{
                if (despesa.isQuitada()){
                    data.add(new Historico(despesa.getData(), despesa.getNome() + ": " + despesa.getPagante().getNome() + " pagou R$" + despesa.getValor(), "quitada"));
                }
                else
                    data.add(new Historico(despesa.getData(), despesa.getNome() + ": " + despesa.getPagante().getNome() + " pagou R$" + despesa.getValor(), "não envolvido"));
            }
        });

    }
}