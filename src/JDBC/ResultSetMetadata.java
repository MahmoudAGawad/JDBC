package JDBC;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import DBMS.*;
 
public class ResultSetMetadata implements ResultSetMetaData {
	private ArrayList<Col> columns = null;
	private String currentTable = "";
 
	public ResultSetMetadata(Tables tb) {
		currentTable = tb.getTableName();
		columns = tb.getFields();
	}
 
	@Override
	public String getColumnName(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getFieldName();
		} else {
			return null;
		}
	}
 
	@Override
	public String getColumnTypeName(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getFieldType();
		} else {
			return null;
		}
	}
 
	@Override
	public boolean isAutoIncrement(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getAutoIncrement();
		} else {
			return false;
		}
	}
 
	@Override
	public String getColumnLabel(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getFieldLabel();
 
		} else {
			return null;
		}
	}
 
	@Override
	public boolean isWritable(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getIsWritable();
		} else {
			return false;
		}
	}
 
	@Override
	public int isNullable(int i) {
		if (check(i)) {
			if (columns.get(i - 1).getIsNullable()) {
				return columnNoNulls;
			}
			return columnNullable;
		} else {
			return columnNullableUnknown;
		}
	}
 
	@Override
	public boolean isReadOnly(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getIsReadOnly();
		} else {
			return false;
		}
	}
 
	@Override
	public boolean isSearchable(int i) throws SQLException {
		if (check(i)) {
			return columns.get(i - 1).getIsSearchable();
		} else {
			return false;
		}
	}
 
	@Override
	public int getColumnCount() throws SQLException {
		if (columns.size() > 0) {
			return columns.size();
		} else {
			return 0;
		}
	}
 
	@Override
	public String getTableName(int column) throws SQLException {
		SchemaParser ob = new SchemaParser();
		// private ArrayList<Tables> tables = ob.loadDataBase(databaseName);
		return null;
 
	}
 
	private boolean check(int i) {
		if (i <= columns.size() && i > 0) {
			return true;
		} else {
			return false;
		}
	}
 
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////////////////////////
 
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public String getCatalogName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public String getColumnClassName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public int getColumnType(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public int getPrecision(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public int getScale(int column) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
 
	@Override
	public String getSchemaName(int column) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean isCurrency(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
	@Override
	public boolean isSigned(int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
 
}
 
