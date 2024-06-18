package com.example.splitza.controller;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Grupo;
import com.example.splitza.model.Usuario;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
import com.example.splitza.utilitarios.leitura.impl.LerDespesas;
import com.example.splitza.utilitarios.leitura.impl.LerGrupos;
import com.example.splitza.utilitarios.leitura.impl.LerUsuarios;
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
import java.util.List;
import java.util.Objects;

public class SaldosController extends ControllerAbstrato {

    @FXML
    private ListView<String> saldosListView;

    private String grupoValue;

    public void setGrupoValue(String grupoValue) {
        this.grupoValue = grupoValue;
    }

    @FXML
    public void initialize() {
        LerDespesas lerDespesas = new LerDespesas();
        List<Despesa> despesas = lerDespesas.lerArquivo("despesas-grupo-" + this.grupoValue + ".xml");
        LerGrupos grupos = new LerGrupos();
        List<String> membros = grupos.lerArquivo("grupos.xml").stream()
                .filter(grupo -> grupo.getNome().equals(grupoValue))
                .findFirst()
                .map(Grupo::getMembros)
                .orElse(null);
        if (Objects.nonNull(despesas) && Objects.nonNull(membros)){
            calcularSaldos(membros, despesas);
        }

    }

    @FXML
    protected void onSimplificarButtonClick(ActionEvent event) throws IOException {

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
        janela.show();
    }

    private void calcularSaldos(List<String> membros, List<Despesa> despesas) {
        for(String membro : membros){
            double totalPago = despesas.stream()
                    .filter(despesa -> despesa.getPagante().getNome().equals(membro))
                    .mapToDouble(despesa -> despesa.getPagante().getSaldo())
                    .sum();
            double totalDivida = 0;
            for(Despesa d : despesas){
                for(Usuario u : d.getDevedores()){
                    if(u.getNome().equals(membro) && !u.getNome().equals(d.getPagante().getNome())){
                        totalDivida -= u.getSaldo();
                    }
                }
            }
            if(totalPago - totalDivida > 0){
                saldosListView.getItems().add(membro + " recebe de volta R$" + (totalPago - totalDivida) + " no total");
            } else if(totalPago - totalDivida < 0){
                saldosListView.getItems().add(membro + " deve R$" + (totalDivida - totalPago) + " no total");
            } else {
                saldosListView.getItems().add(membro + " não tem dívidas");
            }
        }
    }

}
