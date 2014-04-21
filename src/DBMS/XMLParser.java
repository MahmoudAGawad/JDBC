package DBMS;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {

	public static String path = System.getenv().get("TEMP")+"/JDBC";

	@SuppressWarnings("unused")
	private void update(String DataBase, String TableName,
			ArrayList<Col> fields) throws ParserConfigurationException,
			TransformerException {

		try {
			File file = new File(path + "/" + DataBase + "_" + TableName
					+ ".xml");
			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createTable(DataBase, TableName, fields);

	}

	public void createTable(String DataBase, String TableName,
			ArrayList<Col> fields) throws ParserConfigurationException,
			TransformerException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("table");
		rootElement.setAttribute("name", TableName);
		doc.appendChild(rootElement);
		// staff elements
		for (Col col : fields) {
			Element field = doc.createElement("field");
			rootElement.appendChild(field);
			// //

			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(col.getFieldName()));
			field.appendChild(name);

			Element type = doc.createElement("type");
			type.appendChild(doc.createTextNode(col.getFieldType().toUpperCase()));
			field.appendChild(type);
			
			Element auto_increment = doc.createElement("auto-increment");
			if(col.getAutoIncrement())
				auto_increment.appendChild(doc.createTextNode("yes"));
			else
				auto_increment.appendChild(doc.createTextNode("no"));
			field.appendChild(auto_increment);
			
			Element auto_increment_counter = doc.createElement("auto-increment-counter");
			
			auto_increment_counter.appendChild(doc.createTextNode(col.get_auto_increment()+""));
			field.appendChild(auto_increment_counter);
			
			Element seachable = doc.createElement("searchable");
			if(col.getIsSearchable())
				seachable.appendChild(doc.createTextNode("yes"));
			else
				seachable.appendChild(doc.createTextNode("no"));
			field.appendChild(seachable);
			
			Element nullable = doc.createElement("nullable");
			if(col.getIsNullable())
				nullable.appendChild(doc.createTextNode("yes"));
			else
				nullable.appendChild(doc.createTextNode("no"));
			field.appendChild(nullable);
			
			Element writable = doc.createElement("writable");
			if(col.getIsWritable())
				writable.appendChild(doc.createTextNode("yes"));
			else
				writable.appendChild(doc.createTextNode("no"));
			field.appendChild(writable);
			
			Element primary_key = doc.createElement("primary-key");
			if(col.getPrimaryKey())
				primary_key.appendChild(doc.createTextNode("yes"));
			else
				primary_key.appendChild(doc.createTextNode("no"));
			field.appendChild(primary_key);
			
			

			// //////
			for (int j = 0; j < col.getElements().size(); j++) {
				Element element = doc.createElement("element");
				element.appendChild(doc.createTextNode(col.getElements().get(j)));
				field.appendChild(element);

			}
		}

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path + "\\" + DataBase
				+ "_" + TableName + ".xml"));
		transformer.transform(source, result);

		SchemaParser ob = new SchemaParser();

		ob.addTable(DataBase, TableName, new Tables(fields));

	}

	private ArrayList<Col> returnedarr = new ArrayList<>();

	public  ArrayList<Col> load(String databaseName, String table) {

		DTDValidator ob = new DTDValidator();

		if (ob.validate(databaseName + "_" + table) == false) {
			System.out.println("table format not correct");
			return null;

		} else {

			try {

				File fXmlFile = new File(path + "/" + databaseName + "_"
						+ table + ".xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				doc.getDocumentElement().normalize();

				// System.out.println("Root element :" +
				// doc.getDocumentElement().getNodeName());

				NodeList nList = doc.getElementsByTagName("field");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Col obj = new Col();
					ArrayList<String> elements = new ArrayList<>();

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						obj.setFieldName(eElement.getElementsByTagName("name")
								.item(0).getTextContent());
						obj.setFieldType(eElement.getElementsByTagName("type")
								.item(0).getTextContent());
						
						String booleans = eElement.getElementsByTagName("nullable")
								.item(0).getTextContent();
						
						if(booleans.equalsIgnoreCase("yes")){
							obj.setNullAble(true);
						}else
							obj.setNullAble(false);
						
						
						booleans = eElement.getElementsByTagName("searchable")
								.item(0).getTextContent();
						
						if(booleans.equalsIgnoreCase("yes")){
							obj.setSearchAble(true);
						}else
							obj.setSearchAble(false);
						
						booleans = eElement.getElementsByTagName("primary-key")
								.item(0).getTextContent();
						
						if(booleans.equalsIgnoreCase("yes")){
							obj.setPrimaryKey(true);
						}else
							obj.setPrimaryKey(false);
						
						booleans = eElement.getElementsByTagName("auto-increment")
								.item(0).getTextContent();
						
						if(booleans.equalsIgnoreCase("yes")){
							obj.setAutoIncrement(true);
						}else
							obj.setAutoIncrement(false);
						
						booleans = eElement.getElementsByTagName("writable")
								.item(0).getTextContent();
						
						if(booleans.equalsIgnoreCase("yes")){
							obj.setWritable(true);
						}else
							obj.setWritable(false);
						
						booleans = eElement.getElementsByTagName("auto-increment-counter")
								.item(0).getTextContent();
						obj.set_auto_increment(Integer.parseInt(booleans));
						
						
						NodeList nList2 = eElement
								.getElementsByTagName("element");

						// System.out.println(nList2.);

						for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {

							Node nNode2 = nList2.item(temp2);

							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								Element eElement2 = (Element) nNode2;
								elements.add(eElement2.getTextContent());

							}
						}
						// rrr.add(eElement.getElementsByTagName("element").item(1).getTextContent());

						obj.setElements(elements);

						returnedarr.add(obj);
						// rrr.clear();

					}
				}

			} catch (Exception e) {
				return null;
			}

			return returnedarr;
		}
	}

}