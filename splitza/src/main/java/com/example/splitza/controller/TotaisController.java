package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Usuario;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerDespesas;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TotaisController extends ControllerAbstrato {

    @FXML
    private Label totaisGrupoLbl;

    @FXML
    private Label financasLbl;

    @FXML
    private Label dividasLbl;

    @FXML
    private Label saldoLbl;

    @FXML
    private Label totalUsuarioLogadoLbl;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        LerUsuarios lerUsuarios = new LerUsuarios();
        List<UsuarioAbstrato> usuarios = lerUsuarios.lerArquivo("usuarios.xml");
        UsuarioLogado usuarioLogado = usuarios.stream()
                .filter(u -> u instanceof UsuarioLogado && ((UsuarioLogado) u).isLogado())
                .map(u -> (UsuarioLogado) u)
                .findFirst()
                .orElse(null);
        LerDespesas lerDespesas = new LerDespesas();
        List<Despesa> despesas = lerDespesas.lerArquivo("despesas-grupo-" + this.grupoValue + ".xml");
        if (Objects.nonNull(despesas) && Objects.nonNull(usuarioLogado)){
            double totalGrupo = despesas.stream()
                    .mapToDouble(Despesa::getValor)
                    .sum();
            totaisGrupoLbl.setText("R$" + totalGrupo);

            double totalUsuarioLogado = despesas.stream()
                    .filter(despesa -> despesa.getPagante().getNome().equals(usuarioLogado.getNome()))
                    .mapToDouble(Despesa::getValor)
                    .sum();
            totalUsuarioLogadoLbl.setText("R$" + totalUsuarioLogado);
            double aux = 0;
            for(Despesa d : despesas){
                if(!d.getPagante().getNome().equals(usuarioLogado.getNome())){
                    for(Usuario u : d.getDevedores()){
                        if(u.getNome().equals(usuarioLogado.getNome())){
                            aux -= u.getSaldo();
                        }
                    }
                }
            }
            dividasLbl.setText("R$" + aux);

            double saldo = totalUsuarioLogado - aux;
            if(saldo >= 0){
                saldoLbl.setText("R$" + String.format("%.2f", saldo));
                financasLbl.setText("devem a você");
            } else {
                saldoLbl.setText("R$" + String.format("%.2f", -saldo));
                financasLbl.setText("você deve");
            }
        }

    }


    @FXML
    protected void onVoltarButtonClick(ActionEvent event) throws IOException {
        redirectWindow(event, "/com/example/splitza/view/grupo.fxml");
    }

    protected void redirectWindow(ActionEvent event, String path) throws IOException {
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
        janela.setResizable(false);
        janela.setMaximized(false);
        janela.show();
    }

}
