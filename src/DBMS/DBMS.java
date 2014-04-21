package DBMS;

import java.util.ArrayList;
 
 
public interface DBMS {
 
	public void createDateBase ( String name );
	public void createTable ( String dataBaseName , String tableName , ArrayList<Field> entries );
	public int insert ( String dataBaseName , String tableName , ArrayList<Field> entries);
	public Tables selectAll ( String dataBaseName , String tableName );
	public Tables select( String dataBaseName , String tableName , Field entry );
	public int update ( String dataBaseName , String tableName , ArrayList<Field> entries , Field entry );
	public int delete( String dataBaseName , String tableName , Field entry );
 
 
}