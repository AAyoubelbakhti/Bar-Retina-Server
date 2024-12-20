package com.project;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Arrays;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class FuncsBar {

    public static void generarXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element rootElement = doc.createElement("productes");
            doc.appendChild(rootElement);

            List<ElementProducte> productes = Arrays.asList(
                new ElementProducte("1", "Cafè", 1.5, "Beguda calenta feta de grans de cafè.", "cafe.png", "Beguda"),
                new ElementProducte("2", "Te", 1.3, "Beguda calenta feta de fulles de te.", "te.png",  "Beguda"),
                new ElementProducte("3", "Refresc", 1.0, "Beguda freda amb gas.", "refresc.png",  "Beguda"),
                new ElementProducte("4", "Suc de taronja", 1.8, "Suc de taronja acabat d'esprémer.", "suc.png",  "Beguda"),
                new ElementProducte("5", "Cervesa", 2.5, "Beguda alcohòlica amb gas.", "cervesa.png",  "Beguda"),

                new ElementProducte("6", "Entrepà", 3.5, "Pa farcit d'ingredients variats.", "entrepà.png",  "Primer plat"),
                new ElementProducte("7", "Hamburguesa", 5.0, "Carn en pa amb enciam i tomàquet.", "hamburguesa.png",  "Primer plat"),
                new ElementProducte("8", "Amanida", 4.5, "Amanida fresca de vegetals.", "amanida.png",  "Primer plat"),
                new ElementProducte("9", "Pizza", 7.0, "Pizza amb salsa de tomàquet i formatge.", "pizza.png",  "Primer plat"),
                new ElementProducte("10", "Pasta", 6.5, "Pasta italiana amb salsa bolonyesa.", "pasta.png",  "Primer plat"),

                new ElementProducte("11", "Pastís", 3.0, "Dolç de xocolata.", "pastis.png",  "Reposteria"),
                new ElementProducte("12", "Galetes", 2.0, "Galetes dolces i cruixents.", "galetes.png",  "Reposteria"),
                new ElementProducte("13", "Croissant", 1.8, "Pa dolç d'origen francès.", "croissant.png",  "Reposteria"),
                new ElementProducte("14", "Dònuts", 2.2, "Donut de sucre i canyella.", "donuts.png",  "Reposteria"),
                new ElementProducte("15", "Gelat", 2.5, "Gelat de vainilla amb xocolata.", "gelat.png",  "Reposteria"),

                new ElementProducte("16", "Truita de patates", 2.5, "Truita de patates casolana.", "truita_patates.png", "Tapa"),
                new ElementProducte("17", "Calamars a la romana", 3.0, "Calamars a la romana fregits.", "calamars.png", "Tapa"),
                new ElementProducte("18", "Croquetes de pollastre", 2.8, "Croquetes casolanes de pollastre.", "croquetes_pollastre.png", "Tapa"),
                new ElementProducte("19", "Braves", 2.5, "Patates braves amb salsa picant.", "braves.png", "Tapa"),
                new ElementProducte("20", "Pa amb tomàquet", 1.5, "Pa amb tomàquet i oli d'oliva.", "pa_tomaquet.png", "Tapa"),
                
                new ElementProducte("21", "Pastís de fruita", 3.5, "Pastís amb fruita.", "pastis_fruita.png",  "Postre"),
                new ElementProducte("22", "Magdalena", 2.2, "Magdalena de xocolata.", "magdalena.png",  "Postre"),
                new ElementProducte("23", "Brownie", 2.8, "Brownie de xocolata.", "brownie.png",  "Postre"),
                new ElementProducte("24", "Xurros", 1.5, "Xurros amb sucre.", "xurros.png",  "Postre"),
                new ElementProducte("25", "Smoothie", 3.2, "Batut de fruites.", "smoothie.png",  "Postre")
            );

            for (ElementProducte producte : productes) {
                Element producteElement = doc.createElement("producte");

                Element id = doc.createElement("id");
                id.appendChild(doc.createTextNode(producte.id));
                producteElement.appendChild(id);

                Element nom = doc.createElement("nom");
                nom.appendChild(doc.createTextNode(producte.nom));
                producteElement.appendChild(nom);
                Element preu = doc.createElement("preu");
                preu.appendChild(doc.createTextNode(String.valueOf(producte.preu)));
                producteElement.appendChild(preu);

                Element descripcio = doc.createElement("descripcio");
                descripcio.appendChild(doc.createTextNode(producte.descripcio));
                producteElement.appendChild(descripcio);

                Element imatge = doc.createElement("imatge");
                imatge.appendChild(doc.createTextNode(producte.imatge));
                producteElement.appendChild(imatge);

                Element categoria = doc.createElement("categoria");
                categoria.appendChild(doc.createTextNode(producte.categoria));
                producteElement.appendChild(categoria);

                rootElement.appendChild(producteElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("PRODUCTES.XML"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String mostrarProductes() {
        try {
            File file = new File("PRODUCTES.XML");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList producteList = doc.getElementsByTagName("producte");
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < producteList.getLength(); i++) {
                Node producteNode = producteList.item(i);
                if (producteNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element producteElement = (Element) producteNode;
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("id", producteElement.getElementsByTagName("id").item(0).getTextContent());
                    jsonObject.put("nom", producteElement.getElementsByTagName("nom").item(0).getTextContent());
                    jsonObject.put("preu", producteElement.getElementsByTagName("preu").item(0).getTextContent());
                    jsonObject.put("descripcio", producteElement.getElementsByTagName("descripcio").item(0).getTextContent());
                    jsonObject.put("imatge", producteElement.getElementsByTagName("imatge").item(0).getTextContent());

                    jsonArray.put(jsonObject);
                }
            }

            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String mostrarTags(String categoria) {
        try {
            File file = new File("PRODUCTES.XML");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();
            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "//producte[categoria='" + categoria + "']"; 
            NodeList nodeList = (NodeList) xPath.evaluate(expression, doc, XPathConstants.NODESET);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node producteNode = nodeList.item(i);
                if (producteNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element producteElement = (Element) producteNode;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", producteElement.getElementsByTagName("id").item(0).getTextContent());
                    jsonObject.put("nom", producteElement.getElementsByTagName("nom").item(0).getTextContent());
                    jsonObject.put("preu", producteElement.getElementsByTagName("preu").item(0).getTextContent());
                    jsonObject.put("descripcio", producteElement.getElementsByTagName("descripcio").item(0).getTextContent());
                    jsonObject.put("imatge", producteElement.getElementsByTagName("imatge").item(0).getTextContent());
                    jsonArray.put(jsonObject);
                }
            }
    
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}