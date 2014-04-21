package DBMS;

import java.util.*;

import javax.swing.plaf.SliderUI;

public class Validator {

	// returns true if the database name exists

	public boolean check_dataBase(String dataBaseName) {

		SchemaParser ob = new SchemaParser();
		return ob.loadRoot().contains(dataBaseName);
	}

	// returns true if the table exists in a certain database

	private boolean check_table(String dataBaseName, String tableName) {
		SchemaParser ob = new SchemaParser();
		ArrayList<Tables> db = ob.loadDataBase(dataBaseName);

		if (db != null)
			for (Tables tb : db) {
				if (tb.getTableName().equalsIgnoreCase(tableName))
					return true;
			}
		return false;

	}

	// returns true if the table's data type matches the entry's data type

	private boolean check_type(String dataBaseName, String tableName,
			String entryValue, String entryName) {
		SchemaParser ob = new SchemaParser();
		ArrayList<Tables> tablesList = ob.loadDataBase(dataBaseName);

		if (tablesList != null)
			for (Tables tb : tablesList) {
				if (tb.getTableName().equals(tableName)) {
					ArrayList<Col> fields = tb.getFields();
					for (Col field : fields) {
						if (field.getFieldName().equals(entryName)) {
							String temp = field.getFieldType().toLowerCase();
							switch (temp) {

							case "smallint":
								return check_int(entryValue);
							case "bigint":
								return check_int(entryValue);
							case "tinyint":
								return check_int(entryValue);
							case "integer":
								return check_int(entryValue);

							case "double":
								return check_double(entryValue);
							case "float":
								return check_double(entryValue);
							case "decimal":
								return check_double(entryValue);
							case "real":
								return check_double(entryValue);

							case "bit":
								return check_boolean(entryValue);

							case "array":
								return true;

							case "date":
								return check_date(entryValue);
								
							case "varchar":
								return true;
							
							case "string":
								return true;
								
							default:
								return false;
							}

						}
					}
				}
			}
		return false;

	}


	private boolean check_date(String str) {
		if (!str.contains("/"))
			return false;

		StringTokenizer s = new StringTokenizer(str, "/");
		int year = -1, month = -1, day = -1;
		while (s.hasMoreTokens()) {
			String value = s.nextToken();
			if (!check_int(value))
				return false;

			if (day == -1) {

				day = Integer.parseInt(value);
				if (day < 1)
					return false;
			} else if (month == -1) {
				month = Integer.parseInt(value);
				if (month < 1)
					return false;
			} else if (year == -1) {
				year = Integer.parseInt(value);
				if (year < 1)
					return false;
			} else {
				return false;
			}
		}
		return true;

	}

