package opencv_gui;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileGenerator {
	private static FileGenerator fileGeneratorLinux, fileGeneratorWindows;
	private final String filePathWindows ="C:\\Users\\Laura\\Desktop\\test.xml";
	private String filePathLinux;//FilterManager.xmlFilePath;//"/home/pi/OpenCV/test.xml";
	private final String filePathNew = "C:\\Users\\Laura\\Desktop\\test2.xml";

	private ArrayList<Integer> allInputs;
	private boolean read = false;
	private DocumentBuilder builder;
	private String filePath;
	
	private FileGenerator(boolean windows){
		allInputs = new ArrayList<Integer>();
		filePathLinux = FilterManager.gear ? FilterManager.xmlFileGear : FilterManager.xmlFileBoiler;
		filePath = windows ? filePathWindows : filePathLinux;

		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static FileGenerator getInstance(boolean windows){
		if(windows && fileGeneratorWindows == null)
			fileGeneratorWindows = new FileGenerator(true);
		else if(!windows && fileGeneratorLinux == null)
			fileGeneratorLinux = new FileGenerator(false);
		return windows ? fileGeneratorWindows : fileGeneratorLinux;
	}
	
	public void createFile(ArrayList<Filter> filters){
		System.out.println("in createFile");
		Document newDoc = builder.newDocument();
		Element root = newDoc.createElement("vision");
		newDoc.appendChild(root);
		
		int sliderIndex = 0;
		for(Filter filter : filters){
			Element filterElement = newDoc.createElement("filter");
			root.appendChild(filterElement);
			
			Element nameElement = newDoc.createElement("name");
			nameElement.setTextContent(filter.toString().substring(0, filter.toString().indexOf(":")));
			filterElement.appendChild(nameElement);
			
			int[] inputs = filter.getInputs();
			Element numInputElement = newDoc.createElement("numInputs");
			numInputElement.setTextContent(new Integer(inputs.length).toString());
			filterElement.appendChild(numInputElement);
			
			for(int i = 0; i < inputs.length; i++){
				Element inputElement = newDoc.createElement("input");
				inputElement.setTextContent(new Integer(inputs[i]).toString());
				inputElement.setAttribute("sliderName", getSliderNames().get(sliderIndex));
				filterElement.appendChild(inputElement);
				sliderIndex++;
			}
		}
		
		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(newDoc);
			StreamResult result = new StreamResult(new File(filePathNew));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public ArrayList<Filter> loadFilters(){
		ArrayList<String> file = readFile();
		ArrayList<Filter> filters = new ArrayList<Filter>();
		int i = 0;
		
		do{
			String filter = file.get(i);
			int numInputs = Integer.parseInt(file.get(i + 1));
			int[] inputs = new int[numInputs];

			int j = 0;
			i += 2;
			while(!file.get(i).equals(";")){
				inputs[j] = Integer.parseInt(file.get(i));
				allInputs.add(Integer.parseInt(file.get(i)));
				j++;
				i += 2;
			}
			for(int k = j; k < numInputs; k++){
				allInputs.add(0);
			}
			createFilter(filter, inputs, filters);
			i++;
		}while(i < file.size());
		for(Filter f : filters)
			System.out.println(f);
		read = true;
		return filters;
	}
	
	public ArrayList<String> getSliderNames(){
		ArrayList<String> file = readFile();
		ArrayList<String> names = new ArrayList<String>();
		boolean numInputs = true;
		String prefix = "slider_";
		for(int i = 0; i < file.size(); i++){
			if(file.get(i).startsWith(prefix)){
				names.add(file.get(i).substring(prefix.length()));
			}
		}
		return names;
	}
	
	public ArrayList<Integer> getInputs(){
		if(!read)
			loadFilters();
		return allInputs;
	}
	
	
	private void createFilter(String filter, int[] inputs, ArrayList<Filter> filters){
		switch(filter){
			case "HSL": 
				filters.add(new HSLThreshold(inputs));
				break;	
			case "Dilate":
				filters.add(new Dilate(inputs));
				break;
			case "FilterContours":
				filters.add(new FilterContours(inputs));
				break;
			case "GetImage":
				filters.add(new GetImage());
				break;
			case "Close":
				filters.add(new Close(inputs));
				break;
			default: 
				break;
		}
	}
	
	private ArrayList<String> readFile(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//DocumentBuilder builder = null;
		Document document = null;
		
		try {
			System.out.println("File Path: " + filePath);
			File file = new File(filePath);
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element root = document.getDocumentElement();
		NodeList filters = root.getElementsByTagName("filter");
		ArrayList<String> file = new ArrayList<String>();
		
		for(int i = 0; i < filters.getLength(); i++){
			Node filterNode = filters.item(i);
			if(filterNode.getNodeType() == Node.ELEMENT_NODE){
				Element filterElement = (Element) filterNode;
				NodeList inputNodes = filterElement.getElementsByTagName("input");
				file.add(filterElement.getElementsByTagName("name").item(0).getTextContent());
				file.add(filterElement.getElementsByTagName("numInputs").item(0).getTextContent());
				
				for(int j = 0; j < inputNodes.getLength(); j++){
					Node inputNode = inputNodes.item(j);
					if(inputNode.getNodeType() == Node.ELEMENT_NODE){
						Element inputElement = (Element) inputNode;
						file.add(inputElement.getTextContent());
						file.add("slider_" + inputElement.getAttribute("sliderName"));					}
				}
				file.add(";");
			}
		}
		return file;
	}
	
}
