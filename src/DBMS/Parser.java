package DBMS;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

	public static void main(String[] args) {
		String table = "use yy";
		Parser x = new Parser();
		x.parseStatment(table);

	}

	private ArrayList<Field> entries = new ArrayList<>();
	private Field entry;
	private String dataBaseName = "", tableName = "";
	private boolean all = false;
	private int change_counter = 0;
	private Tables table = null;
	private ArrayList<String> valid_types;

	public Parser() {
		valid_types = new ArrayList<String>();
		valid_types.add("string");
		valid_types.add("varchar");
		valid_types.add("tinyint");
		valid_types.add("smallint");
		valid_types.add("integer");
		valid_types.add("bigint");
		valid_types.add("bit");
		valid_types.add("array");
		valid_types.add("date");
		valid_types.add("double");
		valid_types.add("float");
		valid_types.add("real");
		valid_types.add("decimal");

	}

	public int get_change() {
		return change_counter;
	}

	public Tables get_table() {
		return table;
	}

	public void parseStatment(String stat) {
		change_counter=0;
		table=null;
		PrintStream stdout = System.out;
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out))); // System.setOut(System.console());
		all = false;
		entry = null;
		entries.clear();
		// dataBaseName="";
		tableName = "";
		Validator validate = new Validator();

		StringTokenizer str = new StringTokenizer(stat);
		String first = str.nextToken();
		if (first.equalsIgnoreCase("use")) {
			if(!str.hasMoreTokens()){
				System.out.println("Unknown order ");
				return ;
			}
			String temp="";
			while(str.hasMoreTokens()){
				if(!temp.equals(""))temp+="_";
				 temp += str.nextToken().replace(";", "");

			}
			System.out.println(temp);
			if (validate.check_dataBase(temp)) {
				dataBaseName = temp;
				System.out.println("You are now using this "+dataBaseName+" .");
				return;

			}
			System.out.println("DataBase doesn't exist !!!");
			return;
		}
		if (createDatabaseStatment(stat)) {
			// System.out.println(dataBaseName);
			// Validator validate = new Validator();
		
			validate.validate_createDateBase(dataBaseName);
			
			
		} else if (createTableStatment(stat)) {
//			System.out.println(dataBaseName + " " + tableName);
//			for (int i = 0; i < entries.size(); i++) {
//				System.out.println(entries.get(i).getName() + " "
//						+ entries.get(i).getValue());
//				System.out.println("auto icrement : "
//						+ entries.get(i).getAutoIncrement());
//				System.out.println("null : " + entries.get(i).getIsNullable());
//
//				System.out.println("searchable : "
//						+ entries.get(i).getIsSearchable());
//				System.out.println("writable : "
//						+ entries.get(i).getIsWritable());
//				System.out
//						.println("--------------------------------------------");
//
//			}
			validate.validate_createTable(dataBaseName, tableName, entries);
			

		} else if (selectStatment(stat)) {
			if (all) {
				// System.out.println(tableName+" all");
				table = validate.validate_selectAll(dataBaseName, tableName);
			} else {
				// System.out.println(tableName+" where "+entry.getName()+" "+entry.getRelation()+" "+entry.getValue());
				table = validate
						.validate_select(dataBaseName, tableName, entry);
			}

		} else if (deleteStatment(stat)) {
			// System.out.println(tableName+" where "+entry.getName()+" "+entry.getRelation()+" "+entry.getValue());
			change_counter = validate.validate_delete(dataBaseName, tableName,
					entry);
		} else if (insertStatment(stat)) {
//			System.out.println(tableName);
//			for (int i = 0; i < entries.size(); i++) {
//				System.out.println(entries.get(i).getName() + " "
//						+ entries.get(i).getValue());
//			}
			// System.out.println("mostsss");
			change_counter = validate.validate_insert(dataBaseName, tableName, entries);

		} else if (updateStatment(stat)) {
			// System.out.println(tableName+" where "+entry.getName()+" "+entry.getRelation()+" "+entry.getValue());
			// for (int i = 0; i < entries.size(); i++) {
			// System.out.println(entries.get(i).getName()+" "+entries.get(i).getValue());
			// }
			change_counter = validate.validate_update(dataBaseName, tableName,
					entries, entry);
		} else {
			System.out.println("unknown order");
		}
		return;
	}

	public boolean selectStatment(String stat) {
		stat=stat.replace("*", " * ");
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;
		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("select")) {

			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equals("*")) {
				if (!str.hasMoreTokens())
					return false;

				String thirdWord = str.nextToken();
				if (thirdWord.equalsIgnoreCase("from")) {
					if (!str.hasMoreTokens())
						return false;

					tableName = str.nextToken().replace(";", "");

					if (str.hasMoreTokens()) {
						String forthWord = str.nextToken();
						if (forthWord.equalsIgnoreCase("where")) {
							// parameter
							String line = "";
							while (str.hasMoreTokens()) {
								line += str.nextToken();
							}
							line = line.replaceAll("'", "")
									.replaceAll("\"", "").replace(";", "");
							boolean check = getvar(line);

							// String fieldName = str.nextToken();
							// String relation = str.nextToken();
							// String value =str.nextToken().replaceAll("'",
							// "").replaceAll("\"", "");
							// entry = new
							// Field(fieldName,value,relation.charAt(o));
							return check;
						} else if (forthWord.equalsIgnoreCase(";")) {
							// selectAll
							all = true;
							return true;
						}

					} else {
						// selectAll
						all = true;
						return true;
					}

				}

			}

		}
		return false;
	}

	public boolean createDatabaseStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("create")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("database")) {
				if (!str.hasMoreTokens())
					return false;

				dataBaseName = str.nextToken().replace(";", "");
				return true;

			}
		}
		return false;

	}

	public boolean createTableStatment(String stat) {
		entries.clear();
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("create")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("table")) {
				String line = "";
				while (str.hasMoreTokens()) {
					line += (str.nextToken() + " ");
				}

				line = line.replace("(", " ( ").replace(")", " ) ")
						.replace(",", " , ");

				str = new StringTokenizer(line);
				if (!str.hasMoreTokens())
					return false;
				tableName = str.nextToken();
				if (tableName.equals("") || tableName.equals("("))
					return false;

				String name = "", type = "";
				boolean isNullAble = true, autoIncrement = false, isSearchAble = true, isWritableReadable = true, not = false;
				int checkok = 1;
				while (str.hasMoreTokens()) {
					String word = str.nextToken();
					word = word.toLowerCase();
					if (word.equals("(") || word.equals(";")) {
						continue;
					}

					if (name.equals("")) {
						name = word;
					} else if (type.equals("")) {
						type = word;
						if (name.equalsIgnoreCase("primarykey")) {
							boolean check = true;
							for (Field entry : entries) {
								if (entry.getName().equalsIgnoreCase(type)) {
									entry.setPrimaryKey(true);
									check = false;
									break;
								}
							}
							if (check) {
								System.out.print("This field -" + type
										+ "- doesn't exist! , ");
								return false;
							}
							continue;
						}

						if (!valid_types.contains(type.toLowerCase())) {

							return false;

						}
					} else if (word.equals(",") || word.equals(")")) {
						if (name.equals("") && type.equals(""))
							return false;

						if (not && checkok == 1)
							return false;

						if (!name.equalsIgnoreCase("primarykey")) {
							Field temp = new Field(name, type);
							temp.setAutoIncrement(autoIncrement);
							temp.setNullAble(isNullAble);
							temp.setSearchAble(isSearchAble);
							temp.setWritable(isWritableReadable);
							entries.add(temp);
						}
						type = "";
						name = "";
						not=autoIncrement = false;
						isNullAble = true;
						isSearchAble = true;
						isWritableReadable = true;
						checkok = 1;
						

					} else if (word.equalsIgnoreCase("not")) {
						if (not)
							return false;
						not = true;
					} else if (word.contains("not")) {
						String var = word.substring(3, word.length());
						if (var.equals("null")) {
							isNullAble = false;
							checkok = -1;
						} else if (var.equalsIgnoreCase("autoincrement")) {
							autoIncrement = false;
							checkok = -1;

						} else if (var.equalsIgnoreCase("SearchAble")) {
							isSearchAble = false;
							checkok = -1;

						} else if (var.equalsIgnoreCase("Writable")) {
							isWritableReadable = false;
							checkok = -1;

						} else if (var.equalsIgnoreCase("ReadOnly")) {
							isWritableReadable = true;
							checkok = -1;

						} else {
							return false;
						}
					} else {
						String var = word.toLowerCase();
						if (var.equalsIgnoreCase("null")) {
							if (!not) {

								isNullAble = true;
							} else {
								checkok = -1;

								isNullAble = false;
							}

						} else if (var.equalsIgnoreCase("autoincrement")) {
							if (!not)
								autoIncrement = true;

							else {
								checkok = -1;

								autoIncrement = false;
							}
						} else if (var.equalsIgnoreCase("SearchAble")) {

							if (!not)
								isSearchAble = true;

							else {
								checkok = -1;

								isSearchAble = false;
							}
						} else if (var.equalsIgnoreCase("Writable")) {

							if (!not)
								isWritableReadable = true;
							else {
								checkok = -1;

								isWritableReadable = false;
							}
						} else if (var.equalsIgnoreCase("ReadOnly")) {
							if (!not)
								isWritableReadable = false;
							else {
								checkok = -1;

								isWritableReadable = true;
							}
						} else {
							return false;
						}

					}
				}

				if (entries.size() == 0)
					return false;
				return true;
			}
		}
		return false;

	}

	public boolean deleteStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();
		if (firstWord.equalsIgnoreCase("delete")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("from")) {
				if (!str.hasMoreTokens())
					return false;

				tableName = str.nextToken();
				if (!str.hasMoreTokens())
					return false;

				String forthWord = str.nextToken();
				// //////////////////////////////////////////////////
				if (forthWord.equalsIgnoreCase("where")) {
					// parameter
					String line = "";
					while (str.hasMoreTokens()) {
						line += str.nextToken();
					}
					line = line.replaceAll("'", "").replaceAll("\"", "")
							.replace(";", "");
					boolean check = getvar(line);

					// String fieldName = str.nextToken();
					// String relation = str.nextToken();
					// String value = str.nextToken().replaceAll("'",
					// "").replaceAll("\"", "");
					// entry = new Field(fieldName, value, relation.charAt(o));
					return check;
				}

			}

		}
		return false;

	}

	public boolean insertStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("insert")) {
			if (!str.hasMoreTokens())
				return false;

			String secondWord = str.nextToken();
			if (secondWord.equalsIgnoreCase("into")) {
				boolean check = false, choose = false;
				ArrayList<String> fieldname = new ArrayList<>();
				ArrayList<String> value = new ArrayList<>();
				String line = "";
				while (str.hasMoreTokens()) {
					line += str.nextToken();
				}
				line = line.replace(";", "");
				String word = "";
				for (int i = 0; i < line.length(); i++) {
					if (line.charAt(i) == '(') {
						if (i == 0)
							return false;
						if (!check) {
							tableName = word;
							// System.out.println(tableName+"///////"+word);
							// tableName="tb";
							// System.out.println(word);
						}

						check = true;
						word = "";
					} else if (line.charAt(i) == ')') {
						word = word.replace(" ", "").replace("[", "[,")
								.replace("]", ",]");
						StringTokenizer strr = new StringTokenizer(word, ",");

						while (strr.hasMoreTokens() && !choose) {
							fieldname.add(strr.nextToken());
						}
						boolean para = false;
						String temp = "";
						while (strr.hasMoreTokens() && choose) {
							String exp = strr.nextToken().replace("'", "")
									.replace("\"", "");
							if (exp.equals("[") && para) {
								return false;
							}
							if (exp.equals("]") && !para) {
								return false;
							}
							if (para) {
								if (exp.equals("]")) {
									temp += (exp);
									para = false;
									// temp+="]";
									exp = temp;
									exp = exp.replace(",]", "]");
									temp = "";
								} else
									temp += exp + ",";
							}
							if (exp.equals("[")) {
								para = true;
								temp = "[";
							}
							// if (exp.equals("]")) {
							//
							// //value.add(temp);
							// }
							if (!para)
								value.add(exp);
						}
						choose = true;

					} else
						word += line.charAt(i);
				}
				// tableName = str.nextToken().replace("(", "");
				//
				// while (str.hasMoreTokens()) {
				// String word = str.nextToken().replace("'", "").replace("\"",
				// "");
				// if
				// (word.equals(",")||word.equals("(")||word.equals(")")||word.equals(";"))
				// {
				// continue;
				// }
				// else
				// if(word.equalsIgnoreCase("values")||word.equalsIgnoreCase("value"))
				// {
				// check = ! check;
				// }
				// else if(check) {
				// value.add(word.replace("(", "").replace(")", ""));
				// }
				// else {
				// fieldname.add(word.replace("(", "").replace(")",
				// "").replace(";", ""));
				// }
				// }
				if (fieldname.size() == value.size()) {
					entries.clear();
					for (int i = 0; i < fieldname.size(); i++) {
						entries.add(new Field(fieldname.get(i), value.get(i)));
					}
					return true;
				}
				// parameter

			}

		}

		return false;

	}

	public boolean updateStatment(String stat) {
		StringTokenizer str = new StringTokenizer(stat);
		if (!str.hasMoreTokens())
			return false;

		String firstWord = str.nextToken();

		if (firstWord.equalsIgnoreCase("update")) {
			if (!str.hasMoreTokens())
				return false;

			tableName = str.nextToken();
			if (!str.hasMoreTokens())
				return false;

			String thirdWord = str.nextToken();

			if (thirdWord.equalsIgnoreCase("set")) {
				// parameter
				// String fieldName = "", value = "";
				entries.clear();
				String line = "";
				while (str.hasMoreTokens()) {
					String word = str.nextToken();

					if (word.equalsIgnoreCase("where")) {
						break;
					}
					line += word;

				}
				line = line.replaceAll("'", "").replaceAll("\"", "")
						.replace(";", "");
				boolean check = getvar2(line);
				if (!check) {
					return false;
				}
				line = "";
				while (str.hasMoreTokens()) {
					line += str.nextToken();
				}
				line = line.replaceAll("'", "").replaceAll("\"", "")
						.replace(";", "");
				check = getvar(line);
				// fieldName=str.nextToken();
				// String relation = str.nextToken();
				// value = str.nextToken().replaceAll("'", "").replaceAll("\"",
				// "");
				// entry=new Field(fieldName,value,relation.charAt(o));
				return check;

			}

		}

		return false;

	}

	public boolean getvar(String stat) {
		String first = "", second = "", relation = "";
		boolean check = false;
		for (int i = 0; i < stat.length(); i++) {
			if (stat.charAt(i) == '=' || stat.charAt(i) == '>'
					|| stat.charAt(i) == '<') {
				check = true;
				relation += stat.charAt(i);
				continue;
			}
			if (check) {
				second += stat.charAt(i);
			} else {
				first += stat.charAt(i);

			}
		}
		if (first.equals("") || second.equals("") || relation.equals(""))
			return false;

		entry = new Field(first, second, relation.charAt(0));
		return true;
	}

	public boolean getvar2(String stat) {
		StringTokenizer str = new StringTokenizer(stat, ",");
		while (str.hasMoreTokens()) {
			String line = str.nextToken();
			String first = "", second = "", relation = "";
			boolean check = false;
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == '=' || line.charAt(i) == '>'
						|| line.charAt(i) == '<') {
					check = true;
					relation += line.charAt(i);
					continue;
				}
				if (check) {
					second += line.charAt(i);
				} else {
					first += line.charAt(i);

				}
			}

			if (!check) {
				return false;
			}
			entries.add(new Field(first, second, relation.charAt(0)));

		}

		return true;

	}
}