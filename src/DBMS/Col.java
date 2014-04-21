package DBMS;
 
import java.util.ArrayList;
 
public class Col {
 
	private String fieldName;
	private String fieldType;
	private ArrayList<String> elements;
	private int auto_increment = 0;
	private boolean isNullAble, autoIncrement, isSearchAble,
			isWritableReadable, primary_key;
 
	public Col() {
		// TODO Auto-generated constructor stub
		fieldName = "";
		fieldType = "";
		elements = new ArrayList<>();
		isWritableReadable = isSearchAble = isNullAble = true;
		primary_key = autoIncrement = false;
	}
 
	public Col(String name, String Type) {
		fieldName = name;
		fieldType = Type;
		elements = new ArrayList<>();
 
		isWritableReadable = isSearchAble = isNullAble = true;
		primary_key = autoIncrement = false;
 
	}
 
	public Col(String name, String Type, ArrayList<String> array) {
		fieldName = name;
		fieldType = Type;
		elements = array;
		isWritableReadable = isSearchAble = isNullAble = true;
		primary_key = autoIncrement = false;
 
	}
 
	public int get_auto_increment(){
		return auto_increment;
	}
	public void set_auto_increment(int x){
		auto_increment = x;
	}
	public boolean getPrimaryKey() {
		return primary_key;
	}
 
	public void setPrimaryKey(boolean f) {
		primary_key = f;
	}
 
	public boolean getAutoIncrement() {
		return autoIncrement;
	}
 
	public String getFieldLabel() {
		return null;
	}
 
	public boolean getIsWritable() {
		return isWritableReadable;
	}
 
	public boolean getIsNullable() {
		return isNullAble;
	}
 
	public boolean getIsReadOnly() {
		return !isWritableReadable;
	}
 
	public boolean getIsSearchable() {
		return isSearchAble;
	}
 
	public void setNullAble(boolean isNullAble) {
		this.isNullAble = isNullAble;
	}
 
	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
 
	public void setSearchAble(boolean isSearchAble) {
		this.isSearchAble = isSearchAble;
	}
 
	public void setWritable(boolean isWritable) {
		this.isWritableReadable = isWritable;
	}
 
	public void setReadOnly(boolean isReadOnly) {
		this.isWritableReadable = !isReadOnly;
	}
 
	public String getFieldName() {
		return fieldName;
	}
 
	public String getFieldType() {
		return fieldType;
	}
 
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
 
	public ArrayList<String> getElements() {
		return elements;
	}
 
	public void setElements(ArrayList<String> elements) {
		this.elements = elements;
	}
 
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
 
	public String get(int index) {
		return elements.get(index);
 
	}
 
	public void setElement(int index, String val) {
		elements.set(index, val);
	}
 
	public void addElem(String element) {
		elements.add(element);
	}
 
	public void addElemAt(String element, int index) {
		elements.add(index, element);
	}
 
	public int getLen() {
		return elements.size();
	}
 
	public void removeIndex(int index) {
		elements.remove(index);
	}
 
	public void addElemByDefault() {
		if (autoIncrement) {
			auto_increment++;
			elements.add(auto_increment+"");
			return ;
 
		}
		if (fieldType.equalsIgnoreCase("varchar")||fieldType.equalsIgnoreCase("array")||fieldType.equalsIgnoreCase("date")
				||fieldType.equalsIgnoreCase("string")) {
			elements.add("Null");
		} else if (fieldType.toLowerCase().contains("int")) {
			elements.add("0");
		} else {
			elements.add("0.0");
		}
	}
}