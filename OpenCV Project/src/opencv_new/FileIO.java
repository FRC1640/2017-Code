package opencv_new;

import java.io.File;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileIO {
	private static DocumentBuilder builder;

	public static LinkedHashMap<String, String[]> getFilters(String filePath){
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
		
		try {
			builder = factory.newDocumentBuilder();
			File file = new File(filePath);
			document = builder.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Element root = document.getDocumentElement();
		NodeList filters = root.getElementsByTagName("filter");

		for(int i = 0; i < filters.getLength(); i++){
			if(filters.item(i).getNodeType() == Node.ELEMENT_NODE){
				Element filterData = (Element) filters.item(i);
				String name = filterData.getElementsByTagName("name").item(0).getTextContent();
				int size = new Integer(filterData.getElementsByTagName("numInputs").item(0).getTextContent());
				
				String[] inputs = new String[size];
				for(int j = 0; j < inputs.length; j++){
					inputs[j] = filterData.getElementsByTagName("input").item(j).getTextContent();
				}
				map.put(name, inputs);
			}

		}

		return map;
	}

}
