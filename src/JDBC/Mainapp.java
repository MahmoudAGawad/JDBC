package JDBC;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import java.io.*;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import DBMS.XMLParser;

public class Mainapp {

	/**
	 * @param args
	 */
	final static String URL = System.getenv().get("TEMP") + "/JDBC";
	public static Thread main_thread;
	public static Timer timer;
	public static Statement stat;
	public static String sql = "", user_name = "root", password = "password";
	public static Resultset rs;
	public static int x;
	public static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(Mainapp.class);

	public static void main(String[] args) throws IOException,
			InterruptedException, SQLException {
		// TODO Auto-generated method stub

		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		Mainapp ob = new Mainapp();
		ob.CreateRoot();

		while (true) {
			System.out.print("enter your user name : ");
			String first = k.readLine();
			System.out.print("\nenter your password : ");
			String second = k.readLine();
			if (ob.check_prop(first, second)) {
				break;
			}
			System.out.println("\nWrong password or user name , try again");
		}

		System.out
				.println("*************************************************************");

		Connection conn = null;
		// Statement stat = null;

		Driver driver = new Driver();

		System.out.println("Starting The Application");
		System.out
				.println("*************************************************************");
		System.out.println("Wait a Second Until Connecting");
		System.out.print("Connecting To DataBase ");
		for (int i = 0; i < 5; i++) {
			Thread.sleep(300);
			System.out.print(".");
		}
		try {

			logger.info("connected to database");

			long currentTime = System.currentTimeMillis();

			conn = driver.connect(URL, null);
			System.out.println("\nConnected Successfully !!!");

			long currentTime2 = System.currentTimeMillis();
			long difference = currentTime2 - currentTime;
			logger.info("operation timestamp " + difference);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e + "");
			e.printStackTrace();

		}

		try {

			stat = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out
				.println("*************************************************************");

		stat.setQueryTimeout(2);
		while (true) {

			sql = k.readLine();

			String temp = sql.trim().toLowerCase().replace("*", " * ");
			if (temp.startsWith("use") || temp.startsWith("create")) {

				try {
					String result = "connected to database";
					if (temp.startsWith("use"))
						result += "... using database";
					else
						result += "...creating database";
					logger.info(result);

					long currentTime = System.currentTimeMillis();

					timer = new Timer();
					main_thread = new Thread() {

						public void run() {
							try {
								stat.execute(sql);
								timer.cancel();

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					};
					main_thread.start();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (main_thread.isAlive()) {
								main_thread.stop();
								System.out
										.println("\nthis method exceded the time limit..Sorry !!");

							}
							timer.cancel();

						}
					}, stat.getQueryTimeout() * 1000);

					while (main_thread.isAlive()) {

					}

					long currentTime2 = System.currentTimeMillis();

					long difference = (currentTime2 - currentTime);

					logger.info("operation timestamp " + difference);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error(e + "");

					e.printStackTrace();

				}

			}

			else if (temp.startsWith("select")) {
				try {

					String result = "connected to database..selecting from table";

					logger.info(result);
					long currentTime = System.currentTimeMillis();

					main_thread = new Thread() {

						public void run() {
							try {
								rs = stat.executeQuery(sql);
								timer.cancel();

							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					};

					timer = new Timer();
					main_thread.start();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (main_thread.isAlive()) {
								main_thread.stop();
								System.out
										.println("\nthis method exceded the time limit..Sorry !!");

							}
							timer.cancel();

						}
					}, stat.getQueryTimeout() * 1000);

					while (main_thread.isAlive()) {

					}

					long currentTime2 = System.currentTimeMillis();
					long difference = (currentTime2 - currentTime) - 1500;
					logger.info("operation timestamp " + difference);
					if (rs != null) {
						rs.print_result();
						rs.close();

					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e + "");

				}

			} else if (temp.startsWith("update") || temp.startsWith("insert")
					|| temp.startsWith("delete")) {

				try {
					String result = "connected to database";
					if (temp.startsWith("update"))
						result += "...updating";
					else if (temp.startsWith("insert"))
						result += "...inserting";
					else
						result += "delete";
					logger.info(result);
					long currentTime = System.currentTimeMillis();

					main_thread = new Thread() {

						public void run() {
							try {
								x = stat.executeUpdate(sql);
								timer.cancel();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					};

					timer = new Timer();
					main_thread.start();

					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (main_thread.isAlive()) {
								main_thread.stop();
								System.out
										.println("\nthis method exceded the time limit..Sorry !!");

							}
							timer.cancel();
						}
					}, stat.getQueryTimeout() * 1000);

					while (main_thread.isAlive()) {

					}

					long currentTime2 = System.currentTimeMillis();
					long difference = (currentTime2 - currentTime) - 1500;
					logger.info("operation timestamp " + difference);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e + "");

				}
				if (x >= 0)
					System.out.println("affected raws " + x);
			} else if (temp.equalsIgnoreCase("exit")) {
				break;
			} else {
				System.out.println("Unknown Order");
			}

		}

		try {

			logger.info("connected to database");
			long currentTime = System.currentTimeMillis();

			stat.close();

			long currentTime2 = System.currentTimeMillis();
			long difference = (currentTime2 - currentTime);
			logger.info("operation timestamp " + difference);

		} catch (SQLException e) {
			// TODO Auto-generated catch block

			logger.error(e + "");

			e.printStackTrace();

		}
		try {

			logger.info("connected to database");
			long currentTime = System.currentTimeMillis();

			conn.close();
			stat.close();
			long currentTime2 = System.currentTimeMillis();
			long difference = (currentTime2 - currentTime);
			logger.info("operation timestamp " + difference);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e + "");

		}

	}

	private boolean check_prop(String first, String second) {
		Properties prop = new Properties();

		try {
			// load a properties file
			prop.load(new FileInputStream(URL + "/config.properties"));

			// get the property value and print it out
			user_name = prop.getProperty("user_name");
			password = prop.getProperty("password");
			return (user_name.equals(first) && password.equals(second));

		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("asdas");
		}
		return false;
	}

	private void CreateRoot() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		String temp = URL.substring(0, URL.lastIndexOf("/"));
		File a = new File(URL);
		if (!a.exists()) {
			a.mkdir();
		}
		File c = new File(URL + "/config.properties");
		if (!c.exists()) {

			Properties prop = new Properties();

			try {
				// set the properties value
				prop.setProperty("user_name", "root");
				prop.setProperty("password", "password");

				// save properties to project root folder
				prop.store(new FileOutputStream(URL + "/config.properties"),
						null);

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		File b = new File(XMLParser.path + "/root.xml");
		if (b.exists())
			return;
		try {
			docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("databases");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", "root");

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(b);

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		} catch (TransformerConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Root Not Created");
		}
	}

}