package main;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	public static void main(String[] args){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		
		try {
			File file = new File("C:\\Users\\Laura\\Desktop\\test.xml");
			System.out.println(file.length());
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File("C:\\Users\\Laura\\Desktop\\test.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element root = document.getDocumentElement();
		System.out.println(root.getNodeName());
		NodeList filters = root.getElementsByTagName("filter");
		ArrayList<String> inputs = new ArrayList<String>();
		
		for(int i = 0; i < filters.getLength(); i++){
			Node filterNode = filters.item(i);
			if(filterNode.getNodeType() == Node.ELEMENT_NODE){
				Element filterElement = (Element) filterNode;
				//System.out.println("Name: " + filterElement.getElementsByTagName("name").item(0).getTextContent());
				NodeList inputNodes = filterElement.getElementsByTagName("input");
				inputs.add(filterElement.getElementsByTagName("name").item(0).getTextContent());
				for(int j = 0; j < inputNodes.getLength(); j++){
					Node inputNode = inputNodes.item(j);
					if(inputNode.getNodeType() == Node.ELEMENT_NODE){
						Element inputElement = (Element) inputNode;
						//System.out.println("Input: " + inputElement.getTextContent());
						inputs.add(inputElement.getTextContent());
					}
				}
				inputs.add(";");
			}
		}
		
		for(String s : inputs){
			System.out.println(s);
		}

	}
	
}
