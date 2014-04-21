package JDBC;
 
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import DBMS.*;
 
public class Resultset implements ResultSet {
 
	public Resultset(Tables tb){
		currentTable = tb;
		indexPointer = 0;
		if(tb.getFields().size()==0)numberOfRows=0;
		else
		numberOfRows = tb.getFields().get(0).getLen();
		currentStatementObject = null;
	}
	public Resultset(Tables tb , Statement stat){
		currentTable = tb;
		indexPointer = 0;
		
		if(tb.getFields().size()==0)numberOfRows=0;
		else
		numberOfRows = tb.getFields().get(0).getLen();
		currentStatementObject = stat;
	}
 
 
	private int indexPointer = 0; // before first
	private Tables currentTable = null;
	private int numberOfRows = 0;
	private Statement currentStatementObject = null;
	
	public int get_index_pointer(){
		return indexPointer;
	}
	public Tables get_current_table(){
		return currentTable;
	}
	
	public void print_result() throws SQLException{
		
		
			ArrayList<Col> entry = currentTable.getFields();
			if (entry.get(0).getLen()==0) {
				System.out.println("Empty table");
				return ;
			}
			
			for (int i = 0; i < entry.size(); i++) {
				System.out.print(entry.get(i).getFieldName()+" ");
			}
			System.out.println();
			for (int i = 0; i < entry.get(0).getLen(); i++) {
				
				for (int j = 0; j < entry.size(); j++) {
					System.out.print(entry.get(j).get(i)+" ");
				}
				System.out.println();

			}
		

		
	}
	
	
	
	@Override
	public boolean absolute(int go) throws SQLException {
		
		if (go > numberOfRows) {
			afterLast();
			return false;
		} else if (go < -numberOfRows) {
			beforeFirst();
			return false;
		} else if (go < 0) {
			indexPointer = numberOfRows + go + 1;
		} else {
			indexPointer = go;
		}
 
		return true;
	}
 
	@Override
	public void afterLast() throws SQLException {
		indexPointer = numberOfRows + 1;
 
	}
 
	@Override
	public void beforeFirst() throws SQLException {
		indexPointer = 0;
	}
 
	@Override
	public void close() throws SQLException {
		currentTable = null;
 
	}
 
	@Override
	public int findColumn(String columnLabel) throws SQLException { // LABEL
																	// !!!!!!
		ArrayList<Col> cols = currentTable.getFields();
		int index = 0;
		for (Col field : cols) {
			index++;
			if (field.getFieldName().equalsIgnoreCase(columnLabel)) { // Label
																		// not
																		// name
																		// !!!
				return index;
			}
 
		}
 
		return -1;
	}
 
	@Override
	public boolean first() throws SQLException {
		if (numberOfRows != 0) {
			indexPointer = 1;
			return true;
		}
 
		return false;
	}
 
	@Override
	 public Array getArray(int arg0) throws SQLException { // !!!!!!!!!!!!!!!
	  // TODO Auto-generated method stub
	    ArrayList<Col> fields = currentTable.getFields();

	    if (arg0 > fields.size() || arg0 < 1 || indexPointer == 0
	      || indexPointer == numberOfRows + 1) {
	     return null;
	    }
	    String value = fields.get(arg0 - 1).get(indexPointer - 1);
	    if (value.equalsIgnoreCase("null")) {
	     return null;
	    }
	    String[] res = value.split("||");
	    return new ArrayImplementer(res);
	 }
	
	
	 @Override
	 public boolean getBoolean(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return false;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (value.equals("1")||value.equalsIgnoreCase("true")) {
	   return true;
	  }
	  return false;
	 }
 
