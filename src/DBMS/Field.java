package DBMS;
public class Field {
 
	private String name;
	private String value;
	private char relation;
	private boolean isNullAble ,autoIncrement, isSearchAble , isWritableReadable, primary_key ;

 
	public Field() {
 
		this.name = "";
		this.value = "";
		this.relation = '=';
		isWritableReadable = isSearchAble = isNullAble = true;
		primary_key = autoIncrement = false;
	}
 
	public Field(String Name, String Value, char Relation) {
		// TODO Auto-generated constructor stub
		this.name = Name;
		this.value = Value;
		this.relation = Relation;
		isWritableReadable = isSearchAble = isNullAble = true;
		primary_key=autoIncrement = false;
	}
 
	public Field(String Name, String Value) {
		// TODO Auto-generated constructor stub
		this.name = Name;
		this.value = Value;
		this.relation = '=';
	}
	
	public boolean getPrimaryKey(){
		return primary_key;
	}
	public void setPrimaryKey(boolean f){
		primary_key=f;
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
	
	
	public String getName() {
		return name;
	}
 
	public String getValue() {
		return value;
	}
 
	public char getRelation() {
		return relation;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public void setValue(String value) {
		this.value = value;
	}
 
	public void setRelation(char relation) {
		this.relation = relation;
	}
 
}