	private boolean check_boolean(String str) {
		if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")
				|| str.equalsIgnoreCase("0") || str.equalsIgnoreCase("1"))
			return true;
		return false;
	}

	private boolean check_int(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean check_double(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// checks if the new database name already exists, if not it calls dbms to
	// create it

	public void validate_createDateBase(String name) {
		// check if database found
		if (check_dataBase(name))
			System.out.println("database already exists!");
		else {
			System.out.print("Creating DataBase ");
			for(int i = 0 ; i < 5 ; i++){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print(".");
			}

			DBMS ob = new DBMSImplementer();
			ob.createDateBase(name);
			System.out.println("\nDataBase Created !!");
		}
	}

	// checks if the new table name already exists if not it calls dbms to
	// create it

	public void validate_createTable(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName))
				System.out.println("table already exist!");
			else {
				System.out.print("Creating Table ");
				for(int i = 0 ; i < 5 ; i++){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.print(".");
				}
				DBMS ob = new DBMSImplementer();
				ob.createTable(dataBaseName, tableName, entries);
				System.out.println("\nTable Created !!");
			}
		} else
			System.out.println("database does not exist!");

	}

	// checks if database and table exist if true it calls dbms to insert an
	// entry
	public int validate_insert(String dataBaseName, String tableName,
			ArrayList<Field> entries) {

		boolean flag = true;

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				for (Field element : entries) {

					if (!check_type(dataBaseName, tableName,
							element.getValue(), element.getName())) {
						flag = false;
						break;

					}
				}

				if (flag) {
					if (validate_update_field(dataBaseName, tableName, entries)
							&& validate_insert_null(dataBaseName, tableName,entries)
									&& validate_primary_key(dataBaseName, tableName, entries)) {
						
						System.out.print("Inserting into Table ");
						for(int i = 0 ; i < 5 ; i++){
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.print(".");
						}
						DBMS ob = new DBMSImplementer();
						System.out.println("\nDone Inserting !!");
						return ob.insert(dataBaseName, tableName, entries);
					}
				} else {
					System.out
							.println("data type does not math the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
		
		return -2;
	}

	
	
	private boolean validate_primary_key(String dataBaseName, String tableName,
			ArrayList<Field> entries) {
		XMLParser ob = new XMLParser();
		ArrayList<Col> table = ob.load(dataBaseName, tableName);
		String name;
		
		for(Field entry : entries){
			name = entry.getName();
			for(Col field : table){
				if(name.equalsIgnoreCase(field.getFieldName())
						&&field.getPrimaryKey()){
					if(field.getElements().contains(entry.getValue())){
						System.out.println("Error: Duplicated value. This field -"+name+"- is primary key!");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	private boolean validate_insert_null(String dataBaseName, String tableName,
			ArrayList<Field> entries) {

		XMLParser ob = new XMLParser();
		ArrayList<Col> table = ob.load(dataBaseName, tableName);

		boolean final_return = true;
		ArrayList<String> invalid = new ArrayList<String>();
		for (Col field : table) {
			boolean check = true;
			for (Field entry : entries) {
				if (field.getFieldName().equalsIgnoreCase(entry.getName())) {
					check = false;
					break;
				}
			}

			if (check) {
				if (!field.getIsNullable()) {
					final_return = false;
					invalid.add(field.getFieldName());
				}
			}

		}

		if (!final_return) {
			if (invalid.size() == 1)
				System.out.print("This Field Can't be Null:");
			else
				System.out.print("These Fields Can't be Null:");
			for (String st : invalid) {
				System.out.print(" " + st);
			}
			System.out.println();
		}

		return final_return;

	}

	// checks if database and table exist if true it calls dbms to select
	// entries
	public Tables validate_selectAll(String dataBaseName, String tableName) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				System.out.print("Searching For Match ");
				for(int i = 0 ; i < 5 ; i++){
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.print(".");
				}
				DBMS ob = new DBMSImplementer();
				Tables table = ob.selectAll(dataBaseName, tableName); 
				if(table!=null)	System.out.println("\nDone Searching !!!");
				else System.out.println("\nNo match found !!!");
				return table;
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");

		return null;

	}

	// checks if database and table exist if true it calls dbms to select an
	// entry
	public Tables validate_select(String dataBaseName, String tableName,
			Field entry) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				if (check_type(dataBaseName, tableName, entry.getValue(),
						entry.getName())) {

					if (validate_select_field(dataBaseName, tableName, entry)) {
						System.out.print("Searching For Match ");
						for(int i = 0 ; i < 5 ; i++){
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.print(".");
						}
						DBMS ob = new DBMSImplementer();
						
						Tables table = ob.select(dataBaseName, tableName, entry);
						if(table!=null)System.out.println("\nDone Searching !!!");
						else System.out.println("\nNo match found");
						return table;
					}

				} else {
					System.out
							.println("data type does not math the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");

		return null;
	}

	private boolean validate_select_field(String dataBaseName,
			String tableName, Field entry) {
		XMLParser ob = new XMLParser();
		ArrayList<Col> table = ob.load(dataBaseName, tableName);

		for (Col field : table) {
			if (field.getFieldName().equalsIgnoreCase(entry.getName())) {
				if (field.getIsSearchable()) {
					return true;
				} else {
					System.out.println("This Field -" + field.getFieldName()
							+ "- Can't be searchable!");
					return false;
				}
			}
		}
		System.out.println("This Field -" + entry.getName()
				+ "- doesn't exist!");
		return false;
	}

	// checks if database and table exist if true it calls dbms to update
	// entries
	public int validate_update(String dataBaseName, String tableName,
			ArrayList<Field> entries, Field entry) {

		boolean flag = true;

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				for (Field element : entries) {

					if (!check_type(dataBaseName, tableName,
							element.getValue(), element.getName())) {
						flag = false;
						break;

					}
				}

				if (flag) {
					if (check_type(dataBaseName, tableName, entry.getValue(),
							entry.getName())) {
						if (validate_select_field(dataBaseName, tableName,
								entry)) {
							if (validate_update_field(dataBaseName, tableName,entries)
									&& validate_primary_key(dataBaseName, tableName, entries)) {
								System.out.print("Update Selected Fields");

								for(int i = 0 ; i < 5 ; i++){
									try {
										Thread.sleep(300);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									System.out.print(".");
								}
								
								DBMS ob = new DBMSImplementer();
								
								int rawaffected = ob.update(dataBaseName, tableName, entries,entry);
								if(rawaffected!=0)System.out.println("\nTable Updated !!!");
								else System.out.println("\nNo match found !!!");
								return rawaffected;
							}
						}
					} else {

						System.out.println("conition does not apply!");
					}
				} else {
					System.out
							.println("data type does not match the table's data type");
				}
			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
		return -2;
	}

	private boolean validate_update_field(String dataBaseName,
			String tableName, ArrayList<Field> entries) {
		XMLParser ob = new XMLParser();
		ArrayList<Col> table = ob.load(dataBaseName, tableName);

		for (Field entry : entries) {
			for (Col tb : table) {
				if (entry.getName().equalsIgnoreCase(tb.getFieldName())) {
					if (tb.getAutoIncrement() || tb.getIsReadOnly()) {
						System.out.println("This Field -" + entry.getName()
								+ "-Can't be Updated!");
						return false;
					}
				}
			}
		}
		return true;
	}

	// checks if database and table exist if true it calls dbms to delete
	// entries
	public int validate_delete(String dataBaseName, String tableName,
			Field entry) {

		if (check_dataBase(dataBaseName)) {
			if (check_table(dataBaseName, tableName)) {
				if (check_type(dataBaseName, tableName, entry.getValue(),
						entry.getName())) {
					if (validate_select_field(dataBaseName, tableName, entry)) {
						System.out.print("Deleting The Selected Fields");
						for(int i = 0 ; i < 5 ; i++){
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.print(".");
						}
						System.out.println();
						DBMS ob = new DBMSImplementer();
						int rawaffected = ob.delete(dataBaseName, tableName, entry);
						if(rawaffected!=0)
						System.out.println("Done Deleting !!!");
						
						else System.out.println("\nNo match found");
						return rawaffected;
					}
				} else
					System.out.println("conition does not apply!");

			} else

				System.out.println("table does not exist!");
		} else
			System.out.println("database does not exist!");
		
		return 0;

	}

}