	@Override
	public boolean getBoolean(String name) throws SQLException {
 
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					if (cellValue.equalsIgnoreCase("true")
							|| cellValue.equalsIgnoreCase("1")) {
						return true;
					} else
						return false;
				}
 
			}
		}
 
		return false;
	}
 
	 @Override
	 public Date getDate(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return null;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("date")) {
	   int[] date = parseDate(value);
	   return new Date(date[2], date[1], date[0]);
 
	  }
 
 
	  return null;
	 }
 
	@SuppressWarnings("deprecation")
	@Override
	public Date getDate(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("DATE")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					int[] date = parseDate(cellValue);
					return new Date(date[2], date[1], date[0]);
				}
 
			}
		}
 
		return null;
	}
 
	private int[] parseDate(String str) {
		String[] res = str.split("/");
		return new int[] { Integer.parseInt(res[0]), Integer.parseInt(res[1]),
				Integer.parseInt(res[2]) };
	}
 
	 @Override
	 public double getDouble(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return 0;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("double")) {
	   return Double.parseDouble(value);
	  }
 
	  return 0;
	 }
 
	@Override
	public double getDouble(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("DOUBLE")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					return Double.parseDouble(cellValue);
				}
 
			}
		}
 
		return 0.0;
	}
 
	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return Resultset.FETCH_UNKNOWN;
	}
 
	 @Override
	 public float getFloat(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return 0;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("double")) {
	   return Float.parseFloat(value);
	  }
 
	  return 0;
	 }
 
	@Override
	public float getFloat(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("DOUBLE")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					return Float.parseFloat(cellValue);
				}
 
			}
		}
 
		return 0.0f;
	}
 
	 @Override
	 public int getInt(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return 0;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("integer")) {
	   return Integer.parseInt(value);
	  }
 
	  return 0;
	 }
 
	@Override
	public int getInt(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("INTEGER")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					return Integer.getInteger(cellValue);
				}
 
			}
		}
 
		return 0;
	}
 
	 @Override
	 public long getLong(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return 0;
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("integer")) {
	   return Long.parseLong(value);
	  }
	  return 0;
	 }
 
	@Override
	public long getLong(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("INTEGER")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					return Long.getLong(cellValue);
				}
 
			}
		}
 
		return 0;
	}
 
	@Override
	public ResultSetMetadata getMetaData() throws SQLException {
		
		
		return new ResultSetMetadata(currentTable);
	}
 
	@Override
	public Object getObject(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		
		  ArrayList<Col> fields = currentTable.getFields();
		  
		  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
		   return 0;
		  }
		  String value = fields.get(arg0-1).get(indexPointer-1);
		  if (value.equalsIgnoreCase("null")||value.equals("0")||value.equals("0.0")) {
			return null;
		  }
		  return value;
	 
	}
 
	@Override
	public Statement getStatement() throws SQLException {
		// TODO Auto-generated method stub
		return currentStatementObject;
	}
 
	 @Override
	 public String getString(int arg0) throws SQLException {
	  // TODO Auto-generated method stub
	  ArrayList<Col> fields = currentTable.getFields();
 
	  if ( arg0 > fields.size() || arg0 < 1 || indexPointer == 0 || indexPointer == numberOfRows + 1 ) {
	   return "";
	  }
	  String value = fields.get(arg0-1).get(indexPointer-1);
	  if (fields.get(arg0-1).getFieldType().equalsIgnoreCase("string")) {
	   return value;
	  }
	  return "";
 
	 }
 
	@Override
	public String getString(String name) throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			ArrayList<Col> cols = currentTable.getFields();
 
			for (Col field : cols) {
 
				if (field.getFieldType().equalsIgnoreCase("INTEGER")
						&& field.getFieldName().equalsIgnoreCase(name)) {
					String cellValue = field.getElements()
							.get(indexPointer - 1);
					return cellValue;
				}
 
			}
		}
 
		return "";
	}
 
	 @Override
	 public boolean isAfterLast() throws SQLException {
	  return indexPointer > numberOfRows;
	 }
 
	@Override
	public boolean isBeforeFirst() throws SQLException {
 
		return (indexPointer < 1);
	}
 
	@Override
	public boolean isClosed() throws SQLException {
 
		return (currentTable == null);
	}
 
	@Override
	public boolean isFirst() throws SQLException {
 
		return (indexPointer == 1);
	}
 
	 @Override
	 public boolean isLast() throws SQLException {
	  return indexPointer == numberOfRows;
	 }
 
	@Override
	public boolean last() throws SQLException {
		if (indexPointer > 0 && indexPointer <= numberOfRows) {
			indexPointer = numberOfRows;
			return true;
		}
		return false;
	}
 
	 @Override
	 public boolean next() throws SQLException {
	  // TODO Auto-generated method stub
	  if(indexPointer < numberOfRows){
	   indexPointer++;
	   return true;
	  }
	  indexPointer = numberOfRows + 1 ;
	  return false;
	 }
 
	@Override
	public boolean previous() throws SQLException {
 
		if (indexPointer > 1) {
			indexPointer--;
			return true;
		} else {
			indexPointer = 0;
			return false;
		}
	}
 
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------------------
 
	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public Array getArray(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getAsciiStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getAsciiStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public BigDecimal getBigDecimal(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public BigDecimal getBigDecimal(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getBinaryStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getBinaryStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Blob getBlob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Blob getBlob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public byte getByte(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public byte getByte(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public byte[] getBytes(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public byte[] getBytes(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Reader getCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Reader getCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Clob getClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Clob getClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Date getDate(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Date getDate(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Object getObject(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public <T> T getObject(int arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public <T> T getObject(String arg0, Class<T> arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Ref getRef(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Ref getRef(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public short getShort(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public short getShort(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public Time getTime(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Time getTime(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Time getTime(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Time getTime(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Timestamp getTimestamp(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Timestamp getTimestamp(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public URL getURL(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public URL getURL(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getUnicodeStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public InputStream getUnicodeStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public boolean relative(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateArray(int arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateArray(String arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBigDecimal(String arg0, BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(int arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(String arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBlob(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBoolean(String arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateByte(int arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateByte(String arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateBytes(String arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(String arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(int arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(String arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateDate(int arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateDate(String arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateDouble(int arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateDouble(String arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateFloat(int arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateFloat(String arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateInt(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateInt(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateLong(int arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateLong(String arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNull(int arg0) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateNull(String arg0) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateObject(int arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateObject(String arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateObject(int arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateObject(String arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateRef(int arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateRef(String arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateShort(int arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateShort(String arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateTime(int arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateTime(String arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public void updateTimestamp(String arg0, Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub
 
	}
 
	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
}