package com.example.splitza.utilitarios.leitura.impl;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.I_Arquivo;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LerDespesas implements I_Arquivo<Despesa> {

    public List<Despesa> lerArquivo(String path) {
        List<Despesa> despesas = new ArrayList<>();
        try {
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Despesa");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element despesaElement = (Element) nodeList.item(i);

                String nome = despesaElement.getElementsByTagName("nome").item(0).getTextContent();
                String nomeGrupo = despesaElement.getElementsByTagName("nomeGrupo").item(0).getTextContent();
                String data = despesaElement.getElementsByTagName("data").item(0).getTextContent();
                double valor = Double.parseDouble(despesaElement.getElementsByTagName("valor").item(0).getTextContent());

                String nomePagante = despesaElement.getElementsByTagName("nome").item(0).getTextContent();
                double saldoPagante = Double.parseDouble(despesaElement.getElementsByTagName("saldo").item(0).getTextContent());
                Usuario pagante = new Usuario(nomePagante, saldoPagante);

                NodeList devedoresNodeList = despesaElement.getElementsByTagName("devedor");
                List<Usuario> devedores = new ArrayList<>();
                for (int j = 0; j < devedoresNodeList.getLength(); j++) {
                    Element devedorElement = (Element) devedoresNodeList.item(j);
                    String nomeDevedor = devedorElement.getElementsByTagName("nome").item(0).getTextContent();
                    double saldoDevedor = Double.parseDouble(devedorElement.getElementsByTagName("saldo").item(0).getTextContent());
                    devedores.add(new Usuario(nomeDevedor, saldoDevedor));
                }

                Despesa despesa = new Despesa(nome, nomeGrupo, data, valor, pagante, devedores);
                despesas.add(despesa);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return despesas;
    }

    public void gravarArquivo(String path, Despesa d) {
        File file = new File(path);
        List<Despesa> despesas = new ArrayList<>();

        if(file.exists()){
            despesas = lerArquivo(path);
        }
        despesas.add(d);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("Despesas");
            document.appendChild(rootElement);

            for (Despesa despesa : despesas){
                Element despesaElement = document.createElement("Despesa");
                rootElement.appendChild(despesaElement);

                Element nomeElement = document.createElement("nome");
                nomeElement.appendChild(document.createTextNode(despesa.getNome()));
                despesaElement.appendChild(nomeElement);

                Element nomeGrupoElement = document.createElement("nomeGrupo");
                nomeGrupoElement.appendChild(document.createTextNode(despesa.getNomeGrupo()));
                despesaElement.appendChild(nomeGrupoElement);

                Element dataElement = document.createElement("data");
                dataElement.appendChild(document.createTextNode(despesa.getData()));
                despesaElement.appendChild(dataElement);

                Element valorElement = document.createElement("valor");
                valorElement.appendChild(document.createTextNode(String.valueOf(despesa.getValor())));
                despesaElement.appendChild(valorElement);

                Element paganteElement = document.createElement("pagante");

                Element nomePaganteElement = document.createElement("nome");
                nomePaganteElement.appendChild(document.createTextNode(despesa.getPagante().getNome()));
                paganteElement.appendChild(nomePaganteElement);

                Element saldoPaganteElement = document.createElement("saldo");
                saldoPaganteElement.appendChild(document.createTextNode(String.valueOf(despesa.getPagante().getSaldo())));
                paganteElement.appendChild(saldoPaganteElement);

                despesaElement.appendChild(paganteElement);

                Element devedoresElement = document.createElement("devedores");
                for (Usuario devedor : despesa.getDevedores()) {
                    Element devedorElement = document.createElement("devedor");

                    Element nomeDevedorElement = document.createElement("nome");
                    nomeDevedorElement.appendChild(document.createTextNode(devedor.getNome()));
                    devedorElement.appendChild(nomeDevedorElement);

                    Element saldoElement = document.createElement("saldo");
                    saldoElement.appendChild(document.createTextNode(String.valueOf(devedor.getSaldo())));
                    devedorElement.appendChild(saldoElement);

                    devedoresElement.appendChild(devedorElement);
                }
                despesaElement.appendChild(devedoresElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
