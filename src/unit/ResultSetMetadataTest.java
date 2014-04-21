package unit.Test;
 
import static org.junit.Assert.*;
 
import java.util.ArrayList;
 
import org.junit.*;
 
import DBMS.*;
import JDBC.ResultSetMetadata;
 
public class ResultSetMetadataTest {
 
	@BeforeClass
	public static void start_test(){
		System.out.println("Testing ResultSetMetadata.class ...");
	}
 
	public static ArrayList<Col> columns = null;
	public static String currentTable = "";
	public static ResultSetMetadata test=null;
 
	@Before
	public void do_before_every_test(){
 
		columns = new ArrayList<>();
		currentTable = "students";
 
 
		ArrayList<String> e = new ArrayList<>();
		e.add("1");e.add("2");e.add("3");e.add("4");
		Col col = new Col("id", "integer", e);
		col.setWritable(false);
		col.setAutoIncrement(true);
		columns.add(col);
 
		e = new ArrayList<>();
		e.add("Waleed");e.add("Abdallah");e.add("Mostafa");e.add("Diaa");
		col = new Col("name", "varchar", e);
		columns.add(col);
 
		e = new ArrayList<>();
		e.add("2/1/1993");e.add("5/3/1993");e.add("6/7/1992");e.add("9/8/1992");
		col = new Col("Date Of Birth", "Date", e);
		col.setNullAble(true);
		columns.add(col);
 
		Tables ob = new Tables(columns);
		ob.setTableName(currentTable);
		test = new ResultSetMetadata(ob);
 
	}
 
	@Test
	public void testGetColumnName() throws Exception {
 
		assertEquals(test.getColumnName(3), "Date Of Birth");
	}
 
	@Test
	public void testGetColumnNam2() throws Exception {
 
		assertEquals(test.getColumnName(6), null);
	}
 
 
	@Test
	public void testGetColumnTypeName() throws Exception {
		assertEquals(test.getColumnTypeName(2), "varchar");
	}
 
	@Test
	public void testIsAutoIncrement() throws Exception {
		assertEquals(test.isAutoIncrement(1), true);
	}
 
	@Test
	public void testGetColumnLabel()throws Exception {
		assertEquals(test.getColumnLabel(10), null);
	}
 
	@Test
	public void testIsWritable() throws Exception {
		assertEquals(test.isWritable(1), false);
	}
 
	@Test
	public void testIsNullable() throws Exception {
		assertEquals(test.isNullable(2), ResultSetMetadata.columnNoNulls);
	}
 
	@Test
	public void testIsReadOnly() throws Exception {
		assertEquals(test.isReadOnly(1), true);
	}
 
	@Test
	public void testIsSearchable() throws Exception {
		assertEquals(test.isSearchable(2), true);
	}
 
	@Test
	public void testGetColumnCount() throws Exception {
		assertEquals(test.getColumnCount(), 3);
	}
 
	@AfterClass
	public static void finish(){
		System.out.println("Finish :)");
	}
 
}