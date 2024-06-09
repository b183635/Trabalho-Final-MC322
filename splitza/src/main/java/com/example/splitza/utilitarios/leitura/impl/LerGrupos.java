package com.example.splitza.utilitarios.leitura.impl;

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

                Grupo grupo = new Grupo(nome, membrosGrupo);

                grupos.add(grupo);
            }
        } catch (Exception e) {
            return null;
        }
        return grupos;
    }

    @Override
    public void gravarArquivo(String path, Grupo grupo) {
        try {
            // Create the DocumentBuilderFactory and the DocumentBuilder
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Create a new XML document
            Document document = documentBuilder.newDocument();

            // Create the root element
            Element rootElement = document.createElement("Grupo");
            document.appendChild(rootElement);

            // Add the "nome" element
            Element nomeElement = document.createElement("nome");
            nomeElement.appendChild(document.createTextNode(grupo.getNome()));
            rootElement.appendChild(nomeElement);

            // Create the "membros" element
            Element membrosElement = document.createElement("membros");
            rootElement.appendChild(membrosElement);

            // Add a "membro" element for each member in the group
            for (String membro : grupo.getMembros()) {
                Element membroElement = document.createElement("membro");
                membroElement.appendChild(document.createTextNode(membro));
                membrosElement.appendChild(membroElement);
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