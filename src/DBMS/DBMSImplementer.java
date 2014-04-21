package DBMS;

import java.sql.Date;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class DBMSImplementer implements DBMS {

	private String result = "";

	@Override
	public void createDateBase(String name) {
		// TODO Auto-generated method stub
		SchemaParser obj = new SchemaParser();
		obj.addDataBase(name);

	}

	@Override
	public void createTable(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		// TODO Auto-generated method stub
		XMLParser obj = new XMLParser();
		ArrayList<Col> array = new ArrayList<>();
		for (int i = 0; i < entries.size(); i++) {
			Col temp = new Col(entries.get(i).getName(), entries.get(i)
					.getValue());
			temp.setAutoIncrement(entries.get(i).getAutoIncrement());
			temp.setNullAble(entries.get(i).getIsNullable());
			temp.setReadOnly(entries.get(i).getIsReadOnly());
			temp.setSearchAble(entries.get(i).getIsSearchable());
			temp.setWritable(entries.get(i).getIsWritable());
			temp.setPrimaryKey(entries.get(i).getPrimaryKey());
			array.add(temp);
		}
		try {
			obj.createTable(dataBaseName, tableName, array);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int insert(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col> table = obj.load(dataBaseName, tableName);

		// ArrayList<Col> table = intialize();
		if (table == null) {
			System.out.println("NULL TABLE");
			return 0;
		}
		boolean[] check = new boolean[table.size()];
		int len = table.get(0).getLen();
		for (int i = 0; i < table.size(); i++) {
			table.get(i).addElemByDefault();
		}
		for (int i = 0; i < entries.size(); i++) {
			// search for the name of the col
			String fieldName = entries.get(i).getName();
			for (int j = 0; j < table.size(); j++) {
				if (fieldName.equals(table.get(j).getFieldName())) {
					table.get(j).setElement(len, entries.get(i).getValue());
					check[j] = true;
				}
			}
		}
		try {
			obj.createTable(dataBaseName, tableName, table);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print(table);
		return 1;
	}

	@Override
	public Tables selectAll(String dataBaseName, String tableName) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col> array = obj.load(dataBaseName, tableName);

		// ArrayList<Col> array = intialize();
		if (array == null)
			return null;

		Tables table = new Tables(array);
		table.setTableName(tableName);
		return table;

	}

	@Override
	public Tables select(String dataBaseName, String tableName, Field entry) {
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col> table = obj.load(dataBaseName, tableName);
		// ArrayList<Col> table = intialize();
		if (table == null)
			return null;

		int indexOfConField = getIndexOfField(table, entry.getName());// to
																		// store
																		// the
																		// field
																		// of
																		// condition
																		// the

		String comparedVal = entry.getValue();// to store the compared value
		char relation = entry.getRelation();// to store the condition relation

		ArrayList<Col> array = new ArrayList<>();
		for (int i = 0; i < table.size(); i++) {
			array.add(new Col(table.get(i).getFieldName(), table.get(i)
					.getFieldType()));

		}

		for (int i = 0; i < table.get(0).getLen(); i++) {
			if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
					relation, table.get(indexOfConField).getFieldType())) {
				// printRaw(table, i);
				for (int j = 0; j < table.size(); j++) {
					array.get(j).addElem(table.get(j).get(i));
				}

			}
		}
		Tables t = new Tables(array);
		t.setTableName(tableName);
		return t;

	}

	@Override
	public int update(String dataBaseName, String tableName,
			ArrayList<Field> entries, Field entry) {
		int updated = 0;
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col> table = obj.load(dataBaseName, tableName);
		// ArrayList<Col> table = intialize();
		if (table == null)
			return 0;

		int indexOfConField = getIndexOfField(table, entry.getName());
		String comparedVal = entry.getValue();// to store the compared value
		char relation = entry.getRelation();// to store the condition relation

		for (int i = 0; i < table.get(0).getLen(); i++) {

			if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
					relation, table.get(indexOfConField).getFieldType())) {
				updateRaw(table, i, entries);
				updated++;
				
			}

		}
		try {
			obj.createTable(dataBaseName, tableName, table);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print(table);
		//System.out.println("Done :)");
		return updated;
	}

	@Override
	public int delete(String dataBaseName, String tableName, Field entry) {
		int deleted = 0;
		// get the array of col from load
		XMLParser obj = new XMLParser();
		ArrayList<Col> table = obj.load(dataBaseName, tableName);
		// ArrayList<Col> table = intialize();
		if (table == null)
			return 0;

		int indexOfConField = getIndexOfField(table, entry.getName());
		String comparedVal = entry.getValue();// to store the compared value
		char relation = entry.getRelation();// to store the condition relation

		for (int i = 0; i < table.get(0).getLen(); i++) {

			if (compareRelation(table.get(indexOfConField).get(i), comparedVal,
					relation, table.get(indexOfConField).getFieldType())) {
				deleteRaw(table, i);
				deleted++;
				i--;
			}

		}
		try {
			obj.createTable(dataBaseName, tableName, table);
			return deleted;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// print(table);
		return 0;
		// System.out.println("Done :)");
	}

	public void updateRaw(ArrayList<Col> table, int index,
			ArrayList<Field> entries) {

		for (int i = 0; i < entries.size(); i++) {
			for (int j = 0; j < table.size(); j++) {
				if (table.get(j).getFieldName()
						.equalsIgnoreCase(entries.get(i).getName())) {
					table.get(j).setElement(index, entries.get(i).getValue());
				}
			}
		}

	}

	public int getIndexOfField(ArrayList<Col> table, String fieldName) {

		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).getFieldName().equalsIgnoreCase(fieldName)) {
				return i;

			}
		}
		return 0;
	}

	public boolean compareRelation(String first, String second, char relation,
			String type) {
		if (relation == '>') {
			if(type.equalsIgnoreCase("date")){
				String[] date = first.split("/");
				Date date1 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				date = second.split("/");
				Date date2 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				return date1.compareTo(date2)>0;
			}else
			if (type.equalsIgnoreCase("String")||type.equalsIgnoreCase("varchar")) {
				return first.compareTo(second) > 0;
			}
			return Double.parseDouble(first) > Double.parseDouble(second);
		}
		if (relation == '<') {
			if(type.equalsIgnoreCase("date")){
				String[] date = first.split("/");
				Date date1 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				date = second.split("/");
				Date date2 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				return date1.compareTo(date2)<0;
			}else
			if (type.equalsIgnoreCase("String")||type.equalsIgnoreCase("varchar")) {
				return first.compareTo(second) < 0;
			}
			return Double.parseDouble(first) < Double.parseDouble(second);
		}
		if (relation == '=') {
			if(type.equalsIgnoreCase("date")){
				String[] date = first.split("/");
				Date date1 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				date = second.split("/");
				Date date2 = new Date(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
				return date1.compareTo(date2)==0;
			}else
			if (type.equalsIgnoreCase("String")||type.equalsIgnoreCase("varchar")) {
				return first.compareTo(second) == 0;
			}
			return Double.parseDouble(first) == Double.parseDouble(second);

		}
		return false;
	}

	public ArrayList<Col> intialize() {

		ArrayList<Col> array = new ArrayList<>();

		array.add(new Col("First Name", "string"));
		array.add(new Col("Last Name", "string"));
		array.add(new Col("Rank", "Integer"));

		return array;

	}

	public void printRaw(ArrayList<Col> table, int indexOfRaw) {
		for (int i = 0; i < table.size(); i++) {
			System.out.print(table.get(i).get(indexOfRaw));
			result += table.get(i).get(indexOfRaw);
			if (i < table.size() - 1) {
				System.out.print(" / ");
				result += " / ";

			}

		}
		System.out.println();
		result += "\n";

	}

	public String getResult() {
		return result;
	}

	public void deleteRaw(ArrayList<Col> table, int indexOfRaw) {
		for (int i = 0; i < table.size(); i++) {

			table.get(i).removeIndex(indexOfRaw);
		}

	}

	public void print(ArrayList<Col> table) {
		for (int i = 0; i < table.size(); i++) {
			System.out.print(table.get(i).getFieldName());
			if (i < table.size() - 1) {
				System.out.print(" / ");
			}

		}
		System.out.println();
		for (int i = 0; i < table.get(0).getLen(); i++) {
			printRaw(table, i);
		}
	}

}