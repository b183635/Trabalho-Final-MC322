package com.example.splitza.utilitarios.leitura.impl;

import com.example.splitza.model.Despesa;
import com.example.splitza.model.Grupo;
import com.example.splitza.model.Usuario;
import com.example.splitza.utilitarios.leitura.I_Arquivo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LerGrupos implements I_Arquivo<Grupo> {
    @Override
    public List<Grupo> lerArquivo(String path) {
        List<Grupo> grupos = new ArrayList<>();
        try {
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Grupo");
            for (int i = 0; i < nodeList.getLength(); i++) {
                List<String> membrosGrupo = new ArrayList<>();
                Element grupoElement = (Element) nodeList.item(i);

                String nome = grupoElement.getElementsByTagName("nome").item(0).getTextContent();

                NodeList membros = ((Element) grupoElement.getElementsByTagName("membros").item(0)).getElementsByTagName("membro");
                for (int j = 0; j < membros.getLength(); j++) {
                    Element item = (Element) membros.item(j);
                    membrosGrupo.add(item.getTextContent());
                }

                Grupo grupo = new Grupo.GrupoBuilder()
                        .setNome(nome)
                        .setMembros(membrosGrupo)
                        .setDespesas(new ArrayList<>())
                        .build();

                grupos.add(grupo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return grupos;
    }

    @Override
    public void gravarArquivo(String path, Grupo grupo) {
        File file = new File(path);
        List<Grupo> grupos = new ArrayList<>();

        if(file.exists()){
            grupos = lerArquivo(path);
        }
        grupos.add(grupo);

        try {
            // Create the DocumentBuilderFactory and the DocumentBuilder
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Create a new XML document
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("Grupos");
            document.appendChild(rootElement);

            for(Grupo g : grupos){
                Element grupoElement = document.createElement("Grupo");
                rootElement.appendChild(grupoElement);

                Element nomeElement = document.createElement("nome");
                nomeElement.appendChild(document.createTextNode(g.getNome()));
                grupoElement.appendChild(nomeElement);

                Element membrosElement = document.createElement("membros");
                grupoElement.appendChild(membrosElement);

                for(String membro : g.getMembros()){
                    Element membroElement = document.createElement("membro");
                    membroElement.appendChild(document.createTextNode(membro));
                    membrosElement.appendChild(membroElement);
                }
            }

            // Create the TransformerFactory and the Transformer to transform the document into XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Create the DOM source and the Stream result
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            // Transform the document into XML and write it to the file
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}