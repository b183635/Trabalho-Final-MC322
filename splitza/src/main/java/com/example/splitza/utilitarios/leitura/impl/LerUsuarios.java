package com.example.splitza.utilitarios.leitura.impl;

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

public class LerUsuarios implements I_Arquivo<Usuario> {
    @Override
    public List<Usuario> lerArquivo(String path) {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            File file = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Usuario");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element usuarioElement = (Element) nodeList.item(i);

                String nome = usuarioElement.getElementsByTagName("nome").item(0).
                        getTextContent();
                String email = usuarioElement.getElementsByTagName("email").item(0).
                        getTextContent();
                String senha = usuarioElement.getElementsByTagName("senha").item(0).
                        getTextContent();

                int saldo = Integer.parseInt(usuarioElement.getElementsByTagName("saldo").item(0).
                        getTextContent());

                Usuario user = new Usuario(nome, email, senha, saldo);

                usuarios.add(user);
            }
        } catch (Exception e) {
            System.err.println(" Erro ao ler o arquivo : " + e.getMessage());
            e.printStackTrace();

        }
        return usuarios;
    }

    @Override
    public void gravarArquivo(String path, Usuario usuario) {
        try {
            // Cria o DocumentBuilderFactory e o DocumentBuilder
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Cria um novo documento XML
            Document document = documentBuilder.newDocument();

            // Cria o elemento raiz
            Element rootElement = document.createElement("Usuario");
            document.appendChild(rootElement);

            // Adiciona o elemento "nome"
            Element nomeElement = document.createElement("nome");
            nomeElement.appendChild(document.createTextNode(usuario.getNome()));
            rootElement.appendChild(nomeElement);

            // Adiciona o elemento "nome"
            Element emailElement = document.createElement("email");
            emailElement.appendChild(document.createTextNode(usuario.getEmail()));
            rootElement.appendChild(emailElement);

            // Adiciona o elemento "nome"
            Element senhaElement = document.createElement("senha");
            senhaElement.appendChild(document.createTextNode(usuario.getSenha()));
            rootElement.appendChild(senhaElement);

            // Adiciona o elemento "saldo"
            Element saldoElement = document.createElement("saldo");
            saldoElement.appendChild(document.createTextNode(String.valueOf(usuario.getSaldo())));
            rootElement.appendChild(saldoElement);


            // Cria o TransformerFactory e o Transformer para transformar o documento em XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Cria a fonte DOM e o resultado Stream
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            // Transforma o documento em XML e grava no arquivo
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
