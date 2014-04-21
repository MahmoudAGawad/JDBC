package DBMS;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SchemaParser {

	public static String rootPath = XMLParser.path;

	public void CreateDataBase(String DataBase, ArrayList<Tables> tables) { 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("database");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", DataBase);

			if (tables != null)
				for (Tables tb : tables) {
					Element table = doc.createElement("table");

					Element name = doc.createElement("name");
					name.setTextContent(tb.getTableName());
					table.appendChild(name);

					ArrayList<Col> fields = tb.getFields();
					for (Col field : fields) {
						Element e = doc.createElement("field");

						Element nm = doc.createElement("name");
						nm.setTextContent(field.getFieldName());
						e.appendChild(nm);

						Element type = doc.createElement("type");
						type.setTextContent(field.getFieldType().toUpperCase());
						e.appendChild(type);
						
						
						
						Element auto_increment = doc.createElement("auto-increment");
						if(field.getAutoIncrement())
							auto_increment.appendChild(doc.createTextNode("yes"));
						else
							auto_increment.appendChild(doc.createTextNode("no"));
						e.appendChild(auto_increment);
						
						Element seachable = doc.createElement("searchable");
						if(field.getIsSearchable())
							seachable.appendChild(doc.createTextNode("yes"));
						else
							seachable.appendChild(doc.createTextNode("no"));
						e.appendChild(seachable);
						
						Element nullable = doc.createElement("nullable");
						if(field.getIsNullable())
							nullable.appendChild(doc.createTextNode("yes"));
						else
							nullable.appendChild(doc.createTextNode("no"));
						e.appendChild(nullable);
						
						Element writable = doc.createElement("writable");
						if(field.getIsNullable())
							writable.appendChild(doc.createTextNode("yes"));
						else
							writable.appendChild(doc.createTextNode("no"));
						e.appendChild(writable);
						
						Element primary_key = doc.createElement("primary-key");
						if(field.getPrimaryKey())
							primary_key.appendChild(doc.createTextNode("yes"));
						else
							primary_key.appendChild(doc.createTextNode("no"));
						e.appendChild(primary_key);
						
						
						
						
						table.appendChild(e);
					}
					rootElement.appendChild(table);

				}
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath +"/"+DataBase + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}
	}

	public ArrayList<Tables> loadDataBase(String databaseName) { // abanub
		//System.out.println("Successfully");
		ArrayList<Tables> returnDataBases = null;
		try {
			
			
			File fXmlFile = new File(rootPath +"/"+ databaseName+".xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			returnDataBases = new ArrayList<Tables>();
			NodeList database = doc.getElementsByTagName("table");
			for (int i = 0; i < database.getLength(); i++) {
				Tables tb = new Tables();

				Node node = database.item(i);
				Element e = (Element) node;
				tb.setTableName(e.getElementsByTagName("name").item(0)
						.getTextContent());

				NodeList fields = e.getElementsByTagName("field");

				ArrayList<Col> fieldsCol = new ArrayList<Col>();
				for (int j = 0; j < fields.getLength(); j++) {
					Col field = new Col();
					Node node2 = fields.item(j);
					Element e2 = (Element) node2;

					field.setFieldName(e2.getElementsByTagName("name").item(0)
							.getTextContent());
					field.setFieldType(e2.getElementsByTagName("type").item(0)
							.getTextContent());
					
					String booleans = e2.getElementsByTagName("nullable")
							.item(0).getTextContent();
					
					if(booleans.equalsIgnoreCase("yes")){
						field.setNullAble(true);
					}else
						field.setNullAble(false);
					
					
					booleans = e2.getElementsByTagName("searchable")
							.item(0).getTextContent();
					
					if(booleans.equalsIgnoreCase("yes")){
						field.setSearchAble(true);
					}else
						field.setSearchAble(false);
					
					booleans = e2.getElementsByTagName("primary-key")
							.item(0).getTextContent();
					
					if(booleans.equalsIgnoreCase("yes")){
						field.setPrimaryKey(true);
					}else
						field.setPrimaryKey(false);
					
					booleans = e2.getElementsByTagName("auto-increment")
							.item(0).getTextContent();
					
					if(booleans.equalsIgnoreCase("yes")){
						field.setAutoIncrement(true);
					}else
						field.setAutoIncrement(false);
					
					booleans = e2.getElementsByTagName("writable")
							.item(0).getTextContent();
					
					if(booleans.equalsIgnoreCase("yes")){
						field.setWritable(true);
					}else
						field.setWritable(false);
					
					
					
					

					fieldsCol.add(field);
				}

				tb.setFields(fieldsCol);
				returnDataBases.add(tb);
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No DataBase \""+databaseName+"\" Found");
		}
		return returnDataBases;
	}

	public boolean addTable(String DataBase, String TableName, Tables tb) { // waleed
		
		tb.setTableName(TableName);
		ArrayList<Tables> tables = loadDataBase(DataBase);
		
		boolean f=true;
		for(Tables tab : tables){
			if(tab.getTableName().equalsIgnoreCase(TableName)){
				f=false;
				break;
			}
		}
		if(f){
		tables.add(tb);
		
		CreateDataBase(DataBase, tables);
		
		new DTDGenerator(rootPath+"/"+DataBase+"_"+TableName);
		}
		return true;
	}

	public void addDataBase(String DataBase) { // mostafa-diaa

		ArrayList<String> databases = loadRoot();
		databases.add(DataBase);
		updateRoot(databases);
		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("database");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", DataBase);

			
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath +"/"+DataBase + ".xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}

	}

	private void updateRoot(ArrayList<String> databases) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("databases");
			doc.appendChild(rootElement);

			for (String db : databases) {
				Element e = doc.createElement("database");
				e.setTextContent(db);
				rootElement.appendChild(e);
			}

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(rootPath
					+ "/root.xml"));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "DataBase Not Created");
		}
	}

	public ArrayList<String> loadRoot() { // abanub
		ArrayList<String> returnDataBases = null;
		try {

			File fXmlFile = new File(rootPath + "/root.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			returnDataBases = new ArrayList<String>();
			NodeList database = doc.getElementsByTagName("database");
			for (int i = 0; i < database.getLength(); i++) {
				Node node = database.item(i);
				Element e = (Element) node;
				returnDataBases.add(e.getTextContent());
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No Root-DataBase Found");
		}
		return returnDataBases;
	}

}
