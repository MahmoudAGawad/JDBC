package JDBC;

import java.io.File;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

public class Driver implements java.sql.Driver {
	@Override
	public boolean acceptsURL(String url) throws SQLException {

		if (new File(url).exists()) {
			
			return true;

		}
		return false;

	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {

		Connection conn = null;
		if (acceptsURL(url)) {

			conn = new Connection(url, info);

		}
		return conn;
	}

	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {

		DriverPropertyInfo[] theInfos = {
				new DriverPropertyInfo("UserName", info.getProperty("UserName")),
				new DriverPropertyInfo("Password", info.getProperty("password")) };

		return theInfos;
	}

	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}