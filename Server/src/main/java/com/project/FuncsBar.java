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
                new ElementProducte("1", "Cafè", 1.5, "Beguda calenta feta de grans de cafè.", "cafe.png"),
                new ElementProducte("2", "Te", 1.3, "Beguda calenta feta de fulles de te.", "te.png"),
                new ElementProducte("3", "Refresc", 1.0, "Beguda freda amb gas.", "refresc.png"),
                new ElementProducte("4", "Suc de taronja", 1.8, "Suc de taronja acabat d'esprémer.", "suc.png"),
                new ElementProducte("5", "Cervesa", 2.5, "Beguda alcohòlica amb gas.", "cervesa.png"),

                new ElementProducte("6", "Entrepà", 3.5, "Pa farcit d'ingredients variats.", "entrepà.png"),
                new ElementProducte("7", "Hamburguesa", 5.0, "Carn en pa amb enciam i tomàquet.", "hamburguesa.png"),
                new ElementProducte("8", "Amanida", 4.5, "Amanida fresca de vegetals.", "amanida.png"),
                new ElementProducte("9", "Pizza", 7.0, "Pizza amb salsa de tomàquet i formatge.", "pizza.png"),
                new ElementProducte("10", "Pasta", 6.5, "Pasta italiana amb salsa bolonyesa.", "pasta.png"),

                new ElementProducte("11", "Pastís", 3.0, "Dolç de xocolata.", "pastis.png"),
                new ElementProducte("12", "Galetes", 2.0, "Galetes dolces i cruixents.", "galetes.png"),
                new ElementProducte("13", "Croissant", 1.8, "Pa dolç d'origen francès.", "croissant.png"),
                new ElementProducte("14", "Dònuts", 2.2, "Donut de sucre i canyella.", "donuts.png"),
                new ElementProducte("15", "Gelat", 2.5, "Gelat de vainilla amb xocolata.", "gelat.png"),

                new ElementProducte("16", "Aigua", 0.8, "Aigua mineral embotellada.", "aigua.png"),
                new ElementProducte("17", "Sandvitx", 3.0, "Pa amb pernil i formatge.", "sandvitx.png"),
                new ElementProducte("18", "Empanada", 2.5, "Empanada farcida de carn.", "empanada.png"),
                new ElementProducte("19", "Patates fregides", 1.5, "Patates fregides cruixents.", "patates.png"),
                new ElementProducte("20", "Nachos", 3.5, "Nachos amb formatge fos.", "nachos.png"),

                new ElementProducte("21", "Pastís de fruita", 3.5, "Pastís amb fruita.", "pastis_fruita.png"),
                new ElementProducte("22", "Magdalena", 2.2, "Magdalena de xocolata.", "magdalena.png"),
                new ElementProducte("23", "Brownie", 2.8, "Brownie de xocolata.", "brownie.png"),
                new ElementProducte("24", "Xurros", 1.5, "Xurros amb sucre.", "xurros.png"),
                new ElementProducte("25", "Smoothie", 3.2, "Batut de fruites.", "smoothie.png")
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
    public static List<String> mostrarTags(String tag) {
        List<String> elementsList = new ArrayList<>();
        try {
            File file = new File("PRODUCTES.XML");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expression = xPath.compile("//" + tag);
            NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                elementsList.add(nodeList.item(i).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elementsList;
    }
}