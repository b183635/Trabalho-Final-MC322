package com.example.splitza.utilitarios.leitura.impl;

import com.example.splitza.model.Usuario;
import com.example.splitza.model.UsuarioAbstrato;
import com.example.splitza.model.UsuarioLogado;
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

public class LerUsuarios implements I_Arquivo<UsuarioAbstrato> {
    @Override
    public List<UsuarioAbstrato> lerArquivo(String path) {
        List<UsuarioAbstrato> usuarios = new ArrayList<>();
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
                double saldo = Double.parseDouble(usuarioElement.getElementsByTagName("saldo").item(0).
                        getTextContent());

                UsuarioAbstrato user;
                if (usuarioElement.getElementsByTagName("email").getLength() > 0 &&
                    usuarioElement.getElementsByTagName("senha").getLength() > 0) {
                    String email = usuarioElement.getElementsByTagName("email").item(0).
                            getTextContent();
                    String senha = usuarioElement.getElementsByTagName("senha").item(0).
                            getTextContent();
                    boolean logado = Boolean.parseBoolean(usuarioElement.getElementsByTagName("logado").item(0).
                            getTextContent());
                    user = UsuarioLogado.getInstance();
                    ((UsuarioLogado) user).setEmail(email);
                    ((UsuarioLogado) user).setSenha(senha);
                    ((UsuarioLogado) user).setLogado(logado);
                } else {
                    user = new Usuario(nome, saldo);
                }

                user.setNome(nome);
                user.setSaldo(saldo);

                usuarios.add(user);
            }
        } catch (Exception e) {
            return null;

        }
        return usuarios;
    }

    @Override
    public void gravarArquivo(String path, UsuarioAbstrato usuario) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("Usuario");
            document.appendChild(rootElement);

            Element nomeElement = document.createElement("nome");
            nomeElement.appendChild(document.createTextNode(usuario.getNome()));
            rootElement.appendChild(nomeElement);

            Element saldoElement = document.createElement("saldo");
            saldoElement.appendChild(document.createTextNode(String.valueOf(usuario.getSaldo())));
            rootElement.appendChild(saldoElement);

            if (usuario instanceof UsuarioLogado) {
                Element emailElement = document.createElement("email");
                emailElement.appendChild(document.createTextNode(((UsuarioLogado) usuario).getEmail()));
                rootElement.appendChild(emailElement);

                Element senhaElement = document.createElement("senha");
                senhaElement.appendChild(document.createTextNode(((UsuarioLogado) usuario).getSenha()));
                rootElement.appendChild(senhaElement);

                Element logadoElement = document.createElement("logado");
                logadoElement.appendChild(document.createTextNode(String.valueOf(((UsuarioLogado) usuario).isLogado())));
                rootElement.appendChild(logadoElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            return;
        }
    }
}
