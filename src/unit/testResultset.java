package unit.Test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import DBMS.Col;
import DBMS.Tables;
import JDBC.Resultset;

public class testResultset {
	public static Tables tb = new Tables();
	
	@Before
	public void intialize(){
		tb=new Tables();

		ArrayList<Col>array = new ArrayList<>();

		ArrayList<String>data = new ArrayList<>();
		ArrayList<String>data1 = new ArrayList<>();

		data.add("mostafa");
		data.add("hamdy");
		data1.add("ahmed");
		data1.add("ali");
		array.add(new Col("type", "String", data));
		array.add(new Col("name", "string", data1));
		tb.setFields(array);
		
		
	}
	
	@Test
	public void testAbsolute() throws SQLException {
		
		Resultset re = new Resultset(tb);
		re.absolute(30);
		assertEquals(re.get_index_pointer() , 3);
		re.absolute(2);
		assertEquals(re.get_index_pointer() , 2);
		re.absolute(1);
		assertEquals(re.get_index_pointer() , 1);
		re.absolute(-10);
		assertEquals(re.get_index_pointer(), 0);
		re.absolute(-1);
		assertEquals(re.get_index_pointer(), 2);

		
		

	}

	@Test
	public void testAfterLast() throws SQLException {
		
		Resultset re = new Resultset(tb);
		re.afterLast();
		assertEquals(3, re.get_index_pointer());
	}

	@Test
	public void testBeforeFirst() throws SQLException {
		
		Resultset re = new Resultset(tb);
		re.beforeFirst();
		assertEquals(0, re.get_index_pointer());
		
	}

	@Test
	public void testClose() throws SQLException {
		Resultset re = new Resultset(tb);
		re.close();
		assertEquals(null, re.get_current_table());
	}

	@Test
	public void testFindColumn() throws SQLException {
		Resultset re = new Resultset(tb);
		assertEquals(re.findColumn("type"), 1);

		assertEquals(re.findColumn("name"), 2);
		
		assertEquals(re.findColumn("ew"), -1);
	}

	@Test
	public void testFirst() throws SQLException {
		Resultset re = new Resultset(tb);
		re.first();
		Resultset re2 = new Resultset(new Tables());
		assertEquals(re.get_index_pointer(), 1);
		assertEquals(re2.first(), false);
	}

//	@Test
//	public void testGetArrayInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBooleanInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBooleanString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDateInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDateString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDoubleInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDoubleString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFetchDirection() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFloatInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFloatString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetIntInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetIntString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetLongInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetLongString() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetMetaData() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObjectInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetStatement() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetStringInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetStringString() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testIsAfterLast() throws SQLException {
		Resultset re = new Resultset(tb);
		assertEquals(re.isAfterLast(), false);

		re.afterLast();
		assertEquals(re.isAfterLast(), true);
	}
//
	@Test
	public void testIsBeforeFirst() throws SQLException {
		Resultset re = new Resultset(tb);
		re.absolute(1);
		assertEquals(re.isBeforeFirst(), false);

		re.beforeFirst();
		assertEquals(re.isBeforeFirst(), true);
	}

	@Test
	public void testIsClosed() throws SQLException {
		Resultset re = new Resultset(tb);
		assertEquals(re.isClosed(), false);

		re.close();
		assertEquals(re.isClosed(), true);
	}

	@Test
	public void testIsFirst() throws SQLException {
		Resultset re = new Resultset(tb);
		
		assertEquals(re.isFirst(), false);
		re.absolute(1);
		assertEquals(re.isFirst(), true);
		
	}

	@Test
	public void testIsLast() throws SQLException {
		Resultset re = new Resultset(tb);
		assertEquals(re.isLast(), false);
		re.absolute(2);
		assertEquals(re.isLast(), true);


	}

	@Test
	public void testLast() throws SQLException {
		Resultset re = new Resultset(tb);
		assertEquals(re.last(), false);
		re.absolute(1);
		assertEquals(re.last(), true);
		assertEquals(re.get_index_pointer(), 2);
	}

	@Test
	public void testNext() throws SQLException {
		Resultset re = new Resultset(tb);
		
		assertEquals(re.next(), true);
		assertEquals(re.get_index_pointer(), 1);
		
		assertEquals(re.next(), true);
		assertEquals(re.get_index_pointer(), 2);

		assertEquals(re.next(), false);
		assertEquals(re.isAfterLast(), true);

	}

	@Test
	public void testPrevious() throws SQLException {
		Resultset re = new Resultset(tb);
		re.absolute(3);
		assertEquals(re.previous(), true);
		assertEquals(re.get_index_pointer(), 2);
		assertEquals(re.previous(), true);
		assertEquals(re.get_index_pointer(), 1);
		assertEquals(re.previous(), false);
		assertEquals(re.isBeforeFirst(), true);

		
		
	}

}
